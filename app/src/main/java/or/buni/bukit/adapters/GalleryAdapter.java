package or.buni.bukit.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import or.buni.ventz.R;

/**
 * Created by yusuphwickama on 9/6/17.
 */

public class GalleryAdapter extends ArrayAdapter<String> {

    private ArrayList<String> imageUrls;
    private Context mContext;
    private int layoutRes;

    public GalleryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.imageUrls = objects;
        this.layoutRes = resource;
    }

    public GalleryAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.mContext = context;
        this.imageUrls = new ArrayList<>();
        this.layoutRes = resource;
    }

    public void addImages(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void addImage(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return imageUrls.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);

        ImageView pic = view.findViewById(R.id.galleryItem);

        String imageUrl = imageUrls.get(position);

        if (!imageUrl.equals("")) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.useUnlimitedSourceGeneratorsPool(true);
            options.sizeMultiplier(0.75f);

            Glide.with(mContext)
                    .load(imageUrl)
                    .apply(options)
                    .into(pic);
        }

        return view;
    }

}
