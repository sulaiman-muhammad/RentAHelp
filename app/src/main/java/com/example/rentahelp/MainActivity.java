package com.example.rentahelp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentahelp.adapter.AcceptedServiceAdapter;
import com.example.rentahelp.adapter.PostedServiceAdapter;
import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.Service;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        MaterialToolbar topToolbar = findViewById(R.id.topToolbar);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        RecyclerView postedRecyclerView = findViewById(R.id.postedRecyclerView);
        RecyclerView acceptedRecyclerView = findViewById(R.id.acceptedRecyclerView);

        topToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.findItem) {
                Log.d(TAG, "Find Selected");
                Intent intent = new Intent(this, FindServiceActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.postItem) {
                Log.d(TAG, "Post Selected");
                Intent intent = new Intent(this, PostServiceActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.aboutItem) {
                Log.d(TAG, "About Selected");
                AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
                aboutDialog.setTitle("Authors")
                        .setMessage("Version 1.0, by Sulaiman, Rishabh, Anusha, Nandhini and Gowtham.")
                        .setPositiveButton("OK", (aboutDialogInterface, id) -> {})
                        .show();
                return true;
            } else if (itemId == R.id.notificationsItem) {
                Log.d(TAG, "Notification Selected");
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.logoutItem) {
                Log.d(TAG, "Logout Selected");
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
                return true;
            } else {
                return false;
            }
        });

        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.chatItem) {
                Log.d(TAG, "Chat Selected");
                Intent intent = new Intent(this, UsersChatActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.homeItem) {
                Log.d(TAG, "Home Selected");
            } else if (itemId == R.id.profileItem) {
                Log.d(TAG, "Profile Selected");
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
            }
            return true;
        });

        List<Service> postedServiceList = new ArrayList<>();
        PostedServiceAdapter postedServiceAdapter = new PostedServiceAdapter(this, postedServiceList);
        postedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postedRecyclerView.setAdapter(postedServiceAdapter);

        List<Service> acceptedServiceList = new ArrayList<>();
        AcceptedServiceAdapter acceptedServiceAdapter = new AcceptedServiceAdapter(this, acceptedServiceList);
        acceptedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        acceptedRecyclerView.setAdapter(acceptedServiceAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postedServiceList.clear();
                acceptedServiceList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    if (service != null && service.getPostedBy() != null && currentUser != null && service.getPostedBy().equals(currentUser.getUid())) {
                        postedServiceList.add(service);
                    }
                    if (service != null && service.getAcceptedBy() != null && currentUser != null && service.getAcceptedBy().equals(currentUser.getUid())) {
                        acceptedServiceList.add(service);
                    }
                }
                postedServiceAdapter.notifyDataSetChanged();
                acceptedServiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database Error");
            }
        });
    }

}