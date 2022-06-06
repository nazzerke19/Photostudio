package com.example.photostudio.fragment;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.photostudio.R;
import com.example.photostudio.common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientsPageFragment extends Fragment {

    TextView name, email, photographer,phone, roomName,price,time,timestamp;
    static ClientsPageFragment instance;


    public static ClientsPageFragment getInstance() {
        if(instance == null)
            instance = new ClientsPageFragment();
        return instance;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_page,container,false);

         name = view.findViewById(R.id.textViewPersonName);
         email = view.findViewById(R.id.textViewEmailAddress);
         time = view.findViewById(R.id.timeTextView);
         timestamp = view.findViewById(R.id.timstampTextView);
         roomName = view.findViewById(R.id.roomNameTextView);
         price =view.findViewById(R.id.roomPriceTextView);
         phone = view.findViewById(R.id.textViewPhone);
         photographer = view.findViewById(R.id.selectedPhotographer);
        loadClientsInfo();
        loadClientsBookings();

        return view;
    }

    public void loadClientsInfo(){
        final DocumentReference userInfo = FirebaseFirestore.getInstance()
                .collection("users").document(Common.currentClient.getId());

        userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot!=null){
                    Log.d(TAG,documentSnapshot.getString("name"));
                    name.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("email"));
                    phone.setText(documentSnapshot.getString("phone"));
                }
            }
        });

    }

    public void loadClientsBookings(){
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentClient.getId())
                .collection("Booking");
        userBooking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                if (task.isSuccessful()){

                    String timestampString = new SimpleDateFormat("dd-MM-yyyy").format(documentSnapshot.getDate("timestamp"));
                    timestamp.setText(timestampString);
                    photographer.setText(documentSnapshot.getString("photographerName"));
                    time.setText(documentSnapshot.getString("time"));
                    price.setText(documentSnapshot.getString("roomPrice"));
                    roomName.setText(documentSnapshot.getString("roomName"));

                }
                }
            }
        });
    }

    public void deleteBooking(){
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentClient.getId())
                .collection("Booking");

        userBooking.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot:task.getResult()){
                    if (task.isSuccessful()){
                        documentSnapshot.getReference().delete();
                    }
                }
            }
        });

    }
}