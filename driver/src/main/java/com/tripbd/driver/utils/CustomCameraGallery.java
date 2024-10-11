package com.tripbd.driver.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomCameraGallery{
    private static final String TAG = "CustomCameraGallery";
    static Uri picUri;

    MyCamera myCamera;
    Activity activity;
    boolean permissionIsGranted = false;
    public CustomCameraGallery(Activity activity){
        this.activity = activity;
    }


    private static ArrayList<String> permissionsToRequest;
    private static ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    int requestCode;

    public void pickImage(Activity activity, MyCamera myCamera, int requestCode){

        this.myCamera = myCamera;
        this.requestCode = requestCode;
        this.activity = activity;
        //Step1: check and request permission;
        //Step2: if permission Granted, start activity for pick Image
        checkAndRequestPermission();
        if(permissionIsGranted){
            activity.startActivityForResult(getPickImageChooserIntent(), requestCode);
        }
    }

    public void checkAndRequestPermission(){
        permissions.add(Manifest.permission.CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                Log.d(TAG, "checkAndRequestPermission: count: "+permissionsToRequest.size());
                activity.requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }else{
                //permission granted...
                permissionIsGranted = true;
                //activity.startActivityForResult(getPickImageChooserIntent(), requestCode);
            }
        }else{
            //No need to wait for permission since lower api level, start activity to choose image...
            permissionIsGranted = true;
            //activity.startActivityForResult(getPickImageChooserIntent(), requestCode);
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br />
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br />
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {
        Toast.makeText(activity, "getPickImageChooserIntent called", Toast.LENGTH_SHORT).show();;
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList();
        PackageManager packageManager = activity.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = (Intent) allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals(activity.getApplication().getPackageName())) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = activity.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public byte[] compressImage(Uri imageUri){
        byte[] bitmapdata;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            //Compressed file size if greater than 1 MB;
            if (height>=3000){
                bitmap = Bitmap.createScaledBitmap(bitmap, width/3, height/3, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

            }else if (height>=2500){
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width/2.5), (int) (height/2.5), true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            }else if (height>=2000){
                bitmap = Bitmap.createScaledBitmap(bitmap, (width/2),  (height/2), true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            }else if (height>=1500){
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width/1.5), (int) (height/1.5), true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            }else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }

            bitmapdata = byteArrayOutputStream.toByteArray();
            return bitmapdata;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmapdata = null;
        }
    }


    public Uri receiveImageFromDeviceRequest(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                Log.d(TAG, "receiveImageFromDeviceRequest: picUri: "+picUri.toString());

                //todo...uri found use it...
                myCamera.gotUri(picUri, requestCode);


            } else {
                //bitmap = (Bitmap) data.getExtras().get("data");
                picUri = (Uri) data.getExtras().get("data");
                }
        }
        return null;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br />
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    //@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        receiveImageFromDeviceRequest(requestCode, resultCode, data);
    }

    //@Override
    public static void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    //@Override
    public static void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: permissions: "+ Arrays.toString(grantResults)+", RequestCode: "+requestCode);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {
                        //todo...Permission Granted ... open requested feature...
                        //activity starting to choose image...
                        Log.d(TAG, "onRequestPermissionsResult: Granted: start activity for result");
                        activity.startActivityForResult(getPickImageChooserIntent(), 200);
                    } else {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                            activity.requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }
    }



    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();
        Log.d(TAG, "findUnAskedPermissions: called");
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
                Log.d(TAG, "findUnAskedPermissions: hasPErmission: "+false);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                boolean grantedOrNot = (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
                Log.d(TAG, "hasPermission : "+grantedOrNot);
                return grantedOrNot;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private static boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }




    public static abstract class MyCamera{
        public abstract void gotUri(Uri picUri, int requestCode);

        public abstract void somethingWrong(String errorMessage);
    }
}

