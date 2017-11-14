package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceDatabaseHelper;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.PoiWriteExcelFile;


public class MainActivity extends AppCompatActivity
        implements Add_Classes_Fragment.OnFragmentInteractionListener,
        ClassAndSubject_Fragment.OnFragmentTakeInteractionListener,
        Student_Fragment.OnStudentFragmentInteractionListener

{
    public final static String LOG_TAG = "DevicePolicyAdmin";
    protected static final int REQUEST_ENABLE = 1;
    protected static final int SET_PASSWORD = 2;
    PoiWriteExcelFile obj;
    DevicePolicyManager MobileAttendanceDevicePolicyManager;
    ComponentName MobiAttendanceDevicePolicyAdmin;
    ClassAndSubject_Fragment classAndSubject_fragment;
    Add_Classes_Fragment add_class_fragment;
    AttendanceDatabaseHelper attendanceDBHelper;
    /* TextView one,two,three,four,five;*/
    boolean flag;
    private CheckBox truitonAdminEnabledCheckbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flag = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
        MobileAttendanceDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        MobiAttendanceDevicePolicyAdmin = new ComponentName(getApplication(), MyDevicePolicyReceiver.class);

        //obj=new PoiWriteExcelFile(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.home);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    //    getSupportActionBar().setHomeButtonEnabled(true);
       /* getSupportActionBar().setTitle("Mobile Attendance");
        getSupportActionBar().setIcon(R.drawable.home);*/
        add_class_fragment = new Add_Classes_Fragment();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // this.deleteDatabase("MOBI_ATTENDANCE.DB");

        if (findViewById(R.id.fragment_place) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
           /* classAndSubject_fragment = new ItemFragment();*/
            classAndSubject_fragment = new ClassAndSubject_Fragment();
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            // add_class_fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_place, classAndSubject_fragment).commit();
        }


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE:
                    Log.v(LOG_TAG, "Enabling Policies Now");
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isMyDevicePolicyReceiverActive()) {
            menu.findItem(R.id.action_prevent_uninstall).setChecked(true);
        } else {
            menu.findItem(R.id.action_prevent_uninstall).setChecked(false);
        }
        if (add_class_fragment != null && add_class_fragment.isVisible()) {
            menu.findItem(R.id.action_prevent_uninstall).setEnabled(false);
            menu.findItem(R.id.action_prevent_uninstall).setVisible(false);
            menu.findItem(R.id.action_add_class).setEnabled(false);
            menu.findItem(R.id.action_add_class).setVisible(false);
            menu.findItem(R.id.action_submit_new_class).setEnabled(true);
            menu.findItem(R.id.action_submit_new_class).setVisible(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        } else {
            menu.findItem(R.id.action_prevent_uninstall).setEnabled(true);
            menu.findItem(R.id.action_prevent_uninstall).setVisible(true);
            menu.findItem(R.id.action_add_class).setEnabled(true);
            menu.findItem(R.id.action_add_class).setVisible(true);
            menu.findItem(R.id.action_submit_new_class).setEnabled(false);
            menu.findItem(R.id.action_submit_new_class).setVisible(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        //Also you can do this for sub menu

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_submit_new_class:
                add_class_fragment.submitNewClass();
                break;
            case R.id.action_add_class:

                getSupportFragmentManager().beginTransaction()
                    /*.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_out, R.anim.fade_in)*/
                        .replace(R.id.fragment_place, add_class_fragment, "add_class").commit();
                return true;

            case R.id.action_prevent_uninstall:
                if (item.isChecked()) {
                    MobileAttendanceDevicePolicyManager.removeActiveAdmin(MobiAttendanceDevicePolicyAdmin);
                    item.setChecked(false);
                } else {

                    Intent intent = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(
                            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            MobiAttendanceDevicePolicyAdmin);
                    intent.putExtra(
                            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            getString(R.string.admin_explanation));
                    startActivityForResult(intent, REQUEST_ENABLE);
                    item.setChecked(true);

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.v("Before backstack count " + getSupportFragmentManager().getBackStackEntryCount(), " Total number of Fragments Paused");

        if (add_class_fragment != null && add_class_fragment.isVisible()) {

            Log.v("backstack count  " + getSupportFragmentManager().getBackStackEntryCount(), " Total number of Fragments Paused");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, classAndSubject_fragment, "classAndSubject_fragment").commit();
            add_class_fragment.editText.clearFocus();
            add_class_fragment.editText.setText("");
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onFragmentTakeInteraction(String uri) {
        if (uri != null)

            if (uri.equals("ok")) {


            }

    }

    @Override
    public void onFragmentSelected(String uri) {
        if (uri != null)
            if (uri.equals("cancel")) { /*fab.setVisibility(findViewById(R.id.content_main).VISIBLE);*/
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, classAndSubject_fragment, "classAndSubject_fragment").commit();
                add_class_fragment.editText.clearFocus();
                add_class_fragment.editText.setText("");
            } else if (uri.equals("add")) {
            /*fab.setVisibility(findViewById(R.id.content_main).VISIBLE);*/
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, classAndSubject_fragment, "classAndSubject_fragment").commit();
            } else if (uri.equals("false")) {

            }
    }

    public void switchContent(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_place, fragment).commit();


    }

    @Override
    public void onStudentFragmentInteraction(Uri uri) {

    }

    private boolean isMyDevicePolicyReceiverActive() {
        return MobileAttendanceDevicePolicyManager.isAdminActive(MobiAttendanceDevicePolicyAdmin);
    }

    public static class MyDevicePolicyReceiver extends DeviceAdminReceiver {

        @Override
        public void onDisabled(Context context, Intent intent) {
            Toast.makeText(context, "Mobile Attendance Device Admin Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            Toast.makeText(context, "Mobile Attendance Device Admin is now enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
            return disableRequestedSeq;

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG,
                    "MyDevicePolicyReciever Received: " + intent.getAction());
            super.onReceive(context, intent);
        }
    }


}
