package com.bezeka.igor.mobilegidkiev.helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Igor on 03.12.2015.
 */
public class PlacesAPI {

    private ProgressDialog pDialog;
    private Context context;

    public PlacesAPI(Context context){
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    public static void login(final String email, final String password){

    }

    public static void registration(final String email, final String password, final String name){

    }

    public static void getPlaces(){

    }

    public static void getPlace(final String placeId){

    }

    public static void getComments(final String placeId){

    }

    public static void getRates(final String placeId){

    }

    public static void setComment(final String placeId
            , final String userId, final String messageText){

    }

    public static void setRate(Context context, final String placeId,
                               final String userId, final String rating){

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
