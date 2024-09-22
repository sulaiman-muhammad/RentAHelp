package com.example.rentahelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WalletActivity extends AppCompatActivity {
    private static final String TAG = WalletActivity.class.getSimpleName();
    private double credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        TextView creditsTextView = findViewById(R.id.creditsTextView);
        EditText amountEditText = findViewById(R.id.amountEditText);
        Button addMoneyButton = findViewById(R.id.addMoneyButton);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
            usersReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        if (user != null && user.getUserId().equals(currentUser.getUid())) {
                            credits = user.getCredits();
                            creditsTextView.setText("Credits: $" + credits);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database Error");
                }
            });
        }

        addMoneyButton.setOnClickListener(view -> {
            double amount = Double.parseDouble(amountEditText.getText().toString());
            if (currentUser != null) {
                double amountToAdd = credits + amount;

                DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
                usersReference.child(currentUser.getUid()).child("credits").setValue(amountToAdd);

                DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                String notificationKey = notificationsReference.push().getKey();
                Notification notification = new Notification(currentUser.getUid(), "You added $" + amount + ". Your current balance is $" + amountToAdd);
                if (notificationKey != null) {
                    notificationsReference.child(notificationKey).setValue(notification);
                }
            }
        });

    }
}