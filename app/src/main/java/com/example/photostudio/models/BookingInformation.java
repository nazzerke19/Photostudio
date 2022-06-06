package com.example.photostudio.models;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private String clientName,roomPrice,roomName,time,roomId,clientId,photographerName;
    private long slot;
    private Timestamp timestamp;
    private boolean done;

    public BookingInformation(){

    }

    public BookingInformation(String clientName,String clientId,String roomName,String roomId,String roomPrice,
                              Long slot,String time,String photographerName){
        this.clientId=clientId;
        this.clientName=clientName;
        this.roomId=roomId;
        this.roomName=roomName;
        this.roomPrice=roomPrice;
        this.slot=slot;
        this.time=time;
        this.photographerName = photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone(){return done;}
}



