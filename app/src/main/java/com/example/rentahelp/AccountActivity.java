package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = AccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView ratingTextView = findViewById(R.id.ratingTextView);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
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

        Button verifyEmailButton = findViewById(R.id.verifyEmailButton);
        verifyEmailButton.setOnClickListener(view -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null && !currentUser.isEmailVerified()) {
                currentUser.sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(view, "Email verification link sent.", Snackbar.LENGTH_SHORT).show();
                        DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                        String notificationKey = notificationsReference.push().getKey();
                        Notification notification = new Notification(currentUser.getUid(), "Email verification link sent successfully.");
                        if (notificationKey != null) {
                            notificationsReference.child(notificationKey).setValue(notification);
                        }
                    } else {
                        Snackbar.make(view, "Failed to send email verification link.", Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(view, "Email already verified.", Snackbar.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userReference = databaseReference.child(userId);
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userData = snapshot.getValue(User.class);
                    if (userData != null) {
                        nameTextView.setText(userData.getFirstName() + " " + userData.getLastName());
                        ratingTextView.setText("Average Rating: " + (userData.getRatingCount() != 0 ? userData.getRatingTotal() / userData.getRatingCount() : "N/A"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database Error");
                }
            });
        }

        Button editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        Button myAddressesButton = findViewById(R.id.myAddressesButton);
        myAddressesButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, Addresses.class);
            startActivity(intent);
        });

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                String notificationKey = notificationsReference.push().getKey();
                Notification notification = new Notification(currentUser.getUid(), "Logout succesful.");
                if (notificationKey != null) {
                    notificationsReference.child(notificationKey).setValue(notification);
                }
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Logout successful.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}