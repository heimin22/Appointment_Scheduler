// PROFILE CLASS

package com.example.appointmentscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

//        //no transitions
//        overridePendingTransition(0, 0);


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

        dbHelper = new DatabaseHelper(this);

        getCurrentUserName();
        getCurrentScheduleCount();
    }

    private void getCurrentScheduleCount() {
        TextView schedCount = findViewById(R.id.schedCount);

        String currentUsername = dbHelper.getCurrentUsername();
//        String totalSchedCount = dbHelper.getCurrentSchedCount(currentUsername);
//        if (totalSchedCount != null) {
//            schedCount.setText(totalSchedCount);
//        }
//        else {
//            schedCount.setText("0");
//        }

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