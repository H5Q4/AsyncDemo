package com.github.geoffreyhuang.asyncdemo.handler;

import android.os.Looper;

/**
 * Created by Geoffrey Huang on 2016/9/16.
 */
public class SimpleLooper extends Thread {
    boolean started = false;
    final Object startMonitor = new Object();
    Looper threadLooper;

    @Override
    public void run() {
        Looper.prepare();
        threadLooper = Looper.myLooper();
        synchronized (startMonitor) {
            started = true;
            startMonitor.notifyAll();
        }
        Looper.loop();
    }

    Looper getLooper() {
        return threadLooper;
    }

    void waitForStart() {
        synchronized (startMonitor) {
            while (!started){
                try {
                    startMonitor.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
