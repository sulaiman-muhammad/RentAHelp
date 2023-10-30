package com.example.rentahelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FindServiceActivity extends AppCompatActivity {

    Spinner spinner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service);



         spinner = findViewById(R.id.spinner);



        String[] options = {"Jobs","Option 1", "Option 2", "Option 3", "Option 4"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item here
                String selectedOption = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected
            }
        });



    }
}



