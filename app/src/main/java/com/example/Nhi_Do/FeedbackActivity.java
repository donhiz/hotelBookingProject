package com.example.Nhi_Do;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class FeedbackActivity extends AppCompatActivity {
    private Button submitButton;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback); // Make sure the layout file name matches
        submitButton = findViewById(R.id.buttonSubmitFeedback);
        backButton = findViewById(R.id.imageButtonBack);
        submitButton.setOnClickListener(v -> submitFeedback());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void submitFeedback() {
        Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
    }

}
