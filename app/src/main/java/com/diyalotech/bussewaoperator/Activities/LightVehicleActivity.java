package com.diyalotech.bussewaoperator.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.diyalotech.bussewaoperator.Model.SelectedSeatDto;
import com.diyalotech.bussewaoperator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class LightVehicleActivity extends AppCompatActivity implements View.OnClickListener {
    ResponseClass.TripsDto tripsDto;
    ImageButton seatA1, seatA2, seatA3, seatA4, seatA5, seatA6, seatA7, seatA8, seatA9, seatA10, seatA11, seatA12,
            seatB3, seatB4, seatB5, seatB6, seatB7, seatB8, seatB9, seatB10, seatB11, seatB12;

    TextView text_view_A1, text_view_A2, text_view_A3, text_view_A4, text_view_A5, text_view_A6, text_view_A7, text_view_A8, text_view_A9, text_view_A10, text_view_A11, text_view_A12,
            text_view_B3, text_view_B4, text_view_B5, text_view_B6, text_view_B7, text_view_B8, text_view_B9, text_view_B10, text_view_B11, text_view_B12;
    LinearLayout selectedSeatsLinearLayout;
    Button bookingConfirmButton;
    Toolbar toolbar;
    String TAG = this.getClass().getSimpleName();
    RadioButton bookRadioButton, cancelBookRadioButton;
    ArrayList<SelectedSeatDto> tempSelectedSeatDtoArrayList;
    TextView seatTagTextView, amountTagTextView, rsTagTextView, selectedSeatTextView, ticketAmountTextView;
    LinearLayout ticketAndSeatLinearLayout;
    ProgressDialog progressDialog;
    Menu optionsMenu;
    int selectedSeatNumber = 0;
    int renderFlag = 0;
    int serverCall = 0;
    boolean showIndex = true;
    Double selectedSeatAmount = 0.0;
    String selectedSeatNameToCustomer = "";
    String selectedSeatNameToMechant = "";
    String journeyDate;
    String price;
    String vehilceType = "";

    TextView routNameTextView;
    TextView busNumberTextView;
    TextView departureTextView;
    ImageButton showIndexImageButton;
    SharedPreferences sharedPreferences;

    LinearLayout indexLinearLayout;


    CoordinatorLayout coordinatorLayout;
    List<String> ticketPriceArraryConverted;
    List<String> unBookedSeatsArrayConverted, bookedSeatsArrayConverted, invisibleSeatsArrayConverted, abstractSeatsArrayConverted, totalSeatsArrayConverted, seatsBookedByCustomerArrayConverted;
    ArrayList<SelectedSeatDto> selectedSeatDtoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_light_vehicle);
        getVehicleDetails();
        initViews();
        populatedToolbar();
        convertSeatsInStringToList();
        selectedSeatDtoArrayList = new ArrayList<>();
        tempSelectedSeatDtoArrayList = new ArrayList<>();
        checkVehicleTypeAndRenderSeatsAccordingly(totalSeatsArrayConverted, unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted, invisibleSeatsArrayConverted, abstractSeatsArrayConverted);
        bookingConfirmButton.setOnClickListener(this);
    }


    private void populatedToolbar() {
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            routNameTextView = (TextView) toolbar.findViewById(R.id.tv_rout_name);
            busNumberTextView = (TextView) toolbar.findViewById(R.id.tv_bus_number);
            departureTextView = (TextView) toolbar.findViewById(R.id.tv_departure_time);
            routNameTextView.setText(tripsDto.getRoute());
            busNumberTextView.setText(tripsDto.getBusNo());
            departureTextView.setText(tripsDto.getDepartureTime());

            showIndexImageButton = (ImageButton) toolbar.findViewById(R.id.ib_show_index);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            AppLog.showLog(TAG + " loggedInFirstTime boolean value::::::: " + sharedPreferences.getBoolean("loggedInFirstTime", true));
            if (sharedPreferences.contains("loggedInFirstTime")) {
                if (sharedPreferences.getBoolean("loggedInFirstTime", true)) {
                    showIndexImageButton.setVisibility(View.GONE);
                    indexLinearLayout.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedInFirstTime", false);
                    editor.apply();
                } else {
                    showIndexImageButton.setVisibility(View.VISIBLE);
                    indexLinearLayout.setVisibility(View.GONE);
                }
            }
            showIndexImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showIndex) {
                        indexLinearLayout.setVisibility(View.VISIBLE);
                        showIndex = false;

                    } else if (!showIndex) {
                        indexLinearLayout.setVisibility(View.GONE);
                        showIndex = true;
                    }

                }
            });

        }
    }

    private void getVehicleDetails() {
        tripsDto = new ResponseClass.TripsDto();
        tripsDto = getIntent().getParcelableExtra("busDetails");
        journeyDate = getIntent().getStringExtra("journeyDate");

        AppLog.showLog("In LightVehicleActivity BusType ::::" + tripsDto.getBusType());
        AppLog.showLog("In LightVehicleActivity TotalSeats ::::" + tripsDto.getTotalSeats());
        AppLog.showLog("In LightVehicleActivity BookedSeats ::::" + tripsDto.getBookedSeats());
        AppLog.showLog("In LightVehicleActivity UnbookedSeats ::::" + tripsDto.getUnbookedSeats());
        AppLog.showLog("In LightVehicleActivity Vehicle type ::::" + tripsDto.getVehicleType());
        AppLog.showLog("In LightVehicleActivity Invisible seats ::::" + tripsDto.getInvisibleSeats());
        AppLog.showLog("In LightVehicleActivity Abstract seats ::::" + tripsDto.getRemovableSeats());
        AppLog.showLog("In LightVehicleActivity Sold seats ::::" + tripsDto.getBookedByCustomer());
    }


    private void convertSeatsInStringToList() {
        totalSeatsArrayConverted = Arrays.asList(tripsDto.getTotalSeats().split("\\s*,\\s*"));
        ticketPriceArraryConverted = Arrays.asList(tripsDto.getTicketPrice().split("\\s*,\\s*"));
        if (!tripsDto.getUnbookedSeats().equalsIgnoreCase("")) {
            unBookedSeatsArrayConverted = Arrays.asList(tripsDto.getUnbookedSeats().split("\\s*,\\s*"));
            AppLog.showLog("In LightVehicleActivity Size of unBookedSeatsArrayConverted:::::::" + unBookedSeatsArrayConverted.size());
        } else
            unBookedSeatsArrayConverted = null;

        if (!tripsDto.getBookedSeats().equalsIgnoreCase("")) {
            bookedSeatsArrayConverted = Arrays.asList(tripsDto.getBookedSeats().split("\\s*,\\s*"));
            AppLog.showLog("In LightVehicleActivity Size of bookedSeatsArrayConverted:::::::" + bookedSeatsArrayConverted.size());

        } else
            bookedSeatsArrayConverted = null;

        if (!tripsDto.getRemovableSeats().equalsIgnoreCase("")) {
            abstractSeatsArrayConverted = Arrays.asList(tripsDto.getRemovableSeats().split("\\s*,\\s*"));
            AppLog.showLog("In LightVehicleActivity Size of abstractSeatsArrayConverted:::::::" + abstractSeatsArrayConverted.size());

        } else
            abstractSeatsArrayConverted = null;

        if (!tripsDto.getUnbookedSeats().equalsIgnoreCase("")) {

            invisibleSeatsArrayConverted = Arrays.asList(tripsDto.getInvisibleSeats().split("\\s*,\\s*"));
            AppLog.showLog("In LightVehicleActivity Size of invisibleSeatsArrayConverted:::::::" + invisibleSeatsArrayConverted.size());

        } else
            invisibleSeatsArrayConverted = null;

        if (!tripsDto.getBookedByCustomer().equalsIgnoreCase(""))
            seatsBookedByCustomerArrayConverted = Arrays.asList(tripsDto.getBookedByCustomer().split("\\s*,\\s*"));

        else
            seatsBookedByCustomerArrayConverted = null;


        AppLog.showLog("In LightVehicleActivity Size of totalSeatsArrayConverted:::::::" + totalSeatsArrayConverted.size());
    }


    private void checkVehicleTypeAndRenderSeatsAccordingly(List<String> totalSeatsArrayConverted, List<String> unBookedSeatsArrayConverted, List<String> bookedSeatsArrayConverted, List<String> seatsBookedByCustomerArrayConverted, List<String> invisibleSeatsArrayConverted, List<String> abstractSeatsArrayConverted) {
        AppLog.showLog("Inside renderSeats::::::::");
        if (unBookedSeatsArrayConverted != null) {
            if (!unBookedSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < unBookedSeatsArrayConverted.size(); i++) {
                    String selectionFor = "book";
                    int colonPosition = unBookedSeatsArrayConverted.get(i).indexOf(":");
                    AppLog.showLog("In LightVehicleActivity Size of unBookedSeatsArrayConverted:::::::" + unBookedSeatsArrayConverted.size());
                    AppLog.showLog("SeatName to customer in unbookedseats:::: " + unBookedSeatsArrayConverted.get(i).substring(colonPosition + 1));
                    AppLog.showLog("SeatName to merchant  in unbookedseats:::: " + unBookedSeatsArrayConverted.get(i).substring(0, colonPosition));
                    String seatNameToMerchant = unBookedSeatsArrayConverted.get(i).substring(0, colonPosition);
                    String seatNameToCustomer = unBookedSeatsArrayConverted.get(i).substring(colonPosition + 1);
                    switch (seatNameToMerchant) {
                        case "A1":
                            seatA1.setVisibility(View.VISIBLE);
                            text_view_A1.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A2":
                            seatA2.setVisibility(View.VISIBLE);
                            text_view_A2.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A3":
                            seatA3.setVisibility(View.VISIBLE);
                            text_view_A3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A4":
                            seatA4.setVisibility(View.VISIBLE);
                            text_view_A4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A5":
                            seatA5.setVisibility(View.VISIBLE);
                            text_view_A5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A6":
                            seatA6.setVisibility(View.VISIBLE);
                            text_view_A6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A7":
                            seatA7.setVisibility(View.VISIBLE);
                            text_view_A7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A8":
                            seatA8.setVisibility(View.VISIBLE);
                            text_view_A8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A9":
                            seatA9.setVisibility(View.VISIBLE);
                            text_view_A9.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A10":
                            seatA10.setVisibility(View.VISIBLE);
                            text_view_A10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A11":
                            seatA11.setVisibility(View.VISIBLE);
                            text_view_A11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A12":
                            seatA12.setVisibility(View.VISIBLE);
                            text_view_A12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B3":
                            seatB3.setVisibility(View.VISIBLE);
                            text_view_B3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B4":
                            seatB4.setVisibility(View.VISIBLE);
                            text_view_B4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B5":
                            seatB5.setVisibility(View.VISIBLE);
                            text_view_B5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B6":
                            seatB6.setVisibility(View.VISIBLE);
                            text_view_B6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B7":
                            seatB7.setVisibility(View.VISIBLE);
                            text_view_B7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B8":
                            seatB8.setVisibility(View.VISIBLE);
                            text_view_B8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B9":
                            seatB9.setVisibility(View.VISIBLE);
                            text_view_B9.setText(seatNameToCustomer);
                            AppLog.showLog("Seat Name::::::::::::" + seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B10":
                            seatB10.setVisibility(View.VISIBLE);
                            text_view_B10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B11":
                            seatB11.setVisibility(View.VISIBLE);
                            text_view_B11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B12":
                            seatB12.setVisibility(View.VISIBLE);
                            text_view_B12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;

                    }

                }
            }
        }
        if (bookedSeatsArrayConverted != null) {
            if (!bookedSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < bookedSeatsArrayConverted.size(); i++) {
                    int colonPosition = bookedSeatsArrayConverted.get(i).indexOf(":");
                    String selectionFor = "cancelBooking";
                    AppLog.showLog("SeatName to customer booked seats::::    " + bookedSeatsArrayConverted.get(i).substring(colonPosition + 1));
                    AppLog.showLog("SeatName to merchant booked seats::::" + bookedSeatsArrayConverted.get(i).substring(0, colonPosition));

                    String seatNameToMerchant = bookedSeatsArrayConverted.get(i).substring(0, colonPosition);
                    String seatNameToCustomer = bookedSeatsArrayConverted.get(i).substring(colonPosition + 1);

                    switch (seatNameToMerchant) {
                        case "A1":
                            seatA1.setVisibility(View.VISIBLE);
                            text_view_A1.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A2":
                            seatA2.setVisibility(View.VISIBLE);
                            text_view_A2.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A3":
                            seatA3.setVisibility(View.VISIBLE);
                            text_view_A3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A4":
                            seatA4.setVisibility(View.VISIBLE);
                            text_view_A4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A5":
                            seatA5.setVisibility(View.VISIBLE);
                            text_view_A5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A6":
                            seatA6.setVisibility(View.VISIBLE);
                            text_view_A6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A7":
                            seatA7.setVisibility(View.VISIBLE);
                            text_view_A7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A8":
                            seatA8.setVisibility(View.VISIBLE);
                            text_view_A8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A9":
                            seatA9.setVisibility(View.VISIBLE);
                            text_view_A9.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A10":
                            seatA10.setVisibility(View.VISIBLE);
                            text_view_A10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A11":
                            seatA11.setVisibility(View.VISIBLE);
                            text_view_A11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A12":
                            seatA12.setVisibility(View.VISIBLE);
                            text_view_A12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B3":
                            seatB3.setVisibility(View.VISIBLE);
                            text_view_B3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B4":
                            seatB4.setVisibility(View.VISIBLE);
                            text_view_B4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B5":
                            seatB5.setVisibility(View.VISIBLE);
                            text_view_B5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B6":
                            seatB6.setVisibility(View.VISIBLE);
                            text_view_B6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B7":
                            seatB7.setVisibility(View.VISIBLE);
                            text_view_B7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B8":
                            seatB8.setVisibility(View.VISIBLE);
                            text_view_B8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B9":
                            seatB9.setVisibility(View.VISIBLE);
                            text_view_B9.setText(seatNameToCustomer);
                            AppLog.showLog("Seat Name::::::::::::" + seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B10":
                            seatB10.setVisibility(View.VISIBLE);
                            text_view_B10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B11":
                            seatB11.setVisibility(View.VISIBLE);
                            text_view_B11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B12":
                            seatB12.setVisibility(View.VISIBLE);
                            text_view_B12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                    }
                }
            }
        }
        if (seatsBookedByCustomerArrayConverted != null) {
            if (!seatsBookedByCustomerArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < bookedSeatsArrayConverted.size(); i++) {

                    String seatNameToMerchant = seatsBookedByCustomerArrayConverted.get(i);
                    String seatNameToCustomer = seatsBookedByCustomerArrayConverted.get(i);

                    switch (seatNameToMerchant) {
                        case "A1":
                            seatA1.setVisibility(View.VISIBLE);
                            text_view_A1.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA1);
                            break;
                        case "A2":
                            seatA2.setVisibility(View.VISIBLE);
                            text_view_A2.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA2);
                            break;
                        case "A3":
                            seatA3.setVisibility(View.VISIBLE);
                            text_view_A3.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA3);
                            break;
                        case "A4":
                            seatA4.setVisibility(View.VISIBLE);
                            text_view_A4.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA4);
                            break;
                        case "A5":
                            seatA5.setVisibility(View.VISIBLE);
                            text_view_A5.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA5);
                            break;
                        case "A6":
                            seatA6.setVisibility(View.VISIBLE);
                            text_view_A6.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA6);
                            break;
                        case "A7":
                            seatA7.setVisibility(View.VISIBLE);
                            text_view_A7.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA7);
                            break;
                        case "A8":
                            seatA8.setVisibility(View.VISIBLE);
                            text_view_A8.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA8);
                            break;
                        case "A9":
                            seatA9.setVisibility(View.VISIBLE);
                            text_view_A9.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA9);
                            break;
                        case "A10":
                            seatA10.setVisibility(View.VISIBLE);
                            text_view_A10.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA10);
                            break;
                        case "A11":
                            seatA11.setVisibility(View.VISIBLE);
                            text_view_A11.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA11);
                            break;
                        case "A12":
                            seatA12.setVisibility(View.VISIBLE);
                            text_view_A12.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA12);
                            break;
                        case "B3":
                            seatB3.setVisibility(View.VISIBLE);
                            text_view_B3.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB3);
                            break;
                        case "B4":
                            seatB4.setVisibility(View.VISIBLE);
                            text_view_B4.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB4);
                            break;
                        case "B5":
                            seatB5.setVisibility(View.VISIBLE);
                            text_view_B5.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB5);
                            break;
                        case "B6":
                            seatB6.setVisibility(View.VISIBLE);
                            text_view_B6.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB6);
                            break;
                        case "B7":
                            seatB7.setVisibility(View.VISIBLE);
                            text_view_B7.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB7);
                            break;
                        case "B8":
                            seatB8.setVisibility(View.VISIBLE);
                            text_view_B8.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB8);
                            break;
                        case "B9":
                            seatB9.setVisibility(View.VISIBLE);
                            text_view_B9.setText(seatNameToCustomer);
                            AppLog.showLog("Seat Name::::::::::::" + seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB9);
                            break;
                        case "B10":
                            seatB10.setVisibility(View.VISIBLE);
                            text_view_B10.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB10);
                            break;
                        case "B11":
                            seatB11.setVisibility(View.VISIBLE);
                            text_view_B11.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB11);
                            break;
                        case "B12":
                            seatB12.setVisibility(View.VISIBLE);
                            text_view_B12.setText(seatNameToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB12);
                            break;
                    }
                }
            }
        }
        if (invisibleSeatsArrayConverted != null) {
            if (!invisibleSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < invisibleSeatsArrayConverted.size(); i++) {
                    AppLog.showLog("SeatName to customer in invisibleSeatsArrayConverted::::::::" + invisibleSeatsArrayConverted.get(i));
                    String seatNameToMerchant = invisibleSeatsArrayConverted.get(i);
                    switch (seatNameToMerchant) {
                        case "A1":
                            seatA1.setVisibility(View.INVISIBLE);
                            text_view_A1.setVisibility(View.INVISIBLE);
                            break;
                        case "A2":
                            seatA2.setVisibility(View.INVISIBLE);
                            text_view_A2.setVisibility(View.INVISIBLE);
                            break;
                        case "A3":
                            seatA3.setVisibility(View.INVISIBLE);
                            text_view_A3.setVisibility(View.INVISIBLE);
                            break;
                        case "A4":
                            seatA4.setVisibility(View.INVISIBLE);
                            text_view_A4.setVisibility(View.INVISIBLE);
                            break;
                        case "A5":
                            seatA5.setVisibility(View.INVISIBLE);
                            text_view_A5.setVisibility(View.INVISIBLE);
                            break;
                        case "A6":
                            seatA6.setVisibility(View.INVISIBLE);
                            text_view_A6.setVisibility(View.INVISIBLE);
                            break;
                        case "A7":
                            seatA7.setVisibility(View.INVISIBLE);
                            text_view_A7.setVisibility(View.INVISIBLE);
                            break;
                        case "A8":
                            seatA8.setVisibility(View.INVISIBLE);
                            text_view_A8.setVisibility(View.INVISIBLE);
                            break;
                        case "A9":
                            seatA9.setVisibility(View.INVISIBLE);
                            text_view_A9.setVisibility(View.INVISIBLE);
                            break;
                        case "A10":
                            seatA10.setVisibility(View.INVISIBLE);
                            text_view_A10.setVisibility(View.INVISIBLE);
                            break;
                        case "A11":
                            seatA11.setVisibility(View.INVISIBLE);
                            text_view_A11.setVisibility(View.INVISIBLE);
                            break;
                        case "A12":
                            seatA12.setVisibility(View.INVISIBLE);
                            text_view_A12.setVisibility(View.INVISIBLE);
                            break;
                        case "B3":
                            seatB3.setVisibility(View.INVISIBLE);
                            text_view_B3.setVisibility(View.INVISIBLE);
                            break;
                        case "B4":
                            seatB4.setVisibility(View.INVISIBLE);
                            text_view_B4.setVisibility(View.INVISIBLE);
                            break;
                        case "B5":
                            seatB5.setVisibility(View.INVISIBLE);
                            text_view_B5.setVisibility(View.INVISIBLE);
                            break;
                        case "B6":
                            seatB6.setVisibility(View.INVISIBLE);
                            text_view_B6.setVisibility(View.INVISIBLE);
                            break;
                        case "B7":
                            seatB7.setVisibility(View.INVISIBLE);
                            text_view_B7.setVisibility(View.INVISIBLE);
                            break;
                        case "B8":
                            seatB8.setVisibility(View.INVISIBLE);
                            text_view_B8.setVisibility(View.INVISIBLE);
                            break;
                        case "B9":
                            seatB9.setVisibility(View.INVISIBLE);
                            text_view_B9.setVisibility(View.INVISIBLE);
                            break;
                        case "B10":
                            seatB10.setVisibility(View.INVISIBLE);
                            text_view_B10.setVisibility(View.INVISIBLE);
                            break;
                        case "B11":
                            seatB11.setVisibility(View.INVISIBLE);
                            text_view_B11.setVisibility(View.INVISIBLE);
                            break;
                        case "B12":
                            seatB12.setVisibility(View.INVISIBLE);
                            text_view_B12.setVisibility(View.INVISIBLE);
                            break;
                    }

                }
            }
        }
        if (abstractSeatsArrayConverted != null) {
            if (!abstractSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < abstractSeatsArrayConverted.size(); i++) {
                    AppLog.showLog("SeatName to customer in abstractSeatsArrayConverted::::::::" + abstractSeatsArrayConverted.get(i));
                    String seatNameToMerchant = abstractSeatsArrayConverted.get(i);
                    switch (seatNameToMerchant) {
                        case "A1":
                            seatA1.setVisibility(View.GONE);
                            text_view_A1.setVisibility(View.GONE);
                            break;
                        case "A2":
                            seatA2.setVisibility(View.GONE);
                            text_view_A2.setVisibility(View.GONE);
                            break;
                        case "A3":
                            seatA3.setVisibility(View.GONE);
                            text_view_A3.setVisibility(View.GONE);
                            break;
                        case "A4":
                            seatA4.setVisibility(View.GONE);
                            text_view_A4.setVisibility(View.GONE);
                            break;
                        case "A5":
                            seatA5.setVisibility(View.GONE);
                            text_view_A5.setVisibility(View.GONE);
                            break;
                        case "A6":
                            seatA6.setVisibility(View.GONE);
                            text_view_A6.setVisibility(View.GONE);
                            break;
                        case "A7":
                            seatA7.setVisibility(View.GONE);
                            text_view_A7.setVisibility(View.GONE);
                            break;
                        case "A8":
                            seatA8.setVisibility(View.GONE);
                            text_view_A8.setVisibility(View.GONE);
                            break;
                        case "A9":
                            seatA9.setVisibility(View.GONE);
                            text_view_A9.setVisibility(View.GONE);
                            break;
                        case "A10":
                            seatA10.setVisibility(View.GONE);
                            text_view_A10.setVisibility(View.GONE);
                            break;
                        case "A11":
                            seatA11.setVisibility(View.GONE);
                            text_view_A11.setVisibility(View.GONE);
                            break;
                        case "A12":
                            seatA12.setVisibility(View.GONE);
                            text_view_A12.setVisibility(View.GONE);
                            break;
                        case "B3":
                            seatB3.setVisibility(View.GONE);
                            text_view_B3.setVisibility(View.GONE);
                            break;
                        case "B4":
                            seatB4.setVisibility(View.GONE);
                            text_view_B4.setVisibility(View.GONE);
                            break;
                        case "B5":
                            seatB5.setVisibility(View.GONE);
                            text_view_B5.setVisibility(View.GONE);
                            break;
                        case "B6":
                            seatB6.setVisibility(View.GONE);
                            text_view_B6.setVisibility(View.GONE);
                            break;
                        case "B7":
                            seatB7.setVisibility(View.GONE);
                            text_view_B7.setVisibility(View.GONE);
                            break;
                        case "B8":
                            seatB8.setVisibility(View.GONE);
                            text_view_B8.setVisibility(View.GONE);
                            break;
                        case "B9":
                            seatB9.setVisibility(View.GONE);
                            text_view_B9.setVisibility(View.GONE);
                            break;
                        case "B10":
                            seatB10.setVisibility(View.GONE);
                            text_view_B10.setVisibility(View.GONE);
                            break;
                        case "B11":
                            seatB11.setVisibility(View.GONE);
                            text_view_B11.setVisibility(View.GONE);
                            break;
                        case "B12":
                            seatB12.setVisibility(View.GONE);
                            text_view_B12.setVisibility(View.GONE);
                            break;
                    }

                }
            }
        }
    }

    public void onSubmitClicked(View view) {

    }

    public void setImageSourceForBookedByCustomerSeats(final ImageButton selectedSeat) {
        selectedSeat.setImageResource(R.drawable.sold_seats_image);
        selectedSeat.setClickable(false);
    }

    public Integer seatSelectionCounterAndAmountCalculator(ImageButton selectedSeat, Integer check, String selectedSeatNameForCustomer, String seatNameForMerchant, String selectionType) {
        tempSelectedSeatDtoArrayList = (ArrayList<SelectedSeatDto>) selectedSeatDtoArrayList.clone();
        AppLog.showLog("tempSelectedSeatDtoArrayList size inside seatSelectionCounterAndAmountCalculator::::::" + tempSelectedSeatDtoArrayList.size());
        if (check == null) {
            if (selectionType.equalsIgnoreCase("book")) {
                selectedSeat.setImageResource(R.drawable.selected_seat);
                bookRadioButton.setChecked(true);
                cancelBookRadioButton.setClickable(false);
                if (tempSelectedSeatDtoArrayList != null) {
                    for (int i = 0; i < tempSelectedSeatDtoArrayList.size(); i++) {
                        AppLog.showLog("tempSelectedSeatDtoArrayList size inside cancel caceled::::::" + tempSelectedSeatDtoArrayList.size());
                        if (tempSelectedSeatDtoArrayList.get(i).getSelectionType().equalsIgnoreCase("cancelBooking")) {
                            ImageButton imageButton = tempSelectedSeatDtoArrayList.get(i).getSeatName();
                            AppLog.showLog("Image Button inside Booking::::::::" + imageButton);
                            String seatNameToMerchant = tempSelectedSeatDtoArrayList.get(i).getSeatNameToMerchant();
                            String seatNameToCustomer = tempSelectedSeatDtoArrayList.get(i).getSeatNameToCustomer();
                            AppLog.showLog("selectedSeatNameToCustomer To Merchant before inside cancel canceled booked ::::::" + selectedSeatNameToMechant);
                            selectedSeatNameToMechant = selectedSeatNameToMechant.replaceAll(seatNameToMerchant + ",", "");
                            selectedSeatNameToCustomer = selectedSeatNameToCustomer.replaceAll(seatNameToCustomer + ",", "");
                            selectedSeatDtoArrayList.remove(tempSelectedSeatDtoArrayList.get(i));
                            AppLog.showLog("selectedSeatNameToCustomer To Merchant after inside cancel canceled  booked::::::" + selectedSeatNameToMechant);
                            AppLog.showLog("selectedSeatDtoArrayList size in cancel booked or unbooked seats::::::" + selectedSeatDtoArrayList.size());
                            setImageSourceAndOnClickListener(imageButton, seatNameToCustomer, seatNameToMerchant, "cancelBooking");
                            AppLog.showLog("tempSelectedSeatDtoArrayList size inside cancel caceled after ::::::" + tempSelectedSeatDtoArrayList.size());
                        }
                    }
                }

            } else if (selectionType.equalsIgnoreCase("cancelBooking")) {
                selectedSeat.setImageResource(R.drawable.cancel_booking_image);
                cancelBookRadioButton.setChecked(true);
                bookRadioButton.setClickable(false);
                if (tempSelectedSeatDtoArrayList != null) {
                    for (int i = 0; i < tempSelectedSeatDtoArrayList.size(); i++) {
                        AppLog.showLog("tempSelectedSeatDtoArrayList size inside cancel booked::::::" + tempSelectedSeatDtoArrayList.size());
                        if (tempSelectedSeatDtoArrayList.get(i).getSelectionType().equalsIgnoreCase("book")) {
                            ImageButton imageButton = tempSelectedSeatDtoArrayList.get(i).getSeatName();
                            AppLog.showLog("Image Button inside cancel Booking::::::::" + imageButton.toString());
                            String seatNameToMerchant = tempSelectedSeatDtoArrayList.get(i).getSeatNameToMerchant();
                            String seatNameToCustomer = tempSelectedSeatDtoArrayList.get(i).getSeatNameToCustomer();
                            selectedSeatDtoArrayList.remove(tempSelectedSeatDtoArrayList.get(i));
                            AppLog.showLog("selectedSeatNameToCustomer To Merchant before inside canceled cancel::::::" + selectedSeatNameToMechant);
                            selectedSeatNameToCustomer = selectedSeatNameToCustomer.replaceAll(seatNameToCustomer + ",", "");
                            selectedSeatNameToMechant = selectedSeatNameToMechant.replaceAll(seatNameToMerchant + ",", "");
                            AppLog.showLog("selectedSeatNameToCustomer To Merchant after inside canceled cancel::::::" + selectedSeatNameToMechant);
                            setImageSourceAndOnClickListener(imageButton, seatNameToCustomer, seatNameToMerchant, "book");

                        }
                    }
                }

            }

            SelectedSeatDto selectedSeatDto = new SelectedSeatDto();
            selectedSeatDto.setSeatName(selectedSeat);
            selectedSeatDto.setSeatId(selectedSeat.toString());
            selectedSeatDto.setSeatNameToCustomer(selectedSeatNameForCustomer);
            selectedSeatDto.setSeatNameToMerchant(seatNameForMerchant);
            selectedSeatDto.setSelectionType(selectionType);
            selectedSeatDtoArrayList.add(selectedSeatDto);
            selectedSeatNameToCustomer += selectedSeatNameForCustomer + ",";
            selectedSeatNameToMechant += seatNameForMerchant + ",";
            checkSeatSelectionVisibility(selectedSeatNameToCustomer);
            AppLog.showLog("selectedSeatDtoArrayList size::::::" + selectedSeatDtoArrayList.size());
            AppLog.showLog("selection Type::::::" + selectedSeatDto.getSelectionType());
            AppLog.showLog("selectedSeatNameToCustomer To Customer::::::" + selectedSeatNameToCustomer);
            AppLog.showLog("selectedSeatNameToCustomer To Merchant::::::" + selectedSeatNameToMechant);

            return 1;


        } else if (check != null) {
            if (selectionType.equalsIgnoreCase("book")) {
                selectedSeat.setImageResource(R.drawable.available_seat);

            } else if (selectionType.equalsIgnoreCase("cancelBooking")) {
                selectedSeat.setImageResource(R.drawable.booked_seat);
            }


            ///    if(!selectedSeatDtoArrayList.isEmpty()) {
            for (int i = 0; i < selectedSeatDtoArrayList.size(); i++) {
                AppLog.showLog("Unselected seat and equivalent seat in arraylist:::::::::" + "  " + selectedSeatDtoArrayList.get(i).getSeatId() + " " + selectedSeat.toString());
                AppLog.showLog("Unselected seat name:::::::" + selectedSeatNameForCustomer);
                AppLog.showLog("Unselected seat name2 in selectedSeatDtoArrayList:::::::" + selectedSeatDtoArrayList.get(i).getSeatNameToCustomer());
                if (selectedSeatDtoArrayList.get(i).getSeatNameToCustomer().equalsIgnoreCase(selectedSeatNameForCustomer)) {
                    AppLog.showLog("Unselected seat and equivalent seat in arraylist:::::::::" + "  " + selectedSeatDtoArrayList.get(i).getSeatId() + " " + selectedSeat.toString());
                    selectedSeatDtoArrayList.remove(selectedSeatDtoArrayList.get(i));
                }
            }
            //      }
            selectedSeatNameToCustomer = selectedSeatNameToCustomer.replaceAll(selectedSeatNameForCustomer + ",", "");
            selectedSeatNameToMechant = selectedSeatNameToMechant.replaceAll(seatNameForMerchant + ",", "");
            AppLog.showLog("selectedSeatNameToCustomer To Customer::::::" + selectedSeatNameToCustomer);
            AppLog.showLog("selectedSeatNameToCustomer To Merchant::::::" + selectedSeatNameToCustomer);
            AppLog.showLog("selectedSeatDtoArrayList size::::::" + selectedSeatDtoArrayList.size());
            checkSeatSelectionVisibility(selectedSeatNameToCustomer);
            return null;
        }
        return null;
    }

    private void checkSeatSelectionVisibility(String selectedSeatName) {

        if (!selectedSeatName.equalsIgnoreCase("")) {
            selectedSeatTextView.setText(selectedSeatName.substring(0, selectedSeatName.length() - 1));
            if (bookRadioButton.isChecked()) {
                seatTagTextView.setText("Seat(s) selected to book");
                bookingConfirmButton.setText("BOOK SELECTED SEATS");
            } else {
                seatTagTextView.setText("Seat(s) selected to unbook");
                bookingConfirmButton.setText("FREE SELECTED SEATS");
            }
            selectedSeatsLinearLayout.setVisibility(View.VISIBLE);
            bookingConfirmButton.setVisibility(View.VISIBLE);
        } else {
            selectedSeatsLinearLayout.setVisibility(View.GONE);
            bookingConfirmButton.setVisibility(View.GONE);
        }
    }


    public void setImageSourceAndOnClickListener(final ImageButton seatButton, final String seatNameToCustomer, final String seatNameToMerchant, final String selectionType) {
        seatButton.setClickable(true);
        AppLog.showLog("tempSelectedSeatDtoArrayList size inside setImageSourceAndOnClickListener::::::" + tempSelectedSeatDtoArrayList.size());
        if (selectionType.equalsIgnoreCase("book"))
            seatButton.setImageResource(R.drawable.available_seat);
        else
            seatButton.setImageResource(R.drawable.booked_seat);
        AppLog.showLog("tempSelectedSeatDtoArrayList size inside setImageSourceAndOnClickListener 1::::::" + tempSelectedSeatDtoArrayList.size());

        seatButton.setOnClickListener(new View.OnClickListener() {
            Integer check;

            @Override
            public void onClick(View v) {
                AppLog.showLog("tempSelectedSeatDtoArrayList size inside setImageSourceAndOnClickListener 2::::::" + tempSelectedSeatDtoArrayList.size());
                check = seatSelectionCounterAndAmountCalculator(seatButton, check, seatNameToCustomer, seatNameToMerchant, selectionType);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_light_vehicle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        clearCache();
        Intent returnIntent = new Intent();
        returnIntent.setFlags(RESULT_CANCELED);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);

    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh_button_light_vehicle);
            if (refreshItem != null) {
                if (refreshing) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        refreshItem.setActionView(null);
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.refresh_button_light_vehicle:
                serverCall = 1;
                if (AppUtil.isInternetConnectionAvailable(LightVehicleActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
                    setRefreshActionButtonState(true);
                    String url = APIs.REFRESH_SEATS_MAPPING;
                    AppLog.showLog(TAG + " url:::::" + url);

                    RequestQueue queue = Volley.newRequestQueue(LightVehicleActivity.this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", tripsDto.getId());
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

                } else {
                    Utility.showSnackbar(coordinatorLayout, AppTexts.NO_INTERNET_MESSAGE_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Response.Listener<ResponseClass> createMyReqSuccessListener() {
        return new Response.Listener<ResponseClass>() {
            @Override
            public void onResponse(final ResponseClass response) {
                AppLog.showLog(TAG + " Success Response::::::" + response.getmStatus());


                String status = response.getmStatus();

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                setRefreshActionButtonState(false);

                if (status.equalsIgnoreCase("fail")) {
                    String cases = response.getmCase();
                    if (cases.equalsIgnoreCase("locked")) {
                        Context ctx = LightVehicleActivity.this;
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ctx);
                        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                        alert.setView(customView);
                        alert.setTitle(null);
                        alert.setCustomTitle(null);
                        TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                        TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                        titleTextViewInDialogue.setText("Trip Locked !");
                        messageTextViewInDialogue.setText(response.getmMessage());
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        android.app.AlertDialog dialog = alert.create();
                        dialog.show();


                    } else if (cases.equalsIgnoreCase("booked")) {

                        Context ctx = LightVehicleActivity.this;
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ctx);
                        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                        alert.setView(customView);
                        alert.setTitle(null);
                        alert.setCustomTitle(null);
                        TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                        TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                        titleTextViewInDialogue.setText("Seats already booked!");
                        messageTextViewInDialogue.setText(response.getmMessage());
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        android.app.AlertDialog dialog = alert.create();
                        dialog.show();

                    } else {
                        Context ctx = LightVehicleActivity.this;
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
                                startActivity(new Intent(LightVehicleActivity.this, RegistrationActivity.class));
                                finish();
                            }
                        });
                        android.app.AlertDialog dialog = alert.create();
                        dialog.show();
                    }


                } else if (status.equalsIgnoreCase("success")) {
                    if (serverCall == 1) {
                        String unbookedSeats = response.getmUnbookedSeats();
                        String bookedSeats = response.getmBookedSeats();
                        String bookedSeatsbyCustomer = response.getmBookedByCustomer();
                        AppLog.showLog("Unbooked Seats:::::" + unbookedSeats);
                        AppLog.showLog("Booked Seats:::::" + bookedSeats);
                        AppLog.showLog("Booked Seats by Customer:::::" + bookedSeatsbyCustomer);
                        final List<String> unBookedSeatsArrayConverted = Arrays.asList(unbookedSeats.split("\\s*,\\s*"));
                        final List<String> bookedSeatsArrayConverted = Arrays.asList(bookedSeats.split("\\s*,\\s*"));
                        final List<String> seatsBookedByCustomerArrayConverted = Arrays.asList(bookedSeatsbyCustomer.split("\\s*,\\s*"));
                        checkVehicleTypeAndRenderSeatsAccordingly(totalSeatsArrayConverted, unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted, invisibleSeatsArrayConverted, abstractSeatsArrayConverted);
                        clearCache();
                        checkSeatSelectionVisibility(selectedSeatNameToCustomer);
                    } else {

                        Context ctx = LightVehicleActivity.this;
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ctx);
                        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
                        alert.setView(customView);
                        alert.setTitle(null);
                        alert.setCustomTitle(null);
                        TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
                        TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
                        // You Can Customise your Title here
                        titleTextViewInDialogue.setText("Successfull !");
                        messageTextViewInDialogue.setText(response.getmMessage());
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                AppLog.showLog("UnbookedSeats::::::" + response.getmUnbookedSeats());
                                AppLog.showLog("BookedSeats::::::" + response.getmBookedSeats());

                                if (!response.getmUnbookedSeats().equalsIgnoreCase("")) {
                                    unBookedSeatsArrayConverted = Arrays.asList(response.getmUnbookedSeats().split("\\s*,\\s*"));
                                    AppLog.showLog("UnbookedSeatsArrayConverted::::::" + unBookedSeatsArrayConverted.size());
                                } else
                                    unBookedSeatsArrayConverted = null;

                                if (!response.getmBookedSeats().equalsIgnoreCase("")) {
                                    bookedSeatsArrayConverted = Arrays.asList(response.getmBookedSeats().split("\\s*,\\s*"));
                                    AppLog.showLog("BookedSeatsArrayConverted::::::" + bookedSeatsArrayConverted.size());
                                } else
                                    bookedSeatsArrayConverted = null;
                                if (!response.getmBookedByCustomer().equalsIgnoreCase("")) {
                                    seatsBookedByCustomerArrayConverted = Arrays.asList(response.getmBookedByCustomer().split("\\s*,\\s*"));
                                    AppLog.showLog("bookedByCustomerArrayConverted::::::" + seatsBookedByCustomerArrayConverted.size());
                                } else
                                    seatsBookedByCustomerArrayConverted = null;

                                clearCache();
                                checkSeatSelectionVisibility(selectedSeatNameToCustomer);
                                checkVehicleTypeAndRenderSeatsAccordingly(totalSeatsArrayConverted, unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted, invisibleSeatsArrayConverted, abstractSeatsArrayConverted);

                            }
                        });
                        android.app.AlertDialog dialog = alert.create();
                        dialog.show();

                    }

                }
            }
        };

    }

    private void clearCache() {
        selectedSeatDtoArrayList.clear();
        serverCall = 0;
        selectedSeatNumber = 0;
        selectedSeatNameToCustomer = "";
        selectedSeatNameToMechant = "";
        selectedSeatAmount = 0.0;
    }

    private void initViews() {
        indexLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_seats_index);
        bookRadioButton = (RadioButton) findViewById(R.id.book_radiobutton);
        cancelBookRadioButton = (RadioButton) findViewById(R.id.cancel_book_radiobutton);
        selectedSeatsLinearLayout = (LinearLayout) findViewById(R.id.selected_seat_linear_layout);
        seatA1 = (ImageButton) findViewById(R.id.A1);
        seatA2 = (ImageButton) findViewById(R.id.A2);
        seatA3 = (ImageButton) findViewById(R.id.A3);
        seatA4 = (ImageButton) findViewById(R.id.A4);
        seatA5 = (ImageButton) findViewById(R.id.A5);
        seatA6 = (ImageButton) findViewById(R.id.A6);
        seatA7 = (ImageButton) findViewById(R.id.A7);
        seatA8 = (ImageButton) findViewById(R.id.A8);
        seatA9 = (ImageButton) findViewById(R.id.A9);
        seatA10 = (ImageButton) findViewById(R.id.A10);
        seatA11 = (ImageButton) findViewById(R.id.A11);
        seatA12 = (ImageButton) findViewById(R.id.A12);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        seatB3 = (ImageButton) findViewById(R.id.B3);
        seatB4 = (ImageButton) findViewById(R.id.B4);
        seatB5 = (ImageButton) findViewById(R.id.B5);
        seatB6 = (ImageButton) findViewById(R.id.B6);
        seatB7 = (ImageButton) findViewById(R.id.B7);
        seatB8 = (ImageButton) findViewById(R.id.B8);
        seatB9 = (ImageButton) findViewById(R.id.B9);
        seatB10 = (ImageButton) findViewById(R.id.B10);
        seatB11 = (ImageButton) findViewById(R.id.B11);
        seatB12 = (ImageButton) findViewById(R.id.B12);

        text_view_A1 = (TextView) findViewById(R.id.TextView_A1);
        text_view_A2 = (TextView) findViewById(R.id.TextView_A2);
        text_view_A3 = (TextView) findViewById(R.id.TextView_A3);
        text_view_A4 = (TextView) findViewById(R.id.TextView_A4);
        text_view_A5 = (TextView) findViewById(R.id.TextView_A5);
        text_view_A6 = (TextView) findViewById(R.id.TextView_A6);
        text_view_A7 = (TextView) findViewById(R.id.TextView_A7);
        text_view_A8 = (TextView) findViewById(R.id.TextView_A8);
        text_view_A9 = (TextView) findViewById(R.id.TextView_A9);
        text_view_A10 = (TextView) findViewById(R.id.TextView_A10);
        text_view_A11 = (TextView) findViewById(R.id.TextView_A11);
        text_view_A12 = (TextView) findViewById(R.id.TextView_A12);

        text_view_B3 = (TextView) findViewById(R.id.TextView_B3);
        text_view_B4 = (TextView) findViewById(R.id.TextView_B4);
        text_view_B5 = (TextView) findViewById(R.id.TextView_B5);
        text_view_B6 = (TextView) findViewById(R.id.TextView_B6);
        text_view_B7 = (TextView) findViewById(R.id.TextView_B7);
        text_view_B8 = (TextView) findViewById(R.id.TextView_B8);
        text_view_B9 = (TextView) findViewById(R.id.TextView_B9);
        text_view_B10 = (TextView) findViewById(R.id.TextView_B10);
        text_view_B11 = (TextView) findViewById(R.id.TextView_B11);
        text_view_B12 = (TextView) findViewById(R.id.TextView_B12);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        seatTagTextView = (TextView) findViewById(R.id.selected_seats_tag);
        selectedSeatTextView = (TextView) findViewById(R.id.selected_seat_text_view);
        bookingConfirmButton = (Button) findViewById(R.id.confirm_booking_button);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_booking_button) {
            final Context ctx = LightVehicleActivity.this;
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.empty_alert_dialog, null);
            alert.setView(customView);
            alert.setTitle(null);
            alert.setCustomTitle(null);
            TextView titleTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_title);
            TextView messageTextViewInDialogue = (TextView) customView.findViewById(R.id.textview_dialog_message);
            // You Can Customise your Title here
            titleTextViewInDialogue.setText("Are you sure ?");
            if (bookRadioButton.isChecked())
                messageTextViewInDialogue.setText("Do you want to continue booking");
            else
                messageTextViewInDialogue.setText("Do you want to continue cancel booking ? ");

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    callApi();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            android.app.AlertDialog dialog = alert.create();
            dialog.show();

        }

    }

    private void callApi() {

        if (AppUtil.isInternetConnectionAvailable(LightVehicleActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
            progressDialog = AppUtil.createProgressDialog(this);
            progressDialog.show();

            String url = "";
            if (bookRadioButton.isChecked())
                url = APIs.SUBMIT_BOOKING__MAPPING;
            else if (cancelBookRadioButton.isChecked())
                url = APIs.SUBMIT__CANCEL_BOOKING_MAPPING;

            AppLog.showLog(TAG + " url:::::" + url);

            RequestQueue queue = Volley.newRequestQueue(LightVehicleActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", tripsDto.getId());
                jsonObject.put("seats", selectedSeatNameToCustomer);
                jsonObject.put("signature", Utility.encryption(tripsDto.getId() + selectedSeatNameToCustomer + "userOperator"));
                // jsonObject.put("mobileSrlNo", AppUtil.getDeviceId(HeavyVehicleActivity.this));
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

        } else {
            Utility.showSnackbar(coordinatorLayout, AppTexts.NO_INTERNET_MESSAGE_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
        }

    }
}
