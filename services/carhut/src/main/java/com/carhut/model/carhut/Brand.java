package com.carhut.model.carhut;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    private int id;
    private String brand;

    public Brand(int id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public Brand() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
