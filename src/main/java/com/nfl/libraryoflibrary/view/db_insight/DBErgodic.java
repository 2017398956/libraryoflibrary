package com.nfl.libraryoflibrary.view.db_insight;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nfl.libraryoflibrary.utils.ToastTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBErgodic {

    private SQLiteDatabase db;
    private List<Map<String, String>> tablesName; // 所有符合条件的表名称
    private List<Map<String, String>> tableContent; // 获得指定表的所有内容
    private String[] columnNames; // 获得指定表的所有列名称
    private String tableName;

    public DBErgodic(String dbPath) {
        tablesName = new ArrayList<>();
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READONLY);
    }

    public DBErgodic(String dbPath, String tableName) {

        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READONLY);
        this.tableName = tableName;
    }

    /**
     * 获取所有符合条件的表名称 List<Map<String,String>> 2015年12月30日
     *
     * @return
     */
    public List<Map<String, String>> getTablesName() {
        if (null == db) {
            return null;
        }
        tablesName.clear();
        Cursor cursor = null;
        // The file may be encrypted or is not a database .
        try {
            cursor = db.query("sqlite_master",
                    new String[]{"type", "name"},
                    "type='table' and name!='android_metadata'", null, null, null,
                    null);
            if (cursor != null) {
                // 循环遍历cursor
                HashMap<String, String> map;
                while (cursor.moveToNext()) {
                    map = new HashMap<>();
                    map.put("tableName",
                            cursor.getString(cursor.getColumnIndex("name")));
                    tablesName.add(map);
                }
            }
        } catch (Exception e) {
            ToastTool.showShortToast("数据库已加密或不是数据库文件");
        }
        if (null != cursor) {
            cursor.close();
        }
        return tablesName;
    }

    /**
     * 获得指定表的所有内容 void 2015年12月30日
     */
    public List<Map<String, String>> getTableContent() {
        if (null == db) {
            return null;
        }
        tableContent = new ArrayList<>();
        Cursor cursor = null;
        // The file may be encrypted or is not a database .
        try {
            cursor = db.query(tableName, null, null, null, null, null, null);
            if (cursor != null) {
                // 循环遍历cursor
                HashMap<String, String> map;
                int length = cursor.getColumnCount();
                columnNames = cursor.getColumnNames();
                while (cursor.moveToNext()) {
                    map = new HashMap<>();
                    for (int i = 0; i < length; i++) {
                        map.put(columnNames[i], cursor.getString(i) + "");
                    }
                    tableContent.add(map);
                }
            }
        } catch (Exception e) {
            ToastTool.showShortToast("数据库已加密或不是数据库文件");
        }
        if (null != cursor) {
            cursor.close();
        }
        return tableContent;
    }

    /**
     * 必须在getTableContent()方法执行后才可调用 String[] 2015年12月30日
     *
     * @return
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    public void closeDB() {
        db.close();
    }
}
