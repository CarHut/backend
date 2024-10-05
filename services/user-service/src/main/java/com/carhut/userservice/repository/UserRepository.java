package com.carhut.userservice.repository;

import com.carhut.userservice.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User getUserByUsername(@Param("username") String username);

}
