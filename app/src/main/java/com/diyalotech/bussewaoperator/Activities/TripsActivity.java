package com.diyalotech.bussewaoperator.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.diyalotech.bussewaoperator.Adapters.TripBusesCustomAdapterSimpleListView;
import com.diyalotech.bussewaoperator.AppUtils.APIs;
import com.diyalotech.bussewaoperator.AppUtils.AppLog;
import com.diyalotech.bussewaoperator.AppUtils.AppTexts;
import com.diyalotech.bussewaoperator.AppUtils.AppUtil;
import com.diyalotech.bussewaoperator.AppUtils.DateUtils;
import com.diyalotech.bussewaoperator.AppUtils.GsonRequest;
import com.diyalotech.bussewaoperator.AppUtils.Utility;
import com.diyalotech.bussewaoperator.Model.ExpandableTripsDto;
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TripsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String DATEPICKER_TAG = "datepicker";
    // ExpandableListView tripBusesListView;
    ListView tripBusesListView;
    Toolbar toolbar;
    Menu optionsMenu;
    String journeyDate;
    String dateString;
    String deviceSrlNumber;
    ImageButton nextImageButton;
    ImageButton previousImageButton;
    TextView dateTextView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    String TAG = this.getClass().getSimpleName();
    ArrayList<ResponseClass.TripsDto> tripBusesDtoArrayList;
    ArrayList<ExpandableTripsDto> expandableTripsDtoArrayList;
    String formattedDate;
    SimpleDateFormat df;
    List<String> dateValues = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;


    Calendar calendar = Calendar.getInstance();
    Calendar calendarTemp = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendarTemp.get(Calendar.YEAR), calendarTemp.get(Calendar.MONTH), calendarTemp.get(Calendar.DAY_OF_MONTH));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        df = new SimpleDateFormat("yyyy-MM-dd");
        tripBusesDtoArrayList = new ArrayList<>();
        initViews();
        populateToolbar();
        setDateAndGetTrips();
        // getIntents();
        showVehicles();
    }

    private void getIntents() {
        tripBusesDtoArrayList = new ArrayList<>();
        dateString = getIntent().getStringExtra("dateString");
        deviceSrlNumber = getIntent().getStringExtra("deviceSrlNumber");
        tripBusesDtoArrayList = getIntent().getParcelableArrayListExtra("busesAvailabeDtoArrayList");
    }

    private void populateToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            dateTextView = (TextView) toolbar.findViewById(R.id.date_textview);
            nextImageButton = (ImageButton) toolbar.findViewById(R.id.image_button_next);
            previousImageButton = (ImageButton) toolbar.findViewById(R.id.image_button_previous);
            dateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
            nextImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarTemp.add(Calendar.DATE, 1);
                    formattedDate = df.format(calendarTemp.getTime());
                    AppLog.showLog(TAG + " formattedDate:::::::::::" + formattedDate);
                    getTrips(createFormattedDate(formattedDate));
                }
            });
            previousImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarTemp.add(Calendar.DATE, -1);
                    formattedDate = df.format(calendarTemp.getTime());
                    AppLog.showLog(TAG + " formattedDate:::::::::::" + formattedDate);
                    getTrips(createFormattedDate(formattedDate));

                }
            });

        }
    }

    private void checkPreviousButtonVisibility() {
        AppLog.showLog(TAG + " time comparison:::::::" + " " + calendarTemp.getTime() + " " + calendar.getTime());
        if (calendarTemp.getTime().equals(calendar.getTime())) {
            previousImageButton.setVisibility(View.INVISIBLE);
        } else if (calendarTemp.getTime().before(calendar.getTime())) {
            previousImageButton.setVisibility(View.INVISIBLE);
        } else {
            previousImageButton.setVisibility(View.VISIBLE);

        }

    }

    public void getTrips(String journeyDate) {
        if (AppUtil.isInternetConnectionAvailable(TripsActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
            progressBar.setVisibility(View.VISIBLE);
            String url = APIs.GET_TRIP_MAPPING;
            AppLog.showLog(TAG + " url:::::" + url);
            RequestQueue queue = Volley.newRequestQueue(TripsActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date", journeyDate);
                jsonObject.put("mobileSrlNo", AppUtil.getDeviceId(TripsActivity.this));
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


    private void setDateAndGetTrips() {

        System.out.println("Current time => " + calendarTemp.getTime());
        formattedDate = df.format(calendarTemp.getTime());
        getTrips(createFormattedDate(formattedDate));


    }

    public String createFormattedDate(String formattedDate) {

        dateValues = Arrays.asList(formattedDate.split("\\s*-\\s*"));

//        String currentDate = dateValues.get(0) + DateUtils.getDateSuffix(Integer.valueOf(dateValues.get(0)))+ " " +
//                DateUtils.getMonthName(Integer.valueOf(dateValues.get(1))) + " " +Integer.valueOf(dateValues.get(2));
//        dateTextView.setText(currentDate);
        return formattedDate;

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + DateUtils.getDateSuffix(Calendar.DAY_OF_MONTH) + " " +
                DateUtils.getMonthName(calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR);
        if ((i < calendar.get(Calendar.YEAR))) {
            AppLog.showLog("Inside journey date checker");
            Toast.makeText(getApplicationContext(), "Selected Date is invalid", Toast.LENGTH_SHORT).show();
        } else {
            if (i == calendar.get(Calendar.YEAR) && i1 < calendar.get(Calendar.MONTH)) {
                Toast.makeText(this, "Selected Date is invalid", Toast.LENGTH_SHORT).show();
            } else {
                if (i2 < calendar.get(Calendar.DAY_OF_MONTH) && i1 == calendar.get(Calendar.MONTH)) {
                    Toast.makeText(this, "Selected Date is invalid", Toast.LENGTH_SHORT).show();

                } else {
                    i1 = i1 + 1;
                    dateString = i + "-" + i1 + "-" + i2;
                    calendarTemp.set(i, i1 - 1, i2);
                    getTrips(dateString);
                    AppLog.showLog("journey day " + dateString);
                }
            }
        }
    }

    private void showDialog() {
        datePickerDialog.initialize(this, calendarTemp.get(Calendar.YEAR), calendarTemp.get(Calendar.MONTH), calendarTemp.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }


    private void initViews() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tripBusesListView = (ExpandableListView) findViewById(R.id.trip_buses_expandable_listview);
        tripBusesListView = (ListView) findViewById(R.id.trip_buses_listview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
            } else if (resultCode == Activity.RESULT_CANCELED) {
                setDateAndGetTrips();
            }

        }
    }

    private Response.Listener<ResponseClass> createMyReqSuccessListener() {
        return new Response.Listener<ResponseClass>() {
            @Override
            public void onResponse(ResponseClass response) {
                AppLog.showLog(TAG + " Success Response::::::" + response.getmStatus());
                String status = response.getmStatus();
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);

                }
                if (status.equalsIgnoreCase("success")) {
                    AppLog.showLog(TAG + " TripsDto size::::::" + response.getMtripArrayList().size());
                    AppLog.showLog(TAG + "  Response dtos::::::" + response.getMtripArrayList().get(0).getId());
                    tripBusesDtoArrayList = response.getMtripArrayList();
                    dateTextView.setText(tripBusesDtoArrayList.get(0).getDate());
                    checkPreviousButtonVisibility();
                    showVehicles();
                } else if (status.equalsIgnoreCase("fail")) {
                    Utility.showSnackbar(coordinatorLayout, response.getmMessage(), AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    if (response.getmCode() == 3) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Context ctx = TripsActivity.this;
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
                        messageTextViewInDialogue.setText(response.getmMessage());
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(new Intent(TripsActivity.this, RegistrationActivity.class));
                                finish();
                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();

                    }
                } else {

                }
            }
        };
    }

    private void showVehicles() {
        //   expandableTripsDtoArrayList = new ArrayList<>();
        // journeyDate = getIntent().getStringExtra("journeyDate");
        tripBusesListView.setVisibility(View.VISIBLE);
        AppLog.showLog("Size of busesAvailabeDtoArrayList inside TripsActivity::::::" + tripBusesDtoArrayList.size());
//        if (tripBusesDtoArrayList != null) {
//            for (int i = 0; i < tripBusesDtoArrayList.size(); i++) {
//                AppLog.showLog("TripsBussesDtoaarayList::::::::getBusType::::::" + tripBusesDtoArrayList.get(i).getBusType());
//                AppLog.showLog("TripsBussesDtoaarayList::::::::getOperator::::::" + tripBusesDtoArrayList.get(i).getOperator());
//                AppLog.showLog("TripsBussesDtoaarayList::::::::getRoute::::::" + tripBusesDtoArrayList.get(i).getRoute());
//                AppLog.showLog("TripsBussesDtoaarayList::::::::getBusNo::::::" + tripBusesDtoArrayList.get(i).getBusNo());
//                ExpandableTripsDto expandableTripsDto = new ExpandableTripsDto();
//                expandableTripsDto.setBusType(tripBusesDtoArrayList.get(i).getBusType());
//                expandableTripsDto.setOperator(tripBusesDtoArrayList.get(i).getOperator());
//                expandableTripsDto.setRoute(tripBusesDtoArrayList.get(i).getRoute());
//                expandableTripsDto.setBusNo(tripBusesDtoArrayList.get(i).getBusNo());
//                ChildView childView = new ChildView();
//                childView.setTicketPrice(tripBusesDtoArrayList.get(i).getTicketPrice());
//                childView.setAmmenities(tripBusesDtoArrayList.get(i).getAmmenities());
//                childView.setDate(tripBusesDtoArrayList.get(i).getDate());
//                childView.setDepartureTime(tripBusesDtoArrayList.get(i).getDepartureTime());
//                expandableTripsDto.setChildView(childView);
//                expandableTripsDtoArrayList.add(expandableTripsDto);
//            }
//        }

//        TripBusesCustomAdapter tripBusesCustomAdapter = new TripBusesCustomAdapter(this, expandableTripsDtoArrayList);
//        tripBusesListView.setAdapter(tripBusesCustomAdapter);
//        tripBusesListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
        TripBusesCustomAdapterSimpleListView tripBusesCustomAdapterSimpleListView = new TripBusesCustomAdapterSimpleListView(this, tripBusesDtoArrayList);
        tripBusesListView.setAdapter(tripBusesCustomAdapterSimpleListView);
        tripBusesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppLog.showLog("Inside bustype hiace ::::::::::");
                if (tripBusesDtoArrayList.get(position).getVehicleType().equalsIgnoreCase("bus")) {
                    Intent heavyVehicleIntent = new Intent(TripsActivity.this, HeavyVehicleActivity.class);
                    heavyVehicleIntent.putExtra("busDetails", tripBusesDtoArrayList.get(position));
                    heavyVehicleIntent.putExtra("journeyDate", journeyDate);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivityForResult(heavyVehicleIntent, 5, bndlanimation);

                    } else {
                        startActivity(heavyVehicleIntent);
                    }
                } else {
                    Intent lightVehicleIntent = new Intent(TripsActivity.this, LightVehicleActivity.class);
                    AppLog.showLog("Inside bustype hiace1 ::::::::::");
                    lightVehicleIntent.putExtra("busDetails", tripBusesDtoArrayList.get(position));
                    lightVehicleIntent.putExtra("journeyDate", journeyDate);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivityForResult(lightVehicleIntent, 5, bndlanimation);
                    } else {
                        startActivity(lightVehicleIntent);
                    }
                }
            }
        });
//
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int position, int childPosition, long id) {
//
//
//
//                }
//                return false;
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.noaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


}
