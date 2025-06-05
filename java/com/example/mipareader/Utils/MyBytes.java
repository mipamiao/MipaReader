package com.example.mipareader.Utils;

public class MyBytes {
    byte[] buffer = new byte[1024];
    int size = 0;

    void appendByte(byte b) {
        if (size >= buffer.length) {
            byte[] newBuffer = new byte[buffer.length * 2];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
        buffer[size++] = b;
    }
    public void addBytes(byte[] bytes){
        enLarge(size + bytes.length);
        System.arraycopy(bytes, 0, buffer, size, bytes.length);
        size += bytes.length;
    }

    void enLarge(int targetSize){
        int scale = (int)Math.ceil(Math.log(targetSize)/Math.log(2));
        int trueTargetSize = (int)Math.pow(2,scale);
        if(trueTargetSize>buffer.length){
            byte[] newBuffer = new byte[trueTargetSize];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
    }

    public byte[] getBytes(){
        return buffer;
    }
    public int getSize(){
        return size;
    }
    public void clear(){
        size  = 0;
    }

}
