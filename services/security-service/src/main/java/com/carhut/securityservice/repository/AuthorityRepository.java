package com.carhut.securityservice.repository;

import com.carhut.securityservice.security.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @Query(value = "SELECT * FROM authorities WHERE id = :id", nativeQuery = true)
    Authority getAuthorityById(@Param("id") Integer authorityId);

}
