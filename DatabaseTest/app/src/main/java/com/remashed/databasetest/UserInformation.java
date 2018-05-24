package com.remashed.databasetest;

public class UserInformation {
    //This class will help us store data in the firebase
    //whatever data you wwant to save in FIREBASE, create the variables here

    //access modifiers should always be public
    public String name;
    public String address;

    //need a default constructor
    public UserInformation() {

    }

    public UserInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
