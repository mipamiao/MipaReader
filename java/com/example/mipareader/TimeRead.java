package com.example.mipareader;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class TimeRead {
    Data dt ;
    Thread TimeThread;
    boolean NeedClose;
    View TimeView;
    public TimeRead(Data DT, View tv, Activity activity){
        dt = DT;
        TimeView = tv;
        TimeThread = new Thread(new Runnable() {
            @Override
            public void run() {
             while(!NeedClose){
                 activity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         ((TextView)TimeView).setText(MinutesToString(dt.ReadTimeMinutes));
                     }
                 });
                 try {
                     Thread.sleep(30000);
                     dt.ReadTimeMinutes+=0.5;

                 } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();
                 }

             }
            }
        });
    }
    public void Start(){
        TimeThread.start();
    }
    public void Stop() throws InterruptedException {
        NeedClose = true;
        TimeThread.interrupt();
        TimeThread.join();
    }
    public boolean isAlive(){
        return TimeThread.getState()!=Thread.State.TERMINATED;
    }
    public boolean isTerminated(){
        return TimeThread.getState()==Thread.State.TERMINATED;
    }
    public String MinutesToString(float minutes){
        int hour = (int) (minutes/60);
        int minute = (int) (minutes - 60*hour);
        return hour+":"+minute;
    }
}
