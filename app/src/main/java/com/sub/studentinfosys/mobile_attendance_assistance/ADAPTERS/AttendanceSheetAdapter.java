package com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

import static com.sub.studentinfosys.mobile_attendance_assistance.UI.TakeAttendance.counter;

/**
 * Created by Sagar on 1/9/2017.
 */

public class AttendanceSheetAdapter extends ArrayAdapter<AttendanceSheet> {

    Context context1;
    int layoutResourceId1;
    int count = 0;

    ArrayList<AttendanceSheet> attendanceList = new ArrayList<AttendanceSheet>();


    public AttendanceSheetAdapter(Context context, int layoutResourceId, ArrayList<AttendanceSheet> Alist) {
        super(context, layoutResourceId, Alist);
        this.layoutResourceId1 = layoutResourceId;
        this.context1 = context;
        this.attendanceList = Alist;

    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Log.v("Log position", ""+count);
        final AttendanceSheetWrapper ASW;
        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(layoutResourceId1, parent, false);
            ASW = new AttendanceSheetWrapper();
            ASW.id = (TextView) item.findViewById(R.id.tv_id);
            ASW.name = (TextView) item.findViewById(R.id.tv_ViewstudentName);
            ASW.PRN = (TextView) item.findViewById(R.id.tv_PRN);
            ASW.blank = (TextView) item.findViewById(R.id.blank);
            ASW.blank1 = (TextView) item.findViewById(R.id.blank1);
            ASW.ico = (TextView) item.findViewById(R.id.tv_studicon);
            item.setTag(ASW);

        } else {
            ASW = (AttendanceSheetWrapper) item.getTag();
            // count--;
        }
        AttendanceSheet AS = attendanceList.get(position);
        String name, roll, id, date, StudentID, presenti;
        id = String.valueOf(AS.getId());
        name = AS.getStudentName();
        roll = AS.getStudentRoll();
        date = AS.getCreate_on();
        StudentID = AS.getId(); //It is Student_Id
        presenti = String.valueOf(AS.getPresenti());
        //  ASW.id.setText(id);
        ASW.id.setBackgroundColor(Color.parseColor("#E8F5E9"));
        ASW.ico.setBackgroundResource(R.drawable.p);
        ASW.name.setText(name);
        ASW.PRN.setText(roll);
        ASW.blank.setText(date);
        ASW.blank1.setText(StudentID);
        //  ASW.PRN.setId(Integer.parseInt(AS.getStudentPRN()));
        if (counter[position]) {
            ASW.id.setBackgroundColor(Color.parseColor("#FBE9E7"));
            ASW.ico.setBackgroundResource(R.drawable.a);
            attendanceList.set(position, new AttendanceSheet(id, roll, name, date, 1));
            notifyDataSetChanged();
        } else {
            ASW.id.setBackgroundColor(Color.parseColor("#E8F5E9"));
            ASW.ico.setBackgroundResource(R.drawable.p);
            attendanceList.set(position, new AttendanceSheet(id, roll, name, date, 0));
            notifyDataSetChanged();
        }
        return item;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }


    static class AttendanceSheetWrapper {
        TextView id;
        TextView name;
        TextView PRN;
        TextView blank;
        TextView blank1;
        TextView ico;
    }
}
