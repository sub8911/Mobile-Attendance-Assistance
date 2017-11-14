package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.EditAttendanceAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.StudentDataHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider.ATTENDANCE_URI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link View_Attendance_fragment.OnFragmentViewInteractionListener} interface
 * to handle interaction events.
 * Use the {@link View_Attendance_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class View_Attendance_fragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "Attendance_Fragment";
    private static final String ARG_PARAM2 = "param2";
    private static final long CLICK_TIME_INTERVAL = 300;
    public static boolean[] countr;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int yr,day,month;
    ArrayList<String> str = new ArrayList<String>();
    EditAttendanceAdapter adapter;
    int attendance_id, size;
    AttendanceSheet[] presenti;
    int attendanceId, count;
    static final int DIALOG_ID=0;
    String[] crtdOn;
    View v;
    String AbsentStudentList = "", class_id, subject_id, created_on, number_of_lectures, absent_stdnt_list = "";
    ArrayList<AttendanceSheet> attendance = new ArrayList<AttendanceSheet>();
    ArrayList<AttendanceSheet> ASL = new ArrayList<AttendanceSheet>();
    TextView taken_on;
    Spinner num_of_lectures;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    // Identifies a particular Loader being used in this component
    private long mLastClickTime = System.currentTimeMillis();
    private OnFragmentViewInteractionListener mListener;

    public View_Attendance_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment View_Attendance_fragment.
     */

    public static View_Attendance_fragment newInstance(String param1, String param2) {
        View_Attendance_fragment fragment = new View_Attendance_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        Bundle args = getArguments();
        if (args != null) {
            str = args.getStringArrayList("index");
            attendance_id = args.getInt("attendance_id");
        }
        Calendar today=Calendar.getInstance();
        yr=today.get(Calendar.YEAR);
        day=today.get(Calendar.DAY_OF_MONTH);
        month=today.get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view__attendance, container, false);
        taken_on=(TextView) v.findViewById(R.id.btn_cng_date);
        num_of_lectures=(Spinner) v.findViewById(R.id.sp_lectures);
        attendance = getPresentStdntList();
        getActivity().invalidateOptionsMenu();
        if(created_on.contains(",")) {
            taken_on.setText(created_on.substring(0, created_on.indexOf(',')));
            crtdOn = created_on.substring(0, created_on.indexOf(',')).split(" ");
        }
        else
        {
            taken_on.setText(created_on);
            crtdOn = created_on.split(" ");
        }
        taken_on.setOnClickListener(this);
        num_of_lectures.setSelection(Integer.valueOf(number_of_lectures)-1);

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview_view_attendance);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        recyclerview.setLayoutManager(glm);
        adapter = new EditAttendanceAdapter(getContext(), attendance);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(
                new MyRecyclerItemClickListener(getContext(), new MyRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        long now = System.currentTimeMillis();
                        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                            return;
                        }
                        mLastClickTime = now;
                        TextView a, b, c, d,edit_roll;
                        edit_roll = (TextView) view.findViewById(R.id.edit_roll_number);
                        a = (TextView) view.findViewById(R.id.a);
                        b = (TextView) view.findViewById(R.id.b);
                        c = (TextView) view.findViewById(R.id.c);
                        d = (TextView) view.findViewById(R.id.d);
                        String StudentId = a.getText().toString();
                        String StudentName = b.getText().toString();
                        String CreatedOn = c.getText().toString();
                        String StudentRoll = edit_roll.getText().toString();
                        Log.i(TAG, "Clicked Item Position : " + position);
                        ColorDrawable intID = (ColorDrawable) edit_roll.getBackground();
                        int nt = intID.getColor();
                        if (nt == Color.parseColor("#df464b")) {
                            countr[position] = true;

                            attendance.get(position).setPresenti(1);
                            presenti[position] = new AttendanceSheet(StudentId,
                                    StudentRoll,
                                    StudentName,
                                    CreatedOn,
                                    0);
                           /* Toast.makeText(getContext(), "("+StudentId+",\n" +
                                    "                                    "+StudentRoll+",\n" +
                                    "                                    "+StudentName+",\n" +
                                    "                                   "+ CreatedOn+","+0, Toast.LENGTH_SHORT).show();*/
                            edit_roll.setBackgroundColor(Color.parseColor("#59a450"));
                            d.setBackgroundColor(Color.parseColor("#59a450"));
                            adapter.notifyDataSetChanged();

                        } else {
                            countr[position] = false;
                            attendance.get(position).setPresenti(0);
                            presenti[position] = new AttendanceSheet(StudentId,
                                    StudentRoll,
                                    StudentName,
                                    CreatedOn,
                                    1);
                           /* Toast.makeText(getContext(), "("+StudentId+",\n" +
                                    "                                    "+StudentRoll+",\n" +
                                    "                                    "+StudentName+",\n" +
                                    "                                   "+ CreatedOn+","+1, Toast.LENGTH_SHORT).show();*/
                            edit_roll.setBackgroundColor(Color.parseColor("#df464b"));
                            d.setBackgroundColor(Color.parseColor("#df464b"));
                            adapter.notifyDataSetChanged();
                        }
                    }

                })
        );

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentViewInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentViewInteractionListener) {
            mListener = (OnFragmentViewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_cng_date:
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                taken_on.setText(dayOfMonth + " "
                                        + MONTHS[monthOfYear] + " " + year);

                            }
                        },
                        Integer.valueOf(crtdOn[2]), indexOfMonth(crtdOn[1]), Integer.valueOf(crtdOn[0]));
               dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                        }
                    }
                });
                dpd.show();
                break;
        }

    }

    public int indexOfMonth(String str)
    {
        int index=0;
        for(int i=0;i<12;i++)
        {
            if(MONTHS[i].contains(str))
            {
                return i;
            }
        }
        return index;
    }

    public ArrayList<AttendanceSheet> getStdntList() {
        ArrayList<AttendanceSheet> Temp = new ArrayList<AttendanceSheet>();
        Cursor cursor = null;
        int id = 0;
        try {

            String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
            Uri list = Uri.parse(URL);
            cursor = getActivity().getContentResolver().query(list, null, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{str.get(0)}, AttendanceSystemContract.Classes.CLASS_ID);
            int n = cursor.getCount();
            countr = new boolean[n];
            presenti = new AttendanceSheet[n];
            if (cursor.moveToFirst())
                do {

                    countr[id++] = true;
                    int StudentPresent;
                    String StudentName, StudentRoll, StudentId, StudentDate;
                    StudentId = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ID));
                    StudentName = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_NAME));
                    StudentRoll = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ROLL));
                    StudentDate = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_DATE));
                    StudentPresent = 0;
                    Temp.add(new AttendanceSheet(StudentId, StudentRoll, StudentName, StudentDate, StudentPresent));
                } while (cursor.moveToNext());

        } finally {
            //  cursor.close();
        }

        return Temp;
    }

    public ArrayList<AttendanceSheet> getPresentStdntList() {
        ArrayList<AttendanceSheet> PSL;
        PSL = getStdntList();
        Cursor cursor;
        StudentDataHandler studentDataHandler=new StudentDataHandler();
        cursor = getActivity().getContentResolver().query(ATTENDANCE_URI, null, AttendanceSystemContract.Attendance.ATTENDANCE_ID + "=?", new String[]{"" + attendance_id}, null);
        if (cursor.moveToFirst())
            do {

                attendanceId = cursor.getInt(cursor.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_ID));
                class_id = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Attendance.CLASS_ID));
                subject_id = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Attendance.SUBJECT_ID));
                created_on = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON));
                number_of_lectures= cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES));
                absent_stdnt_list = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_ABSENT_LIST));
               // Toast.makeText(getContext(),absent_stdnt_list,Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());

        //split the string using separator, in this case it is ","
        try {
            if (!(absent_stdnt_list =="")) {
                Toast.makeText(getContext(),absent_stdnt_list+" Absent Students",Toast.LENGTH_LONG).show();
                String[] strValues = absent_stdnt_list.split(",");
               // Toast.makeText(getContext(),strValues.length+" Absent Students",Toast.LENGTH_LONG).show();
                /*
                 * Use asList method of Arrays class to convert Java String array to ArrayList
                 */


//                AttendanceSheet temp = studentDataHandler.getStudentDatailes(getContext(),strValues[1],class_id);
//                if(temp!=null)
//                Toast.makeText(getContext(),temp.getStudentRoll(),Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(getContext(),"false",Toast.LENGTH_LONG).show();

                int j=0;
               do
                {
                    //String[] str1 = hold.split("/@@");
                    AttendanceSheet temp = studentDataHandler.getStudentDatailes(getContext(),strValues[j],class_id);
                // Toast.makeText(getContext(),temp.getStudentRoll(),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < PSL.size(); i++) {
                    //    Toast.makeText(getContext(),PSL.get(i).getStudentRoll()+"  77 "+ temp.getStudentRoll() ,Toast.LENGTH_LONG).show();
                        if (PSL.get(i).getStudentRoll().equals(temp.getStudentRoll())) {
                            countr[i] = false;
                      //      Toast.makeText(getContext(),PSL.get(i).getStudentRoll()+"=="+temp.getStudentRoll() +" I am in if",Toast.LENGTH_LONG).show();
                            PSL.set(i, temp);
                        }
                        presenti[i] = PSL.get(i);
                    }
                    ASL.add(temp);
                    j++;
                }while(j<strValues.length);
            }
        } catch (Exception e) {
            return PSL;
        }

        return PSL;
    }

    public void submit_edit() {
        String rolls = null;

        final ContentValues cv = new ContentValues();
        for (int i = 0; i < presenti.length; i++) {

            //AttendanceSheet std1=std.iterator().next();
            if (presenti[i] != null) {
              //  Toast.makeText(getContext(), " //  "+presenti[i].getStudentRoll(), Toast.LENGTH_SHORT).show();
                if (presenti[i].getPresenti() == 1) {

                    count++;
                 //   Toast.makeText(getContext(),""+presenti[i].getPresenti(),Toast.LENGTH_LONG).show();
                    if (AbsentStudentList.equals("") && presenti[i].getId() != null) {
                        rolls = "" + presenti[i].getStudentRoll();
                        AbsentStudentList = presenti[i].getStudentRoll() + "," ;
                    } else {
                        rolls = rolls + ",\n" + presenti[i].getStudentRoll();
                        AbsentStudentList = AbsentStudentList +  presenti[i].getStudentRoll() + "," ;
                    }
                }
            }
        }
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON,taken_on.getText().toString());
        String temp = num_of_lectures.getSelectedItem().toString();
        // Toast.makeText(getApplicationContext(),""+temp,Toast.LENGTH_LONG).show();
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES, temp);
        cv.put(AttendanceSystemContract.Attendance.ATTENDANCE_ABSENT_LIST, AbsentStudentList);
        Log.e("" + AbsentStudentList, "");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Edit Attendance Record of " + str.get(3));
        // alertDialogBuilder.setIcon(R.drawable.edit).;
        if (rolls != null)
            alertDialogBuilder.setMessage("Following Roll numbers will be Saved as Absent \n" + rolls);
        else
            alertDialogBuilder.setMessage("All Student Present");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        getContext().getContentResolver().update(ATTENDANCE_URI, cv,
                                AttendanceSystemContract.Attendance.ATTENDANCE_ID + "=?", new String[]{String.valueOf(attendance_id)});
                        callFragment();

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callFragment();
                return; //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void callFragment() {
        getActivity().onBackPressed();
    }

    public interface OnFragmentViewInteractionListener {

        void onFragmentViewInteraction(Uri uri);
    }
}
