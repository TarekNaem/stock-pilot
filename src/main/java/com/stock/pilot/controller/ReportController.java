package com.stock.pilot.controller;

import com.stock.pilot.dto.report.InventoryReportResponse;
import com.stock.pilot.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/reports")
@PreAuthorize("hasAnyRole('MANAGER','CLERK')")
public class ReportController {
    private final ReportService s;

    public ReportController(ReportService s) {
        this.s = s;
    }

    @GetMapping("/inventory")
    public InventoryReportResponse inventory(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        if (from != null && to != null && from.isAfter(to))
            throw new IllegalArgumentException("from must be before to");
        return s.inventory(from, to);
    }
}
