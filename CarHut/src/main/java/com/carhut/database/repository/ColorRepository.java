package com.carhut.database.repository;

import com.carhut.models.carhut.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, String> {

    @Query(value = "SELECT * FROM color", nativeQuery = true)
    List<Color> getColors();

}
