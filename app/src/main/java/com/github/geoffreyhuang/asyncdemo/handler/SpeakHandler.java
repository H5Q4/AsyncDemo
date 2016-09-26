package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Geoffrey Huang on 2016/9/16.
 */
public class SpeakHandler extends Handler {
    public static final int SAY_HELLO = 0;
    public static final int SAY_BYE = 1;
    public static final int SAY_WORD = 2;
    private static final String TAG = SpeakHandler.class.getName();

    public SpeakHandler() {
        super();
    }

    public SpeakHandler(Callback callback) {
        super(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case SAY_HELLO:
                sayWord("hello"); break;
            case SAY_BYE:
                sayWord("goodbye"); break;
            case SAY_WORD:
                // Get an Object
                sayWord((String)msg.obj); break;
            default:
                super.handleMessage(msg);
        }
    }
    private void sayWord(String word) {
        Log.i(TAG, "sayWord: " + word);
    }
}
