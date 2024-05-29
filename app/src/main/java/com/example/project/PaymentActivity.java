package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Button payButton;
    private EditText cardNumberEditText, cardExpiryEditText, cardCVVEditText, cardHolderNameEditText;
    private BookingDAO bookingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        // Initialize back button and set its click listener
        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Retrieve extras from the intent
        Bundle extras = getIntent().getExtras();
        String hotelName = null, hotelLocation = null, hotelPrice = null;
        int hotelImageResId = 0;

        if (extras != null) {
            hotelName = extras.getString("hotelName");
            hotelLocation = extras.getString("hotelLocation");
            hotelPrice = extras.getString("hotelPrice");
            hotelImageResId = extras.getInt("hotelImage");
            Log.d("PaymentActivity", "Hotel Image Resource ID: " + hotelImageResId);

            // Set data to views
            TextView nameTextView = findViewById(R.id.detailTitle);
            TextView locationTextView = findViewById(R.id.detailLocation);
            TextView priceTextView = findViewById(R.id.detailPrice);
            ImageView imageView = findViewById(R.id.detailImage);

            nameTextView.setText(hotelName);
            locationTextView.setText(hotelLocation);
            priceTextView.setText(hotelPrice);
            imageView.setImageResource(hotelImageResId);
        }

        // Initialize payment input fields
        cardNumberEditText = findViewById(R.id.editTextCardNumber);
        cardExpiryEditText = findViewById(R.id.editTextCardExpiry);
        cardCVVEditText = findViewById(R.id.editTextCardCVV);
        cardHolderNameEditText = findViewById(R.id.editTextCardHolderName);

        // Initialize BookingDAO
        bookingDAO = new BookingDAO(this);

        // Initialize and set onClick listeners for the Pay button
        payButton = findViewById(R.id.buttonPay);
        String finalHotelName = hotelName;
        String finalHotelLocation = hotelLocation;
        String finalHotelPrice = hotelPrice;
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    // Store booking details
                    storeBookingDetails(finalHotelName, finalHotelLocation, finalHotelPrice);

                    // Show confirmation dialog
                    showConfirmationDialog();
                }
            }
        });
    }

    private boolean validateInputs() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cardExpiry = cardExpiryEditText.getText().toString().trim();
        String cardCVV = cardCVVEditText.getText().toString().trim();
        String cardHolderName = cardHolderNameEditText.getText().toString().trim();

        if (cardNumber.isEmpty() || cardExpiry.isEmpty() || cardCVV.isEmpty() || cardHolderName.isEmpty()) {
            showValidationErrorDialog();
            return false;
        }
        return true;
    }

    private void showValidationErrorDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
        alertDialog.setTitle("Validation Error");
        alertDialog.setMessage("Please fill out all the fields before proceeding.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());

        alertDialog.show();
    }

    private void showConfirmationDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
        alertDialog.setTitle("Payment Successful");
        alertDialog.setMessage("Your payment has been submitted successfully. What would you like to do next?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next", (dialog, which) -> {
            Intent intent = new Intent(PaymentActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            Toast.makeText(PaymentActivity.this, "Action canceled", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        alertDialog.show();
    }

    private void storeBookingDetails(String hotelName, String hotelLocation, String hotelPrice) {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cardExpiry = cardExpiryEditText.getText().toString().trim();
        String cardCVV = cardCVVEditText.getText().toString().trim();
        String cardHolderName = cardHolderNameEditText.getText().toString().trim();

        // Insert booking details into the database
        long insertId = bookingDAO.insertBooking(hotelName, hotelLocation, hotelPrice,
                cardHolderName, "user@example.com", "1234567890", cardNumber, cardExpiry, cardCVV);

        if (insertId != -1) {
            // Show confirmation dialog if insertion was successful
            showConfirmationDialog();
        } else {
            // Handle insertion error
            Toast.makeText(this, "Failed to store booking details.", Toast.LENGTH_SHORT).show();
        }
    }
}

