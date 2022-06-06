package com.example.photostudio.common;

import com.example.photostudio.models.BookingInformation;
import com.example.photostudio.models.Client;
import com.example.photostudio.models.Room;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static  String KEY_TIME_SLOT = "TIME_SLOT";
    public static final int TIME_SLOT_TOTAL = 12;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static Calendar bookingDate = Calendar.getInstance();
    public static Room currentRoom;
    public static String photgrapher = "photgrapher";
    public static Client currentClient;
    public static int step = 0;
    public static int currentTimeSlot =-1;
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String KEY_ROOMS_LOAD_DONE = "ROOMS_LOAD_DONE";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ENABLE_BUTTON_BOOKDAY = "ENABLE_BUTTON_BOOKDAY";
    public static final String KEY_ROOM_STORE = "ROOM_SAVE";
    public static BookingInformation currentBooking;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");



    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "9:00-10:00";
            case 1:
                return "10:00-11:00";
            case 2:
                return "11:00-12:00";
            case 3:
                return "12:00-13:00";
            case 4:
                return "13:00-14:00";
            case 5:
                return "14:00-15:00";
            case 6:
                return "15:00-16:00";
            case 7:
                return "16:00-17:00";
            case 8:
                return "17:00-18:00";
            case 9:
                return "18:00-19:00";
            case 10:
                return "19:00-20:00";
            case 11:
                return "20:00-21:00";

            default:
                return "Closed";
        }
    }

    public static String convertTimeStampToStringKey(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(date);
    }
}
