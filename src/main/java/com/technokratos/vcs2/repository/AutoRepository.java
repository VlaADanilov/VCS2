package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AutoRepository extends JpaRepository<Auto, UUID> {
}
