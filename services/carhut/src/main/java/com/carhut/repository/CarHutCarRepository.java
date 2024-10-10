package com.carhut.repository;

import com.carhut.models.carhut.CarHutCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarHutCarRepository extends JpaRepository<CarHutCar, String> {

    @Query(value = "SELECT * FROM carhut_cars WHERE id = :id",nativeQuery = true)
    CarHutCar getCarWithId(@Param("id") String carId);

    @Query(value = "SELECT * FROM carhut_cars WHERE is_active = true", nativeQuery = true)
    List<CarHutCar> getAllCars();

    @Query(value = "SELECT * FROM carhut_cars WHERE seller_id = :sellerId", nativeQuery = true)
    List<CarHutCar> getListingsByUserId(@Param("sellerId") String sellerId);
}
