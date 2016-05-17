package com.diyalotech.bussewaoperator.AppUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by AQhtar on 12/23/2015.
 */
public class Utility {

    public static String encryption(String signature){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(signature.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex= Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Hex format : " + hexString.toString());
        return hexString.toString();
    }

    public static void hideKeyboard(Context context,View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showSnackbar(CoordinatorLayout coordinatorLayout,String message,String actinLable){
        final Snackbar snackBar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackBar.setAction(actinLable, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });

        // Changing message amountTextView color
        snackBar.setActionTextColor(Color.WHITE);

// Changing action button amountTextView color
        View sbView = snackBar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBar.show();


//        final Snackbar snackBar = Snackbar.make(coordinatorLayout,message, Snackbar.LENGTH_LONG);
//        snackBar.setAction(actinLable, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackBar.dismiss();
//            }
//        });
//        snackBar.setActionTextColor(Color.GREEN);
//        View view = snackBar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
//        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
//        snackBar.show();
    }

    public static Response.ErrorListener createMyReqErrorListener(final CoordinatorLayout coordinatorLayout, final ProgressDialog progressDialog, final String TAG) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    AppLog.showLog(TAG + "  Inside error response::::::");
                }
                AppLog.showLog(TAG + " Error Response:::::" + error.toString());

                if (error instanceof TimeoutError) {
                    showSnackbar(coordinatorLayout, AppTexts.CONNECTION_TIMEOUT_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    Log.e("Volley", "TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    showSnackbar(coordinatorLayout, AppTexts.CONNECTION_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    showSnackbar(coordinatorLayout, AppTexts.AUTHENTICATION_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    showSnackbar(coordinatorLayout, AppTexts.SERVER_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    showSnackbar(coordinatorLayout, AppTexts.NETWORK_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    showSnackbar(coordinatorLayout, AppTexts.PARSE_ERROR_MESSAGE, AppTexts.DISMISS_ACTION_SNACKBAR_MESSAGE);

                    Log.e("Volley", "ParseError");
                } else if(error.getMessage() != null) {
                    json = new String(error.getMessage());
                    json = trimMessage(json, "message");
                    AppLog.showLog("Message only after trimmed yo nigga:::::" + json);
                    showSnackbar(coordinatorLayout, json, AppTexts.OK_ACTION_SNACKBAR_MESSAGE);
                }


            }
        };
    }

    public static String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
}
