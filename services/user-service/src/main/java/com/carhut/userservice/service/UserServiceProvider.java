package com.carhut.userservice.service;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.userservice.repository.UserRepository;
import com.carhut.userservice.repository.model.User;
import com.carhut.userservice.repository.resourceprovider.UserDatabaseResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class UserServiceProvider {

    @Autowired
    private UserRepository userRepository;
    private static UserDatabaseResourceManager userDatabaseResourceManager =
            UserDatabaseResourceManager.getInstance();
    private RequestAuthenticationCaller caller = new RequestAuthenticationCaller();

    public CompletableFuture<User> getUserCredentialsForSecurityService(String username, String userId) {
        if (userId == null && username == null) {
            CompletableFuture<User> cf = new CompletableFuture<>();
            cf.complete(null);
            return cf;
        }

        Function<Void, User> function;
        if (username != null) {
            function = (unused) -> userRepository.getUserByUsername(username);
        } else {
            function = (unused) -> userRepository.getUserByUserId(userId);
        }

        return userDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<Boolean> updateCarCountForUserId(String userId, String bearerToken)
            throws IOException, InterruptedException {
        CompletableFuture<Boolean> cf = new CompletableFuture<>();
        if (userId == null || bearerToken == null) {
            cf.complete(false);
            return cf;
        }

        if (!isRequestAuthenticated(userId, bearerToken)) {
            cf.complete(false);
            return cf;
        }

        Function<Void, Boolean> function = (unused) -> {
            try {
                userRepository.updateCarCountByUserId(userId);
                return true;
            } catch (Exception e) {
                return false;
            }
        };

        return userDatabaseResourceManager.acquireDatabaseResource(function);
    }

    private boolean isRequestAuthenticated(String userId, String bearerToken) throws IOException, InterruptedException {
        return caller.isRequestAuthenticated(userId, bearerToken);
    }
}
