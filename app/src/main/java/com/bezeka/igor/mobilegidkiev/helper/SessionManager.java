package com.bezeka.igor.mobilegidkiev.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bezeka.igor.mobilegidkiev.app.AppController;

/**
 * Created by Igor on 21.06.2015.
 */
public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "MobileGidLogin";

    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_NAME = "userName";
    public static final String KEY_USER_ID = "userId";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        AppController.getInstance().userEmail = pref.getString(KEY_EMAIL,"email");
        AppController.getInstance().userName = pref.getString(KEY_NAME,"name");
        AppController.getInstance().userId = pref.getString(KEY_USER_ID,"userId");
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLogin(boolean isLoggedIn, String email, String name, String userId) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_USER_ID,userId);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
