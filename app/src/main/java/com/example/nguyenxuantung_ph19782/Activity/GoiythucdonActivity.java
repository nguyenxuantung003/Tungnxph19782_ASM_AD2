package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoiythucdonActivity extends AppCompatActivity {


    private TextView dietSuggestionText;
    private TextView bmiTextView;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiythucdon);

        dietSuggestionText = findViewById(R.id.diet_suggestion_text);
        bmiTextView = findViewById(R.id.bmi_text_view);

        userId = getIntent().getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        loadUserBMI();
    }

    private void loadUserBMI() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Float height = dataSnapshot.child("profile").child("height").getValue(Float.class);
                    Float weight = dataSnapshot.child("profile").child("weight").getValue(Float.class);

                    if (height != null && weight != null) {
                        float bmi = calculateBMI(weight, height);
                        displayBMI(bmi);
                        String suggestion = getDietSuggestion(bmi);
                        dietSuggestionText.setText(suggestion);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GoiythucdonActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private float calculateBMI(float weight, float height) {
        if (height == 0) return 0;
        return weight / ((height / 100) * (height / 100));
    }

    private void displayBMI(float bmi) {
        bmiTextView.setText("BMI: " + String.format("%.2f", bmi));
    }

    private String getDietSuggestion(float bmi) {
        if (bmi < 18.5) {
            return "You are underweight. It's important to eat a balanced diet with enough calories.";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "You have a normal weight. Maintain a balanced diet and regular exercise.";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "You are overweight. Consider a diet low in calories and high in nutrients.";
        } else {
            return "You are obese. Seek advice from a nutritionist to create a healthy diet plan.";
        }
    }
}