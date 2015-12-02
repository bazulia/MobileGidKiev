package com.bezeka.igor.mobilegidkiev.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bezeka.igor.mobilegidkiev.model.Place;
import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.activity.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Igor on 25.11.2015.
 */
public class PacesAdapter extends RecyclerView.Adapter<PacesAdapter.ViewHolder>{

    private ArrayList<Place> places = new ArrayList<>();
    private Context context;

    public PacesAdapter(Context context, ArrayList<Place> places) {
        this.places = places;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView img;
        private RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            img = (ImageView) itemView.findViewById(R.id.img);
            rating = (RatingBar) itemView.findViewById(R.id.ratingbar);
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
                Place place = places.get((holder.getPosition()));
                Intent intent = new Intent(context,DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title",place.getTitle());
                intent.putExtra("description",place.getDescription());
                intent.putExtra("img",place.getImgLink());
                intent.putExtra("rating",place.getRating());
                context.startActivity(intent);
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        Place place = places.get(i);
        viewHolder.title.setText(place.getTitle());
        viewHolder.description.setText(place.getDescription());
        viewHolder.rating.setRating(new Random().nextInt(5));
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
        return places.size();
    }


}
