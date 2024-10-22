package imageservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "car_images")
public class CarImage {

    @Id
    private String id;
    private String path;
    private String carId;
    private String userId;
    private boolean isActive;

    public CarImage() {}

    public CarImage(String id, String path, String carId, String userId, boolean isActive) {
        this.id = id;
        this.path = path;
        this.carId = carId;
        this.userId = userId;
        this.isActive = isActive;
    }

    public CarImage(String path, String carId, String userId, boolean isActive) {
        this.id = generateId(path, carId, userId);
        this.path = path;
        this.carId = carId;
        this.userId = userId;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private String generateId(String path, String carId, String userId) {
        String dataToHash = path + carId + userId;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            // Convert bytes to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
