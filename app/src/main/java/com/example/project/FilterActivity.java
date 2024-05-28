package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {
    private ImageButton backButton;
    private CheckBox checkBoxWonderful,checkBoxVeryGood,checkBoxGood;
    private SeekBar seekBarPrice;
    private CheckBox checkBoxHotel, checkBoxMotel;
    private CheckBox checkBox1Bed, checkBox2Beds;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        backButton = findViewById(R.id.imageButtonBack);
        checkBoxWonderful = findViewById(R.id.checkBoxWonderful);
        checkBoxVeryGood = findViewById(R.id.checkBoxVeryGood);
        checkBoxGood = findViewById(R.id.checkBoxGood);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        checkBoxHotel = findViewById(R.id.checkBoxHotel);
        checkBoxMotel = findViewById(R.id.checkBoxMotel);
        checkBox1Bed = findViewById(R.id.checkBox1Bed);
        checkBox2Beds = findViewById(R.id.checkBox2Beds);
        btnDone = findViewById(R.id.btnApplyFilter);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                           // Apply filter logic here
                boolean isWonderfulSelected = checkBoxWonderful.isChecked();
                boolean isVeryGoodSelected = checkBoxVeryGood.isChecked();
                boolean isGoodSelected = checkBoxGood.isChecked();
                int maxPrice = seekBarPrice.getProgress();
                boolean isHotelSelected = checkBoxHotel.isChecked();
                boolean isMotelSelected = checkBoxMotel.isChecked();
                boolean is1BedSelected = checkBox1Bed.isChecked();
                boolean is2BedsSelected = checkBox2Beds.isChecked();


                //pass the filter options back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("isWonderfulSelected", isWonderfulSelected);
                intent.putExtra("isVeryGoodSelected", isVeryGoodSelected);
                intent.putExtra("isGoodSelected", isGoodSelected);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("isHotelSelected", isHotelSelected);
                intent.putExtra("isMotelSelected", isMotelSelected);
                intent.putExtra("is1BedSelected", is1BedSelected);
                intent.putExtra("is2BedsSelected", is2BedsSelected);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


                backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
