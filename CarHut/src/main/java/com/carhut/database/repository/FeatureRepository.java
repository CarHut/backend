package com.carhut.database.repository;

import com.carhut.models.carhut.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, String> {

    @Query(value = "SELECT * FROM feature ORDER BY feature ASC;", nativeQuery = true)
    List<Feature> getFeaturesAsc();

    @Query(value = "SELECT f.id FROM feature f WHERE f.feature = :feature", nativeQuery = true)
    int getFeatureIdByFeatureName(@Param("feature") String feature);
}
