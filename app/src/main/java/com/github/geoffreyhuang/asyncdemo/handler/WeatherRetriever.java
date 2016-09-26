package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.TimeUnit;

/**
 * Created by Geoffrey Huang on 2016/9/17.
 */
public class WeatherRetriever extends Handler {

    private final Handler mainHandler;
    public static final int GET_TODAY_FORECAST = 1;

    public WeatherRetriever(Looper looper, Handler handler) {
        super(looper);
        this.mainHandler = handler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case GET_TODAY_FORECAST:
                String forecast = getForecast();
                Message message = mainHandler.obtainMessage(GET_TODAY_FORECAST, forecast);
                mainHandler.sendMessage(message);
                break;

        }
    }

    String getForecast() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Today's forecast";
    }
}
