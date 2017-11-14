package com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.UI.AttendanceActivity;
import com.sub.studentinfosys.mobile_attendance_assistance.UI.StudentActivity;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.DateUtils;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.PoiWriteExcelFile;

import java.util.ArrayList;

/**
 * Created by Sagar on 2/20/2017.
 */

public class ClassSubjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    public Cursor mCursor_class;
    protected Context mContext_class;
    protected boolean mAutoRequery_class;
    protected boolean mDataValid_class;
    protected int mRowIDColumn_class;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver = new MyDataSetObserver();


    public ClassSubjectListAdapter(Context context, Cursor c) {

        init(context, c, true);
    }

    public ClassSubjectListAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super();
        init(context, cursor, autoRequery);
    }

    protected void init(Context context, Cursor c, boolean autoRequery) {
        boolean cursorPresent = c != null;
        mAutoRequery_class = autoRequery;
        mCursor_class = c;
        mDataValid_class = cursorPresent;
        mContext_class = context;
        mRowIDColumn_class = cursorPresent ? c.getColumnIndexOrThrow("class_id") : -1;
        mChangeObserver = new ChangeObserver();
        if (cursorPresent) {
            c.registerContentObserver(mChangeObserver);
            c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor_class;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int type) {
        if (!mDataValid_class) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
// create a new view
        View view = null;
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.card_layout_classe_subject, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                LayoutInflater childinflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = childinflater.inflate(R.layout.list_child, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

// Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!mDataValid_class) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor_class.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
// - get element from your dataset at this position
// - replace the contents of the view with that element
        final Cursor cursor = getItem(position);

        switch (cursor.getInt(cursor.getColumnIndex(AttendanceSystemContract.Subject.ROW_TYPE))) {
            case HEADER:
                final String ClassName, ClassId, ClassCreatedOn;
                cursor.moveToPosition(position);
                ClassName = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_NAME));
                ClassId = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_ID));
                ClassCreatedOn = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_DATE));

                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.header_title.setText(ClassName);
                itemController.createdOnDate.setText(ClassCreatedOn);
                itemController.hideid.setText(ClassId);
                itemController.btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        Log.v("Add Subject ", "Clicked");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                        alertDialogBuilder.setMessage("Enter Subject name for Class " + ClassName);
                        alertDialogBuilder.setTitle("Add Subject");
                        //alertDialogBuilder.setIcon(R.drawable.add);
                        final EditText input = new EditText(v.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        alertDialogBuilder.setView(input);
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String CreatedON = DateUtils.getDate();
                                        ContentValues values = new ContentValues();
                                        values.put(AttendanceSystemContract.Subject.CLASS_ID, ClassId);
                                        values.put(AttendanceSystemContract.Subject.SUBJECT_NAME, input.getText().toString());
                                        values.put(AttendanceSystemContract.Subject.SUBJECT_DATE, CreatedON);
                                        mContext_class.getContentResolver().insert(Class_SubjectProvider.SUBJECTS_URI, values);

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
                    }
                });
                itemController.btn_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Log.v("Delete ", "Clicked");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                        alertDialogBuilder.setTitle("Delete Class " + ClassName);
                        //alertDialogBuilder.setIcon(R.drawable.delete);
                        alertDialogBuilder.setMessage("Do you want to Delete Class " + ClassName);
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        mContext_class.getContentResolver().delete(Class_SubjectProvider.CLASS_URI,
                                                AttendanceSystemContract.Classes.CLASS_ID + "=?", new String[]{ClassId});
                                        int subjects = mContext_class.getContentResolver().delete(Class_SubjectProvider.SUBJECTS_URI, AttendanceSystemContract.Subject.CLASS_ID + "=?", new String[]{ClassId});
                                        int students = mContext_class.getContentResolver().delete(Class_SubjectProvider.STUDENTS_URI, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{ClassId});
                                        int attendance = mContext_class.getContentResolver().delete(Class_SubjectProvider.ATTENDANCE_URI, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{ClassId});
                                        Toast.makeText(mContext_class, ClassName + " Deleted Subjects = " + subjects + " And Attendance Record =" + attendance, Toast.LENGTH_LONG).show();

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
                    }
                });

                itemController.btn_Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Log.v("Edit ", "Clicked");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                        alertDialogBuilder.setTitle("Edit Name of " + ClassName);
                        // alertDialogBuilder.setIcon(R.drawable.edit).;
                        alertDialogBuilder.setMessage("Enter Updated name for Class " + ClassName);
                        final EditText input = new EditText(v.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        alertDialogBuilder.setView(input);
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        ContentValues values = new ContentValues();
                                        values.put(AttendanceSystemContract.Subject.CLASS_ID, ClassId);
                                        values.put(AttendanceSystemContract.Classes.CLASS_NAME, input.getText().toString());
                                        mContext_class.getContentResolver().update(Class_SubjectProvider.CLASS_URI, values,
                                                AttendanceSystemContract.Classes.CLASS_ID + "=?", new String[]{ClassId});
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
                    }
                });

                itemController.btn_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> str = new ArrayList<String>();
                        str.add(0, ClassId);
                        str.add(1, ClassName);
                        str.add(2, ClassCreatedOn);

                        Intent intent = new Intent(mContext_class, StudentActivity.class);
                        intent.putStringArrayListExtra("data", str);
                        mContext_class.startActivity(intent);
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder childItemController = (ListChildViewHolder) holder;
                final String SubjectName, SubjectId, SubjectCreatedOn;
                cursor.moveToPosition(position);
                SubjectName = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_NAME));
                SubjectId = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_ID));
                SubjectCreatedOn = cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_DATE));
                childItemController.childhideid.setText(SubjectId);
                childItemController.childsubjectname.setText(SubjectName);
                childItemController.childcreatedon.setText(SubjectCreatedOn);
                childItemController.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(childItemController.action.getContext(), childItemController.action);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.actions);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_edit:
                                        //handle menu2 click

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext_class);
                                        alertDialogBuilder.setTitle("Edit Name" + SubjectName);
                                        // alertDialogBuilder.setIcon(R.drawable.edit).;
                                        alertDialogBuilder.setMessage("Enter Updated name for Subject " + SubjectName);
                                        final EditText input = new EditText(mContext_class);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        input.setLayoutParams(lp);

                                        alertDialogBuilder.setView(input);
                                        alertDialogBuilder.setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {

                                                        ContentValues values = new ContentValues();
                                                        values.put(AttendanceSystemContract.Subject.SUBJECT_ID, SubjectId);
                                                        values.put(AttendanceSystemContract.Subject.SUBJECT_NAME, input.getText().toString());
                                                        mContext_class.getContentResolver().update(Class_SubjectProvider.SUBJECTS_URI, values,
                                                                AttendanceSystemContract.Subject.SUBJECT_ID + "=?", new String[]{SubjectId});
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

                                        break;
                                    case R.id.menu_delete:
                                        //handle menu3 click
                                        AlertDialog.Builder alertDialogBuildr = new AlertDialog.Builder(mContext_class);
                                        alertDialogBuildr.setTitle("Delete Subject " + SubjectName);
                                        //alertDialogBuilder.setIcon(R.drawable.delete);
                                        alertDialogBuildr.setMessage("Do you want to Delete Subject " + SubjectName);
                                        alertDialogBuildr.setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        mContext_class.getContentResolver().delete(Class_SubjectProvider.SUBJECTS_URI,
                                                                AttendanceSystemContract.Subject.SUBJECT_ID + "=?", new String[]{SubjectId});
                                                        int subjects = mContext_class.getContentResolver().delete(Class_SubjectProvider.ATTENDANCE_URI, AttendanceSystemContract.Subject.SUBJECT_ID + "=?", new String[]{SubjectId});

                                                        Toast.makeText(mContext_class, SubjectName + " Deleted Records = " + subjects, Toast.LENGTH_LONG).show();

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
                                        break;
                                    case R.id.menu_export:
                                        cursor.moveToPosition(position);
                                        ArrayList<String> str = new ArrayList<String>();
                                        str.add(0, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_ID)));
                                        str.add(1, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_NAME)));
                                        str.add(2, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_ID)));
                                        str.add(3, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_NAME)));
                                        PoiWriteExcelFile obj = new PoiWriteExcelFile(mContext_class, str);
                                        obj.exportToExcel();
                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();

                    }
                });
                childItemController.cv_child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        cursor.moveToPosition(position);
                        ArrayList<String> str = new ArrayList<String>();
                        cursor.moveToPosition(position);
                        str.add(0, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_ID)));
                        str.add(1, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Classes.CLASS_NAME)));
                        str.add(2, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_ID)));
                        str.add(3, cursor.getString(cursor.getColumnIndex(AttendanceSystemContract.Subject.SUBJECT_NAME)));
                        Intent intent = new Intent(mContext_class, AttendanceActivity.class);
                        intent.putStringArrayListExtra("data", str);
                        mContext_class.startActivity(intent);
                    }
                });

                break;
        }
    }

    /*@Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return view;
     }*/
    @Override
    public int getItemViewType(int position) {
        mCursor_class.moveToPosition(position);
        return mCursor_class.getInt(mCursor_class.getColumnIndex("row_type"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataValid_class && mCursor_class != null) {
            return mCursor_class.getCount();
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
        if (newCursor == mCursor_class) {
            return null;
        }
        Cursor oldCursor = mCursor_class;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor_class = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn_class = newCursor.getColumnIndexOrThrow("class_id");
            mDataValid_class = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn_class = -1;
            mDataValid_class = false;
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
        if (mAutoRequery_class && mCursor_class != null && !mCursor_class.isClosed()) {
            mDataValid_class = mCursor_class.requery();
        }
    }

    public Cursor getItem(int position) {
        if (mCursor_class != null) {
            mCursor_class.moveToPosition(position);
            return mCursor_class;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mCursor_class != null) {
            if (mCursor_class.moveToPosition(position)) {
                return mCursor_class.getLong(mRowIDColumn_class);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public TextView createdOnDate;
        public TextView hideid;
        public ImageButton btn_Delete;
        public ImageButton btn_Edit;
        public ImageButton btn_add;
        public ImageButton btn_view;
        public CardView cv;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.info_text);
            createdOnDate = (TextView) itemView.findViewById(R.id.fr_lb_created_on);
            hideid = (TextView) itemView.findViewById(R.id.hide_id);
            btn_Delete = (ImageButton) itemView.findViewById(R.id.fr_btn_delete_class);
            btn_Edit = (ImageButton) itemView.findViewById(R.id.fr_btn_edit_class);
            btn_add = (ImageButton) itemView.findViewById(R.id.fr_btn_add_subject);
            btn_view = (ImageButton) itemView.findViewById(R.id.fr_btn_view_students);
            cv = (CardView) itemView.findViewById(R.id.cv);
        }
    }

    public static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView childsubjectname;
        public TextView childcreatedon;
        public TextView childhideid;
        public TextView action;
        public FrameLayout cv_child;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            childhideid = (TextView) itemView.findViewById(R.id.child_hide_id);
            childsubjectname = (TextView) itemView.findViewById(R.id.child_info_text);
            childcreatedon = (TextView) itemView.findViewById(R.id.child_fr_lb_created_on);
            action = (TextView) itemView.findViewById(R.id.child_action);
            cv_child = (FrameLayout) itemView.findViewById(R.id.cv_child);
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
            mDataValid_class = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid_class = false;
            notifyDataSetInvalidated();
        }
    }

}