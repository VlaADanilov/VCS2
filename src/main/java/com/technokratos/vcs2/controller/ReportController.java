package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.ReportApi;
import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.service.ReportService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReportController implements ReportApi {
    private final ReportService reportService;

    public void report(UUID autoId, ReportRequestDto reportRequestDto) {
        log.info("Report request: {}: To auto with id {}, from user with id {}",
                reportRequestDto,
                autoId,
                UserReturner.getCurrentUser().get().getId());
        reportService.addReport(reportRequestDto,
                UserReturner.getCurrentUser().get().getId(),
                autoId);
    }

    public void addView(UUID reportId) {
        log.info("User with id {} add view to report with id {}",
                UserReturner.getCurrentUser().get().getId(),
                reportId);
        reportService.addView(reportId, UserReturner.getCurrentUser().get().getId());
    }


    public String listOfReports(Model model,
                                int page,
                                int size) {
        model.addAttribute("list", reportService.getReports(page, size));
        return "list_of_reports";
    }

    public void deleteReport(UUID reportId) {
        log.info("delete report with id {}, from user with id {}",
                reportId,
                UserReturner.getCurrentUser().get().getId());
        reportService.deleteReport(reportId);
    }
}
