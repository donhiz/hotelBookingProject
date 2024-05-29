package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {
    private CheckBox checkBoxWonderful, checkBoxVeryGood, checkBoxGood, checkBoxAny;
    private CheckBox checkBox1Bed, checkBox2Beds;
    private CheckBox checkBox1Bath, checkBox2Baths;
    private CheckBox checkBoxWifiAvailable, checkBoxWifiNotAvailable;
    private SeekBar seekBarPrice;
    private Button btnApplyFilter;
    private TextView textViewPriceValue;
    ImageButton backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        // Initialize views
        textViewPriceValue = findViewById(R.id.textViewPriceValue);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        checkBoxWonderful = findViewById(R.id.checkBoxWonderful);
        checkBoxVeryGood = findViewById(R.id.checkBoxVeryGood);
        checkBoxGood = findViewById(R.id.checkBoxGood);
        checkBoxAny = findViewById(R.id.checkBoxAny);
        checkBox1Bed = findViewById(R.id.checkBox1Bed);
        checkBox2Beds = findViewById(R.id.checkBox2Beds);
        checkBox1Bath = findViewById(R.id.checkBox1Bath);
        checkBox2Baths = findViewById(R.id.checkBox2Baths);
        checkBoxWifiAvailable = findViewById(R.id.checkBoxWifiAvailable);
        checkBoxWifiNotAvailable = findViewById(R.id.checkBoxWifiNotAvailable);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);

        // Set listener for SeekBar
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewPriceValue.setText("Price: $" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        // Initialize back button
        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set click listener for the Apply Filter button
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get filter options from checkboxes, seekbar, etc.
                boolean isWonderfulSelected = checkBoxWonderful.isChecked();
                boolean isVeryGoodSelected = checkBoxVeryGood.isChecked();
                boolean isGoodSelected = checkBoxGood.isChecked();
                boolean isAnySelected = checkBoxAny.isChecked();
                boolean is1BedSelected = checkBox1Bed.isChecked();
                boolean is2BedsSelected = checkBox2Beds.isChecked();
                boolean is1BathSelected = checkBox1Bath.isChecked();
                boolean is2BathsSelected = checkBox2Baths.isChecked();
                boolean isWifiAvailableSelected = checkBoxWifiAvailable.isChecked();
                boolean isWifiNotAvailableSelected = checkBoxWifiNotAvailable.isChecked();
                int maxPrice = seekBarPrice.getProgress();

                // Pass the filter options back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("isWonderfulSelected", isWonderfulSelected);
                intent.putExtra("isVeryGoodSelected", isVeryGoodSelected);
                intent.putExtra("isGoodSelected", isGoodSelected);
                intent.putExtra("isAnySelected", isAnySelected);
                intent.putExtra("is1BedSelected", is1BedSelected);
                intent.putExtra("is2BedsSelected", is2BedsSelected);
                intent.putExtra("is1BathSelected", is1BathSelected);
                intent.putExtra("is2BathsSelected", is2BathsSelected);
                intent.putExtra("isWifiAvailableSelected", isWifiAvailableSelected);
                intent.putExtra("isWifiNotAvailableSelected", isWifiNotAvailableSelected);
                intent.putExtra("maxPrice", maxPrice);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
