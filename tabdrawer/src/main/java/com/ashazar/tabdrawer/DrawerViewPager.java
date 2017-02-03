package com.ashazar.tabdrawer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Serdar Hazar on 2/1/17.
 */

public class DrawerViewPager extends ViewPager {
    boolean scrollHorizontal;

    public DrawerViewPager(Context context) {
        super(context);
    }

    public void setScrollDirection(boolean horizontal) {
        scrollHorizontal = horizontal;
        if (!horizontal) {
            setPageTransformer(true, new VerticalPageTransformer());
            setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(scrollHorizontal ? ev : swapXY(ev));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollHorizontal) {
            boolean intercepted = super.onInterceptHoverEvent(swapXY(ev));
            swapXY(ev);
            return intercepted;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private MotionEvent swapXY(MotionEvent event) {
        //Log.d("--ASH", "width: " + getWidth() + " - Height: " + getHeight());
        float width = getWidth();
        float height = getHeight();

        float newX = (event.getY() / height) * width;
        float newY = (event.getX() / width) * height;

        Log.d("--ASH", "x: " + event.getX() + " - y: " + event.getY());
        Log.d("--ASH", "newX: " + newX + " - newY: " + newY);


        event.setLocation(newX, newY);
        return event;
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            if (position < -1) {
                page.setAlpha(0);
            }
            else if (position <= 1) {
                page.setAlpha(1);
                page.setTranslationX(page.getWidth() * -position);
                float yPosition = position * page.getHeight();
                page.setTranslationY(yPosition);
            }
            else {
                page.setAlpha(0);
            }
        }
    }
}
