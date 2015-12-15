package com.bezeka.igor.mobilegidkiev.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.adapter.PlacesAdapter;
import com.bezeka.igor.mobilegidkiev.app.AppConfig;
import com.bezeka.igor.mobilegidkiev.app.AppController;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.AuthDialogFragment;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.NoConnectionDialogFragment;
import com.bezeka.igor.mobilegidkiev.helper.Checker;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;
import com.bezeka.igor.mobilegidkiev.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NoConnectionDialogFragment.CheckInternetInterface {

    public static final String TAG = MainActivity.class.getSimpleName();

    double lat;
    double lng;
    String address;

    private Menu menu;

    private GoogleMap mMap;

    private EditText etSearch;

    private RecyclerView rvPlaces;
    private PlacesAdapter adapterP;
    private RecyclerView.LayoutManager manager;
    private ArrayList<Place> places;

    private SessionManager session;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));

        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        places = new ArrayList<>();


        rvPlaces = (RecyclerView) findViewById(R.id.rvResplaces);

        etSearch = (EditText) findViewById(R.id.etSearch);

        adapterP = new PlacesAdapter(getApplicationContext(), places);

        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvPlaces.setLayoutManager(manager);

        rvPlaces.setAdapter(adapterP);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap))
                .getMap();
        mMap.setMyLocationEnabled(true);

        if (Checker.checkInternetConnection(getApplicationContext())) {
            getPlaces();
        } else {
            Checker.showCheckInternetDialog(this);
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterP.getFilter().filter(s.toString());
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    return true;
                } else {

                    return false;
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_auth);
        if (session.isLoggedIn()) {
            item.setTitle(R.string.cabinet);
        } else {
            item.setTitle(R.string.auth);
        }

        this.menu = menu;

        return true;
    }

    public void updateMenuTitles() {
        MenuItem item = menu.findItem(R.id.action_auth);
        if (session.isLoggedIn()) {
            item.setTitle(R.string.cabinet);
        } else {
            item.setTitle(R.string.auth);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                if(etSearch.getVisibility() != View.VISIBLE) {
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
            } else {
                    etSearch.setVisibility(View.GONE);
            }
            break;
            case R.id.action_sort_by_alpha:
                hideKeyboard();
                etSearch.setVisibility(View.GONE);
                adapterP.sortByAlphabet();
                break;
            case R.id.action_sort_by_rating:
                hideKeyboard();
                etSearch.setVisibility(View.GONE);
                adapterP.sortByRating();
                break;
            case R.id.action_sort_by_distance:
                hideKeyboard();
                etSearch.setVisibility(View.GONE);
                adapterP.sortByDistance();
                adapterP.notifyDataSetChanged();
                break;
            case R.id.action_auth:
                hideKeyboard();
                etSearch.setVisibility(View.GONE);
                if (Checker.checkInternetConnection(getApplicationContext())) {
                    AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("isSendComment", false);
                    authDialogFragment.setArguments(args);
                    authDialogFragment.show(getSupportFragmentManager(), AuthDialogFragment.class.getSimpleName());
                } else {
                    Checker.showCheckInternetDialog(this);
                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }


    private void getPlaces() {
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

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);

                        String id = object.getString("id");
                        String title = object.getString("title");
                        String description = object.getString("description");
                        String address = object.getString("address");
                        String link_image = object.getString("link_image");
                        String work_time = object.getString("work_time");
                        String type = object.getString("name");
                        String region = object.getString("region");

                        float minX = 1.0f;
                        float maxX = 5.0f;

                        Random rand = new Random();

                        float rating = rand.nextFloat() * (maxX - minX) + minX;

                        getLatLongFromGivenAddress(address);

                        double curLat;
                        double curLng;

//                        curLat = mMap.getMyLocation().getLatitude();
//                        curLng = mMap.getMyLocation().getLongitude();
//
//                        LatLng curLatLng = new LatLng(curLat, curLng);
//                        LatLng findedLatLng = new LatLng(lat, lng);


                        double distance = 243;//CalculationByDistance(curLatLng, findedLatLng);

                        Place place = new Place(object);
                        place.setDistance(distance);
                        places.add(place);

                        rvPlaces.setAdapter(adapterP);

                        adapterP.getFilter().filter("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Places Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        R.string.sry_cant_load_data_from_server, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_PLACES, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menu!=null )
        updateMenuTitles();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void getLatLongFromGivenAddress(String youraddress) {
//        try {
//            youraddress = URLEncoder.encode(youraddress, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                youraddress + "&sensor=false&language=ru";


        OkHttpClient client = new OkHttpClient();
        String stringResponce = null;

        try {
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(uri)
                    .build();

            com.squareup.okhttp.Response response = client.newCall(request).execute();

            stringResponce = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (stringResponce != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringResponce);
                lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", lat + "");
                Log.d("longitude", lng + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);


        return meterInDec;
    }

    @Override
    public void onGetResult(boolean connect) {
        if (Checker.checkInternetConnection(getApplicationContext())) {
            getPlaces();
        } else {
            Checker.showCheckInternetDialog(this);
        }
    }


    public class LocationAsync extends AsyncTask {

        String mAddress;

        public LocationAsync(String mAddress) {
            this.mAddress = mAddress;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            getLatLongFromGivenAddress(mAddress);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }
}
