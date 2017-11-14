package com.sub.studentinfosys.mobile_attendance_assistance.DATABASE;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sagar on 2/6/2017.
 */

public class AttendanceSystemContract {
    static final String PROVIDER_NAME = "com.sub.studentinfosys.mobile_attendance_assistance.DATABASE";
    public static final Uri CLASS_SUBJECT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + View_Triggers.VIEW_CLASS_SUBJECTS);
    public static final Uri CLASS_STUDENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + View_Triggers.VIEW_CLASS_STUDENTS);
    static final String URL_CLASSES = "content://" + PROVIDER_NAME + "/" + Classes.CLASS_TABLE;
    static final Uri CLASS_URI = Uri.parse(URL_CLASSES);
    static final String URL_SUBJECTS = "content://" + PROVIDER_NAME + "/" + Subject.SUBJECT_TABLE;
    static final Uri SUBJECTS_URI = Uri.parse(URL_SUBJECTS);
    static final String URL_STUDENTS = "content://" + PROVIDER_NAME + "/" + Student.STUDENT_TABLE;
    static final Uri STUDENTS_URI = Uri.parse(URL_STUDENTS);
    static final String URL_ATTENDANCE = "content://" + PROVIDER_NAME + "/" + Attendance.ATTENDANCE_TABLE;
    static final Uri ATTENDANCE_URI = Uri.parse(URL_ATTENDANCE);

    public static abstract class Classes implements BaseColumns {


        public static final String CLASS_TABLE = "class_list";
        public static final String CLASS_ID = "class_id";
        public static final String ROW_TYPE = "row_type";
        public static final String CLASS_NAME = "class_name";
        public static final String CLASS_DATE = "date";
        public static final String CLASS_STATUS = "class_status";
        public static final String CREATE_CLASSES_QUERY =
                "CREATE TABLE " + Classes.CLASS_TABLE + "("
                        + Classes.CLASS_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT,"
                        + Classes.CLASS_NAME + " TEXT NOT NULL, "
                        + Classes.CLASS_DATE + " TEXT NOT NULL,"
                        + Classes.ROW_TYPE + " INTEGER DEFAULT 0,"
                        + Classes.CLASS_STATUS + " INTEGER DEFAULT 0);";
        public static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + Classes.CLASS_TABLE;


    }

    public static abstract class Subject implements BaseColumns {


        public static final String SUBJECT_TABLE = "subject_list";
        public static final String CLASS_ID = "class_id";
        public static final String SUBJECT_ID = "subject_id";
        public static final String ROW_TYPE = "row_type";
        public static final String SUBJECT_NAME = "subject_name";
        public static final String SUBJECT_DATE = "date";
        public static final String SUBJECT_EXCEL = "to_excel_pointer";
        public static final String SUBJECT_STATUS = "subject_status";
        public static final String CREATE_SUBJECT_QUERY =
                "CREATE TABLE " + Subject.SUBJECT_TABLE + "("
                        + Subject.SUBJECT_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT,"
                        + Subject.CLASS_ID + " INTEGER NOT NULL, "
                        + Subject.SUBJECT_NAME + " TEXT, "
                        + Subject.SUBJECT_DATE + " TEXT, "
                        + Subject.ROW_TYPE + " INTEGER DEFAULT 1, "
                        + Subject.SUBJECT_EXCEL + " INTEGER DEFAULT 0, "
                        + Subject.SUBJECT_STATUS + " INTEGER DEFAULT 0, "
                        + " FOREIGN KEY (" + Subject.CLASS_ID + ") "
                        + " REFERENCES " + Classes.CLASS_TABLE + " (" + Classes.CLASS_ID + "));";
        public static final String DROP_SUBJECT_TABLE = "DROP TABLE IF EXISTS " + Subject.SUBJECT_TABLE;

    }

    public static abstract class Student implements BaseColumns {

        public static final String STUDENT_TABLE = "student_list";
        public static final String CLASS_ID = "class_id";
        public static final String STUDENT_ID = "student_id";
        public static final String ROW_TYPE = "row_type";
        public static final String STUDENT_NAME = "student_name";
        public static final String STUDENT_ROLL = "student_roll_num";
        public static final String STUDENT_DATE = "date";
        public static final String STUDENT_STATUS = "student_status";
        public static final String CREATE_STUDENT_QUERY =
                "CREATE TABLE " + Student.STUDENT_TABLE + "("
                        + Student.STUDENT_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT,"
                        + Student.CLASS_ID + " INTEGER NOT NULL, "
                        + Student.STUDENT_NAME + " TEXT , "
                        + Student.STUDENT_ROLL + " TEXT , "
                        + Student.STUDENT_DATE + " TEXT , "
                        + Student.ROW_TYPE + " INTEGER DEFAULT 1,"
                        + Student.STUDENT_STATUS + " INTEGER DEFAULT 0,"
                        + " FOREIGN KEY (" + Student.CLASS_ID + ") "
                        + " REFERENCES " + Classes.CLASS_TABLE + " (" + Classes.CLASS_ID + "));";
        public static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + Student.STUDENT_TABLE;


    }

    public static abstract class Attendance implements BaseColumns {


        public static final String ATTENDANCE_TABLE = "attendance_list";
        public static final String ATTENDANCE_ID = "attendance_id";
        public static final String CLASS_ID = "class_id";
        public static final String SUBJECT_ID = "subject_id";
        public static final String ROW_TYPE = "row_type";
        public static final String ATTENDANCE_CREATE_ON = "created_on_date";
        public static final String ATTENDANCE_EDITED_ON = "modified_on_date";
        public static final String ATTENDANCE_NUMBER_OF_LECTURES = "number_of_lectures";
        public static final String ATTENDANCE_STATUS = "attendance_status";
        public static final String ATTENDANCE_ABSENT_LIST = "attendance_absent";
        public static final String CREATE_ATTENDANCE_QUERY =
                "CREATE TABLE " + Attendance.ATTENDANCE_TABLE
                        + "(" + Attendance.ATTENDANCE_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT,"
                        + Attendance.CLASS_ID + " TEXT NOT NULL, "
                        + Attendance.SUBJECT_ID + " TEXT NOT NULL, "
                        + Attendance.ATTENDANCE_CREATE_ON + " TEXT , "
                        + Attendance.ATTENDANCE_EDITED_ON + " TEXT , "
                        + Attendance.ATTENDANCE_NUMBER_OF_LECTURES + " TEXT DEFAULT 1, "
                        + Attendance.ROW_TYPE + " INTEGER DEFAULT 2, "
                        + Attendance.ATTENDANCE_STATUS + " INTEGER DEFAULT 0, "
                        + Attendance.ATTENDANCE_ABSENT_LIST + " TEXT DEFAULT '0',"


                        + " FOREIGN KEY (" + Attendance.CLASS_ID + ") "
                        + " REFERENCES " + Classes.CLASS_TABLE + " (" + Classes.CLASS_ID + "),"
                        + " FOREIGN KEY (" + Attendance.SUBJECT_ID + ") "
                        + " REFERENCES " + Subject.SUBJECT_TABLE + " (" + Subject.SUBJECT_ID + "));";
        public static final String DROP_ATTENDANCE_TABLE = "DROP TABLE IF EXISTS " + Attendance.ATTENDANCE_TABLE;

    }

    public static abstract class View_Triggers {
        public static final String VIEW_CLASS_SUBJECTS = "class_subject";
        public static final String VIEW_CLASS_STUDENTS = "class_student";
        public static final String VIEW_ATTENDANCES = "attendance";
        public static final String VIEW_STUDENT_ATTENDANCES = "student_attendance";
        public static final String VIEW_CLASS_SUBJECT_ATTENDANCES = "class_subject_attendance";



      /* public static final String TRI_Class_Attendance="CREATE TRIGGER TRIClassAttendance AFTER INSERT ON "
               + Subject.SUBJECT_TABLE
               +" BEGIN INSERT INTO "+ Attendance.ATTENDANCE_TABLE
               +" ( "+Attendance.CLASS_ID+" ,"+ Attendance.SUBJECT_ID +", "+Attendance.ROW_TYPE
               +") VALUES (new.class_id,new.subject_id,new.row_type); END ";*/

        /*public static final String TRI_Class_Student="CREATE TRIGGER TRIClassStudent AFTER INSERT ON "
                +Classes.CLASS_TABLE+" BEGIN INSERT INTO "
                +Student.STUDENT_TABLE+"("+Student.CLASS_ID+","+Student.ROW_TYPE
                +") VALUES (new.class_id,new.row_type); END";*/

        public static final String TRI_Class_Subject = "CREATE TRIGGER TRIClassSubject AFTER INSERT ON " + Classes.CLASS_TABLE
                + " BEGIN INSERT INTO " + Subject.SUBJECT_TABLE
                + "(" + Subject.CLASS_ID + "," + Subject.ROW_TYPE + ") VALUES (new.class_id,new.row_type); END";

        public static final String VIEW_Class_Subject = "CREATE VIEW class_subject AS SELECT "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_ID + ","
                + Classes.CLASS_TABLE + "." + Classes.CLASS_NAME + ","
                + Classes.CLASS_TABLE + "." + Classes.CLASS_DATE + ","
                + Subject.SUBJECT_TABLE + "." + Subject.ROW_TYPE + ","
                + Subject.SUBJECT_TABLE + "." + Subject.SUBJECT_ID + ","
                + Subject.SUBJECT_TABLE + "." + Subject.SUBJECT_DATE + ","
                + Subject.SUBJECT_TABLE + "." + Subject.SUBJECT_NAME + " FROM "
                + Subject.SUBJECT_TABLE + " left join "
                + Classes.CLASS_TABLE + " where "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_ID + "="
                + Subject.SUBJECT_TABLE + "." + Subject.CLASS_ID + " AND "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_STATUS + " =0 AND "
                + Subject.SUBJECT_TABLE + "." + Subject.SUBJECT_STATUS + " =0 "
                + " ORDER BY " + Subject.SUBJECT_TABLE + "." + Subject.CLASS_ID + " ASC";
        public static final String VIEW_Class_Student = "CREATE VIEW class_student AS SELECT "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_ID + ","
                + Classes.CLASS_TABLE + "." + Classes.CLASS_NAME + ","
                + Classes.CLASS_TABLE + "." + Classes.CLASS_DATE + ","
                + Student.STUDENT_TABLE + "." + Student.ROW_TYPE + ","
                + Student.STUDENT_TABLE + "." + Student.STUDENT_NAME + ","
                + Student.STUDENT_TABLE + "." + Student.STUDENT_ROLL + ","
                + Student.STUDENT_TABLE + "." + Student.STUDENT_ID + ","
                + Student.STUDENT_TABLE + "." + Student.STUDENT_DATE
                + " FROM " + Student.STUDENT_TABLE + " left join " + Classes.CLASS_TABLE + " where "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_ID + " = " + Student.STUDENT_TABLE + "." + Student.CLASS_ID + " and "
                + Classes.CLASS_TABLE + "." + Classes.CLASS_STATUS + " =0 and " + Student.STUDENT_TABLE + "." + Student.STUDENT_STATUS + "=0 " +
                "ORDER BY " + Student.STUDENT_TABLE + "." + Student.CLASS_ID + " ASC";

    }


}
