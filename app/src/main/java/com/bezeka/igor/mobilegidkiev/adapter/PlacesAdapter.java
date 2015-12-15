package com.bezeka.igor.mobilegidkiev.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.activity.DetailActivity;
import com.bezeka.igor.mobilegidkiev.model.Place;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Igor on 25.11.2015.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> implements Filterable{

    private ArrayList<Place> places = new ArrayList<>();
    private ArrayList<Place> filteredPlaces;
    private Context context;

    public PlacesAdapter(Context context, ArrayList<Place> places) {
        this.places = places;
        this.context = context;
        this.filteredPlaces = new ArrayList<>();
    }

    @Override
    public Filter getFilter() {
        return new PlacesFilter(this,places);
    }

    public static class PlacesFilter extends Filter{

        PlacesAdapter placesAdapter;

        ArrayList<Place> originalPlaces;
        ArrayList<Place> filteredPlaces;

        public PlacesFilter(PlacesAdapter adapter,ArrayList<Place> originalPlaces){
            super();
            this.placesAdapter = adapter;
            this.originalPlaces = new ArrayList<>(originalPlaces);
            this.filteredPlaces = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            filteredPlaces.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredPlaces.addAll(originalPlaces);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Place place : originalPlaces) {
                    if (place.getTitle().toLowerCase().contains(constraint)
                            || place.getAddress().toLowerCase().contains(constraint)
                            || place.getAddress().contains(constraint)
                            || place.getTitle().contains(constraint)) {
                        filteredPlaces.add(place);
                    }
                }
            }
            results.values = filteredPlaces;
            results.count = filteredPlaces.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            placesAdapter.filteredPlaces.clear();
            placesAdapter.filteredPlaces.addAll((ArrayList<Place>) results.values);
            placesAdapter.notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView img;
        private RatingBar rating;
        private TextView tvDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            img = (ImageView) itemView.findViewById(R.id.img);
            rating = (RatingBar) itemView.findViewById(R.id.ratingbar);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        }
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        final ViewHolder holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place place = filteredPlaces.get((holder.getPosition()));
                Intent intent = new Intent(context,DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("placeId", place.getId());
                intent.putExtra("title",place.getTitle());
                intent.putExtra("description",place.getDescription());
                intent.putExtra("img",place.getImgLink());
                intent.putExtra("rating",place.getRating());
                intent.putExtra("address",place.getAddress());
                intent.putExtra("distance",place.getDistance());
                context.startActivity(intent);
            }
        });

        return holder;
    }

    public void sortByRating(){
        Collections.sort(filteredPlaces,new CustomComparatorRating());
        this.notifyDataSetChanged();
    }

    public void sortByAlphabet(){
        Collections.sort(filteredPlaces,new CustomComparatorAlphabet());
        this.notifyDataSetChanged();
    }

    public void sortByDistance(){
        Collections.sort(filteredPlaces,new CustomComparatorAlphabet());
        this.notifyDataSetChanged();
    }

    public class CustomComparatorAlphabet implements Comparator<Place> {
        @Override
        public int compare(Place p1, Place p2) {
            return p2.getTitle().compareTo(p1.getTitle());
        }
    }

    public class CustomComparatorRating implements Comparator<Place> {
        @Override
        public int compare(Place p1, Place p2) {
            if(p2.getRating() < p1.getRating())
                return -1;
            else
                return 1;
        }
    }

    public class CustomComparatorDistance implements Comparator<Place> {
        @Override
        public int compare(Place p1, Place p2) {
            if(p2.getDistance() > p1.getDistance())
                return -1;
            else
                return 1;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        Place place = filteredPlaces.get(i);
        viewHolder.title.setText(place.getTitle());
        viewHolder.description.setText(place.getDescription());
        viewHolder.rating.setRating(place.getRating());
        viewHolder.tvDistance.setText(place.getDistance()+"");
        Picasso.with(context)
                .load(place.getImgLink())
                .into(viewHolder.img);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }


    @Override
    public int getItemCount() {
        return filteredPlaces.size();
    }


}