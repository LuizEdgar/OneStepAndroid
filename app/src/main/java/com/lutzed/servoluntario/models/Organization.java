package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Organization implements FeedItem {

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
}
