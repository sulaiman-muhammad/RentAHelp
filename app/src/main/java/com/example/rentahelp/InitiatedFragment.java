package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.model.Service;
import com.example.rentahelp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InitiatedFragment extends Fragment {
    private static final String TAG = InitiatedFragment.class.getSimpleName();
    private final Service service;

    public InitiatedFragment(Service service) {
        this.service = service;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initiated, container, false);

        RecyclerView userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<User> userList = new ArrayList<>();
        if (service.getPotential() != null) {
            List<String> potentialList = service.getPotential();
            UserAdapter userAdapter = new UserAdapter(requireContext(), userList, service);
            userRecyclerView.setAdapter(userAdapter);
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
            usersReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        if (user != null && potentialList.contains(user.getUserId())) {
                            userList.add(user);
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database Error");
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
