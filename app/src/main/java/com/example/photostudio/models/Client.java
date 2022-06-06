package com.example.photostudio.models;

import android.os.Parcel;

public class Client {
    String id;
    String name,email,phone,password;
    String comment;

    public Client(Parcel in){

    }
    public Client(){

    }
    public Client( String name, String email,String password,String phone){

        this.name=name;
        this.email=email;
        this.phone=phone;
    }

    public Client(String email,String password ){
        this.email=email;
        this.password=password;
    }

    public Client(String id, String email, String password){
        this.id=id;
        this.email=email;
        this.password=password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
