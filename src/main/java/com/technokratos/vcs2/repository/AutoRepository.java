package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutoRepository extends JpaRepository<Auto, UUID> {
    @Query("SELECT auto FROM Auto auto JOIN auto.user user WHERE user.id=:userId")
    Page<Auto> getPageableAutoFromUser(@Param("userId") UUID userId, Pageable pageRequest);

    @Query("SELECT image FROM Auto auto JOIN auto.images image WHERE auto.id=:autoId AND image.id=:imageId")
    Optional<Image> getImageWhereAutoId(@Param("autoId") UUID autoId, @Param("imageId") UUID imageId);

    @Query("SELECT COUNT(a) FROM Auto a WHERE a.brand.id = :brandId")
    Long countOfBrandCars(@Param("brandId") UUID brandId);

    @Query("SELECT COUNT(a) FROM Auto a WHERE a.user.id = :userId AND a.brand.id = :brandId")
    Long countOf(@Param("userId") UUID userID,@Param("brandId") UUID brandId);

    @Query("SELECT COUNT(a) FROM Auto a WHERE a.user.id = :userId")
    Long countOf(@Param("userId") UUID userID);
}
