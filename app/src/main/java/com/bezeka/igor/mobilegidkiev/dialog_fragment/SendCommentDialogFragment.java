package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bezeka.igor.mobilegidkiev.R;

/**
 * Created by Igor on 03.12.2015.
 */
public class SendCommentDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCancel;
    Button btnSend;

    CheckBox isAnonim;
    EditText etText;

    boolean isAnonuimBoolean;

    boolean isLike;

    String mainText;

    RadioButton rbLike;
    RadioButton rbDislike;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.send_comment_dialog_fragment, null);


        etText = (EditText) view.findViewById(R.id.etMainText);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        isAnonim = (CheckBox) view.findViewById(R.id.chbAnonim);
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        mainText = etText.getText().toString();
        isAnonuimBoolean = isAnonim.isChecked();


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancle:
                getDialog().cancel();
                break;
            case R.id.btnSentComment:
//                if(Checker.checkTwoEditText(getActivity(), etEmail, etPassword))
//                    PlacesAPI.login(getActivity(), email, password);
                break;
            default:
        }
    }
}