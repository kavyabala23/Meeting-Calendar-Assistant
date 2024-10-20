package com.example.MeetingCalendarAssistant.model;

import lombok.Data;

import java.util.List;

@Data
public class FreeSlotRequest {
    private List<Meeting> employee1Calendar;
    private List<Meeting> employee2Calendar;
    private long durationMinutes;

}
