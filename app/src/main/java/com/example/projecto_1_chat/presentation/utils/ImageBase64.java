package com.example.projecto_1_chat.presentation.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageBase64 {

    public static  String bitmapToBase64(Bitmap bitmap){
        if(bitmap == null) return null;

        int masSize = 800;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(width > masSize || height > masSize){
            float ratio = (float) width / (float) height;
            if(ratio > 1) {
                width = masSize;
                height = (int) (width / ratio);
            } else {
                height = masSize;
                width = (int) (height * ratio);
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64String){
        if(base64String == null || base64String.trim().isEmpty()) return null;
        byte[] decodeBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);
    }

}
