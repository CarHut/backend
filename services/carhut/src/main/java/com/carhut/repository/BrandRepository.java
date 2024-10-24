package com.carhut.repository;

import com.carhut.model.carhut.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, String> {

    @Query(value = "SELECT * FROM brand", nativeQuery = true)
    List<Brand> getAllBrands();

    @Query(value = """
                    SELECT b.id FROM brand b
                    WHERE b.brand = :brandName
                    """, nativeQuery = true)
    Integer getBrandIdFromBrandName(@Param("brandName") String brandName);
}
