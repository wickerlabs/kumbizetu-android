package or.buni.ventz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import or.buni.ventz.R;
import or.buni.ventz.interfaces.ItemClickListener;
import or.buni.ventz.objects.VenueObject;
import or.buni.ventz.util.Formatter;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class VenueListAdapter extends RecyclerView.Adapter<VenueListAdapter.ViewHolder> {

    private ArrayList<VenueObject> venues;
    private Context context;
    private ItemClickListener clickListener;

    public VenueListAdapter(Context context, ItemClickListener listener) {
        this.venues = new ArrayList<>();
        this.context = context;
        this.clickListener = listener;
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

        holder.name.setText(venue.getName());
        holder.price.setText(venue.getPriceFrom());
        holder.location.setText(venue.getLocationDesc());
        holder.price.setText(Formatter.shortenMoney(Float.parseFloat(venue.getPriceFrom())));

        if (!venue.getPreviewImage().equals("")) {
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            options.skipMemoryCache(true);
            options.centerCrop();
            options.sizeMultiplier(0.8f);

            Glide.with(context)
                    .load(venue.getPreviewImage())
                    .apply(options)
                    .into(holder.venueImage);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(venues.get(getAdapterPosition()));
                }
            });
        }
    }

}
