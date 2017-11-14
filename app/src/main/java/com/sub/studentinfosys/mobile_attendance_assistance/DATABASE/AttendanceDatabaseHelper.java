package com.sub.studentinfosys.mobile_attendance_assistance.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Sagar on 2/18/2017.
 */

public class AttendanceDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MOBI_ATTENDANCE.DB";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public AttendanceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("DATABASE", "CREATED/OPEN");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        execCreatQueris(db);
        Log.v(" Tables ", "CREATED/OPEN");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(AttendanceSystemContract.Attendance.DROP_ATTENDANCE_TABLE);
        db.execSQL(AttendanceSystemContract.Classes.DROP_CLASS_TABLE);
        db.execSQL(AttendanceSystemContract.Subject.DROP_SUBJECT_TABLE);
        db.execSQL(AttendanceSystemContract.Student.DROP_STUDENT_TABLE);
        onCreate(db);
    }

    public void execCreatQueris(SQLiteDatabase db) {


        db.execSQL(AttendanceSystemContract.Classes.CREATE_CLASSES_QUERY);
        db.execSQL(AttendanceSystemContract.Subject.CREATE_SUBJECT_QUERY);
        db.execSQL(AttendanceSystemContract.Student.CREATE_STUDENT_QUERY);
        db.execSQL(AttendanceSystemContract.Attendance.CREATE_ATTENDANCE_QUERY);
        db.execSQL(AttendanceSystemContract.View_Triggers.TRI_Class_Subject);
        db.execSQL(AttendanceSystemContract.View_Triggers.VIEW_Class_Student);
       /* db.execSQL( AttendanceSystemContract.View_Triggers.TRI_Class_Attendance);*/
       /* db.execSQL( AttendanceSystemContract.View_Triggers.TRI_Class_Student);*/
        db.execSQL(AttendanceSystemContract.View_Triggers.VIEW_Class_Subject);

    }
}
    

