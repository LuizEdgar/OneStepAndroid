package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.lutzed.servoluntario.models.User.Kind.VOLUNTEER;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class User {

    @Expose private Long id;

    @Expose private String name;

    @Expose private String username;

    @Expose private String email;

    @Expose private String kind;

    @Expose private String auth;

    @Expose(serialize = true, deserialize = false)
    private String password;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("facebook_token")
    private String facebookToken;

    @Expose(serialize = false, deserialize = true)
    private List<Location> locations;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("locations_attributes")
    private List<Location> locationsAttributes;

    @Expose(serialize = false, deserialize = true)
    private List<Phone> phones;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("phones_attributes")
    private List<Phone> phonesAttributes;

    @Expose(serialize = false, deserialize = true)
    private Volunteer volunteer;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("volunteer_attributes")
    private Volunteer volunteerAttributes;

    @Expose(serialize = false, deserialize = true)
    private Organization organization;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("organization_attributes")
    private Organization organizationAttributes;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;

    public List<Long> getSkillsIds(){
        List<Long> ids = new ArrayList<>();
        List<Skill> skills;
        if (getKindEnum() == VOLUNTEER){
            skills = getVolunteer().getSkills();
        }else{
            skills = getOrganization().getSkills();
        }
        for (Skill skill : skills) {
            ids.add(skill.getId());
        }
        return ids;
    }

    public List<Long> getCauseIds(){
        List<Long> ids = new ArrayList<>();
        List<Cause> causes;
        if (getKindEnum() == VOLUNTEER){
            causes = getVolunteer().getCauses();
        }else{
            causes = getOrganization().getCauses();
        }
        for (Cause cause : causes) {
            ids.add(cause.getId());
        }
        return ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKind() {
        return kind;
    }

    public Kind getKindEnum() {
        return Kind.fromString(kind);
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Volunteer getVolunteerAttributes() {
        return volunteerAttributes;
    }

    public void setVolunteerAttributes(Volunteer volunteerAttributes) {
        this.volunteerAttributes = volunteerAttributes;
    }

    public Organization getOrganizationAttributes() {
        return organizationAttributes;
    }

    public void setOrganizationAttributes(Organization organizationAttributes) {
        this.organizationAttributes = organizationAttributes;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Phone> getPhonesAttributes() {
        return phonesAttributes;
    }

    public void setPhonesAttributes(List<Phone> phonesAttributes) {
        this.phonesAttributes = phonesAttributes;
    }

    public List<Location> getLocationsAttributes() {
        return locationsAttributes;
    }

    public void setLocationsAttributes(List<Location> locationsAttributes) {
        this.locationsAttributes = locationsAttributes;
    }

    public enum Kind {
        VOLUNTEER("volunteer"),
        ORGANIZATION("organization");

        private final String kind;

        Kind(String kind) {
            this.kind = kind;
        }

        @Override
        public String toString() {
            return kind;
        }

        public static Kind fromString(String kindString) {
            switch (kindString) {
                case "volunteer":
                    return VOLUNTEER;
                case "organization":
                    return ORGANIZATION;
                default:
                    return VOLUNTEER;
            }
        }
    }
}
