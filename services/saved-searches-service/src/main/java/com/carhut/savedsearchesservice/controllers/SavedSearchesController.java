package com.carhut.savedsearchesservice.controllers;

import com.carhut.savedsearchesservice.models.SavedSearch;
import com.carhut.savedsearchesservice.requests.RemoveSavedSearchRequestModel;
import com.carhut.savedsearchesservice.services.SavedSearchesService;
import com.carhut.savedsearchesservice.status.SavedSearchesServiceStatus;
import com.carhut.savedsearchesservice.util.loggers.SavedSearchesServiceLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/saved-searches-service")
public class SavedSearchesController {

    @Autowired
    private SavedSearchesService savedSearchesService;
    private static final SavedSearchesServiceLogger logger = SavedSearchesServiceLogger.getInstance();

    @PostMapping("/add-new-saved-search")
    @ResponseBody
    public ResponseEntity<String> addNewSavedSearch(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody SavedSearch savedSearch) throws ExecutionException, InterruptedException, IOException {
        SavedSearchesServiceStatus status = savedSearchesService.addNewSavedSearch(savedSearch, bearerToken).get();

        if (status == SavedSearchesServiceStatus.SUCCESS) {
            logger.logInfo("[SavedSearchesController][OK]: /addNewSavedSearch - Successfully saved search to database.");
            return ResponseEntity.status(201).body("Successfully saved search.");
        } else if (status == SavedSearchesServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            logger.logWarn("[SavedSearchesController][WARN]: /addNewSavedSearch - User is not authenticated. " + savedSearch.toString());
            return ResponseEntity.status(403).body("User is not authenticated.");
        } else {
            logger.logWarn("[SavedSearchesController][WARN]: /addNewSavedSearch - Something went wrong while trying to save search.");
            return ResponseEntity.internalServerError().body("Something went wrong while trying to save search.");
        }
    }

    @GetMapping("/get-saved-searches-by-user-id")
    @ResponseBody
    public ResponseEntity<List<SavedSearch>> getSavedSearchesByUserId(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam("user-id") String userId) throws ExecutionException, InterruptedException, IOException {
        List<SavedSearch> searches = savedSearchesService.getSavedSearchesByUsername(userId, bearerToken).get();

        if (searches != null) {
            logger.logInfo("[SavedSearchesController][OK]: /getSavedSearchesByUsername - Successfully retrieved searches.");
            return ResponseEntity.ok().body(searches);
        } else {
            logger.logWarn("[SavedSearchesController][WARN]: /getSavedSearchesByUsername - Something went wrong while trying to retrieve searches.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/remove-saved-search")
    @ResponseBody
    public ResponseEntity<String> removeSavedSearch(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody RemoveSavedSearchRequestModel removeSavedSearchRequestModel)
            throws ExecutionException, InterruptedException, IOException {

        SavedSearchesServiceStatus status = savedSearchesService
                .removeSavedSearch(removeSavedSearchRequestModel, bearerToken).get();
        if (status == SavedSearchesServiceStatus.SUCCESS) {
            logger.logInfo("[SavedSearchesController][OK]: /removeSavedSearch - Successfully removed saved search.");
            return ResponseEntity.ok().body("Successfully removed saved search.");
        } else if (status == SavedSearchesServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            logger.logError("[SavedSearchesController][ERROR]: /removeSavedSearch - User is not authenticated.");
            return ResponseEntity.status(403).body("User not authenticated.");
        } else {
            logger.logError("[SavedSearchesController][ERROR]: /removeSavedSearch - could not remove saved search.");
            return ResponseEntity.internalServerError().body("Internal error. Please try again later.");
        }
    }

}
