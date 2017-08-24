package or.buni.ventz.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import or.buni.ventz.R;
import or.buni.ventz.VenueDetailsActivity;
import or.buni.ventz.adapters.BrowseAdapter;
import or.buni.ventz.interfaces.GetEventCallback;
import or.buni.ventz.interfaces.ItemClickListener;
import or.buni.ventz.networking.Backend;
import or.buni.ventz.objects.EventType;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private static BrowseFragment instance;
    private BrowseAdapter adapter;
    private BrowseAdapter adapter2;
    private ProgressDialog dialog;


    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment getInstance() {
        if (instance == null) {
            instance = new BrowseFragment();
            return instance;
        } else {
            return instance;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        RecyclerView privateRecycler = view.findViewById(R.id.privateRecycler);
        RecyclerView cooperateRecycler = view.findViewById(R.id.cooperateRecycler);

        adapter = new BrowseAdapter(getContext(), new ItemClickListener() {
            @Override
            public void onClick(Object object) {
                EventType eventType = (EventType) object;
                Intent intent = new Intent(getContext(), VenueDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("json", eventType.getName());

                startActivity(intent);
                //Toast.makeText(getContext(), venue.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter2 = new BrowseAdapter(getContext(), new ItemClickListener() {
            @Override
            public void onClick(Object object) {
                EventType eventType = (EventType) object;
                Intent intent = new Intent(getContext(), VenueDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("json", eventType.getName());

                startActivity(intent);
                //Toast.makeText(getContext(), venue.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        privateRecycler.setAdapter(adapter);
        cooperateRecycler.setAdapter(adapter2);

        loadEvents(view);

        return view;
    }

    private void loadEvents(final View mView) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading, please wait...");
        dialog.setCanceledOnTouchOutside(false);
        //dialog.show();

        Backend.getInstance().getEvents(new GetEventCallback() {
            @Override
            public void onComplete(final ArrayList<EventType> events, final ArrayList<EventType> copEvents, Exception e) {
                if (e == null) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addEvents(events);
                            adapter2.addEvents(copEvents);
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
                                    loadEvents(mView);
                                }
                            }).setActionTextColor(getResources().getColor(R.color.coolYellow)).show();
                        }
                    });
                }
            }
        });
    }
}
