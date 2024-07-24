package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.nguyenxuantung_ph19782.adapter.BuocchanAdapter;
import com.example.nguyenxuantung_ph19782.model.Buocchan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class QuanlyvandongActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepsTextView, goalTextView, statusTextView;
    private RecyclerView historyRecyclerView;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isSensorPresent;
    private int stepCount = 0;
    private int stepGoal = 10000; // Default goal
    private DatabaseReference databaseReference;
    private String userId;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1;
    private BuocchanAdapter stepHistoryAdapter;
    private List<Buocchan> stepHistoryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlyvandong);

        stepsTextView = findViewById(R.id.steps_text_view);
        goalTextView = findViewById(R.id.goal_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        historyRecyclerView = findViewById(R.id.history_recycler_view);

        // Setup RecyclerView
        stepHistoryAdapter = new BuocchanAdapter(stepHistoryList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(stepHistoryAdapter);


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
            // xin quyền
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
        loadStepHistory();
        loadTodayStepData();
 
    }

    private void initializeStepData() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate);

        userStepRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createNewStepData(currentDate);
                } else {
                    Log.d("QuanlyvandongActivity", "Step data already exists for date: " + currentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuanlyvandongActivity", "Error checking step data existence", databaseError.toException());
            }
        });
    }

    private void createNewStepData(String date) {
        String activityId = UUID.randomUUID().toString();
        long currentTime = System.currentTimeMillis();

        Buocchan newStepData = new Buocchan(userId, activityId, date, stepCount, stepGoal, false, currentTime, currentTime);
        databaseReference.child(userId).child(date).setValue(newStepData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("QuanlyvandongActivity", "Step data created successfully for date: " + date);
            } else {
                Log.e("QuanlyvandongActivity", "Failed to create step data", task.getException());
            }
        });
    }


    private void loadStepHistory() {
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId);
        userStepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stepHistoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buocchan stepHistory = snapshot.getValue(Buocchan.class);
                    if (stepHistory != null) {
                        stepHistoryList.add(stepHistory);
                    } else {
                        Log.e("loadStepHistory", "Error deserializing Buocchan object");
                    }
                }
                stepHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("loadStepHistory", "Error fetching step history", databaseError.toException());
            }
        });
    }


    private void loadTodayStepData() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference todayStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate);

        todayStepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("loadTodayStepData", "DataSnapshot value: " + dataSnapshot.getValue());
                Buocchan todayStepData = dataSnapshot.getValue(Buocchan.class);
                if (todayStepData != null) {
                    stepsTextView.setText("Steps: " + todayStepData.getStepsCount());
                    goalTextView.setText("Goal: " + todayStepData.getGoal() + " steps");
                    statusTextView.setText(todayStepData.isGoalAchieved() ? "Goal achieved!" : "Keep going! " + (todayStepData.getGoal() - todayStepData.getStepsCount()) + " steps remaining.");
                } else {
                    stepsTextView.setText("Steps: 0");
                    goalTextView.setText("Goal: " + stepGoal + " steps");
                    statusTextView.setText("Keep going! You can do it.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("loadTodayStepData", "Error fetching today step data", databaseError.toException());
            }
        });
    }
    private void loadStepGoal() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.child("stepGoal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    stepGoal = dataSnapshot.getValue(Integer.class);
                    goalTextView.setText("Goal: " + stepGoal + " steps");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("loadStepGoal", "Error fetching step goal", databaseError.toException());
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            stepsTextView.setText("Steps: " + stepCount);

            if (stepCount >= stepGoal) {
                statusTextView.setText("Goal achieved!");
            } else {
                int stepsRemaining = stepGoal - stepCount;
                statusTextView.setText("Keep going! " + stepsRemaining + " steps remaining.");
            }

            // Update the steps count in the Firebase database
            updateStepCountInFirebase();
        }
    }

    private void updateStepCountInFirebase() {
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId);
        userStepRef.orderByChild("date").equalTo(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))
                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                if (key != null) {
                                    snapshot.getRef().child("stepsCount").setValue(stepCount);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("updateStepCount", "Error updating step count", databaseError.toException());
                    }
                });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

}