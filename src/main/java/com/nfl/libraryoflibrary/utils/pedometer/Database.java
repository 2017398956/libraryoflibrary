package com.nfl.libraryoflibrary.utils.pedometer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.nfl.libraryoflibrary.BuildConfig;
import com.nfl.libraryoflibrary.utils.DesTool;
import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Database extends SQLiteOpenHelper {

    private final static String DB_NAME = "steps";
    private final static String DB_NAME_ENCRYPT = "steps_encrypt";
    private final static int DB_VERSION = 2;

    private static Database instance;
    private static final AtomicInteger openCounter = new AtomicInteger();

    private Database(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized Database getInstance(final Context c) {
        if (instance == null) {
            instance = new Database(c.getApplicationContext());
        }
        openCounter.incrementAndGet();
        return instance;
    }

    @Override
    public void close() {
        if (openCounter.decrementAndGet() == 0) {
            super.close();
        }
    }

    /**
     * date : 计步的日期
     * steps : 开关机后存储的步数
     * sensor : 传感器的步数，计步器传感器每次变化都会触发
     * off : 是否关机过，默认false(0)；关机时置为true(1)，待计步服务开启后将当天的off置为false(0) ；
     *
     * @param db
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_NAME + " (date INTEGER, steps INTEGER , sensor INTEGER , off INTEGER)");
        db.execSQL("CREATE TABLE " + DB_NAME_ENCRYPT + " (date INTEGER, steps TEXT , sensor TEXT , off INTEGER)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion == 1) {
//            // drop PRIMARY KEY constraint
//            db.execSQL("CREATE TABLE " + DB_NAME + "2 (date INTEGER, steps INTEGER)");
//            db.execSQL("INSERT INTO " + DB_NAME + "2 (date, steps) SELECT date, steps FROM " +
//                    DB_NAME);
//            db.execSQL("DROP TABLE " + DB_NAME);
//            db.execSQL("ALTER TABLE " + DB_NAME + "2 RENAME TO " + DB_NAME + "");
//        }
    }

    /**
     * Query the 'steps' table. Remember to close the cursor!
     *
     * @param columns       the colums
     * @param selection     the selection
     * @param selectionArgs the selction arguments
     * @param groupBy       the group by statement
     * @param having        the having statement
     * @param orderBy       the order by statement
     * @return the cursor
     */
    public Cursor query(final String[] columns, final String selection,
                        final String[] selectionArgs, final String groupBy, final String having,
                        final String orderBy, final String limit) {
        return getReadableDatabase()
                .query(DB_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 插入新的数据
     *
     * @param date
     * @param steps
     * @param correctSensorSteps 需要根据上一天的开关机判断
     * @param off
     */
    public void insertNewDay(long date, int steps, int correctSensorSteps, int off) {
        getWritableDatabase().beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("steps", steps);
            values.put("sensor", correctSensorSteps);
            values.put("off", off);
            getWritableDatabase().insert(DB_NAME, null, values);
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
        insertNewDayEncrypt(date , steps , correctSensorSteps , off);
    }
    public void insertNewDayEncrypt(long date, int steps, int correctSensorSteps, int off) {
        getWritableDatabase().beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("steps", DesTool.encrypt(steps + "" ));
            values.put("sensor", DesTool.encrypt(correctSensorSteps + "" ));
            values.put("off", off);
            getWritableDatabase().insert(DB_NAME_ENCRYPT , null, values);
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    /**
     * 更新steps(关机时才会调用)
     *
     * @param date
     * @param correctSensorSteps 应该传的传感器数值，不一定为读取到的传感器数值
     */
    public void updateSteps(long date, int correctSensorSteps) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录,说明之前传感器没有动，所以一概为0 ；
            insertNewDay(date, 0, 0, 1);
        } else {
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME + " SET steps = steps + " + correctSensorSteps + " WHERE date = " + date);
        }
        c.close();
        updateStepsEncrypt(date , correctSensorSteps);
    }
    public void updateStepsEncrypt(long date, int correctSensorSteps) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT , new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录,说明之前传感器没有动，所以一概为0 ；
            insertNewDayEncrypt(date, 0, 0, 1);
        } else {
            int steps = 0 ;
            try{
                steps = Integer.parseInt(DesTool.decrypt(getStepsEncrypt(date))) ;
            }catch (Exception e){

            }
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME_ENCRYPT + " SET steps = \"" + DesTool.encrypt((steps + correctSensorSteps) + "") + "\" WHERE date = " + date);
        }
        c.close();
    }

    /**
     * @param date the date in millis since 1970
     * @return steps的值
     */
    public int getSteps(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"steps"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int re = 0;
        if (c.getCount() == 0) {
        } else {
            re = c.getInt(0);
        }
        return re;
    }
    public String getStepsEncrypt(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT , new String[]{"steps"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        String re = null ;
        if (c.getCount() == 0) {
            re = "" ;
        } else {
            re = c.getString(0);
            if(null == re){
                re = "" ;
            }
        }
        c.close();
        return re;
    }

    /**
     * 更新某一天（date）的传感器步数（sensor）
     *
     * @param date               所属的时间，in millis since 1970
     * @param correctSensorSteps
     */
    public void updateSensorSteps(long date, int correctSensorSteps) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录
            insertNewDay(date, 0, correctSensorSteps, 0);
        } else {
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME + " SET sensor = " + correctSensorSteps + " WHERE date = " + date);
        }
        c.close();
        updateSensorStepsEncrypt(date , correctSensorSteps);
    }
    public void updateSensorStepsEncrypt(long date, int correctSensorSteps) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT, new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录
            insertNewDayEncrypt(date, 0, correctSensorSteps, 0);
        } else {
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME_ENCRYPT + " SET sensor = \"" + DesTool.encrypt(correctSensorSteps + "") + "\" WHERE date = " + date);
        }
        c.close();
    }

    /**
     * @param date the date in millis since 1970
     * @return 存储的传感器的步数
     */
    public int getSensorSteps(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"sensor"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int re;
        if (c.getCount() == 0) {
            re = 0;
        } else {
            re = c.getInt(0);
        }
        c.close();
        return re;
    }
    public String getSensorStepsEncrypt(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT, new String[]{"sensor"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        String re = null ;
        if (c.getCount() == 0) {
            re = "" ;
        } else {
            re = c.getString(0);
            if(null == re){
                re = "" ;
            }
        }
        c.close();
        return re;
    }

    /**
     * 更新off的状态，开关机的时候才用
     *
     * @param date
     * @param offStatus
     */
    public void updateOffStatus(long date, int offStatus) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录,统统为0
            insertNewDay(date, 0 , 0 , offStatus);
        }else {
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME + " SET off = " + offStatus + " WHERE date = " + date);
        }
        c.close();
        updateOffStatusEncrypt(date , offStatus) ;
    }
    public void updateOffStatusEncrypt(long date, int offStatus) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT , new String[]{"date"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            // 没有记录,统统为0
            insertNewDay(date, 0 , 0 , offStatus);
        }else {
            getWritableDatabase().execSQL(
                    "UPDATE " + DB_NAME_ENCRYPT + " SET off = " + offStatus + " WHERE date = " + date);
        }
        c.close();
    }

    /**
     * 得到off的值 0 = false ; 1 = true ;
     *
     * @param date
     * @return
     */
    public int getOffStatus(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"off"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int hasOffed = 0;
        if (c.getCount() == 0) {
        } else {
            hasOffed = c.getInt(0);
        }
        c.close();
        return hasOffed;
    }
    public int getOffStatusEncypt(long date) {
        Cursor c = getReadableDatabase().query(DB_NAME_ENCRYPT, new String[]{"off"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int hasOffed = 0;
        if (c.getCount() == 0) {
        } else {
            hasOffed = c.getInt(0);
        }
        c.close();
        return hasOffed;
    }

}
