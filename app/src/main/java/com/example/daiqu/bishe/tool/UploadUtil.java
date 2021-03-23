package com.example.daiqu.bishe.tool;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class UploadUtil  {
    private static String ip = "10.16.66.2";
    private static int port = 9000;
    /**
     * 由于输入流的read方法会阻塞，为了避免它影响主界面的其他数据处理， 启动一个线程来读取输入流中的数据，并对数据进行相应的处理
     */
    public  void loadPic(ImageView imageView){
        new Thread(() -> {
            try {
                Socket sc = new Socket(ip,port);
                DataInputStream inputStream = new DataInputStream(sc.getInputStream());
                int size = inputStream.readInt();
                byte[] data = new byte[size];
                int len=0;
                while(len<size){
                    len+=inputStream.read(data, len, size-len);
                }
                ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outPut);
                imageView.setImageBitmap(bmp);
                sc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}