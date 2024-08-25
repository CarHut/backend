package com.carhut.services.carhutapi;

import com.carhut.requests.requestmodels.RemoveSavedSearchRequestModel;
import com.carhut.database.repository.carhutapi.SavedSearchesRepository;
import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveSearchRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.User;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.services.security.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedSearchesService {

    @Autowired
    private SavedSearchesRepository savedSearchesRepository;
    @Autowired
    private UserCredentialsService userCredentialsService;

    @UserAccessCheck
    public ServiceStatusEntity addNewSavedSearch(PrincipalRequest<SaveSearchRequestModel> saveSearchRequestModelPrincipalRequest) {
        if (saveSearchRequestModelPrincipalRequest == null) {
            return ServiceStatusEntity.ERROR;
        }

        if (validateSavedSearchModel(saveSearchRequestModelPrincipalRequest.getDto()) == ServiceStatusEntity.OBJECT_NOT_FOUND) {
            return ServiceStatusEntity.ERROR;
        }

        SaveSearchRequestModel savedSearch = saveSearchRequestModelPrincipalRequest.getDto();
        try {
            SavedSearch newSavedSearch = new SavedSearch(savedSearch.getUserId(), savedSearch.getSortBy(), savedSearch.getOffersPerPage(),
                    savedSearch.getPriceFrom(), savedSearch.getPriceTo(), savedSearch.getMileageFrom(),
                    savedSearch.getMileageTo(), savedSearch.getFuelType(), savedSearch.getGearboxType(), savedSearch.getPowertrainType(),
                    savedSearch.getPowerFrom(), savedSearch.getPowerTo(), savedSearch.getBrandsAndModels());
            savedSearchesRepository.save(newSavedSearch);
            return ServiceStatusEntity.SUCCESS;
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
        }
    }

    private ServiceStatusEntity validateSavedSearchModel(SaveSearchRequestModel dto) {
        try {
            System.out.println(userCredentialsService);
            UserDetails user = userCredentialsService.loadUserByUsername(dto.getUsername());
            if (user == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        } catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        try {
            User user = userCredentialsService.findUserById(dto.getUserId());

            if (user == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }
        } catch (Exception e) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND_EXCEPTIONALLY;
        }

        return ServiceStatusEntity.SUCCESS;
    }

    @UserAccessCheck
    public List<SavedSearch> getSavedSearchesByUsername(PrincipalRequest<SimpleUsernameRequestModel> username) {
        if (username == null) {
            return null;
        }

        if (!doesUserExist(username.getDto().getUsername())) {
            return null;
        }

        try {
            User user = (User) userCredentialsService.loadUserByUsername(username.getDto().getUsername());
            return savedSearchesRepository.getSavedSearchesByUserId(user.getId());
        }
        catch (Exception e) {
            return null;
        }

    }

    @UserAccessCheck
    public ServiceStatusEntity removeSavedSearch(PrincipalRequest<RemoveSavedSearchRequestModel> removeSavedSearchRequestModelPrincipalRequest) {

        if (removeSavedSearchRequestModelPrincipalRequest == null) {
            System.out.println("Request is null. Cannot remove saved search from database.");
            return ServiceStatusEntity.ERROR;
        }

        if (!doesUserExist(removeSavedSearchRequestModelPrincipalRequest.getDto().getUsername())) {
            return ServiceStatusEntity.OBJECT_NOT_FOUND;
        }

        try {
            SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(removeSavedSearchRequestModelPrincipalRequest.getDto().getSearchId());

            if (savedSearch == null) {
                return ServiceStatusEntity.OBJECT_NOT_FOUND;
            }

            savedSearchesRepository.delete(savedSearch);
        }
        catch (Exception e) {
            return ServiceStatusEntity.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY;
        }
        return ServiceStatusEntity.SUCCESS;
    }

    private boolean doesUserExist(String username) {
        try {
            User user = (User) userCredentialsService.loadUserByUsername(username);

            if (user == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void flush() {
        savedSearchesRepository.deleteAll();
    }

}
