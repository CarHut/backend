package com.carhut.services.security;

import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.requests.PrincipalRequest;
import com.carhut.security.models.User;
import com.carhut.requests.requestmodels.UserDetailsRequestBody;
import com.carhut.security.annotations.UserAccessCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService implements UserDetailsService {

    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userCredentialsRepository.findUserByUsername(username);
        }
        catch (Exception e) {
            return null;
        }
    }

    @UserAccessCheck
    public User getUserDetailsInfo(PrincipalRequest<UserDetailsRequestBody> userDetailsRequestBody) {
        try {
            return userCredentialsRepository.findUserByUsername(userDetailsRequestBody.getDto().getUsername());
        }
        catch (Exception e) {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try {
            return userCredentialsRepository.findUserByEmail(email);
        }
        catch (Exception e) {
            return null;
        }
    }

    public User findUserById(String id) {
        return userCredentialsRepository.findUserById(id);
    }

}
