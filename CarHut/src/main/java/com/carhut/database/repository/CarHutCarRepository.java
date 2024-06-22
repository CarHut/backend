package com.carhut.database.repository;

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

    @Query(value = "SELECT c.seller_id FROM carhut_cars c WHERE id = :carId", nativeQuery = true)
    String getSellerIdByCarId(@Param("carId") String carId);

    @Query(value = "SELECT * FROM carhut_cars WHERE seller_id = :sellerId", nativeQuery = true)
    List<CarHutCar> getMyListings(@Param("sellerId") String sellerId);
}
