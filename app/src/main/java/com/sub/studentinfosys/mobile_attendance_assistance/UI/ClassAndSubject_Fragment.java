package com.sub.studentinfosys.mobile_attendance_assistance.UI;


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

import com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS.ClassSubjectListAdapter;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.Class_SubjectProvider;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassAndSubject_Fragment.OnFragmentTakeInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassAndSubject_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassAndSubject_Fragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int LOADER_ID = 7685;
    public Cursor mCursor;
    ClassSubjectListAdapter adapter;
    // Identifies a particular Loader being used in this component
    private RecyclerView recyclerview;
    private OnFragmentTakeInteractionListener mListener;

    public ClassAndSubject_Fragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassAndSubject_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassAndSubject_Fragment newInstance(String param1, String param2) {
        ClassAndSubject_Fragment fragment = new ClassAndSubject_Fragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the asynchronous callback for the contacts data loader

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_classes_subject, container, false);
        getActivity().invalidateOptionsMenu();
        /*String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/"+ AttendanceSystemContract.View_Triggers.VIEW_CLASS_SUBJECTS;
        Uri list= Uri.parse(URL);
        mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);

*/
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(llm);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return v;
    }

    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentTakeInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = "content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/" + AttendanceSystemContract.View_Triggers.VIEW_CLASS_SUBJECTS;
        Uri list = Uri.parse(URL);
        //  mCursor = getActivity().getContentResolver().query(list, null, null, null, AttendanceSystemContract.Classes.CLASS_NAME);
        CursorLoader cursorLoader = new CursorLoader(getContext(), list, null, null, null, AttendanceSystemContract.Classes.CLASS_ID);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 0:
                cursor.setNotificationUri(getActivity().getContentResolver(), Class_SubjectProvider.CLASS_SUBJECT_URI);
                if (null == adapter)
                    adapter = new ClassSubjectListAdapter(getContext(), cursor, true);
                //gv is a GridView
                if (recyclerview.getAdapter() != adapter)
                    recyclerview.setAdapter(adapter);
                if (adapter.getCursor() != cursor)
                    adapter.swapCursor(cursor);
                break;
            case 1:
                adapter = new ClassSubjectListAdapter(getContext(), cursor, true);
                recyclerview.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    public interface OnFragmentTakeInteractionListener {

        void onFragmentTakeInteraction(String uri);
    }

}
