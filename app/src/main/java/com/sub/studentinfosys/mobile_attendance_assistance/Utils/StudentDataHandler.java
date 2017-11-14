package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;

import java.util.ArrayList;

/**
 * Created by Sagar on 8/13/2017.
 */

public class StudentDataHandler
{
    public AttendanceSheet getStudentDatailes(Context context, String prn,String class_id)
    {
        AttendanceSheet std_deatails = null;
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
        Uri list = Uri.parse(URL);
      //  Toast.makeText(context,"I am in",Toast.LENGTH_LONG).show();
        //  mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);
        Cursor cursor = context.getContentResolver().query( list, null, AttendanceSystemContract.Student.STUDENT_ROLL + "=? AND "+ AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{prn,class_id}, AttendanceSystemContract.Classes.CLASS_ID);
        if (cursor.moveToFirst())
            do {
               // Toast.makeText(context,"I am in 2",Toast.LENGTH_LONG).show();
                std_deatails=new AttendanceSheet(cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ROLL)),
                        cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_DATE)),
                        1);
                } while (cursor.moveToNext());
        //Toast.makeText(context,"/-- "+std_deatails.getStudentRoll(),Toast.LENGTH_LONG).show();
        return  std_deatails;
    }
}
