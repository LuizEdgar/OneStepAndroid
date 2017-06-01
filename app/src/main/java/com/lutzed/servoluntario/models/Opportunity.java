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

public class Opportunity implements Parcelable, FeedItem {

    @Expose private Long id;

    @Expose private String title;

    @Expose
    @SerializedName("feedable_type")
    private String feedableType;

    @Expose
    @SerializedName("is_ongoing")
    private Boolean isOngoing;

    @Expose
    @SerializedName("is_virtual")
    private Boolean isVirtual;

    @Expose
    @SerializedName("volunteers_number")
    private Integer volunteersNumber;

    @Expose private String description;

    @Expose
    @SerializedName("time_commitment")
    private String timeCommitment;

    @Expose
    @SerializedName("others_requirements")
    private String othersRequirements;

    @Expose private String tags;

    @Expose(serialize = false, deserialize = true)
    private Location location;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("location_attributes")
    private Location locationAttributes;

    @Expose(serialize = false, deserialize = true)
    private Contact contact;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("contact_attributes")
    private Contact contactAttributes;

    @Expose(serialize = false, deserialize = true)
    private List<Cause> causes;

    @Expose
    @SerializedName("cause_ids")
    private List<Long> causeIds;

    @Expose(serialize = false, deserialize = true)
    private List<Skill> skills;

    @Expose
    @SerializedName("skill_ids")
    private List<Long> skillIds;

    @Expose
    @SerializedName("start_at")
    private String startAt;

    @Expose
    @SerializedName("end_at")
    private String endAt;

    @Expose
    @SerializedName("start_date_set")
    private Boolean startDateSet;

    @Expose
    @SerializedName("end_date_set")
    private Boolean endDateSet;

    @Expose
    @SerializedName("start_time_set")
    private Boolean startTimeSet;

    @Expose
    @SerializedName("end_time_set")
    private Boolean endTimeSet;

    @Expose(serialize = false, deserialize = true)
    private List<Image> images;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("images_attributes")
    private List<Image> imagesAttributes;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("images_attributes_64")
    private List<String> imagesAttributes64;

    @Expose
    private Opportunitable opportunitable;

    @Expose
    @SerializedName("opportunitable_type")
    private String opportunitableType;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getOngoing() {
        return isOngoing;
    }

    public void setOngoing(Boolean ongoing) {
        isOngoing = ongoing;
    }

    public Boolean getVirtual() {
        return isVirtual;
    }

    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public Integer getVolunteersNumber() {
        return volunteersNumber;
    }

    public void setVolunteersNumber(Integer volunteersNumber) {
        this.volunteersNumber = volunteersNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocationAttributes() {
        return locationAttributes;
    }

    public void setLocationAttributes(Location locationAttributes) {
        this.locationAttributes = locationAttributes;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContactAttributes() {
        return contactAttributes;
    }

    public void setContactAttributes(Contact contactAttributes) {
        this.contactAttributes = contactAttributes;
    }

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(List<Cause> causes) {
        this.causes = causes;
    }

    public List<Long> getCauseIds() {
        return causeIds;
    }

    public void setCauseIds(List<Long> causeIds) {
        this.causeIds = causeIds;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }

    public String getOpportunitableType() {
        return opportunitableType;
    }

    public void setOpportunitableType(String opportunitableType) {
        this.opportunitableType = opportunitableType;
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

    public String getTimeCommitment() {
        return timeCommitment;
    }

    public void setTimeCommitment(String timeCommitment) {
        this.timeCommitment = timeCommitment;
    }

    public String getOthersRequirements() {
        return othersRequirements;
    }

    public void setOthersRequirements(String othersRequirements) {
        this.othersRequirements = othersRequirements;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public Boolean getStartDateSet() {
        return startDateSet;
    }

    public void setStartDateSet(Boolean startDateSet) {
        this.startDateSet = startDateSet;
    }

    public Boolean getEndDateSet() {
        return endDateSet;
    }

    public void setEndDateSet(Boolean endDateSet) {
        this.endDateSet = endDateSet;
    }

    public Boolean getStartTimeSet() {
        return startTimeSet;
    }

    public void setStartTimeSet(Boolean startTimeSet) {
        this.startTimeSet = startTimeSet;
    }

    public Boolean getEndTimeSet() {
        return endTimeSet;
    }

    public void setEndTimeSet(Boolean endTimeSet) {
        this.endTimeSet = endTimeSet;
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

    @Override
    public Type getFeedableTypeAsEnum(){
        return Type.fromString(this.feedableType);
    }

    public String getFeedableType() {
        return feedableType;
    }

    public void setFeedableType(String feedableType) {
        this.feedableType = feedableType;
    }

    public Opportunity() {
    }

    public Opportunitable getOpportunitable() {
        return opportunitable;
    }

    public void setOpportunitable(Opportunitable opportunitable) {
        this.opportunitable = opportunitable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.feedableType);
        dest.writeValue(this.isOngoing);
        dest.writeValue(this.isVirtual);
        dest.writeValue(this.volunteersNumber);
        dest.writeString(this.description);
        dest.writeString(this.timeCommitment);
        dest.writeString(this.othersRequirements);
        dest.writeString(this.tags);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.locationAttributes, flags);
        dest.writeParcelable(this.contact, flags);
        dest.writeParcelable(this.contactAttributes, flags);
        dest.writeTypedList(this.causes);
        dest.writeList(this.causeIds);
        dest.writeTypedList(this.skills);
        dest.writeList(this.skillIds);
        dest.writeString(this.startAt);
        dest.writeString(this.endAt);
        dest.writeValue(this.startDateSet);
        dest.writeValue(this.endDateSet);
        dest.writeValue(this.startTimeSet);
        dest.writeValue(this.endTimeSet);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.imagesAttributes);
        dest.writeStringList(this.imagesAttributes64);
        dest.writeParcelable(this.opportunitable, flags);
        dest.writeString(this.opportunitableType);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    protected Opportunity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.feedableType = in.readString();
        this.isOngoing = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isVirtual = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.volunteersNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.timeCommitment = in.readString();
        this.othersRequirements = in.readString();
        this.tags = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.locationAttributes = in.readParcelable(Location.class.getClassLoader());
        this.contact = in.readParcelable(Contact.class.getClassLoader());
        this.contactAttributes = in.readParcelable(Contact.class.getClassLoader());
        this.causes = in.createTypedArrayList(Cause.CREATOR);
        this.causeIds = new ArrayList<Long>();
        in.readList(this.causeIds, Long.class.getClassLoader());
        this.skills = in.createTypedArrayList(Skill.CREATOR);
        this.skillIds = new ArrayList<Long>();
        in.readList(this.skillIds, Long.class.getClassLoader());
        this.startAt = in.readString();
        this.endAt = in.readString();
        this.startDateSet = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.endDateSet = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.startTimeSet = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.endTimeSet = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.images = in.createTypedArrayList(Image.CREATOR);
        this.imagesAttributes = in.createTypedArrayList(Image.CREATOR);
        this.imagesAttributes64 = in.createStringArrayList();
        this.opportunitable = in.readParcelable(Opportunitable.class.getClassLoader());
        this.opportunitableType = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<Opportunity> CREATOR = new Creator<Opportunity>() {
        @Override
        public Opportunity createFromParcel(Parcel source) {
            return new Opportunity(source);
        }

        @Override
        public Opportunity[] newArray(int size) {
            return new Opportunity[size];
        }
    };

    public enum LocationType {
        LOCATION, VIRTUAL;
    }

    public enum TimeType {
        DATED, ONGOING;
    }
}
