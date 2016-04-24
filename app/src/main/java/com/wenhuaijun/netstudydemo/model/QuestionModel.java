package com.wenhuaijun.netstudydemo.model;

import android.app.Activity;

import com.google.gson.Gson;
import com.wenhuaijun.netstudydemo.config.API;
import com.wenhuaijun.netstudydemo.model.bean.Question;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/20 0020.
 */
public class QuestionModel {
    private static QuestionModel model;
    public static synchronized  QuestionModel getInstance(){

        if(model ==null){
            model = new QuestionModel();
        }
        return model;
    }
    public  void getQuestion(Activity activity,String page,String count, final ModelCallback<Question.QuestionResult> callback){
        HashMap<String,String> params =new HashMap<>();
            params.put("page",page);
            params.put("count",count);
            NetRequest.postRequest(activity, API.GetQuestions, params, new NetRequest.CallBack() {
                @Override
                public void onSuccess(String response) {
                    Question.QuestionResult questionResult= new Gson().fromJson(response,Question.QuestionResult.class);
                    callback.onSuccess(questionResult);
                }

                @Override
                public void onError(Exception exception, String errorInfo) {
                        callback.onError(exception,errorInfo);
                }
            });
        model=null;
    }
}
