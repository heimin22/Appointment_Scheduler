// SETTING APPOINTMENT CLASS

package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseHelper(this);

//        //no transitions
//        overridePendingTransition(0, 0);


        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //adrian

        ImageButton homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button datePicker = findViewById(R.id.datePicker);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });

        Button timePicker = findViewById(R.id.timePicker);

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog();
            }
        });

        Button submitSchedButton = findViewById(R.id.submitSchedButton);

        submitSchedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingSchedule();
            }
        });

    }


    private void dateDialog() {

        TextView dateText = findViewById(R.id.dateText);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String formattedMonth = String.format("%02d", month + 1);
                String formattedDay = String.format("%02d", day);

                dateText.setText(year + "-" + formattedMonth + "-" + formattedDay);
            }
        }, currentYear, currentMonth, currentDay);
        dialog.show();
    }

    private void timeDialog() {

        TextView timeText = findViewById(R.id.timeText);

        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                String amPm;
                if (hours >= 12) {
                    amPm = "PM";
                    if (hours > 12) {
                        hours -= 12;
                    }
                } else {
                    amPm = "AM";
                    if (hours == 0) {
                        hours = 12;
                    }
                }

                // Format the minutes to always have two digits
                String formattedMinutes = String.format("%02d", minutes);

                timeText.setText(String.format("%d:%s %s", hours, formattedMinutes, amPm));
            }
        }, 12, 00, false);
        dialog.show();
    }

    private void addingSchedule() {
        // Get the input values
        EditText inputSchedName=findViewById(R.id.nameSet);
        EditText inputSchedDesc=findViewById(R.id.descriptionSet);
        EditText inputSchedLink=findViewById(R.id.linkSet);

        TextView inputSchedDate=findViewById(R.id.dateText);
        TextView inputSchedTime=findViewById(R.id.timeText);

        String schedName=inputSchedName.getText().toString();
        String schedDesc=inputSchedDesc.getText().toString();
        String schedLink=inputSchedLink.getText().toString();
        String schedDate=inputSchedDate.getText().toString();
        String schedTime=inputSchedTime.getText().toString();

        String errorMessage="";

        String[] requiredFields={schedName, schedDate, schedDesc, schedTime};
        String[] requiredFieldNames={"Name", "Date", "Description", "Time"};

        for (int i=0; i < requiredFields.length; i++) {
            if (requiredFields[i].isEmpty()) {
                errorMessage+=requiredFieldNames[i] + ", ";
            }
        }

        if (!errorMessage.isEmpty()) {
            errorMessage=errorMessage.substring(0, errorMessage.length() - 2); // tatanggalin yung last ", "
            Toast.makeText(MainActivity2.this, "The following fields cannot be empty: " + errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper dbHelper=new DatabaseHelper(this);
            String currentUsername=dbHelper.getCurrentUsername();

            // save sa database rito
            // add dito yung database code
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values=new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, schedName);
                values.put(DatabaseHelper.COLUMN_DATE, schedDate);
                values.put(DatabaseHelper.COLUMN_DESCRIPTION, schedDesc);
                values.put(DatabaseHelper.COLUMN_TIME, schedTime);
                values.put(DatabaseHelper.COLUMN_LINK, schedLink);
                values.put(DatabaseHelper.COLUMN_USER_FK, currentUsername); //replace it with the actual username tho

                long newRowId=db.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values);

                if (newRowId != -1) {
                    // Insert into AppointmentStatus table
                    ContentValues statusValues=new ContentValues();
                    statusValues.put(DatabaseHelper.COLUMN_USER_FK, currentUsername);
                    statusValues.put(DatabaseHelper.COLUMN_SCHEDID, newRowId);
                    statusValues.put(DatabaseHelper.COLUMN_IS_FINISHED, 0); // 0 indicates unfinished status

                    long statusRowId=db.insert(DatabaseHelper.TABLE_APPOINTMENT_STATUS, null, statusValues);

                    if (statusRowId != -1) {
                        db.setTransactionSuccessful();
                        Toast.makeText(MainActivity2.this, "Saved", Toast.LENGTH_SHORT).show();
                        inputSchedName.setText("");
                        inputSchedDate.setText("");
                        inputSchedTime.setText("");
                        inputSchedDesc.setText("");
                        inputSchedLink.setText("");
                    } else {
                        Toast.makeText(MainActivity2.this, "Error saving schedule status", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity2.this, "Error saving schedule", Toast.LENGTH_SHORT).show();
                }
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
}