package com.example.rentahelp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    static String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar topToolbar = findViewById(R.id.topToolbar);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        firebaseAuth = FirebaseAuth.getInstance();

        topToolbar.setNavigationContentDescription("LeftAlign");
        topToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.findItem) {
                Log.d(TAG, "Find Selected");
                return true;
            } else if (itemId == R.id.postItem) {
                Log.d(TAG, "Post Selected");
                return true;
            } else if (itemId == R.id.aboutItem) {
                Log.d(TAG, "About Selected");
                Toast.makeText(MainActivity.this, "Version 1.0, by Sulaiman, Rishabh, Anusha, Nandhini and Gowtham.", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.notificationsItem) {
                Log.d(TAG, "Notifications Selected");
                return true;
            } else if (itemId == R.id.logoutItem) {
                Log.d(TAG, "Logout Selected");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Logout successful.", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                return false;
            }
        });

        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.chatItem) {
                Log.d(TAG, "Chat Selected");
                return true;
            } else if (itemId == R.id.homeItem) {
                Log.d(TAG, "Home Selected");
                return true;
            } else if (itemId == R.id.profileItem) {
                Log.d(TAG, "Profile Selected");
                return true;
            } else {
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Logged in as " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }
}