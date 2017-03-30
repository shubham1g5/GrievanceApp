package com.example.shubham.grievanceapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham on 28/3/17.
 */

public class Customer implements Parcelable{
    private String name;
    private String age;
    private String sex;

    public Customer() {
    }

    public Customer(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    protected Customer(Parcel in) {
        name = in.readString();
        age = in.readString();
        sex = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(sex);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
