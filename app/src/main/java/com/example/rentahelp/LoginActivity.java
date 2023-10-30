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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    static String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerNowTextView = findViewById(R.id.registerNowTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString(getString(R.string.preferences_email_key), "");
        emailEditText.setText(savedEmail);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password) || password.length() < 6) {
                Toast.makeText(this, "Invalid password (minimum 6 characters).", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Login successful.");
                            Toast.makeText(this, "Login successful." , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.preferences_email_key), email);
            editor.apply();
        });

        registerNowTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}