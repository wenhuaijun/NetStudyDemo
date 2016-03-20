package com.wenhuaijun.netstudydemo.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wenhuaijun.netstudydemo.R;
import com.wenhuaijun.netstudydemo.config.API;
import com.wenhuaijun.netstudydemo.model.ModelCallback;
import com.wenhuaijun.netstudydemo.model.NetRequest;
import com.wenhuaijun.netstudydemo.model.QuestionModel;
import com.wenhuaijun.netstudydemo.model.bean.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private Button getBtn;
    private Button downloadBtn;
    private Button postBtn;
    private TextView tv;
    private ImageView downloadImag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();

    }

    private void setListener() {
        getBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getBtn = (Button)findViewById(R.id.get_btn);
        postBtn = (Button)findViewById(R.id.post_btn);
        tv =(TextView)findViewById(R.id.html_tv);
        downloadBtn =(Button)findViewById(R.id.downloadImg_btn);
        downloadImag = (ImageView)findViewById(R.id.download_img);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public String getRequest(String httpUrl){
        HttpURLConnection httpUrlConnection=null;
    //    BufferedReader bufferedReader=null;
        String response=null;
      //  String temp;
        try {
            URL url = new URL(httpUrl);
             httpUrlConnection =(HttpURLConnection)url.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            //设置是否允许输入，默认都是true
           // httpUrlConnection.setDoInput(true);
            //设置是否允许输出，默认是false,post的时候需要改为true
            //httpUrlConnection.setDoOutput(true);
                    InputStream inputStream =httpUrlConnection.getInputStream();
           response =getStringFromInputStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(httpUrlConnection!=null)
            httpUrlConnection.disconnect();
        }
        return response;
    }
    public String postRequest(String httpUrl, HashMap<String, String> params){
        HttpURLConnection httpUrlConnection = null;
        PrintWriter printWriter;
        String stringParams=praseParams(params);
        String response=null;
        try {
            URL url = new URL(httpUrl);
            httpUrlConnection = (HttpURLConnection)url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
            //获取UrlConnection对象对应的输入流
            printWriter = new PrintWriter(httpUrlConnection.getOutputStream());
            //发送请求参数到请求正文
            printWriter.write(stringParams);
            printWriter.flush();
            //根据responseCode判断连接是否成功
            if(httpUrlConnection.getResponseCode()!=200){
                Log.i("response","postRequest onError");
            }else{
                Log.i("response","postRequest success");
            }
            response=getStringFromInputStream(httpUrlConnection.getInputStream());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            httpUrlConnection.disconnect();
        }
        return response;
    }

    public Bitmap downloadImgFromUrl(String httpUrl){
        HttpURLConnection httpUrlConnection=null;
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(httpUrl);
            httpUrlConnection = (HttpURLConnection)url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            //和服务器建立tcp连接
            httpUrlConnection.connect();
            inputStream = httpUrlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream!=null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpUrlConnection!=null)
            httpUrlConnection.disconnect();

        }
        return bitmap;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public String praseParams(Map<String,String> params){
        StringBuffer postParams=new StringBuffer();
        //组织请求参数
        Iterator iterable = params.entrySet().iterator();
        while(iterable.hasNext()){
            Map.Entry entry =(Map.Entry)iterable.next();
            postParams.append(entry.getKey());
            postParams.append("=");
            postParams.append(entry.getValue());
            postParams.append("&");

        }
        if(postParams.length()>0){
            postParams.deleteCharAt(postParams.length()-1);
            Log.i("response", postParams.toString());
        }
        return  postParams.toString();
    }
    //将输入字节流转化为String
    public String getStringFromInputStream(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String temp;
        try {
            while ((temp =reader.readLine())!=null){
                buffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

                try {
                    if(reader!=null)
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return  buffer.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_btn:
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String response = getRequest("http://www.baidu.com");
                        tv.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response != null) {
                                    tv.setText(response);
                                } else {
                                    tv.setText("获取失败");
                                }

                            }
                        });
                    }
                }).start();*/
                NetRequest.getRequest(this, "http://www.baidu.com", new NetRequest.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        tv.setText(response);
                    }

                    @Override
                    public void onError(Exception exception, String errorInfo) {
                        Toast.makeText(MainActivity.this,"post error",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.downloadImg_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = downloadImgFromUrl("http://c.hiphotos.baidu.com/image/h=300/sign=abaceeaf6309c93d18f208f7af3cf8bb/aa64034f78f0f736cd78e8a00e55b319eac41388.jpg");

                        downloadImag.post(new Runnable() {
                            @Override
                            public void run() {
                                if(bitmap==null){
                                    Toast.makeText(MainActivity.this,"download error",Toast.LENGTH_SHORT).show();
                                }else{
                                    downloadImag.setImageBitmap(bitmap);
                                }

                            }
                        });
                    }
                }).start();
                break;
            case R.id.post_btn:
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,String> params =new HashMap<>();
                        params.put("page","1");
                        params.put("count","5");
                        final String response = postRequest(API.GetQuestions, params);
                        final Question.QuestionResult questionResult = new Gson().fromJson(response,Question.QuestionResult.class);
                        tv.post(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(questionResult.getQuestions()[0].toString());
                            }
                        });
                    }
                }).start();*/
                HashMap<String,String> params =new HashMap<>();
                params.put("page", "1");
                params.put("count", "5");
                NetRequest.postRequest(this, API.GetQuestions, params, Question.QuestionResult.class, new NetRequest.BeanCallback<Question.QuestionResult>() {
                    @Override
                    public void onSuccess(Question.QuestionResult response) {
                        tv.setText(response.getQuestions()[0].toString());
                    }

                    @Override
                    public void onError(Exception exception, String errorInfo) {

                    }
                });
                /*NetRequest.postRequest(this, API.GetQuestions, params, new NetRequest.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        tv.setText(response);
                    }

                    @Override
                    public void onError(Exception exception, String errorInfo) {
                        Toast.makeText(MainActivity.this,"post error",Toast.LENGTH_SHORT).show();
                    }
                });*/
                /*QuestionModel.getInstance().getQuestion(this, "1", "20", new ModelCallback<Question.QuestionResult>() {
                    @Override
                    public void onSuccess(Question.QuestionResult response) {
                        tv.setText(response.getQuestions()[0].toString());
                    }

                    @Override
                    public void onError(Exception e, String errorInfo) {

                    }
                });*/
                break;
        }
    }
}
