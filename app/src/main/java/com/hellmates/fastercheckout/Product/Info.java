package com.hellmates.fastercheckout.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {

    private String itemName;
    private double itemMRP;
    private double itemDiscount;
    private double itemSP;

    public Info(String itemName, double itemMRP, double itemDiscount) {
        this.itemName = itemName;
        this.itemMRP = itemMRP;
        this.itemDiscount = itemDiscount;
        this.itemSP = itemMRP * ( 1.0 - itemDiscount/100.0 );
        this.itemSP = Double.parseDouble(String.format("%.2f", this.itemSP));
    }

    public Info(Parcel source) {
        itemName = source.readString();
        itemMRP = source.readDouble();
        itemDiscount = source.readDouble();
        itemSP = source.readDouble();
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemMRP() {
        return itemMRP;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    public double getItemSP() {
        return itemSP;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeDouble(itemMRP);
        parcel.writeDouble(itemDiscount);
        parcel.writeDouble(itemSP);
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>(){

        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}
