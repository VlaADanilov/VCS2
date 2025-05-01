package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Like;
import com.technokratos.vcs2.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    List<Like> findAllByUser(User user, Pageable pageable);

    boolean existsByUserAndAuto(User user, Auto auto);

    @Modifying
    void deleteByUserAndAuto(User user, Auto auto);
}
