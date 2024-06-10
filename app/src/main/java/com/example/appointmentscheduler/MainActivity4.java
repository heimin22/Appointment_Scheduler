// VIEWING SCHEDULES CLASS

package com.example.appointmentscheduler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity4 extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ArrayList<String> array_id, array_name, array_description, array_date, array_time, array_link;
    ScheduleAdapter meetingAdapter;
    RecyclerView recyclerView;
    ImageView empty_sched_img;
    TextView empty_sched_txt;



    void storeSchedToArray() {
        Cursor cursor = dbHelper.readAllSchedule();
        if(cursor.getCount()==0) {

            empty_sched_img = findViewById(R.id.empty_schedule_img);
            empty_sched_txt = findViewById(R.id.empty_message_txt);

            empty_sched_img.setVisibility(View.VISIBLE);
            empty_sched_txt.setVisibility(View.VISIBLE);

        }
        else {
            while(cursor.moveToNext()) {
                array_id.add(cursor.getString(0));
                array_name.add(cursor.getString(1));
                array_date.add(cursor.getString(2));
                array_description.add(cursor.getString(3));
                array_time.add(cursor.getString(4));
                array_link.add(cursor.getString(5));

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


//      RECYCLER VIEW       //
        recyclerView = findViewById(R.id.meeting_recyclerview);

        dbHelper = new DatabaseHelper(MainActivity4.this);
        array_id = new ArrayList<>();
        array_name = new ArrayList<>();
        array_description = new ArrayList<>();
        array_date = new ArrayList<>();
        array_time = new ArrayList<>();
        array_link = new ArrayList<>();

        storeSchedToArray();

        meetingAdapter = new ScheduleAdapter(MainActivity4.this, this, array_id, array_name,array_date,array_time,array_description,array_link);
        recyclerView.setAdapter(meetingAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity4.this));

//      RECYCLER VIEW       //

        ImageButton homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            recreate();
        }
    }
}