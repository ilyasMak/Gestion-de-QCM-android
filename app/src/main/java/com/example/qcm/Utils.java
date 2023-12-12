package com.example.qcm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static Bitmap getImage(byte[] image){

        return BitmapFactory.decodeByteArray(image,0,image.length);

    }

    public static byte[] geyBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream bytebuffer = new ByteArrayOutputStream();
        int buffersize = 1024 ;
        byte[] buffer = new byte[buffersize] ;
        int len = 0 ;
        while((len =inputStream.read(buffer))!=-1){
            bytebuffer.write(buffer , 0 , len);
        }
        return  bytebuffer.toByteArray();
    }
}
