package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.StudentAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public Cursor mCursor;
    ArrayList<String> str = new ArrayList<String>();
    StudentAdapter adapter;
    // Identifies a particular Loader being used in this component
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str = bd.getStringArrayList("data");
        }
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview_students);
        recyclerview.setLayoutManager(llm);
        getSupportLoaderManager().initLoader(1, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
        Uri list = Uri.parse(URL);
        //  mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), list, null, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{str.get(0)}, AttendanceSystemContract.Classes.CLASS_ID);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 0:
                cursor.setNotificationUri(getContentResolver(), Class_SubjectProvider.CLASS_STUDENT_URI);
                if (null == adapter)
                    adapter = new StudentAdapter(StudentActivity.this, cursor, true);
                //gv is a GridView
                if (recyclerview.getAdapter() != adapter)
                    recyclerview.setAdapter(adapter);
                if (adapter.getCursor() != cursor)
                    adapter.swapCursor(cursor);
                break;
            case 1:
                adapter = new StudentAdapter(getApplicationContext(), cursor, true);
                recyclerview.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }


    @Override
    public void onClick(View v) {

    }
}
