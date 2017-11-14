package com.sub.studentinfosys.mobile_attendance_assistance.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by Sagar on 3/30/2017.
 */

public class ReadXLSXfile
{
    Context context;
  public  ReadXLSXfile(Context context)
    {
        this.context=context;
    }
    public static ArrayList<InputStream> getSheetsXML(ZipFile zipFile) throws IOException {

        Enumeration<? extends ZipEntry> em = zipFile.entries();

        ArrayList<InputStream> sheets = new ArrayList<InputStream>();

        ZipEntry zipEntry = null;

        while(em.hasMoreElements()){

            zipEntry = em.nextElement();

            if(zipEntry.getName().contains("sheet")){

                sheets.add(zipFile.getInputStream((zipEntry)));

            }
        }

        return sheets;
    }


    public static List<String> getSharedStrings(ZipFile zipFile) throws IOException, XMLStreamException {

        Enumeration<? extends ZipEntry> em = zipFile.entries();

        InputStream strings = null;

        ZipEntry zipEntry = null;

        while(em.hasMoreElements()){

            zipEntry = em.nextElement();

            if(zipEntry.getName().contains("sharedStrings")){

                strings = zipFile.getInputStream(zipEntry);
                break;
            }
        }


		/* Now we have the xml stream with all unique strings, I'll use stax to read the xml and add them to a list */

        ArrayList<String> stringList = new ArrayList<String>();

        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(strings);

        while (xmlStreamReader.hasNext()) {

            // go to next event
            xmlStreamReader.next();

            // the current event is characters and the content is not all white space
            if ((xmlStreamReader.getEventType() == XMLStreamConstants.CHARACTERS) && (xmlStreamReader.getText().trim().length() > 0)) {

                stringList.add(xmlStreamReader.getText());

            }

        }

        xmlStreamReader.close();

        return stringList;
    }

    public static Map<String,String> getCellValues(InputStream input, List<String> sharedStrings) throws XMLStreamException, FactoryConfigurationError {

        HashMap<String,String> cellValues = new HashMap<String,String>();

        boolean isString = false;
        String cellID = null;

        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(input);

        while (xmlStreamReader.hasNext()) {

            xmlStreamReader.next();

            if (xmlStreamReader.getEventType() == XMLStreamConstants.START_ELEMENT) {

                if (xmlStreamReader.getLocalName().equals("c") && xmlStreamReader.getAttributeCount() > 0) {

                    for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {

                        if( xmlStreamReader.getAttributeLocalName(i).equals("t")
                                && xmlStreamReader.getAttributeValue(i).equals("s")){

                            isString = true;

                        }

                        if(xmlStreamReader.getAttributeLocalName(i).equals("r")){
                            cellID = xmlStreamReader.getAttributeValue(i);
                        }

                    }

                }

            }

            if ((xmlStreamReader.getEventType() == XMLStreamConstants.CHARACTERS) && (xmlStreamReader.getText().trim().length() > 0)) {

                if(isString){

                    int sharedStringKey = Integer.valueOf(xmlStreamReader.getText());
                    cellValues.put(cellID, sharedStrings.get(sharedStringKey));
                    isString = false;

                }else {
                    cellValues.put(cellID, xmlStreamReader.getText());
                }

            }


        }

        return cellValues;
    }

    public static void main1(String str) throws IOException, XMLStreamException, FactoryConfigurationError {

        String filename = str;

        ZipFile zipFile = new ZipFile(filename);

        ArrayList<InputStream> list = getSheetsXML(zipFile);

        List<String> input = getSharedStrings(zipFile);

        zipFile.close();

    }

    public static void main2(Uri uri) throws IOException, XMLStreamException, FactoryConfigurationError, URISyntaxException {

      //  String filename = str;

     //   String path = uri.getPath().toString(); // "/mnt/sdcard/FileName.mp3"
        File file = new File(uri.getPath());

        ZipFile zipFile = new ZipFile(file);

        ArrayList<InputStream> list = getSheetsXML(zipFile);

        List<String> input = getSharedStrings(zipFile);

        // iterate in a loop for all sheets
        System.out.println(getCellValues(list.get(0), input).toString());

        zipFile.close();

    }
    public boolean isVirtualFile(Uri uri) {
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return false;
        }

        Cursor cursor = context.getContentResolver().query(
                uri,
                new String[] { DocumentsContract.Document.COLUMN_FLAGS },
                null, null, null);

        int flags = 0;
        if (cursor.moveToFirst()) {
            flags = cursor.getInt(0);
        }
        cursor.close();

        return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
    }


}
