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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezeka.igor.mobilegidkiev.R;
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
public class SendCommentDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = SendCommentDialogFragment.class.getSimpleName();

    Button btnCancel;
    Button btnSend;

    CheckBox isAnonim;
    EditText etText;

    boolean isAnonuimBoolean;

    boolean isLike;

    String mainText;
    String placeId;

    RadioButton rbLike;
    RadioButton rbDislike;

    SessionManager session;

    private ProgressDialog pDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.send_comment_dialog_fragment, null);

        placeId = getArguments().getString("placeId");

        etText = (EditText) view.findViewById(R.id.etMainText);

        btnCancel = (Button) view.findViewById(R.id.btnCancle);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        isAnonim = (CheckBox) view.findViewById(R.id.chbAnonim);
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        mainText = etText.getText().toString();
        isAnonuimBoolean = isAnonim.isChecked();

        session = new SessionManager(getActivity().getApplicationContext());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

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
                    if(Checker.checkOneEditText(getActivity(), etText)){
                        String mPlaceId = placeId;
                        String mUserId = AppController.getInstance().userId;
                        String mMessageText = etText.getText().toString();
                        sendComment(mPlaceId,mUserId,mMessageText);
                    }

                break;
            default:
        }
    }

    private void sendComment(final String placeId,final String userId, final String messageText){
        String tag_string_req = "sendComment";


        pDialog.setMessage(getString(R.string.plz_wait_sending_comment));
        showDialog();

        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                Log.d(TAG, "Send Cemment Responce: " + responce);

                hideDialog();
                try {

                    JSONObject object = new JSONObject(responce);

                    boolean error = object.getBoolean("status");
                    if(!error){
                        sendResult(Activity.RESULT_OK);
                        getDialog().cancel();


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
                Log.d(TAG, "Send Comment Error: " + volleyError.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SET_COMMENT, listener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("placeId", placeId);
                params.put("userId",userId);
                params.put("messageText", messageText);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null)
            return;

        Intent i = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
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