package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Button payButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String hotelName = extras.getString("hotelName");
            String hotelLocation = extras.getString("hotelLocation");
            String hotelPrice = extras.getString("hotelPrice");
            int hotelImageResId = extras.getInt("hotelImage");
            Log.d("PaymentActivity", "Hotel Image Resource ID: " + hotelImageResId);

//            String hotelDescription = extras.getString("hotelDescription");

            // Set data to views
            TextView nameTextView = findViewById(R.id.detailTitle);
            TextView locationTextView = findViewById(R.id.detailLocation);
            TextView priceTextView = findViewById(R.id.detailPrice);
            ImageView imageView = findViewById(R.id.detailImage);
//            TextView descriptionTextView = findViewById(R.id.detailDescription);

            nameTextView.setText(hotelName);
            locationTextView.setText(hotelLocation);
            priceTextView.setText(hotelPrice);
            imageView.setImageResource(hotelImageResId);
//            descriptionTextView.setText(hotelDescription);
        }

        // Initialize and set onClick listeners for the Pay button
         payButton = findViewById(R.id.buttonPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
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
}
