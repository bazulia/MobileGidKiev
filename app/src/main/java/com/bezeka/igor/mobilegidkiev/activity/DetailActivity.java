package com.bezeka.igor.mobilegidkiev.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.AuthDialogFragment;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.SendCommentDialogFragment;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;
import com.bezeka.igor.mobilegidkiev.model.Comment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Igor on 25.11.2015.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = DetailActivity.class.getSimpleName();

    private static final int REQUEST_LOGIN = 1662;
    private static final int REQUEST_REGISTRATION = 8823;
    private static final int REQUEST_SEND_COMMENT = 8824;
    private static final int REQUEST_AUTH = 8825;

    ImageView img;
    TextView title;
    TextView tvAddress;
    TextView tvNoComments;
    ImageView imgSeeOnMap;
    RatingBar rating;

    SessionManager session;

    Button btnSendComment;

    ArrayList<Comment> comments = new ArrayList<>();

    String placeId;
    String imgLink;
    String titleText;
    String descriptionText;
    String addressText;
    float ratingFloat;

    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        mImageView = findViewById(R.id.imgDetail);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setVisibility(View.GONE);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.action_bar)));

        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.action_bar));

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

        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();

        placeId = intent.getStringExtra("placeId");
        imgLink = intent.getStringExtra("img");
        titleText = intent.getStringExtra("title");
        descriptionText = intent.getStringExtra("description");
        addressText = intent.getStringExtra("address");
        ratingFloat = intent.getFloatExtra("rating", 4);


        Picasso.with(getApplicationContext())
                .load(imgLink)
                .into(img);
        getSupportActionBar().setTitle(Html.fromHtml("<p><small>"+titleText+"</small></p>"));
        //title.setText(titleText);
        title.setVisibility(View.GONE);
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
                if(session.isLoggedIn()){
                    SendCommentDialogFragment sendCommentDialog = new SendCommentDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("placeId",placeId);
                    sendCommentDialog.setArguments(args);
                    sendCommentDialog.show(getSupportFragmentManager(),SendCommentDialogFragment.class.getSimpleName());
                } else {
                    AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("isSendComment",true);
                    authDialogFragment.setArguments(args);
                    authDialogFragment.show(getSupportFragmentManager(),AuthDialogFragment.class.getSimpleName());
                }

                break;
        }
    }

}
