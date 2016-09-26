package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Geoffrey Huang on 2016/9/16.
 */
public class StackTraceHandler extends Handler {

    public StackTraceHandler() {
        super();
    }

    public StackTraceHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        Log.e("StackTraceHandler", "CurrentThread: " + Thread.currentThread().getName());
    }
}
