package com.csci448.tkarol_a1.tmkarol_a2;

/**
 * Created by tkarol on 3/1/18.
 * This class is used to represent a playing card by storing it's corresponding drawable resource id and it's face value
 */

public class Card {
    private int mResID;
    private int mValue;

    Card(int id, int value){
        mResID = id;
        mValue = value;
    }

    public int getResID() {
        return mResID;
    }

    public int getValue() {
        return mValue;
    }
}
