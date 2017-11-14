package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.ClickTransferInterface;
import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.ViewAttendanceAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

import static com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider.ATTENDANCE_URI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Attendance_fragment.OnFragmentAttendanceInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Attendance_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Attendance_fragment extends Fragment
        implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, ClickTransferInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOADER_ID = 1234;
    public View_Attendance_fragment view_attendance_fragment;
    // Identifies a particular Loader being used in this component
    public Cursor mCursor;
    ViewAttendanceAdapter adapter;
    View v;
    ArrayList<String> str = new ArrayList<String>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    private OnFragmentAttendanceInteractionListener mListener;

    public Attendance_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Attendance_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Attendance_fragment newInstance(String param1, String param2) {
        Attendance_fragment fragment = new Attendance_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Bundle args = getArguments();
            if (args != null) {
                str = args.getStringArrayList("index");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_attendance, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview_attendance);
        getActivity().invalidateOptionsMenu();
        recyclerview.setLayoutManager(llm);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentAttendanceInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAttendanceInteractionListener) {
            mListener = (OnFragmentAttendanceInteractionListener) context;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.Attendance.ATTENDANCE_TABLE;
        Uri list = Uri.parse(URL);
        //  mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);
        CursorLoader cursorLoader = new CursorLoader(getContext(), ATTENDANCE_URI, null, AttendanceSystemContract.Attendance.CLASS_ID + "=? AND " + AttendanceSystemContract.Attendance.SUBJECT_ID + "=?", new String[]{str.get(0), str.get(2)}, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        adapter = new ViewAttendanceAdapter(getActivity().getApplicationContext(), cursor, true, this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }


    @Override
    public void setValues(Cursor al) {
        mListener.onFragmentAttendanceInteraction("Fab_Gone");
        view_attendance_fragment = new View_Attendance_fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place_attendance, view_attendance_fragment);
        Bundle args = new Bundle();
        args.putStringArrayList("index", str);
        args.putInt("attendance_id", al.getInt(al.getColumnIndex(AttendanceSystemContract.Attendance.ATTENDANCE_ID)));
        view_attendance_fragment.setArguments(args);
        fragmentTransaction.commit();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentAttendanceInteractionListener {
        // TODO: Update argument type and name
        void onFragmentAttendanceInteraction(String uri);
    }
}
