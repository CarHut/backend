package com.carhut.services;

import com.carhut.database.repository.RegistrationTokenRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RegistrationStatus;
import com.carhut.mail.service.EmailService;
import com.carhut.models.security.Authority;
import com.carhut.models.security.RegisterUserBody;
import com.carhut.models.security.RegistrationToken;
import com.carhut.models.security.User;
import com.carhut.paths.NetworkPaths;
import com.carhut.util.exceptions.registration.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;

    public RegistrationStatus registerInitiate(RegisterUserBody registerUserBody) throws RegistrationException {

        if (wasRegistrationVerificationAlreadySentToEmail(registerUserBody.getEmail())) {
            return RegistrationStatus.REGISTRATION_TOKEN_ALREADY_SENT;
        }

        RegistrationStatus validationStatusOfRegistration = validateRegistrationBody(registerUserBody);

        if (validationStatusOfRegistration != RegistrationStatus.SUCCESS) {
            return validationStatusOfRegistration;
        }

        saveInformationOfUserToDatabase(registerUserBody);

        RegistrationToken registrationToken = generateRegistrationToken(registerUserBody);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello " + registerUserBody.getUsername() + ",\n");
        stringBuilder.append("We are very excited that we can welcome you onboard.\n");
        stringBuilder.append("Click on the following link to finish verification process.\n\n");
        stringBuilder.append(NetworkPaths.successfulRegistrationVerificationAddress + "?token=" + registrationToken.getToken() + "\n\n");
        stringBuilder.append("CarHut");

        emailService.sendEmailMessage(registerUserBody.getEmail(), NetworkPaths.emailSender, "CarHut account verification", stringBuilder.toString());

        return RegistrationStatus.SUCCESS;
    }

    // Check if token exists and user is in inactive state
    private boolean wasRegistrationVerificationAlreadySentToEmail(String email) {
        User user = userCredentialsRepository.findUserByEmail(email);

        if (user == null) {
            return false;
        }

        if (!user.isActive()) {
            RegistrationToken registrationToken = registrationTokenRepository.findRegistrationTokenByUserId(user.getId());

            if (registrationToken != null) {
                return true;
            }
        }
        return false;
    }

    private RegistrationToken generateRegistrationToken(RegisterUserBody registerUserBody) throws RegistrationException {
        String userId;
        try {
            userId = userCredentialsRepository.findUserIdByUsername(registerUserBody.getUsername());
        }
        catch (Exception e) {
            throw new RegistrationException("User was not found while generating new token.");
        }

        if (userId == null) {
            throw new RegistrationException("User was not found while generating new token.");
        }

        RegistrationToken registrationToken = new RegistrationToken(userId);
        try {
            registrationTokenRepository.save(registrationToken);
        }
        catch (Exception e) {
            throw new RegistrationException("Error occurred while saving token to database.");
        }

        return registrationToken;
    }


    private void saveInformationOfUserToDatabase(RegisterUserBody registerUserBody) throws RegistrationException {

        String hashedPassword = hashPassword(registerUserBody.getPassword());

        User user = new User(registerUserBody.getUsername(), hashedPassword, registerUserBody.getEmail(),
                registerUserBody.getFirstName(), registerUserBody.getSurname(), new Date(System.currentTimeMillis()),
                0, false, new Authority(2, "ROLE_USER"));

        try {
            userCredentialsRepository.save(user);
        }
        catch (Exception e) {
            throw new RegistrationException("Error occurred while saving new user to database. Message: " + e.getMessage());
        }

    }

    private String hashPassword(String password) {
        return new BCryptPasswordEncoder(5).encode(password);
    }

    private RegistrationStatus validateRegistrationBody(RegisterUserBody registerUserBody) throws RegistrationException {
        if (!isFirstNameValid(registerUserBody.getFirstName())) {
            return RegistrationStatus.INVALID_FIRST_NAME;
        }

        if (!isFirstNameValid(registerUserBody.getSurname())) {
            return RegistrationStatus.INVALID_SURNAME;
        }

        RegistrationStatus usernameStatus = usernameValidation(registerUserBody.getUsername());
        if (usernameStatus != RegistrationStatus.SUCCESS) {
            return usernameStatus;
        }

        RegistrationStatus emailStatus = emailValidation(registerUserBody.getEmail());
        if (emailStatus != RegistrationStatus.SUCCESS) {
            return emailStatus;
        }

        RegistrationStatus passwordStatus = passwordValidation(registerUserBody.getPassword(), registerUserBody.getRepeatPassword());
        if (passwordStatus != RegistrationStatus.SUCCESS) {
            return passwordStatus;
        }

        return RegistrationStatus.SUCCESS;
    }

    private RegistrationStatus passwordValidation(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            return RegistrationStatus.PASSWORDS_DO_NOT_MATCH;
        }

        if (password.length() < 8) {
            return RegistrationStatus.SHORT_PASSWORD;
        }

        if (!doesPasswordHaveUpperCharacter(password)) {
            return RegistrationStatus.PASSWORD_DOES_NOT_CONTAIN_UPPER_CHARACTER;
        }

        if (!doesPasswordHaveDigit(password)) {
            return RegistrationStatus.PASSWORD_DOES_NOT_CONTAIN_DIGIT;
        }

        if (!doesPasswordHaveSpecialCharacter(password)) {
            return RegistrationStatus.PASSWORD_DOES_NOT_CONTAIN_SPECIAL_CHARACTER;
        }

        if (doesPasswordHaveForbiddenCharacter(password)) {
            return RegistrationStatus.INVALID_PASSWORD;
        }

        return RegistrationStatus.SUCCESS;
    }

    private boolean doesPasswordHaveForbiddenCharacter(String password) {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9@$!%*?&_]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    private boolean doesPasswordHaveSpecialCharacter(String password) {
        Pattern pattern = Pattern.compile(".*[@$!%*?&_].*");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean doesPasswordHaveDigit(String password) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean doesPasswordHaveUpperCharacter(String password) {
        Pattern pattern = Pattern.compile(".*[A-Z].*");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private RegistrationStatus usernameValidation(String username) throws RegistrationException {
        if (!isUsernameValid(username)) {
            return RegistrationStatus.INVALID_USERNAME;
        }

        if (isUsernameInUse(username)) {
            return RegistrationStatus.USERNAME_IN_USE;
        }

        return RegistrationStatus.SUCCESS;
    }

    private boolean isUsernameInUse(String username) throws RegistrationException {
        try {
            return userCredentialsRepository.findUsername(username) != null;
        }
        catch (Exception e) {
            throw new RegistrationException("Problem occurred while getting username from database. Message: " + e.getMessage());
        }
    }

    private boolean isUsernameValid(String username) {
        Pattern pattern = Pattern.compile("^(?!.*[_.-]{2})[A-Za-z0-9]+(?:[_.-][A-Za-z0-9]+)*$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private RegistrationStatus emailValidation(String email) throws RegistrationException {
        if (!isEmailValid(email)) {
            return RegistrationStatus.INVALID_EMAIL;
        }

        if (isEmailInUse(email)) {
            return RegistrationStatus.EMAIL_IN_USE;
        }

        return RegistrationStatus.SUCCESS;
    }

    private boolean isEmailInUse(String email) throws RegistrationException {
        try {
            return userCredentialsRepository.findEmail(email) != null;
        }
        catch (Exception e) {
            throw new RegistrationException("Problem occurred while getting email from database. Message: " + e.getMessage());
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z]+(?: [A-Za-z]+)*$");
        Matcher matcher = pattern.matcher(firstName);

        return matcher.matches();
    }

    public RegistrationStatus verifyAccount(String token) throws RegistrationException {
        RegistrationToken registrationToken;

        try {
            registrationToken = registrationTokenRepository.findRegistrationTokenByToken(token);
        }
        catch (Exception e) {
            throw new RegistrationException("Could not find registration token in database. Message: " + e.getMessage());
        }

        if (registrationToken != null) {
            try {
                userCredentialsRepository.setIsActiveToUserByUserId(registrationToken.getUserId());
            }
            catch (Exception e) {
                throw new RegistrationException("Could not set user active. Message: " + e.getMessage());
            }

            try {
                registrationTokenRepository.delete(registrationToken);
            }
            catch (Exception e) {
                throw new RegistrationException("Could not delete registration token from database. Message: " + e.getMessage());
            }

            return RegistrationStatus.SUCCESS;
        }

        return RegistrationStatus.INVALID_REGISTRATION_TOKEN;
    }
}
