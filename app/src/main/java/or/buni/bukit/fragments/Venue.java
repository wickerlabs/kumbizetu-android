package or.buni.bukit.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import or.buni.bukit.VenueDetailsActivity;
import or.buni.bukit.adapters.VenueListAdapter;
import or.buni.bukit.interfaces.GetVenueCallback;
import or.buni.bukit.interfaces.ItemClickListener;
import or.buni.bukit.networking.Backend;
import or.buni.bukit.objects.VenueObject;
import or.buni.ventz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Venue extends Fragment {

    private static Venue instance;
    private VenueListAdapter adapter;
    private ProgressDialog dialog;

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

        adapter = new VenueListAdapter(getContext(), new ItemClickListener() {
            @Override
            public void onClick(Object object) {
                VenueObject venue = (VenueObject) object;
                Intent intent = new Intent(getContext(), VenueDetailsActivity.class);
                intent.putExtra("json", venue.getVenueJSON());

                getActivity().startActivity(intent);
                //Toast.makeText(getContext(), venue.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        loadVenues(view);

        return view;

    }

    private void loadVenues(final View mView) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading, please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Intent intent = getActivity().getIntent();
        final String id = intent.getStringExtra("typeId");
        final String name = intent.getStringExtra("name");

        Backend.getInstance().getVenuesByEvent(id, new GetVenueCallback() {
            @Override
            public void onComplete(final ArrayList<VenueObject> venues, Exception e) {
                if (e == null) {

                    Log.d(getClass().getSimpleName(), "[+] Found -> " + String.valueOf(venues.size() + " in " + name + " with Id " + id));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addVenues(venues);
                            dialog.dismiss();
                        }
                    });

                } else {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            final Snackbar bar = Snackbar.make(mView, "Connection failed", Snackbar.LENGTH_INDEFINITE);
                            bar.setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bar.dismiss();
                                    loadVenues(mView);
                                }
                            }).setActionTextColor(getResources().getColor(R.color.coolYellow)).show();
                        }
                    });
                }
            }
        });
    }


}
