package com.bezeka.igor.mobilegidkiev.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.LinearLayout;
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
import com.bezeka.igor.mobilegidkiev.dialog_fragment.NoConnectionDialogFragment;
import com.bezeka.igor.mobilegidkiev.fragment.FragmentDrawer;
import com.bezeka.igor.mobilegidkiev.helper.Checker;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;
import com.bezeka.igor.mobilegidkiev.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NoConnectionDialogFragment.CheckInternetInterface,
        FragmentDrawer.FragmentDrawerListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<String> categories = new ArrayList<>();

    private FragmentDrawer.FragmentDrawerListener listener;

    double lat;
    double lng;
    String address;

    private Menu menu;

    private GoogleMap mMap;

    public EditText etSearch;

    private LinearLayout linErrorConnect;
    private TextView tvRepeat;

    private RecyclerView rvPlaces;
    private PlacesAdapter adapterP;
    private RecyclerView.LayoutManager manager;
    private ArrayList<Place> places;

    private SessionManager session;

    private ProgressDialog pDialog;

    public Context context;

    private Toolbar mToolbar;
    public FragmentDrawer drawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        places = new ArrayList<>();


        rvPlaces = (RecyclerView) findViewById(R.id.rvResplaces);

        etSearch = (EditText) findViewById(R.id.etSearch);

        linErrorConnect = (LinearLayout) findViewById(R.id.linErrorConnectServer);

        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        tvRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.checkInternetConnection(getApplicationContext())) {
                    getPlaces((FragmentDrawer.FragmentDrawerListener)drawerFragment);
                } else {
                    Checker.showCheckInternetDialog((MainActivity) getApplicationContext());
                }
            }
        });

        adapterP = new PlacesAdapter(getApplicationContext(), places);

        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvPlaces.setLayoutManager(manager);

        rvPlaces.setAdapter(adapterP);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap))
                .getMap();
        mMap.setMyLocationEnabled(true);

        if (Checker.checkInternetConnection(getApplicationContext())) {
            getPlaces(this);
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

//        MenuItem item = menu.findItem(R.id.action_auth);
//        if (session.isLoggedIn()) {
//            item.setTitle(R.string.cabinet);
//        } else {
//            item.setTitle(R.string.auth);
//        }

        this.menu = menu;

        return true;
    }

    public void updateMenuTitles() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                if (etSearch.getVisibility() != View.VISIBLE) {
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

        }


        return super.onOptionsItemSelected(item);
    }


    private void getPlaces(final FragmentDrawer.FragmentDrawerListener drawerListener) {
        String tag_string_req = "get_places";

        linErrorConnect.setVisibility(View.GONE);
        rvPlaces.setVisibility(View.VISIBLE);

        pDialog.setMessage("Завантаження закладів");
        showDialog();

        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Places Responce: " + responce);

                responce = responce.substring(responce.indexOf("["));

                hideDialog();
                try {

                    places.clear();

                    JSONArray array = new JSONArray(responce);

                    if(!categories.contains("Всі")){
                        categories.add("Всі");
                    }

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);


                        if (!categories.contains(object.getString(Place.JSON_NAME))) {
                            categories.add(object.getString(Place.JSON_NAME));

                        }

                        places.add(new Place(object));

                        rvPlaces.setAdapter(adapterP);

                        adapterP.getFilter().filter("");
                    }
                    if (places.size() > 0) {
                        drawerFragment = (FragmentDrawer)
                                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
                        drawerFragment.setDrawerListener(drawerListener);
                        setAllDistances();
                    } else {
                        linErrorConnect.setVisibility(View.VISIBLE);
                        rvPlaces.setVisibility(View.GONE);
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
                linErrorConnect.setVisibility(View.VISIBLE);
                rvPlaces.setVisibility(View.GONE);
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
        if (menu != null)
            updateMenuTitles();
        if(drawerFragment!=null)
            drawerFragment.updateDrawerText();
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

    public void setAllDistances() {
        for (Place place : places) {
            getLatLngFromAddress(place);
        }
        adapterP.notifyDataSetChanged();
    }



    @Override
    public void onGetResult(boolean connect) {
        if (Checker.checkInternetConnection(getApplicationContext())) {
            getPlaces(this);
        } else {
            Checker.showCheckInternetDialog(this);
        }
    }

    private float distanceBetween(LatLng latLng1, LatLng latLng2) {

        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);


        float distance = loc1.distanceTo(loc2);

        distance = Math.round(distance);
        return distance;
    }

    private void getLatLngFromAddress(final Place place) {

        String youraddress = place.getAddress();

        try {
            youraddress = URLEncoder.encode(youraddress, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                youraddress + "&sensor=false&language=ru";


        String tag_string_req = "setAllDistance";


        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "SetAllDistance Responce: " + responce);
                {
                    if (responce != null) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject = new JSONObject(responce);
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


                    double curLat;
                    double curLng;

                    curLat = mMap.getMyLocation().getLatitude();
                    curLat = mMap.getMyLocation().getLatitude();

                    curLng = mMap.getMyLocation().getLongitude();
                    curLng = mMap.getMyLocation().getLongitude();

                    LatLng curLatLng = new LatLng(curLat, curLng);
                    LatLng findedLatLng = new LatLng(lat, lng);

                    double distance = distanceBetween(curLatLng, findedLatLng);

                    place.setDistance(distance);
                }
            }

        };


        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "SetAllDistance Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        R.string.sry_cant_load_data_from_server, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.GET,
                uri, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void filterByCategory(int position) {
        adapterP.getFilter().filter(categories.get(position));
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        filterByCategory(position);
        getSupportActionBar().setTitle(categories.get(position));
    }

    public List<String> getCategories() {
        return categories;
    }
}
