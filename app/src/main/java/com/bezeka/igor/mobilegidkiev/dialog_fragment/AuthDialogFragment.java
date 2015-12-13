package com.bezeka.igor.mobilegidkiev.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bezeka.igor.mobilegidkiev.R;
import com.bezeka.igor.mobilegidkiev.activity.MainActivity;
import com.bezeka.igor.mobilegidkiev.app.AppController;
import com.bezeka.igor.mobilegidkiev.helper.SessionManager;

/**
 * Created by Igor on 03.12.2015.
 */
public class AuthDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCancel;
    Button btnRegistration;
    Button btnLogin;
    Button btnExit;

    TextView tvTitle;
    TextView tvPreAuth;
    TextView tvAfterAuth;

    RelativeLayout layPre;
    RelativeLayout layAfter;

    SessionManager session;

    boolean isSendComment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.auth_dialog_fragment, null);

        session = new SessionManager(getActivity().getApplicationContext());

        isSendComment = getArguments().getBoolean("isSendComment");

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnRegistration = (Button) view.findViewById(R.id.btnRegister);
        btnExit = (Button) view.findViewById(R.id.btnExit);

        tvPreAuth = (TextView) view.findViewById(R.id.tvPreAuth);
        tvAfterAuth = (TextView) view.findViewById(R.id.tvAfterAuth);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        layPre = (RelativeLayout) view.findViewById(R.id.layPreAuth);
        layAfter = (RelativeLayout) view.findViewById(R.id.layAfterAuth);

        if(session.isLoggedIn()){
            String email = AppController.getInstance().userEmail;
            String name = AppController.getInstance().userName;

            tvPreAuth.setVisibility(View.GONE);
            layPre.setVisibility(View.GONE);
            tvTitle.setText(R.string.cabinet);

            String html = "<p><b>"+getString(R.string.login_email)+": </b>"+email+"</p></br>" +
                    "<p><b>"+getString(R.string.login_name)+": </b>"+name+"</p>";

            tvAfterAuth.setText(Html.fromHtml(html));

        } else {
            tvAfterAuth.setVisibility(View.GONE);
            layAfter.setVisibility(View.GONE);
            tvTitle.setText(R.string.auth);
        }

        btnCancel.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
        btnExit.setOnClickListener(this);


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
                getDialog().cancel();
                LoginDialogFragment logDialog = new LoginDialogFragment();
                Bundle args = new Bundle();
                args.putBoolean("isSendComment", isSendComment);
                logDialog.setArguments(args);
                logDialog.show(getActivity().getSupportFragmentManager(),LoginDialogFragment.class.getSimpleName());
                break;
            case R.id.btnRegister:
                getDialog().cancel();
                RegistrationDialogFragment regDialog = new RegistrationDialogFragment();
                Bundle args1 = new Bundle();
                args1.putBoolean("isSendComment", isSendComment);
                regDialog.setArguments(args1);
                regDialog.show(getActivity().getSupportFragmentManager(),RegistrationDialogFragment.class.getSimpleName());
                break;
            case R.id.btnExit:
                getDialog().cancel();
                session.setLogin(false);
                ((MainActivity)getActivity()).updateMenuTitles();
            default:
                break;
        }
    }
}
