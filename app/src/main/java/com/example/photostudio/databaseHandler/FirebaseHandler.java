package com.example.photostudio.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.photostudio.models.Booking;
import com.example.photostudio.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHandler {
    private static final String TAG = "FirebaseHandler";


    private boolean addedToFirebase = false;
    private boolean documentExists = false;
    private ArrayList<Booking> bookingsList = new ArrayList<>();
    private ArrayList<Client> clientArrayList = new ArrayList<>();

    public void addDocumentTiFirebase(Object object, CollectionReference collectionReference){
        collectionReference.document()
                .set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    Log.d(TAG, "Документ Firebase-ке сәтті жазылды");
                    setAddedToFirebase(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Firebase-ке жазу барысында қате шықты",e);
                setAddedToFirebase(false);
            }
        });
    }





        public boolean queryIfDocumentExists(Query query){
        setDocumentExists(false);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            setDocumentExists(true);
                        }
                    }
                }
            }
        });
        return isDocumentExists();
    }

    public boolean isAddedToFirebase(){return addedToFirebase;}
    public void setAddedToFirebase(boolean addedToFirebase){
        this.addedToFirebase=addedToFirebase;
    }

    public boolean isDocumentExists() {
        return documentExists;
    }

    public void setDocumentExists(boolean documentExists) {
        this.documentExists = documentExists;
    }

    public ArrayList<Booking> getBookingsList() {
        return bookingsList;
    }
    public ArrayList<Client> getClientssList() {
        return clientArrayList;
    }

    public void setBookingsList(ArrayList<Booking> bookingsList) {
        this.bookingsList = bookingsList;
    }

    public ArrayList<Booking> findBookings(CollectionReference collectionReference, String clientID) throws InterruptedException{


        Task<QuerySnapshot> snapshots = collectionReference.whereEqualTo("clientID", Integer.parseInt(clientID)).get();
        Thread.sleep(3000);

        for (QueryDocumentSnapshot documentSnapshot: snapshots.getResult()){
            Log.d(TAG,"Data: "+documentSnapshot.getData());
            bookingsList.add(documentSnapshot.toObject(Booking.class));
        }
        return getBookingsList();
    }

    public ArrayList<Long> GetRoomBookedSlots(int room_id){

        // select slot
        // from Bookings
        // where room_id == room_id and date >= DATE.NOW
      return null;
    }

}

