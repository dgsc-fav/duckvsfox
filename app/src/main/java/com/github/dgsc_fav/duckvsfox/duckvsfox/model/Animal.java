package com.github.dgsc_fav.duckvsfox.duckvsfox.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import static com.github.dgsc_fav.duckvsfox.duckvsfox.util.Constants.DEFAULT_ANIMAL_PAINT_RADIUS;

/**
 * Created by DG on 15.10.2016.
 */

public abstract class Animal {

    // оригинальные на View
    protected float x;
    protected float y;
    protected Paint mNormalPaint;
    protected Paint mActivePaint;
    protected Paint mCatchedPaint;
    protected Paint mFreedPaint;
    protected int   mPaintRadius;

    private boolean mIsActive;
    private boolean mIsCatched;
    private boolean mIsFreed;

    protected float mViewOffsetX;
    protected float mViewOffsetY;

    protected Animal() {
        mPaintRadius = DEFAULT_ANIMAL_PAINT_RADIUS;
    }

    protected abstract void init(Context context);

    public abstract void draw(Canvas canvas);

    public void setActive(boolean isActive) {
        this.mIsActive = isActive;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // TODO: 15.10.2016 enum
    public Paint getPaint() {
        return mIsActive ? mActivePaint : mIsCatched ? mCatchedPaint : mIsFreed ? mFreedPaint : mNormalPaint;
    }

    public int getPaintRadius() {
        return mPaintRadius;
    }

    public boolean isCatched() {
        return mIsCatched;
    }

    public void setCatched(boolean catched) {
        mIsCatched = catched;
    }

    public boolean isFreed() {
        return mIsFreed;
    }

    public void setFreed(boolean freed) {
        mIsFreed = freed;
    }

    public void setViewOffsetX(float mViewOffsetX) {
        this.mViewOffsetX = mViewOffsetX;
    }

    public void setViewOffsetY(float mViewOffsetY) {
        this.mViewOffsetY = mViewOffsetY;
    }
}
