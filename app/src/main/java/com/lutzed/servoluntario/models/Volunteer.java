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

public class Volunteer implements Parcelable {

    @Expose private Long id;

    @Expose private String name;

    @Expose private String about;

    @Expose private String occupation;

    @Expose
    @SerializedName("period_availability")
    private Integer periodAvailability;

    @Expose private Boolean volunteered;

    @Expose private String cpf;

    @Expose private String rg;

    @Expose private String gender;

    @Expose private Boolean verified;

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
    @SerializedName("birth_at")
    private String birthAt;

    @Expose
    @SerializedName("user_id")
    private Long userId;

    @Expose
    @SerializedName("profile_image")
    private Image profileImage;

    @Expose
    @SerializedName("profile_image_64")
    private String profileImage64;

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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getPeriodAvailability() {
        return periodAvailability;
    }

    public void setPeriodAvailability(Integer periodAvailability) {
        this.periodAvailability = periodAvailability;
    }

    public Boolean getVolunteered() {
        return volunteered;
    }

    public void setVolunteered(Boolean volunteered) {
        this.volunteered = volunteered;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getBirthAt() {
        return birthAt;
    }

    public void setBirthAt(String birthAt) {
        this.birthAt = birthAt;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getGender() {
        return gender;
    }

    public GenderEnum getGenderEnum() {
        return GenderEnum.fromString(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage64() {
        return profileImage64;
    }

    public void setProfileImage64(String profileImage64) {
        this.profileImage64 = profileImage64;
    }

    public enum GenderEnum {
        OTHER("other"),
        MALE("male"),
        FEMALE("female");

        private final String value;

        GenderEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static GenderEnum fromString(String value) {
            switch (value) {
                case "male":
                    return MALE;
                case "female":
                    return FEMALE;
                default:
                    return OTHER;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.about);
        dest.writeString(this.occupation);
        dest.writeValue(this.periodAvailability);
        dest.writeValue(this.volunteered);
        dest.writeString(this.cpf);
        dest.writeString(this.rg);
        dest.writeString(this.gender);
        dest.writeValue(this.verified);
        dest.writeTypedList(this.locations);
        dest.writeTypedList(this.locationsAttributes);
        dest.writeTypedList(this.contacts);
        dest.writeTypedList(this.contactsAttributes);
        dest.writeTypedList(this.causes);
        dest.writeList(this.causeIds);
        dest.writeTypedList(this.skills);
        dest.writeList(this.skillIds);
        dest.writeString(this.birthAt);
        dest.writeValue(this.userId);
        dest.writeParcelable(this.profileImage, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public Volunteer() {
    }

    protected Volunteer(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.about = in.readString();
        this.occupation = in.readString();
        this.periodAvailability = (Integer) in.readValue(Integer.class.getClassLoader());
        this.volunteered = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.cpf = in.readString();
        this.rg = in.readString();
        this.gender = in.readString();
        this.verified = (Boolean) in.readValue(Boolean.class.getClassLoader());
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
        this.birthAt = in.readString();
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.profileImage = in.readParcelable(Image.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Volunteer> CREATOR = new Parcelable.Creator<Volunteer>() {
        @Override
        public Volunteer createFromParcel(Parcel source) {
            return new Volunteer(source);
        }

        @Override
        public Volunteer[] newArray(int size) {
            return new Volunteer[size];
        }
    };
}
