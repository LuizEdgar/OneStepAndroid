package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Volunteer {

    @Expose private Long id;

    @Expose private String about;

    @Expose private String occupation;

    @Expose
    @SerializedName("day_availability")
    private Integer dayAvailability;

    @Expose
    @SerializedName("period_availability")
    private Integer periodAvailability;

    @Expose private Boolean volunteered;

    @Expose private String cpf;

    @Expose private String rg;

    @Expose private String gender;

    @Expose private Boolean verified;

    @Expose(serialize = false, deserialize = true)
    private List<Education> educations;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("educations_attributes")
    private List<Education> educationsAttributes;

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

    public Integer getDayAvailability() {
        return dayAvailability;
    }

    public void setDayAvailability(Integer dayAvailability) {
        this.dayAvailability = dayAvailability;
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

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
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

    public List<Education> getEducationsAttributes() {
        return educationsAttributes;
    }

    public void setEducationsAttributes(List<Education> educationsAttributes) {
        this.educationsAttributes = educationsAttributes;
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
}
