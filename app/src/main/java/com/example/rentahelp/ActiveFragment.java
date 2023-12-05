package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class ActiveFragment extends Fragment {
    private static final String TAG = ActiveFragment.class.getSimpleName();
    private final Service service;
    private User postedByUser = null;
    private User acceptedByUser = null;

    public ActiveFragment(Service service) {
        this.service = service;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView dobTextView = view.findViewById(R.id.dobTextView);
        TextView phoneTextView = view.findViewById(R.id.phoneTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        Button payNowButton = view.findViewById(R.id.payNowButton);

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

        payNowButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
            if (postedByUser != null && acceptedByUser != null && postedByUser.getCredits() >= service.getPrice()) {
                alertDialog.setTitle("Payment Confirmation")
                        .setMessage("Do you want to proceed with the payment?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            usersReference.child(postedByUser.getUserId()).child("credits").setValue(postedByUser.getCredits() - service.getPrice());
                            usersReference.child(acceptedByUser.getUserId()).child("credits").setValue(acceptedByUser.getCredits() + service.getPrice());

                            DatabaseReference servicesReference = FirebaseDatabase.getInstance().getReference("Services");
                            servicesReference.child(service.getServiceId()).child("status").setValue(Status.COMPLETED.name());

                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            requireContext().startActivity(intent);
                        })
                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                alertDialog.setTitle("Insufficient Credits")
                        .setMessage("You have insufficient credits to make this payment.")
                        .setPositiveButton("OK", (dialog, which) -> {
                        })
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
