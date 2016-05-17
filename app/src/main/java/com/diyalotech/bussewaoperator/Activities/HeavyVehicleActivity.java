package com.diyalotech.bussewaoperator.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.RelativeLayout;
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

public class HeavyVehicleActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton sample4, sample5, sample6, seatC1, seatC2, seatC3, seatC4, seatC5, seatA1, seatA2, seatA3, seatA4, seatA5, seatA6, seatA7, seatA8, seatA9, seatA10, seatA11, seatA12, seatA13, seatA14, seatA15, seatA16, seatB1, seatB2, seatB3, seatB4, seatB5, seatB6, seatB7,
            seatB8, seatB9, seatB10, seatB11, seatB12, seatB13, seatB14, seatB15, seatB16, seatB17, seatB18, seatL1, seatL2, seatL3, seatL4, seatL5, seatL6;

    TextView text_view_C1, text_view_C2, text_view_C3, text_view_C4, text_view_C5, text_view_A1, text_view_A2, text_view_A3, text_view_A4, text_view_A5, text_view_A6, text_view_A7, text_view_A8, text_view_A9, text_view_A10, text_view_A11, text_view_A12, text_view_A13, text_view_A14,
            text_view_A15, text_view_A16, text_view_B1, text_view_B2, text_view_B3, text_view_B4, text_view_B5, text_view_B6, text_view_B7, text_view_B8, text_view_B9, text_view_B10, text_view_B11, text_view_B12, text_view_B13, text_view_B14, text_view_B15, text_view_B16, text_view_B17,
            text_view_B18, text_view_L1, text_view_L2, text_view_L3, text_view_L4, text_view_L5;

    Menu optionsMenu;

    ResponseClass.TripsDto tripsDto;

    Toolbar toolbar;

    String TAG = this.getClass().getSimpleName();

    Button bookingConfirmButton;

    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    boolean showIndex = true;

    ArrayList<SelectedSeatDto> selectedSeatDtoArrayList;
    ArrayList<SelectedSeatDto> tempSelectedSeatDtoArrayList;

    int serverCall = 0;
    int selectedSeatNumber = 0;
    int renderFlag = 0;
    Double selectedSeatAmount = 0.0;

    LinearLayout selectedSeatsLinearLayout;

    TextView seatTagTextView, selectedSeatTextView;
    RadioButton bookRadioButton, cancelBookRadioButton;

    RelativeLayout relativeLayout;
    LinearLayout indexLinearLayout;
    String journeyDate;
    String selectedSeatNameToCustomer = "";
    String selectedSeatNameToMechant = "";

    String bookedSeatsString = "";
    String unbookedSeatsString = "";
    String bookedSeatsbyCustomerString = "";

    Button indexButton;
    SharedPreferences sharedPreferences;

    TextView routNameTextView;
    TextView busNumberTextView;
    TextView departureTextView;
    ImageButton showIndexImageButton;
    List<String> ticketPriceArraryConverted;
    List<String> unBookedSeatsArrayConverted, bookedSeatsArrayConverted, totalSeatsArrayConverted, seatsBookedByCustomerArrayConverted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_heavy_vehicle);
        getVehicleDetails();
        initViews();
        populatedToolbar();
        convertSeatsInStringToList();
        selectedSeatDtoArrayList = new ArrayList<>();
        tempSelectedSeatDtoArrayList = new ArrayList<>();
        checkBusSeatNumbersAndRenderSeatsAccordingly();
        bookingConfirmButton.setOnClickListener(this);
//        indexButton.setOnClickListener(this);
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
        journeyDate = getIntent().getStringExtra("journeyDate");
        tripsDto = getIntent().getParcelableExtra("busDetails");
        bookedSeatsString = tripsDto.getBookedSeats();
        unbookedSeatsString = tripsDto.getUnbookedSeats();
        bookedSeatsbyCustomerString = tripsDto.getBookedByCustomer();
        AppLog.showLog("Size of tripsDto HeavyVehicleActivity::::" + tripsDto.getBusType());
        AppLog.showLog("Size of TotalSeats HeavyVehicleActivity::::" + tripsDto.getTotalSeats());
        AppLog.showLog("Size of BookedSeats HeavyVehicleActivity::::" + tripsDto.getBookedSeats());
        AppLog.showLog("Size of UnbookedSeats HeavyVehicleActivity::::" + tripsDto.getUnbookedSeats());
    }

    private void convertSeatsInStringToList() {

        totalSeatsArrayConverted = Arrays.asList(tripsDto.getTotalSeats().split("\\s*,\\s*"));
        AppLog.showLog("Size oftotalSeatsArrayConverted:::::::" + totalSeatsArrayConverted.size());

        ticketPriceArraryConverted = Arrays.asList(tripsDto.getTicketPrice().split("\\s*,\\s*"));
        if (!tripsDto.getUnbookedSeats().equalsIgnoreCase("")) {
            unBookedSeatsArrayConverted = Arrays.asList(tripsDto.getUnbookedSeats().split("\\s*,\\s*"));
            AppLog.showLog("In HeavyVehicleActivity Size of unBookedSeatsArrayConverted:::::::" + unBookedSeatsArrayConverted.size());
        } else
            unBookedSeatsArrayConverted = null;

        if (!tripsDto.getBookedSeats().equalsIgnoreCase("")) {
            bookedSeatsArrayConverted = Arrays.asList(tripsDto.getBookedSeats().split("\\s*,\\s*"));
            AppLog.showLog("In HeavyVehicleActivity Size of bookedSeatsArrayConverted:::::::" + bookedSeatsArrayConverted.size());

        } else
            bookedSeatsArrayConverted = null;
        if (!tripsDto.getBookedByCustomer().equalsIgnoreCase("")) {
            seatsBookedByCustomerArrayConverted = Arrays.asList(tripsDto.getBookedByCustomer().split("\\s*,\\s*"));
            AppLog.showLog("Size BookedSeatsByCustomerArrayConverted:::::::" + seatsBookedByCustomerArrayConverted.size());
        } else
            seatsBookedByCustomerArrayConverted = null;


        ticketPriceArraryConverted = Arrays.asList(tripsDto.getTicketPrice().split("\\s*,\\s*"));
        AppLog.showLog("Size ofticketPriceArrayConverted:::::::" + ticketPriceArraryConverted.size());
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
                    if (cases != null) {
                        if (cases.equalsIgnoreCase("locked")) {
                            final Dialog dialog = new Dialog(HeavyVehicleActivity.this);
                            dialog.setContentView(R.layout.dialog_view);
                            dialog.setTitle("Trip Locked!");

                            Button dialogButton = (Button) dialog.findViewById(R.id.ok_button);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

                        } else if (cases.equalsIgnoreCase("booked")) {
                            String message = response.getmMessage();
                            new AlertDialog.Builder(HeavyVehicleActivity.this)
                                    .setTitle("Seats already booked!")
                                    .setMessage(message)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            Context ctx = HeavyVehicleActivity.this;
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
                                    startActivity(new Intent(HeavyVehicleActivity.this, RegistrationActivity.class));
                                    finish();
                                }
                            });
                            android.app.AlertDialog dialog = alert.create();
                            dialog.show();
                        }
                    }
                } else if (status.equalsIgnoreCase("success")) {
                    if (serverCall == 1) {
                        String unbookedSeats = response.getmUnbookedSeats();
                        String bookedSeats = response.getmBookedSeats();
                        String bookedSeatsbyCustomer = response.getmBookedByCustomer();
                        AppLog.showLog("Unbooked Seats:::::" + unbookedSeats);
                        AppLog.showLog("Booked Seats:::::" + bookedSeats);
                        AppLog.showLog("Booked Seats by Customer:::::" + bookedSeatsbyCustomer);
//                        List<String> unBookedSeatsArrayConverted = null;
//                        List<String> bookedSeatsArrayConverted = null;
//                        List<String> bookedByCustomerArrayConverted = null;
                        bookedSeatsString = response.getmBookedSeats();
                        unbookedSeatsString = response.getmUnbookedSeats();
                        bookedSeatsbyCustomerString = response.getmBookedByCustomer();

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
                        renderSeats(unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted);
                    } else {

                        Context ctx = HeavyVehicleActivity.this;
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
//                                List<String> unBookedSeatsArrayConverted = null;
//                                List<String> bookedSeatsArrayConverted = null;
//                                List<String> bookedByCustomerArrayConverted = null;
                                bookedSeatsString = response.getmBookedSeats();
                                unbookedSeatsString = response.getmUnbookedSeats();
                                bookedSeatsbyCustomerString = response.getmBookedByCustomer();
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
                                renderSeats(unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted);
                            }
                        });
                        android.app.AlertDialog dialog = alert.create();
                        dialog.show();


                    }
                }
            }
        };
    }

    public void setImageSourceAndOnClickListener(final ImageButton seatButton, final String seatNameToCustomer, final String seatNameToMerchant, final String selectionType) {
        seatButton.setClickable(true);
        if (selectionType.equalsIgnoreCase("book"))
            seatButton.setImageResource(R.drawable.available_seat);
        else
            seatButton.setImageResource(R.drawable.booked_seat);

        seatButton.setOnClickListener(new View.OnClickListener() {
            Integer check;

            @Override
            public void onClick(View v) {
                AppLog.showLog("tempSelectedSeatDtoArrayList size inside setImageSourceAndOnClickListener 2::::::" + tempSelectedSeatDtoArrayList.size());
                check = seatSelectionCounterAndAmountCalculator(seatButton, check, seatNameToCustomer, seatNameToMerchant, selectionType);
            }
        });
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


    public void setImageSourceForBookedByCustomerSeats(final ImageButton selectedSeat) {
        selectedSeat.setImageResource(R.drawable.sold_seats_image);
        selectedSeat.setClickable(false);
    }


    private void initViews() {
        selectedSeatsLinearLayout = (LinearLayout) findViewById(R.id.selected_seat_linear_layout);
        indexLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_seats_index);
        seatTagTextView = (TextView) findViewById(R.id.selected_seats_tag);
        selectedSeatTextView = (TextView) findViewById(R.id.selected_seat_text_view);
        bookRadioButton = (RadioButton) findViewById(R.id.book_radiobutton);
        cancelBookRadioButton = (RadioButton) findViewById(R.id.cancel_book_radiobutton);
        // indexButton = (Button) findViewById(R.id.button_index);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_heavyvehicle);
        sample4 = (ImageButton) findViewById(R.id.sample4);
        sample5 = (ImageButton) findViewById(R.id.sample5);
        sample6 = (ImageButton) findViewById(R.id.sample6);
        seatC1 = (ImageButton) findViewById(R.id.C1);
        seatC2 = (ImageButton) findViewById(R.id.C2);
        seatC3 = (ImageButton) findViewById(R.id.C3);
        seatC4 = (ImageButton) findViewById(R.id.C4);
        seatC5 = (ImageButton) findViewById(R.id.C5);
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
        seatA13 = (ImageButton) findViewById(R.id.A13);
        seatA14 = (ImageButton) findViewById(R.id.A14);
        seatA15 = (ImageButton) findViewById(R.id.A15);
        seatA16 = (ImageButton) findViewById(R.id.A16);
        seatB1 = (ImageButton) findViewById(R.id.B1);
        seatB2 = (ImageButton) findViewById(R.id.B2);
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
        seatB13 = (ImageButton) findViewById(R.id.B13);
        seatB14 = (ImageButton) findViewById(R.id.B14);
        seatB15 = (ImageButton) findViewById(R.id.B15);
        seatB16 = (ImageButton) findViewById(R.id.B16);
        seatB17 = (ImageButton) findViewById(R.id.B17);
        seatB18 = (ImageButton) findViewById(R.id.B18);
        seatL1 = (ImageButton) findViewById(R.id.L1);
        seatL2 = (ImageButton) findViewById(R.id.L2);
        seatL3 = (ImageButton) findViewById(R.id.L3);
        seatL4 = (ImageButton) findViewById(R.id.L4);
        seatL5 = (ImageButton) findViewById(R.id.L5);
        seatL6 = (ImageButton) findViewById(R.id.L6);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        text_view_C1 = (TextView) findViewById(R.id.TextView_C1);
        text_view_C2 = (TextView) findViewById(R.id.TextView_C2);
        text_view_C3 = (TextView) findViewById(R.id.TextView_C3);
        text_view_C4 = (TextView) findViewById(R.id.TextView_C4);
        text_view_C5 = (TextView) findViewById(R.id.TextView_C5);
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
        text_view_A13 = (TextView) findViewById(R.id.TextView_A13);
        text_view_A14 = (TextView) findViewById(R.id.TextView_A14);
        text_view_A15 = (TextView) findViewById(R.id.TextView_A15);
        text_view_A16 = (TextView) findViewById(R.id.TextView_A16);
        text_view_B1 = (TextView) findViewById(R.id.TextView_B1);
        text_view_B2 = (TextView) findViewById(R.id.TextView_B2);
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
        text_view_B13 = (TextView) findViewById(R.id.TextView_B13);
        text_view_B14 = (TextView) findViewById(R.id.TextView_B14);
        text_view_B15 = (TextView) findViewById(R.id.TextView_B15);
        text_view_B16 = (TextView) findViewById(R.id.TextView_B16);
        text_view_B17 = (TextView) findViewById(R.id.TextView_B17);
        text_view_B18 = (TextView) findViewById(R.id.TextView_B18);
        text_view_L1 = (TextView) findViewById(R.id.TextView_L1);
        text_view_L2 = (TextView) findViewById(R.id.TextView_L2);
        text_view_L3 = (TextView) findViewById(R.id.TextView_L3);
        text_view_L4 = (TextView) findViewById(R.id.TextView_L4);
        text_view_L5 = (TextView) findViewById(R.id.TextView_L5);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bookingConfirmButton = (Button) findViewById(R.id.confirm_booking_button);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_heavy_vehicle, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.refresh_button_heavy_vehicle:
                serverCall = 1;
                if (AppUtil.isInternetConnectionAvailable(HeavyVehicleActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
                    setRefreshActionButtonState(true);
                    String url = APIs.REFRESH_SEATS_MAPPING;
                    AppLog.showLog(TAG + " url:::::" + url);

                    RequestQueue queue = Volley.newRequestQueue(HeavyVehicleActivity.this);
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


    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh_button_heavy_vehicle);
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

    private void checkBusSeatNumbersAndRenderSeatsAccordingly() {
        if (totalSeatsArrayConverted.get(0).equalsIgnoreCase("A:16")) {
            seatA15.setVisibility(View.VISIBLE);
            seatA16.setVisibility(View.VISIBLE);
            seatB17.setVisibility(View.VISIBLE);
            seatB18.setVisibility(View.VISIBLE);
            sample4.setVisibility(View.INVISIBLE);
            text_view_A15.setVisibility(View.GONE);
            text_view_A16.setVisibility(View.GONE);
            text_view_B17.setVisibility(View.VISIBLE);
            text_view_B18.setVisibility(View.VISIBLE);
        }
        if (totalSeatsArrayConverted.get(0).equalsIgnoreCase("A:14") && totalSeatsArrayConverted.get(1).equalsIgnoreCase("A1:18")) {
            seatA15.setVisibility(View.GONE);
            text_view_A15.setVisibility(View.GONE);
            seatA16.setVisibility(View.GONE);
            text_view_A16.setVisibility(View.GONE);
            seatB17.setVisibility(View.VISIBLE);
            text_view_B17.setVisibility(View.VISIBLE);
            seatB18.setVisibility(View.VISIBLE);
            text_view_B18.setVisibility(View.VISIBLE);
            sample5.setVisibility(View.INVISIBLE);
            sample6.setVisibility(View.INVISIBLE);

        }
        if (totalSeatsArrayConverted.get(2).equalsIgnoreCase("L:0")) {
            seatL1.setVisibility(View.GONE);
            seatL2.setVisibility(View.GONE);
            seatL3.setVisibility(View.GONE);
            seatL4.setVisibility(View.GONE);
            seatL5.setVisibility(View.GONE);
            text_view_L1.setVisibility(View.GONE);
            text_view_L2.setVisibility(View.GONE);
            text_view_L3.setVisibility(View.GONE);
            text_view_L4.setVisibility(View.GONE);
            text_view_L5.setVisibility(View.GONE);
        }

        if (totalSeatsArrayConverted.get(3).equalsIgnoreCase("C:0")) {
            seatC1.setVisibility(View.INVISIBLE);
            seatC2.setVisibility(View.INVISIBLE);
            seatC3.setVisibility(View.INVISIBLE);
            seatC4.setVisibility(View.INVISIBLE);
            text_view_C1.setVisibility(View.INVISIBLE);
            text_view_C2.setVisibility(View.INVISIBLE);
            text_view_C3.setVisibility(View.INVISIBLE);
            text_view_C4.setVisibility(View.INVISIBLE);

        }
        if (totalSeatsArrayConverted.get(3).equalsIgnoreCase("C:1")) {
            seatC2.setVisibility(View.INVISIBLE);
            seatC3.setVisibility(View.INVISIBLE);
            seatC4.setVisibility(View.INVISIBLE);

            text_view_C2.setVisibility(View.INVISIBLE);
            text_view_C3.setVisibility(View.INVISIBLE);
            text_view_C4.setVisibility(View.INVISIBLE);

        }
        if (totalSeatsArrayConverted.get(3).equalsIgnoreCase("C:5")) {
            seatC5.setVisibility(View.VISIBLE);

            text_view_C5.setVisibility(View.INVISIBLE);
        }
        renderSeats(unBookedSeatsArrayConverted, bookedSeatsArrayConverted, seatsBookedByCustomerArrayConverted);

    }


    void renderSeats(List<String> unBookedSeatsArrayConverted, List<String> bookedSeatsArrayConverted, List<String> seatsBookedByCustomerArrayConverted) {


        AppLog.showLog("Inside renderSeats::::::::");
//        AppLog.showLog("Inside renderSeats unBookedSeatsArrayConverted::::::"+unBookedSeatsArrayConverted.size());
//        AppLog.showLog("Inside renderSeats bookedSeatsArrayConverted::::::"+bookedSeatsArrayConverted.size());
        if (unBookedSeatsArrayConverted != null) {
            if (!unBookedSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < unBookedSeatsArrayConverted.size(); i++) {
                    String selectionFor = "book";
                    int colonPosition = unBookedSeatsArrayConverted.get(i).indexOf(":");
                    AppLog.showLog("SeatName to customer in unbookedseats::::    " + unBookedSeatsArrayConverted.get(i).substring(colonPosition + 1));
                    AppLog.showLog("SeatName to merchant  in unbookedseats::::" + unBookedSeatsArrayConverted.get(i).substring(0, colonPosition));
                    String seatNameToMerchant = unBookedSeatsArrayConverted.get(i).substring(0, colonPosition);
                    String seatNameToCustomer = unBookedSeatsArrayConverted.get(i).substring(colonPosition + 1);

                    switch (seatNameToMerchant) {

                        case "C1":
                            text_view_C1.setText(seatNameToCustomer);
                            // checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_C1, seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatC1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "C2":
                            text_view_C2.setText(seatNameToCustomer);
                            //checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_C1, seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatC2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "C3":
                            text_view_C3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatC3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "C4":
                            text_view_C4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatC4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "C5":
                            text_view_C5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatC5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A1":
                            text_view_A1.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A2":
                            text_view_A2.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A3":
                            text_view_A3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A4":
                            text_view_A4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A5":
                            text_view_A5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A6":
                            text_view_A6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A7":
                            text_view_A7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A8":
                            text_view_A8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A9":
                            text_view_A9.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A10":
                            text_view_A10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A11":
                            text_view_A11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A12":
                            text_view_A12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A13":
                            text_view_A13.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA13, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A14":
                            text_view_A14.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA14, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A15":
                            text_view_A15.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA15, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "A16":
                            text_view_A16.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatA16, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B1":

                            text_view_B1.setText(seatNameToCustomer);
                            //checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_B1, seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B2":

                            text_view_B2.setText(seatNameToCustomer);
                            //checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_B2, seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B3":
                            text_view_B3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B4":
                            text_view_B4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B5":
                            text_view_B5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B6":
                            text_view_B6.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B7":
                            text_view_B7.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB7, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B8":
                            text_view_B8.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB8, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B9":
                            text_view_B9.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB9, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B10":
                            text_view_B10.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB10, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B11":
                            text_view_B11.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB11, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B12":
                            text_view_B12.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB12, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B13":
                            text_view_B13.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB13, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B14":
                            text_view_B14.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB14, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B15":
                            text_view_B15.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB15, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B16":
                            text_view_B16.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB16, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B17":
                            text_view_B17.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB17, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "B18":
                            text_view_B18.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatB18, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L1":
                            text_view_L1.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatL1, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L2":
                            text_view_L2.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatL2, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L3":
                            text_view_L3.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatL3, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L4":
                            text_view_L4.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatL4, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L5":
                            text_view_L5.setText(seatNameToCustomer);
                            setImageSourceAndOnClickListener(seatL5, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        case "L6":
                            setImageSourceAndOnClickListener(seatL6, seatNameToCustomer, seatNameToMerchant, selectionFor);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if (bookedSeatsArrayConverted != null) {
            if (!bookedSeatsArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < bookedSeatsArrayConverted.size(); i++) {
                    String selectionFor = "cancelBooking";
                    int colonPosition = bookedSeatsArrayConverted.get(i).indexOf(":");
                    AppLog.showLog("SeatName to customer booked seats::::    " + bookedSeatsArrayConverted.get(i).substring(colonPosition + 1));
                    AppLog.showLog("SeatName to merchant booked seats::::" + bookedSeatsArrayConverted.get(i).substring(0, colonPosition));

                    String bookedSeatToMerchant = bookedSeatsArrayConverted.get(i).substring(0, colonPosition);
                    String bookedSeatToCustomer = bookedSeatsArrayConverted.get(i).substring(colonPosition + 1);

                    switch (bookedSeatToMerchant) {
                        case "C1":
                            text_view_C1.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatC1, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "C2":
                            text_view_C2.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatC2, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "C3":
                            text_view_C3.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatC3, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "C4":
                            text_view_C4.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatC4, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "C5":
                            text_view_C5.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatC5, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A1":
                            text_view_A1.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA1, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A2":
                            text_view_A2.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA2, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A3":
                            text_view_A3.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA3, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A4":
                            text_view_A4.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA4, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A5":
                            text_view_A5.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA5, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A6":
                            text_view_A6.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA6, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A7":
                            text_view_A7.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA7, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A8":
                            text_view_A8.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA8, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A9":
                            text_view_A9.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA9, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A10":
                            text_view_A10.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA10, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A11":
                            text_view_A11.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA11, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A12":
                            text_view_A12.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA12, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A13":
                            text_view_A13.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA13, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A14":
                            text_view_A14.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA14, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A15":
                            text_view_A15.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA15, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "A16":
                            text_view_A16.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatA16, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B1":
                            //checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_B1, bookedSeatToCustomer);
                            text_view_B1.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB1, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B2":
                            //checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_B2, bookedSeatToCustomer);
                            text_view_B2.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB2, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B3":
                            // checkSeatNameToCustomerCharacterAndSetMarginLeft(text_view_B3, bookedSeatToCustomer);
                            text_view_B3.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB3, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B4":
                            text_view_B4.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB4, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B5":
                            text_view_B5.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB5, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B6":
                            text_view_B6.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB6, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B7":
                            text_view_B7.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB7, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B8":
                            text_view_B8.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB8, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B9":
                            text_view_B9.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB9, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B10":
                            text_view_B10.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB10, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B11":
                            text_view_B11.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB11, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B12":
                            text_view_B12.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB12, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B13":
                            text_view_B13.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB13, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B14":
                            text_view_B14.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB14, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B15":
                            text_view_B15.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB15, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B16":
                            text_view_B16.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB16, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B17":
                            text_view_B17.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB17, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "B18":
                            text_view_B18.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatB18, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L1":
                            text_view_L1.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL1, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L2":
                            text_view_L2.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL2, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L3":
                            text_view_L3.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL3, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L4":
                            text_view_L4.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL4, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L5":
                            text_view_L5.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL5, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        case "L6":
                            text_view_L1.setText(bookedSeatToCustomer);
                            setImageSourceAndOnClickListener(seatL6, bookedSeatToCustomer, bookedSeatToMerchant, selectionFor);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (seatsBookedByCustomerArrayConverted != null) {
            if (!seatsBookedByCustomerArrayConverted.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < seatsBookedByCustomerArrayConverted.size(); i++) {
                    int colonPosition = seatsBookedByCustomerArrayConverted.get(i).indexOf(":");


                    AppLog.showLog("SeatName to customer seatsBookedByCustomerArrayConverted seats::::    " + seatsBookedByCustomerArrayConverted.get(i).substring(colonPosition + 1));
                    AppLog.showLog("SeatName to merchant seatsBookedByCustomerArrayConverted seats::::" + seatsBookedByCustomerArrayConverted.get(i).substring(0, colonPosition));

                    String bookedSeatToMerchant = seatsBookedByCustomerArrayConverted.get(i).substring(0, colonPosition);
                    String bookedSeatToCustomer = seatsBookedByCustomerArrayConverted.get(i).substring(colonPosition + 1);
                    switch ((bookedSeatToMerchant)) {

                        case "C1":
                            text_view_C1.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatC1);
                            break;
                        case "C2":
                            text_view_C2.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatC2);
                            break;
                        case "C3":
                            text_view_C3.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatC3);
                            break;
                        case "C4":
                            text_view_C4.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatC4);
                            break;
                        case "C5":
                            text_view_C5.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatC5);
                            break;
                        case "A1":
                            text_view_A1.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA1);
                            break;
                        case "A2":
                            text_view_A2.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA2);
                            break;
                        case "A3":
                            text_view_A3.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA3);
                            break;
                        case "A4":
                            text_view_A4.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA4);
                            break;
                        case "A5":
                            text_view_A5.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA5);
                            break;
                        case "A6":
                            text_view_A6.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA6);
                            break;
                        case "A7":
                            text_view_A7.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA7);
                            break;
                        case "A8":
                            text_view_A8.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA8);
                            break;
                        case "A9":
                            text_view_A9.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA9);
                            break;
                        case "A10":
                            text_view_A10.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA10);
                            break;
                        case "A11":
                            text_view_A11.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA11);
                            break;
                        case "A12":
                            text_view_A12.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA12);
                            break;
                        case "A13":
                            text_view_A13.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA13);
                            break;
                        case "A14":
                            text_view_A14.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA14);
                            break;
                        case "A15":
                            text_view_A15.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA15);
                            break;
                        case "A16":
                            text_view_A16.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatA16);
                            break;
                        case "B1":
                            text_view_B1.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB1);
                            break;
                        case "B2":
                            text_view_B2.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB2);
                            break;
                        case "B3":
                            text_view_B3.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB3);
                            break;
                        case "B4":
                            text_view_B4.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB4);
                            break;
                        case "B5":
                            text_view_B5.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB5);
                            break;
                        case "B6":
                            text_view_B6.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB6);
                            break;
                        case "B7":
                            text_view_B7.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB7);
                            break;
                        case "B8":
                            text_view_B8.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB8);
                            break;
                        case "B9":
                            text_view_B9.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB9);
                            break;
                        case "B10":
                            text_view_B10.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB10);
                            break;
                        case "B11":
                            text_view_B11.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB11);
                            break;
                        case "B12":
                            text_view_B12.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB12);
                            break;
                        case "B13":
                            text_view_B13.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB13);
                            break;
                        case "B14":
                            text_view_B14.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB14);
                            break;
                        case "B15":
                            text_view_B15.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB15);
                            break;
                        case "B16":
                            text_view_B16.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB16);
                            break;
                        case "B17":
                            text_view_B17.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB17);
                            break;
                        case "B18":
                            text_view_B18.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatB18);
                            break;
                        case "L1":
                            text_view_L1.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL1);
                            break;
                        case "L2":
                            text_view_L2.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL2);
                            break;
                        case "L3":
                            text_view_L3.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL3);
                            break;
                        case "L4":
                            text_view_L4.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL4);
                            break;
                        case "L5":
                            text_view_L5.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL5);
                            break;
                        case "L6":
                            text_view_L1.setText(bookedSeatToCustomer);
                            setImageSourceForBookedByCustomerSeats(seatL6);
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

//    private void checkSeatNameToCustomerCharacterAndSetMarginLeft(TextView text_view_seat_number, String seatNameToCustomer) {
//        AppLog.showLog(TAG + " seatname in checkSeatNameToCustomerCharacterAndSetMarginLeft::::::: " + seatNameToCustomer);
//        if (seatNameToCustomer.length() > 2) {
//            RelativeLayout.LayoutParams params = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                AppLog.showLog(TAG + " seatname in checkSeatNameToCustomerCharacterAndSetMarginLeft2::::::: " + seatNameToCustomer);
//                params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                params.setMargins(14, 14, 14, 14);
//                text_view_seat_number.setLayoutParams(params);
//            }
//        }
//    }

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


    private void clearCache() {
        selectedSeatDtoArrayList.clear();
        serverCall = 0;
        selectedSeatNumber = 0;
        selectedSeatNameToMechant = "";
        selectedSeatNameToCustomer = "";
        selectedSeatAmount = 0.0;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_booking_button) {
            final Context ctx = HeavyVehicleActivity.this;
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
        if (AppUtil.isInternetConnectionAvailable(HeavyVehicleActivity.this, AppTexts.INTERNET_TYPE_ALL)) {
            progressDialog = AppUtil.createProgressDialog(this);
            progressDialog.show();
            String url = "";
            if (bookRadioButton.isChecked())
                url = APIs.SUBMIT_BOOKING__MAPPING;
            else if (cancelBookRadioButton.isChecked())
                url = APIs.SUBMIT__CANCEL_BOOKING_MAPPING;

            AppLog.showLog(TAG + " url:::::" + url);

            RequestQueue queue = Volley.newRequestQueue(HeavyVehicleActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", tripsDto.getId());
                jsonObject.put("seats", selectedSeatNameToMechant);
                jsonObject.put("signature", Utility.encryption(tripsDto.getId() + selectedSeatNameToMechant + "userOperator"));
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
