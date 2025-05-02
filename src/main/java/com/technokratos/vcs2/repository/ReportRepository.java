package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
}
