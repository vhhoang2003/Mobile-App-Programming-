package com.example.lab1;

import java.io.Serializable;

public class StudentInfo implements Serializable {
    public String fullName, studentID, className, schoolYear, yearLevel, departments, plan;

    public StudentInfo(String fullName, String studentID, String className, String schoolYear,
                       String yearLevel, String departments, String plan) {
        this.fullName = fullName;
        this.studentID = studentID;
        this.className = className;
        this.schoolYear = schoolYear;
        this.yearLevel = yearLevel;
        this.departments = departments;
        this.plan = plan;
    }
}
