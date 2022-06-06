package com.example.photostudio.fragment;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photostudio.R;
import com.example.photostudio.activity.BookingsActivity;
import com.example.photostudio.activity.ConfirmBookingActivity;
import com.example.photostudio.common.Common;
import com.example.photostudio.common.SpacesItemDecoration;
import com.example.photostudio.controllers.TimeSlotAdapter;
import com.example.photostudio.models.ITimeSlotLoadListener;
import com.example.photostudio.models.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class RoomBookingFragment extends Fragment implements ITimeSlotLoadListener {

    DocumentReference documentReference;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;
    Button bookBtn;
    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;



    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    private void displayTimeSlot() {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotOfRoom((Common.currentRoom.getRoom_id()),
                    simpleDateFormat.format(date.getTime()));
        }



    private void loadAvailableTimeSlotOfRoom(String roomID, final String bookDate) {
//        dialog.show();

        documentReference = FirebaseFirestore.getInstance()

                .collection("rooms")
                .document(String.valueOf(Common.currentRoom.getRoom_id()));

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) // if room is available
                    {
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("rooms")
                                .document(String.valueOf(Common.currentRoom.getRoom_id()))
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty())
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    else
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                        Log.d(TAG,"loadavailabetime slot"+timeSlots.toString());
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    static RoomBookingFragment instance;

    public static RoomBookingFragment getInstance() {
        if(instance == null)
            instance = new RoomBookingFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = (ITimeSlotLoadListener) this;

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0);
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        //Log.d(TAG, Common.currentRoom.getName()+"  RoomBooking  current room isss ");

        displayTimeSlot();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        localBroadcastManager.unregisterReceiver(displayTimeSlot);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_room,container,false);
        unbinder = ButterKnife.bind(this,itemView);
        init(itemView);
        Button confirmBtn = itemView.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConfirmBookingActivity.class);
                startActivity(intent);
            }
        });
        Log.d(TAG,"adapter is  connected");
        return itemView;
    }

    private void init(View itemView) {
        Log.d(TAG,"INIT nacahlo "+Common.bookingDate.toString());
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                if (Common.bookingDate.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.bookingDate = date;
                    loadAvailableTimeSlotOfRoom(String.valueOf(Common.currentRoom.getRoom_id()),
                            simpleDateFormat.format(date.getTime()));
                    Log.d(TAG,"RoomBookingFragment INIT "+Common.bookingDate.toString());
                }

            }
        });
    }

@Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
    Log.d(TAG,"onTimeSlotLoadSuccess is working");
        TimeSlotAdapter adapter = new TimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

@Override
    public void onTimeSlotLoadEmpty() {
        TimeSlotAdapter adapter = new TimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }
}

