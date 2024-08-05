package com.example.nguyenxuantung_ph19782.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.CircularProgressDrawable;
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
import java.util.UUID;

public class QuanlyvandongActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepsTextView, goalTextView, statusTextView, caloriesTextView;
    private RecyclerView historyRecyclerView;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isSensorPresent;
    private int stepCount = 0;
    private int stepGoal = 200; // Default goal
    private DatabaseReference databaseReference;
    private String userId;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1;
    private BuocchanAdapter stepHistoryAdapter;
    private double userWeight = 70.0; // Default weight
    private DatabaseReference userRef;
    private List<Buocchan> stepHistoryList = new ArrayList<>();
    private EditText goalEditText;
    private Button updateGoalButton;
    private ImageView progressImageView;
    private CircularProgressDrawable progressDrawable;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlyvandong);

        progressImageView = findViewById(R.id.progressImageView);
        progressDrawable = new CircularProgressDrawable(100); // Max value is 100
        progressImageView.setImageDrawable(progressDrawable);

        stepsTextView = findViewById(R.id.steps_text_view);
        goalTextView = findViewById(R.id.goal_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        historyRecyclerView = findViewById(R.id.history_recycler_view);
        caloriesTextView = findViewById(R.id.calories_text_view);
        goalEditText = findViewById(R.id.goal_edit_text);
        updateGoalButton = findViewById(R.id.update_goal_button);

        updateGoalButton.setOnClickListener(v -> {
            String newGoalString = goalEditText.getText().toString();
            if (!newGoalString.isEmpty()) {
                try {
                    int newGoal = Integer.parseInt(newGoalString);
                    updateDailyGoal(newGoal);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid goal number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a goal", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup RecyclerView
        stepHistoryAdapter = new BuocchanAdapter(stepHistoryList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(stepHistoryAdapter);

        stepHistoryAdapter.setOnItemClickListener(date -> {
            Intent intent = new Intent(QuanlyvandongActivity.this, LichsubuocchanActivity.class);
            intent.putExtra("selectedDate", date);
            startActivity(intent);
        });

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

        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        } else {
            registerStepSensor();
        }

        // Get userId from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("profile");
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("PhysicalActivities");
        initializeStepData();
        loadStepGoal();
        loadStepHistory();
        loadTodayStepData();
        getUserWeight();
    }

    private void registerStepSensor() {
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void updateDailyGoal(int newGoal) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate);

        userStepRef.child("goal").setValue(newGoal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(QuanlyvandongActivity.this, "Goal updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QuanlyvandongActivity.this, "Failed to update goal", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.e("loadStepHistory", "Error loading step history", databaseError.toException());
            }
        });
    }

    private void loadTodayStepData() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate);

        userStepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Buocchan stepData = dataSnapshot.getValue(Buocchan.class);
                    if (stepData != null) {
                        stepCount = stepData.getStepsCount();
                        stepGoal = stepData.getGoal();
                        stepsTextView.setText(String.valueOf(stepCount));
                        goalTextView.setText(String.valueOf(stepGoal));
                        updateProgress(stepCount, stepGoal);
                        if (stepCount >= stepGoal) {
                            statusTextView.setText("Bạn đã hoàn thành mục tiêu");
                        } else {
                            statusTextView.setText("Tiếp tục cố gắng!");
                        }
                        updateCalories();
                    }
                } else {
                    createNewStepData(currentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("loadTodayStepData", "Error loading today's step data", databaseError.toException());
            }
        });
    }

    private void updateProgress(int steps, int goal) {
        int progressPercentage = (int) ((steps / (double) goal) * 100);
        progressDrawable.setProgress(progressPercentage);
    }

    private void updateCalories() {
        double caloriesBurned = calculateCaloriesBurned(stepCount, userWeight);
        caloriesTextView.setText(String.format(Locale.getDefault(), "%.2f", caloriesBurned));
    }

    private double calculateCaloriesBurned(int steps, double weight) {
        double stepsToKm = steps * 0.0008; // Average step length of 0.8 meters
        double caloriesPerKmPerKg = 0.75; // Average calories burned per km per kg of body weight
        return stepsToKm * weight * caloriesPerKmPerKg;
    }

    private void loadStepGoal() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate).child("goal");

        userStepRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer fetchedGoal = dataSnapshot.getValue(Integer.class);
                    if (fetchedGoal != null) {
                        stepGoal = fetchedGoal;
                        goalTextView.setText(String.valueOf(stepGoal));
                    }
                } else {
                    createNewStepData(currentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("loadStepGoal", "Error loading step goal", databaseError.toException());
            }
        });
    }

    private void getUserWeight() {
        if (userRef != null) {
            userRef.child("weight").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Double weight = dataSnapshot.getValue(Double.class);
                        if (weight != null) {
                            userWeight = weight;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("getUserWeight", "Error loading user weight", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            stepsTextView.setText(String.valueOf(stepCount));
            updateProgress(stepCount, stepGoal);
            updateCalories();
            saveStepCountToFirebase();
        }
    }

    private void saveStepCountToFirebase() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference userStepRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId).child(currentDate).child("stepsCount");

        userStepRef.setValue(stepCount).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("saveStepCountToFirebase", "Failed to save step count", task.getException());
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle sensor accuracy changes if needed
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerStepSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerStepSensor();
            } else {
                Toast.makeText(this, "Permission for activity recognition is required for step counting", Toast.LENGTH_SHORT).show();
            }
        }
    }
}