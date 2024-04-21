package com.carhut.services;

import com.carhut.database.repository.ResetPasswordTokenRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.mail.service.EmailService;
import com.carhut.models.User;
import com.carhut.models.security.PasswordResetRequestBody;
import com.carhut.models.security.PasswordResetToken;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public RequestStatusEntity resetPasswordSendEmail(String email, UserCredentialsService userCredentialsService) {
        User user = userCredentialsService.getUserByEmail(email);

        if (user == null) {
            System.out.println("User was not found");
            return RequestStatusEntity.ERROR;
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);
        emailService.sendEmailMessage(email, "Reset password request", token);
        return RequestStatusEntity.SUCCESS;
    }

    private void createPasswordResetToken(User user, String token) {
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION_TIME);
        PasswordResetToken newTokenObject = new PasswordResetToken(user.getId(), token, user, Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
        resetPasswordTokenRepository.save(newTokenObject);
    }

    public boolean isPasswordResetTokenExpired(String token) {
        PasswordResetToken passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(token);

        if (passwordResetToken == null) {
            return true;
        }

        LocalDateTime currentDate = LocalDateTime.now();

        Date date = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());

        if (date.after(passwordResetToken.getExpiryDate())) {
            resetPasswordTokenRepository.delete(passwordResetToken);
            return true;
        }

        return false;
    }

    /**
     * @return Returns whether password reset went through or not.
     * **/
    public RequestStatusEntity resetPasswordInitiate(PasswordResetRequestBody passwordResetRequestBody) {

        if (!passwordResetRequestBody.getNewPassword().equals(passwordResetRequestBody.getRepeatedNewPassword())) {
            return RequestStatusEntity.ERROR;
        }

        if (isPasswordResetTokenExpired(passwordResetRequestBody.getPasswordResetToken())) {
            return RequestStatusEntity.ERROR;
        }

        PasswordResetToken passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(passwordResetRequestBody.getPasswordResetToken());
        resetPasswordTokenRepository.delete(passwordResetToken);
        userCredentialsRepository.setNewPasswordByEmail(
                new BCryptPasswordEncoder(5).encode(passwordResetRequestBody.getNewPassword()),
                        passwordResetRequestBody.getEmail());

        return RequestStatusEntity.SUCCESS;
    }
}
