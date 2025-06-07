package com.swissre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeAnalyzerTest {
    
    private Map<String, Employee> employees;
    
    @BeforeEach
    public void setUp() {
        // Sample employee data for testing
        employees = new HashMap<>();
        employees.put("123", new Employee("123", "Joe", "Doe", 60000, null));
        employees.put("124", new Employee("124", "Martin", "Chekov", 45000, "123"));
        employees.put("125", new Employee("125", "Bob", "Ronstad", 47000, "123"));
        employees.put("300", new Employee("300", "Alice", "Hasacat", 50000, "124"));
        employees.put("305", new Employee("305", "Brett", "Hardleaf", 34000, "300"));
        
        // Simulating an employee with more than 4 managers
        employees.put("999", new Employee("999", "Test", "Employee", 30000, "123"));
        employees.put("1000", new Employee("1000", "Test2", "Employee", 30000, "999"));
        employees.put("1001", new Employee("1001", "Test3", "Employee", 30000, "1000"));
        employees.put("1002", new Employee("1002", "Test4", "Employee", 30000, "1001"));
        employees.put("1003", new Employee("1003", "Test5", "Employee", 30000, "1002"));
    }

    @Test
    public void testManagerEarnsLessThanExpected() {
        List<Employee> subordinates = Arrays.asList(
                employees.get("124"), employees.get("125"));
        double averageSalary = subordinates.stream()
                                           .mapToDouble(Employee::getSalary)
                                           .average()
                                           .orElse(0);

        Employee manager = employees.get("124");
        double minSalary = averageSalary * EmployeeAnalyzer.MIN_SALARY_MULTIPLIER;

        // Verify if manager earns less than expected
        assertTrue(manager.getSalary() < minSalary, 
                   "Manager should earn more than " + minSalary);
    }

    @Test
    public void testManagerEarnsMoreThanExpected() {
        List<Employee> subordinates = Arrays.asList(
                employees.get("124"), employees.get("125"));
        double averageSalary = subordinates.stream()
                                           .mapToDouble(Employee::getSalary)
                                           .average()
                                           .orElse(0);

        Employee manager = employees.get("123");
        double maxSalary = averageSalary * EmployeeAnalyzer.MAX_SALARY_MULTIPLIER;

        // Verify if manager earns more than expected, it should retrun false 
        //as no condition met this
        assertFalse(manager.getSalary() > maxSalary, 
                   "Manager should earn less than " + maxSalary);
    }

    @Test
    public void testNoManagersEarningMoreOrLessThanExpected() {
        List<Employee> subordinates = Arrays.asList(
                employees.get("124"), employees.get("125"));
        double averageSalary = subordinates.stream()
                                           .mapToDouble(Employee::getSalary)
                                           .average()
                                           .orElse(0);

        Employee manager = employees.get("123");
        double minSalary = averageSalary * EmployeeAnalyzer.MIN_SALARY_MULTIPLIER;
        double maxSalary = averageSalary * EmployeeAnalyzer.MAX_SALARY_MULTIPLIER;

        // Check that no manager earns less or more than expected
        assertTrue(manager.getSalary() >= minSalary && manager.getSalary() <= maxSalary, 
                   "Manager salary is within the expected range.");
    }

    @Test
    public void testEmployeesWithTooLongReportingLine() {
        // Set up employees with reporting lines
        Employee employee1 = employees.get("300"); // Reporting line: 3 managers
        Employee employee2 = employees.get("305"); // Reporting line: 4 managers
        Employee employeeWithLongLine = employees.get("1003"); // Reporting line: 5 managers
        
        // Check reporting line length for each employee
        int reportingLineLength1 = EmployeeAnalyzer.getReportingLineLength(employee1.getManagerId(), employees);
        int reportingLineLength2 = EmployeeAnalyzer.getReportingLineLength(employee2.getManagerId(), employees);
        int reportingLineLength3 = EmployeeAnalyzer.getReportingLineLength(employeeWithLongLine.getManagerId(), employees);
        
        // Employees with reporting line > 4 should be flagged
        assertTrue(reportingLineLength1 <= EmployeeAnalyzer.MAX_REPORTING_LINE_LENGTH);
        assertTrue(reportingLineLength2 <= EmployeeAnalyzer.MAX_REPORTING_LINE_LENGTH);
        assertTrue(reportingLineLength3 > EmployeeAnalyzer.MAX_REPORTING_LINE_LENGTH, 
                   "Employee with ID 1003 should have more than 4 managers in the reporting line.");
    }

    @Test
    public void testEmployeesWithNormalReportingLine() {
        Employee employee1 = employees.get("124"); // Reporting line: 1 manager
        Employee employee2 = employees.get("125"); // Reporting line: 1 manager
        
        // Check reporting line length for each employee
        int reportingLineLength1 = EmployeeAnalyzer.getReportingLineLength(employee1.getManagerId(), employees);
        int reportingLineLength2 = EmployeeAnalyzer.getReportingLineLength(employee2.getManagerId(), employees);
        
        // Employees with reporting line <= 4 should be valid
        assertTrue(reportingLineLength1 <= EmployeeAnalyzer.MAX_REPORTING_LINE_LENGTH);
        assertTrue(reportingLineLength2 <= EmployeeAnalyzer.MAX_REPORTING_LINE_LENGTH);
    }

    @Test
    public void testEmployeeReportingLineTooLong() {
        // Mock an employee with more than 4 managers
        int lineLength = EmployeeAnalyzer.getReportingLineLength("1002", employees);
        
        assertTrue(lineLength > 4, "Employee has a reporting line that is too long.");
    }

    @Test
    public void testEmployeeReportingLineCorrectLength() {
        Employee employee = employees.get("124");
        int lineLength = EmployeeAnalyzer.getReportingLineLength(employee.getManagerId(), employees);
        
        assertEquals(lineLength, 1, "Employee has a reporting line that is too long.");
    }
}
