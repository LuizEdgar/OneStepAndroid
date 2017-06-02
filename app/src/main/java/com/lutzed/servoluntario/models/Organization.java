package com.lutzed.servoluntario.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Organization implements FeedItem, Parcelable {

    @Expose private Long id;

    @Expose private String name;

    @Expose private String cnpj;

    @Expose private String site;

    @Expose private String about;

    @Expose private String mission;

    @Expose private Integer size;

    @Expose
    @SerializedName("feedable_type")
    private String feedableType;

    @Expose(serialize = false, deserialize = true)
    private List<Location> locations;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("locations_attributes")
    private List<Location> locationsAttributes;

    @Expose(serialize = false, deserialize = true)
    private List<Contact> contacts;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("contacts_attributes")
    private List<Contact> contactsAttributes;

    @Expose(serialize = false, deserialize = true)
    private List<Cause> causes;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("cause_ids")
    private List<Long> causeIds;

    @Expose(serialize = false, deserialize = true)
    private List<Skill> skills;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("skill_ids")
    private List<Long> skillIds;

    @Expose
    @SerializedName("profile_image")
    private Image profileImage;

    @Expose
    @SerializedName("profile_image_64")
    private String profileImage64;

    @Expose(serialize = false, deserialize = true)
    private List<Image> images;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("images_attributes")
    private List<Image> imagesAttributes;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("images_attributes_64")
    private List<String> imagesAttributes64;

    @Expose
    @SerializedName("established_at")
    private String establishedAt;

    @Expose
    @SerializedName("user_id")
    private Long userId;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getEstablishedAt() {
        return establishedAt;
    }

    public void setEstablishedAt(String establishedAt) {
        this.establishedAt = establishedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(List<Cause> causes) {
        this.causes = causes;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Long> getCauseIds() {
        return causeIds;
    }

    public void setCauseIds(List<Long> causeIds) {
        this.causeIds = causeIds;
    }

    public List<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocationsAttributes() {
        return locationsAttributes;
    }

    public void setLocationsAttributes(List<Location> locationsAttributes) {
        this.locationsAttributes = locationsAttributes;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContactsAttributes() {
        return contactsAttributes;
    }

    public void setContactsAttributes(List<Contact> contactsAttributes) {
        this.contactsAttributes = contactsAttributes;
    }

    public String getFeedableType() {
        return feedableType;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImagesAttributes() {
        return imagesAttributes;
    }

    public void setImagesAttributes(List<Image> imagesAttributes) {
        this.imagesAttributes = imagesAttributes;
    }

    public List<String> getImagesAttributes64() {
        return imagesAttributes64;
    }

    public void setImagesAttributes64(List<String> imagesAttributes64) {
        this.imagesAttributes64 = imagesAttributes64;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public void setFeedableType(String feedableType) {
        this.feedableType = feedableType;
    }

    @Override
    public Type getFeedableTypeAsEnum(){
        return Type.fromString(this.feedableType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.cnpj);
        dest.writeString(this.site);
        dest.writeString(this.about);
        dest.writeString(this.mission);
        dest.writeValue(this.size);
        dest.writeString(this.feedableType);
        dest.writeTypedList(this.locations);
        dest.writeTypedList(this.locationsAttributes);
        dest.writeTypedList(this.contacts);
        dest.writeTypedList(this.contactsAttributes);
        dest.writeTypedList(this.causes);
        dest.writeList(this.causeIds);
        dest.writeTypedList(this.skills);
        dest.writeList(this.skillIds);
        dest.writeParcelable(this.profileImage, flags);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.imagesAttributes);
        dest.writeStringList(this.imagesAttributes64);
        dest.writeString(this.establishedAt);
        dest.writeValue(this.userId);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public Organization() {
    }

    protected Organization(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.cnpj = in.readString();
        this.site = in.readString();
        this.about = in.readString();
        this.mission = in.readString();
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.feedableType = in.readString();
        this.locations = in.createTypedArrayList(Location.CREATOR);
        this.locationsAttributes = in.createTypedArrayList(Location.CREATOR);
        this.contacts = in.createTypedArrayList(Contact.CREATOR);
        this.contactsAttributes = in.createTypedArrayList(Contact.CREATOR);
        this.causes = in.createTypedArrayList(Cause.CREATOR);
        this.causeIds = new ArrayList<Long>();
        in.readList(this.causeIds, Long.class.getClassLoader());
        this.skills = in.createTypedArrayList(Skill.CREATOR);
        this.skillIds = new ArrayList<Long>();
        in.readList(this.skillIds, Long.class.getClassLoader());
        this.profileImage = in.readParcelable(Image.class.getClassLoader());
        this.images = in.createTypedArrayList(Image.CREATOR);
        this.imagesAttributes = in.createTypedArrayList(Image.CREATOR);
        this.imagesAttributes64 = in.createStringArrayList();
        this.establishedAt = in.readString();
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Organization> CREATOR = new Parcelable.Creator<Organization>() {
        @Override
        public Organization createFromParcel(Parcel source) {
            return new Organization(source);
        }

        @Override
        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };

    public String getProfileImage64() {
        return profileImage64;
    }

    public void setProfileImage64(String profileImage64) {
        this.profileImage64 = profileImage64;
    }
}
