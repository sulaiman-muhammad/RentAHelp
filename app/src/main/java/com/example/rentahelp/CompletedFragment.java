package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import com.example.rentahelp.model.Service;
import com.example.rentahelp.model.Status;
import com.example.rentahelp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompletedFragment extends Fragment {
    private static final String TAG = CompletedFragment.class.getSimpleName();
    private final Service service;
    private User postedByUser = null;
    private User acceptedByUser = null;

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
                postedByUser = null;
                acceptedByUser = null;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user != null && service.getPostedBy().equals(user.getUserId())) {
                        postedByUser = user;
                    }
                    if (user != null && service.getAcceptedBy().equals(user.getUserId())) {
                        acceptedByUser = user;
                        nameTextView.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                        dobTextView.setText("Date of Birth: " + user.getDob());
                        phoneTextView.setText("Phone: " + user.getPhoneNumber());
                        emailTextView.setText("Email: " + user.getEmail());
                        ratingTextView.setText("Rating: 0");
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
