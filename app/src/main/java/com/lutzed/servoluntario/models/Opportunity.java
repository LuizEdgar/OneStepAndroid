package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Opportunity {

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
    @SerializedName("timeCommitment")
    private String time_commitment;

    @Expose
    @SerializedName("othersRequirements")
    private String others_requirements;

    @Expose private String tags;

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
    private Long opportunitableType;

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

    public String getTime_commitment() {
        return time_commitment;
    }

    public void setTime_commitment(String time_commitment) {
        this.time_commitment = time_commitment;
    }

    public String getOthers_requirements() {
        return others_requirements;
    }

    public void setOthers_requirements(String others_requirements) {
        this.others_requirements = others_requirements;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public Long getOpportunitableType() {
        return opportunitableType;
    }

    public void setOpportunitableType(Long opportunitableType) {
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

    public void addCause(Cause cause){
        if (causes == null) causes = new ArrayList<>();
        causes.add(cause);

        if (causeIds == null) causeIds = new ArrayList<>();
        causeIds.add(cause.getId());
    }

    public void addSkill(Skill skill){
        if (skills == null) skills = new ArrayList<>();
        skills.add(skill);

        if (skillIds == null) skillIds = new ArrayList<>();
        skillIds.add(skill.getId());
    }
}
