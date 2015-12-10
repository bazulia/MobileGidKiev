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

/**
 * Created by Igor on 03.12.2015.
 */
public class RegistrationDialogFragment extends DialogFragment implements View.OnClickListener {

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.login_dialog_fragment, null);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        etName = (EditText) view.findViewById(R.id.etName);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        passwordConfirm = etConfirmPassword.getText().toString();
        name = etName.getText().toString();

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
            case R.id.btnRegister:
                if(Checker.checkFourEditText(getActivity(), etEmail,etPassword, etConfirmPassword, etName)){
                    if(Checker.isPasswordsEquals(getActivity(), password,passwordConfirm))
                    {
                       registration();
                    }
                }
                break;
        }
    }

    private void registration(){

    }
}
