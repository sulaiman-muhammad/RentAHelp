package com.example.rentahelp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentahelp.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private TextInputEditText firstNameEditText, lastNameEditText, phoneNumberEditText, emailEditText, dateOfBirthEditText;
    private Button buttonPickDate, btnSubmit;
    public List<String> addresses;
    private TextView textViewDateOfBirth;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        emailEditText = findViewById(R.id.email);
        dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        btnSubmit = findViewById(R.id.buttonSubmit);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    phoneNumberEditText.setText(user.getPhoneNumber());
                    emailEditText.setText(user.getEmail());
                    dateOfBirthEditText.setText(user.getDob());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database Error");
            }
        });

        buttonPickDate.setOnClickListener(view -> showDatePickerDialog());

        Button btnSubmit = findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(view -> {
            if (areAllFieldsFilled() && isValidPhoneNumber(phoneNumberEditText.getText().toString()) && isValidDateOfBirth(dateOfBirthEditText.getText().toString())) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String emailId = emailEditText.getText().toString();
                String dob = dateOfBirthEditText.getText().toString();

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
                String userId1 = firebaseAuth.getCurrentUser().getUid();
                databaseReference1.child(userId1).child("firstName").setValue(firstName);
                databaseReference1.child(userId1).child("lastName").setValue(lastName);
                databaseReference1.child(userId1).child("phoneNumber").setValue(phoneNumber);
                databaseReference1.child(userId1).child("emailId").setValue(emailId);
                databaseReference1.child(userId1).child("emailId").setValue(dob);

                Toast.makeText(EditProfileActivity.this, "Data stored in Firebase", Toast.LENGTH_SHORT).show();
                finish();
            } else if(!areAllFieldsFilled()) {
                Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!isValidPhoneNumber(phoneNumberEditText.getText().toString()) || phoneNumberEditText.getText().toString() == "0") {
                Toast.makeText(EditProfileActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            } else if (!isValidDateOfBirth(dateOfBirthEditText.getText().toString())) {
                Toast.makeText(EditProfileActivity.this, "Please enter a valid date of birth", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditProfileActivity.this, "Something else is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateOfBirthEditText.setText(formattedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean areAllFieldsFilled() {
        return !TextUtils.isEmpty(firstNameEditText.getText())
                && !TextUtils.isEmpty(lastNameEditText.getText())
                && !TextUtils.isEmpty(phoneNumberEditText.getText())
                && !TextUtils.isEmpty(emailEditText.getText())
                && !TextUtils.isEmpty(dateOfBirthEditText.getText());
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "CA");
            return phoneNumberUtil.isValidNumber(parsedPhoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    private boolean isValidDateOfBirth(String dob) {
        try {
            Calendar selectedDate = Calendar.getInstance();
            String[] dateParts = dob.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int year = Integer.parseInt(dateParts[2]);
            selectedDate.set(year, month, day);

            Calendar currentDate = Calendar.getInstance();
            return selectedDate.compareTo(currentDate) <= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
