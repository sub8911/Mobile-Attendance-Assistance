package com.sub.studentinfosys.mobile_attendance_assistance.MODELS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sagar on 1/9/2017.
 */

public class AttendanceSheet implements Serializable {


    private int gen_id;
    private String id, studentRoll;
    private String studentName, create_on;
    private int presenti;


    public AttendanceSheet(String id, String roll, String name, String created_on, int presenti) {
        super();
        this.id = id;
        this.studentRoll = roll;
        this.studentName = name;
        this.presenti = presenti;
        this.create_on = created_on;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        studentName = studentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPresenti() {
        return presenti;
    }

    public void setPresenti(int presenti) {
        presenti = presenti;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }

    public String getCreate_on() {
        return create_on;
    }

    public void setCreate_on(String create_on) {
        this.create_on = create_on;
    }

    public String[] attendanceToString(ArrayList<AttendanceSheet> attendanceSheets)
    {
        Object[] objectArray = attendanceSheets.toArray();
        String[] stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);

        return stringArray;
    }


}
