package com.swissre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class EmployeeAnalyzer {
	
	static final double MIN_SALARY_MULTIPLIER = 1.2;  // 20%
    static final double MAX_SALARY_MULTIPLIER = 1.5;  // 50%
    static final int MAX_REPORTING_LINE_LENGTH = 4;   // More than 4 managers

 // Load employees data from CSV
    private static Map<String, Employee> loadEmployees(String filePath) {
        Map<String, Employee> employees = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.skip(1)  // Skip header line
                 .map(line -> line.split(","))
                 .map(fields -> new Employee(fields[0], fields[1], fields[2], Double.parseDouble(fields[3]), fields.length > 4 ? fields[4] : null))
                 .forEach(employee -> employees.put(employee.getId(), employee));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
        return employees;
    }

    // Build a map of managers with their direct subordinates
    private static Map<String, List<Employee>> getManagersMap(Map<String, Employee> employees) {
        Map<String, List<Employee>> managers = new HashMap<>();
        for (Employee employee : employees.values()) {
            String managerId = employee.getManagerId();
            if (managerId != null) {
                managers.computeIfAbsent(managerId, k -> new ArrayList<>()).add(employee);
            }
        }
        return managers;
    }

    // Get the length of reporting line to CEO
    static int getReportingLineLength(String managerId, Map<String, Employee> employees) {
        int length = 0;
        while (managerId != null) {
            Employee manager = employees.get(managerId);
            if (manager == null){
            	break;
            }
            managerId = manager.getManagerId();
            length++;
        }
        return length;
    }

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the path to the employees CSV file: ");
		String filePath = scanner.nextLine();
		// Try to load the CSV file from the specified file path
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			System.out.println("File not found at the specified path.");
			return;
		}

		 // Load employees data from CSV
        Map<String, Employee> employees = loadEmployees(filePath);
        if (employees == null) return;

        // Process salary conditions for managers
        Map<String, List<Employee>> managers = getManagersMap(employees);
        boolean earnMoreFlag = false;
        boolean earnLessFlag = false;
        for (Map.Entry<String, List<Employee>> entry : managers.entrySet()) {
            String managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();
            double averageSalary = subordinates.stream()
                                               .mapToDouble(Employee::getSalary)
                                               .average()
                                               .orElse(0);

            // Check if the manager earns less or more than they should
            Employee manager = employees.get(managerId);
            if (manager != null) {
                double managerSalary = manager.getSalary();
                double minSalary = averageSalary * MIN_SALARY_MULTIPLIER;
                double maxSalary = averageSalary * MAX_SALARY_MULTIPLIER;

                if (managerSalary < minSalary) {
                	earnMoreFlag = true;
                    System.out.printf("Manager %s earns %.2f less than expected. Should earn %.2f more.\n", 
                        manager, minSalary - managerSalary, minSalary - managerSalary);
                }
                if (managerSalary > maxSalary) {
                	earnLessFlag = true;
                    System.out.printf("Manager %s earns %.2f more than expected. Should earn %.2f less.\n", 
                        manager, managerSalary - maxSalary, managerSalary - maxSalary);
                }
            }
        }
        
        if(!earnMoreFlag){
        	 System.out.printf("There are no Manager who needs to earns more than expected.\n");
        }
        
        if(!earnLessFlag){
       	 System.out.printf("There are no Manager who needs to earns less than expected.\n");
       }

        // Check for employees with too long reporting lines
        boolean maxReportingExist = false;
        for (Employee employee : employees.values()) {
            int reportingLineLength = getReportingLineLength(employee.getManagerId(), employees);
            
            if (reportingLineLength > MAX_REPORTING_LINE_LENGTH) {
            	maxReportingExist = true;
            	System.out.printf("Employee %s has too long a reporting line: %d managers.\n", employee, reportingLineLength);
            }            
            if (reportingLineLength > 1) {
                System.out.printf("Employee %s has too long a reporting line: %d managers.\n", employee, reportingLineLength);
            }
        }
        
        if(!maxReportingExist){
        	System.out.printf("There are no employees which have more than 4 managers between them and the CEO. \n");
        }
    }
    
}

