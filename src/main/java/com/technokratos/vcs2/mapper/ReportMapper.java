package com.technokratos.vcs2.mapper;

import com.technokratos.vcs2.model.dto.response.ReportResponseDto;
import com.technokratos.vcs2.model.entity.Report;
import com.technokratos.vcs2.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportResponseDto toReportResponseDto(Report report) {
        return new ReportResponseDto(
                report.getId(),
                report.getComment(),
                report.getReporter().getUsername(),
                report.getAuto().getId(),
                report.getViewed().stream().map(User::getUsername).toList()
        );
    }
}
