package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.ReportNotFoundException;
import com.technokratos.vcs2.mapper.ReportMapper;
import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.model.dto.response.ReportResponseDto;
import com.technokratos.vcs2.model.entity.Report;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.ReportRepository;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final UserRepository userRepository;
    private final AutoRepository autoRepository;
    @Override
    public List<ReportResponseDto> getReports(int page, int size) {
        return reportRepository
                .findAll(PageRequest.of(page,size))
                .stream()
                .map(reportMapper::toReportResponseDto)
                .toList();
    }

    @Override
    public UUID addReport(ReportRequestDto report, UUID userId, UUID autoId) {
        Report result = new Report();
        UUID id = UUID.randomUUID();
        result.setId(id);
        result.setReporter(userRepository.getReferenceById(userId));
        result.setAuto(autoRepository.getReferenceById(autoId));
        result.setComment(report.getComment());
        reportRepository.save(result);
        return id;
    }

    @Override
    public void addView(UUID reportId, UUID userId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ReportNotFoundException(reportId));
        report.getViewed().add(userRepository.findById(userId).get());
        reportRepository.save(report);
    }

    @Override
    public void deleteReport(UUID reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new ReportNotFoundException(reportId);
        }
        reportRepository.deleteById(reportId);
    }

    @Override
    @Transactional
    public void deleteAllViewWhere(UUID id) {
        reportRepository.deleteViewRelationsByUserIdNative(id);
    }
}
