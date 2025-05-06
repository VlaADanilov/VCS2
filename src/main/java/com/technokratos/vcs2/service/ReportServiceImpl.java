package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.ReportNotFoundException;
import com.technokratos.vcs2.mapper.ReportMapper;
import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.model.dto.response.ReportResponseDto;
import com.technokratos.vcs2.model.entity.Report;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.ReportRepository;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final UserRepository userRepository;
    private final AutoRepository autoRepository;

    @Override
    public List<ReportResponseDto> getReports(int page, int size) {
        log.info("Fetching reports (page: {}, size: {})", page, size);

        List<ReportResponseDto> responseDtos = reportRepository
                .findAll(PageRequest.of(page, size))
                .stream()
                .map(reportMapper::toReportResponseDto)
                .toList();

        log.info("Found {} reports", responseDtos.size());
        return responseDtos;
    }

    @Override
    public UUID addReport(ReportRequestDto report, UUID userId, UUID autoId) {
        log.info("Adding new report for auto {} by user {}", autoId, userId);

        if (!autoRepository.existsById(autoId)) {
            log.warn("Auto with ID {} not found", autoId);
            throw new AutoNotFoundException(autoId);
        }

        Report result = new Report();
        UUID id = UUID.randomUUID();
        result.setId(id);
        result.setReporter(userRepository.getReferenceById(userId));
        result.setAuto(autoRepository.getReferenceById(autoId));
        result.setComment(report.getComment());

        reportRepository.save(result);
        log.info("Report added successfully with ID: {}", id);

        return id;
    }

    @Override
    public void addView(UUID reportId, UUID userId) {
        log.info("User {} is viewing report {}", userId, reportId);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    log.warn("Report with ID {} not found", reportId);
                    return new ReportNotFoundException(reportId);
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new RuntimeException("User not found");
                });

        if (!report.getViewed().contains(user)) {
            report.getViewed().add(user);
            reportRepository.save(report);
            log.info("User {} viewed report {}", userId, reportId);
        } else {
            log.debug("User {} already viewed report {}", userId, reportId);
        }
    }

    @Override
    public void deleteReport(UUID reportId) {
        log.info("Deleting report with ID: {}", reportId);

        if (!reportRepository.existsById(reportId)) {
            log.warn("Report with ID {} does not exist", reportId);
            throw new ReportNotFoundException(reportId);
        }

        reportRepository.deleteById(reportId);
        log.info("Report deleted successfully: {}", reportId);
    }

    @Override
    @Transactional
    public void deleteAllViewWhere(UUID id) {
        log.info("Deleting all view relations for report {}", id);

        reportRepository.deleteViewRelationsByUserIdNative(id);
        log.info("Deleted all view records for report {}", id);
    }
}
