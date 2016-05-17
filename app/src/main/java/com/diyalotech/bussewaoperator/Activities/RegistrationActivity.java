package com.diyalotech.bussewaoperator.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.diyalotech.bussewaoperator.AppUtils.APIs;
import com.diyalotech.bussewaoperator.AppUtils.AppLog;
import com.diyalotech.bussewaoperator.AppUtils.AppTexts;
import com.diyalotech.bussewaoperator.AppUtils.AppUtil;
import com.diyalotech.bussewaoperator.AppUtils.GsonRequest;
import com.diyalotech.bussewaoperator.AppUtils.Utility;
import com.diyalotech.bussewaoperator.AppUtils.ValidationUtil;
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button registerButton;
    EditText phoneNumberEditText;
    String TAG = this.getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    String mobileNumber;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        populatedToolbar();
        registerButton.setOnClickListener(this);

    }

    private void populatedToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Operator Registration </font>"));
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        registerButton = (Button) findViewById(R.id.register_button);
        phoneNumberEditText = (EditText) findViewById(R.id.phonenumber_edit_text);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_button) {
            Utility.hideKeyboard(RegistrationActivity.this, RegistrationActivity.this.getCurrentFocus());
            if (AppUtil.isInternetConnectionAvailable(RegistrationActivity.this, AppTexts.INTERNET_TYPE_ALL)) {

                mobileNumber = phoneNumberEditText.getText().toString();
                if (mobileNumber.isEmpty()) {
                    phoneNumberEditText.setError("Please provide phone number ");
                } else if (!ValidationUtil.isMobileNumberValid(mobileNumber)) {
                    phoneNumberEditText.setError("Phone number is invalid");
                } else {
                    progressDialog = AppUtil.createProgressDialog(this);
                    progressDialog.show();
                    String url = APIs.REGISTRAION_MAPPING;
                    AppLog.showLog(TAG + " url:::::" + url);

                    RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mobileNumber", phoneNumberEditText.getText().toString());
                        jsonObject.put("mobileSrlNo", AppUtil.getDeviceId(RegistrationActivity.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("Request bodyJson:::::", jsonObject.toString());

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", AppTexts.AUTHORIZATION_VALUE);
                    headers.put("user", AppTexts.USER_VALUE);
                    Log.e("Headers :::::", headers.toString());

                    GsonRequest<ResponseClass> req = new GsonRequest<ResponseClass>(
                            Request.Method.POST,
                            url,
                            ResponseClass.class,
                            headers,
                            jsonObject,
                            createMyReqSuccessListener(),
                            Utility.createMyReqErrorListener(coordinatorLayout, progressDialog, TAG));
                    req.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(req);

                }


            } else {
                Utility.showSnackbar(coordinatorLayout, AppTexts.NO_INTERNET_MESSAGE_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);

            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // AppLog.showLog("Inside onActivityResult HeavyVehicleActivity::::: " + data.getStringExtra("unbookedSeats"));
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }

        }
    }

    private Response.Listener<ResponseClass> createMyReqSuccessListener() {
        return new Response.Listener<ResponseClass>() {
            @Override
            public void onResponse(ResponseClass response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                AppLog.showLog(TAG + " Success Response::::::" + response.getmStatus());
                String status = response.getmStatus();
                if (status.equalsIgnoreCase("success")) {
                    Context ctx = RegistrationActivity.this;
                    AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                    alert.setView(customView);
                    alert.setTitle(null);
                    alert.setCustomTitle(null);
                    TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                    TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                    // You Can Customise your Title here
                    titleTextViewInDialogue.setText("Attention!");
                    messageTextViewInDialogue.setText("A validation token has been sent to " + phoneNumberEditText.getText().toString());
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivityForResult(new Intent(RegistrationActivity.this, TokenValidationActivity.class), 1);
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();

                } else if (status.equalsIgnoreCase("fail")) {
                    Context ctx = RegistrationActivity.this;
                    AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                    alert.setView(customView);
                    alert.setTitle(null);
                    alert.setCustomTitle(null);
                    TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                    TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                    // You Can Customise your Title here
                    titleTextViewInDialogue.setText("Error!");
                    messageTextViewInDialogue.setText(response.getmMessage());
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(RegistrationActivity.this, RegistrationActivity.class));
                            finish();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                } else {

                }
            }
        };
    }

}
