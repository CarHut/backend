package com.carhut.repository;

import com.carhut.models.carhut.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, String> {

    @Query(value = """
                    SELECT m.* FROM Model m
                    WHERE m.brand_id = (SELECT b.id FROM Brand b
                                        WHERE b.brand = :brandId)
                   """, nativeQuery = true)
    List<Model> getModelByBrandId(@Param("brandId") String brandId);

    @Query(value = """
                    SELECT m.* FROM Model m
                    WHERE m.brand_id = (SELECT b.id FROM Brand b
                                        WHERE LOWER(b.brand) = LOWER(:brandName))
                   """, nativeQuery = true)
    List<Model> getModelByBrandName(@Param("brandName") String brandName);

    @Query(value = """
                    SELECT m.id FROM Model m
                    WHERE m.model = :modelName and m.brand_id = :brandId
                    """, nativeQuery = true)
    Integer getModelIdByModelName(@Param("modelName") String modelName, @Param("brandId") int brandId);
}
