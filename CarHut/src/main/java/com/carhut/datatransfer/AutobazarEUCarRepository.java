package com.carhut.datatransfer;

import com.carhut.models.AutobazarEUCarObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutobazarEUCarRepository extends JpaRepository<AutobazarEUCarObject, String> {

    @Query(value = "SELECT * FROM autobazar_eu", nativeQuery = true)
    List<AutobazarEUCarObject> getAllCars();



}