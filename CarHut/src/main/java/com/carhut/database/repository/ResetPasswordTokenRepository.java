package com.carhut.database.repository;

import com.carhut.models.security.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResetPasswordTokenRepository extends JpaRepository<PasswordResetToken, String> {

    @Query(value = "SELECT * FROM password_reset_token WHERE token = :token", nativeQuery = true)
    PasswordResetToken getPasswordResetTokenByToken(@Param("token") String token);

}
