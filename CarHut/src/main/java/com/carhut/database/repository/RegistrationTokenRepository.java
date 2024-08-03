package com.carhut.database.repository;

import com.carhut.security.models.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, String> {

    @Query(value = "SELECT * FROM registration_token WHERE token = :token", nativeQuery = true)
    RegistrationToken findRegistrationTokenByToken(@Param("token") String token);

    @Query(value = "SELECT * FROM registration_token WHERE user_id = :userId", nativeQuery = true)
    RegistrationToken findRegistrationTokenByUserId(@Param("userId") String userId);

}
