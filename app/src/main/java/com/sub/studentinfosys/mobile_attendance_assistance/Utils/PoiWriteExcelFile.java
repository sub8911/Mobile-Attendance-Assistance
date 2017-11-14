package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PoiWriteExcelFile {
    Context context;
    ArrayList<String> str;
    String SubjectName, SubjectId, ClassName, ClassId;
    int lec;
    public PoiWriteExcelFile(Context context, ArrayList<String> str) {
        this.context = context;
        this.str = str;
    }

    public void exportToExcel() {
        try {

            SubjectName = str.get(3);
            SubjectId = str.get(2);
            ClassName = str.get(1);
            ClassId = str.get(0);
            File folder;
            //Create Folder
            if (isExternalStoragePresent()) {
                folder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "/Attendance/" + ClassName);
                folder.mkdirs();
            } else {
                folder = new File(Environment.getDataDirectory().toString() + File.separator + "/Attendance/" + ClassName);
                folder.mkdirs();
            }
            //Save the path as a string value
            String extStorageDirectory = folder.getAbsolutePath().toString();
            FileOutputStream fileOut;
            String filename = SubjectName;
            fileOut = new FileOutputStream(extStorageDirectory + "/" + filename + ".xls", false);


            Toast.makeText(context, "" + extStorageDirectory + "/" + filename, Toast.LENGTH_SHORT).show();

            Cursor mCursor_students = context.getContentResolver().query(Class_SubjectProvider.CLASS_STUDENT_URI, null, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{ClassId}, AttendanceSystemContract.Student.CLASS_ID);
            Cursor mCursor_attendance = context.getContentResolver().query(Class_SubjectProvider.ATTENDANCE_URI, null, AttendanceSystemContract.Attendance.SUBJECT_ID + "=?", new String[]{SubjectId}, AttendanceSystemContract.Attendance.SUBJECT_ID);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet(filename);
            // index from 0,0... cell A1 is cell(0,0)

            // HSSFRow row = worksheet.createRow(mCursor_students.getCount()+2);
            int count = 1;
            String Date = null;
            Row row = worksheet.createRow(0);
            if (mCursor_attendance.moveToFirst()) {
                do {
                    lec=Integer.valueOf(mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES)));
                    for (int i = 1; i <= lec; i++) {
                        Cell cellA1 = row.createCell(++count);
                        Date = mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON));
                        //  Toast.makeText(context, "" + mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON)), Toast.LENGTH_SHORT).show();
                       if(Date.contains(",")) {
                           cellA1.setCellValue(Date.substring(0, Date.indexOf(',')));
                       }
                       else
                       {
                           cellA1.setCellValue(Date);
                       }
                        HSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
                        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        cellA1.setCellStyle(cellStyle);
                    }
                } while (mCursor_attendance.moveToNext());
            }

            int row_count = 0;

            if (mCursor_students.moveToFirst()) {

                do {
                    int cell_counter=0;
                        String Roll = mCursor_students.getString(mCursor_students.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ROLL)),
                                Name = mCursor_students.getString(mCursor_students.getColumnIndex(AttendanceSystemContract.Student.STUDENT_NAME));
                        Row row1 = worksheet.createRow(++row_count);
                        Cell cellA2 = row1.createCell(cell_counter++);
                        cellA2.setCellValue(Roll);
                        HSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
                        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        cellA2.setCellStyle(cellStyle);

                        Cell cellA3 = row1.createCell(cell_counter++);
                        cellA3.setCellValue(Name);
                        HSSFCellStyle cellStyle1 = workbook.createCellStyle();
                        cellStyle1.setFillForegroundColor(HSSFColor.GOLD.index);
                        cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        cellA2.setCellStyle(cellStyle1);


                    for(int i=2;i<mCursor_attendance.getCount()+2;i++)
                    {
                        mCursor_attendance.moveToPosition(i-2);
                        int lec=Integer.valueOf(mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_NUMBER_OF_LECTURES)));
                        String temp= mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_ABSENT_LIST));
                        CursorManipulation sr=new CursorManipulation();
                        ArrayList<AttendanceSheet> HasAbsentList=sr.getAbsentDetails(temp,context,ClassId);
                        String setAttendance = "P";
                        if(HasAbsentList != null) {
                            HasAbsentList.iterator();

                            for (AttendanceSheet std : HasAbsentList) {
                                if (std.getStudentRoll().equals(Roll)) {
                                    setAttendance = "A";
                                    break;
                                } else {
                                    setAttendance = "P";
                                }
                            }
                        }
                        int h=0;
                        while(h<lec) {
                            Cell cell = row1.createCell(cell_counter++);
                            cell.setCellValue(setAttendance);
                            HSSFCellStyle cellStyle4 = workbook.createCellStyle();
                            cellStyle4.setFillForegroundColor(HSSFColor.GOLD.index);
                            cellStyle4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            cellA2.setCellStyle(cellStyle4);
                            h++;
                        }
                       // cell_counter=lec-1;
                    }
                } while (mCursor_students.moveToNext());
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //int n=context.getContentResolver().delete(Class_SubjectProvider.ATTENDANCE_URI, AttendanceSystemContract.Attendance.SUBJECT_ID+"=?", new String[]{SubjectId});
        // Toast.makeText(context, "Records SuccessFully Copied To Excel and Deleted from AppDatabase"+n, Toast.LENGTH_SHORT).show();
    }

    private boolean isExternalStoragePresent() {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {
            Toast.makeText(context, "SD card not present", Toast.LENGTH_LONG)
                    .show();

        }
        return (mExternalStorageAvailable) && (mExternalStorageWriteable);
    }

    public void updateToExcel(int colCount, String SubjectId) {
        ContentValues values = new ContentValues();
        values.put(AttendanceSystemContract.Subject.SUBJECT_ID, SubjectId);
        values.put(AttendanceSystemContract.Subject.SUBJECT_EXCEL, colCount);
        context.getContentResolver().update(Class_SubjectProvider.SUBJECTS_URI, values,
                AttendanceSystemContract.Subject.SUBJECT_ID + "=?", new String[]{SubjectId});
    }
}