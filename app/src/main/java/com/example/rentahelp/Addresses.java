package com.example.rentahelp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Addresses extends AppCompatActivity {
    private static final String TAG = Addresses.class.getSimpleName();
    private AutoCompleteTextView autoCompleteTextView;
    private Button buttonSave;
    private Button buttonAddAddress;
    private ListView listViewAddresses;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<String> addressesList;
    private ArrayAdapter<String> addressesAdapter;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Addresses");

        addressesList = new ArrayList<>();
        addressesAdapter = new AddressesAdapter(this, addressesList);
        listViewAddresses = findViewById(R.id.listViewAddresses);
        listViewAddresses.setAdapter(addressesAdapter);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        buttonSave = findViewById(R.id.buttonSave);
        buttonAddAddress = findViewById(R.id.buttonAddAddress);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Place place = (Place) parent.getItemAtPosition(position);
            String address = place.getAddress();
            autoCompleteTextView.setText(address);
        });

        buttonSave.setOnClickListener(v -> saveAddress());
        buttonAddAddress.setOnClickListener(v -> startAutocompleteActivity());

        Places.initialize(getApplicationContext(), "AIzaSyCh55s6m4p956OVc1ZmJphTNtowUrhYJsQ");
        listViewAddresses.setOnItemClickListener((parent, view, position, id) -> {
        });
        loadAddressesFromFirebase();
    }

    private void loadAddressesFromFirebase() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userAddressesRef = databaseReference.child(userId);

        userAddressesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addressesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String address = snapshot.getValue(String.class);
                    addressesList.add(address);
                }
                addressesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database Error");
            }
        });
    }

    private class AddressesAdapter extends ArrayAdapter<String> {
        public AddressesAdapter(Context context, List<String> addresses) {
            super(context, R.layout.item_address, addresses);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_address, parent, false);
            }

            TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);
            Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

            String address = getItem(position);
            textViewAddress.setText(address);

            buttonDelete.setOnClickListener(v -> deleteAddress(position));

            return convertView;
        }
    }

    private void deleteAddress(int position) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userAddressesRef = databaseReference.child(userId);
        String selectedAddress = addressesList.get(position);
        userAddressesRef.orderByValue().equalTo(selectedAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database Error");
            }
        });
    }

    private void saveAddress() {
        String address = autoCompleteTextView.getText().toString().trim();
        if (!address.isEmpty()) {
            addressesList.add(address);
            saveToFirebase(address);
            autoCompleteTextView.setText("");
        }
    }

    private void saveToFirebase(String address) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference userAddressesRef = databaseReference.child(userId);
        userAddressesRef.push().setValue(address);
        Toast.makeText(this, "Data stored", Toast.LENGTH_SHORT).show();
    }

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("CA")  // Set your country code
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();
                autoCompleteTextView.setText(address);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "Autocomplete error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
