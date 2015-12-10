package com.bezeka.igor.mobilegidkiev.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Igor on 21.06.2015.
 */
public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "CoachLogin";

    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
