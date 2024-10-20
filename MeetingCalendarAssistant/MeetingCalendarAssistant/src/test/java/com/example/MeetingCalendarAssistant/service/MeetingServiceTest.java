package com.example.MeetingCalendarAssistant.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.model.Meeting;
import com.example.MeetingCalendarAssistant.model.TimeSlot;
import com.example.MeetingCalendarAssistant.repo.EmployeeRepo;
import com.example.MeetingCalendarAssistant.repo.MeetingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MeetingServiceTest {
    @InjectMocks
    private MeetingService meetingService;

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private MeetingRepo meetingRepo;

    private Employee employee;
    private Meeting meeting;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1);
        employee.setName("Navin");

        meeting = new Meeting();
        meeting.setId(1);
        meeting.setTitle("Project Kickoff");
        meeting.setStartTime(LocalDateTime.of(2024, 10, 20, 9, 0));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 20, 10, 0));
    }

    @Test
    public void testBookMeeting_Success() throws Exception {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(meetingRepo.findByEmployeeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList()); // No conflicts

        Meeting bookedMeeting = meetingService.bookMeeting(1, meeting);

        assertNotNull(bookedMeeting);
        assertEquals(meeting.getTitle(), bookedMeeting.getTitle());
        verify(meetingRepo).save(meeting);
        verify(employeeRepo).save(employee);
    }

    @Test
    public void testBookMeeting_Conflict() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(meetingRepo.findByEmployeeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(meeting)); // Conflict exists

        Exception exception = assertThrows(Exception.class, () -> {
            meetingService.bookMeeting(1, meeting);
        });

        assertEquals("Meeting Time Conflict", exception.getMessage());
    }

    @Test
    public void testFindFreeSlots() {
        when(meetingRepo.findByEmployeeId(1)).thenReturn(Arrays.asList());
        when(meetingRepo.findByEmployeeId(2)).thenReturn(Arrays.asList());

        List<TimeSlot> freeSlots = meetingService.findFreeSlots(1, 2, Duration.ofMinutes(30));

        assertNotNull(freeSlots);
        assertEquals(1, freeSlots.size()); // Expecting one free slot covering the day
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), freeSlots.get(0).getStartTime());
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0)), freeSlots.get(0).getEndTime());
    }


}