package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
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
import java.util.List;

import static com.sub.studentinfosys.mobile_attendance_assistance.UI.TakeAttendance.presenti;

/**
 * Created by Sagar on 2/27/2017.
 */

public class DialogSaveAttendance extends Dialog implements
        android.view.View.OnClickListener {
    public Context c;
    public Dialog d;
    public Button yes, no;
    public String DateView;
    public Context context;
    public String ClassId;
    public String SubjectId;
    public AttendanceSheet[] map;
    public ArrayList<String> str;
    int count = 0;
    Spinner lecturs;
    TextView Date, StudentPresent;
    ContentValues cv = new ContentValues();
    int size;

    public DialogSaveAttendance(Context context, ArrayList<String> str, int size) {
        super(context);
        this.context = context;
        this.ClassId = str.get(0);
        this.SubjectId = str.get(2);
        this.map = presenti;
        this.size = size;
        DateView = DateUtils.getDate();
        this.str = str;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                Intent intent1 = new Intent(context, AttendanceActivity.class);
                context.startActivity(intent1);
                dismiss();
                break;
            case R.id.btn_save:
                if (cv != null) {
                    getContext().getContentResolver().insert(Class_SubjectProvider.ATTENDANCE_URI, cv);
                    Toast.makeText(getContext(), "Attendance Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), TakeAttendance.class);
                    intent.putStringArrayListExtra("data", str);
                    getContext().startActivity(intent);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Error in Values", Toast.LENGTH_LONG).show();
                    Log.e("Content Values is Empty", "Attendance Sheet");
                }
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogbox_save_attendance);

        yes = (Button) findViewById(R.id.btn_cancel);
        no = (Button) findViewById(R.id.btn_save);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        String temp;
        lecturs = (Spinner) findViewById(R.id.sp_lectures);
        cv.put(AttendanceSystemContract.Attendance.CLASS_ID, ClassId);
        cv.put(AttendanceSystemContract.Attendance.SUBJECT_ID, SubjectId);
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON, DateView);
        temp = lecturs.getSelectedItem().toString();
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES, temp);
        StudentPresent = (TextView) findViewById(R.id.tv_numPresent);
        Date = (TextView) findViewById(R.id.tv_DateView);
        Date.setText(DateView);
    /*   int cnt= size-getNumStdnts(map);
         StudentPresent.setText(String.valueOf(cnt));*/
        Toast.makeText(getContext(), ClassId + "/" + SubjectId, Toast.LENGTH_LONG).show();
        getNumStdnts(map);

    }

    public void getNumStdnts(AttendanceSheet[] presenti) {
        if (presenti != null)
            for (AttendanceSheet std : presenti) { //AttendanceSheet std1=std.iterator().next();
              /*  Log.v("" + std1.getGen_id(), "///" + String.valueOf(std1.getPresenti()));
           // cv.put("'" + std1.getGen_id() + "'", String.valueOf(std1.getPresenti()));
                count++;*/
                //System.out.print("Gen id == "+std.getGen_id());
                //System.out.print(" Student Roll == "+std.getStudentRoll());
                // System.out.print("Student Name == "+std.getStudentName());
                // System.out.print("Student id == "+std.getCreate_on());
                // System.out.print("Student id view == "+std.getId()+"/");
                System.out.print("" + std.toString() + "/");

            }

    }


}
