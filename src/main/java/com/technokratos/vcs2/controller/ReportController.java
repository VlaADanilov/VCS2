package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.ReportApi;
import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.service.ReportService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReportController implements ReportApi {
    private final ReportService reportService;

    public void report(UUID autoId, ReportRequestDto reportRequestDto) {
        reportService.addReport(reportRequestDto,
                UserReturner.getCurrentUser().get().getId(),
                autoId);
    }

    public void addView(UUID reportId) {
        reportService.addView(reportId, UserReturner.getCurrentUser().get().getId());
    }


    public String listOfReports(Model model,
                                int page,
                                int size) {
        model.addAttribute("list", reportService.getReports(page, size));
        return "list_of_reports";
    }

    public void deleteReport(UUID reportId) {
        reportService.deleteReport(reportId);
    }
}
