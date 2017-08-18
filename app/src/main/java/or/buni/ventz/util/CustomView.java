package or.buni.ventz.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.allattentionhere.fabulousfilter.AAH_FilterView;

/**
 * Created by yusuphwickama on 8/15/17.
 */

public class CustomView extends AAH_FilterView {
    FrameLayout fl;
    FloatingActionButton fab;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init() {
        fl = new FrameLayout(getContext());
        fl.setTag("aah_fl");
        fab = new FloatingActionButton(getContext());
        fab.setTag("aah_fab");
        fab.setCompatElevation(0);
        fab.setVisibility(INVISIBLE);
        fl.addView(fab);
        this.addView(fl);
        //super.init();
    }
}
