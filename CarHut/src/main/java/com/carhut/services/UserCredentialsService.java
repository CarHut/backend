package com.carhut.services;

import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.models.security.User;
import com.carhut.models.requestmodels.UserDetailsRequestBody;
import com.carhut.util.exceptions.usercredentials.UserCredentialsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService implements UserDetailsService {

    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userCredentialsRepository.findUserByUsername(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Username not found in user credentials database.");
        }
    }

    public User getUserDetailsInfo(UserDetailsRequestBody userDetailsRequestBody) throws UserCredentialsNotFoundException {
        try {
            return userCredentialsRepository.findUserByUsername(userDetailsRequestBody.getUsername());
        }
        catch (Exception e) {
            throw new UserCredentialsNotFoundException("Internal error while getting user from database. Message: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) throws UserCredentialsNotFoundException {
        try {
            return userCredentialsRepository.findUserByEmail(email);
        }
        catch (Exception e) {
            throw new UserCredentialsNotFoundException("Internal error while getting user from database. Message: " + e.getMessage());
        }
    }

}
