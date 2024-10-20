package com.example.MeetingCalendarAssistant.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.MeetingCalendarAssistant.repo.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.MeetingCalendarAssistant.model.Employee;


class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepo employeeRepo;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1);
        employee.setName("Navin");
    }

    @Test
    public void testAddEmployee() {
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.addEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals(employee.getName(), savedEmployee.getName());
        verify(employeeRepo).save(employee);
    }

    @Test
    public void testGetEmployees() {
        when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee));

        List<Employee> employees = employeeService.getEmployee();

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(employee.getName(), employees.get(0).getName());
    }

    @Test
    public void testDeleteEmployee_Success() {
        when(employeeRepo.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1));

        verify(employeeRepo).deleteById(1);
    }

    @Test
    public void testDeleteEmployee_NotFound() {
        when(employeeRepo.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.deleteEmployee(1);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
    }
}

