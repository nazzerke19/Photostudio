package com.example.photostudio.controllers;

import com.example.photostudio.databaseHandler.FirebaseHandler;
import com.example.photostudio.models.Booking;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class BookingManager {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseHandler firebaseHandler = new FirebaseHandler();
    private CollectionReference bookingsCollection = db.collection("bookings");

    public Booking createBooking(int aClientID, int aRoomID, Long slot){
        Booking booking = new Booking();

        booking.setClient_id(aClientID);
        booking.setRoom_id(aRoomID);
       booking.setSlot(slot);
        return (booking);
    }

    public void addBookingToFirebase(Booking booking) throws InterruptedException{
        firebaseHandler.addDocumentTiFirebase(booking,bookingsCollection);
    }

    public ArrayList<String> findBookings(String clientID) throws InterruptedException {
        ArrayList<String> bookingsDetails = new ArrayList<>();

        ArrayList<Booking> bookingsMapsList = firebaseHandler.findBookings(db.collection("bookings"),clientID );

        for (int i=0;i<=bookingsMapsList.size()-1;i++){
            bookingsDetails.add("Date: "+bookingsMapsList.get(i).getSlot());

        }
       return bookingsDetails;
    }
}

