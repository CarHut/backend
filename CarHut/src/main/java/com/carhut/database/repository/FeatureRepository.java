package com.carhut.database.repository;

import com.carhut.models.carhut.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, String> {

    @Query(value = "SELECT * FROM feature ORDER BY feature ASC;", nativeQuery = true)
    List<Feature> getFeaturesAsc();

}
