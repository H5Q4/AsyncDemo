package com.github.geoffreyhuang.asyncdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.geoffreyhuang.asyncdemo.asynctask.ShowPuppyActivity;
import com.github.geoffreyhuang.asyncdemo.handler.HandlerUsagesActivity;
import com.github.geoffreyhuang.asyncdemo.service.SaveMyLocationActivity;
import com.github.geoffreyhuang.asyncdemo.service.SaveMyLocationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mHandlerBtn;
    Button mAsyncTaskBtn;
    Button mServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandlerBtn = (Button) findViewById(R.id.btn_handler);
        mAsyncTaskBtn = (Button) findViewById(R.id.btn_asysnctask);
        mServiceBtn = (Button) findViewById(R.id.btn_service);
        mServiceBtn.setOnClickListener(this);
        mAsyncTaskBtn.setOnClickListener(this);
        mHandlerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.btn_handler:
                intent.setClass(MainActivity.this, HandlerUsagesActivity.class);
                break;
            case R.id.btn_asysnctask:
                intent.setClass(MainActivity.this, ShowPuppyActivity.class);
                break;
            case R.id.btn_service:
                intent.setClass(MainActivity.this, SaveMyLocationActivity.class);
                break;
        }
        startActivity(intent);
    }
}
