package com.example.photostudio.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class Room extends BroadcastReceiver implements Parcelable {
    String room_id;
    String name,shirina,dlina;
    String description,image_url;
    double price;

    public Room(Parcel in){

    }
    public  Room(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public Room(String name,String room_id,String description,
                String shirina,String dlina, double price,String image_url){
        this.name=name;
        this.room_id=room_id;
        this.description=description;
        this.price=price;
        this.shirina = shirina;
        this.dlina=dlina;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDlina() {
        return dlina;
    }

    public void setDlina(String dlina) {
        this.dlina = dlina;
    }

    public String getShirina() {
        return shirina;
    }

    public void setShirina(String shirina) {
        this.shirina = shirina;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(room_id);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(shirina);
        dest.writeString(dlina);
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[0];
        }
    };
}
