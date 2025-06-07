package com.swissre;


public class Employee {
	 private String id;
     private String firstName;
     private String lastName;
     private double salary;
     private String managerId;

     public Employee(String id, String firstName, String lastName, double salary, String managerId) {
         this.id = id;
         this.firstName = firstName;
         this.lastName = lastName;
         this.salary = salary;
         this.managerId = managerId;
     }

     public String getId() {
         return id;
     }

     public double getSalary() {
         return salary;
     }

     public String getManagerId() {
         return managerId;
     }

     public String toString() {
         return String.format("%s %s (%s)", firstName, lastName, id);
     }
}

