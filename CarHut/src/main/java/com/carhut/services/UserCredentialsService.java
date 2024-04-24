package com.carhut.services;

import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.models.security.User;
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
        return userCredentialsRepository.findUserByUsername(username);
    }

    public User getUserDetailsInfo(String username) {
        return userCredentialsRepository.findUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userCredentialsRepository.findUserByEmail(email);
    }

}
