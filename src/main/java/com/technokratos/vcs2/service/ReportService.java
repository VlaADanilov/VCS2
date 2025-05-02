package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.model.dto.response.ReportResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
public interface ReportService {
    List<ReportResponseDto> getReports(int page, int size);

    UUID addReport(@Valid ReportRequestDto report, UUID userId, UUID autoId);

    void addView(UUID reportId,UUID userId);

    void deleteReport(UUID reportId);
}
