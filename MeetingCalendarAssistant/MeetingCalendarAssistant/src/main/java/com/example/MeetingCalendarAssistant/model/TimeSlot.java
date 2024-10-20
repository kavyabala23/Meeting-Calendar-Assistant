package com.example.MeetingCalendarAssistant.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;


}
