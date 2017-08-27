package or.buni.bukit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import or.buni.ventz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bookings extends Fragment {

    private static Bookings instance;

    public Bookings() {
        // Required empty public constructor
    }

    public static Bookings getInstance() {
        if (instance == null) {
            instance = new Bookings();
            return instance;
        } else {
            return instance;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

}
