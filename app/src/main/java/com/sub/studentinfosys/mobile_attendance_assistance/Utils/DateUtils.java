package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sagar on 2/19/2017.
 */

public class DateUtils {
    public static String getReadable(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm");
        Date resultdate = new Date(Long.parseLong(time));
        return sdf.format(resultdate);
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy,h:mm a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
