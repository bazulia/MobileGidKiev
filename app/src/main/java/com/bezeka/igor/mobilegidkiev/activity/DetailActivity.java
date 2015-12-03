package com.bezeka.igor.mobilegidkiev.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bezeka.igor.mobilegidkiev.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Igor on 25.11.2015.
 */
public class DetailActivity extends AppCompatActivity {

    ImageView img;
    TextView title;
    TextView tvAddress;
    Button btnSeeOnMap;
    RatingBar rating;

    ExpandableTextView expTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ActionBar bar = getSupportActionBar();
        bar.hide();

        img = (ImageView) findViewById(R.id.imgDetail);
        title = (TextView) findViewById(R.id.titleDesc);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        rating = (RatingBar) findViewById(R.id.ratingbarDesc);

        btnSeeOnMap = (Button) findViewById(R.id.btnSeeOnMap);


        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view_description);


        Intent intent = getIntent();
        String imgLink = intent.getStringExtra("img");
        String titleText = intent.getStringExtra("title");
        String descriptionText = intent.getStringExtra("description");
        final String addressText = intent.getStringExtra("address");
        float ratingFloat = intent.getFloatExtra("rating",4);

        btnSeeOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MapActivity.class);
                intent.putExtra("address", addressText);
                startActivity(intent);
            }
        });

        Picasso.with(getApplicationContext())
                .load(imgLink)
                .into(img);

        title.setText(titleText);
        //description.setText(descriptionText);
        expTv1.setText(descriptionText);
        rating.setRating(ratingFloat);
        tvAddress.setText(addressText);
    }
}
