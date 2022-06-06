package com.example.photostudio.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photostudio.R;
import com.example.photostudio.common.Common;
import com.example.photostudio.models.BookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ConfirmBookingActivity extends AppCompatActivity {

    TextView room_name, room_price, clients_name, booking_time, phograph,halfprice;
    Button buttonPayment;
    PayPalConfiguration payPalConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        buttonPayment = findViewById(R.id.btn_confirm_booking);
        room_name = findViewById(R.id.txt_room_name);
        room_price = findViewById(R.id.txt_room_price);
        booking_time = findViewById(R.id.txt_booking_time_text);
        clients_name = findViewById(R.id.txt_booking_client_text);
        halfprice = findViewById(R.id.halfprice);
        phograph = findViewById(R.id.txt_photographer);
        Common.currentTimeSlot = Integer.parseInt(Common.KEY_TIME_SLOT);
        setData();

        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK) //ENVIRONMENT_NO_NETWORK, because app is in development phase
                .clientId("ARedtQLcfzsmkz5hjIAF-vG_2LjuD9v-IH2XbHED2fnFsBh-3SQjmBUZkmF1Qh5HWakdvC2F5yrrIvxg");

        //sandbox acount - sb-0gmjn521314@business.example.com
        //Client id - AT1moYlQvscjeViNpK0ILpZHWr2bFnbY5NwxcM8aCBVr5_upmd2n_dHzCqP0p8zt8jWqmi_Z3oHx3kuZ
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PayPalPayment payPalPayment =
                        new PayPalPayment(new BigDecimal(Common.currentRoom.getPrice()/2), "USD", "Төлем жасау",
                                PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent =
                        new Intent(ConfirmBookingActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
                // .class is payment activity class of paypal

                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                // paypal default keys

                intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payPalPayment);

                startActivityForResult(intent, 0);

            }
        });

    }

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (resultCode == Activity.RESULT_OK) { // if payment is successful
           PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
           if (confirm != null) {
               try {
                   confirmBooking();
                   startActivity(new Intent(ConfirmBookingActivity.this, HomeActivity.class));
                   Toast.makeText(this, "Төлем сәтті түрде жасалды!", Toast.LENGTH_SHORT).show();

               } catch (Exception e) {
                   Log.e("payment", "Payment Failure: ", e);
               }
           }
       }
       else if (resultCode == Activity.RESULT_CANCELED) {
           Log.i("paymentExample", "The user canceled.");
       }
       else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
           Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted." +
                   " Please see the docs.");
       }
    }


    public void confirmBooking(){
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");

        String[] startTimeConvert = convertTime[0].split(":"); //9:00
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());//9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());//00


        Calendar bookingDateWithoutHouse = Calendar.getInstance();
        bookingDateWithoutHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithoutHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
        bookingDateWithoutHouse.set(Calendar.MINUTE,startMinInt);

        Timestamp timestamp = new Timestamp(bookingDateWithoutHouse.getTime());

        final BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setPhotographerName(Common.photgrapher);
        bookingInformation.setClientName(Common.currentClient.getName());
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false);
        bookingInformation.setRoomId(Common.currentRoom.getRoom_id());
        bookingInformation.setRoomName(Common.currentRoom.getName());
        bookingInformation.setRoomPrice(String.valueOf(Common.currentRoom.getPrice()));
        bookingInformation.setClientId(Common.currentClient.getId());
       bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)).toString());
              //  .append(" at "))
//                .append(simpleDateFormat.format(bookingDateWithoutHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(String.valueOf(Common.currentRoom.getRoom_id()))
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //addBookingToFirebase(bookingInformation);
                        addToUserBooking(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addBookingToFirebase(final BookingInformation bookingInformation){
        final CollectionReference booking = FirebaseFirestore.getInstance()
                .collection("booking");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        booking
                .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                .whereEqualTo("done",false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            booking.document()
                                    .set(bookingInformation);

                        }
                    }
                });
    }



    private void addToUserBooking(final BookingInformation bookingInformation) {

        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentClient.getId())
                .collection("Booking");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        userBooking
                .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                .whereEqualTo("done",false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty())
                        {
                            userBooking.document()
                                    .set(bookingInformation);
                                   /* .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            if (dialog.isShowing())
//                                                dialog.dismiss();

//                                            addToCalendar(Common.bookingDate,
//                                                    Common.convertTimeSlotToString(Common.currentTimeSlot));
                                           // resetStaticData();
                                           // getActivity().finish();
                                           // Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                            if (dialog.isShowing())
//                                                dialog.dismiss();
//                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });*/
                        }
                        else
                        {
//                            if (dialog.isShowing())
//                                dialog.dismiss();
                            resetStaticData();
                           // getActivity().finish();
                           // Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
/*
    private void addToCalendar(Calendar bookingDate, String startDate) {
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");

        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY, startHourInt);
        startEvent.set(Calendar.MINUTE, startMinInt);

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY, endHourInt);
        endEvent.set(Calendar.MINUTE, endMinInt);

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime, endEventTime, "Photostudio Booking",
                new StringBuilder("Haircut from ")
                        .append(startTime)
                        .append(" with ")
                        .append(Common.currentClient.getName())
                        .append(" at ")
                        .append(Common.currentRoom.getName()).toString(),
                new StringBuilder("Address: ").append(Common.currentRoom.getPrice()).toString());
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date end = calendarDateFormat.parse(endEventTime);
            Date start = calendarDateFormat.parse(startEventTime);

            ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID,getCalendar(ConfirmBookingActivity.this));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION,description);
            event.put(CalendarContract.Events.EVENT_LOCATION,location);

            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY,0);
            event.put(CalendarContract.Events.HAS_ALARM,1);

            String timeZone = TimeZone.getDefault().getID();


            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone);

            Uri calendars;
            if (Build.VERSION.SDK_INT > 8)
                calendars = Uri.parse("content://com.android.calendar/events");
            else
                calendars = Uri.parse("content://calendar/events");

            Uri uri_save = ConfirmBookingActivity.this.getContentResolver().insert(calendars,event);
//            Paper.init(ConfirmBookingActivity.this);
//            Paper.book().write(Common.EVENT_URI_CACHE,uri_save.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
*/
    private String getCalendar(Context context) {
        String gmailIdCalendar = "";
        String projection[]={"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars,projection,null,null,null);
        if (managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                if (calName.contains("@gmail.com"))
                {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break;
                }
            } while (managedCursor.moveToNext());
            managedCursor.close();
        }
        return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentRoom = null;
        Common.bookingDate.add(Calendar.DATE,0);
    }

    private void setData() {
        Log.d(TAG,"ConfirmBookingActivity bookingdate "+Common.bookingDate.toString());
        room_name.setText(Common.currentRoom.getName());
        clients_name.setText(Common.currentClient.getName());
        booking_time.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)));
                //.append(simpleDateFormat.format(Common.bookingDate.getTime())));

        room_price.setText( Double.toString(Common.currentRoom.getPrice()));
        double price = Common.currentRoom.getPrice()/2;
        halfprice.setText(Double.toString(price));
        phograph.setText(Common.photgrapher);
    }


}