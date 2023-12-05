package com.example.rentahelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.model.Service;

import java.util.List;

public class AcceptedServiceAdapter extends RecyclerView.Adapter<AcceptedServiceAdapter.ViewHolder> {
    private final List<Service> serviceList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AcceptedServiceAdapter(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedServiceAdapter.ViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.titleTextView.setText(service.getTitle());
        holder.statusTextView.setText(service.getStatus().name());
        holder.priceTextView.setText(String.valueOf(service.getPrice()));
        holder.dateTextView.setText(service.getAvailability());
        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
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
