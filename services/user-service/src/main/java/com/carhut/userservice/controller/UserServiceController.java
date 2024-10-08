package com.carhut.userservice.controller;

import com.carhut.userservice.repository.model.User;
import com.carhut.userservice.service.UserServiceProvider;
import models.responses.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(value = "/user-service")
public class UserServiceController {

    @Autowired
    private UserServiceProvider userServiceProvider;

    @GetMapping(value = "/security-get-user-credentials")
    public ResponseEntity<User> getUserCredentialsForSecurityService(@RequestParam("username") String username)
            throws ExecutionException, InterruptedException {
        User user = userServiceProvider.getUserCredentialsForSecurityService(username).get();
        return user == null ? ResponseEntity.status(404).body(null) : ResponseEntity.ok(user);
    }

}
