package com.sub.studentinfosys.mobile_attendance_assistance.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Sagar on 2/7/2017.
 */

public class MyEdittext extends EditText {

    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
    }

}

