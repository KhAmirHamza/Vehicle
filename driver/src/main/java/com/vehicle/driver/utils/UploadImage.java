package com.vehicle.driver.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
        class Get extends Upload{
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



