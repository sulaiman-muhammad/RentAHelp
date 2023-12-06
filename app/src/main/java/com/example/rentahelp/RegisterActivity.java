package com.example.rentahelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText firstNameEditText = findViewById(R.id.firstNameEditText);
        EditText lastNameEditText = findViewById(R.id.lastNameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button registerButton = findViewById(R.id.registerButton);
        TextView loginNowTextView = findViewById(R.id.loginNowTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), MODE_PRIVATE);

        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password) || password.length() < 6) {
                Toast.makeText(this, "Invalid password (minimum 6 characters).", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.equals(password, confirmPassword)) {
                Toast.makeText(this, "Password and confirm password don't match.", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Register successful.");
                            Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();

                            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
                            DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                            FirebaseUser currentUser = task.getResult().getUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                User user = new User(userId, firstName, lastName, null, email, null, 0.0, 0.0, 0);
                                usersReference.child(userId).setValue(user);
                                String notificationKey = notificationsReference.push().getKey();
                                Notification notification = new Notification(userId, "Account created.");
                                if (notificationKey != null) {
                                    notificationsReference.child(notificationKey).setValue(notification);
                                }
                            }

                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);

                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.preferences_email_key), email);
            editor.apply();
        });

        loginNowTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}