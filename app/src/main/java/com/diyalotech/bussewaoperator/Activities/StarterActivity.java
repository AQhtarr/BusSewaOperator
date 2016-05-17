package com.diyalotech.bussewaoperator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.diyalotech.bussewaoperator.AppUtils.APIs;
import com.diyalotech.bussewaoperator.AppUtils.AppLog;
import com.diyalotech.bussewaoperator.AppUtils.AppTexts;
import com.diyalotech.bussewaoperator.AppUtils.AppUtil;
import com.diyalotech.bussewaoperator.AppUtils.GsonRequest;
import com.diyalotech.bussewaoperator.AppUtils.Utility;
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StarterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String TAG = this.getClass().getSimpleName();
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StarterActivity.this);
        if (sharedPreferences.contains("loggedIn")) {
            if(sharedPreferences.getBoolean("loggedIn", false)){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (!sharedPreferences.contains("loggedInFirstTime")) {
                    editor.putBoolean("loggedInFirstTime", true);
                    editor.apply();
                }
                startActivity(new Intent(StarterActivity.this, TripsActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(StarterActivity.this, RegistrationActivity.class));
            finish();
        }


    }





}
