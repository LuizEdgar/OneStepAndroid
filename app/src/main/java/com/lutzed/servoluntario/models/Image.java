package com.lutzed.servoluntario.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lutzed.servoluntario.helpers.DateHelper;

/**
 * Created by luizfreitas on 17/07/2016.
 */

public class Image implements Parcelable {

    @Expose private Long id;

    @Expose private String url;

    @Expose
    @SerializedName(value = "created_at")
    private String createdAt;

    @Expose
    @SerializedName(value = "updated_at")
    private String updatedAt;

    @Expose(deserialize = false, serialize = true)
    @SerializedName(value = "image_64")
    private String image64;

    private String readableCreatedAt;

    public Image(String image64) {
        this.image64 = image64;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected Image(Parcel in) {
        id = in.readLong();
        url = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(id);
        dest.writeString(url);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    public String getReadableCreatedAt() {
        if (readableCreatedAt == null){
            readableCreatedAt = DateHelper.format(DateHelper.postDatetimeFormat, createdAt);
        }
        return readableCreatedAt;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }
}
