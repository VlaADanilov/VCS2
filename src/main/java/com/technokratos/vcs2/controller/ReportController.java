package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.request.ReportRequestDto;
import com.technokratos.vcs2.service.ReportService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/{autoId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void report(@PathVariable("autoId") UUID autoId, @RequestBody ReportRequestDto reportRequestDto) {
        reportService.addReport(reportRequestDto,
                UserReturner.getCurrentUser().get().getId(),
                autoId);
    }

    @PostMapping("/addView/{reportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public void addView(@PathVariable("reportId") UUID reportId) {
        reportService.addView(reportId, UserReturner.getCurrentUser().get().getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String listOfReports(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("list", reportService.getReports(page, size));
        return "list_of_reports";
    }

    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReport(@PathVariable("reportId") UUID reportId) {
        reportService.deleteReport(reportId);
    }
}
