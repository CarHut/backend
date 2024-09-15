package com.carhut.savedcarsservice.repositories;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedCarsByUsersRepository extends JpaRepository<SavedCarByUser, String> {

    @Query(value = "SELECT COUNT(*) FROM saved_cars_by_users", nativeQuery = true)
    int getSizeOfSavedCars();

    @Query(value = "SELECT c FROM carhut_cars c WHERE c.id = (SELECT s.car_id FROM saved_cars_by_user s WHERE s.user_id = userId)", nativeQuery = true)
    List<CarHutCar> getSavedCarsByUserId(@Param("userId") String userId);

}
