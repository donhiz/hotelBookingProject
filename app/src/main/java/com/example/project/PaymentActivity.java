package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.app.NotificationCompat;

public class PaymentActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Button payButton;
    private EditText cardNumberEditText, cardExpiryEditText, cardCVVEditText, cardHolderNameEditText;
    private BookingDAO bookingDAO;

    private NotificationManager notificationManager;

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

        // Initialize payment input fields
        cardNumberEditText = findViewById(R.id.editTextCardNumber);
        cardExpiryEditText = findViewById(R.id.editTextCardExpiry);
        cardCVVEditText = findViewById(R.id.editTextCardCVV);
        cardHolderNameEditText = findViewById(R.id.editTextCardHolderName);

        // Initialize BookingDAO
        bookingDAO = new BookingDAO(this);

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

        // Initialize NotificationManager
        notificationManager = getSystemService(NotificationManager.class);

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

                    // Show notification
                    showNotification();
                }
            }
        });
    }

    private boolean validateInputs() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cardExpiry = cardExpiryEditText.getText().toString().trim();
        String cardCVV = cardCVVEditText.getText().toString().trim();
        String cardHolderName = cardHolderNameEditText.getText().toString().trim();

        if (cardNumber.isEmpty() || !isValidCardNumber(cardNumber)) {
            cardNumberEditText.setError("Enter a valid card number");
            return false;
        }

        if (cardExpiry.isEmpty() || !isValidCardExpiry(cardExpiry)) {
            cardExpiryEditText.setError("Enter a valid expiry date (MM/YY)");
            return false;
        }

        if (cardCVV.isEmpty() || !isValidCVV(cardCVV)) {
            cardCVVEditText.setError("Enter a valid CVV (3 or 4 digits)");
            return false;
        }

        if (cardHolderName.isEmpty()) {
            cardHolderNameEditText.setError("Enter cardholder name");
            return false;
        }

        return true;
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Add validation logic for card number format (e.g., length and characters)
        return cardNumber.length() == 16; // Example validation for a 16-digit card number
    }

    private boolean isValidCardExpiry(String cardExpiry) {
        // Add validation logic for card expiry format (e.g., MM/YY)
        return cardExpiry.matches("\\d{2}/\\d{2}"); // Example validation for MM/YY format
    }

    private boolean isValidCVV(String cardCVV) {
        // Add validation logic for CVV format (e.g., 3 or 4 digits)
        return cardCVV.length() == 3 || cardCVV.length() == 4; // Example validation for 3 or 4 digits
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

    private void showNotification() {
        // Retrieve check-in and check-out dates from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("HotelBooking", MODE_PRIVATE);
        String checkinDate = sharedPreferences.getString("checkinDate", "");
        String checkoutDate = sharedPreferences.getString("checkoutDate", "");
        String travellers = sharedPreferences.getString("travellers", "");

        // Create an intent to open an activity when the notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.start_icon)
                .setContentTitle("Hotel Booking Successfully")
                .setContentText("Check-in Date: " + checkinDate + ", Check-out Date: " + checkoutDate + ", " +travellers)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Create a notification channel (required for Android Oreo and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Show the notification
        notificationManager.notify(1, builder.build());
    }
}
