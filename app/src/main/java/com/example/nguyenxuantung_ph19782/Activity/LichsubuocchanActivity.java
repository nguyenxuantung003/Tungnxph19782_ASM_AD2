package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.CircularProgressDrawable;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Buocchan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LichsubuocchanActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tv1, tv2, tv3, tv4, tv5;
    private CircularProgressDrawable progressDrawable;

    private String userId;
    private String selectedDate;
    private double userWeight;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsubuocchan);
        imageView = findViewById(R.id.progressImageViewLS);
        tv1 = findViewById(R.id.steps_text_viewLS);
        tv2 = findViewById(R.id.goal_text_viewLS);
        tv3 = findViewById(R.id.status_text_viewLS);
        tv4 = findViewById(R.id.calories_text_viewLS);
        tv5 = findViewById(R.id.distance_text_viewLS);

        progressDrawable = new CircularProgressDrawable(100); // Giá trị tối đa là 100
        imageView.setImageDrawable(progressDrawable);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        selectedDate = getIntent().getStringExtra("selectedDate");
        loadUserWeight();
    }

    private void loadUserWeight() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("profile");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("weight")) {
                    userWeight = dataSnapshot.child("weight").getValue(Double.class);
                    loadStepDataForSelectedDate();
                } else {
                    Toast.makeText(LichsubuocchanActivity.this, "User weight data not found", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadUserWeight", "Error fetching user weight data", error.toException());
            }
        });
    }

    private void loadStepDataForSelectedDate() {
        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(selectedDate);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("loadTodayStepData", "DataSnapshot date: " + selectedDate);
                Buocchan todayStepData = dataSnapshot.getValue(Buocchan.class);
                if (todayStepData != null) {
                    tv1.setText("Số bước chân: " + todayStepData.getStepsCount());
                    tv2.setText("Mục tiêu: " + todayStepData.getGoal() + " bước");
                    tv3.setText(todayStepData.isGoalAchieved() ? "Bạn đã hoàn thành mục tiêu ngày hôm đó" : "Bạn còn thiếu " + (todayStepData.getGoal() - todayStepData.getStepsCount()) + " bước nữa. Hãy cố gắng trong ngày hôm sau nhé!");

                    // Tính toán và hiển thị calo tiêu thụ và khoảng cách
                    double caloriesBurned = calculateCalories(todayStepData.getStepsCount(), userWeight);
                    double distanceInKm = calculateDistanceInKm(todayStepData.getStepsCount());

                    tv4.setText("Calo đã tiêu thụ: " + caloriesBurned + " kcal");
                    tv5.setText("Khoảng cách: " + distanceInKm + " km");

                    int progress = (int) ((double) todayStepData.getStepsCount() / todayStepData.getGoal() * 100);
                    progressDrawable.setProgress(progress);
                } else {
                    tv1.setText("Steps: 0");
                    tv2.setText("Goal: ");
                    tv3.setText("Keep going! You can do it.");
                    tv4.setText("Calories: 0 kcal");
                    tv5.setText("Distance: 0 km");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadTodayStepData", "Error fetching today step data", error.toException());
            }
        });
    }

    private double calculateCalories(int stepsCount, double weight) {
        return stepsCount * weight * 0.04;
    }

    private double calculateDistanceInKm(int stepsCount) {
        return stepsCount * 0.000762;
    }
}