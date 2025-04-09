// PROFILE CLASS

package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    RecyclerView finished_recycler;
    FinishedSchedAdapter finishedAdapter;
    ArrayList<String> array_name, array_date, array_time, array_desc, array_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ImageButton homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ImageButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        dbHelper = new DatabaseHelper(MainActivity3.this);

        getCurrentUserName();
        getCurrentScheduleCount();
        getCurrentFinishedScheduleCount();

        // Playground ni kho

        finished_recycler = findViewById(R.id.finished_sched_recycler);
        String user_name = dbHelper.getCurrentUsername();

        array_name = new ArrayList<>();
        array_date = new ArrayList<>();
        array_time = new ArrayList<>();
        array_desc = new ArrayList<>();
        array_link = new ArrayList<>();

        storeFinishedSched(user_name);

        finishedAdapter = new FinishedSchedAdapter(MainActivity3.this, array_name, array_date, array_time, array_desc, array_link);
        finished_recycler.setLayoutManager(new LinearLayoutManager(MainActivity3.this));
        finished_recycler.setAdapter(finishedAdapter);

        // Playground ni kho

    }

    void storeFinishedSched(String name) {
        Cursor cursor = dbHelper.readFinishedSchedule(name);
        if(cursor.getCount()==0){

        } else {
            while(cursor.moveToNext()) {
                array_name.add(cursor.getString(1));
                array_date.add(cursor.getString(2));
                array_desc.add(cursor.getString(3));
                array_time.add(cursor.getString(4));
                array_link.add(cursor.getString(5));
            }
        }
    }

    private void getCurrentFinishedScheduleCount() {
        TextView finishedSchedCount = findViewById(R.id.finishedCount);
        String currentUsername = dbHelper.getCurrentUsername();

        if (currentUsername != null) {
            int finishedCount = dbHelper.getFinishedScheduleCount(currentUsername);
            finishedSchedCount.setText(String.valueOf(finishedCount));
        }
        else {
            finishedSchedCount.setText("0");
        }
    }

    private void getCurrentScheduleCount() {
        TextView schedCount = findViewById(R.id.schedCount);

        String currentUsername = dbHelper.getCurrentUsername();

        Log.d("MainActivity3", "Retrieved current username: " + currentUsername);

        if (currentUsername != null) {
            String totalSchedCount = dbHelper.getCurrentSchedCount(currentUsername);
            Log.d("MainActivity3", "Retrieved schedule count: " + totalSchedCount);
            schedCount.setText(totalSchedCount);
        } else {
            Log.d("MainActivity3", "Current username is null");
            schedCount.setText("0");
        }
    }

    private void getCurrentUserName() {
        dbHelper = new DatabaseHelper(this);

        TextView greetUser = findViewById(R.id.greetUser);

        String username = dbHelper.getCurrentUsername();
        if (username != null) {
            greetUser.setText("Hello, " + username + "!");
        } else {
            greetUser.setText("Hello!");
        }
    }
}