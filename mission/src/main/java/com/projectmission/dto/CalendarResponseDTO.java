package com.projectmission.dto;

import java.util.ArrayList;
import java.util.List;

public class CalendarResponseDTO {
    private String from;
    private String to;
    private int totalEvents;
    private int upcomingCount;
    private int totalHours;
    private List<CalendarEventDTO> events = new ArrayList<>();

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public int getTotalEvents() { return totalEvents; }
    public void setTotalEvents(int totalEvents) { this.totalEvents = totalEvents; }
    public int getUpcomingCount() { return upcomingCount; }
    public void setUpcomingCount(int upcomingCount) { this.upcomingCount = upcomingCount; }
    public int getTotalHours() { return totalHours; }
    public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
    public List<CalendarEventDTO> getEvents() { return events; }
    public void setEvents(List<CalendarEventDTO> events) { this.events = events; }
}
