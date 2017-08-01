package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * The model for a Die, where one can toggle
 * between the states active, passive and disabled.
 * The Die can be rolled and by that a random number between 1 and 6 is
 * assigned as the current value.
 */
class Die implements Parcelable {

    private int active;
    private int value;

    Die() {
        rollDie();
        active = 0;
    }

    private Die(Parcel in) {
        this.value = in.readInt();
        this.active = in.readInt();
    }

    /**
     * sets the state to Active(1), Passive(0) or (Disabled(-1).
     * @param state the new state.
     */
    void setState(int state) {
        if (state > -2 && state < 2) {
            active = state;
        }
    }

    /**
     * The states either -1(Disabled), 0(Passive, not clicked on) or 1(Active, clicked on).
     * @return the active state of the die.
     */
    int getActiveState() {
        return active;
    }

    /**
     *
     * @return the value if the die.
     */
    int getValue() {
        return value;
    }

    /**
     * Toggle the state representing the Die being clicked on or not.
     */
    void toggleActiveState() {
        if (active == 1) {
            active = 0;
        } else if (active == 0) {
            active = 1;
        }
    }

    /**
     * Shuffles a new value for the Die
     */
    void rollDie() {
        Random random = new Random();
        value = random.nextInt((6 - 1) + 1) + 1;
    }


    /**
     * Kills the Die when the player scores with it.
     */
    void disableDie() {
        active = -1;
    }

    /**
     * For receiving the class loader in the Java Runtime Environment,
     * which can load a class dynamically during runtime. It will use the private constructor
     * that takes a Parcel instance.
     */
    public static final Parcelable.Creator<Die> CREATOR = new Parcelable.Creator<Die>() {

        /**
         *
         * @param source the parcel storing the old data.
         * @return a new instance of the Die class, which implements Parcelable class. It uses the
         * Parcel instance that holds the saved data.
         */
        @Override
        public Die createFromParcel(Parcel source) {
            return new Die(source);
        }

        /**
         * Makes it possible to create an array of the now Parcelable object.
         * @param size the desired size.
         * @return the new Array.
         */
        @Override
        public Die[] newArray(int size) {
            return new Die[size];
        }
    };

    /**
     * Makes it possible to pass more information about the class, in form of a bit mask.
     * @return a bitmask with information, 0 if none.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * A parcel works as a message container, and here the values of the class
     * is written into the Parcel.
     * @param parcel a Parcel object for storing data.
     * @param flags describes different ways to write to the parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(value);
        parcel.writeInt(active);
    }
}