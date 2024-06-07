package com.example.appointmentscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            COLUMN_IS_FINISHED + " INTEGER, " +
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
}
