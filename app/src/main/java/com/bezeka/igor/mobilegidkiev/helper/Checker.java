package com.bezeka.igor.mobilegidkiev.helper;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.bezeka.igor.mobilegidkiev.R;

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

    public static boolean checkFourEditText(Context context, final EditText et1, final EditText et2, final EditText et3, final EditText et4){

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
}
