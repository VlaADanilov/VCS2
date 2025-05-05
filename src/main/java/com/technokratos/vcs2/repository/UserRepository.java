package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT user FROM Auto auto JOIN auto.user user WHERE auto.id = :autoId")
    Optional<User> findByAuto(@Param("autoId") UUID auto_id);

    Page<User> findByUsernameStartingWithIgnoreCase(String search, PageRequest of);

    Long countByUsernameStartingWithIgnoreCase(String search);
}
