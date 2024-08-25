package com.carhut.controllers.carhutapi;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.RemoveSavedSearchRequestModel;
import com.carhut.requests.requestmodels.SaveSearchRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.services.carhutapi.SavedSearchesService;
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
        ServiceStatusEntity status = savedSearchesService.addNewSavedSearch(savedSearch);

        if (status == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[SavedSearchesController][OK]: /addNewSavedSearch - Successfully saved search to database.");
            return ResponseEntity.ok().body("Successfully saved search.");
        } else {
            controllerLogger.saveToFile("[SavedSearchesController][WARN]: /addNewSavedSearch - Something went wrong while trying to save search.");
            return ResponseEntity.internalServerError().body("Something went wrong while trying to save search.");
        }
    }

    @PostMapping("/getSavedSearchesByUsername")
    @ResponseBody
    public ResponseEntity<List<SavedSearch>> getSavedSearchesByUsername(@RequestParam PrincipalRequest<SimpleUsernameRequestModel> username) {
        List<SavedSearch> searches = savedSearchesService.getSavedSearchesByUsername(username);

        if (searches != null) {
            controllerLogger.saveToFile("[SavedSearchesController][OK]: /getSavedSearchesByUsername - Successfully retrieved searches.");
            return ResponseEntity.ok().body(searches);
        } else {
            controllerLogger.saveToFile("[SavedSearchesController][WARN]: /getSavedSearchesByUsername - Something went wrong while trying to retrieve searches.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/removeSavedSearch")
    @ResponseBody
    public ResponseEntity<String> removeSavedSearch(@RequestParam PrincipalRequest<RemoveSavedSearchRequestModel> removeSavedSearchRequestModelPrincipalRequest) {
            ServiceStatusEntity status = savedSearchesService.removeSavedSearch(removeSavedSearchRequestModelPrincipalRequest);
            if (status == ServiceStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[SavedSearchesController][OK]: /removeSavedSearch - Successfully removed saved search.");
                return ResponseEntity.ok().body("Successfully removed saved search.");
            } else {
                controllerLogger.saveToFile("[SavedSearchesController][ERROR]: /removeSavedSearch - could not remove saved search.");
                return ResponseEntity.internalServerError().body(null);
            }

    }

}
