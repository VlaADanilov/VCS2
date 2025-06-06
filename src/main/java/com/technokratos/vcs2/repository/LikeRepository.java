package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Like;
import com.technokratos.vcs2.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    List<Like> findAllByUser(User user, Pageable pageable);

    boolean existsByUserAndAuto(User user, Auto auto);

    @Modifying
    void deleteByUserAndAuto(User user, Auto auto);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.user.id = :id")
    Long getCountOfLikeByUserId(@Param("id") UUID id);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.user.id = :id AND l.auto.brand.id = :brandId")
    Long getCountOfLikeByUserId(@Param("id") UUID id,@Param("brandId") UUID brandId);
}
