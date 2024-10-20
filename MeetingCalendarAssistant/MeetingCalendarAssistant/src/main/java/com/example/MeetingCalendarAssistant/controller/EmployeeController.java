package com.example.MeetingCalendarAssistant.controller;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        Employee savedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    @GetMapping("employee")
    public  ResponseEntity<List<Employee>> getEmployee(){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployee());
    }

    @DeleteMapping("employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

}
