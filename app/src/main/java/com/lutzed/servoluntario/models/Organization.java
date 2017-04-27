package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Organization {

    @Expose private Long id;

    @Expose private String cnpj;

    @Expose private String site;

    @Expose private String about;

    @Expose private String requirements;

    @Expose private String goal;

    @Expose private String need;

    @Expose private Integer size;

    @Expose(serialize = false, deserialize = true)
    private List<Cause> causes;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("cause_ids")
    private List<Integer> causeIds;

    @Expose(serialize = false, deserialize = true)
    private List<Skill> skills;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("skill_ids")
    private List<Integer> skillIds;

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

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
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

    public List<Integer> getCauseIds() {
        return causeIds;
    }

    public void setCauseIds(List<Integer> causeIds) {
        this.causeIds = causeIds;
    }

    public List<Integer> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Integer> skillIds) {
        this.skillIds = skillIds;
    }
}
