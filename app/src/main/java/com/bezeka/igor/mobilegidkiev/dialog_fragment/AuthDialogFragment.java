package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.bezeka.igor.mobilegidkiev.R;

/**
 * Created by Igor on 03.12.2015.
 */
public class AuthDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCancel;
    Button btnRegistration;
    Button btnLogin;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.auth_dialog_fragment, null);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnRegistration = (Button) view.findViewById(R.id.btnRegister);

        btnCancel.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);


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
                LoginDialogFragment logDialog = new LoginDialogFragment();
                logDialog.show(getActivity().getSupportFragmentManager(),LoginDialogFragment.class.getSimpleName());
                break;
            case R.id.btnRegister:
                RegistrationDialogFragment regDialog = new RegistrationDialogFragment();
                regDialog.show(getActivity().getSupportFragmentManager(),RegistrationDialogFragment.class.getSimpleName());
                break;
            default:
                break;
        }
    }
}
