package com.bezeka.igor.mobilegidkiev;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Igor on 25.11.2015.
 */
public class DetailActivity extends AppCompatActivity {

    ImageView img;
    TextView title;
    TextView description;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ActionBar bar = getSupportActionBar();
        bar.hide();

        img = (ImageView) findViewById(R.id.imgDetail);
        title = (TextView) findViewById(R.id.titleDesc);
        description = (TextView) findViewById(R.id.descriptionDesc);
        rating = (RatingBar) findViewById(R.id.ratingbarDesc);

        Intent intent = getIntent();
        String imgLink = intent.getStringExtra("img");
        String titleText = intent.getStringExtra("title");
        String descriptionText = intent.getStringExtra("description");
        float ratingFloat = intent.getFloatExtra("rating",4);

        Picasso.with(getApplicationContext())
                .load(imgLink)
                .into(img);

        title.setText(titleText);
        description.setText(descriptionText);
        rating.setRating(ratingFloat);
    }
}
