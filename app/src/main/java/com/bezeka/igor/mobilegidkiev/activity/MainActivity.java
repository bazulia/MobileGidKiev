package com.bezeka.igor.mobilegidkiev.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezeka.igor.mobilegidkiev.adapter.PacesAdapter;
import com.bezeka.igor.mobilegidkiev.model.Place;
import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.app.AppConfig;
import com.bezeka.igor.mobilegidkiev.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvPlaces;
    private RecyclerView.Adapter adapterP;
    private RecyclerView.LayoutManager manager;
    private ArrayList<Place> places;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        places = new ArrayList<>();


        rvPlaces = (RecyclerView) findViewById(R.id.rvResplaces);

        adapterP = new PacesAdapter(getApplicationContext(),places);

        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);

        rvPlaces.setLayoutManager(manager);

        rvPlaces.setAdapter(adapterP);

        getPlaces();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    private void getPlaces(){
        String tag_string_req = "get_places";

        pDialog.setMessage("Завантаження закладів");
        showDialog();

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Places Responce: " + responce);

                responce = responce.substring(responce.indexOf("["));

                hideDialog();
                try {

                    places.clear();

                    JSONArray array = new JSONArray(responce);

                    for(int i = 0; i < array.length();i++){
                        JSONObject object = (JSONObject)array.get(i);

                        String id = object.getString("id");
                        String title = object.getString("title");
                        String description = object.getString("description");
                        String address = object.getString("address");
                        String link_image = object.getString("link_image");
                        String work_time = object.getString("work_time");
                        String type = object.getString("type");
                        String region = object.getString("region");
                        float rating = object.getInt("rating");

                        places.add(new Place(id,title,description,link_image,rating,address,work_time,type,region));


                        rvPlaces.setAdapter(adapterP);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Login Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_PLACES, listener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

}
