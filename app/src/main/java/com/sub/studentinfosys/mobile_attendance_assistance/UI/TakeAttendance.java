package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.AttendanceSheetAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;
import java.util.Arrays;

public class TakeAttendance extends AppCompatActivity {
    public static int ACTIVITY_CONSTANT = 0;
    public static AttendanceSheet[] presenti;
    public static boolean[] counter;
    ListView list1;
    ArrayList<String> str = new ArrayList<String>();
    AttendanceSheetAdapter attendanceSheetAdapter;
    ArrayList<AttendanceSheet> attendanceSheets;
    ArrayList<AttendanceSheet> attendance = new ArrayList<AttendanceSheet>();
    TextView counterView;
    int count = 0;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        // inflate the array list with data

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str = bd.getStringArrayList("data");
        }
        getSupportActionBar().setTitle(str.get(1) + "  Take Attendance");
        // getSupportActionBar().setIcon(R.drawable.home);
        attendanceSheets = getStdntList();
        size = (attendanceSheets.size());
        presenti = new AttendanceSheet[size];
        counter = new boolean[size];
        counterView = (TextView) findViewById(R.id.tv_ed_count);
        counterView.setText(String.valueOf(0));
        // set the array adapter to use the above array list and tell the listview to set as the adapter
        // our custom adapter

        attendanceSheetAdapter = new AttendanceSheetAdapter(getApplicationContext(), R.layout.row_attendance, attendanceSheets);
        list1 = (ListView) findViewById(R.id.take_attendance_list);

        //list1.setItemsCanFocus(false);
        list1.setAdapter(attendanceSheetAdapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView StudentIndex, StudentId, StudentName, StudentRoll, StudentDate, StudentGenId, StudentPresent;
                StudentIndex = (TextView) view.findViewById(R.id.tv_id);
                StudentName = (TextView) view.findViewById(R.id.tv_ViewstudentName);
                StudentRoll = (TextView) view.findViewById(R.id.tv_PRN);
                StudentDate = (TextView) view.findViewById(R.id.blank);
                StudentId = (TextView) view.findViewById(R.id.blank1);

                // ASW.ico=(TextView)view.findViewById(R.id.tv_studicon) ;
                ColorDrawable intID = (ColorDrawable) StudentIndex.getBackground();
                int nt = intID.getColor();
                //  Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();
                if (nt == Color.parseColor("#FBE9E7")) {
                    counter[position] = false;
                    StudentIndex.setBackgroundColor(Color.parseColor("#E8F5E9"));
                    presenti[position] = new AttendanceSheet(StudentId.getText().toString(),
                            StudentRoll.getText().toString(),
                            StudentName.getText().toString(),
                            StudentDate.getText().toString()
                            , 0);
                    count--;
                    counterView.setText(String.valueOf(count));
                    attendanceSheetAdapter.notifyDataSetChanged();
                } else {
                    counter[position] = true;
                    StudentIndex.setBackgroundColor(Color.parseColor("#FBE9E7"));
                    presenti[position] = new AttendanceSheet(StudentId.getText().toString(),
                            StudentRoll.getText().toString(),
                            StudentName.getText().toString(),
                            StudentDate.getText().toString()
                            , 1);
                    count++;
                    counterView.setText(String.valueOf(count));
                    attendanceSheetAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.take_attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_submit:
                Intent intent = new Intent(getApplicationContext(), FinaliseAttendance.class);
                ArrayList<AttendanceSheet> attendance = new ArrayList<AttendanceSheet>(Arrays.asList(presenti));
                intent.putExtra("list_as_string", attendance);
                intent.putStringArrayListExtra("data", str);
                startActivityForResult(intent, ACTIVITY_CONSTANT);
                break;
        }
        return true;
    }

    public ArrayList<AttendanceSheet> getStdntList() {
        ArrayList<AttendanceSheet> Temp = new ArrayList<AttendanceSheet>();
        Cursor cursor = null;
        int id = 0;
        try {

            String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
            Uri list = Uri.parse(URL);
            cursor = getContentResolver().query(list, null, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{str.get(0)}, AttendanceSystemContract.Classes.CLASS_ID);
            if (cursor.moveToFirst())
                do {
                    id = id + 1;
                    String StudentName, StudentRoll, StudentId, StudentDate, StudentPresent;
                    StudentId = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ID));
                    StudentName = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_NAME));
                    StudentRoll = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ROLL));
                    StudentDate = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_DATE));
                    StudentPresent = "0";
                    Temp.add(new AttendanceSheet(StudentId, StudentRoll, StudentName, StudentDate, Integer.valueOf(StudentPresent)));
                } while (cursor.moveToNext());

        } finally {
            cursor.close();
        }
        return Temp;
    }

    public void submit(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_CONSTANT) {
            finish();
        }
    }
}
