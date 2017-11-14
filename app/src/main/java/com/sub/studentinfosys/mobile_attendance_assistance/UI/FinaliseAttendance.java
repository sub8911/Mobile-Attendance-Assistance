package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.DateUtils;

import java.util.ArrayList;


public class FinaliseAttendance extends AppCompatActivity implements
        android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button yes, no;
    public String DateView;
    public Context context;
    public String ClassId;
    public String SubjectId, ClassName, SubjectName;
    public ArrayList<String> str;
    int count = 0;
    Spinner lecturs;
    String temp;
    TextView Date, StudentPresent;
    ContentValues cv = new ContentValues();
    int size;
    String Table_Name, date;
    AttendanceSheet ame;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogbox_save_attendance);
        DateView = DateUtils.getDate();
        ArrayList<AttendanceSheet> presenti = new ArrayList<AttendanceSheet>();
        ArrayList<String> str = new ArrayList<String>();
        presenti = (ArrayList<AttendanceSheet>) getIntent().getSerializableExtra("list_as_string");
        str = getIntent().getStringArrayListExtra("data");
        ClassId = str.get(0);
        ClassName = str.get(1);
        SubjectId = str.get(2);
        SubjectName = str.get(3);
        getSupportActionBar().setTitle(ClassName + " Attendance Records");

        yes = (Button) findViewById(R.id.btn_cancel);
        no = (Button) findViewById(R.id.btn_save);

        cv.put(AttendanceSystemContract.Attendance.CLASS_ID, ClassId);
        cv.put(AttendanceSystemContract.Attendance.SUBJECT_ID, SubjectId);
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON, DateView);

        StudentPresent = (TextView) findViewById(R.id.tv_numPresent);
        Date = (TextView) findViewById(R.id.tv_DateView);
        Date.setText(DateView);
        int cnt = getNumStdnts(presenti);
        if (cnt != 0)
            StudentPresent.setText(String.valueOf(cnt));
        else
            StudentPresent.setText("No one was Absent");
    }

    public int getNumStdnts(ArrayList<AttendanceSheet> presenti) {
        ArrayList<AttendanceSheet> std = presenti;
        String AbsentStudentList = null;

        for (AttendanceSheet std1 : std) { //AttendanceSheet std1=std.iterator().next();
            if (std1 != null) {
                if (std1.getPresenti() == 1) {
                    count++;
                    //Toast.makeText(getApplicationContext(),""+std1.getPresenti(),Toast.LENGTH_LONG).show();
                    if (AbsentStudentList == null)
                        AbsentStudentList = std1.getStudentRoll() + ",";
                    else
                        AbsentStudentList = AbsentStudentList + std1.getStudentRoll() + ",";

                }
            }
        }
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_ABSENT_LIST, AbsentStudentList);

        return count;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_submit_record:
                if (cv != null) {
                    spinner1 = (Spinner) findViewById(R.id.sp_lectures);
                    temp = spinner1.getSelectedItem().toString();
                   // Toast.makeText(getApplicationContext(),""+temp,Toast.LENGTH_LONG).show();
                    cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES, temp);
                    getContentResolver().insert(Class_SubjectProvider.ATTENDANCE_URI, cv);
                    Toast.makeText(getApplicationContext(), "Attendance Saved", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error in Values", Toast.LENGTH_LONG).show();
                    Log.e("Content Values is Empty", "Attendance Sheet");
                }
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
