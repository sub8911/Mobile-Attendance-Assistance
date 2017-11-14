package com.sub.studentinfosys.mobile_attendance_assistance.DATABASE;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Sagar on 2/19/2017.
 */

public class Class_SubjectProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.sub.studentinfosys.mobile_attendance_assistance.DATABASE";
    public static final Uri CLASS_SUBJECT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_SUBJECTS);
    public static final Uri CLASS_STUDENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS);
    static final String URL_CLASSES = "content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.Classes.CLASS_TABLE;
    public static final Uri CLASS_URI = Uri.parse(URL_CLASSES);
    static final String URL_SUBJECTS = "content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.Subject.SUBJECT_TABLE;
    public static final Uri SUBJECTS_URI = Uri.parse(URL_SUBJECTS);
    static final String URL_STUDENTS = "content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.Student.STUDENT_TABLE;
    public static final Uri STUDENTS_URI = Uri.parse(URL_STUDENTS);
    static final String URL_ATTENDANCE = "content://" + PROVIDER_NAME + "/" + AttendanceSystemContract.Attendance.ATTENDANCE_TABLE;
    public static final Uri ATTENDANCE_URI = Uri.parse(URL_ATTENDANCE);
    /**
     * Constants to identify the requested operation
     */
    private static final int CLASS_LIST = 101;
    private static final int CLASS_LIST_ID = 102;
    private static final int SUBJECT_LIST = 201;
    private static final int SUBJECT_LIST_ID = 202;
    private static final int STUDENT_LIST = 301;
    private static final int STUDENT_LIST_ID = 302;
    private static final int ATTENDANCE_LIST = 401;
    private static final int ATTENDANCE_LIST_ID = 402;
    private static final int CLASS_SUBJECT = 501;
    private static final int CLASS_STUDENT = 502;
    private static final UriMatcher uriMatcher;
    private static HashMap<String, String> CLASSES_SUBJECT_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "class_list", CLASS_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "class_list/#", CLASS_LIST_ID);
        uriMatcher.addURI(PROVIDER_NAME, "subject_list", SUBJECT_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "subject_list/#", SUBJECT_LIST_ID);
        uriMatcher.addURI(PROVIDER_NAME, "student_list", STUDENT_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "student_list/#", STUDENT_LIST_ID);
        uriMatcher.addURI(PROVIDER_NAME, "attendance_list", ATTENDANCE_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "attendance_list/#", ATTENDANCE_LIST_ID);
        uriMatcher.addURI(PROVIDER_NAME, "class_subject", CLASS_SUBJECT);
        uriMatcher.addURI(PROVIDER_NAME, "class_student", CLASS_STUDENT);
    }

    private AttendanceDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public Class_SubjectProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new AttendanceDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID;
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {
            case CLASS_LIST:
                try {
                    rowID = db.insert(AttendanceSystemContract.Classes.CLASS_TABLE, "", values);
                    /**
                     * If record is added successfully
                     *
                     */
                    if (rowID > 0) {
                        _uri = ContentUris.withAppendedId(CLASS_URI, rowID);
                        getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                        return _uri;
                    }

                } catch (SQLiteException ex) {
                    throw new SQLException("Failed to add a record into " + ex);
                }
                break;


            case SUBJECT_LIST:

                rowID = db.insert(AttendanceSystemContract.Subject.SUBJECT_TABLE, "", values);

                /**
                 * If record is added successfully
                 */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(SUBJECTS_URI, rowID);
                    getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);

            case STUDENT_LIST:

                rowID = db.insert(AttendanceSystemContract.Student.STUDENT_TABLE, "", values);

                /**
                 * If record is added successfully
                 */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(STUDENTS_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);

            case ATTENDANCE_LIST:

                rowID = db.insert(AttendanceSystemContract.Attendance.ATTENDANCE_TABLE, "", values);

                /**
                 * If record is added successfully
                 */
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(ATTENDANCE_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);

        }

        return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        String TableName = null;
        SQLiteQueryBuilder qb = null;
        switch (uriMatcher.match(uri)) {
            case CLASS_SUBJECT:
                TableName = AttendanceSystemContract.View_Triggers.VIEW_CLASS_SUBJECTS;
                break;
            case CLASS_LIST:
                TableName = AttendanceSystemContract.Classes.CLASS_TABLE;
                break;
            case CLASS_STUDENT:
                TableName = AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
                break;
            case ATTENDANCE_LIST:
                TableName = AttendanceSystemContract.Attendance.ATTENDANCE_TABLE;
                break;
        }
        qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);
        qb.setProjectionMap(CLASSES_SUBJECT_PROJECTION_MAP);
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = AttendanceSystemContract.Classes.CLASS_ID;
        }
        c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String id;
        switch (uriMatcher.match(uri)) {
            case CLASS_LIST:
                count = db.delete(AttendanceSystemContract.Classes.CLASS_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;

            case CLASS_LIST_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(AttendanceSystemContract.Classes.CLASS_TABLE, AttendanceSystemContract.Classes.CLASS_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;
            case SUBJECT_LIST:
                count = db.delete(AttendanceSystemContract.Subject.SUBJECT_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;

            case SUBJECT_LIST_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(AttendanceSystemContract.Subject.SUBJECT_TABLE, AttendanceSystemContract.Subject.SUBJECT_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;
            case STUDENT_LIST:
                count = db.delete(AttendanceSystemContract.Student.STUDENT_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_STUDENT_URI, null);
                break;

            case STUDENT_LIST_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(AttendanceSystemContract.Student.STUDENT_TABLE, AttendanceSystemContract.Student.STUDENT_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_STUDENT_URI, null);
                break;
            case ATTENDANCE_LIST:
                count = db.delete(AttendanceSystemContract.Attendance.ATTENDANCE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.ATTENDANCE_URI, null);
                break;

            case ATTENDANCE_LIST_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(AttendanceSystemContract.Attendance.ATTENDANCE_TABLE, AttendanceSystemContract.Attendance.ATTENDANCE_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.ATTENDANCE_URI, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CLASS_LIST:
                count = db.update(AttendanceSystemContract.Classes.CLASS_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;

            case CLASS_LIST_ID:
                count = db.update(AttendanceSystemContract.Classes.CLASS_TABLE, values,
                        AttendanceSystemContract.Classes.CLASS_ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;
            case SUBJECT_LIST:
                count = db.update(AttendanceSystemContract.Subject.SUBJECT_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;

            case SUBJECT_LIST_ID:
                count = db.update(AttendanceSystemContract.Subject.SUBJECT_TABLE, values,
                        AttendanceSystemContract.Subject.SUBJECT_ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_SUBJECT_URI, null);
                break;
            case STUDENT_LIST:
                count = db.update(AttendanceSystemContract.Student.STUDENT_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_STUDENT_URI, null);
                break;

            case STUDENT_LIST_ID:
                count = db.update(AttendanceSystemContract.Student.STUDENT_TABLE, values,
                        AttendanceSystemContract.Student.STUDENT_ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.CLASS_STUDENT_URI, null);
                break;
            case ATTENDANCE_LIST:
                count = db.update(AttendanceSystemContract.Attendance.ATTENDANCE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.ATTENDANCE_URI, null);
                break;

            case ATTENDANCE_LIST_ID:
                count = db.update(AttendanceSystemContract.Attendance.ATTENDANCE_TABLE, values,
                        AttendanceSystemContract.Attendance.ATTENDANCE_ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                getContext().getContentResolver().notifyChange(AttendanceSystemContract.ATTENDANCE_URI, null);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

            case CLASS_LIST:
                return "vnd.android.cursor.dir/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.class_list";

            case CLASS_LIST_ID:
                return "vnd.android.cursor.item/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.class_list";
            case SUBJECT_LIST:

            case SUBJECT_LIST_ID:
                return "vnd.android.cursor.item/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.subject_list";
            case STUDENT_LIST:
                return "vnd.android.cursor.dir/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.student_list";

            case STUDENT_LIST_ID:
                return "vnd.android.cursor.item/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.student_list";
            case ATTENDANCE_LIST:
                return "vnd.android.cursor.dir/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.attendance_list";

            case ATTENDANCE_LIST_ID:
                return "vnd.android.cursor.item/com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.attendance_list";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


}