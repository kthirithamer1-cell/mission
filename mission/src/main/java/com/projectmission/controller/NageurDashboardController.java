package com.projectmission.controller;

import com.projectmission.dto.CalendarResponseDTO;
import com.projectmission.dto.NageurDashboardDTO;
import com.projectmission.service.CalendarService;
import com.projectmission.service.NageurDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class NageurDashboardController {

    @Autowired
    private NageurDashboardService dashboardService;

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/nageur")
    public ResponseEntity<NageurDashboardDTO> getNageurDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @GetMapping("/nageur/calendar")
    public ResponseEntity<CalendarResponseDTO> getNageurCalendar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(calendarService.getNageurCalendar(from, to));
    }
}
