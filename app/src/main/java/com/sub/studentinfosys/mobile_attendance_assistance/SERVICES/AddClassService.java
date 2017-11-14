package com.sub.studentinfosys.mobile_attendance_assistance.SERVICES;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.DateUtils;

import java.util.Iterator;

import static com.sub.studentinfosys.mobile_attendance_assistance.UI.Add_Classes_Fragment.excelData;

/**
 * Created by Sagar on 2/23/2017.
 */

public class AddClassService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public AddClassService() {
        super("hi");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Uri data = workIntent.getData();
        String ClassName = data.getQueryParameter(AttendanceSystemContract.Classes.CLASS_NAME);
        String CreatedON = DateUtils.getDate();
        ContentValues values = new ContentValues();
        values.put(AttendanceSystemContract.Classes.CLASS_NAME, ClassName);
        values.put(AttendanceSystemContract.Classes.CLASS_DATE, CreatedON);
        getContentResolver().insert(Class_SubjectProvider.CLASS_URI, values);
        Iterator<AttendanceSheet> itr = excelData.iterator();
        // Retrieve Class ID records
        String ClassID = null;
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.Classes.CLASS_TABLE;
        String[] selection = {AttendanceSystemContract.Classes.CLASS_ID};
        Uri classes = Uri.parse(URL);
        Cursor c = getContentResolver().query(classes, selection, null, null, AttendanceSystemContract.Classes.CLASS_ID);

        if (c.moveToLast()) {
            do {

                ClassID = c.getString(c.getColumnIndex(AttendanceSystemContract.Classes.CLASS_ID));

            } while (c.moveToNext());
        }
        int count = 1;
        while (itr.hasNext()) {
            ContentValues cv1 = new ContentValues();
            AttendanceSheet row = itr.next();
            cv1.put(AttendanceSystemContract.Student.STUDENT_NAME, row.getStudentName());
            cv1.put(AttendanceSystemContract.Student.STUDENT_ROLL, row.getStudentRoll());
            cv1.put(AttendanceSystemContract.Student.CLASS_ID, ClassID);
            cv1.put(AttendanceSystemContract.Student.STUDENT_DATE, CreatedON);
            getContentResolver().insert(Class_SubjectProvider.STUDENTS_URI, cv1);

        }
    }

}