package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Geoffrey Huang on 2016/9/17.
 */
public class WeatherPresenter extends Handler {
    private static final String TAG = WeatherPresenter.class.getName();
    public static final int TODAY_FORECAST = 1;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TODAY_FORECAST:
                Log.i(TAG, "handleMessage: " + msg.obj);
                break;
        }
    }
}
