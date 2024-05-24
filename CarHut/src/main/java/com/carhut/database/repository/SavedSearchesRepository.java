package com.carhut.database.repository;

import com.carhut.models.carhut.SavedSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedSearchesRepository extends JpaRepository<SavedSearch, String> {

    @Query(value = "SELECT * FROM saved_searches WHERE user_id = :userId", nativeQuery = true)
    List<SavedSearch> getSavedSearchesByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM saved_searches WHERE id = :id", nativeQuery = true)
    SavedSearch getSavedSearchesBySavedSearchId(@Param("id") String id);
}
