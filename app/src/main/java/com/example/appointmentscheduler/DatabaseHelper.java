package com.example.appointmentscheduler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appointments.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_APPOINTMENTS = "Appointments";
    public static final String TABLE_APPOINTMENT_STATUS = "AppointmentStatus";

    // Columns for Users table
    public static final String COLUMN_USERNAME = "Username";

    // Columns for Appointments table
    public static final String COLUMN_SCHEDID = "SchedID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_LINK = "Link";
    public static final String COLUMN_NOTES = "Notes";
    public static final String COLUMN_USER_FK = "Username";

    // Columns for AppointmentStatus table
    public static final String COLUMN_IS_FINISHED = "isFinished";

    //user for setting the username
    public static String USERNAME;

    // Create table statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            COLUMN_USERNAME + " TEXT PRIMARY KEY);";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS + "(" +
            COLUMN_SCHEDID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_LINK + " TEXT, " +
            COLUMN_NOTES + " TEXT, " +
            COLUMN_USER_FK + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "));";

    private static final String CREATE_TABLE_APPOINTMENT_STATUS = "CREATE TABLE " + TABLE_APPOINTMENT_STATUS + "(" +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_SCHEDID + " INTEGER, " +
            COLUMN_IS_FINISHED + " TEXT DEFAULT 'unfinished', " +
            "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "), " +
            "FOREIGN KEY(" + COLUMN_SCHEDID + ") REFERENCES " + TABLE_APPOINTMENTS + "(" + COLUMN_SCHEDID + "), " +
            "PRIMARY KEY(" + COLUMN_USERNAME + ", " + COLUMN_SCHEDID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_APPOINTMENT_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT_STATUS);
        onCreate(db);
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint("Range")
    public String getCurrentUsername() {
        String username = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS, null);
            if (cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            }
            cursor.close();
        }
        return username;
    }


    public boolean updateUserName(String newUsername, String oldUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, newUsername);
        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{oldUsername});
        return rowsAffected > 0;
    }


    public String getCurrentSchedCount(String username) {
        int schedCounts = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        if (db != null) {
            Cursor cursor = db.rawQuery("SELECT COUNT(" + COLUMN_SCHEDID + ") FROM " + TABLE_APPOINTMENTS +
                    " WHERE " + COLUMN_USER_FK + "=?", new String[]{username});
            if (cursor.moveToFirst()) {
                schedCounts = cursor.getInt(0);
            }
            cursor.close();
        }

        return String.valueOf(schedCounts);
    }

    Cursor readAllSchedule() {
        String query = "SELECT * FROM " + TABLE_APPOINTMENTS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

}
