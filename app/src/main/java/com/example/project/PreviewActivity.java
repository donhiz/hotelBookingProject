package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PreviewActivity extends AppCompatActivity {

    private EditText checkinDate, checkoutDate;
    private Spinner travellersSpinner;
    private String hotelName, hotelLocation, hotelPrice;
    private int hotelImageResId;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);

        // Retrieve extras from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hotelName = extras.getString("hotelName");
            hotelLocation = extras.getString("hotelLocation");
            hotelPrice = extras.getString("hotelPrice");
            hotelImageResId = extras.getInt("hotelImage");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
        }

        // Initialize views
        checkinDate = findViewById(R.id.checkinDate);
        checkoutDate = findViewById(R.id.checkoutDate);
        travellersSpinner = findViewById(R.id.travellersSpinner);
        Button bookButton = findViewById(R.id.bookButton);

        // Set up ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.travellers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travellersSpinner.setAdapter(adapter);

        // Set up button click listener
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle booking logic here
                Toast.makeText(PreviewActivity.this, "Book Now Clicked", Toast.LENGTH_SHORT).show();

                // Store check-in and check-out dates in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("HotelBooking", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("checkinDate", checkinDate.getText().toString());
                editor.putString("checkoutDate", checkoutDate.getText().toString());
                editor.putString("travellers", travellersSpinner.getSelectedItem().toString()); // Store number of travelers
                editor.apply();


                // Navigate to PaymentActivity with the same hotel details
                Intent intent = new Intent(PreviewActivity.this, PaymentActivity.class);
                intent.putExtra("hotelName", hotelName);
                intent.putExtra("hotelLocation", hotelLocation);
                intent.putExtra("hotelPrice", hotelPrice);
                intent.putExtra("hotelImage", hotelImageResId);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                // Optionally, pass additional data such as check-in/out dates or number of travellers
                intent.putExtra("checkinDate", checkinDate.getText().toString());
                intent.putExtra("checkoutDate", checkoutDate.getText().toString());
                intent.putExtra("travellers", travellersSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        // Set up date pickers
        checkinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkinDate);
            }
        });

        checkoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkoutDate);
            }
        });

        // Set up spinner item selected listener
        travellersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(PreviewActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });
    }

    // Method to show date picker dialog
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText, calendar);
            }
        };

        // Create DatePickerDialog with minimum date set
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PreviewActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today's date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Method to update the EditText with selected date
    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }
}
