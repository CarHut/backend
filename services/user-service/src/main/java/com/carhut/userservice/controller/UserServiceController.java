package com.carhut.userservice.controller;

import com.carhut.userservice.repository.model.User;
import com.carhut.userservice.service.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(value = "/user-service")
public class UserServiceController {

    @Autowired
    private UserServiceProvider userServiceProvider;

    @GetMapping(value = "/security-get-user-credentials")
    public ResponseEntity<User> getUserCredentialsForSecurityService(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "user-id", required = false) String userId)
            throws ExecutionException, InterruptedException {
        User user = userServiceProvider.getUserCredentialsForSecurityService(username, userId).get();
        return user == null ? ResponseEntity.status(404).body(null) : ResponseEntity.ok(user);
    }

    @GetMapping(value = "/update-car-count-for-user-id")
    public ResponseEntity<String> updateCarCountForUserId(@RequestHeader("Authorization") String bearerToken,
                                                          @RequestParam("user-id") String userId)
            throws IOException, InterruptedException, ExecutionException {
        Boolean updated = userServiceProvider.updateCarCountForUserId(userId, bearerToken).get();
        return updated
                ? ResponseEntity.ok("Successfully updated offers count.")
                : ResponseEntity.internalServerError().body("Couldn't update offers count for user with id: " + userId + ". Please try again later.");
    }

    @GetMapping(value = "/decrement-offer-count-by-user-id")
    public ResponseEntity<String> decrementOfferCountByUserId(@RequestHeader("Authorization") String bearerToken,
                                                              @RequestParam("user-id") String userId)
            throws IOException, InterruptedException, ExecutionException {
        Boolean updated = userServiceProvider.decrementOfferCountByUserId(userId, bearerToken).get();
        return updated
                ? ResponseEntity.ok("Successfully updated offers count.")
                : ResponseEntity.internalServerError().body("Couldn't update offers count for user with id: " + userId + ". Please try again later.");
    }

}
