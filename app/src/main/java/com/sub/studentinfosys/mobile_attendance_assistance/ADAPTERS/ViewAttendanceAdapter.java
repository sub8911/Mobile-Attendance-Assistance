package com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

/**
 * Created by Sagar on 2/24/2017.
 */

public class ViewAttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int position;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    public Cursor mCursor_attendance;
    protected Context mContext_attendance;
    protected boolean mAutoRequery_class;
    protected boolean mDataValid_attendance;
    protected int mRowIDColumn_attendance;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver = new MyDataSetObserver();
    View view = null;
    ClickTransferInterface obj;

    public ViewAttendanceAdapter(Context context, Cursor cursor, boolean autoRequery, ClickTransferInterface obj) {
        super();
        this.obj = obj;
        init(context, cursor, autoRequery);
    }

    protected void init(Context context, Cursor c, boolean autoRequery) {
        boolean cursorPresent = c != null;
        mAutoRequery_class = autoRequery;
        mCursor_attendance = c;
        mDataValid_attendance = cursorPresent;
        mContext_attendance = context;
        mRowIDColumn_attendance = cursorPresent ? c.getColumnIndexOrThrow("class_id") : -1;
        mChangeObserver = new ChangeObserver();
        if (cursorPresent) {
            c.registerContentObserver(mChangeObserver);
            c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor_attendance;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int type) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_layout_attendance, parent, false);
        ListHeaderViewHolder holdr = new ListHeaderViewHolder(view);
        return holdr;
    }

// Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final String AttendanceDate, ClassId, SubjectId, AttendanceId;
        mCursor_attendance.moveToPosition(position);
        final Cursor c = mCursor_attendance;

        AttendanceDate = mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_CREATE_ON));
        AttendanceId = mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_ID));
        SubjectId = mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.SUBJECT_ID));
        ClassId = mCursor_attendance.getString(mCursor_attendance.getColumnIndex(AttendanceSystemContract.Attendance.CLASS_ID));

        final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
        itemController.attendance_date.setText(AttendanceDate);
        itemController.attendance_id.setText(AttendanceId);
        itemController.subject_id.setText(SubjectId);
        itemController.class_id.setText(ClassId);
        itemController.cv.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                c.moveToPosition(position);
                obj.setValues(c);

            }
        });
        itemController.cv.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Delete Class Attendance Record ");
                //alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Do you want to Delete Record on Date " + AttendanceDate);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(mContext_attendance, "", Toast.LENGTH_SHORT).show();
                                int attendance = mContext_attendance.getContentResolver().delete(Class_SubjectProvider.ATTENDANCE_URI, AttendanceSystemContract.Attendance.ATTENDANCE_ID + "=?", new String[]{AttendanceId});
                                Toast.makeText(mContext_attendance, "Attendance Record of date " + AttendanceDate + " Deleted", Toast.LENGTH_LONG).show();
                                notifyItemRemoved(position);
                                return;
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return; //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }

        });


    }

    @Override
    public int getItemCount() {
        if (mDataValid_attendance && mCursor_attendance != null) {
            return mCursor_attendance.getCount();
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
        if (newCursor == mCursor_attendance) {
            return null;
        }
        Cursor oldCursor = mCursor_attendance;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor_attendance = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn_attendance = newCursor.getColumnIndexOrThrow("class_id");
            mDataValid_attendance = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn_attendance = -1;
            mDataValid_attendance = false;
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
        if (mAutoRequery_class && mCursor_attendance != null && !mCursor_attendance.isClosed()) {
            mDataValid_attendance = mCursor_attendance.requery();
        }
    }

    public Cursor getItem(int position) {
        if (mCursor_attendance != null) {
            mCursor_attendance.moveToPosition(position);
            return mCursor_attendance;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mCursor_attendance != null) {
            if (mCursor_attendance.moveToPosition(position)) {
                return mCursor_attendance.getLong(mRowIDColumn_attendance);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView attendance_date;
        public TextView attendance_id;
        public TextView subject_id;
        public TextView class_id;
        public CardView cv;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            attendance_id = (TextView) itemView.findViewById(R.id.child_hide_id_attendance);
            attendance_date = (TextView) itemView.findViewById(R.id.child_date_attendance);
            subject_id = (TextView) itemView.findViewById(R.id.child_subject_id_attendance);
            class_id = (TextView) itemView.findViewById(R.id.child_class_id_attendance);
            cv = (CardView) itemView.findViewById(R.id.cv_child);


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
            mDataValid_attendance = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid_attendance = false;
            notifyDataSetInvalidated();
        }
    }


}