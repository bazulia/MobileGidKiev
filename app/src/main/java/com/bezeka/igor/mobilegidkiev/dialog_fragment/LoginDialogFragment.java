package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.bezeka.igor.mobilegidkiev.R;

/**
 * Created by Igor on 03.12.2015.
 */
public class LoginDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.login_dialog_fragment, null);



        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
