package com.carhut.database.repository;

import com.carhut.models.carhut.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {


    @Query(value = "SELECT * FROM image WHERE id = :id", nativeQuery = true)
    Image getImageById(@Param("id") String id);


    @Query(value = "SELECT * FROM image WHERE car_id = :carId", nativeQuery = true)
    List<Image> getImagesByCarId(@Param("carId") String carId);
}
