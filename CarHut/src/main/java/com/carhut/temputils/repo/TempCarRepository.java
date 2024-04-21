package com.carhut.temputils.repo;

import com.carhut.temputils.models.TempCarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TempCarRepository extends JpaRepository<TempCarModel, String> {

    @Query(value = "SELECT * FROM tmp_car_list", nativeQuery = true)
    List<TempCarModel> getAllTempCars();

    @Query(value = "UPDATE tmp_car_list SET brand_id = :brandId WHERE id = :id", nativeQuery = true)
    void updateBrand(@Param("brandId") Integer brandId, @Param("id") String id);

    @Query(value = "UPDATE tmp_car_list SET model_id = :modelId WHERE id = :id", nativeQuery = true)
    void updateModel(@Param("modelId") Integer modelId, @Param("id") String id);

    @Query(value = "SELECT * FROM tmp_car_list WHERE id = :carId", nativeQuery = true)
    TempCarModel getTempCarWithId(@Param("carId") String carId);

}
