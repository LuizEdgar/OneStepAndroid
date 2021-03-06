package com.lutzed.servoluntario.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Contact implements Parcelable {

    @Expose private Long id;

    @Expose private String phone;

    @Expose private String name;

    @Expose private String email;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;

    public Contact() {
    }

    public Contact(Long id) {
        this.id = id;
    }

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        String s = name;
        if (!TextUtils.isEmpty(phone)) s += " - " + phone;
        if (!TextUtils.isEmpty(email)) s += " - " + email;
        return s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    protected Contact(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.phone = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Contact && this.getId() != null && this.getId().equals(((Contact) obj).getId());
    }

    public static int containsIndentiq(List<Contact> contactList, Contact contact) {
        if (contact == null) return -1;

        for (int i = 0; i < contactList.size(); i++) {
            Contact contactItem = contactList.get(i);
            boolean identiq = TextUtils.equals(contactItem.getName(), contact.getName())
                    && TextUtils.equals(contactItem.getPhone(), contact.getPhone())
                    && TextUtils.equals(contactItem.getEmail(), contact.getEmail());

            if (identiq) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isIndentiq(Contact contacta, Contact contactb) {
        return TextUtils.equals(contacta.getName(), contactb.getName())
                && TextUtils.equals(contacta.getPhone(), contactb.getPhone())
                && TextUtils.equals(contacta.getEmail(), contactb.getEmail());

    }
}
