package com.bezeka.igor.mobilegidkiev.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.app.AppConfig;
import com.bezeka.igor.mobilegidkiev.app.AppController;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.AuthDialogFragment;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.NoConnectionDialogFragment;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.SendCommentDialogFragment;
import com.bezeka.igor.mobilegidkiev.helper.Checker;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;
import com.bezeka.igor.mobilegidkiev.interfaces.SendCommentInterfase;
import com.bezeka.igor.mobilegidkiev.model.Comment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 25.11.2015.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener, SendCommentInterfase, NoConnectionDialogFragment.CheckInternetInterface {

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
    RatingBar rbRating;

    Button btnSendComment;

    ArrayList<Comment> comments = new ArrayList<>();

    String placeId;
    String imgLink;
    String titleText;
    String descriptionText;
    String addressText;
    String commentsCount;
    float ratingFloat;

    private LinearLayout linCommentsLayout;

    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

    private TextView tvCommentsCount;
    private TextView tvRating;

    private SessionManager session;

    private ProgressBar pDialog;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.action_bar));

        img = (ImageView) findViewById(R.id.imgDetail);
        title = (TextView) findViewById(R.id.titleDesc);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvNoComments = (TextView) findViewById(R.id.tvNoComments);
        rbRating = (RatingBar) findViewById(R.id.ratingbarDesc);
        tvCommentsCount = (TextView) findViewById(R.id.tvRatingCount);
        tvRating = (TextView) findViewById(R.id.tvRatingAVGNum);


        linCommentsLayout = (LinearLayout) findViewById(R.id.linCommentsLayout);

        imgSeeOnMap = (ImageView) findViewById(R.id.imgSeeOnMap);
        imgSeeOnMap.setOnClickListener(this);

        btnSendComment = (Button) findViewById(R.id.btnSentComment);
        btnSendComment.setOnClickListener(this);

        pDialog = (ProgressBar) findViewById(R.id.pbProgress);

        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view_description);

        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();

        placeId = intent.getStringExtra("placeId");
        imgLink = intent.getStringExtra("img");
        titleText = intent.getStringExtra("title");
        descriptionText = intent.getStringExtra("description");
        addressText = intent.getStringExtra("address");
        ratingFloat = intent.getFloatExtra("rating",0);
        commentsCount = intent.getStringExtra("count_comments");


        Picasso.with(getApplicationContext())
                .load(imgLink)
                .into(img);
        getSupportActionBar().setTitle(Html.fromHtml("<p><small>" + titleText + "</small></p>"));
        title.setVisibility(View.GONE);
        expTv1.setText(descriptionText);
        rbRating.setRating(ratingFloat);
        tvAddress.setText(addressText);
        tvCommentsCount.setText("("+commentsCount+")");
        tvRating.setText(ratingFloat+"");


        if(Checker.checkInternetConnection(getApplicationContext())){
            getComments(placeId);
        } else {
            Checker.showCheckInternetDialog(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSeeOnMap:
                Intent intent = new Intent(DetailActivity.this, MapActivity.class);
                intent.putExtra("address", addressText);
                startActivity(intent);
                break;
            case R.id.btnSentComment:
                if (session.isLoggedIn()) {
                    SendCommentDialogFragment sendCommentDialog = new SendCommentDialogFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("isSendComment", true);
                    args.putString("placeId", placeId);
                    sendCommentDialog.setArguments(args);
                    sendCommentDialog.show(getSupportFragmentManager(), SendCommentDialogFragment.class.getSimpleName());
                } else {
                    AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("isSendComment", true);
                    args.putString("placeId",placeId);
                    authDialogFragment.setArguments(args);
                    authDialogFragment.show(getSupportFragmentManager(), AuthDialogFragment.class.getSimpleName());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_SEND_COMMENT) {
            if (Checker.checkInternetConnection(getApplicationContext())) {
                getComments(placeId);
            } else {
                Checker.showCheckInternetDialog(this);
            }
        }
    }

    public void getComments(final String placeId) {
        String tag_string_req = "sendComment";

        showDialog();

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Get Comments Responce: " + responce);

                hideDialog();
                try {

                    JSONArray array = new JSONArray(responce);

                    comments.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        comments.add(new Comment(object));
                    }

                    float titleTextSize = getResources().getDimension(R.dimen.comment_title_size);
                    float mainTextSize = getResources().getDimension(R.dimen.comment_text_size);

                    if (comments.isEmpty()) {
                        tvNoComments.setVisibility(View.VISIBLE);
                    } else {
                        tvNoComments.setVisibility(View.GONE);
                    }

                    if (((LinearLayout) linCommentsLayout).getChildCount() > 0)
                        ((LinearLayout) linCommentsLayout).removeAllViews();

                    for (Comment comment : comments) {

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        lp.setMargins(0, 0, 0, 20);

                        LinearLayout linContainer = (LinearLayout) findViewById(R.id.linCommentsLayout);

                        TextView tvTitle = new TextView(getApplicationContext());
                        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
                        tvTitle.setTextColor(getResources().getColor(android.R.color.black));
                        tvTitle.setText(comment.getName());


                        TextView tvText = new TextView(getApplicationContext());
                        tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mainTextSize);
                        tvText.setText(comment.getText());
                        tvText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        tvText.setBackgroundDrawable(getResources().getDrawable(R.drawable.comment_background));
                        tvText.setPadding(10, 10, 10, 10);

                        linContainer.addView(tvTitle, lp);
                        linContainer.addView(tvText, lp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Get Comments Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_COMMENTS, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("placeId", placeId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        pDialog.setVisibility(View.VISIBLE);
        linCommentsLayout.setVisibility(View.GONE);
    }

    private void hideDialog() {
        pDialog.setVisibility(View.GONE);
        linCommentsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishSendDialogFragment(boolean result) {
        if(Checker.checkInternetConnection(getApplicationContext())){
             getComments(placeId);
        } else {
            Checker.showCheckInternetDialog(this);
        }
    }


    @Override
    public void onGetResult(boolean connect) {

    }


}
