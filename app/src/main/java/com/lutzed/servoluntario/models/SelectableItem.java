package com.lutzed.servoluntario.models;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luizfreitas on 02/05/2017.
 */

public abstract class SelectableItem implements Parcelable {

    @Expose protected Long id;

    @Expose protected String name;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Expose protected Image image;

    @Expose protected String description;

    @Expose
    @SerializedName("created_at")
    protected String createdAt;

    @Expose
    @SerializedName("updated_at")
    protected String updatedAt;

    protected boolean isAddPlaceholder;

    protected boolean isSelected;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isAddPlaceholder() {
        return isAddPlaceholder;
    }

    public void setAddPlaceholder(boolean addPlaceholder) {
        isAddPlaceholder = addPlaceholder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof SelectableItem && this.getId() != null && this.getId().equals(((SelectableItem) obj).getId());
    }
}
