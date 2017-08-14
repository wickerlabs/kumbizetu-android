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
public class Payments extends Fragment {

    private static Payments instance;

    public Payments() {
        // Required empty public constructor
    }

    public static Payments getInstance() {
        if (instance == null) {
            instance = new Payments();
            return instance;
        } else {
            return instance;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payments, container, false);
    }

}
