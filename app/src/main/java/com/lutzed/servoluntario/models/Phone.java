package com.lutzed.servoluntario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luizfreitas on 25/07/2016.
 */

public class Phone {

    @Expose private Long id;

    @Expose private String number;

    @Expose private String kind;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind.toString();
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

    public enum Kind {
        OTHER("other"),
        MOBILE("mobile"),
        WORK("work"),
        HOME("home"),
        FAX("fax");

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
                case "mobile":
                    return MOBILE;
                case "work":
                    return WORK;
                case "home":
                    return HOME;
                case "fax":
                    return FAX;
                default:
                    return OTHER;
            }
        }
    }
}
