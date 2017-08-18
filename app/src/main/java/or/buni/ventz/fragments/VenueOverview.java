package or.buni.ventz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import or.buni.ventz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueOverview extends Fragment {

    private static VenueOverview instance;

    public VenueOverview() {
        // Required empty public constructor
    }

    public static VenueOverview getInstance() {
        if (instance == null) {
            instance = new VenueOverview();
        }

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_venue_overview, container, false);
    }

}
