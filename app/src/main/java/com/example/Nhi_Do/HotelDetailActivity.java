package com.example.Nhi_Do;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class HotelDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private double latitude;
    private double longitude;
    private String hotelName;
    ImageButton ivShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        hotelName = intent.getStringExtra("hotelName");
        ivShowOnMap = findViewById(R.id.imageButtonMap);
        ivShowOnMap.setOnClickListener(v -> {
            // Create the URI for the location and start the map intent
            String uri = "geo:" + latitude + "," + longitude + "?z=17&q=" + latitude + "," + longitude + "(" + Uri.encode(hotelName) + ")";



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
        final TextView descriptionTextView = findViewById(R.id.detailDescription);
        final ImageView imageView = findViewById(R.id.detailImage);
        final TextView bedTextView = findViewById(R.id.bedTxt);
        final TextView bathTextView = findViewById(R.id.bathTxt);
        final TextView wifiTextView = findViewById(R.id.wifiTxt);
        int hotelImageResId = 0;

        if (extras != null) {
            String hotelLocation = extras.getString("hotelLocation");
            String hotelPrice = extras.getString("hotelPrice");
            hotelImageResId = extras.getInt("hotelImage");
            int numberOfBeds = extras.getInt("numberOfBeds");
            int numberOfBaths = extras.getInt("numberOfBaths");
            boolean hasWifi = extras.getBoolean("hasWifi");

            // Set data to views
            nameTextView.setText(hotelName);
            locationTextView.setText(hotelLocation);
            priceTextView.setText(hotelPrice);
            descriptionTextView.setText(extras.getString("hotelDescription"));

            imageView.setImageResource(hotelImageResId);
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
                // Create intent for PreviewActivity
                Intent intent = new Intent(HotelDetailActivity.this, DateActivity.class);
                // Pass hotel details as extras
                intent.putExtra("hotelName", nameTextView.getText().toString());
                intent.putExtra("hotelLocation", locationTextView.getText().toString());
                intent.putExtra("hotelPrice", priceTextView.getText().toString());
                intent.putExtra("hotelImage", finalHotelImageResId);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                // Start PreviewActivity
                startActivity(intent);
            }
        });
    }
}
