package com.carhut.services;

import com.carhut.models.security.User;
import com.carhut.services.UserCredentialsService;
import com.carhut.util.exceptions.usercredentials.UserCredentialsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

@SpringBootTest
public class UserCredentialsServiceTests {

    @Autowired
    private UserCredentialsService userCredentialsService;

    @Test
    public void testLoadUserByUsername_adminUser() {
        UserDetails userDetails = userCredentialsService.loadUserByUsername("admin");

        Assertions.assertNotNull(userDetails, "user details are null");
        Assertions.assertEquals("admin", userDetails.getUsername());
        Assertions.assertNotNull(userDetails.getPassword(), "user password is null");
    }

    @Test
    public void testLoadUserByUsername_nonExistantUser() {
        UserDetails userDetails = userCredentialsService.loadUserByUsername(UUID.randomUUID().toString());

        Assertions.assertNull(userDetails, "user details are null");
    }

    @Test
    public void testGetUserDetailsByUsername_adminUser() {
        com.carhut.models.security.User user = null;
        try {
            user = userCredentialsService.getUserDetailsInfo("admin");
        }
        catch (UserCredentialsException e) {
            System.out.println("Internal error.");
            return;
        }

        Assertions.assertNotNull(user, "user details are null");
        Assertions.assertEquals("admin", user.getUsername());
        Assertions.assertNotNull(user.getPassword(), "user password is null");
    }

    @Test
    public void testGetUserDetailsByUsername_nonExistantUser() {
        User user = null;
        try {
            user = userCredentialsService.getUserDetailsInfo(UUID.randomUUID().toString());
        }
        catch (UserCredentialsException e) {
            System.out.println("Internal error.");
            return;
        }

        Assertions.assertNull(user, "user details are null");
    }

    @Test
    public void testGetUserByEmail_adminUser() {
        com.carhut.models.security.User user = null;
        try {
            user = userCredentialsService.getUserByEmail("admin@admin.com");
        }
        catch (UserCredentialsException e) {
            System.out.println("Internal error.");
            return;
        }


        Assertions.assertNotNull(user, "user details are null");
        Assertions.assertEquals("admin", user.getUsername());
        Assertions.assertNotNull(user.getPassword(), "user password is null");
    }

    @Test
    public void testGetUserByEmail_nonExistantUser() {
        com.carhut.models.security.User user = null;
        try {
            user = userCredentialsService.getUserByEmail(UUID.randomUUID().toString());
        }
        catch (UserCredentialsException e) {
            System.out.println("Internal error.");
            return;
        }

        Assertions.assertNull(user, "user details are null");
    }

}
