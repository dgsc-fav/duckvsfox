package com.github.dgsc_fav.duckvsfox.duckvsfox.util;

import java.util.Random;

public class Geometry {

    public static float distance(float x1, float y1, float x2, float y2) {
        final float xAbs = Math.abs(x1 - x2);
        final float yAbs = Math.abs(y1 - y2);
        return (float) Math.sqrt((yAbs * yAbs) + (xAbs * xAbs));
    }

    public static boolean isInCircle(float x1, float y1, float r) {
        final float xAbs = Math.abs(x1);
        final float yAbs = Math.abs(y1);
        return Math.pow(xAbs, 2) + Math.pow(yAbs, 2) < Math.pow(r, 2);
    }

    public static boolean isIntercept(float x1, float y1, float x2, float y2, float treshold) {
        return distance(x1, y1, x2, y2) < treshold;
    }

    public static float[] getRandomXyOnCircle(float r) {
        Random random = new Random();
        // [0..1] * 3.14
        float angle = (float) (random.nextFloat() * 2 * Math.PI);
        return getXyOnCircle(angle, r);
    }

    /**
     *
     * @param angle [0..PI]
     * @param r
     * @return
     */
    public static float[] getXyOnCircle(float angle, float r) {
        float[] xy = new float[2];
        xy[0] = (float) (Math.cos(-angle) * r);// "-" ибо угол у них тут считается со II части координат
        xy[1] = (float) (Math.sin(-angle) * r);
        return xy;
    }

    public static float getXByAngle(float angle, float r) {
        return (float) (Math.cos(angle) * r);
    }

    public static float getYByAngle(float angle, float r) {
        return (float) (Math.sin(angle) * r);
    }

    public static float[] getMinimalDistanceOnCircle(float xT, float yT, float xH, float yH, float r) {
        float angle = getCorrectedAngle(xT, yT);

        return getXyOnCircle(angle, r);
    }

    public static float getCorrectedAngle(float x, float y) {
        float angle = (float) Math.atan2(y, x);

        if(angle < 0) {
            angle = (float) (2 * Math.PI - Math.abs(angle));
        }
        return angle;
    }

    public static float getAngle(float x, float y) {
        return (float) Math.atan2(y, x);
    }

    public static float getNextXByAngle(float prevX, float angle, float length) {
        return prevX + getXByAngle(angle, length);
    }

    public static float getNextYByAngle(float prevY, float angle, float length) {
        return prevY + getYByAngle(angle, length);
    }
}
