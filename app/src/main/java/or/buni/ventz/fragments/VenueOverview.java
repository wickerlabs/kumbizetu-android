package or.buni.ventz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import or.buni.ventz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueOverview extends Fragment {

    private static VenueOverview instance;
    String name, size, desc;
    private TextView description;

    public VenueOverview() {
        // Required empty public constructor
    }

    public static VenueOverview getInstance() {
        if (instance == null) {
            instance = new VenueOverview();
        }

        return instance;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_overview, container, false);

        description = view.findViewById(R.id.descriptionTxt);
        description.setText(desc);
        /*description.setText(name + " is a bright, " +
                "flexible space that can accommodate up to " + size + " people. " +
                "While it is commonly used for small conferences and seminars, " +
                "its proximity to the kitchen also makes " +
                "it perfect for small receptions and dinners.");*/

        return view;

    }


}
