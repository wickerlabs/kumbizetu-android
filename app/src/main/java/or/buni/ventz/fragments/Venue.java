package or.buni.ventz.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import or.buni.ventz.R;
import or.buni.ventz.adapters.VenueListAdapter;
import or.buni.ventz.interfaces.GetCallback;
import or.buni.ventz.networking.Backend;
import or.buni.ventz.objects.VenueObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Venue extends Fragment {

    private static Venue instance;
    private VenueListAdapter adapter;

    public Venue() {
        // Required empty public constructor
    }

    public static Venue getInstance() {
        if (instance == null) {
            instance = new Venue();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.venueList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new VenueListAdapter(getContext());
        recyclerView.setAdapter(adapter);

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading, please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Backend.getInstance().getVenues(new GetCallback() {
            @Override
            public void onComplete(ArrayList<VenueObject> venues, Exception e) {
                if (e == null) {
                    adapter.addVenues(venues);
                    dialog.dismiss();
                } else {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });


        return view;

    }


}
