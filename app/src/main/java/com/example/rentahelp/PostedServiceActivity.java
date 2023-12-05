package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentahelp.model.Service;
import com.example.rentahelp.model.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostedServiceActivity extends AppCompatActivity {
    private static final String TAG = PostServiceActivity.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_service);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView availabilityTextView = findViewById(R.id.availabilityTextView);
        TextView startTimeTextView = findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = findViewById(R.id.endTimeTextView);
        TextView bottomFrameLayoutHeadingTextView = findViewById(R.id.bottomFrameLayoutHeading);

        Service service = (Service) getIntent().getSerializableExtra("Service");

        if (service != null) {
            titleTextView.setText(service.getTitle());
            priceTextView.setText("Price: $" + service.getPrice());
            descriptionTextView.setText("Description: " + service.getDescription());
            availabilityTextView.setText("Availability: " + service.getAvailability());
            startTimeTextView.setText("Start Time: " + service.getStartTime());
            endTimeTextView.setText("End Time: " + service.getEndTime());

            if (service.getStatus() == Status.INITIATED) {
                bottomFrameLayoutHeadingTextView.setText("Waiting for an agent to accept your request...");
                InitiatedFragment initiatedFragment = new InitiatedFragment(service);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottomFrameLayout, initiatedFragment)
                        .commit();
            } else if (service.getStatus() == Status.ACTIVE) {
                bottomFrameLayoutHeadingTextView.setText("Agent Details:");
                ActiveFragment activeFragment = new ActiveFragment(service);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottomFrameLayout, activeFragment)
                        .commit();
            } else {
                bottomFrameLayoutHeadingTextView.setText("Agent Details:");
                CompletedFragment completedFragment = new CompletedFragment(service);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottomFrameLayout, completedFragment)
                        .commit();
            }
        }

        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.chatItem) {
                Log.d(TAG, "Chat Selected");
            } else if (itemId == R.id.homeItem) {
                Log.d(TAG, "Home Selected");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.profileItem) {
                Log.d(TAG, "Profile Selected");
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
            }
            return true;
        });
    }
}