package com.example.rentahelp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar topToolbar = findViewById(R.id.topToolbar);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);

        topToolbar.setNavigationContentDescription("LeftAlign");
        topToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.findItem) {
                Log.d(TAG, "Find Selected");
                Intent intent = new Intent(this, FindServiceActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.postItem) {
                Log.d(TAG, "Post Selected");
                Intent intent = new Intent(this, PostServiceActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.aboutItem) {
                Log.d(TAG, "About Selected");
                AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
                aboutDialog.setTitle("Authors")
                        .setMessage("Version 1.0, by Sulaiman, Rishabh, Anusha, Nandhini and Gowtham.")
                        .setPositiveButton("OK", (aboutDialogInterface, id) -> {})
                        .show();
                return true;
            } else if (itemId == R.id.notificationsItem) {
                Log.d(TAG, "Notifications Selected");
                return true;
            } else if (itemId == R.id.logoutItem) {
                Log.d(TAG, "Logout Selected");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Logout successful.", Toast.LENGTH_SHORT).show();
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
            } else if (itemId == R.id.homeItem) {
                Log.d(TAG, "Home Selected");
            } else if (itemId == R.id.profileItem) {
                Log.d(TAG, "Profile Selected");
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
            }
            return true;
        });
    }

}