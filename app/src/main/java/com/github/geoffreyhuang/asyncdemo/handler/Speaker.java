package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Geoffrey Huang on 2016/9/17.
 */
public class Speaker implements Handler.Callback {
    public static final int SPEAKER_HELLO = 3;
    private static final String TAG = Speaker.class.getName();

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case SPEAKER_HELLO:
                sayWord("Hello from Speaker");
                break;
            default:
                return false;
        }
        return true;
    }

    private void sayWord(String word) {
        Log.i(TAG, "sayWord: " + word);
    }
}
