package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

import android.content.Context;

import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sagar on 3/4/2017.
 */

public class CursorManipulation {
    public ArrayList<AttendanceSheet> getAbsentDetails(String absent_stdnt_list, Context context,String classId) {
        ArrayList<AttendanceSheet> AList = new ArrayList<AttendanceSheet>();
        StudentDataHandler studentDataHandler=new StudentDataHandler();
        try {
            if (!absent_stdnt_list.equals("")) {
                String[] strValues = absent_stdnt_list.split(",");

                /*
                 * Use asList method of Arrays class to convert Java String array to ArrayList
                 */
                if(strValues!=null)
                for (int i=0;i<strValues.length;i++) {

                    AttendanceSheet temp = studentDataHandler.getStudentDatailes(context,strValues[i],classId);
                    AList.add(temp);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return AList;
    }
}
