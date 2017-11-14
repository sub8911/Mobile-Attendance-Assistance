package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

/**
 * Created by Sagar on 4/9/2017.
 */

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamToFileApp {


    public File ConvertertToFile(InputStream inputStream) {
        File file=new File(Environment.getDataDirectory().toString() + File.separator + "/Attendance/temp.xlsx");
        OutputStream outputStream = null;

        try {
          /*  // read this file into InputStream
            inputStream = new FileInputStream("/Users/mkyong/Downloads/holder.js");
*/
            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(""));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Done!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return file;

    }
}
