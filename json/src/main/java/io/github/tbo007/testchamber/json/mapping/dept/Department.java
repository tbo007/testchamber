package io.github.tbo007.testchamber.json.mapping.dept;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Department {
    @JsonProperty("_id")
    private int id;
    private String departmentName;
    private String location;
    private List<Employee> employees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public static class Employee {
        private int employeeNumber;
        private String employeeName;
        private String job;
        private double salary;

        public int getEmployeeNumber() {
            return employeeNumber;
        }

        public void setEmployeeNumber(int employeeNumber) {
            this.employeeNumber = employeeNumber;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }
    }
}

