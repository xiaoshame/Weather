package com.example.weather.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.weather.service.AutoUpdateService;

/**
 * Created by Lijinpu on 2015/1/20.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context,Intent intent){
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
