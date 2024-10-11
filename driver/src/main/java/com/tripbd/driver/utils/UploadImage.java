package com.tripbd.driver.utils;

import android.content.Context;

import com.google.firebase.storage.StorageReference;

public  class UploadImage{
    byte[] bitmapdata; StorageReference ref;
    Context context;
    public UploadImage(Context context, byte[] bitmapdata, StorageReference ref){
        this.bitmapdata= bitmapdata;
        this.context= context;
        this.ref = ref;



    }

String url;
    public Upload getObj(String s){
        class Get extends Upload {
            @Override
            String getImageUrl() {
                return s;
            }
        }
        Upload get = new Get();
        url = get.getImageUrl();
        url = s;
        return get;
    }

    public String getUploadedImageUrl(){
        return url;
    }


}



