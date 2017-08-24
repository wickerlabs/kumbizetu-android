package or.buni.ventz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import or.buni.ventz.R;
import or.buni.ventz.interfaces.ItemClickListener;
import or.buni.ventz.objects.EventType;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private ArrayList<EventType> events;
    private Context context;
    private ItemClickListener clickListener;

    public BrowseAdapter(Context context, ItemClickListener listener) {
        this.events = new ArrayList<>();
        this.context = context;
        this.clickListener = listener;
    }

    public BrowseAdapter(ArrayList<EventType> events) {
        this.events = events;
    }

    public void addEvents(ArrayList<EventType> events) {
        this.events.addAll(events);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View venues = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_hr_item, parent, false);


        return new ViewHolder(venues);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventType ev = events.get(position);


        holder.eventType.setText(ev.getName());

        if (!ev.getImageUrl().equals("")) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.useUnlimitedSourceGeneratorsPool(true);
            options.sizeMultiplier(0.75f);

            Glide.with(context)
                    .load(ev.getImageUrl())
                    .apply(options)
                    .into(holder.eventImage);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventType;
        ImageView eventImage;

        private ViewHolder(View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.eventName);
            eventImage = itemView.findViewById(R.id.eventImage);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(events.get(getAdapterPosition()));
                }
            });*/
        }
    }

}
