package com.diyalotech.bussewaoperator.AppUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diyalotech.bussewaoperator.R;


/**
 * Created by saurav on 5/11/15.
 */
public class AppUtil {

    public static void showInternetDailog(Context context) {
//        new AlertDialog.Builder(context)
//                .setTitle("No Internet")
//                .setMessage("Check your internet connection and try again")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//        final Snackbar snackbar = Snackbar
//                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
//                .setAction(actionLabel, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//
//// Changing message amountTextView color
//        snackbar.setActionTextColor(Color.RED);
//
//// Changing action button amountTextView color
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        snackbar.show();
        new AlertDialog.Builder(context)
                .setTitle("No Internet")
                .setMessage("Check your internet connection and try again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }


    public static String convertStreamToString(java.io.InputStream is) {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader
                (is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static boolean isInternetConnectionAvailable(Context context, int internetType) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            switch (internetType) {
                case 0:

                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo
                            .getType() == ConnectivityManager.TYPE_MOBILE) {
                        isAvailable = true;
                    }
                    break;
                case 1:

                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isAvailable = true;
                    }
                case 2:

                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isAvailable = true;
                    }
                    break;
                default:
            }
        }
        return isAvailable;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setmMessage(Message);
        return dialog;
    }


    public static String getDeviceId(Context context) {
        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String uid = tManager.getDeviceId();
return uid;
    }


    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
