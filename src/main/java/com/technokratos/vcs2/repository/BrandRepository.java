package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    @Query("SELECT brand FROM Auto auto JOIN auto.brand brand WHERE auto.id = :carId")
    Brand getBrandFromCarByCarId(@Param("carId") UUID carId);
}
