package com.example.MeetingCalendarAssistant.service;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.repo.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;


    public Employee addEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public List<Employee> getEmployee() {
        return employeeRepo.findAll();
    }


    public void deleteEmployee(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepo.deleteById(id);
    }
}
