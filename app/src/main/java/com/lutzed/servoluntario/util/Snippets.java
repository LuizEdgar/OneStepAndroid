package com.lutzed.servoluntario.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

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

    public static String encodeToBase64(Bitmap image) {
        if (image == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap getProportionalResizedBitmap(Bitmap bm, int maxSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width < maxSize && height < maxSize) {
            return bm;
        }

        float scale = 1f;

        if (width >= height) {
            scale = ((float) maxSize) / width;
        } else {
            scale = ((float) maxSize) / height;
        }

        int scaleWidth = (int) (scale * width);
        int scaleHeight = (int) (scale * height);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, false);

        bm.recycle();

        return resizedBitmap;
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

}
