package com.github.dgsc_fav.duckvsfox.duckvsfox.util;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by DG on 15.10.2016.
 */
public class PaintUtils {

    public static Paint createRadiusPaint(Context context, @ColorRes int id) {
        return createRadiusPaint(ContextCompat.getColor(context, id));
    }

    public static Paint createRadiusPaint(@ColorInt int color) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        p.setAntiAlias(true);
        p.setColor(color);
        return p;
    }

    public static Paint createFilledRadiusPaint(Context context, @ColorRes int id) {
        return createFilledRadiusPaint(ContextCompat.getColor(context, id));
    }

    public static Paint createFilledRadiusPaint(@ColorInt int color) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeWidth(3f);
        p.setAntiAlias(true);
        p.setColor(color);
        return p;
    }
}
