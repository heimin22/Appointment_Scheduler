// PROFILE SETTINGS

package com.example.appointmentscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity7 extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);


        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity7.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity7.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity7.this, MainActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity7.this, MainActivity5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        getCurrentUserName();

        LinearLayout editUserName = findViewById(R.id.editUsername);

        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserName();
            }
        });
    }

    private void changeUserName() {
        dbHelper = new DatabaseHelper(this);
        TextView currentUsername = findViewById(R.id.currentUsername);
        String oldUsername = currentUsername.getText().toString();

        View view1 = LayoutInflater.from(MainActivity7.this).inflate(R.layout.dialog_layout, null);
        final TextInputEditText usernameEditText = view1.findViewById(R.id.usernameSet);

        if (usernameEditText == null) {
            Log.e("changeUserName", "TextInputEditText is null. Check the layout file.");
            return;
        }

        // Set a character limit of 10
        int maxLength = 10;

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == maxLength) {
                    Toast.makeText(MainActivity7.this, "Character limit reached", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxLength) {
                    s.delete(maxLength, s.length());
                }
            }
        });

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity7.this)
                .setTitle("Change your username")
                .setView(view1)
                .setPositiveButton("Change", (dialogInterface, i) -> {
                    String newUsername = usernameEditText.getText().toString().trim();
                    if (!newUsername.isEmpty() && !newUsername.equals(oldUsername)) {
                        if (dbHelper.isUsernameTaken(newUsername)) {
                            usernameEditText.setError("The username has already been taken");
                        } else {
                            boolean result = dbHelper.updateUserName(newUsername, oldUsername);
                            if (result) {
                                currentUsername.setText(newUsername);
                                Toast.makeText(MainActivity7.this, "Username updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity7.this, "Error updating username", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        usernameEditText.setError("New username cannot be empty or same as current username");
                    }
                }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).create();
        alertDialog.show();
    }

    private void getCurrentUserName() {
        dbHelper = new DatabaseHelper(this);

        TextView currentUsername = findViewById(R.id.currentUsername);

        String userName = dbHelper.getCurrentUsername();
        if (userName != null) {
            currentUsername.setText(userName);
        }
        else {
            currentUsername.setText("User");
        }
    }
}