package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.activity.MainActivity;
import com.bezeka.igor.mobilegidkiev.app.AppConfig;
import com.bezeka.igor.mobilegidkiev.app.AppController;
import com.bezeka.igor.mobilegidkiev.helper.Checker;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 03.12.2015.
 */
public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = LoginDialogFragment.class.getSimpleName();

    Button btnCancel;
    Button btnLogin;

    EditText etEmail;
    EditText etPassword;

    String email;
    String password;

    SessionManager session;

    boolean isSendComment;

    private ProgressDialog pDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.login_dialog_fragment, null);

        isSendComment = getArguments().getBoolean("isSendComment");

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnCancel.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        session = new SessionManager(getActivity().getApplicationContext());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancle:
                getDialog().cancel();
                break;
            case R.id.btnLogin:
                if(Checker.checkTwoEditText(getActivity(), etEmail,etPassword))
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    login(email,password);
                break;
        }
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null)
            return;

        Intent i = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }


    private void login(final String email,final String password){
        String tag_string_req = "login";


        pDialog.setMessage(getString(R.string.plz_wait_login));
        showDialog();

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Login Responce: " + responce);

                hideDialog();
                try {

                    JSONObject object = new JSONObject(responce);

                    boolean error = object.getBoolean("status");
                    if(!error){

                        sendResult(Activity.RESULT_OK);
                        getDialog().cancel();

                        AppController.getInstance().userEmail = object.getString("email");
                        AppController.getInstance().userName = object.getString("name");

                        session.setLogin(true,object.getString("email"),object.getString("name"));

                        ((MainActivity)getActivity()).updateMenuTitles();

                        if (isSendComment) {
                            SendCommentDialogFragment sendCommentDialogFragment = new SendCommentDialogFragment();
                            sendCommentDialogFragment.show(getActivity().getSupportFragmentManager(),
                                    SendCommentDialogFragment.class.getSimpleName());
                        }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.sry_login_error, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Login Error: " + volleyError.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_LOGIN, listener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userEmail", email);
                params.put("password", AppController.md5(password));

                Log.d(TAG, params.toString());

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
