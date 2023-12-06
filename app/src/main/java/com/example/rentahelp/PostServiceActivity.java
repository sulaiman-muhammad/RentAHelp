package com.example.rentahelp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentahelp.model.Notification;
import com.example.rentahelp.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class PostServiceActivity extends AppCompatActivity {

    private static final String TAG = PostServiceActivity.class.getName();
    private String selectedTitle;
    private Calendar selectedDate;
    private Calendar selectedStartTime;
    private Calendar selectedEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_posting);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Spinner titleSpinner = findViewById(R.id.titleSpinner);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText priceEditText = findViewById(R.id.priceEditText);
        TextView availabilityTextView = findViewById(R.id.availabilityTextView);
        TextView startTimeTextView = findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = findViewById(R.id.endTimeTextView);
        Spinner addressSpinner = findViewById(R.id.addressSpinner);
        Button postButton = findViewById(R.id.postServiceButton);

        ArrayAdapter<CharSequence> titleAdapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_item,
                R.id.customSpinnerItemText,
                getResources().getTextArray(R.array.title_options));
        titleSpinner.setAdapter(titleAdapter);
        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTitle = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        ArrayAdapter<CharSequence> addressAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.title_options,
                android.R.layout.simple_spinner_item
        );
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(addressAdapter);
        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedAddress = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        availabilityTextView.setOnClickListener(view -> {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, year1, month1, day1) -> {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, month1, day1);
                        availabilityTextView.setText(getFormattedDate(selectedDate));
                    },
                    year, month, day
            );
            datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            datePickerDialog.show();
        });

        startTimeTextView.setOnClickListener(view -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timePicker, hour1, minute1) -> {
                        selectedStartTime = Calendar.getInstance();
                        selectedStartTime.set(Calendar.HOUR_OF_DAY, hour1);
                        selectedStartTime.set(Calendar.MINUTE, minute1);
                        startTimeTextView.setText(getFormattedTime(selectedStartTime));
                    },
                    hour, minute, false
            );
            timePickerDialog.show();
        });

        endTimeTextView.setOnClickListener(view -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timePicker, hour1, minute1) -> {
                        selectedEndTime = Calendar.getInstance();
                        selectedEndTime.set(Calendar.HOUR_OF_DAY, hour1);
                        selectedEndTime.set(Calendar.MINUTE, minute1);
                        endTimeTextView.setText(getFormattedTime(selectedEndTime));
                    },
                    hour, minute, false
            );
            timePickerDialog.show();
        });

        postButton.setOnClickListener(view -> {
            if (currentUser != null && currentUser.isEmailVerified()) {
                String description = descriptionEditText.getText().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());

                if (!isValidInput(selectedTitle, description, price, selectedDate, selectedStartTime, selectedEndTime)) {
                    return;
                }

                DatabaseReference servicesReference = FirebaseDatabase.getInstance().getReference("Services");
                String serviceKey = servicesReference.push().getKey();
                Service service = new Service(serviceKey, selectedTitle, description, price, getFormattedDate(selectedDate), getFormattedTime(selectedStartTime), getFormattedTime(selectedEndTime), null, 0.0, null, currentUser != null ? currentUser.getUid() : null, null, null);
                if (serviceKey != null) {
                    servicesReference.child(serviceKey).setValue(service);
                }

                DatabaseReference notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");
                String notificationKey = notificationsReference.push().getKey();
                Notification notification = new Notification(currentUser.getUid(), "Service request for " + selectedTitle + " added.");
                if (notificationKey != null) {
                    notificationsReference.child(notificationKey).setValue(notification);
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Kindly verify your email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFormattedDate(Calendar calendar) {
        return String.format(Locale.getDefault(), "%02d-%02d-%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    private String getFormattedTime(Calendar calendar) {
        return String.format(Locale.getDefault(), "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private boolean isValidInput(String selectedTitle, String description, double price, Calendar selectedDate, Calendar selectedStartTime, Calendar selectedEndTime) {
        if (TextUtils.isEmpty(selectedTitle)) {
            Toast.makeText(this, "Invalid title.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Invalid description.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (price == 0.0) {
            Toast.makeText(this, "Invalid price.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Invalid availability.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedStartTime == null) {
            Toast.makeText(this, "Invalid start time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedEndTime == null) {
            Toast.makeText(this, "Invalid end time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedEndTime.before(selectedStartTime)) {
            Toast.makeText(this, "End time cannot be before start time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}