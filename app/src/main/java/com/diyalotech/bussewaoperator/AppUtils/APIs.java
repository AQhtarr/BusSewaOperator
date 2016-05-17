package com.diyalotech.bussewaoperator.AppUtils;

/**
 * Created by AQhtar on 12/21/2015.
 */
public class APIs {

    public static String HOST_ID ="http://103.233.182.131:41455";
   // public static String HOST_ID ="http://10.13.";
    public static String SECONDARY_MAPPING ="/customer/webresources/operator/";
    public static String REGISTRAION_MAPPING=HOST_ID+SECONDARY_MAPPING+"register";
    public static String TOKEN_VALIDATION_MAPPING=HOST_ID+SECONDARY_MAPPING+"verifyOperator";
    public static String GET_TRIP_MAPPING=HOST_ID+SECONDARY_MAPPING+"trips";
    public static String CANCEL_BOOKING_MAPPING=HOST_ID+SECONDARY_MAPPING+"cancel";
    public static String SUBMIT_BOOKING__MAPPING=HOST_ID+SECONDARY_MAPPING+"book";
    public static String SUBMIT__CANCEL_BOOKING_MAPPING = HOST_ID+ SECONDARY_MAPPING+"cancel";
    public static String REFRESH_SEATS_MAPPING=HOST_ID+SECONDARY_MAPPING+"refresh";
}
