package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity
        implements Attendance_fragment.OnFragmentAttendanceInteractionListener,
        View_Attendance_fragment.OnFragmentViewInteractionListener {
    public Cursor cursor;
    ArrayList<String> str1 = new ArrayList<String>();
    Attendance_fragment attendance_fragment;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str1 = bd.getStringArrayList("data");
        }
        title=(TextView) findViewById(R.id.attendance_title);
        title.setText(str1.get(3).toString());
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        getSupportActionBar().setTitle(" Attendance Sheet ");

        if (findViewById(R.id.fragment_place_attendance) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
           /* classAndSubject_fragment = new ItemFragment();*/
            attendance_fragment = new Attendance_fragment();
            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putStringArrayList("index", str1);
            attendance_fragment.setArguments(args);
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            // add_class_fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_place_attendance, attendance_fragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendance_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (attendance_fragment != null && attendance_fragment.isVisible()) {
            menu.findItem(R.id.action_submit_edited_record).setEnabled(false);
            menu.findItem(R.id.action_submit_edited_record).setVisible(false);
            menu.findItem(R.id.action_add_record).setEnabled(true);
            menu.findItem(R.id.action_add_record).setVisible(true);
        } else {
            menu.findItem(R.id.action_add_record).setEnabled(false);
            menu.findItem(R.id.action_add_record).setVisible(false);
            menu.findItem(R.id.action_submit_edited_record).setEnabled(true);
            menu.findItem(R.id.action_submit_edited_record).setVisible(true);
        }

        //Also you can do this for sub menu

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_submit_edited_record:
                attendance_fragment.view_attendance_fragment.submit_edit();

                break;
            case R.id.action_add_record:
                Intent intent = new Intent(AttendanceActivity.this, TakeAttendance.class);
                intent.putStringArrayListExtra("data", str1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (attendance_fragment != null && attendance_fragment.isVisible()) {
            super.onBackPressed();

        } else {
            /*FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
            fb.setVisibility(View.VISIBLE);*/
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place_attendance, attendance_fragment, "classAndSubject_fragment").commit();

        }
    }


    @Override
    public void onFragmentAttendanceInteraction(String uri) {
        if (uri.equals("Fab_Gone")) {
            /*FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
            fb.setVisibility(View.GONE);*/
        }

    }

    @Override
    public void onFragmentViewInteraction(Uri uri) {

    }
}
