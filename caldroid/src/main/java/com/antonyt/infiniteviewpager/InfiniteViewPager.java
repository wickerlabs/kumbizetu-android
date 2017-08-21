package com.antonyt.infiniteviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hirondelle.date4j.DateTime;

import java.util.ArrayList;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around
 * effect. Should be used with an {@link InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends ViewPager {

    // ******* Declaration *********
    public static final int OFFSET = 1000;

    /**
     * datesInMonth is required to calculate the height correctly
     */
    private ArrayList<DateTime> datesInMonth;

    /**
     * Enable swipe
     */
    private boolean enabled = true;

    /**
     * A calendar height is not fixed, it may have 4, 5 or 6 rows. Set
     * fitAllMonths to true so that the calendar will always have 6 rows
     */
    private boolean sixWeeksInCalendar = false;

    /**
     * Use internally to decide height of the calendar
     */
    private int rowHeight = 0;
    private boolean swipeRightEnable = true, swipeLeftEnable = true;
    private float initialXValue;

    // ************** Constructors ********************
    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfiniteViewPager(Context context) {
        super(context);
    }

    // ******* Setter and getters *********
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSixWeeksInCalendar() {
        return sixWeeksInCalendar;
    }

    public void setSixWeeksInCalendar(boolean sixWeeksInCalendar) {
        this.sixWeeksInCalendar = sixWeeksInCalendar;
        rowHeight = 0;
    }

    public ArrayList<DateTime> getDatesInMonth() {
        return datesInMonth;
    }

    public void setDatesInMonth(ArrayList<DateTime> datesInMonth) {
        this.datesInMonth = datesInMonth;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        // offset first element so that we can scroll to the left
        setCurrentItem(OFFSET);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enabled && this.handleSwipe(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (enabled && this.handleSwipe(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean handleSwipe(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0) {
                    // swipe from left to right detected
                    return isSwipeLeftEnable();
                } else if (diffX < 0) {
                    // swipe from right to left detected
                    return isSwipeRightEnable();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

    /**
     * ViewPager does not respect "wrap_content". The code below tries to
     * measure the height of the child and set the height of viewpager based on
     * child height
     * <p>
     * It was customized from
     * http://stackoverflow.com/questions/9313554/measuring-a-viewpager
     * <p>
     * Thanks Delyan for his brilliant code
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Calculate row height
        int rows = datesInMonth.size() / 7;

        if (getChildCount() > 0 && rowHeight == 0) {
            View firstChild = getChildAt(0);
            int width = getMeasuredWidth();

            // Use the previously measured width but simplify the calculations
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.EXACTLY);


            firstChild.measure(widthMeasureSpec, MeasureSpec
                    .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            rowHeight = firstChild.getMeasuredHeight();
        }

        // Calculate height of the calendar
        int calHeight;

        // If fit 6 weeks, we need 6 rows
        if (sixWeeksInCalendar) {
            calHeight = rowHeight * 6;
        } else { // Otherwise we return correct number of rows
            calHeight = rowHeight * rows;
        }

        // Prevent small vertical scroll
        calHeight -= 12;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(calHeight,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isSwipeRightEnable() {
        return swipeRightEnable;
    }

    public void setSwipeRightEnable(boolean swipeRightEnable) {
        this.swipeRightEnable = swipeRightEnable;
    }

    public boolean isSwipeLeftEnable() {
        return swipeLeftEnable;
    }

    public void setSwipeLeftEnable(boolean swipeLeftEnable) {
        this.swipeLeftEnable = swipeLeftEnable;
    }
}
