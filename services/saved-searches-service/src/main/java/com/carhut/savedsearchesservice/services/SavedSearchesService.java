package com.carhut.savedsearchesservice.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import com.carhut.savedsearchesservice.models.SavedSearch;
import com.carhut.savedsearchesservice.repository.SavedSearchesRepository;
import com.carhut.savedsearchesservice.repository.resourceprovider.SavedSearchesResourceManager;
import com.carhut.savedsearchesservice.requests.RemoveSavedSearchRequestModel;
import com.carhut.savedsearchesservice.status.SavedSearchesServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
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
                                                                           String bearerToken) {
        CompletableFuture<SavedSearchesServiceStatus> cf = new CompletableFuture<>();
        if (savedSearch == null) {
            cf.complete(SavedSearchesServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        CompletableFuture<HttpResponse<String>> authRequest =
                isRequestAuthenticatedAsync(savedSearch.getUserId(), bearerToken, cf);
        CompletableFuture<SavedSearchesServiceStatus> serviceRequest = authRequest.thenCompose(authResponse -> {
            CompletableFuture<SavedSearchesServiceStatus> failedCf = new CompletableFuture<>();
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
                failedCf.completeExceptionally(new RuntimeException("Couldn't save search to database."));
                return failedCf;
            }
        });

        serviceRequest.whenComplete((result, ex) -> {
            if (ex != null) {
                cf.complete(SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY);
            } else {
                cf.complete(SavedSearchesServiceStatus.SUCCESS);
            }
        });

        return cf;
    }

    public CompletableFuture<List<SavedSearch>> getSavedSearchesByUsername(String userId, String bearerToken) {
        CompletableFuture<List<SavedSearch>> savedSearchesCf = new CompletableFuture<>();
        if (userId == null) {
            savedSearchesCf.complete(null);
            return savedSearchesCf;
        }

        CompletableFuture<HttpResponse<String>> authRequest =
                isRequestAuthenticatedAsync(userId, bearerToken, savedSearchesCf);
        CompletableFuture<List<SavedSearch>> serviceRequest = authRequest.thenCompose(authResponse -> {
            try {
               Function<Void, List<SavedSearch>> function = (
                       unused -> savedSearchesRepository.getSavedSearchesByUserId(userId));

               return savedSearchesResourceManager.acquireDatabaseResource(function);
            } catch (Exception e) {
                CompletableFuture<List<SavedSearch>> failedCf = new CompletableFuture<>();
                failedCf.completeExceptionally(null);
                return failedCf;
            }
        });

        serviceRequest.whenComplete((result, ex) -> {
            if (ex != null || result == null) {
                savedSearchesCf.complete(null);
            } else {
                savedSearchesCf.complete(result);
            }
        });
        return savedSearchesCf;
    }

    public CompletableFuture<SavedSearchesServiceStatus> removeSavedSearch(RemoveSavedSearchRequestModel model,
                                                                           String bearerToken) {
        CompletableFuture<SavedSearchesServiceStatus> statusCf = new CompletableFuture<>();
        if (model == null || bearerToken == null) {
            System.out.println("Request is null. Cannot remove saved search from database.");
            statusCf.complete(SavedSearchesServiceStatus.ERROR);
            return statusCf;
        }

        CompletableFuture<HttpResponse<String>> authRequest =
                isRequestAuthenticatedAsync(model.getUserId(), bearerToken, statusCf);
        CompletableFuture<SavedSearchesServiceStatus> serviceRequest = authRequest.thenCompose(authResponse -> {
            CompletableFuture<SavedSearchesServiceStatus> failedCf = new CompletableFuture<>();
            try {
                SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(model.getSearchId());
                if (savedSearch == null) {
                    failedCf.completeExceptionally(new RuntimeException("Object not found."));
                    return failedCf;
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
                failedCf.completeExceptionally(new RuntimeException("Cannot remove object from database."));
                return failedCf;
            }
        });

        serviceRequest.whenComplete((result, ex) -> {
            if (ex != null || result == null) {
                statusCf.complete(null);
            } else {
                statusCf.complete(result);
            }
        });
        return statusCf;
    }

    private <T> CompletableFuture<HttpResponse<String>> isRequestAuthenticatedAsync(String userId, String bearerToken,
                                                                                CompletableFuture<T> finalizable) {
        CompletableFuture<HttpResponse<String>> response = requestAuthenticationCaller
                .isRequestAuthenticatedAsync(userId, bearerToken);

        response.whenComplete((result, ex) -> {
            if (ex != null) {
                finalizable.complete(null);
            } else if (result.statusCode() < 200 || result.statusCode() > 299){
                finalizable.complete(null);
            }
        });

        return response;
    }

}
