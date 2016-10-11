package com.github.geoffreyhuang.asyncdemo.service;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.geoffreyhuang.asyncdemo.R;

public class CountMsgsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_msgs);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                1);
        }
        Button startBtn = (Button) findViewById(R.id.btn_start_count);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText phoneEt = (EditText) findViewById(R.id.et_phone);
                String phone = phoneEt.getText().toString();
                triggerIntentService(phone);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == CountMsgsIntentService.RESULT_CODE) {
            int result = data.getIntExtra(CountMsgsIntentService.RESULT, -1);
            TextView countTv = (TextView) findViewById(R.id.tv_count);
            countTv.setText(Integer.toString(result));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void triggerIntentService(String phoneNumber) {
        PendingIntent pendingIntent = createPendingResult(REQUEST_CODE, new Intent(), 0);
        Intent intent = new Intent(CountMsgsActivity.this, CountMsgsIntentService.class);
        intent.putExtra(CountMsgsIntentService.KEY_NUMBER, phoneNumber);
        intent.putExtra(CountMsgsIntentService.PENDING_RESULT, pendingIntent);
        startService(intent);
    }
}
