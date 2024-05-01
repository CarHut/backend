package com.carhut.services;

import com.carhut.models.security.User;
import com.carhut.services.UserCredentialsService;
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
        com.carhut.models.security.User user = userCredentialsService.getUserDetailsInfo("admin");

        Assertions.assertNotNull(user, "user details are null");
        Assertions.assertEquals("admin", user.getUsername());
        Assertions.assertNotNull(user.getPassword(), "user password is null");
    }

    @Test
    public void testGetUserDetailsByUsername_nonExistantUser() {
        User user = userCredentialsService.getUserDetailsInfo(UUID.randomUUID().toString());

        Assertions.assertNull(user, "user details are null");
    }

    @Test
    public void testGetUserByEmail_adminUser() {
        com.carhut.models.security.User user = userCredentialsService.getUserByEmail("admin@admin.com");

        Assertions.assertNotNull(user, "user details are null");
        Assertions.assertEquals("admin", user.getUsername());
        Assertions.assertNotNull(user.getPassword(), "user password is null");
    }

    @Test
    public void testGetUserByEmail_nonExistantUser() {
        User user = userCredentialsService.getUserByEmail(UUID.randomUUID().toString());

        Assertions.assertNull(user, "user details are null");
    }

}
