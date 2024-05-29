package com.example.project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private Button selectDateButton;
    private Button bookNowButton, filterButton;
    private ImageButton backButton;
    private Calendar calendar;
    private Context context;
    private RequestQueue requestQueue;
    private Spinner spinner;
    private BookingDatabaseHelper dbHelper;
    private boolean isWonderfulSelected, isVeryGoodSelected, isGoodSelected;
    private int maxPrice;
    private boolean is1BedSelected, is2BedsSelected;
    private static final int FILTER_REQUEST_CODE = 1;
    private TextView filterResultTextView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new BookingDatabaseHelper(this);


        // Initialize RecyclerView, hotel list, searchView, and selectDateButton
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        checkinDate = findViewById(R.id.checkinDate);
        checkoutdate = findViewById(R.id.checkoutDate);

        bookNowButton = findViewById(R.id.bookButton);
        calendar = Calendar.getInstance();
        backButton = findViewById(R.id.imageButtonBack);
        filterButton = findViewById(R.id.filterButton);
        hotelList = new ArrayList<>();
        filteredHotelList = new ArrayList<>();

        hotelAdapter = new HotelAdapter(filteredHotelList, this, new RecycleViewInterface() {
            @Override
            public void onItemClick(int position) {
                Hotel hotel = hotelList.get(position);
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

            }

        });
        recyclerView.setAdapter(hotelAdapter);

        addSampleHotels();
        filteredHotelList.addAll(hotelList);
        hotelAdapter.notifyDataSetChanged();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FilterActivity to apply filters
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            }
        });

        hotelList = new ArrayList<>();

        hotelAdapter = new HotelAdapter(hotelList, this, this);

        // Set RecyclerView layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hotelAdapter);
//        startActivityForResult(new Intent(this, FilterActivity.class), 1);


        // Set up search functionality
        setupSearchView();

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
        // Set up spinner
        spinner = findViewById(R.id.travellersSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Set up ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.travellers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //set up turn back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //set up filter button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            }
        });


        checkinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(
                        MainActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                // Set the minimum date to today's date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        checkoutdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(
                        MainActivity.this, date2,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                // Set the minimum date to today's date
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog2.show();
            }
        });


    }


    // Set up filter function

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve filter options from FilterActivity
            boolean isWonderfulSelected = data.getBooleanExtra("isWonderfulSelected", false);
            boolean isVeryGoodSelected = data.getBooleanExtra("isVeryGoodSelected", false);
            boolean isGoodSelected = data.getBooleanExtra("isGoodSelected", false);
            boolean isAnySelected = data.getBooleanExtra("isAnySelected", false);
            boolean is1BedSelected = data.getBooleanExtra("is1BedSelected", false);
            boolean is2BedsSelected = data.getBooleanExtra("is2BedsSelected", false);
            boolean is1BathSelected = data.getBooleanExtra("is1BathSelected", false);
            boolean is2BathsSelected = data.getBooleanExtra("is2BathsSelected", false);
            boolean isWifiAvailableSelected = data.getBooleanExtra("isWifiAvailableSelected", false);
            boolean isWifiNotAvailableSelected = data.getBooleanExtra("isWifiNotAvailableSelected", false);
            int maxPrice = data.getIntExtra("maxPrice", 0);

            // Apply filters to the hotelList
            filterHotelList(isWonderfulSelected, isVeryGoodSelected, isGoodSelected, isAnySelected, is1BedSelected, is2BedsSelected, is1BathSelected, is2BathsSelected, isWifiAvailableSelected, isWifiNotAvailableSelected, maxPrice);
        }
    }

    private void filterHotelList(boolean isWonderfulSelected, boolean isVeryGoodSelected, boolean isGoodSelected, boolean isAnySelected,
                                 boolean is1BedSelected, boolean is2BedsSelected, boolean is1BathSelected, boolean is2BathsSelected,
                                 boolean isWifiAvailableSelected, boolean isWifiNotAvailableSelected, int maxPrice) {
        filteredHotelList.clear(); // Clear the filtered list before applying filters

        for (Hotel hotel : hotelList) {
            boolean matchesFilter = true;

            // Apply guest ratings filter
            if ((isWonderfulSelected && !hotel.getRating().equalsIgnoreCase("Wonderful")) ||
                    (isVeryGoodSelected && !hotel.getRating().equalsIgnoreCase("Very Good")) ||
                    (isGoodSelected && !hotel.getRating().equalsIgnoreCase("Good")) ||
                    (isAnySelected && !hotel.getRating().equalsIgnoreCase("Any"))) {
                matchesFilter = false;
            }

            // Apply beds and baths filter
            if ((is1BedSelected && hotel.getBeds() != 1) ||
                    (is2BedsSelected && hotel.getBeds() != 2) ||
                    (is1BathSelected && hotel.getBaths() != 1) ||
                    (is2BathsSelected && hotel.getBaths() != 2)) {
                matchesFilter = false;
            }

            // Apply Wi-Fi availability filter
            if ((isWifiAvailableSelected && !hotel.isWifi()) ||
                    (isWifiNotAvailableSelected && hotel.isWifi())) {
                matchesFilter = false;
            }

            // Apply price filter
            int hotelPrice = Integer.parseInt(hotel.getPrice().replaceAll("[^0-9]", ""));
            if (maxPrice > 0 && hotelPrice > maxPrice) {
                matchesFilter = false;
            }

            if (matchesFilter) {
                filteredHotelList.add(hotel);
            }
        }

        hotelAdapter.setHotelList(filteredHotelList); // Update the adapter's data
        hotelAdapter.notifyDataSetChanged(); // Notify adapter of data change
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
        List<Hotel> dataSearchList = new ArrayList<>();
        for (Hotel data : hotelList) {
            if (data.getName().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            hotelAdapter.setSearchList(dataSearchList);
        }
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

    EditText checkinDate,checkoutdate;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel(myCalendar, checkinDate);
        }
    };
    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar,checkoutdate);

        }
    };

    private void updateLabel(Calendar myCalendar,EditText editText) {
        String myFormat ="MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
            editText.setText(sdf.format(myCalendar.getTime()));
    }



// if you want to add details in details activity create class and add here

    @Override
    public void onItemClick(int position) {
        Hotel hotel = hotelList.get(position);
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