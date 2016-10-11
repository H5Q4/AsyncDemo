package com.github.geoffreyhuang.asyncdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class SaveMyLocationService extends Service {
    public static final String LOCATION_KEY = "location_key";
    boolean mShouldStop = false;
    final Queue<String> mJobs = new LinkedList<>();

    public SaveMyLocationService() {
    }

    Thread mThread = new Thread() {
        @Override
        public void run() {
            while (!mShouldStop) {
                String location = takeLocation();
                if (location != null) {
                    consumeLocation(location);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String location = intent.getStringExtra(LOCATION_KEY);
        synchronized (mJobs) {
            mJobs.add(location);
            mJobs.notify();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (mJobs) {
            mShouldStop = true;
            mJobs.notify();
        }
    }

    private void consumeLocation(String location) {
        Log.i("SaveMyLocationService", "Saving location: "
        + location
        + " on Thread "
        + Thread.currentThread().getName());
    }

    private String takeLocation() {
        String location = null;
        synchronized (mJobs) {
            if (mJobs.isEmpty()) {
                try {
                    mJobs.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            location = mJobs.poll();
        }
        return location;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
