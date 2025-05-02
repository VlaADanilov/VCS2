package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Report;
import com.technokratos.vcs2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    @Modifying
    @Query(value = "DELETE FROM report_viewed WHERE viewed_id = :userId", nativeQuery = true)
    void deleteViewRelationsByUserIdNative(@Param("userId") UUID userId);
}
