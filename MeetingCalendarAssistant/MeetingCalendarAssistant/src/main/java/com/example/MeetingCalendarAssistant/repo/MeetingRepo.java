package com.example.MeetingCalendarAssistant.repo;

import com.example.MeetingCalendarAssistant.model.Employee;
import com.example.MeetingCalendarAssistant.model.Meeting;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepo extends JpaRepository<Meeting, Integer> {
    List<Meeting> findByEmployeeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Employee employee, LocalDateTime endTime, LocalDateTime startTime);


    List<Meeting> findByEmployeeId(int employeeId);

    List<Meeting> findByEmployee(Employee employee);
}
