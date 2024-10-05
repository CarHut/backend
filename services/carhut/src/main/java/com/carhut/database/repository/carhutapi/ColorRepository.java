package com.carhut.database.repository.carhutapi;

import com.carhut.models.carhut.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, String> {

    @Query(value = "SELECT * FROM color", nativeQuery = true)
    List<Color> getColors();

    @Query(value = "SELECT c.id FROM color c WHERE c.color = :color", nativeQuery = true)
    String getColorIdByColorName(@Param("color") String color);

    @Query(value = "SELECT c.color FROM color c WHERE c.id = :colorId", nativeQuery = true)
    String getColorStringNameFromColorId(@Param("colorId") String colorId);
}
