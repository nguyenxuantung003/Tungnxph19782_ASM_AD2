package com.example.nguyenxuantung_ph19782.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nguyenxuantung_ph19782.R;

public class ExerciseDetailActivity extends AppCompatActivity {

    @Override  @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        TextView nameTextView = findViewById(R.id.exercise_name);
        TextView descriptionTextView = findViewById(R.id.exercise_description);
        TextView benefitsTextView = findViewById(R.id.exercise_benefits);
        TextView instructionsTextView = findViewById(R.id.exercise_instructions);

        Intent intent = getIntent();
        String exerciseName = intent.getStringExtra("exercise_name");
        String exerciseDescription = intent.getStringExtra("exercise_description");
        String exerciseBenefits = intent.getStringExtra("exercise_benefits");
        String exerciseInstructions = intent.getStringExtra("exercise_instructions");

        nameTextView.setText(exerciseName);
        descriptionTextView.setText(exerciseDescription);
        benefitsTextView.setText(exerciseBenefits);
        instructionsTextView.setText(exerciseInstructions);
    }
}