package com.example.mipareader.DATA;

public class DataManager {
    private DataManager(){};

    private static volatile DataManager instance;

    public static DataManager getDataManager(){
        if(instance == null){
            synchronized (DataManager.class){
                if(instance == null){
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }


}
