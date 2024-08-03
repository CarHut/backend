package com.carhut.services;

import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.requests.PrincipalRequest;
import com.carhut.security.models.User;
import com.carhut.requests.requestmodels.UserDetailsRequestBody;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
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

    @UserAccessCheck
    public User getUserDetailsInfo(PrincipalRequest<UserDetailsRequestBody> userDetailsRequestBody) throws UserCredentialsNotFoundException, CarHutAuthenticationException {
        try {
            return userCredentialsRepository.findUserByUsername(userDetailsRequestBody.getDto().getUsername());
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
