package dev.rism.dobscraper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by risha on 23-05-2017.
 */

public class PresentationViewPager extends ViewPager {

    public static final int DEFAULT_SCROLL_DURATION = 250;
    public static final int PRESENTATION_MODE_SCROLL_DURATION = 1000;

    public PresentationViewPager (Context context) {
        super(context);
    }

    public PresentationViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDurationScroll(int millis) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new OwnScroller(getContext(), millis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OwnScroller extends Scroller {

        private int durationScrollMillis = 1;

        public OwnScroller(Context context, int durationScroll) {
            super(context, new DecelerateInterpolator());
            this.durationScrollMillis = durationScroll;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis);
        }
    }

}

