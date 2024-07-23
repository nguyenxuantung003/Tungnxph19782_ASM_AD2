package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Buocchan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class QuanlyvandongActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepsTextView, goalTextView, statusTextView;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isSensorPresent;
    private int stepCount = 199;
    private int stepGoal = 10000; // Default goal
    private DatabaseReference databaseReference;
    private String userId;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlyvandong);
        stepsTextView = findViewById(R.id.steps_text_view);
        goalTextView = findViewById(R.id.goal_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
            Log.d("SensorCheck", "Step Counter Sensor is available.");
        } else {
            stepsTextView.setText("Step Counter Sensor is not available!");
            isSensorPresent = false;
            Log.d("SensorCheck", "Step Counter Sensor is not available.");
        }
        // Request permission for Android 10+
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        }

        // Get userId from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Get user ID from FirebaseAuth
        } else {
            // Handle case where user is not logged in
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_LONG).show();
            finish(); // Exit the activity
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("PhysicalActivities");
        initializeStepData();
        loadStepGoal();
    }

    private void initializeStepData() {
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities");
        // Tạo một truy vấn để kiểm tra xem dữ liệu bước chân đã tồn tại chưa
        userStepRef.orderByChild("userId").equalTo(userId).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Nếu không có dữ liệu, tạo dữ liệu bước chân mới
                    String initialActivityId = UUID.randomUUID().toString();
                    long currentTime = System.currentTimeMillis();

                    Buocchan initialStepData = new Buocchan(userId, initialActivityId, currentTime, stepCount, stepGoal, false, currentTime, currentTime);
                    userStepRef.child(userId).setValue(initialStepData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("QuanlyvandongActivity", "Initial step data created successfully");
                        } else {
                            Log.e("QuanlyvandongActivity", "Failed to create initial step data", task.getException());
                        }
                    });
                } else {

                    Log.d("QuanlyvandongActivity", "Step data already exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuanlyvandongActivity", "Error checking step data existence", databaseError.toException());
            }
        });
    }

    private void updateStepData(int newStepCount) {
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId);
        userStepRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Buocchan existingStepData = dataSnapshot.getValue(Buocchan.class);
                    if (existingStepData != null) {
                        // Update step count and timestamps
                        existingStepData.setStepsCount(newStepCount);
                        existingStepData.setUpdatedAt(System.currentTimeMillis());

                        // Save updated data
                        userStepRef.setValue(existingStepData).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("updatestep", "Step data updated successfully");
                            } else {
                                Log.e("updatestep", "Failed to update step data", task.getException());
                            }
                        });
                    }
                } else {
                    Log.d("updatestep", "No step data found to update");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("QuanlyvandongActivity", "Error fetching step data", databaseError.toException());
            }
        });
    }

    private void loadStepGoal() {
        DatabaseReference physicalActivitiesRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities");
        physicalActivitiesRef.orderByChild("userId").equalTo(userId).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer goal = snapshot.child("goal").getValue(Integer.class);
                        Integer stepsCount = snapshot.child("stepsCount").getValue(Integer.class);
                        if (goal != null) {
                            stepGoal = goal;
                            goalTextView.setText("Goal: " + stepGoal + " steps");
                        }
                        if (stepsCount != null) {
                            stepsTextView.setText("Steps: " + stepsCount);
                        }
                    }
                } else {
                    goalTextView.setText("No goal set.");
                    stepsTextView.setText("Steps: 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loadStepGoal", "Error fetching data", databaseError.toException());
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isSensorPresent) {
            // Kiểm tra loại cảm biến
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                stepCount = (int) event.values[0];
                stepsTextView.setText("Steps: " + stepCount);
                Log.d("SensorData", "Sensor data received: " + stepCount);
                updateStepData(stepCount); // Cập nhật dữ liệu bước chân
                updateStatus();
            }
        }
    }

    private void updateStatus() {
        if (stepCount >= stepGoal) {
            statusTextView.setText("Congratulations! You've reached your goal.");
        } else {
            statusTextView.setText("Keep going! You can do it.");
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this example
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("onresume", "Sensor registered");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
            Log.d("onpause", "Sensor unregistered");
        }
    }
}