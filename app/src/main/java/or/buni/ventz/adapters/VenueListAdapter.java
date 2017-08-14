package or.buni.ventz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import or.buni.ventz.R;
import or.buni.ventz.objects.VenueObject;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class VenueListAdapter extends RecyclerView.Adapter<VenueListAdapter.ViewHolder> {

    private ArrayList<VenueObject> venues;
    private Context context;

    public VenueListAdapter(Context context) {
        this.venues = new ArrayList<>();
        this.context = context;

        /*VenueObject venue = new VenueObject("xdewP","Impala Halls","1 - 3 Mil","0","500","Kijitonyama");
        venue.setVenueImage(R.drawable.ven_1);

        VenueObject venue2 = new VenueObject("xdewo","Primerose","2 - 3.5 Mil","0","1500","Mbezi");
        venue2.setVenueImage(R.drawable.ven_2);

        VenueObject venue3 = new VenueObject("xdewo","Buni Halls","500k - 3.5 Mil","0","100","Sayansi");
        venue3.setVenueImage(R.drawable.ven_1);

        this.venues.add(venue);
        this.venues.add(venue2);
        this.venues.add(venue3);*/

        this.notifyDataSetChanged();
    }

    public VenueListAdapter(ArrayList<VenueObject> venues) {
        this.venues = venues;
    }

    public void addVenues(ArrayList<VenueObject> venues) {
        this.venues.addAll(venues);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View venues = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item, parent, false);


        return new ViewHolder(venues);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VenueObject venue = venues.get(position);
        DecimalFormat format = new DecimalFormat("#,###");

        Float price = Float.parseFloat(venue.getPriceFrom());

        holder.name.setText(venue.getName());
        holder.price.setText(venue.getPriceFrom());
        holder.location.setText(venue.getLocationDesc());
        holder.price.setText(format.format(price));

        if (!venue.getPreviewImage().equals("")) {

            Glide.with(context).load(venue.getPreviewImage()).into(holder.venueImage);
        }
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, location;
        ImageView venueImage;

        private ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.venueName);
            price = itemView.findViewById(R.id.priceRange);
            location = itemView.findViewById(R.id.location);
            venueImage = itemView.findViewById(R.id.venueImage);
        }
    }

}
