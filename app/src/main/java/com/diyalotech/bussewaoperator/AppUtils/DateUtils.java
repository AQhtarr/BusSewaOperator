package com.diyalotech.bussewaoperator.AppUtils;

/**
 * Created by aqhtar on 7/27/15.
 */
public class DateUtils {

    public static String getDateSuffix(int day) {
        switch (day) {
            case 1:
            case 21:
            case 31:
                return ("st");
            case 2:
            case 22:
                return  ("nd");
            case 3:
            case 23:
                return ("rd");
            default:
                return ("th");

        }
    }
    public static String getMonthName(int month){
        switch (month){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }
}

