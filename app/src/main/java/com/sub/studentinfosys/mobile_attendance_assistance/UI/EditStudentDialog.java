package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.StudentDataHandler;

/**
 * Created by Sagar on 2/24/2017.
 */

public class EditStudentDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button yes, no;
    public String StudentName, StudentId, StudentRoll,Class_Id;
    EditText name, roll;
    StudentDataHandler studentDataHandler=new StudentDataHandler();
    public EditStudentDialog(Context c, String StudentName, String StudentId, String StudentRoll,String Class_Id) {
        super(c);
        this.StudentName = StudentName;
        this.StudentId = StudentId;
        this.StudentRoll = StudentRoll;
        this.Class_Id=Class_Id;
        this.c = c;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogbox_update_student);
        yes = (Button) findViewById(R.id.btn_cancel);
        no = (Button) findViewById(R.id.btn_save);
        name = (EditText) findViewById(R.id.stud_name);
        name.setText(StudentName);
        roll = (EditText) findViewById(R.id.stud_roll);
        roll.setText(StudentRoll);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_save:
                ContentValues values = new ContentValues();
                AttendanceSheet studentValidate=studentDataHandler.getStudentDatailes(getContext(),roll.getText().toString(),Class_Id);
               // Toast.makeText(getContext(),studentValidate.getId()+"I am in",Toast.LENGTH_LONG).show();
                if(studentValidate==null)
                {
                    values.put(AttendanceSystemContract.Student.STUDENT_ROLL, roll.getText().toString());
                    values.put(AttendanceSystemContract.Student.STUDENT_NAME, name.getText().toString());
                    c.getContentResolver().update(Class_SubjectProvider.STUDENTS_URI, values,
                            AttendanceSystemContract.Student.STUDENT_ID + "=?", new String[]{StudentId});
                }
                else
                {
                    AlertDialog.Builder alertDialogBuildr = new AlertDialog.Builder(getContext());
                    alertDialogBuildr.setTitle("RollNumber Already Present ");
                    //alertDialogBuilder.setIcon(R.drawable.delete);
                    alertDialogBuildr.setMessage(" Roll Number " + StudentRoll);
                    alertDialogBuildr.setNegativeButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return; //finish();
                        }
                    });

                    AlertDialog alertDialg = alertDialogBuildr.create();
                    alertDialg.show();
                }
                break;
            default:
                break;
        }
        dismiss();
    }
}