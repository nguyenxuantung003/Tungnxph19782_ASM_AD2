package com.example.nguyenxuantung_ph19782.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.R;

public class BMIActivity extends AppCompatActivity {

    private EditText etWeight;
    private EditText etHeight;
    private Button btnCalculateBMI;
    private Button btnSuggestMenu;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculateBMI = findViewById(R.id.btnCalculateBMI);
        btnSuggestMenu = findViewById(R.id.btnSuggestMenu);
        tvResult = findViewById(R.id.tvResult);

        btnCalculateBMI.setOnClickListener(v -> calculateBMI());

        btnSuggestMenu.setOnClickListener(v -> {
            // Open the suggestion activity
            Intent intent = new Intent(BMIActivity.this, MenuSuggestionActivity.class);
            // Pass BMI result to the new activity
            String bmiString = tvResult.getText().toString();
            // Extract the BMI value from the string
            double bmi = Double.parseDouble(bmiString.replace("BMI: ", "").trim());
            intent.putExtra("BMI", bmi);
            startActivity(intent);
        });
    }

    private void calculateBMI() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(this, "Please enter weight and height", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);
            if (height == 0) {
                Toast.makeText(this, "Height cannot be zero", Toast.LENGTH_SHORT).show();
                return;
            }
            float bmi = weight / (height * height);
            String bmiResult = String.format("BMI: %.2f", bmi);
            tvResult.setText(bmiResult);

            // Show the Suggest Menu button
            btnSuggestMenu.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}