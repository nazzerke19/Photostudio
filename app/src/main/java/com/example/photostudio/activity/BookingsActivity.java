package com.example.photostudio.activity;



import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.photostudio.R;
import com.example.photostudio.common.Common;
import com.example.photostudio.controllers.MyViewPagerAdapter;
import com.example.photostudio.fragment.RoomBookingFragment;
import com.example.photostudio.fragment.SelectedRoomFragment;
import com.example.photostudio.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingsActivity extends AppCompatActivity {
    LocalBroadcastManager localBroadcastManager;
    CollectionReference roomRef;
    private int i=0;
    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.btn_bookday)
    Button btn_bookday;

    public static int CURRENT_FRAGMENT=0;

    public void onResume() {
        super.onResume();
    }
/*

    @OnClick(R.id.btn_bookday)
    void dayBookClicked(){

                    loadTimeSlotOfRoom(String.valueOf(Common.currentRoom.getRoom_id()));
        getSupportFragmentManager().beginTransaction().replace(R.id.bookingFragmentContainer, RoomBookingFragment.getInstance())
                .commit();

    }

    */
    private void confirmBooking() {
        // написать код на стр конфирма букинга
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfRoom(String roomID) {
        Intent intent = new Intent("");
        intent.putExtra(Common.KEY_DISPLAY_TIME_SLOT,Common.KEY_DISPLAY_TIME_SLOT);
//        localBroadcastManager.sendBroadcast(intent);
    }
/*
    private void loadBookingByRoom(int roomID) {


        roomRef = FirebaseFirestore.getInstance()

                .collection("rooms");

        roomRef.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Room> rooms = new ArrayList<Room>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                            Room room = queryDocumentSnapshot.toObject(Room.class);

                            room.setRoom_id(String.valueOf(Integer.parseInt(queryDocumentSnapshot.getId())));

                            rooms.add(room);
                        }
                        Intent intent = new Intent(Common.KEY_ROOMS_LOAD_DONE);
                        intent.putParcelableArrayListExtra(Common.KEY_ROOMS_LOAD_DONE, rooms );
                        localBroadcastManager.sendBroadcast(intent);

                    }
                }
        );


    }
*/
    private BroadcastReceiver buttonBookdayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if (step == 1)
                Common.currentTimeSlot = Integer.parseInt(Common.KEY_TIME_SLOT);

           // setColorButton();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        ButterKnife.bind(BookingsActivity.this);

        Button button = findViewById(R.id.btn_bookday);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoomBookingFragment fragment = new RoomBookingFragment();
                if (fragment != null && SelectedRoomFragment.getInstance().isVisible()) {
                    getSupportFragmentManager().beginTransaction().remove(SelectedRoomFragment.getInstance()).commit();
                    loadTimeSlotOfRoom(String.valueOf(Common.currentRoom.getRoom_id()));
                    getSupportFragmentManager().beginTransaction().add(R.id.bookingFragmentContainer, fragment).commit();
                    Log.d(TAG, "fRAGMENT is not null");
                }
                btn_bookday.setEnabled(false);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.bookingFragmentContainer, SelectedRoomFragment.getInstance()).commit();

    }
}