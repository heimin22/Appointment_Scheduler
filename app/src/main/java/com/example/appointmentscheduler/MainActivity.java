// HOME CLASS

package com.example.appointmentscheduler;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.DatePicker;
import java.util.Calendar;
import android.widget.Button;
import android.app.Application;
import android.graphics.Typeface;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    // request code for notification permission
    private static final int NOTIFICATION_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the background color and initializing the default font method
        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        main_layout.setBackgroundColor(Color.parseColor("#F8F9FA"));

        // if the build version is greater than or equal to android Nougat, then the status bar color is set to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // initializing the default font method
        setDefaultFont();

        Button viewAllScheds=findViewById(R.id.viewAllSched);

        viewAllScheds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent);
            }
        });

        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        //adrian

        ImageButton AddButton = findViewById(R.id.addButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        //adrian

        ImageButton homeButton = findViewById(R.id.homeButton);

//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageButton profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }

    private void setDefaultFont() {
        try {
            final Typeface inter = Typeface.createFromAsset(getAssets(), "fonts/inter_regular.ttf");
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField("SERIF");
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, inter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}