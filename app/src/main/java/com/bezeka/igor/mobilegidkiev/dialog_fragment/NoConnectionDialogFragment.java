package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.helper.Checker;

/**
 * Created by Igor on 15.12.2015.
 */
public class NoConnectionDialogFragment extends DialogFragment implements View.OnClickListener {

    private CheckInternetInterface listener;

    Button btnCancel;
    Button btnRepeat;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.no_connection_dialog, null);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnRepeat = (Button) view.findViewById(R.id.btnRepeat);

        btnCancel.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancle:
                getDialog().dismiss();
                break;
            case R.id.btnRepeat:
                if(Checker.checkInternetConnection(getActivity()))
                {
                    listener.onGetResult(true);
                    getDialog().dismiss();
                }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.listener = (CheckInternetInterface) getActivity();
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public static interface CheckInternetInterface {
        public abstract void onGetResult(boolean connect);
    }
}
