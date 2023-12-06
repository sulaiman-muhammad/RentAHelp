package com.example.rentahelp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.rentahelp.model.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AcceptedServiceActivity extends AppCompatActivity {
    private static final String TAG = AcceptedServiceActivity.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_service);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView availabilityTextView = findViewById(R.id.availabilityTextView);
        TextView startTimeTextView = findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = findViewById(R.id.endTimeTextView);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView reviewTextView = findViewById(R.id.reviewTextView);

        Service service = (Service) getIntent().getSerializableExtra("Service");
        if (service != null) {
            titleTextView.setText(service.getTitle());
            priceTextView.setText("Price: $" + service.getPrice());
            descriptionTextView.setText("Description: " + service.getDescription());
            availabilityTextView.setText("Availability: " + service.getAvailability());
            startTimeTextView.setText("Start Time: " + service.getStartTime());
            endTimeTextView.setText("End Time: " + service.getEndTime());
            ratingBar.setRating((float) service.getRating());
            reviewTextView.setText("Review: " + (service.getReview() != null ? service.getReview() : "N/A"));
        }

        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.chatItem) {
                Log.d(TAG, "Chat Selected");
                Intent intent = new Intent(this, UsersChatActivity.class);
                startActivity(intent);
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