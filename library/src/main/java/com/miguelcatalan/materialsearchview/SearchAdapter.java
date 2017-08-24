package com.miguelcatalan.materialsearchview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Suggestions Adapter.
 *
 * @author Miguel Catalan Bañuls
 */
public class SearchAdapter extends ArrayAdapter<Suggestion> implements Filterable {

    private ArrayList<Suggestion> data;
    private ArrayList<Suggestion> suggestions;
    private LayoutInflater inflater;
    private boolean ellipsize;
    private int layoutId;


    public SearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Suggestion> objects, boolean ellipsize) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = objects;
        this.ellipsize = ellipsize;
        this.layoutId = resource;

    }

    public void addSuggestions(ArrayList<Suggestion> list) {
        this.suggestions = list;
        //notifyDataSetChanged();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Suggestion> collection) {
        super.addAll(collection);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<Suggestion> searchData = new ArrayList<>();

                    for (Suggestion suggestion : suggestions) {

                        boolean name = suggestion.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                        boolean location = suggestion.getLocation().toLowerCase().contains(constraint.toString().toLowerCase());

                        if (name || location) {
                            searchData.add(suggestion);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<Suggestion>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Suggestion getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        Suggestion currentListData = getItem(position);

        viewHolder.textView.setText(currentListData.getName());
        viewHolder.textView2.setText(currentListData.getLocation());
        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.textView2.setSingleLine();
            viewHolder.textView2.setEllipsize(TextUtils.TruncateAt.END);
        }

        return convertView;
    }

    private class SuggestionsViewHolder {

        TextView textView, textView2;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
            textView2 = (TextView) convertView.findViewById(R.id.locationText);

        }
    }
}