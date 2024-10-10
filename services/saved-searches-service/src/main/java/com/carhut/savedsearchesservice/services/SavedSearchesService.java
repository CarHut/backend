package com.carhut.savedsearchesservice.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.savedsearchesservice.models.SavedSearch;
import com.carhut.savedsearchesservice.repository.SavedSearchesRepository;
import com.carhut.savedsearchesservice.repository.resourceprovider.SavedSearchesResourceManager;
import com.carhut.savedsearchesservice.requests.RemoveSavedSearchRequestModel;
import com.carhut.savedsearchesservice.status.SavedSearchesServiceStatus;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class SavedSearchesService {

    @Autowired
    private SavedSearchesRepository savedSearchesRepository;
    private static SavedSearchesResourceManager savedSearchesResourceManager =
            SavedSearchesResourceManager.getInstance();
    private final RequestAuthenticationCaller requestAuthenticationCaller = new RequestAuthenticationCaller();

    public CompletableFuture<SavedSearchesServiceStatus> addNewSavedSearch(SavedSearch savedSearch,
                                                                           String bearerToken)
            throws IOException, InterruptedException {


        CompletableFuture<SavedSearchesServiceStatus> cf = new CompletableFuture<>();
        if (savedSearch == null) {
            cf.complete(SavedSearchesServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if(!isUserAuthenticated(savedSearch.getUserId(), bearerToken)) {
            cf.complete(SavedSearchesServiceStatus.USER_IS_NOT_AUTHENTICATED);
            return cf;
        }


        try {
            Function<Void, SavedSearchesServiceStatus> function = (unused) -> {
                SavedSearch newSavedSearch = new SavedSearch(savedSearch.getUserId(), savedSearch.getSortBy(), savedSearch.getOffersPerPage(),
                        savedSearch.getPriceFrom(), savedSearch.getPriceTo(), savedSearch.getMileageFrom(),
                        savedSearch.getMileageTo(), savedSearch.getFuelType(), savedSearch.getGearboxType(), savedSearch.getPowertrainType(),
                        savedSearch.getPowerFrom(), savedSearch.getPowerTo(), savedSearch.getBrandsAndModels());
                savedSearchesRepository.save(newSavedSearch);
                return SavedSearchesServiceStatus.SUCCESS;
            };
            return savedSearchesResourceManager.acquireDatabaseResource(function);
        }
        catch (Exception e) {
            cf.complete(SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY);
            return cf;
        }
    }

    public CompletableFuture<List<SavedSearch>> getSavedSearchesByUsername(String userId, String bearerToken)
            throws IOException, InterruptedException {
        CompletableFuture<List<SavedSearch>> savedSearchesCf = new CompletableFuture<>();
        if (userId == null) {
            savedSearchesCf.complete(null);
            return savedSearchesCf;
        }

        if (!isUserAuthenticated(userId, bearerToken)) {
            savedSearchesCf.complete(null);
            return savedSearchesCf;
        }

        Function<Void, List<SavedSearch>> function = (
                unused -> savedSearchesRepository.getSavedSearchesByUserId(userId));

        return savedSearchesResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<SavedSearchesServiceStatus> removeSavedSearch(RemoveSavedSearchRequestModel model,
                                                                           String bearerToken)
            throws IOException, InterruptedException {
        CompletableFuture<SavedSearchesServiceStatus> statusCf = new CompletableFuture<>();
        if (model == null || bearerToken == null) {
            System.out.println("Request is null. Cannot remove saved search from database.");
            statusCf.complete(SavedSearchesServiceStatus.ERROR);
            return statusCf;
        }

        if (!isUserAuthenticated(model.getUserId(), bearerToken)) {
            statusCf.complete(SavedSearchesServiceStatus.USER_IS_NOT_AUTHENTICATED);
            return statusCf;
        }

        try {
            SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(model.getSearchId());
            if (savedSearch == null) {
                statusCf.complete(SavedSearchesServiceStatus.OBJECT_NOT_FOUND);
                return statusCf;
            }

            Function<Void, SavedSearchesServiceStatus> function = (unused) -> {
                try {
                    savedSearchesRepository.delete(savedSearch);
                } catch (Exception e) {
                    return SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY;
                }
                return SavedSearchesServiceStatus.SUCCESS;
            };
            return savedSearchesResourceManager.acquireDatabaseResource(function);
        }
        catch (Exception e) {
            statusCf.complete(SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY);
            return statusCf;
        }

    }

    private Boolean isUserAuthenticated(String userId, String bearerToken) throws IOException, InterruptedException {
        return requestAuthenticationCaller.isRequestAuthenticated(userId, bearerToken);
    }

    @VisibleForTesting
    public void flush() {
        savedSearchesRepository.deleteAll();
    }

}
