package com.github.geoffreyhuang.asyncdemo.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.geoffreyhuang.asyncdemo.R;

public class SaveMyLocationActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_my_location);
    }

    public void onStartServiceClick(View view) {
        Intent intent = new Intent(SaveMyLocationActivity.this, SaveMyLocationService.class);
        intent.putExtra(SaveMyLocationService.LOCATION_KEY, getCurrentLocation());
        startService(intent);
    }

    private String getCurrentLocation() {
        count ++;
        String[] locations = {"ShangHai", "SuZhou", "HeFei"};
        return locations[count % locations.length];
    }

    public void onStopServiceClick(View view) {
        Intent intent = new Intent(SaveMyLocationActivity.this, SaveMyLocationService.class);
        stopService(intent);
    }

    public void onCountMsgsClick(View view) {
        Intent intent = new Intent(SaveMyLocationActivity.this, CountMsgsActivity.class);
        startActivity(intent);
    }
}
