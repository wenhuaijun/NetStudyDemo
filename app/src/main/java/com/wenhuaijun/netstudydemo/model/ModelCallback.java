package com.wenhuaijun.netstudydemo.model;

/**
 * Created by Administrator on 2016/3/20 0020.
 */
public interface ModelCallback<T> {
    public void onSuccess(T response);
    public void onError(Exception e,String errorInfo);
}
