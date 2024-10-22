package com.carhut.userservice.repository;

import com.carhut.userservice.repository.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User getUserByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE id = :userId", nativeQuery = true)
    User getUserByUserId(@Param("userId") String userId);

    @Modifying
    @Query(value = "UPDATE users SET num_of_offered_cars = num_of_offered_cars + 1 WHERE id = :userId", nativeQuery = true)
    void updateCarCountByUserId(@Param("userId") String userId);

    @Modifying
    @Query(value = "UPDATE users SET num_of_offered_cars = num_of_offered_cars - 1 WHERE id = :userId", nativeQuery = true)
    void decrementOfferCountByUserId(@Param("userId") String userId);
}
