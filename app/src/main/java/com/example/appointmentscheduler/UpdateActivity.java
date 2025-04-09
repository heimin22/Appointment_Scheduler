package com.example.appointmentscheduler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    TextView sched_date_txt, sched_time_txt;
    EditText sched_name_txt, sched_desc_txt, sched_link_txt;
    Button date_picker_btn, time_picker_btn, save_btn, back_btn;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch edit_switch;
    ImageButton delete_btn;
    CheckBox isFinishedCheckbox;
    String id, name, date, time, desc, link;
    DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        dbHelper = new DatabaseHelper(UpdateActivity.this);

        sched_date_txt = findViewById(R.id.dateText2);
        sched_time_txt = findViewById(R.id.timeText2);

        sched_name_txt = findViewById(R.id.nameSet2);
        sched_desc_txt = findViewById(R.id.descriptionSet2);
        sched_link_txt = findViewById(R.id.linkSet2);

        date_picker_btn = findViewById(R.id.datePicker2);
        time_picker_btn = findViewById(R.id.timePicker2);
        back_btn = findViewById(R.id.btn_back);

        edit_switch = findViewById(R.id.edit_toggle);

        save_btn = findViewById(R.id.saveEditSchedButton);

        delete_btn = findViewById(R.id.deleteButton);

        isFinishedCheckbox = findViewById(R.id.checkFinish);

        //Execute Intent (setting/getting values of clicked schedule)
        getIntentData();

        //DB PREPARATION
        dbHelper = new DatabaseHelper(UpdateActivity.this);

        //Disable EditText, Button, Time/Date Picker
        disableEditText(sched_name_txt, sched_desc_txt, sched_link_txt);
        disableSave();
        disableTimeDatePicker();


        edit_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    enableSave();
                    enableTimeDatePicker();
                    enableEditText(sched_name_txt, sched_desc_txt, sched_link_txt);
                    disableDelete();
                } else {
                    //Disable EditText, Button, Time/Date Picker
                    disableEditText(sched_name_txt, sched_desc_txt, sched_link_txt);
                    disableSave();
                    enableDelete();
                    disableTimeDatePicker();
                    getIntentData();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //confirmation dialog
                confirmDeleteDialog();
            }
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });

        time_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Saves contents from text fields to string values
                name = sched_name_txt.getText().toString();
                date = sched_date_txt.getText().toString();
                time = sched_time_txt.getText().toString();
                desc = sched_desc_txt.getText().toString();
                link = sched_link_txt.getText().toString();
                boolean isFinished = isFinishedCheckbox.isChecked();

                dbHelper.updateSchedule(id, name, date, time, desc, link);
                dbHelper.updateAppointmentStatus(id, isFinished);

                finish();
            }
        });

        isFinishedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dbHelper.updateAppointmentStatus(id, isChecked);
            }
        });

    }

    void getIntentData() {
        if(getIntent().hasExtra("id" ) && getIntent().hasExtra("name" ) &&
                getIntent().hasExtra("date" ) && getIntent().hasExtra("time" ) &&
                getIntent().hasExtra("desc" ) && getIntent().hasExtra("link" )) {

            //Getting the values from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            desc = getIntent().getStringExtra("desc");
            link = getIntent().getStringExtra("link");

            //Setting Intent datas
            sched_name_txt.setText(name);
            sched_date_txt.setText(date);
            sched_time_txt.setText(time);
            sched_desc_txt.setText(desc);
            sched_link_txt.setText(link);

            boolean isFinished = dbHelper.isAppointmentFinished(id);
            isFinishedCheckbox.setChecked(isFinished);

        } else {
            Toast.makeText(this, "Error in finding this card view.", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableEditText(EditText name_txt, EditText desc_txt, EditText link_txt) {

        //meet_name_txt
        name_txt.setFocusable(false);
        name_txt.setFocusableInTouchMode(false);
        name_txt.setClickable(false);
        name_txt.setLongClickable(false);

        //meet_desc_txt
        desc_txt.setFocusable(false);
        desc_txt.setFocusableInTouchMode(false);
        desc_txt.setClickable(false);
        desc_txt.setLongClickable(false);

        //meet_link_txt
        link_txt.setFocusable(false);
        link_txt.setFocusableInTouchMode(false);
        link_txt.setClickable(false);
        link_txt.setLongClickable(false);

    }

    private void enableEditText(EditText name_txt, EditText desc_txt, EditText link_txt) {

        //meet_name_txt
        name_txt.setFocusable(true);
        name_txt.setFocusableInTouchMode(true);
        name_txt.setClickable(true);
        name_txt.setLongClickable(true);

        //meet_desc_txt
        desc_txt.setFocusable(true);
        desc_txt.setFocusableInTouchMode(true);
        desc_txt.setClickable(true);
        desc_txt.setLongClickable(true);

        //meet_link_txt
        link_txt.setFocusable(true);
        link_txt.setFocusableInTouchMode(true);
        link_txt.setClickable(true);
        link_txt.setLongClickable(true);
    }
    void disableTimeDatePicker() {
        date_picker_btn.setVisibility(View.GONE);
        time_picker_btn.setVisibility(View.GONE);
    }
    void enableTimeDatePicker() {
        date_picker_btn.setVisibility(View.VISIBLE);
        time_picker_btn.setVisibility(View.VISIBLE);
    }

    void disableSave() {
        save_btn.setVisibility(View.GONE);
    }
    void enableSave() {
        save_btn.setVisibility(View.VISIBLE);
    }

    void disableDelete() {
        delete_btn.setVisibility(View.GONE);
    }
    void enableDelete() {
        delete_btn.setVisibility(View.VISIBLE);
    }

    private void dateDialog() {

        TextView dateText = findViewById(R.id.dateText2);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String formattedMonth = String.format("%02d", month + 1);
                String formattedDay = String.format("%02d", day);

                dateText.setText(year + "-" + formattedMonth + "-" + formattedDay);
            }
        }, 2024, 0, 1);
        dialog.show();
    }
    private void timeDialog() {

        TextView timeText = findViewById(R.id.timeText2);

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
    private void confirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper = new DatabaseHelper(UpdateActivity.this);
                dbHelper.deleteRowSchedule(id);
                dbHelper.deleteAppointmentStatus(id);

                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
}