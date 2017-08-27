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
public class Favorites extends Fragment {

    private static Favorites instance;

    public Favorites() {
        // Required empty public constructor
    }

    public static Favorites getInstance() {
        if (instance == null) {
            instance = new Favorites();
            return instance;
        } else {
            return instance;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

}
