package com.example.MeetingCalendarAssistant.controller;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.model.Meeting;
import com.example.MeetingCalendarAssistant.model.TimeSlot;
import com.example.MeetingCalendarAssistant.service.MeetingService;
import com.example.MeetingCalendarAssistant.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private EmployeeService employeeService;



    @GetMapping
     public  ResponseEntity<List<Meeting>> getMeeting(){
            return ResponseEntity.status(HttpStatus.OK).body(meetingService.getMeeting());

        }
     @PostMapping("booking/{employeeId}")
    public ResponseEntity<Meeting> bookMeeting(@PathVariable int employeeId,@RequestBody Meeting meeting){
        try{
            Meeting bookedMeeting = meetingService.bookMeeting(employeeId,meeting);
            return  ResponseEntity.ok(bookedMeeting);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }


    @GetMapping("freeSlots/{employeeId1}/{employeeId2}/{durationMinutes}")
    public ResponseEntity<List<TimeSlot>> getFreeSlot(
            @PathVariable int employeeId1,
            @PathVariable int employeeId2,
            @PathVariable long durationMinutes
    ){
        Duration duration = Duration.ofMinutes(durationMinutes);
        List<TimeSlot> freeSlots = meetingService.findFreeSlots(employeeId1, employeeId2, duration);
        return ResponseEntity.ok(freeSlots);
    }

    @GetMapping("/conflicts")
    public ResponseEntity<List<Employee>> checkConflicts(
            @RequestBody Meeting meeting) {

        List<Employee> allEmployees = employeeService. getEmployee();
        List<Employee> conflictedParticipants = meetingService.findConflict(meeting, allEmployees);
        return ResponseEntity.ok(conflictedParticipants);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable int id) {
        try {
            meetingService.deleteMeeting(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

}
