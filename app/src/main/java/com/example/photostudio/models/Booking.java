package com.example.photostudio.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Date;

public class Booking {
    int id, room_id, client_id;

    String bookingStatus;
    // Slot -
    Date date;
    private Long slot;

    public Booking(){

    }
    public Booking (int id, int room_id,int client_id,
                                Long slot,String bookingStatus){
       this.id=id;
       this.slot=slot;
       this.room_id=room_id;
       this.client_id=client_id;

       this.bookingStatus=bookingStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
