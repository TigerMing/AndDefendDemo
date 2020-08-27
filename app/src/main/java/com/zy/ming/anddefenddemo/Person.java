package com.zy.ming.anddefenddemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * description ：
 * author : create by qiaoming on 2020/7/27
 * version :
 */
class Person implements Parcelable {

    public String name;

    public String age;

    public Person(String name,String age){
        this.name = name;
        this.age = age;
    }

    protected Person(Parcel in) {
        name = in.readString();
        age = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(age);
    }
}
