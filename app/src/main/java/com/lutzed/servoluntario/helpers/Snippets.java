package com.lutzed.servoluntario.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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

}
