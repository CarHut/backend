package com.carhut.userservice.controller;

import com.carhut.userservice.repository.model.User;
import com.carhut.userservice.service.UserServiceProvider;
import models.responses.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(value = "/user-service")
public class UserServiceController {

    @Autowired
    private UserServiceProvider userServiceProvider;

    @GetMapping(value = "/security-get-user-credentials")
    public GenericResponse<User> getUserCredentialsForSecurityService(@RequestParam("username") String username)
            throws ExecutionException, InterruptedException {
        return userServiceProvider.getUserCredentialsForSecurityService(username).get();
    }

}
