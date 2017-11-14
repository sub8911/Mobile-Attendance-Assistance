package com.sub.studentinfosys.mobile_attendance_assistance.MODELS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sagar on 2/10/2017.
 */

public class ClassesDataModel {
    public static final List<ClassesDataModel> ITEMS = new ArrayList<ClassesDataModel>();
    public static final Map<String, ClassesDataModel> ITEM_MAP = new HashMap<String, ClassesDataModel>();
    public final int type;
    public final String id;
    public final String className;
    public final String TableName;
    public final String CreatedOn_Date;

    public ClassesDataModel(int type, String id, String className, String Tablename, String CreatedOn_Date) {
        super();
        this.type = type;
        this.id = id;
        this.className = className;
        this.TableName = Tablename;
        this.CreatedOn_Date = CreatedOn_Date;
    }

    private static void addItem(ClassesDataModel item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getTableName() {
        return TableName;
    }

    public String getCreatedOn_Date() {
        return CreatedOn_Date;
    }
}
