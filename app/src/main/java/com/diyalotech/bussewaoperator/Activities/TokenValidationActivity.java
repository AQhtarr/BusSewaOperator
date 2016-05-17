package com.diyalotech.bussewaoperator.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class TokenValidationActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button submitButton;
    EditText tokenEditText;
    CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;
    String TAG = this.getClass().getSimpleName();
    String token = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_validation);
        initViews();
        populatedToolbar();
        submitButton.setOnClickListener(this);
        //  readSmsFromInbox();


    }

    private String readSmsFromInbox() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        Uri uri = Uri.parse(SMS_URI_INBOX);
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = getContentResolver().query(uri, projection, "address='7337'", null, "date desc");
        if (cur.moveToFirst()) {
            int index_Address = cur.getColumnIndex("address");
            int index_Person = cur.getColumnIndex("person");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");
            int index_Type = cur.getColumnIndex("type");
            String strAddress = cur.getString(index_Address);
            int intPerson = cur.getInt(index_Person);
            String strbody = cur.getString(index_Body);
            long longDate = cur.getLong(index_Date);
            int int_Type = cur.getInt(index_Type);
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        cur.close();
        return null;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populatedToolbar() {
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Token Validation</font>"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);

    }

    private void initViews() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        submitButton = (Button) findViewById(R.id.submit_button);
        tokenEditText = (EditText) findViewById(R.id.token_edit_text);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {
            Utility.hideKeyboard(TokenValidationActivity.this, TokenValidationActivity.this.getCurrentFocus());
            if (AppUtil.isInternetConnectionAvailable(TokenValidationActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
                token = tokenEditText.getText().toString();
                if (token.isEmpty()) {
                    tokenEditText.setError("Enter Token here ");
                } else {
                    progressDialog = AppUtil.createProgressDialog(this);
                    progressDialog.show();
                    String url = APIs.TOKEN_VALIDATION_MAPPING;
                    AppLog.showLog(TAG + " url:::::" + url);

                    RequestQueue queue = Volley.newRequestQueue(TokenValidationActivity.this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mobileSrlNo", AppUtil.getDeviceId(TokenValidationActivity.this));
                        jsonObject.put("mobileVerificationCode", tokenEditText.getText().toString());
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedIn", true);
                    editor.apply();
                    Context ctx = TokenValidationActivity.this;
                    AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                    alert.setView(customView);
                    alert.setTitle(null);
                    alert.setCustomTitle(null);
                    TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                    TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                    // You Can Customise your Title here
                    titleTextViewInDialogue.setText("Registered !");
                    messageTextViewInDialogue.setText("Congratulations! you are now successfully registered to busSewa");
                    alert.setPositiveButton("Go to App", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(TokenValidationActivity.this, StarterActivity.class));
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });      alert.setNegativeButton("Go later", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();



                } else if (status.equalsIgnoreCase("fail")) {
                    Context ctx = TokenValidationActivity.this;
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ctx);
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
                            startActivity(new Intent(TokenValidationActivity.this, RegistrationActivity.class));
                            finish();
                        }
                    });
                    android.app.AlertDialog dialog = alert.create();
                    dialog.show();
                } else {

                }
            }
        };
    }

}
