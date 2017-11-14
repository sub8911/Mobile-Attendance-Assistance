package com.sub.studentinfosys.mobile_attendance_assistance.ADAPTERS;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sub.studentinfosys.mobile_attendance_assistance.MODELS.AttendanceSheet;
import com.sub.studentinfosys.mobile_attendance_assistance.R;

import java.util.ArrayList;

import static com.sub.studentinfosys.mobile_attendance_assistance.UI.View_Attendance_fragment.countr;

/**
 * Created by Sagar on 3/2/2017.
 */

public class EditAttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<AttendanceSheet> mData = new ArrayList<AttendanceSheet>();
    Context mContext;
    int position, row_index;

    public EditAttendanceAdapter(Context context, ArrayList<AttendanceSheet> mData) {
        super();
        this.mContext = context;
        this.mData = mData;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_layout_edit_attendance, parent, false);
        ViewHolder header = new ViewHolder(view);
        return header;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        this.position = position;
        final AttendanceSheet mItem = mData.get(position);
        final ViewHolder mholder = (ViewHolder) holder;
        mholder.edit_roll.setText(mItem.getStudentRoll());
        mholder.a.setText(mItem.getId());
        mholder.b.setText(mItem.getStudentName());
        mholder.c.setText(mItem.getCreate_on());

        mholder.edit_roll.setBackgroundColor(Color.parseColor("#59a450"));
        if (countr[position]) {
            mholder.edit_roll.setBackgroundColor(Color.parseColor("#59a450"));
            mholder.d.setBackgroundColor(Color.parseColor("#59a450"));
            mholder.d.setText("Present");
            mholder.p.setBackgroundResource(R.drawable.p);
        } else {
            mholder.edit_roll.setBackgroundColor(Color.parseColor("#df464b"));
            mholder.d.setBackgroundColor(Color.parseColor("#df464b"));
            mholder.d.setText("Absent");
            mholder.p.setBackgroundResource(R.drawable.a);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView edit_roll;
        public final TextView a;
        public final TextView b;
        public final TextView c;
        public final TextView d;
        public final TextView p;


        public ViewHolder(View itemView) {
            super(itemView);
            edit_roll = (TextView) itemView.findViewById(R.id.edit_roll_number);
            a = (TextView) itemView.findViewById(R.id.a);
            b = (TextView) itemView.findViewById(R.id.b);
            c = (TextView) itemView.findViewById(R.id.c);
            d = (TextView) itemView.findViewById(R.id.d);
            p = (TextView) itemView.findViewById(R.id.p);


        }
    }
}
