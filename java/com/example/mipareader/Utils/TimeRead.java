package com.example.mipareader.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.mipareader.DATA.Data;

public class TimeRead {
    private Data data;
    private Thread timeThread;
    private boolean needClose;
    private View timeView;

    public TimeRead(Data data, View timeView, Activity activity) {
        this.data = data;
        this.timeView = timeView;
        this.timeThread = new Thread(() -> {
            while (!needClose) {
                activity.runOnUiThread(() ->
                        ((TextView) timeView).setText(minutesToString(data.getReadTimeMinutes()))
                );
                try {
                    Thread.sleep(30000);
                    data.setReadTimeMinutes(data.getReadTimeMinutes() + 0.5f);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void start() {
        timeThread.start();
    }

    public void stop() throws InterruptedException {
        needClose = true;
        timeThread.interrupt();
        timeThread.join();
    }

    public boolean isAlive() {
        return timeThread.getState() != Thread.State.TERMINATED;
    }

    public boolean isTerminated() {
        return timeThread.getState() == Thread.State.TERMINATED;
    }

    private String minutesToString(float minutes) {
        int hour = (int) (minutes / 60);
        int minute = (int) (minutes - 60 * hour);
        return String.format("%d:%02d", hour, minute);
    }
}