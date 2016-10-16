package com.github.dgsc_fav.duckvsfox.duckvsfox.view.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.dgsc_fav.duckvsfox.duckvsfox.R;
import com.github.dgsc_fav.duckvsfox.duckvsfox.model.Duck;
import com.github.dgsc_fav.duckvsfox.duckvsfox.model.Fox;
import com.github.dgsc_fav.duckvsfox.duckvsfox.util.Constants;
import com.github.dgsc_fav.duckvsfox.duckvsfox.util.Geometry;
import com.github.dgsc_fav.duckvsfox.duckvsfox.util.PaintUtils;

import java.util.Timer;
import java.util.TimerTask;


public class LakeView extends View {

    private int   mGoodRadius;
    private int   mLakeRadius;
    private Paint mGoodCirclePaint;
    private Paint mLakeCirclePaint;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private Duck  mDuck;
    private Fox   mFox;

    private float mPointerX;
    private float mPointerY;

    private final Timer     mTimer                   = new Timer();
    private final Rect      mMyViewRect              = new Rect();
    private final float[]   mOffsetsOnRootView       = new float[2];
    private final TimerTask mTimerTask               = new TimerTask() {
        @Override
        public void run() {
            onTimer();
        }
    };
    private final float     mMaxDuckVelocity         = Constants.DUCK_VELOCITY_PX_PER_TIME * Constants.DUCK_VELOCITY_FACTOR;
    private final Runnable  mActionOnPositionChanges = new Runnable() {
        @Override
        public void run() {
            onPositionChanges();
        }
    };
    private final Runnable  mActionInvalidate        = new Runnable() {
        @Override
        public void run() {
            LakeView.this.invalidate();
        }
    };

    public LakeView(Context context) {
        super(context);
        init(context);
    }

    public LakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LakeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;

        initAnimals();
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        getLocalVisibleRect(mMyViewRect);
        mOffsetsOnRootView[0] = mMyViewRect.left;
        mOffsetsOnRootView[1] = mMyViewRect.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLakeCircles(canvas);

        if(mDuck != null) {
            mDuck.draw(canvas);
        }
        if(mFox != null) {
            mFox.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            nextEvent(x, y, true);
        } else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            nextEvent(x, y, false);
        }

        return super.onTouchEvent(event);
    }

    private void nextEvent(float x, float y, boolean inAction) {
        mPointerX = x - mOffsetsOnRootView[0] - mCenterX + mRadius;
        mPointerY = y - mOffsetsOnRootView[1] - mCenterY + mRadius;

        if(mDuck != null && !mDuck.isFreed() && !mDuck.isCatched()) {
            mDuck.setActive(inAction);
            mFox.setActive(inAction);
        }
    }

    private void drawLakeCircles(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mGoodRadius, mGoodCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mLakeRadius, mLakeCirclePaint);
    }

    public void setLake(int good, int lake) {
        mGoodRadius = good;
        mLakeRadius = lake;
        invalidate();
    }

    private void onTimer() {
        if(mDuck == null || !mDuck.isActive() || mFox == null) {
            return;
        }

        moveDuck();

        moveFox();

        invalidateInUi();

        post(mActionOnPositionChanges);
    }

    private void moveDuck() {
        float desiredAngle = Geometry.getAngle(mPointerX - mDuck.getX(), mPointerY - mDuck.getY());
        //noinspection SuspiciousNameCombination
        float desiredDistance = Geometry.distance(mPointerX, mDuck.getX(), mPointerY, mDuck.getY());

        // не быстрее maxDuckVelocity пикселей за такт/
        // так как обработка в тактах, то maxDuckVelocity есть расстояние
        if(desiredDistance >= mMaxDuckVelocity) {
            mDuck.setX(Geometry.getNextXByAngle(mDuck.getX(), desiredAngle, mMaxDuckVelocity));
            mDuck.setY(Geometry.getNextYByAngle(mDuck.getY(), desiredAngle, mMaxDuckVelocity));
        } else {
            mDuck.setX(mPointerX);
            mDuck.setY(mPointerY);
        }
    }

    private void moveFox() {
        // направление, куда движется утка до края окружности
        float duckMovingAngle = Geometry.getCorrectedAngle(mDuck.getX() - mRadius, mDuck.getY() - mRadius);
        float foxMovingAngle = Geometry.getCorrectedAngle(mFox.getX() - mRadius, mFox.getY() - mRadius);

        float foxW = mMaxDuckVelocity * Constants.FOX_VELOCITY_FACTOR / mRadius;

        if(Math.abs(foxMovingAngle - duckMovingAngle) > foxW) {
            // надо на перехват
            float deltaAngle = (float) ((foxMovingAngle - duckMovingAngle) / Math.PI);

            // влево или вправо бежать лиcе. куда короче
            if(deltaAngle > 0 && deltaAngle < 1 || deltaAngle < -1) {
                foxMovingAngle = foxMovingAngle - foxW;
                // утка по правую лапу лисы
            } else {
                foxMovingAngle = foxMovingAngle + foxW;
                // утка по левую лапу лисы
            }
        } else {
            // на одном векторе
            foxMovingAngle = duckMovingAngle;
        }

        mFox.setX(Geometry.getNextXByAngle(mRadius, foxMovingAngle, mRadius));
        mFox.setY(Geometry.getNextYByAngle(mRadius, foxMovingAngle, mRadius));
    }

    private void init(Context context) {
        mGoodCirclePaint = PaintUtils.createRadiusPaint(context, R.color.goodCircle);
        mLakeCirclePaint = PaintUtils.createRadiusPaint(context, R.color.lakeCircle);

        mTimer.scheduleAtFixedRate(mTimerTask, 0, Constants.DEFAULT_TIMER_PERIOD_MS);
    }

    public void setAnimals(Duck duck, Fox fox) {
        mDuck = duck;
        mFox = fox;
        initAnimals();
        invalidate();
    }

    private void initAnimals() {
        if(mDuck == null || mFox == null) {
            return;
        }

        mRadius = mLakeRadius;

        mDuck.setActive(false);
        mDuck.setCatched(false);
        mDuck.setFreed(false);

        mFox.setActive(false);

        mDuck.setX(mRadius);
        mDuck.setY(mRadius);
        mDuck.setViewOffsetX(mCenterX - mRadius);
        mDuck.setViewOffsetY(mCenterY - mRadius);

        float[] xy = Geometry.getRandomXyOnCircle(mLakeRadius);
        mFox.setX(mRadius + xy[0]);
        mFox.setY(mRadius + xy[1]);
        mFox.setViewOffsetX(mCenterX - mRadius);
        mFox.setViewOffsetY(mCenterY - mRadius);
    }

    private void onPositionChanges() {

        boolean isIntercepted = Geometry.isIntercept(mDuck.getX(), mDuck.getY(), mFox.getX(), mFox.getY(), mDuck.getPaintRadius());
        if(isIntercepted) {
            mDuck.setCatched(true);
            mDuck.setActive(false);
            invalidate();
            return;
        }

        boolean inCircle = Geometry.isInCircle(mLakeRadius - mDuck.getX(), mLakeRadius - mDuck.getY(), mLakeRadius);
        if(!inCircle) {
            mDuck.setFreed(true);
            mDuck.setActive(false);
            invalidate();
        }
    }

    public void reset() {
        initAnimals();
        invalidate();
    }

    private void invalidateInUi() {
        post(mActionInvalidate);
    }
}
