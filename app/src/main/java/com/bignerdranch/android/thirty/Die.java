package com.bignerdranch.android.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;


public class Die implements Parcelable {

    private int active;
    private int value;

    public Die() {
        rollDie();
        active = 0;
    }

    private Die (Parcel in) {
        this.value = in.readInt();
        this.active = in.readInt();
    }

    public void setState(int state) {
        active = state;
    }

    public void toggleActiveState() {
        if (active == 1) {
            active = 0;
        } else if (active == 0) {
            active = 1;
        }
    }

    public int getActiveState() {
        return active;
    }

    public void rollDie() {
        Random random = new Random();
        value = random.nextInt((6 - 1) + 1) + 1;
    }

    public int getValue() {
        return value;
    }

    public void disableDie() {
        active = -1;
    }

    public static final Parcelable.Creator<Die> CREATOR = new Parcelable.Creator<Die>() {
        @Override
        public Die createFromParcel(Parcel source) {
            return new Die(source);
        }

        @Override
        public Die[] newArray(int size) {
            return new Die[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(value);
        parcel.writeInt(active);
    }
}