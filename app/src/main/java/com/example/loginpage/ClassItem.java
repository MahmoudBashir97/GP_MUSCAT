package com.example.loginpage;

public class ClassItem {
    private long cid;

    public ClassItem(long cid, String className, String courseName) {
        this.cid = cid;
        this.className = className;
        this.CourseName = courseName;
    }

    private  String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    private String CourseName;

    public ClassItem(String className, String courseName) {
        this.className = className;
        CourseName = courseName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }
}
