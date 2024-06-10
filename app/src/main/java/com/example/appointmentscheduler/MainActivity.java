// HOME CLASS

package com.example.appointmentscheduler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private static final int RC_NOTIFICATION = 99;
    private static final int RC_AUDIO_STORAGE= 100;
    private DatabaseHelper dbHelper;
    private String userName;
    private String name, time, date, description, link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        getLatestSchedule();
//
//        overridePendingTransition(0, 0);

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        Button viewAllScheds=findViewById(R.id.viewAllSched);

        viewAllScheds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton AddButton = findViewById(R.id.addButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }


        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        boolean isUserNameDialogShown = preferences.getBoolean("isUserNameDialogShown", false);

        if (!isUserNameDialogShown) {
            gettingUserName();
        }

        getLatestSchedule();
    }

    private void gettingUserName() {
        dbHelper = new DatabaseHelper(this);

        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null);
        final TextInputEditText usernameEditText = view1.findViewById(R.id.usernameSet);

        int maxLength = 10;

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == maxLength) {
                    Toast.makeText(MainActivity.this, "Character limit reached", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxLength) {
                    s.delete(maxLength, s.length());
                }
            }
        });

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Your username")
                .setView(view1)
                .setCancelable(false)  // Make the dialog non-cancelable
                .setPositiveButton("Enter", null)
                .create();

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                userName = usernameEditText.getText().toString().trim();
                if (!userName.isEmpty()) {  //if not empty
                    if (dbHelper.isUsernameTaken(userName)) {   //if taken
                        usernameEditText.setError("The username has already been taken");
                    } else {    //writes the username
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_USERNAME, userName);
                        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);

                        if (result != -1) { //if saved
                            Toast.makeText(this, "Username saved", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();

                            // Set a flag indicating that the dialog has been shown
                            SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isUserNameDialogShown", true);
                            editor.apply();
                        } else {    //not saved
                            Toast.makeText(this, "Error saving username", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    usernameEditText.setError("Username cannot be empty");
                }
            });
        });


        alertDialog.show();
    }

    private void getLatestSchedule() {
        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.readLatestSchedule();

        TextView timeTextView = findViewById(R.id.timeText);
        TextView dateTextView = findViewById(R.id.dateText);
        TextView meetNameTextView = findViewById(R.id.meetnameText);
        TextView meetDescriptionTextView = findViewById(R.id.meetdescriptionText);
        TextView meetLinkTextView = findViewById(R.id.meetlinkText);

        name = "";
        date = "";
        time = "";
        description = "";
        link = "";

        if (cursor != null && cursor.moveToFirst()) {

            // Read schedule details from cursor and update UI
            name = cursor.getString(1); //column name
            date = cursor.getString(2); //column date
            time = cursor.getString(4); //column time
            description = cursor.getString(3); //column description
            link = cursor.getString(5); //column link

            // Update your UI elements with the retrieved data
            timeTextView.setText(time);
            dateTextView.setText(date);
            meetNameTextView.setText(name);
            meetDescriptionTextView.setText(description);
            meetLinkTextView.setText(link);
        } else {

            timeTextView.setText("");
            dateTextView.setText("");
            meetNameTextView.setText("");
            meetDescriptionTextView.setText("");
            meetLinkTextView.setText("");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // Show a message explaining why the permission is needed and how to grant it
//                Toast.makeText(this, "Please grant notification permission to receive reminders", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, RC_NOTIFICATION);
        } else {
            requestAudioStoragePermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestAudioStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_AUDIO)) {
                // Show a message explaining why the permission is needed and how to grant it
//                Toast.makeText(this, "Please grant audio storage permission for sound configurations", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, RC_AUDIO_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults.length > 0) {
            switch (requestCode) {
                case RC_NOTIFICATION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Notifications are allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Notifications are denied", Toast.LENGTH_SHORT).show();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestAudioStoragePermission();
                    }
                    break;

                case RC_AUDIO_STORAGE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Audio storage permission granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Audio storage permission denied", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
            }
        }
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        int displayedYear = selectedDate.getYear();
        int displayedMonth = selectedDate.getMonthValue() - 1;

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, displayedYear, displayedMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }


    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}