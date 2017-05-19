package com.lutzed.servoluntario.models;

import android.os.Parcel;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Cause extends SelectableItem{

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public Cause() {
    }

    protected Cause(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Cause> CREATOR = new Creator<Cause>() {
        @Override
        public Cause createFromParcel(Parcel source) {
            return new Cause(source);
        }

        @Override
        public Cause[] newArray(int size) {
            return new Cause[size];
        }
    };
}
