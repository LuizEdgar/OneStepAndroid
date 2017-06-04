package com.lutzed.servoluntario.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static android.R.attr.angle;

/**
 * Created by luizfreitas on 08/05/2016.
 */
public class Snippets {

    public static Intent getShareIntent(String extraText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static float getDisplayDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getScaleDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static String encodeToBase64(Bitmap image, boolean addApiPrefix) {
        if (image == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        if (addApiPrefix)
            return "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);
        else
            return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() > 5;
    }

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9]{3,15}$");

    public static boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isPhoneValid(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    public static long[] toArray(List<Long> values) {
        long[] result = new long[values.size()];
        int i = 0;
        for (Long l : values)
            result[i++] = l;
        return result;
    }

    public static FileAndPathHolder createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return new FileAndPathHolder(image.getAbsolutePath(), image);
    }

    public static Bitmap bitmapFromPath(String path, int maxSize, boolean shouldScale, int rotate) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        int scaleFactor;
        if (photoW >= photoH) {
            scaleFactor = photoW / maxSize;
        } else {
            scaleFactor = photoH / maxSize;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Log.d("SnippetsBMFP", "scaleFactor:" + scaleFactor);

        Bitmap decodeBitmap = BitmapFactory.decodeFile(path, bmOptions);

        if (rotate != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            Bitmap bitmap = Bitmap.createBitmap(decodeBitmap, 0, 0, decodeBitmap.getWidth(), decodeBitmap.getHeight(),
                    matrix, true);
            decodeBitmap.recycle();
            decodeBitmap = bitmap;
        }

        if (shouldScale) {
            return getProportionalResizedBitmap(decodeBitmap, maxSize);
        }

        return decodeBitmap;
    }

    public static Bitmap decodeStreamOptimized(Context context, Uri uri, int maxSize, boolean shouldScale) {

        InputStream boundsInputStream = null;
        try {
            boundsInputStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(boundsInputStream, null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor;
        if (photoW >= photoH) {
            scaleFactor = photoW / maxSize;
        } else {
            scaleFactor = photoH / maxSize;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Log.d("SnippetsBMFP", "scaleFactor:" + scaleFactor);

        InputStream fileInputStream = null;
        try {
            fileInputStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap decodeBitmap = BitmapFactory.decodeStream(fileInputStream, null, bmOptions);

        if (shouldScale) {
            return getProportionalResizedBitmap(decodeBitmap, maxSize);
        }
        return decodeBitmap;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static Bitmap getProportionalResizedBitmap(Bitmap bm, int maxSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return bm;
        }

        float scale;

        if (width >= height) {
            scale = ((float) maxSize) / width;
        } else {
            scale = ((float) maxSize) / height;
        }

        int scaleWidth = (int) (scale * width);
        int scaleHeight = (int) (scale * height);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, false);

        if (bm != resizedBitmap) {
            bm.recycle();
        }

        return resizedBitmap;
    }


    public static int fixCameraRotation(String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;

            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;

            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return 0;
        }
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, int messageRes) {
        return createProgressDialog(context, context.getString(messageRes));
    }

    public static AlertDialog createSimpleMessageDialog(Context context, int titleRes, int messageRes) {
        return new AlertDialog.Builder(context).setTitle(titleRes).setMessage(messageRes).setPositiveButton(android.R.string.ok, null).create();
    }

}
