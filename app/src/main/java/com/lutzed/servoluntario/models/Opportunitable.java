package com.lutzed.servoluntario.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Opportunitable implements Parcelable {

    @Expose private Long id;

    @Expose private String name;

    @Expose
    @SerializedName("profile_image")
    private Image profileImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.profileImage, flags);
    }

    public Opportunitable() {
    }

    protected Opportunitable(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.profileImage = in.readParcelable(Image.class.getClassLoader());
    }

    public static final Parcelable.Creator<Opportunitable> CREATOR = new Parcelable.Creator<Opportunitable>() {
        @Override
        public Opportunitable createFromParcel(Parcel source) {
            return new Opportunitable(source);
        }

        @Override
        public Opportunitable[] newArray(int size) {
            return new Opportunitable[size];
        }
    };
}
