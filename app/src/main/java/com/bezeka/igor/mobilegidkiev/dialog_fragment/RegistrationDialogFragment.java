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
public class RegistrationDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = RegistrationDialogFragment.class.getSimpleName();

    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etName;

    Button btnCancel;
    Button btnRegister;

    String email;
    String password;
    String passwordConfirm;
    String name;

    boolean isSendComment;

    SessionManager session;

    private ProgressDialog pDialog;

    String placeId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.registration_dialog_fragment, null);

        isSendComment = getArguments().getBoolean("isSendComment");

        if (isSendComment)
            placeId = getArguments().getString("placeId");
        else
            placeId = "1";

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        etName = (EditText) view.findViewById(R.id.etName);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        session = new SessionManager(getActivity().getApplicationContext());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onClick(View v) {
        ((MainActivity)getActivity()).drawerFragment.updateDrawerText();
        switch (v.getId()) {
            case R.id.btnCancle:
                getDialog().cancel();
                break;
            case R.id.btnRegister:
                if (Checker.checkFourEditText(getActivity(), etEmail, etPassword, etConfirmPassword, etName)) {
                    password = etPassword.getText().toString();
                    passwordConfirm = etConfirmPassword.getText().toString();
                    if (Checker.isPasswordsEquals(getActivity(), password, passwordConfirm)) {
                        email = etEmail.getText().toString();
                        password = etPassword.getText().toString();
                        passwordConfirm = etConfirmPassword.getText().toString();
                        name = etName.getText().toString();

                        if (isEmailValid(email)){
                            if(Checker.checkInternetConnection(getActivity())){
                                registration(email, password, name);
                            } else {
                                Checker.showCheckInternetDialog((MainActivity)getActivity());
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Ви ввели email в невірному форматі\n перевірте корректність", Toast.LENGTH_LONG).show();
                        }


                    }
                }
                break;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }


    private void registration(final String email, final String password, final String name) {
        String tag_string_req = "registration";


        pDialog.setMessage(getString(R.string.plz_wait_registration));
        showDialog();

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Registration Responce: " + responce);

                hideDialog();
                try {

                    JSONObject object = new JSONObject(responce);

                    boolean error = object.getBoolean("status");
                    if (!error) {
                        session.setLogin(true);
                        sendResult(Activity.RESULT_OK);
                        getDialog().cancel();

                        AppController.getInstance().userName = name;
                        AppController.getInstance().userEmail = email;
                        AppController.getInstance().userId = object.getString("id");

                        session.setLogin(true,object.getString("email"),object.getString("name"),object.getString("id"));

                        ((MainActivity)getActivity()).updateMenuTitles();

                        if (isSendComment) {
                            SendCommentDialogFragment sendCommentDialogFragment = new SendCommentDialogFragment();
                            Bundle args = new Bundle();
                            args.putBoolean("isSendComment",isSendComment);
                            args.putString("placeId", placeId);
                            sendCommentDialogFragment.setArguments(args);
                            sendCommentDialogFragment.show(getActivity().getSupportFragmentManager(),
                                    SendCommentDialogFragment.class.getSimpleName());
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.sry_registration_error, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Registration Error: " + volleyError.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_REGISTRATION, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userEmail", email);
                params.put("password", AppController.md5(password));
                params.put("username", name);

                Log.d(TAG,params.toString());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
