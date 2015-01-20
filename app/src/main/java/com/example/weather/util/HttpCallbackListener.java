package com.example.weather.util;

/**
 * Created by Lijinpu on 2015/1/19.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
