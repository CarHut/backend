package com.carhut.services.auth;

import com.carhut.database.repository.security.ResetPasswordTokenRepository;
import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.services.security.UserCredentialsService;
import com.carhut.services.mail.EmailService;
import com.carhut.requests.requestmodels.AuthenticationRequest;
import com.carhut.requests.requestmodels.PasswordResetRequestBody;
import com.carhut.paths.NetworkPaths;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.security.models.Authority;
import com.carhut.security.models.PasswordResetToken;
import com.carhut.security.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Service;

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
    @Autowired
    private OpaqueTokenIntrospector introspector;

    @UserAccessCheck
    public ServiceStatusEntity resetPasswordSendEmail(String email, UserCredentialsService userCredentialsService) {

        User user;

        try {
            user = userCredentialsService.getUserByEmail(email);
            if (user == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);

        String stringBuilder = "Hello, please click on the following link to finish the password reset procedure.\n" +
                NetworkPaths.publicIPAddress + ":3000/passwordReset?resetPasswordToken=" + token +
                "\n\n" +
                "CarHut";

        emailService.sendEmailMessage(email, NetworkPaths.emailSender, "Reset password request", stringBuilder);
        return ServiceStatusEntity.SUCCESS;
    }

    private ServiceStatusEntity createPasswordResetToken(User user, String token) {

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION_TIME);
        PasswordResetToken newTokenObject = new PasswordResetToken(user.getId(), token, user, Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
        try {
            resetPasswordTokenRepository.save(newTokenObject);
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
        }

        return ServiceStatusEntity.SUCCESS;
    }

    public boolean isPasswordResetTokenExpired(String token) {
        PasswordResetToken passwordResetToken = null;
        try {
            passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(token);
        }
        catch (Exception e) {
            return true;
        }

        if (passwordResetToken == null) {
            return true;
        }

        LocalDateTime currentDate = LocalDateTime.now();
        Date date = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());

        if (date.after(passwordResetToken.getExpiryDate())) {
            try {
                resetPasswordTokenRepository.delete(passwordResetToken);
            }
            catch (Exception e) {
                return true;
            }

            return true;
        }

        return false;
    }

    /**
     * @return Returns whether password reset went through or not.
     * **/
    public ServiceStatusEntity resetPasswordInitiate(PasswordResetRequestBody passwordResetRequestBody) {

        if (!passwordResetRequestBody.getNewPassword().equals(passwordResetRequestBody.getRepeatedNewPassword())) {
            return ServiceStatusEntity.ERROR;
        }

        if (isPasswordResetTokenExpired(passwordResetRequestBody.getPasswordResetToken())) {
            return ServiceStatusEntity.ERROR;
        }

        PasswordResetToken passwordResetToken = null;

        try {
            passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(passwordResetRequestBody.getPasswordResetToken());

            if (passwordResetToken == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        try {
            resetPasswordTokenRepository.delete(passwordResetToken);
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY;
        }

        try {
            userCredentialsRepository.setNewPasswordByEmail(
                    new BCryptPasswordEncoder(5).encode(passwordResetRequestBody.getNewPassword()),
                    passwordResetRequestBody.getEmail());
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_UPDATED_EXCEPTIONALLY;
        }

        return ServiceStatusEntity.SUCCESS;
    }

    public ServiceStatusEntity createAccountForUserLoggedWithGoogleOAuth2(String token) {
        OAuth2AuthenticatedPrincipal userInfo = introspector.introspect(token);

        if (userExists(userInfo)) {
            return ServiceStatusEntity.ERROR;
        }

        User user = new User(
                userInfo.getAttributes().get("name").toString(),
                "",
                userInfo.getAttributes().get("email").toString(),
                "",
                "",
                new java.sql.Date(System.currentTimeMillis()),
                0,
                true,
                new Authority(2, "ROLE_USER"),
                "google"
        );

        userCredentialsRepository.save(user);
        return ServiceStatusEntity.SUCCESS;
    }

    private boolean userExists(OAuth2AuthenticatedPrincipal user) {
        User user1 = userCredentialsRepository.findUserByEmail(user.getAttributes().get("email").toString());
        return user1 != null;
    }

    public boolean isAuthenticationValid(AuthenticationRequest request) {
        try {
            User user = userCredentialsRepository.findUserByUsername(request.getUsername());
            if (!user.getRegistrationType().equals("standard")) {
                return false;
            }

            if (!user.isActive()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
