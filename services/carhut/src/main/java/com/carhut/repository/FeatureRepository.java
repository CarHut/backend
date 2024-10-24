package com.carhut.repository;

import com.carhut.model.carhut.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, String> {

    @Query(value = """
                    SELECT * FROM feature
                    ORDER BY feature ASC;
                    """, nativeQuery = true)
    List<Feature> getFeaturesAsc();

    @Query(value = """
                    SELECT f.id FROM feature f
                    WHERE f.feature = :feature
                    """, nativeQuery = true)
    Integer getFeatureIdByFeatureName(@Param("feature") String feature);

    @Query(value = """
                    SELECT f.feature FROM feature f
                    WHERE f.id = :featureId
                    """, nativeQuery = true)
    String getFeatureNameByFeatureId(@Param("featureId") Integer featureId);

}
