package com.carhut.models.carhut;

import com.carhut.models.carhut.converters.BrandsAndModelsAttributeConverter;
import com.carhut.models.requestmodels.ModelsPostModel;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
@Table(name = "saved_searches")
public class SavedSearch {

    @Id
    private String id;
    private String userId;
    private String sortBy;
    private Integer offersPerPage;
    @Convert(converter = BrandsAndModelsAttributeConverter.class)
    @Column(name = "brands_and_models", length = 10000)
    private List<ModelsPostModel> brandsAndModels;
    private String priceFrom;
    private String priceTo;
    private String mileageFrom;
    private String mileageTo;
    private String fuelType;
    private String gearboxType;
    private String powertrainType;
    private String powerFrom;
    private String powerTo;

    public SavedSearch() {}

    public SavedSearch(String id, String userId, String sortBy, Integer offersPerPage,
                       String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType,
                       String gearboxType, String powertrainType, String powerFrom, String powerTo, List<ModelsPostModel> brandsAndModels) {
        this.id = id;
        this.userId = userId;
        this.sortBy = sortBy;
        this.offersPerPage = offersPerPage;
        this.brandsAndModels = brandsAndModels;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.mileageFrom = mileageFrom;
        this.mileageTo = mileageTo;
        this.fuelType = fuelType;
        this.gearboxType = gearboxType;
        this.powertrainType = powertrainType;
        this.powerFrom = powerFrom;
        this.powerTo = powerTo;
    }

    public SavedSearch(String userId, String sortBy, Integer offersPerPage,
                       String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType,
                       String gearboxType, String powertrainType, String powerFrom, String powerTo, List<ModelsPostModel> brandsAndModels) {
        this.id = generateId(userId, sortBy, offersPerPage, brandsAndModels, priceFrom, priceTo, mileageFrom, mileageTo,
                fuelType, gearboxType, powertrainType, powerFrom, powerTo);
        this.userId = userId;
        this.sortBy = sortBy;
        this.offersPerPage = offersPerPage;
        this.brandsAndModels = brandsAndModels;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.mileageFrom = mileageFrom;
        this.mileageTo = mileageTo;
        this.fuelType = fuelType;
        this.gearboxType = gearboxType;
        this.powertrainType = powertrainType;
        this.powerFrom = powerFrom;
        this.powerTo = powerTo;
    }

    private String generateId(String userId, String sortBy, Integer offersPerPage, List<ModelsPostModel> brandsAndModels,
                              String priceFrom, String priceTo, String mileageFrom, String mileageTo, String fuelType,
                              String gearboxType, String powertrainType, String powerFrom, String powerTo) {
        String input = userId + sortBy + offersPerPage + brandsAndModels.toString() + priceFrom + priceTo + mileageFrom
                + mileageTo + fuelType + gearboxType + powertrainType + powerFrom + powerTo;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getOffersPerPage() {
        return offersPerPage;
    }

    public void setOffersPerPage(Integer offersPerPage) {
        this.offersPerPage = offersPerPage;
    }

    public List<ModelsPostModel> getBrandsAndModels() {
        return brandsAndModels;
    }

    public void setBrandsAndModels(List<ModelsPostModel> brandsAndModels) {
        this.brandsAndModels = brandsAndModels;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getMileageFrom() {
        return mileageFrom;
    }

    public void setMileageFrom(String mileageFrom) {
        this.mileageFrom = mileageFrom;
    }

    public String getMileageTo() {
        return mileageTo;
    }

    public void setMileageTo(String mileageTo) {
        this.mileageTo = mileageTo;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public String getPowertrainType() {
        return powertrainType;
    }

    public void setPowertrainType(String powertrainType) {
        this.powertrainType = powertrainType;
    }

    public String getPowerFrom() {
        return powerFrom;
    }

    public void setPowerFrom(String powerFrom) {
        this.powerFrom = powerFrom;
    }

    public String getPowerTo() {
        return powerTo;
    }

    public void setPowerTo(String powerTo) {
        this.powerTo = powerTo;
    }
}
