package com.diyalotech.bussewaoperator.Activities;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.diyalotech.bussewaoperator.AppUtils.APIs;
import com.diyalotech.bussewaoperator.AppUtils.AppLog;
import com.diyalotech.bussewaoperator.AppUtils.AppTexts;
import com.diyalotech.bussewaoperator.AppUtils.AppUtil;
import com.diyalotech.bussewaoperator.AppUtils.DateUtils;
import com.diyalotech.bussewaoperator.AppUtils.GsonRequest;
import com.diyalotech.bussewaoperator.AppUtils.RippleView;
import com.diyalotech.bussewaoperator.AppUtils.Utility;
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class DateSelectActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    public static final String DATEPICKER_TAG = "datepicker";
    TextView dateTextView;
    Button buttonGetMyBus;
    LinearLayout dateLL;
    ProgressDialog progressDialog;
    String dateString;
    Toolbar toolbar;
    RippleView dateRV;
    CoordinatorLayout coordinatorLayout;
    String TAG = this.getClass().getSimpleName();
    Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);
        populatedToolbar();
        initViews();
        setDate();
        dateRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppLog.showLog("Inside date clicke listener:::::::::::::");
                showDialog();
            }
        });
        buttonGetMyBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrips();
            }
        });
    }

    private void initViews() {
        dateTextView = (TextView) findViewById(R.id.date);
        buttonGetMyBus = (Button) findViewById(R.id.get_my_trips_button);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        dateLL = (LinearLayout) findViewById(R.id.ll_journey_date);
        dateRV = (RippleView) findViewById(R.id.journey_date_relative_layout);
    }


    private void setDate() {
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        String formattedSelectedDate = currentDay + DateUtils.getDateSuffix(currentDay) + " "
                + DateUtils.getMonthName(currentMonth) + " " + currentYear;
        dateTextView.setText(formattedSelectedDate);
        dateString = currentYear + "-" + currentMonth + "-" + currentDay;

    }

    private void showDialog() {
        datePickerDialog.initialize(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }

    public void getTrips() {
        if (AppUtil.isInternetConnectionAvailable(DateSelectActivity.this, AppTexts.INTERNET_TYPE_ALL)) {


            progressDialog = AppUtil.createProgressDialog(this);
            progressDialog.show();
            String url = APIs.GET_TRIP_MAPPING;
            AppLog.showLog(TAG + " url:::::" + url);

            RequestQueue queue = Volley.newRequestQueue(DateSelectActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date", dateString);
                jsonObject.put("mobileSrlNo", AppUtil.getDeviceId(DateSelectActivity.this));
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

            queue.add(req);

        } else {
            Utility.showSnackbar(coordinatorLayout, AppTexts.NO_INTERNET_MESSAGE_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
        }
    }

    private Response.Listener<ResponseClass> createMyReqSuccessListener() {
        return new Response.Listener<ResponseClass>() {
            @Override
            public void onResponse(ResponseClass response) {
                AppLog.showLog(TAG + " Success Response::::::" + response.getmStatus());
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                String status = response.getmStatus();
                if (status.equalsIgnoreCase("success")) {
                    AppLog.showLog(TAG + " TripsDto size::::::" + response.getMtripArrayList().size());
                    AppLog.showLog(TAG + "  Response dtos::::::" + response.getMtripArrayList().get(0).getId());
                    Intent intent = new Intent(DateSelectActivity.this, TripsActivity.class);
                    intent.putParcelableArrayListExtra("busesAvailabeDtoArrayList", response.getMtripArrayList());
                    intent.putExtra("dateString", dateString);
                    intent.putExtra("deviceSrlNumber", AppUtil.getDeviceId(DateSelectActivity.this));
                    startActivity(intent);
                } else if (status.equalsIgnoreCase("fail")) {
                    Utility.showSnackbar(coordinatorLayout, response.getmMessage(), AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                } else {

                }
            }
        };
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + DateUtils.getDateSuffix(Calendar.DAY_OF_MONTH) + " " +
                DateUtils.getMonthName(calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR);
        if ((i < calendar.get(Calendar.YEAR))) {
            AppLog.showLog("Inside journey date checker");
            Log.i("Error::::", "Inside journey date checker");

            Toast.makeText(getApplicationContext(), "Selected Date is invalid", Toast.LENGTH_SHORT).show();
            dateTextView.setText(currentDate);
        } else {
            if (i == calendar.get(Calendar.YEAR) && i1 < calendar.get(Calendar.MONTH)) {
                dateTextView.setText(currentDate);
                Toast.makeText(this, "Selected Date is invalid", Toast.LENGTH_SHORT).show();
            } else {
                if (i2 < calendar.get(Calendar.DAY_OF_MONTH) && i1 == calendar.get(Calendar.MONTH)) {
                    dateTextView.setText(currentDate);
                    Toast.makeText(this, "Selected Date is invalid", Toast.LENGTH_SHORT).show();

                } else {
                    String journeyDate = i2 + DateUtils.getDateSuffix(i2) + " " + DateUtils.getMonthName(i1 + 1) + " " + i;
                    dateTextView.setText(journeyDate);
                    i1 = i1 + 1;
                    dateString = i + "-" + i1 + "-" + i2;
                    AppLog.showLog("journey day " + dateString);
                }
            }
        }
    }

    private void populatedToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Date Selection </font>"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
