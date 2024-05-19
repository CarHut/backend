package com.carhut.controllers;

import com.carhut.enums.RegistrationStatus;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.security.RegisterUserBody;
import com.carhut.services.RegistrationService;
import com.carhut.util.exceptions.registration.RegistrationException;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/registerInitiate")
    public ResponseEntity<String> registerInitiate(@RequestBody RegisterUserBody registerUserBody) {
        try {
            RegistrationStatus registrationStatus = registrationService.registerInitiate(registerUserBody);
            if (registrationStatus == RegistrationStatus.SUCCESS) {
                String registrationStatusString = registrationStatus.toString();
                return ResponseEntity.ok().body(registrationStatusString);
            } else {
                String registrationStatusString = registrationStatus.toString();
                return ResponseEntity.internalServerError().body(registrationStatusString);
            }
        }
        catch (RegistrationException err) {
            controllerLogger.saveToFile("[RegistrationController][ERROR]: /registerInitiate - Internal error while initiating registration. Message: " + err.getMessage());
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

}
