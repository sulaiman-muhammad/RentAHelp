package com.example.rentahelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.text.InputFilter;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Stack;

public class ServicePostingActivity extends AppCompatActivity {

    static String TAG = ServicePostingActivity.class.getName();
    private String title_post;
    private EditText description_post;
    private EditText price_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_posting);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // Enable disk persistence if needed

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference servicesReference = database.getReference("Services");

        // Find the Spinner by its ID
        Spinner titleSpinner = findViewById(R.id.titleSpinner);

// Create a custom ArrayAdapter with the custom layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this,
                R.layout.custom_spinner_item, // The custom layout for the Spinner items
                R.id.customSpinnerItemText,   // The TextView in the custom layout
                getResources().getTextArray(R.array.title_options)
        );

// Apply the custom adapter to the Spinner
        titleSpinner.setAdapter(adapter);



        // Find the Spinner by its ID
//        Spinner titleSpinner = findViewById(R.id.titleSpinner);

        // Create an ArrayAdapter using the string array from resources
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.title_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
//        titleSpinner.setAdapter(adapter);

        // Set the initial selection to the second item (position 1)
//        titleSpinner.setSelection(0); // Change 1 to the desired position

        // Optional: Set an OnItemSelectedListener to respond to user selections
        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedTitle = parentView.getItemAtPosition(position).toString();
                // Do something with the selectedTitle
                title_post = selectedTitle;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
        description_post= findViewById(R.id.editTextDescription);
        price_post = findViewById(R.id.editTextPrice);
        price_post.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        // Define a regular expression to match only numeric characters
                        String regex = "[-]?[0-9]+([.][0-9]*)?";

                        // Check if the input matches the regular expression
                        if (source.toString().matches(regex)) {
                            return null; // Accept the input
                        } else {
                            return ""; // Reject the input (no character/string)
                        }
                    }
                }
        });
        Button postButton = findViewById(R.id.buttonPost1);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ServicePostingActivity.this, "service.getTitle()", Toast.LENGTH_SHORT).show();
                // Get user input
                String title = title_post;
                String description = description_post.getText().toString();
                double price = Double.parseDouble(price_post.getText().toString());

                // Create a new service object
//                Service service = new Service(title, description, price);

                Service service = new Service();
                service.setTitle(title);
                service.setDescription(description);
                service.setPrice(price);
//                Toast.makeText(ServicePostingActivity.this, service.getTitle(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference servicesRef = database.getReference("Services");

                // Push (create) a new service entry in the database
                String serviceKey = servicesRef.push().getKey();
                servicesRef.child(serviceKey).setValue(service);

                // Save the service to the Firebase database
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
//                String serviceId = databaseReference.push().getKey(); // Generate a unique ID for the service
//                databaseReference.child(serviceId).setValue(service);
//                servicesReference.push().setValue(service);

                // Redirect to the service browsing activity or other actions
                startActivity(new Intent(ServicePostingActivity.this, ServiceBrowsingActivity.class));
                finish();
            }
        });
    }
}