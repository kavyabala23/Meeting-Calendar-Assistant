package com.example.MeetingCalendarAssistant.service;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.model.Meeting;
import com.example.MeetingCalendarAssistant.model.TimeSlot;
import com.example.MeetingCalendarAssistant.repo.EmployeeRepo;
import com.example.MeetingCalendarAssistant.repo.MeetingRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MeetingService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private MeetingRepo meetingRepo;

    public Meeting bookMeeting(int employeeId,Meeting meeting) throws Exception {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->new EntityNotFoundException("Employee not found"));
        meeting.setEmployee(employee);
        employee.getMeetings().add(meeting);
        //conflicts
        List<Meeting> conflicts = meetingRepo.findByEmployeeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(employee,meeting.getStartTime(),meeting.getEndTime());
        if(!conflicts.isEmpty()){
            throw  new Exception("Meeting Time Conflict");
        }
        meetingRepo.save(meeting);
        employeeRepo.save(employee);
        return meeting ;
    }


    public List<TimeSlot> findFreeSlots(
            int employeeId1, int employeeId2, Duration duration) {
        List<Meeting> employee1Calendar = meetingRepo.findByEmployeeId(employeeId1);
        List<Meeting> employee2Calendar = meetingRepo.findByEmployeeId(employeeId2);


        // Combine both calendars
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings.addAll(employee1Calendar);
        allMeetings.addAll(employee2Calendar);

        // Sort all meetings by start time
        allMeetings.sort(Comparator.comparing(Meeting::getStartTime));

        // Find free slots
        List<TimeSlot> freeSlots = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN); // Start of the day

        for (Meeting meeting : allMeetings) {
            if (currentTime.plus(duration).isBefore(meeting.getStartTime())) {
                // Add free slot
                freeSlots.add(new TimeSlot(currentTime, meeting.getStartTime()));
            }
            // Move current time to the end of the current meeting
            currentTime = meeting.getEndTime().isAfter(currentTime) ? meeting.getEndTime() : currentTime;
        }

        // Add remaining time after the last meeting until end of the day (5 PM)
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0));
        if (currentTime.plus(duration).isBefore(endOfDay)) {
            freeSlots.add(new TimeSlot(currentTime, endOfDay));
        }

        return freeSlots;
    }


    public  List<Employee> findConflict(Meeting meeting,List<Employee> employees){
        List<Employee> conflictedEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            List<Meeting> existingMeetings = meetingRepo.findByEmployee(employee);

            for (Meeting existingMeeting : existingMeetings) {
                // Check for time conflict
                if (existingMeeting.getStartTime().isBefore(meeting.getEndTime()) &&
                        existingMeeting.getEndTime().isAfter(meeting.getStartTime())) {
                    conflictedEmployees.add(employee);
                    break; // No need to check further meetings for this employee
                }
            }
        }
        return conflictedEmployees;
    }

    public void deleteMeeting(int id) {
        if (!meetingRepo.existsById(id)) {
            throw new EntityNotFoundException("Meeting not found with id: " + id);
        }
        meetingRepo.deleteById(id);
    }

    public List<Meeting> getMeeting() {
        return meetingRepo.findAll();
    }
}
