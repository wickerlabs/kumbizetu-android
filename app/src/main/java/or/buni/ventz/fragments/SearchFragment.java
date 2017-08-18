package or.buni.ventz.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;

import io.apptik.widget.MultiSlider;
import or.buni.ventz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends AAH_FabulousFragment {


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.search_layout, null);
        RelativeLayout contentLay = contentView.findViewById(R.id.searchView);
        LinearLayout ll_buttons = contentView.findViewById(R.id.buttons_ll);


        setAnimationDuration(600);
        setPeekHeight(800);

        setViewgroupStatic(ll_buttons);
        setViewMain(contentLay);
        setMainContentView(contentView);

        super.setupDialog(dialog, R.style.AppTheme);
    }

    public void close(View view) {
        closeFilter("closed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_layout, container, false);

        ImageButton closeBtn = view.findViewById(R.id.closeSearch);
        ImageButton submitBtn = view.findViewById(R.id.submitSearch);

        final TextView rangeStart = view.findViewById(R.id.textView3);
        final TextView rangeEnd = view.findViewById(R.id.textView4);

        MultiSlider slider = view.findViewById(R.id.multi);
        slider.setDrawThumbsApart(true);
        slider.setMin(100000);
        slider.setMax(5000000);
        slider.setStep(500000);
        slider.setStepsThumbsApart(2);
        slider.getThumb(0).setValue(100000);
        slider.getThumb(1).setValue(5000000);


        slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    rangeStart.setText(String.valueOf(value - 100));
                } else {
                    rangeEnd.setText(String.valueOf(value));
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("[+] X Button", "Clicked");

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Soon", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
