package com.example.rentahelp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentahelp.MainActivity;
import com.example.rentahelp.R;
import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.Service;
import com.example.rentahelp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompletedFragment extends Fragment {
    private static final String TAG = CompletedFragment.class.getSimpleName();
    private final Service service;
    private User acceptedByUser;

    public CompletedFragment(Service service) {
        this.service = service;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView dobTextView = view.findViewById(R.id.dobTextView);
        TextView phoneTextView = view.findViewById(R.id.phoneTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText reviewEditText = view.findViewById(R.id.reviewEditText);
        Button postFeedbackButton = view.findViewById(R.id.postFeedbackButton);

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acceptedByUser = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user != null && service.getAcceptedBy().equals(user.getUserId())) {
                        acceptedByUser = user;
                        nameTextView.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                        dobTextView.setText("Date of Birth: " + user.getDob());
                        phoneTextView.setText("Phone: " + user.getPhoneNumber());
                        emailTextView.setText("Email: " + user.getEmail());
                        ratingTextView.setText("Average Rating: " + (user.getRatingCount() != 0 ? user.getRatingTotal() / user.getRatingCount() : "N/A"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database Error");
            }
        });

        postFeedbackButton.setOnClickListener(v -> {
            double rating = ratingBar.getRating();
            String review = reviewEditText.getText().toString();

            if (TextUtils.isEmpty(review)) {
                Toast.makeText(requireContext(), "Invalid review.", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
            alertDialog.setTitle("Post Feedback")
                    .setMessage("Do you want to post feedback?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        DatabaseReference servicesReference = FirebaseDatabase.getInstance().getReference("Services");
                        servicesReference.child(service.getServiceId()).child("rating").setValue(rating);
                        servicesReference.child(service.getServiceId()).child("review").setValue(review);

                        usersReference.child(acceptedByUser.getUserId()).child("ratingTotal").setValue(acceptedByUser.getRatingTotal() + rating);
                        usersReference.child(acceptedByUser.getUserId()).child("ratingCount").setValue(acceptedByUser.getRatingCount() + 1);

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                            String notificationKey = notificationsReference.push().getKey();
                            Notification notification = new Notification(currentUser.getUid(), "Feedback posted for " + service.getTitle() + ".");
                            if (notificationKey != null) {
                                notificationsReference.child(notificationKey).setValue(notification);
                            }
                        }

                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        requireContext().startActivity(intent);
                    })
                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
