package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HotelDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private ImageView ivShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        ivShowOnMap = findViewById(R.id.imageButtonMap);
        ivShowOnMap.setOnClickListener(v -> {
            // Create the URI for the location and start the map intent
            String uri = "geo:" + latitude + "," + longitude + "?z=17&q=" + latitude + "," + longitude + "(" + Uri.encode("Hotel Location") + ")";

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps"); // Ensure Google Maps is used
            startActivity(mapIntent);
        });


    // Initialize back button
        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        final TextView nameTextView = findViewById(R.id.detailTitle);
        final TextView locationTextView = findViewById(R.id.detailLocation);
        final TextView priceTextView = findViewById(R.id.detailPrice);
        final ImageView imageView = findViewById(R.id.detailImage);
        final TextView bedTextView = findViewById(R.id.bedTxt);
        final TextView bathTextView = findViewById(R.id.bathTxt);
        final TextView wifiTextView = findViewById(R.id.wifiTxt);
        int hotelImageResId = 0;

        if (extras != null) {
            String hotelName = extras.getString("hotelName");
            String hotelLocation = extras.getString("hotelLocation");
            String hotelPrice = extras.getString("hotelPrice");
            hotelImageResId = extras.getInt("hotelImage");
            String hotelDescription = extras.getString("hotelDescription");
            int numberOfBeds = extras.getInt("numberOfBeds");
            int numberOfBaths = extras.getInt("numberOfBaths");
            boolean hasWifi = extras.getBoolean("hasWifi");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            // Set data to views
            nameTextView.setText(hotelName);
            locationTextView.setText(hotelLocation);
            priceTextView.setText(hotelPrice);

            TextView descriptionTextView = findViewById(R.id.detailDescription);
            imageView.setImageResource(hotelImageResId);
            descriptionTextView.setText(hotelDescription);
            bedTextView.setText(String.valueOf(numberOfBeds));
            bathTextView.setText(String.valueOf(numberOfBaths));
            wifiTextView.setText(hasWifi ? "Yes" : "No");
        }

        // Declare final variable for hotelImageResId
        final int finalHotelImageResId = hotelImageResId;

        // Book now button functionality
        Button bookNowButton = findViewById(R.id.bookButton);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for PaymentActivity
                Intent intent = new Intent(HotelDetailActivity.this, PaymentActivity.class);

                // Pass hotel details as extras
                intent.putExtra("hotelName", nameTextView.getText().toString());
                intent.putExtra("hotelLocation", locationTextView.getText().toString());
                intent.putExtra("hotelPrice", priceTextView.getText().toString());
                intent.putExtra("hotelImage", finalHotelImageResId);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                // Start PaymentActivity
                startActivity(intent);
            }
        });

    }
}
