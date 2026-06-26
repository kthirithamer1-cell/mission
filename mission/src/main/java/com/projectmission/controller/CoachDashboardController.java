package com.projectmission.controller;

import com.projectmission.dto.CalendarResponseDTO;
import com.projectmission.dto.CoachDashboardDTO;
import com.projectmission.service.CalendarService;
import com.projectmission.service.CoachDashboardService;
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
public class CoachDashboardController {

    @Autowired
    private CoachDashboardService dashboardService;

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/coach")
    public ResponseEntity<CoachDashboardDTO> getCoachDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @GetMapping("/coach/calendar")
    public ResponseEntity<CalendarResponseDTO> getCoachCalendar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(calendarService.getCoachCalendar(from, to));
    }
}
