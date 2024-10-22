package com.carhut.savedcarsservice.repository;

import com.carhut.commons.model.CarHutCar;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedCarsByUsersRepository extends JpaRepository<SavedCarByUser, String> {

    @Query(value = "SELECT COUNT(*) FROM saved_cars_by_users", nativeQuery = true)
    int getSizeOfSavedCars();

    @Query(value = """
    SELECT s.* FROM saved_cars_by_users s
    WHERE s.user_id = :userId
    """, nativeQuery = true)
    List<SavedCarByUser> getSavedCarsByUserId(@Param("userId") String userId);

    @Query(value = """
    SELECT s.* FROM saved_cars_by_users s
    WHERE s.car_id = :carId
    """, nativeQuery = true)
    SavedCarByUser getSavedCarByByCarId(@Param("carId") String carId);
}
