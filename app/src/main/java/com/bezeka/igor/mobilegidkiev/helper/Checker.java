package com.bezeka.igor.mobilegidkiev.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.dialog_fragment.NoConnectionDialogFragment;

/**
 * Created by Igor on 03.12.2015.
 */
public class Checker {


    public static boolean checkOneEditText(Context context, final EditText et){

        if(et.getText().toString().isEmpty()){
            Toast.makeText(context,context.getString(R.string.plz_fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkTwoEditText(Context context, final EditText et1, final EditText et2){

        if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()){
            Toast.makeText(context,context.getString(R.string.plz_fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkThreeEditText(Context context, final EditText et1, final EditText et2, final EditText et3){

        if(et1.getText().toString().isEmpty()
                || et2.getText().toString().isEmpty()
                || et3.getText().toString().isEmpty()){
            Toast.makeText(context,context.getString(R.string.plz_fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkFourEditText(
            Context context, final EditText et1, final EditText et2,
            final EditText et3, final EditText et4){

        if(et1.getText().toString().isEmpty()
                || et2.getText().toString().isEmpty()
                || et3.getText().toString().isEmpty()
                || et4.getText().toString().isEmpty()){
            Toast.makeText(context,context.getString(R.string.plz_fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPasswordsEquals(Context context, final String pass1,final String pass2){
        if(pass1.equals(pass2))
        {
            return true;
        } else {
            Toast.makeText(context,context.getString(R.string.plz_input_equals_passwords), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showCheckInternetDialog(AppCompatActivity activity){
        NoConnectionDialogFragment noConnectionDialogFragment = new NoConnectionDialogFragment();
        noConnectionDialogFragment.show(activity.getSupportFragmentManager(),NoConnectionDialogFragment.class.getSimpleName());
    }
}
