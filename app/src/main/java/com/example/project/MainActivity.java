package com.example.project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecycleViewInterface {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private SearchView searchView;
    private Button selectDateButton;
    private Button bookNowButton, filterButton;
    private ImageButton backButton;
    private Calendar calendar;
    private Context context;
    private RequestQueue requestQueue;
    private Spinner spinner ;
    private BookingDatabaseHelper dbHelper;
    private Button selectCheckInDateButton, selectCheckOutDateButton;
    private boolean isWonderfulSelected, isVeryGoodSelected, isGoodSelected;
    private int maxPrice;
    private boolean isHotelSelected, isMotelSelected;
    private boolean is1BedSelected, is2BedsSelected;
    private static final int FILTER_REQUEST_CODE = 1;

//    ArrayList<String> hotelName = new ArrayList<>();
//    ArrayList<String> hotelLocation = new ArrayList<>();
//    ArrayList<String> hotelPrice = new ArrayList<>();
//    ArrayList<String> hotelDescription = new ArrayList<>();
//    ArrayList<String> hotelImage = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new BookingDatabaseHelper(this);


        // Initialize RecyclerView, hotel list, searchView, and selectDateButton
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        selectCheckInDateButton = findViewById(R.id.selectCheckInDateButton);
        selectCheckOutDateButton = findViewById(R.id.selectCheckOutDateButton);

        bookNowButton = findViewById(R.id.bookButton);
        calendar = Calendar.getInstance();
        backButton = findViewById(R.id.imageButtonBack);
        filterButton = findViewById(R.id.filterButton);
        hotelList = new ArrayList<>();

        hotelAdapter = new HotelAdapter(hotelList, this, this);

        // Set RecyclerView layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hotelAdapter);
//        startActivityForResult(new Intent(this, FilterActivity.class), 1);






//        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();
//        fetchHotelData();

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


        selectCheckInDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });


        selectCheckOutDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //retrieving data and save dates if available
        Cursor cursor = dbHelper.getAllBookings();
        if (cursor != null && cursor.moveToFirst()) {
            String checkInDate = cursor.getString(cursor.getColumnIndexOrThrow(BookingDatabaseHelper.COLUMN_CHECK_IN));
            String checkOutDate = cursor.getString(cursor.getColumnIndexOrThrow(BookingDatabaseHelper.COLUMN_CHECK_OUT));
            selectCheckInDateButton.setText(checkInDate);
            selectCheckOutDateButton.setText(checkOutDate);
            cursor.close();

        }

    }

//        //getting Json
//        try {
//            JSONObject object = new JSONObject(loadJSONFromAsset());
//
//            //fetch JsonArray name hotel
//            JSONArray array = object.getJSONArray("hotels");
//            // implementation of loop for getting hotel list data
//            for (int i = 0; i < array.length(); i++) {
//                //add hotel name to arraylist
//                JSONObject hotelDetails = array.getJSONObject(i);
//                hotelName.add(hotelDetails.getString("name"));
//                hotelLocation.add(hotelDetails.getString("location"));
//                hotelPrice.add(hotelDetails.getString("price"));
//                hotelDescription.add(hotelDetails.getString("description"));
//                hotelImage.add(hotelDetails.getString("image"));
//
//            }
//
//    }catch (JSONException e){
//            e.printStackTrace();
//        }
//        detailAdapter detailAdapter = new detailAdapter(hotelName,hotelLocation,hotelPrice,hotelDescription,hotelImage,MainActivity.this);
//        recyclerView.setAdapter(detailAdapter);
//
//
//
//
//
//    }
//    //method to load json
//    private String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("hotel_data.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
//            // Retrieve filter criteria from the Intent
//            boolean isWonderfulSelected = data.getBooleanExtra("isWonderfulSelected", false);
//            boolean isVeryGoodSelected = data.getBooleanExtra("isVeryGoodSelected", false);
//            boolean isGoodSelected = data.getBooleanExtra("isGoodSelected", false);
//            int maxPrice = data.getIntExtra("maxPrice", 0);
//            boolean isHotelSelected = data.getBooleanExtra("isHotelSelected", false);
//            boolean isMotelSelected = data.getBooleanExtra("isMotelSelected", false);
//            boolean is1BedSelected = data.getBooleanExtra("is1BedSelected", false);
//            boolean is2BedsSelected = data.getBooleanExtra("is2BedsSelected", false);
//
//
//
//            // Apply filter criteria to the hotel list
//            List<Hotel> filteredHotelList = applyFilterCriteria(isWonderfulSelected, isVeryGoodSelected, isGoodSelected,
//                    maxPrice, isHotelSelected, isMotelSelected, is1BedSelected, is2BedsSelected);
//
//            // Update RecyclerView with filtered hotel list
//            hotelAdapter.setSearchList(filteredHotelList);
//        }
//    }
//
//    // Method to apply filter criteria to the hotel list
//    private List<Hotel> applyFilterCriteria(boolean isWonderfulSelected, boolean isVeryGoodSelected, boolean isGoodSelected,
//                                            int maxPrice, boolean isHotelSelected, boolean isMotelSelected,
//                                            boolean is1BedSelected, boolean is2BedsSelected) {
//        List<Hotel> filteredList = new ArrayList<>();
//
//        // Apply filter logic here based on the criteria passed
//
//        // Example: Loop through the original hotel list and add hotels that meet the filter criteria to the filtered list
//
//
//        for (Hotel hotel : hotelList) {
//            int hotelPrice = Integer.parseInt(hotel.getPrice().replace("$", "").replaceAll("/per night", "").trim());
//            // Check if the hotel meets the filter criteria and add it to the filtered list if it does
//            // Example: Check if the hotel's price is within the maxPrice range and other criteria like type, location, etc.
//            if (hotelPrice <= maxPrice && ((isHotelSelected && hotel.getType().equals("Hotel")) || (isMotelSelected && hotel.getType().equals("Motel")))) {
//                // Add the hotel to the filtered list
//                filteredList.add(hotel);
//            }
//        }
//
//
//        return filteredList;
//    }



    private void fetchHotelData() {

        String url = "https://jsonformatter.org/jsonview";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int image = jsonObject.getInt("hotelImage");
                                String name = jsonObject.getString("hotelName");
                                String location = jsonObject.getString("hotelLocation");
                                String price = jsonObject.getString("hotelPrice");
                                String description = jsonObject.getString("hotelDescription");
                                String rating = jsonObject.getString("hotelRating");
                                int numBeds = jsonObject.getInt("hotelNumBeds");
                                int numbaths = jsonObject.getInt("hotelNumBaths");
                                Boolean wifi = jsonObject.getBoolean("hotelWifi");

                                Hotel hotel = new Hotel(image,name,location ,price,description,rating,numBeds,numbaths,wifi);
                                hotelList.add(hotel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

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

    private void addSampleHotels() {
        hotelList.add(new Hotel(R.drawable.hotel4, "Luxury Paradise Hotel", "123 Main Street, Cityville, USA", "$100/per night", "Indulge in luxury at our 5-star hotel located in the heart of the city. Enjoy breathtaking views, world-class amenities, and exceptional service.", "Wonderful", 2, 1, true));
        hotelList.add(new Hotel(R.drawable.hotel2, "Seaside Resort & Spa", "456 Beach Avenue, Oceanfront, USA", "$120/per night", "Escape to our seaside resort for a tranquil getaway. Relax on pristine beaches, rejuvenate at our spa, and savor delicious coastal cuisine.", "Very Good", 1, 1, true));
        hotelList.add(new Hotel(R.drawable.hotel3, "Mountain Lodge Retreat", "789 Pine Trail, Mountain Town, USA", "$150/per night", "Experience the beauty of nature at our mountain lodge. Enjoy hiking trails, cozy cabins, and stunning views of the surrounding wilderness.", "Good", 2, 1, true));

        hotelAdapter.notifyDataSetChanged();
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

    private String selectedDate;
    private String checkInTime;
    private String checkOutTime;

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        showTimePickerDialog(true); // Show check-in time picker after selecting the date
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(final boolean isCheckInTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        if (isCheckInTime) {
                            checkInTime = selectedTime;
                            showTimePickerDialog(false); // Show check-out time picker after selecting check-in time
                        } else {
                            checkOutTime = selectedTime;
                            // Now you have the selected date, check-in time, and check-out time
                            // Perform your availability check or other actions here
                            // For example, you can call a method like checkAvailability(selectedDate, checkInTime, checkOutTime);
                        }
                    }
                },
                0, 0, true);

        timePickerDialog.show();
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
        startActivity(intent);
        Toast.makeText(this, "Clicked: " + hotel.getName(), Toast.LENGTH_SHORT).show();
    }


}