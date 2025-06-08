package com.example.mipareader.Utils;

public class TransResult{

    public TransResult(String resultStr, String restStr, int restLineCount){
        this.resultStr = resultStr;
        this.restStr = restStr;
        this.restLineCount = restLineCount;
    }

    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    private String resultStr;
    private String restStr;

    public int getByteCount() {
        return byteCount;
    }

    private int byteCount;

    private int restLineCount;

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public String getRestStr() {
        return restStr;
    }

    public void setRestStr(String restStr) {
        this.restStr = restStr;
    }

    public int getRestLineCount() {
        return restLineCount;
    }

    public void setRestLineCount(int restLineCount) {
        this.restLineCount = restLineCount;
    }


}
