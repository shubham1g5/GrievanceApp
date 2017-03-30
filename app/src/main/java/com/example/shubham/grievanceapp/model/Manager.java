package com.example.shubham.grievanceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham on 28/3/17.
 */

public class Manager implements Parcelable{

    private String name;

    public Manager() {
        // For Firebase DB
    }

    public Manager(String name) {
        this.name = name;
    }

    protected Manager(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Manager> CREATOR = new Creator<Manager>() {
        @Override
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
