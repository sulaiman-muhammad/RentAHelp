package com.example.rentahelp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.model.Service;

import java.util.List;

public class PostedServiceAdapter extends RecyclerView.Adapter<PostedServiceAdapter.ViewHolder> {
    private final Context context;
    private final List<Service> serviceList;

    public PostedServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedServiceAdapter.ViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.titleTextView.setText(service.getTitle());
        holder.statusTextView.setText(service.getStatus().name());
        holder.priceTextView.setText(String.valueOf(service.getPrice()));
        holder.dateTextView.setText(service.getAvailability());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostedServiceActivity.class);
            intent.putExtra("Service", service);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView statusTextView;
        public TextView priceTextView;
        public TextView dateTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleTextView);
            statusTextView = view.findViewById(R.id.statusTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
        }
    }
}
