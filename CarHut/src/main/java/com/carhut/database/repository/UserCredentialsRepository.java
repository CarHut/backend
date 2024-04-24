package com.carhut.database.repository;

import com.carhut.models.security.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserCredentialsRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User findUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password = :password WHERE email = :email", nativeQuery = true)
    void setNewPasswordByEmail(@Param("password") String password, @Param("email") String email);
}
