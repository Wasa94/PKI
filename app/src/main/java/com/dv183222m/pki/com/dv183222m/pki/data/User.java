package com.dv183222m.pki.com.dv183222m.pki.data;

import com.dv183222m.pki.R;

import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private UserType type;
    private String address;
    private String phoneNumber;
    private String username;
    private String password;
    private Worker worker;
    private int image;

    public User(String firstName, String lastName, UserType type, String address,
                String phoneNumber, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.image = R.drawable.android;

        if (type == UserType.Worker) {
            worker = new Worker(this);
        } else {
            worker = null;
        }
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getFullName() { return firstName + " " + lastName; }
}
