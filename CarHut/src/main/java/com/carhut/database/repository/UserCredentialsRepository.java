package com.carhut.database.repository;

import com.carhut.models.security.User;
import jakarta.transaction.Transactional;
import org.hibernate.tool.schema.internal.SchemaTruncatorImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserCredentialsRepository extends JpaRepository<User, String> {


    @Query(value = "SELECT u.id FROM users u WHERE u.username = :username", nativeQuery = true)
    String findUserIdByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User findUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User findUserByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    User findUserById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password = :password WHERE email = :email", nativeQuery = true)
    void setNewPasswordByEmail(@Param("password") String password, @Param("email") String email);

    @Query(value = "SELECT u.username FROM users u WHERE u.id = (SELECT c.seller_id FROM carhut_cars c WHERE c.id = :carId)", nativeQuery = true)
    String findUserUsernameByCarId(@Param("carId") String carId);

    @Query(value = "SELECT u.email FROM users u WHERE u.email = :email", nativeQuery = true)
    String findEmail(@Param("email") String email);

    @Query(value = "SELECT u.username FROM users u WHERE u.username = :username", nativeQuery = true)
    String findUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_active = true WHERE id = :userId", nativeQuery = true)
    void setIsActiveToUserByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET num_of_offered_cars = num_of_offered_cars+1 WHERE id = :userId", nativeQuery = true)
    void updateCountOfAddedOffers(@Param("userId") String userId);

    @Query(value = "SELECT username FROM users WHERE id = :userId", nativeQuery = true)
    String getUsernameByUserId(String userId);

    @Query(value = "SELECT u.first_name || ' ' ||u.surname FROM users u WHERE u.id = :userId", nativeQuery = true)
    String getFirstNameAndSurnameByUserId(@Param("userId") String userId);

    @Query(value = "SELECT u.num_of_offered_cars FROM users u WHERE u.id = :userId", nativeQuery = true)
    Integer getOffersNumByUserId(@Param("userId") String userId);

    @Query(value = "SELECT u.email FROM users u WHERE u.id = :userId", nativeQuery = true)
    String getEmailByUserId(@Param("userId") String userId);

    @Query(value = "SELECT id FROM users WHERE username = :username", nativeQuery = true)
    String getUserIdByUsername(@Param("username") String username);
}
