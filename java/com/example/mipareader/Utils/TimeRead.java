package com.example.mipareader.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.mipareader.DATA.Data;
import com.example.mipareader.MyApp;

public class TimeRead {
    private Data data;
    private Thread timeThread;
    private boolean needClose;
    private View timeView;

    public TimeRead(Data data, TimeReadShow timeReadShow) {
        this.data = data;
        this.timeView = timeView;
        this.timeThread = new Thread(() -> {
            while (!needClose) {
                try {
                    Thread.sleep(30000);
                    data.setReadTimeMinutes(data.getReadTimeMinutes() + 0.5f);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                timeReadShow.showReadTimeOnUIAsyn(minutesToString(data.getReadTimeMinutes()));
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

    public static String minutesToString(float minutes) {
        int hour = (int) (minutes / 60);
        int minute = (int) (minutes - 60 * hour);
        return String.format("%d:%02d", hour, minute);
    }
}