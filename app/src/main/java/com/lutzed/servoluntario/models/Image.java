package com.lutzed.servoluntario.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @Expose(deserialize = false, serialize = true)
    @SerializedName(value = "_destroy")
    private Boolean destroy;

    private boolean isAddPlaceholder;

    private Bitmap bitmap;

    public Image() {
    }

    public Image(String image64) {
        this.image64 = image64;
    }

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }

    public boolean isAddPlaceholder() {
        return isAddPlaceholder;
    }

    public void setAddPlaceholder(boolean addPlaceholder) {
        isAddPlaceholder = addPlaceholder;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.url);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.image64);
        dest.writeByte(this.isAddPlaceholder ? (byte) 1 : (byte) 0);
    }

    protected Image(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.url = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.image64 = in.readString();
        this.isAddPlaceholder = in.readByte() != 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public Boolean getDestroy() {
        return destroy;
    }

    public void setDestroy(Boolean destroy) {
        this.destroy = destroy;
    }
}
