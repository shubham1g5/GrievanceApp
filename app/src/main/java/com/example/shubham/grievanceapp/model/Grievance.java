package com.example.shubham.grievanceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shubham on 28/3/17.
 */

public class Grievance implements Parcelable{
    private Customer customer;
    private String manager;
    private String username;
    private ArrayList<String> urls;

    public Grievance() {
    }

    public Grievance(Customer customer, Manager manager, String username) {
        this.customer = customer;
        this.manager = manager.getName();
        this.username = username;
    }

    protected Grievance(Parcel in) {
        customer = in.readParcelable(Customer.class.getClassLoader());
        manager = in.readString();
        username = in.readString();
        urls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(customer, flags);
        dest.writeString(manager);
        dest.writeString(username);
        dest.writeStringList(urls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Grievance> CREATOR = new Creator<Grievance>() {
        @Override
        public Grievance createFromParcel(Parcel in) {
            return new Grievance(in);
        }

        @Override
        public Grievance[] newArray(int size) {
            return new Grievance[size];
        }
    };

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
