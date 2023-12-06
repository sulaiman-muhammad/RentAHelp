package com.example.rentahelp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.R;
import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private final Context context;
    private List<Service> serviceList;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.titleTextView.setText(service.getTitle());
        holder.priceTextView.setText(String.valueOf(service.getPrice()));

        holder.itemView.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup_layout);

            TextView titleTextView = dialog.findViewById(R.id.dialogTitleTextView);
            TextView descriptionTextView = dialog.findViewById(R.id.dialogDescriptionTextView);
            TextView PriceTextView = dialog.findViewById(R.id.dialogPriceTextView);
            TextView DateTextView = dialog.findViewById(R.id.dialogDateTextView);
            TextView StartTimeTextView = dialog.findViewById(R.id.dialogStartTimeTextView);
            TextView EndTimeTextView = dialog.findViewById(R.id.dialogEndTimeTextView);
            TextView AddressTextView = dialog.findViewById(R.id.dialogAddressTextView);

            titleTextView.setText(service.getTitle());
            descriptionTextView.setText(service.getDescription());
            PriceTextView.setText(String.valueOf(service.getPrice()));
            DateTextView.setText("Availability: " + service.getAvailability());
            StartTimeTextView.setText("Start Time: " + service.getStartTime());
            EndTimeTextView.setText("End Time: " + service.getEndTime());
            AddressTextView.setText(service.getAddress());

            Button acceptButton = dialog.findViewById(R.id.acceptButton);
            acceptButton.setOnClickListener(v -> {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && currentUser.isEmailVerified()) {
                    String userId = currentUser.getUid();
                    if (service.getPotential() != null && !service.getPotential().contains(userId)) {
                        service.getPotential().add(userId);
                        Toast.makeText(context, "You requested to accept this job", Toast.LENGTH_SHORT).show();
                    } else {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(userId);
                        service.setPotential(tempList);
                    }
                    DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference("Services");
                    serviceRef.child(service.getServiceId()).setValue(service);

                    DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                    String notificationKey = notificationsReference.push().getKey();
                    Notification notification = new Notification(userId, "Request sent for " + service.getTitle() + ".");
                    if (notificationKey != null) {
                        notificationsReference.child(notificationKey).setValue(notification);
                    }
                } else {
                    Toast.makeText(context, "Kindly verify your email.", Toast.LENGTH_SHORT).show();
                }
            });

            Button closeButton = dialog.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        });


        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        int margin = 16;
        layoutParams.setMargins(margin, margin, margin, margin);
        holder.itemView.setLayoutParams(layoutParams);
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void updateList(List<Service> filteredList) {
        this.serviceList = filteredList;
        notifyDataSetChanged();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView priceTextView;

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        private OnItemClickListener mListener;

        public ServiceViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleTextView);
            priceTextView = view.findViewById(R.id.priceTextView);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}