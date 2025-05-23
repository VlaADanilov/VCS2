package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
