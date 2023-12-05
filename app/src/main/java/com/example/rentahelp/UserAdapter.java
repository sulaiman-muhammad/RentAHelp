package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.model.Service;
import com.example.rentahelp.model.Status;
import com.example.rentahelp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final Context context;
    private final List<User> userList;
    private final Service service;

    public UserAdapter(Context context, List<User> userList, Service service) {
        this.context = context;
        this.userList = userList;
        this.service = service;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText("Name: " + user.getFirstName() + " " + user.getLastName());
        holder.dobTextView.setText("Date of Birth: " + user.getDob());
        holder.phoneTextView.setText("Phone: " + user.getPhoneNumber());
        holder.emailTextView.setText("Email: " + user.getEmail());
        holder.ratingTextView.setText("Rating: 0");

        holder.itemView.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Do you want to select " + user.getFirstName() + " " + user.getLastName() + "?")
                    .setPositiveButton("YES", (alertDialogInterface, id) -> {
                        DatabaseReference servicesReference = FirebaseDatabase.getInstance().getReference("Services");
                        servicesReference.child(service.getServiceId()).child("acceptedBy").setValue(user.getUserId());
                        servicesReference.child(service.getServiceId()).child("status").setValue(Status.ACTIVE.name());

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView dobTextView;
        public TextView phoneTextView;
        public TextView emailTextView;
        public TextView ratingTextView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            dobTextView = view.findViewById(R.id.dobTextView);
            phoneTextView = view.findViewById(R.id.phoneTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            ratingTextView = view.findViewById(R.id.ratingTextView);
        }
    }

}
