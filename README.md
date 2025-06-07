# EmployeeAnalyzer

## Overview
`EmployeeAnalyzer` is a simple Java application that reads employee data from a CSV file and analyzes the organizational structure of a company. The program checks:
- Whether a manager earns at least 20% more than the average salary of their direct subordinates, but no more than 50% more.
- Whether any employees have a reporting line that exceeds 4 managers between them and the CEO.

## Features
- **Manager Salary Validation**: Ensures managers earn a fair salary relative to their subordinates.
- **Reporting Line Analysis**: Identifies employees with excessive reporting lines.
- **Data-Driven**: Reads data from a CSV file containing employee information.

## Prerequisites
- **Java**: JDK 8 or later (JDK 11+ recommended)
- **Maven**: To build and manage the project dependencies.
- **Eclipse IDE**: To run the project (optional).

## Setup Instructions

### 1. Clone the Repository
To get started, clone the repository to your local machine:

```bash
git clone https://github.com/nikesh/EmployeeAnalyzer.git

### 2.1. Either run the EmployeeAnalyzer.java file directly by giving the CSV file path on IDE
This is direct java run through which the program output the results on IDE console

### 2.2. OR execute the jar that would get generated over maven install command
This step requires maven commands execution

### 2.2.1. install the dependencies by running
mvn clean install
This step generates the jar with the libraries included  and also executes the test cases as well. It should print "Running com.swissre.EmployeeAnalyzerTest" on maven command execution.

### 2.2.2. over command prompt go to project folder and run below command
java -jar target\EmployeeAnalyzer-1.0-SNAPSHOT-jar-with-dependencies.jar

### 2.2.2. Enter path as mentioned below as per the CSV file path
Enter the path to the employees CSV file: C:\Users\nikesh\workspace\swissre\src\main\resources\employees.csv

### 2.2.2. Below is the sample output it generates over jar execution
Manager Martin Chekov (124) earns 15000.00 less than expected. Should earn 15000.00 more.
There are no Manager who needs to earns less than expected.
Employee Alice Hasacat (300) has too long a reporting line: 2 managers.
Employee Brett Hardleaf (305) has too long a reporting line: 3 managers.
There are no employees which have more than 4 managers between them and the CEO.

