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

public class Opportunity implements Parcelable {

    @Expose private Long id;

    @Expose private String title;

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
    @SerializedName("start_date_at")
    private String startDateAt;

    @Expose
    @SerializedName("end_date_at")
    private String endDateAt;

    @Expose
    @SerializedName("start_time_at")
    private String startTimeAt;

    @Expose
    @SerializedName("end_time_at")
    private String endTimeAt;

    @Expose
    @SerializedName("opportunitable_id")
    private Long opportunitableId;

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

    public String getStartDateAt() {
        return startDateAt;
    }

    public void setStartDateAt(String startDateAt) {
        this.startDateAt = startDateAt;
    }

    public String getEndDateAt() {
        return endDateAt;
    }

    public void setEndDateAt(String endDateAt) {
        this.endDateAt = endDateAt;
    }

    public String getStartTimeAt() {
        return startTimeAt;
    }

    public void setStartTimeAt(String startTimeAt) {
        this.startTimeAt = startTimeAt;
    }

    public String getEndTimeAt() {
        return endTimeAt;
    }

    public void setEndTimeAt(String endTimeAt) {
        this.endTimeAt = endTimeAt;
    }

    public Long getOpportunitableId() {
        return opportunitableId;
    }

    public void setOpportunitableId(Long opportunitableId) {
        this.opportunitableId = opportunitableId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
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
        dest.writeString(this.startDateAt);
        dest.writeString(this.endDateAt);
        dest.writeString(this.startTimeAt);
        dest.writeString(this.endTimeAt);
        dest.writeValue(this.opportunitableId);
        dest.writeString(this.opportunitableType);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public Opportunity() {
    }

    protected Opportunity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
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
        this.startDateAt = in.readString();
        this.endDateAt = in.readString();
        this.startTimeAt = in.readString();
        this.endTimeAt = in.readString();
        this.opportunitableId = (Long) in.readValue(Long.class.getClassLoader());
        this.opportunitableType = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Opportunity> CREATOR = new Parcelable.Creator<Opportunity>() {
        @Override
        public Opportunity createFromParcel(Parcel source) {
            return new Opportunity(source);
        }

        @Override
        public Opportunity[] newArray(int size) {
            return new Opportunity[size];
        }
    };
}
