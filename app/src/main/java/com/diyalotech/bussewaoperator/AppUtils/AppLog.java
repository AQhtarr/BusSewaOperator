package com.diyalotech.bussewaoperator.AppUtils;

import android.util.Log;


import com.diyalotech.bussewaoperator.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AppLog {

    private static final String APP_TAG = "Bus Sewa";

    public static void showLog(String message) {
        if(BuildConfig.DEBUG){
            Log.i(APP_TAG, message);
        }
    }

    public static void showLogWithPosition(String message) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo("showLogWithPosition") == 0) {
                currentIndex = i + 1;
                break;
            }
        }

        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());

        Log.i(APP_TAG, message);
        Log.i(APP_TAG + " position", "at " + fullClassName + "." + methodName + "(" + className + ".java:" + lineNumber + ")");
    }

    public static String cryptHere(String signature){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(signature.getBytes());

        byte byteData[] = md.digest();
//
//        //convert the byte to hex format method 1
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < byteData.length; i++) {
//            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//        }
//
//        System.out.println("Hex format : " + sb.toString());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex= Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Hex format : " + hexString.toString());
        return hexString.toString();
    }


}
