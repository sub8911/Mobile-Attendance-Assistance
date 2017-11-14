package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.StudentAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Student_Fragment.OnStudentFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Student_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Student_Fragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public Cursor mCursor;
    ArrayList<String> str = new ArrayList<String>();
    StudentAdapter adapter;
    private RecyclerView recyclerview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnStudentFragmentInteractionListener mListener;

    public Student_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Student_Fragment newInstance(String param1, String param2) {
        Student_Fragment fragment = new Student_Fragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_, container, false);
        Intent intent = getActivity().getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            str = bd.getStringArrayList("data");
        }
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview_students);
        recyclerview.setLayoutManager(llm);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onStudentFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStudentFragmentInteractionListener) {
            mListener = (OnStudentFragmentInteractionListener) context;
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
    public void onClick(View v) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_STUDENTS;
        Uri list = Uri.parse(URL);
        //  mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);
        CursorLoader cursorLoader = new CursorLoader(getContext(), list, null, AttendanceSystemContract.Student.CLASS_ID + "=?", new String[]{str.get(0)}, AttendanceSystemContract.Classes.CLASS_ID);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 0:
                cursor.setNotificationUri(getActivity().getContentResolver(), Class_SubjectProvider.CLASS_STUDENT_URI);
                if (null == adapter)
                    adapter = new StudentAdapter(getContext(), cursor, true);
                //gv is a GridView
                if (recyclerview.getAdapter() != adapter)
                    recyclerview.setAdapter(adapter);
                if (adapter.getCursor() != cursor)
                    adapter.swapCursor(cursor);
                break;
            case 1:
                adapter = new StudentAdapter(getContext(), cursor, true);
                recyclerview.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
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
    public interface OnStudentFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStudentFragmentInteraction(Uri uri);
    }


}
