package com.carhut.services;

import com.carhut.database.repository.ResetPasswordTokenRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.mail.service.EmailService;
import com.carhut.models.requestmodels.AuthenticationRequest;
import com.carhut.models.requestmodels.PasswordResetRequestBody;
import com.carhut.models.security.*;
import com.carhut.paths.NetworkPaths;
import com.carhut.util.exceptions.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private JwtUtil jwtUtil = new JwtUtil();

    public RequestStatusEntity resetPasswordSendEmail(String email, UserCredentialsService userCredentialsService) throws CarHutAuthenticationException {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User userSecurityContextHolder = ((User)authentication.getPrincipal());
//
//        if (!userSecurityContextHolder.getUsername().equals(userCredentialsRepository.findUserByEmail(email).getUsername())) {
//            throw new CarHutAuthenticationException("Unauthorized access.");
//        }

        User user;

        try {
            user = userCredentialsService.getUserByEmail(email);
        }
        catch (Exception e) {
            throw new AuthenticationUserNotFoundException("Error occurred while getting user account details from database (searching by email). Message: " + e.getMessage());
        }

        if (user == null) {
            return RequestStatusEntity.ERROR;
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello, please click on the following link to finish the password reset procedure.\n");
        stringBuilder.append(NetworkPaths.publicIPAddress + ":3000/passwordReset?resetPasswordToken=").append(token);
        stringBuilder.append("\n\n");
        stringBuilder.append("CarHut");

        emailService.sendEmailMessage(email, NetworkPaths.emailSender, "Reset password request", stringBuilder.toString());
        return RequestStatusEntity.SUCCESS;
    }

    private void createPasswordResetToken(User user, String token) throws CarHutAuthenticationException {

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION_TIME);
        PasswordResetToken newTokenObject = new PasswordResetToken(user.getId(), token, user, Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()));
        try {
            resetPasswordTokenRepository.save(newTokenObject);
        }
        catch (Exception e) {
            throw new AuthenticationCanNotCreateTokenException("Error occurred while saving saving token to database. Message: " + e.getMessage());
        }
    }

    public boolean isPasswordResetTokenExpired(String token) throws CarHutAuthenticationException {
        PasswordResetToken passwordResetToken = null;
        try {
            passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(token);
        }
        catch (Exception e) {
            throw new AuthenticationResetPasswordTokenNotFoundException("Error occurred while getting reset password token. Message: " + e.getMessage());
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
                throw new AuthenticationCanNotDeleteTokenException("Error occurred while deleting token from database. Message: " + e.getMessage());
            }

            return true;
        }

        return false;
    }

    /**
     * @return Returns whether password reset went through or not.
     * **/
    public RequestStatusEntity resetPasswordInitiate(PasswordResetRequestBody passwordResetRequestBody) throws CarHutAuthenticationException {

        if (!passwordResetRequestBody.getNewPassword().equals(passwordResetRequestBody.getRepeatedNewPassword())) {
            return RequestStatusEntity.ERROR;
        }

        if (isPasswordResetTokenExpired(passwordResetRequestBody.getPasswordResetToken())) {
            return RequestStatusEntity.ERROR;
        }

        PasswordResetToken passwordResetToken = null;

        try {
            passwordResetToken = resetPasswordTokenRepository.getPasswordResetTokenByToken(passwordResetRequestBody.getPasswordResetToken());
        }
        catch (Exception e) {
            throw new AuthenticationResetPasswordTokenNotFoundException("Error occurred while getting reset password token. Message: " + e.getMessage());
        }

        try {
            resetPasswordTokenRepository.delete(passwordResetToken);
        }
        catch (Exception e) {
            throw new AuthenticationCanNotDeleteTokenException("Error occurred while deleting token from database. Message: " + e.getMessage());
        }

        try {
            userCredentialsRepository.setNewPasswordByEmail(
                    new BCryptPasswordEncoder(5).encode(passwordResetRequestBody.getNewPassword()),
                    passwordResetRequestBody.getEmail());
        }
        catch (Exception e) {
            throw new AuthenticationCanNotUpdateUserCredentialsException("Error occurred while updating user credentials. Message: " + e.getMessage());
        }

        return RequestStatusEntity.SUCCESS;
    }

    public boolean createAccountForUserLoggedWithGoogleOAuth2(String token) {
        OAuth2AuthenticatedPrincipal userInfo = introspector.introspect(token);

        if (userExists(userInfo)) {
            return false;
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
        return true;
    }

    private boolean userExists(OAuth2AuthenticatedPrincipal user) {
        User user1 = userCredentialsRepository.findUserByEmail(user.getAttributes().get("email").toString());
        return user1 != null;
    }

    public boolean isAuthenticationValid(AuthenticationRequest request) throws AuthenticationUserNotFoundException {
        try {
            User user = userCredentialsRepository.findUserByUsername(request.getUsername());
            if (!user.getRegistrationType().equals("standard")) {
                return false;
            }

            if (!user.isActive()) {
                return false;
            }
        } catch (Exception e) {
            throw new AuthenticationUserNotFoundException("User with name " + request.getUsername() + " was not found in database.");
        }

        return true;
    }
}
