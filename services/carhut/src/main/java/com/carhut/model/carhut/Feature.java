package com.carhut.model.carhut;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "feature")
public class Feature {

    @Id
    private int id;
    private String feature;
    private String description;

    public Feature() {}

    public Feature(int id, String feature, String description) {
        this.id = id;
        this.feature = feature;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
