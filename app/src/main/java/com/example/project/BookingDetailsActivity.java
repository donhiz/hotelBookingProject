package com.example.project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        String hotelName = getIntent().getStringExtra("hotelName");
        String checkInDate = getIntent().getStringExtra("checkInDate");
        String checkOutDate = getIntent().getStringExtra("checkOutDate");

        TextView nameTextView = findViewById(R.id.hotelNameTextView);
        TextView checkInDateTextView = findViewById(R.id.checkInDateTextView);
        TextView checkOutDateTextView = findViewById(R.id.checkOutDateTextView);

        nameTextView.setText("Hotel: " + hotelName);
        checkInDateTextView.setText("Check-in: " + checkInDate);
        checkOutDateTextView.setText("Check-out: " + checkOutDate);
    }
}