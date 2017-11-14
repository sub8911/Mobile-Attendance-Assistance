package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceDatabaseHelper;
import com.sub.studentinfosys.mobile_attendance_assistance.DATABASE.AttendanceSystemContract;

import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;
import com.sub.studentinfosys.mobile_attendance_assistance.SERVICES.AddClassService;
import com.sub.studentinfosys.mobile_attendance_assistance.Utils.ReadXLSXfile;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import static android.app.Activity.RESULT_OK;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Add_Classes_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Classes_Fragment extends Fragment implements View.OnClickListener {

    private static final int OPEN_DOCUMENT_REQUEST = 1;
    public static ArrayList<AttendanceSheet> excelData = new ArrayList<AttendanceSheet>();
    public Intent dt;
    Intent mServiceIntent;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    AttendanceDatabaseHelper attendanceDBHelper;
    SQLiteDatabase db;
    Button addClass, cancelAddClass, selectExcel,addListLater;
    EditText editText;
    TextView fileName, path;
    View view;
    String ClassName, Filename;
    private OnFragmentInteractionListener mListener;


    public Add_Classes_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Add_Classes_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_Classes_Fragment newInstance() {
        Add_Classes_Fragment fragment = new Add_Classes_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_class, container, false);
        getActivity().invalidateOptionsMenu();
        addClass = (Button) view.findViewById(R.id.fr_btn_addclass);
        addListLater=(Button) view.findViewById(R.id.fr_btn_add_later);
        selectExcel = (Button) view.findViewById(R.id.fr_btn_addExcel);
        editText = (EditText) view.findViewById(R.id.fr_et_classname);
        fileName = (TextView) view.findViewById(R.id.fr_lb_fileName);
       /* Button AddGSREAD=(Button) view.findViewById(R.id.fr_btn_GExcel);*/
      /*  AddGSREAD.setOnClickListener(this);*/
        selectExcel.setOnClickListener(this);
        editText.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        if (a instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) a;
        } else {
            throw new RuntimeException(a.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        final Drawable errorIcon;
        String uri = null;
        switch (view.getId()) {

            case R.id.fr_btn_addExcel:
                errorIcon = getResources().getDrawable(R.drawable.blank);
                selectExcel.setError(null, errorIcon);
                fileName.setText("");
                openDocument();
                break;
            case R.id.fr_et_classname:
                errorIcon = getResources().getDrawable(R.drawable.blank);
                editText.setError(null, errorIcon);
                showSoftKeyboard(editText);
                break;
           /* case R.id.fr_btn_GExcel:
                addSpreadSheet();
                break;*/
        }

    }

    private void openDocument() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.ms-excel"/*, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"*/};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        dt = data;
        if (requestCode == OPEN_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;
            Filename = dt.getData().getPathSegments().toString().substring(dt.getData().getPathSegments().toString().lastIndexOf(':') + 1, dt.getData().getPathSegments().toString().length() - 1);
            fileName.setText("Selected File is " + Filename);
            // fileName.setText(dt.getData().toString());

            // Log.v("pppp  "+dt.getData().toString(),""+dt.getData().getLastPathSegment().toString());

            return;

        }

    }

    private ArrayList<AttendanceSheet> getExcelData(Uri uri) {

        if (uri == null)
            return null;
        int id=0;

        String temp = "",name="",prn="";
        ArrayList<AttendanceSheet> dataList=  new ArrayList<AttendanceSheet>();


        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        // Toast.makeText(getContext(), "  "+type, Toast.LENGTH_SHORT).show();
        Iterator<Row> iterator = null;
        InputStream inputStream = null;

        try {
            inputStream = getActivity().getContentResolver().openInputStream(uri);
            if(type.equals("xls")) {
                Workbook workbook = new HSSFWorkbook(inputStream);
                Sheet firstSheet = workbook.getSheetAt(0);
                iterator = firstSheet.iterator();
            } else
            {/*if(type.equals("xlsx")) {
                            XSSFWorkbook myWorkBook = null;
                            myWorkBook = new XSSFWorkbook(inputStream);
                            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                            iterator = mySheet.iterator();*/

            }
            int j = 0;
            int n = 0;
            int count = 1;
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (nextRow != null) {
                    Iterator<Cell> cellIterator = nextRow.cellIterator();
                    int i = 0;
                    if (n != 0) {
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            temp = "";
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_STRING:
                                    temp = (cell.getStringCellValue());
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    temp = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:

                                    temp= BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                                  //  Toast.makeText(getContext(), "  "+cell.getNumericCellValue(), Toast.LENGTH_SHORT).show();

                                    break;
                                default:
                                    temp = null;
                                    break;
                            }

                            if (i == 0) {
                                name = temp;
                                i++;
                            } else
                                prn = temp;
                            Log.e("`" + temp, "\n");

                        }
                        if (prn != null && name != null) {
                            AttendanceSheet as = new AttendanceSheet(String.valueOf(count++), name, prn, "0", 0);
                            dataList.add(j++, as);
                        }
                    }
                    n++;
                }
                else
                {
                    break;
                }
            }
            //Log.v("Data in Excel is ....\n",temp);
            inputStream.close();
            return dataList;
            //mEditText.setText(temp);
        }
        catch (NullPointerException e) {

            return null;
        }
        catch (IOException e) {

            return null;
        } finally {

        }

    }

    public boolean createAllTables(Uri uri, String ClassName) {
        try {
            excelData = getExcelData(uri);
            if (excelData != null) {
                mServiceIntent = new Intent(getActivity(), AddClassService.class);
                Uri builtUri = Uri.parse("content://com.sub.studentinfosys.mobile_attendance_assistance.DATABASE/")
                        .buildUpon()
                        .appendQueryParameter(AttendanceSystemContract.Classes.CLASS_NAME, ClassName)
                        .build();
                mServiceIntent.setData(builtUri);
                // Add a new student record
                getActivity().startService(mServiceIntent);

                Snackbar.make(view, "Class " + ClassName + " Added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(view, "Select Proper file ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }


            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void submitNewClass() {
        final Drawable errorIcon;
        String uri = null;
        if (editText.getText().length() == 0) {
            errorIcon = getResources().getDrawable(R.drawable.error);
            errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
            editText.setError(null, errorIcon);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editText.setError("", null);

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > 0) {
                        editText.setError("", null);
                    } else {
                        editText.setError("Enter Text", errorIcon);
                    }
                }

            });
        } else if (fileName.length() == 0) {
            errorIcon = getResources().getDrawable(R.drawable.error);
            errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
            selectExcel.setError(null, errorIcon);

        } else {
            ClassName = editText.getText().toString();
            if (createAllTables(dt.getData(), ClassName))
                uri = "add";
            else {
                errorIcon = getResources().getDrawable(R.drawable.error);
                errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
                selectExcel.setError(null, errorIcon);
                uri = "false";
            }
            editText.setText("");
        }

        mListener.onFragmentSelected(uri);
    }

    public boolean isExcelEmpty(Uri uri) {
        File file = new File(uri.getPath());

        if (file.length() == 0) {
            return true;
        } else {
            return false;
        }
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
    public interface OnFragmentInteractionListener {
        void onFragmentSelected(String uri);
    }
}
