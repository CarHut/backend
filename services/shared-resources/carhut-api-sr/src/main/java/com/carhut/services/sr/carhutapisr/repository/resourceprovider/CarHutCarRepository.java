package com.carhut.services.sr.carhutapisr.repository.resourceprovider;

import com.carhut.commons.model.CarHutCar;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public interface CarHutCarRepository extends JpaRepository<CarHutCar, String> {

    @Modifying
    @Query(value = "UPDATE carhut_cars SET is_active = false WHERE id = :carId", nativeQuery = true)
    void removeOffer(@Param("carId") String carId);

    @Query(value = "SELECT * FROM carhut_cars WHERE seller_id = :userId", nativeQuery = true)
    List<CarHutCar> getListingsForUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM carhut_cars WHERE id = :carId", nativeQuery = true)
    CarHutCar getCarWithId(@Param("carId") String carId);

    @Query(value = """
                   SELECT c.* FROM carhut_cars c
                   WHERE (:brandId IS NULL OR c.brand_id::INT = :brandId)
                   AND (:modelId IS NULL OR c.model_id::INT = :modelId)
                   AND (:priceFrom IS NULL OR c.price::INT >= :priceFrom)
                   AND (:priceTo IS NULL OR c.price::INT <= :priceTo)
                   AND (:mileageFrom IS NULL OR c.mileage::INT >= :mileageFrom)
                   AND (:mileageTo IS NULL OR c.mileage::INT <= :mileageTo)
                   AND (:registrationFrom IS NULL OR c.registration::INT >= :registrationFrom)
                   AND (:registrationTo IS NULL OR c.registration::INT <= :registrationTo)
                   AND (:seatingConfig IS NULL OR c.seats = :seatingConfig)
                   AND (:doors IS NULL OR c.doors = :doors)
                   AND (:location IS NULL OR c.seller_address = :location)
                   AND (:fuelType IS NULL OR c.fuel = :fuelType)
                   AND (:powerFrom IS NULL OR c.engine_power::INT >= :powerFrom)
                   AND (:powerTo IS NULL OR c.engine_power::INT <= :powerTo)
                   AND (:displacementFrom IS NULL OR c.engine_displacement::INT >= :displacementFrom)
                   AND (:displacementTo IS NULL OR c.engine_displacement::INT <= :displacementTo)
                   AND (:gearbox IS NULL OR c.gearbox = :gearbox)
                   AND (:powertrain IS NULL OR c.powertrain = :powertrain)
                   """, nativeQuery = true)
    List<CarHutCar> getCarsWithFilter(@Param("brandId") Integer brandId, @Param("modelId") Integer modelId,
                                      @Param("priceFrom") Integer priceFrom, @Param("priceTo") Integer priceTo,
                                      @Param("mileageFrom") Integer mileageFrom, @Param("mileageTo") Integer mileageTo,
                                      @Param("registrationFrom") Integer registrationFrom,
                                      @Param("registrationTo") Integer registrationTo,
                                      @Param("seatingConfig") String seatingConfig, @Param("doors") String doors,
                                      @Param("location") String location, @Param("fuelType") String fuelType,
                                      @Param("powerFrom") Integer powerFrom, @Param("powerTo") Integer powerTo,
                                      @Param("displacementFrom") Integer displacementFrom,
                                      @Param("displacementTo") Integer displacementTo, @Param("gearbox") String gearbox,
                                      @Param("powertrain") String powertrain/**, @Param("carTypes") List<String> carTypes**/);

    @Query(value = """
                   SELECT COUNT(c.*) FROM carhut_cars c
                   WHERE (:brandId IS NULL OR c.brand_id::INT = :brandId)
                   AND (:modelId IS NULL OR c.model_id::INT = :modelId)
                   AND (:priceFrom IS NULL OR c.price::INT >= :priceFrom)
                   AND (:priceTo IS NULL OR c.price::INT <= :priceTo)
                   AND (:mileageFrom IS NULL OR c.mileage::INT >= :mileageFrom)
                   AND (:mileageTo IS NULL OR c.mileage::INT <= :mileageTo)
                   AND (:registrationFrom IS NULL OR c.registration::INT >= :registrationFrom)
                   AND (:registrationTo IS NULL OR c.registration::INT <= :registrationTo)
                   AND (:seatingConfig IS NULL OR c.seats = :seatingConfig)
                   AND (:doors IS NULL OR c.doors = :doors)
                   AND (:location IS NULL OR c.seller_address = :location)
                   AND (:fuelType IS NULL OR c.fuel = :fuelType)
                   AND (:powerFrom IS NULL OR c.engine_power::INT >= :powerFrom)
                   AND (:powerTo IS NULL OR c.engine_power::INT <= :powerTo)
                   AND (:displacementFrom IS NULL OR c.engine_displacement::INT >= :displacementFrom)
                   AND (:displacementTo IS NULL OR c.engine_displacement::INT <= :displacementTo)
                   AND (:gearbox IS NULL OR c.gearbox = :gearbox)
                   AND (:powertrain IS NULL OR c.powertrain = :powertrain)
                   """, nativeQuery = true)
    Integer getNumberOfFilteredCars(@Param("brandId") Integer brandId, @Param("modelId") Integer modelId,
                                    @Param("priceFrom") Integer priceFrom, @Param("priceTo") Integer priceTo,
                                    @Param("mileageFrom") Integer mileageFrom, @Param("mileageTo") Integer mileageTo,
                                    @Param("registrationFrom") Integer registrationFrom,
                                    @Param("registrationTo") Integer registrationTo,
                                    @Param("seatingConfig") String seatingConfig, @Param("doors") String doors,
                                    @Param("location") String location, @Param("fuelType") String fuelType,
                                    @Param("powerFrom") Integer powerFrom, @Param("powerTo") Integer powerTo,
                                    @Param("displacementFrom") Integer displacementFrom,
                                    @Param("displacementTo") Integer displacementTo, @Param("gearbox") String gearbox,
                                    @Param("powertrain") String powertrain/**, @Param("carTypes") List<String> carTypes**/);

    @Query(value = """
    SELECT c.* FROM carhut_cars c
    WHERE c.id IN :ids
    """, nativeQuery = true)
    List<CarHutCar> getCarsWithIds(@Param("ids") List<String> ids);
}