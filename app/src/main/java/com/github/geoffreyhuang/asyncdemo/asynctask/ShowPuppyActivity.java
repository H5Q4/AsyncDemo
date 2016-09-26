package com.github.geoffreyhuang.asyncdemo.asynctask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.geoffreyhuang.asyncdemo.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShowPuppyActivity extends AppCompatActivity {

    private static final Queue<Runnable> QUEUE = new LinkedBlockingQueue<>();
    public static final Executor MY_EXECUTOR
            = new ThreadPoolExecutor(4, 8, 1, TimeUnit.MINUTES, (BlockingQueue<Runnable>) QUEUE);

    Button mShowPuppyBtn;
    Button mShowPuppyWrongBtn;
    Button mGoHeadlessBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_puppy);
        mShowPuppyBtn = (Button) findViewById(R.id.btn_show_puppy);
        mShowPuppyWrongBtn = (Button) findViewById(R.id.btn_show_puppy_wrong);
        mGoHeadlessBtn = (Button) findViewById(R.id.btn_go_headless);
        mGoHeadlessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(ShowPuppyActivity.this, ShowPuppyWithHeadlessActivity.class);
                startActivity(intent);
            }
        });
        mShowPuppyWrongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("https://hd.unsplash" +
                            ".com/photo-1444212477490-ca407925329e-no");
                    ImageView imageView = (ImageView) findViewById(R.id.iv_puppy);
                    new SafeDownloadImageTask(ShowPuppyActivity.this, imageView).execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        mShowPuppyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("https://hd.unsplash.com/photo-1444212477490-ca407925329e");
                    ImageView imageView = (ImageView) findViewById(R.id.iv_puppy);
                    new DownloadImageTask(ShowPuppyActivity.this, imageView)
                            .executeOnExecutor(MY_EXECUTOR, url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
