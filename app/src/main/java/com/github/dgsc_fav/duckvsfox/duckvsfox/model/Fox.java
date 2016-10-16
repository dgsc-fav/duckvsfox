package com.github.dgsc_fav.duckvsfox.duckvsfox.model;

import android.content.Context;
import android.graphics.Canvas;

import com.github.dgsc_fav.duckvsfox.duckvsfox.R;
import com.github.dgsc_fav.duckvsfox.duckvsfox.util.PaintUtils;

/**
 * Created by DG on 15.10.2016.
 */
public class Fox extends Animal {

    public Fox(Context context) {
        init(context);
    }

    @Override
    protected void init(Context context) {
        mNormalPaint = PaintUtils.createRadiusPaint(context, R.color.foxNormal);
        mActivePaint = PaintUtils.createFilledRadiusPaint(context, R.color.foxActive);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x + mViewOffsetX, y + mViewOffsetY, mPaintRadius, getPaint());
    }
}
