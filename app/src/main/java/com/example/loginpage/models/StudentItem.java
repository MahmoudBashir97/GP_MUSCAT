package com.example.loginpage.models;

public class StudentItem {

    private String roll;
    private String name;
    private String status;
    private String student_id;
    private String doctor_id;
    private String date;
    private String className;
    private String courseName;

    public StudentItem() {
    }

    public StudentItem(String roll, String name) {
        this.roll = roll;
        this.name = name;
        status = "";
        student_id="";
        doctor_id="";
        className ="";
        courseName ="";
        date="";
    }


    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getRoll() {
        return roll;
    }

    public void setId(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}