package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveSearchRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.services.SavedSearchesService;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.savedsearches.SavedSearchesException;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/carhut/savedSearches")
public class SavedSearchesController {

    @Autowired
    private SavedSearchesService savedSearchesService;
    private static final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/addNewSavedSearch")
    @ResponseBody
    public ResponseEntity<String> addNewSavedSearch(@RequestBody PrincipalRequest<SaveSearchRequestModel> savedSearch) {
        try {
            RequestStatusEntity status = savedSearchesService.addNewSavedSearch(savedSearch);

            if (status == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[SavedSearchesController][OK]: /addNewSavedSearch - Successfully saved search to database.");
                return ResponseEntity.ok().body("Successfully saved search.");
            } else {
                controllerLogger.saveToFile("[SavedSearchesController][WARN]: /addNewSavedSearch - Something went wrong while trying to save search.");
                return ResponseEntity.internalServerError().body("Something went wrong while trying to save search.");
            }
        }
        catch (SavedSearchesException e) {
            controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /addNewSavedSearch - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        } catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /addNewSavedSearch - Unauthorized access. Message: " + e.getMessage());
            return ResponseEntity.status(403).body(null);
        }
    }

    @PostMapping("/getSavedSearchesByUsername")
    @ResponseBody
    public ResponseEntity<List<SavedSearch>> getSavedSearchesByUsername(@RequestParam PrincipalRequest<SimpleUsernameRequestModel> username) {
        try {
            List<SavedSearch> searches = savedSearchesService.getSavedSearchesByUsername(username);

            if (searches != null) {
                controllerLogger.saveToFile("[SavedSearchesController][OK]: /getSavedSearchesByUsername - Successfully retrieved searches.");
                return ResponseEntity.ok().body(searches);
            } else {
                controllerLogger.saveToFile("[SavedSearchesController][WARN]: /getSavedSearchesByUsername - Something went wrong while trying to retrieve searches.");
                return ResponseEntity.internalServerError().body(null);
            }
        }
        catch (SavedSearchesException e) {
            controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /getSavedSearchesByUsername - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        } catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /getSavedSearchesByUsername - Unauthorized access. Message: " + e.getMessage());
            return ResponseEntity.status(403).body(null);
        }
    }

    @GetMapping("/removeSavedSearch")
    @ResponseBody
    public ResponseEntity<String> removeSavedSearch(@RequestParam PrincipalRequest<RemoveSavedSearchRequestModel> removeSavedSearchRequestModelPrincipalRequest) {
        try {
            savedSearchesService.removeSavedSearch(removeSavedSearchRequestModelPrincipalRequest);
            controllerLogger.saveToFile("[SavedSearchesController][OK]: /removeSavedSearch - Successfully removed saved search.");
            return ResponseEntity.ok().body("Successfully removed saved search.");
        }
        catch (SavedSearchesException e) {
            controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /removeSavedSearch - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
