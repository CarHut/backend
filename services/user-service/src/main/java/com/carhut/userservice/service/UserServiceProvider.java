package com.carhut.userservice.service;

import com.carhut.userservice.repository.UserRepository;
import com.carhut.userservice.repository.model.User;
import com.carhut.userservice.repository.resourceprovider.DatabaseResourceProviderManager;
import models.responses.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class UserServiceProvider implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private static DatabaseResourceProviderManager databaseResourceProviderManager =
            DatabaseResourceProviderManager.getInstance();

    public CompletableFuture<User> getUserCredentialsForSecurityService(String username) {
        Function<Void, User> fun = (unused) -> userRepository.getUserByUsername(username);
        return databaseResourceProviderManager.acquireDatabaseResource(fun);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }
}
