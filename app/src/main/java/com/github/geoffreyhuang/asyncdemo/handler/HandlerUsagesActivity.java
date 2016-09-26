package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.geoffreyhuang.asyncdemo.R;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class HandlerUsagesActivity extends AppCompatActivity {

    private static final String TAG = HandlerUsagesActivity.class.getName();
    TextView mTextView;
    Button mFetchWeatherBtn;

    HandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_usages);

        mTextView = (TextView) findViewById(R.id.txt_tv);
        mFetchWeatherBtn = (Button) findViewById(R.id.btn_fetch_weather);

//        Handler handler = new StackTraceHandler();
//        Message message = handler.obtainMessage();
//        handler.sendMessage(message);

//        SimpleLooper sl = new SimpleLooper();
//        sl.setPriority(Thread.MIN_PRIORITY);
//        sl.start();
//        sl.waitForStart();
//        Handler handler = new StackTraceHandler(sl.getLooper());
//        Message message = handler.obtainMessage();
//        handler.sendMessage(message);


        //region Scheduling work with post
//        Handler handler = new Handler(getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mTextView.setText("From handler.post");
//            }
//        });
//        handler.postAtFrontOfQueue(new Runnable() {
//            @Override
//            public void run() {
//                mTextView.setText("From handler.postAtFrontOfQueue");
//            }
//        });
        //endregion

        //region Use handler to defer work
//        Handler handler = new Handler();
//        //Defer work in the main thread by 5 seconds time
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(
//                        HandlerUsagesActivity.this,
//                        "handler.postDelayed",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }, TimeUnit.SECONDS.toMillis(5));
//        //Do work in the main thread at a specific time relative to the system uptime
//        handler.postAtTime(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(
//                        HandlerUsagesActivity.this,
//                        "handler.postAtTime",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }, SystemClock.uptimeMillis() + TimeUnit.SECONDS.toMillis(10));
        //endregion

        //region Using handler to update UI
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                Log.i(TAG, "run: Enter run");
//                try {
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mTextView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mTextView.setText("Using handler to update UI");
//                    }
//                });
//            }
//        };
//        thread.setPriority(Thread.MIN_PRIORITY);
//        thread.start();
        //endregion

        //region Cancelling pending messages
//        Handler handler = new SpeakHandler();
//        String stringRef1 = new String("Welcome!");
//        String stringRef2 = new String("Welcome home!");
//        Message msg1 = Message.obtain(handler, SpeakHandler.SAY_WORD, stringRef1);
//        Message msg2 = Message.obtain(handler, SpeakHandler.SAY_WORD, stringRef2);
//        handler.sendMessageDelayed(msg1, TimeUnit.SECONDS.toMillis(60));
//        handler.sendMessageDelayed(msg2, TimeUnit.SECONDS.toMillis(60));
//        handler.removeMessages(SpeakHandler.SAY_WORD, stringRef1);
//        handler.removeMessages(SpeakHandler.SAY_WORD, new String("Welcome home!"));
//        PrintWriterPrinter pwp = new PrintWriterPrinter(new PrintWriter(System.out, true));
//        handler.getLooper().dump(pwp, ">> Looper dump ");
        //endregion

        //region Composition vs inheritance
//        Handler handler = new SpeakHandler(new Speaker());
//        //Handle by SpeakHandler
//        handler.sendEmptyMessage(SpeakHandler.SAY_HELLO);
//        //Handle by Speaker
//        handler.sendEmptyMessage(Speaker.SPEAKER_HELLO);
        //endregion

        handlerThread = new HandlerThread("background",
                Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        PrintWriterPrinter pwp = new PrintWriterPrinter(new PrintWriter(System.out, true));
        handlerThread.getLooper().setMessageLogging(pwp);
        Handler presentHandler = new WeatherPresenter();
        final WeatherRetriever weatherRetriever
                = new WeatherRetriever(handlerThread.getLooper(), presentHandler);
        mFetchWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherRetriever.sendEmptyMessage(WeatherRetriever.GET_TODAY_FORECAST);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handlerThread != null && isFinishing()) {
            handlerThread.quit();
        }
    }
}
