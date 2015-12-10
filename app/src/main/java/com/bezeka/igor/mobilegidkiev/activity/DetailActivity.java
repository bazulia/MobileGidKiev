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
import com.bezeka.igor.mobilegidkiev.dialog_fragment.SendCommentDialogFragment;
import com.bezeka.igor.mobilegidkiev.model.Comment;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Igor on 25.11.2015.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    TextView title;
    TextView tvAddress;
    TextView tvNoComments;
    ImageView imgSeeOnMap;
    RatingBar rating;

    Button btnSendComment;

    ArrayList<Comment> comments = new ArrayList<>();

    String imgLink;
    String titleText;
    String descriptionText;
    String addressText;
    float ratingFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ActionBar bar = getSupportActionBar();
        bar.hide();

        img = (ImageView) findViewById(R.id.imgDetail);
        title = (TextView) findViewById(R.id.titleDesc);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvNoComments = (TextView) findViewById(R.id.tvNoComments);
        rating = (RatingBar) findViewById(R.id.ratingbarDesc);

        imgSeeOnMap = (ImageView) findViewById(R.id.imgSeeOnMap);
        imgSeeOnMap.setOnClickListener(this);

        btnSendComment = (Button) findViewById(R.id.btnSentComment);
        btnSendComment.setOnClickListener(this);


        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view_description);


        Intent intent = getIntent();
        imgLink = intent.getStringExtra("img");
        titleText = intent.getStringExtra("title");
        descriptionText = intent.getStringExtra("description");
        addressText = intent.getStringExtra("address");
        ratingFloat = intent.getFloatExtra("rating", 4);


        Picasso.with(getApplicationContext())
                .load(imgLink)
                .into(img);

        title.setText(titleText);
        expTv1.setText(descriptionText);
        rating.setRating(ratingFloat);
        tvAddress.setText(addressText);

        if(comments.isEmpty())
        {
            tvNoComments.setVisibility(View.VISIBLE);
        }else{
            makeCommentsBody();
        }
    }

    private void makeCommentsBody(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSeeOnMap:
                Intent intent = new Intent(DetailActivity.this,MapActivity.class);
                intent.putExtra("address", addressText);
                startActivity(intent);
                break;
            case R.id.btnSentComment:
                SendCommentDialogFragment sendCommentDialog = new SendCommentDialogFragment();
                sendCommentDialog.show(getSupportFragmentManager(),SendCommentDialogFragment.class.getSimpleName());
                break;
        }
    }
}
