/*
 * Copyright 2014 Julian Shen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lutzed.servoluntario.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by julian on 13/6/21.
 */
public class LocalCircleTransform {

    private boolean isBordered;

    private final int DEFAULT_BORDER_COLOR = 0x2ab6b6b6;
    private final int DEFAULT_BORDER_RADIUS = 3;

    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size/2f;
        if (!isBordered){
            canvas.drawCircle(r, r, r, paint);
        }else{
            // Prepare the background
            Paint paintBg = new Paint();
            paintBg.setColor(DEFAULT_BORDER_COLOR);
            paintBg.setAntiAlias(true);

            // Draw the background circle
            canvas.drawCircle(r, r, r, paintBg);

            // Draw the image smaller than the background so a little border will be seen
            canvas.drawCircle(r, r, r - DEFAULT_BORDER_RADIUS, paint);
        }

        if (squaredBitmap != source) {
            squaredBitmap.recycle();
        }

        return bitmap;
    }

    public LocalCircleTransform(boolean isBordered) {
        this.isBordered = isBordered;
    }

}