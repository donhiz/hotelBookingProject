package com.example.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecycleViewInterface {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private List<Hotel> filteredHotelList;
    private SearchView searchView;

    private Button bookNowButton;
    private ImageButton backButton;
    private BookingDatabaseHelper dbHelper;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new BookingDatabaseHelper(this);


        // Initialize RecyclerView, hotel list, searchView, and selectDateButton
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);

        bookNowButton = findViewById(R.id.bookButton);
        backButton = findViewById(R.id.imageButtonBack);

        hotelList = new ArrayList<>();
        filteredHotelList = new ArrayList<>();

        hotelAdapter = new HotelAdapter(filteredHotelList, this, this);
        recyclerView.setAdapter(hotelAdapter);
        setupSearchView();

        addSampleHotels();
        filteredHotelList.addAll(hotelList);
        hotelAdapter.notifyDataSetChanged();


        hotelList = new ArrayList<>();

        hotelAdapter = new HotelAdapter(hotelList, this, this);

        // Set RecyclerView layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hotelAdapter);
//        startActivityForResult(new Intent(this, FilterActivity.class), 1);


        // Set up search functionality
        setupSearchView();
        searchView.clearFocus();

        // Add sample hotel data (replace with your actual data)
        addSampleHotels();

        //set up book now button
        if (bookNowButton != null) {
            bookNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // Handle the case where the button is not found (null)
            Toast.makeText(this, "Button not found in the layout", Toast.LENGTH_SHORT).show();
        }


        //set up turn back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void addSampleHotels() {
        hotelList.add(new Hotel(R.drawable.hotel4, "Luxury Paradise Hotel", "East Ram Nagar, Ram Nagar, Shahdara, New Delhi, Delhi, 110032, India", 28.6759959, 77.2950883, "$102/per night", "Indulge in luxury at our 5-star hotel located in the heart of the city. Enjoy breathtaking views, world-class amenities, and exceptional service.", "Wonderful", 2, 1, true));
        hotelList.add(new Hotel(R.drawable.hotel2, "Seaside Resort & Spa", "456 Beach Avenue, Oceanfront, USA", 36.7783, -119.4179, "$120/per night", "Escape to our seaside resort for a tranquil getaway. Relax on pristine beaches, rejuvenate at our spa, and savor delicious coastal cuisine.", "Very Good", 1, 1, true));
        hotelList.add(new Hotel(R.drawable.hotel3, "Mountain Lodge Retreat", "789 Pine Trail, Mountain Town, USA", 39.7392, -104.9903, "$150/per night", "Experience the beauty of nature at our mountain lodge. Enjoy hiking trails, cozy cabins, and stunning views of the surrounding wilderness.", "Good", 2, 1, true));
        hotelList.add(new Hotel(R.drawable.hotel1, "Urban Oasis Boutique Hotel", "123 Main Street, Downtown, Cityville, USA", 40.7128, -74.0060, "$135/per night", "Discover serenity in the heart of the city at our boutique hotel. Experience stylish accommodations, gourmet dining, and personalized service.", "Good", 1, 2, true));
        hotelList.add(new Hotel(R.drawable.hotel5, "Riverside Retreat Resort", "101 River Road, Riverside, Countryside, USA", 34.0522, -118.2437, "$180/per night", "Escape to our riverside resort for a peaceful retreat. Enjoy water activities, luxurious cabins, and picturesque views of the river.", "Good", 2, 2, true));
        hotelList.add(new Hotel(R.drawable.hotel6, "Tropical Paradise Resort & Spa", "789 Palm Avenue, Beachside, Island Paradise, USA", 25.0343, -77.3963, "$250/per night", "Experience paradise at our tropical resort. Relax on white sandy beaches, indulge in spa treatments, and dine under the stars.", "Very Good", 1, 2, true));

        hotelAdapter.notifyDataSetChanged();
    }


    private void setupSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    private void searchList(String text) {
        filteredHotelList.clear();  // Clear the filtered list before adding new search results
        for (Hotel data : hotelList) {
            if (data.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredHotelList.add(data);
            }
        }
        if (filteredHotelList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
        hotelAdapter.setSearchList(filteredHotelList);  // Update the adapter with the filtered list
    }



    @Override
    public void onItemClick(int position) {
        Hotel hotel = filteredHotelList.get(position); // Use filteredHotelList instead of hotelList
        Intent intent = new Intent(MainActivity.this, HotelDetailActivity.class);
        intent.putExtra("hotelName", hotel.getName());
        intent.putExtra("hotelLocation", hotel.getLocation());
        intent.putExtra("hotelPrice", hotel.getPrice());
        intent.putExtra("hotelImage", hotel.getImageResId());
        intent.putExtra("hotelDescription", hotel.getDescription());
        intent.putExtra("hotelRating", hotel.getRating());
        intent.putExtra("numberOfBeds", hotel.getBeds());
        intent.putExtra("numberOfBaths", hotel.getBaths());
        intent.putExtra("hasWifi", hotel.isWifi());
        intent.putExtra("latitude", hotel.getLatitude());
        intent.putExtra("longitude", hotel.getLongitude());

        startActivity(intent);
        Toast.makeText(this, "Clicked: " + hotel.getName(), Toast.LENGTH_SHORT).show();
    }
}