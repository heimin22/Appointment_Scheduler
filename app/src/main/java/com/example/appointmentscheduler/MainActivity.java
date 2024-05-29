package com.example.appointmentscheduler;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    // request code for notification permission
    private static final int NOTIFICATION_PERMISSION_CODE = 123;
    private TextView dateTextView;
    private Button pickDateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        main_layout.setBackgroundColor(Color.parseColor("#F8F9FA"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setDefaultFont();
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