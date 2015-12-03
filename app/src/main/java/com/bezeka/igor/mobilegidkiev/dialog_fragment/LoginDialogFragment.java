package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.helper.Checker;
import com.bezeka.igor.mobilegidkiev.helper.PlacesAPI;

/**
 * Created by Igor on 03.12.2015.
 */
public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCancel;
    Button btnLogin;

    EditText etEmail;
    EditText etPassword;

    String email;
    String password;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.login_dialog_fragment, null);


        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnCancel.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

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
                    PlacesAPI.login(getActivity(),email,password);
                break;
        }
    }


}
