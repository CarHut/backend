package com.carhut.database.repository;

import com.carhut.models.carhut.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, String> {

    @Query(value = "SELECT * FROM car_images WHERE car_id = :carId", nativeQuery = true)
    List<CarImage> getImagesByCarId(@Param("carId") String carId);
}
