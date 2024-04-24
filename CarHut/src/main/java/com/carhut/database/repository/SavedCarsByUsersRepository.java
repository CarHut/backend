package com.carhut.database.repository;

import com.carhut.models.carhut.SavedCarByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedCarsByUsersRepository extends JpaRepository<SavedCarByUser, String> {

    @Query(value = "SELECT * FROM saved_cars_by_users WHERE user_id = :userId" , nativeQuery = true)
    List<SavedCarByUser> getSavedCarsByUserId(@Param("userId") String userId);

    @Query(value = "SELECT COUNT(*) FROM saved_cars_by_users", nativeQuery = true)
    int getSizeOfSavedCars();
}
