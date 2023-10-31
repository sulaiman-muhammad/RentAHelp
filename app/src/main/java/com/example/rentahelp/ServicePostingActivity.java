package com.example.rentahelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServicePostingActivity extends AppCompatActivity {

    static String TAG = ServicePostingActivity.class.getName();
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_posting);

        Spinner titleSpinner = findViewById(R.id.titleSpinner);
        EditText descriptionEditText = findViewById(R.id.editTextDescription);
        EditText priceEditText = findViewById(R.id.editTextPrice);
        Button postButton = findViewById(R.id.buttonPost1);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_item,
                R.id.customSpinnerItemText,
                getResources().getTextArray(R.array.title_options));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        titleSpinner.setAdapter(adapter);
        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                title = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //TODO: Handling when nothing is selected
            }
        });

        postButton.setOnClickListener(view -> {
            String description = descriptionEditText.getText().toString();
            double price = Double.parseDouble(priceEditText.getText().toString());

            Service service = new Service(title, description, price);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
            String serviceKey = databaseReference.push().getKey();
            if (serviceKey != null) {
                databaseReference.child(serviceKey).setValue(service);
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}