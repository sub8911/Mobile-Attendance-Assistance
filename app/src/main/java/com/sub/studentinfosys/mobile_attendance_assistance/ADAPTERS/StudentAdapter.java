package com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.UI.EditStudentDialog;

/**
 * Created by Sagar on 2/23/2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 1;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    public Cursor mCursor_stud;
    protected Context mContext_stud;
    protected boolean mAutoRequery_stud;
    protected boolean mDataValid;
    protected int mRowIDColumn;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver = new MyDataSetObserver();
    View view = null;


    public StudentAdapter(Context context, Cursor c) {

        init(context, c, true);
    }

    public StudentAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super();
        init(context, cursor, autoRequery);
    }

    protected void init(Context context, Cursor c, boolean autoRequery) {
        boolean cursorPresent = c != null;
        mAutoRequery_stud = autoRequery;
        mCursor_stud = c;
        mDataValid = cursorPresent;
        mContext_stud = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("class_id") : -1;
        mChangeObserver = new ChangeObserver();
        if (cursorPresent) {
            c.registerContentObserver(mChangeObserver);
            c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor_stud;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int type) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
// create a new view

        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.card_layout_students, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
        }
        return null;
    }

// Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor_stud.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
// - get element from your dataset at this position
// - replace the contents of the view with that element
        final Cursor cursor = getItem(position);

        switch (cursor.getInt(cursor.getColumnIndex(AttendanceSystemContract.Student.ROW_TYPE))) {
            case HEADER:
                final String StudentName, StudentId, StudentCreatedOn, StudentRoll,class_Id;
                StudentName = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_NAME));
                StudentRoll = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ROLL));
                StudentId = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_ID));
                StudentCreatedOn = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.STUDENT_DATE));
                class_Id= cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Student.CLASS_ID));
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.StudentName.setText(StudentName);
                itemController.StudentRoll.setText(StudentRoll);
                itemController.createdOnDate.setText(StudentCreatedOn);
                itemController.hideid.setText(StudentId);
                itemController.cv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        /*ArrayList<String> str=new ArrayList<String>();
                        Intent intent = new Intent(mContext_stud, StudentActivity.class);
                        intent.putStringArrayListExtra("data", str);
                        mContext_stud.startActivity(intent);*/
                    }
                });
                itemController.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditStudentDialog cdd = new EditStudentDialog(view.getContext(), StudentName, StudentId, StudentRoll,class_Id);
                        cdd.show();
                    }
                });
                itemController.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialogBuildr = new AlertDialog.Builder(view.getContext());
                        alertDialogBuildr.setTitle("Delete Student " + StudentName);
                        //alertDialogBuilder.setIcon(R.drawable.delete);
                        alertDialogBuildr.setMessage("Do you want to Delete Student " + StudentName);
                        alertDialogBuildr.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        ContentValues values = new ContentValues();
                                        values.put(AttendanceSystemContract.Student.STUDENT_STATUS, "1");
                                        mContext_stud.getContentResolver().update(Class_SubjectProvider.STUDENTS_URI, values,
                                                AttendanceSystemContract.Student.STUDENT_ID + "=?", new String[]{StudentId});
                                        /*int attendances=mContext_stud.getContentResolver().delete(Class_SubjectProvider.ATTENDANCE_URI,AttendanceSystemContract.Subject.SUBJECT_ID+"=?", new String[]{StudentId});*/
                                        Toast.makeText(mContext_stud, StudentName + " Deleted ", Toast.LENGTH_LONG).show();

                                        return;
                                    }
                                });

                        alertDialogBuildr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return; //finish();
                            }
                        });

                        AlertDialog alertDialg = alertDialogBuildr.create();
                        alertDialg.show();

                    }
                });
        }
    }

    /*@Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return view;
     }*/
    @Override
    public int getItemViewType(int position) {
        mCursor_stud.moveToPosition(position);
        return mCursor_stud.getInt(mCursor_stud.getColumnIndex("row_type"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataValid && mCursor_stud != null) {
            return mCursor_stud.getCount();
        } else {
            return 0;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor_stud) {
            return null;
        }
        Cursor oldCursor = mCursor_stud;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor_stud = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("class_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    protected void onContentChanged() {
        if (mAutoRequery_stud && mCursor_stud != null && !mCursor_stud.isClosed()) {
            mDataValid = mCursor_stud.requery();
        }
    }

    public Cursor getItem(int position) {
        if (mCursor_stud != null) {
            mCursor_stud.moveToPosition(position);
            return mCursor_stud;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mCursor_stud != null) {
            if (mCursor_stud.moveToPosition(position)) {
                return mCursor_stud.getLong(mRowIDColumn);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView StudentName;
        public TextView createdOnDate;
        public TextView StudentRoll;
        public TextView hideid;
        public ImageButton btn_delete;
        public ImageButton btn_edit;
        public CardView cv;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            StudentName = (TextView) itemView.findViewById(R.id.student_name);
            createdOnDate = (TextView) itemView.findViewById(R.id.fr_students_created_on);
            hideid = (TextView) itemView.findViewById(R.id.hide_id);
            StudentRoll = (TextView) itemView.findViewById(R.id.student_roll);
            btn_delete = (ImageButton) itemView.findViewById(R.id.btn_delete_stud);
            btn_edit = (ImageButton) itemView.findViewById(R.id.btn_edit_stud);
            cv = (CardView) itemView.findViewById(R.id.cv_students);
        }
    }

    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }

}
