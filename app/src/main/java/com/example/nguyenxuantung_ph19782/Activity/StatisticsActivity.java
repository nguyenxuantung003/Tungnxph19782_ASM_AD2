package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvFriendsCount, tvStepsCount, tvGoalsCompleted, tvMentalActivitiesCount, tvPhysicalActivitiesCount;
    private Spinner spinnerTimeRange;
    private DatabaseReference databaseReference;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tvFriendsCount = findViewById(R.id.tvFriendsCount);
        tvStepsCount = findViewById(R.id.tvStepsCount);
        tvGoalsCompleted = findViewById(R.id.tvGoalsCompleted);
        tvMentalActivitiesCount = findViewById(R.id.tvMentalActivitiesCount);
        tvPhysicalActivitiesCount = findViewById(R.id.tvPhysicalActivitiesCount);
        spinnerTimeRange = findViewById(R.id.spinnerTimeRange);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        spinnerTimeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTimeRange = parent.getItemAtPosition(position).toString();
                loadStatistics(selectedTimeRange);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Load initial statistics for the default time range (e.g., week)
        loadStatistics("Week");
    }

    private void loadStatistics(String timeRange) {
        // Load friends count
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(userId);
        friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long friendsCount = snapshot.getChildrenCount();
                tvFriendsCount.setText("Số lượng người đã kết bạn :" + friendsCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "Failed to load friends count", Toast.LENGTH_SHORT).show();
            }
        });
            // lấy số bước chân
        DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference("PhysicalActivities").child(userId);
        long startDate = getStartDateForTimeRange(timeRange);
        stepsRef.orderByChild("createdAt").startAt(startDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long stepsCount = 0;
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    Long steps = activitySnapshot.child("stepsCount").getValue(Long.class);
                    if (steps != null) {
                        stepsCount += steps;
                    }
                }
                tvStepsCount.setText("Số bước chân đã đi được :" + stepsCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "Failed to load steps count", Toast.LENGTH_SHORT).show();
            }
        });

        // Load số ngày hoàn thành mục tiêu
        DatabaseReference goalsRef = databaseReference.child("PhysicalActivities").child(userId);
        goalsRef.orderByChild("goalAchieved").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> uniqueDates = new HashSet<>();
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    String date = activitySnapshot.child("date").getValue(String.class);
                    if (date != null && isWithinTimeRange(date, timeRange)) {
                        uniqueDates.add(date);
                    }
                }
                tvGoalsCompleted.setText("Số ngày hoàn thành mục tiêu :" + uniqueDates.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "Failed to load goals count", Toast.LENGTH_SHORT).show();
            }
        });

        // Load mental activities
        databaseReference.child("MentalActivities")
                .orderByChild("userId")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long mentalActivitiesCount = 0;
                        for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                            String date = activitySnapshot.child("date").getValue(String.class);
                            if (isWithinTimeRange(date, timeRange)) {
                                mentalActivitiesCount++;
                            }
                        }
                        tvMentalActivitiesCount.setText("Lời biết ơn đã viết :" + mentalActivitiesCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StatisticsActivity.this, "Failed to load mental activities count", Toast.LENGTH_SHORT).show();
                    }
                });

        // Load physical activities
        databaseReference.child("PhysicalActivities").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long physicalActivitiesCount = 0;
                        for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                            long timestamp = activitySnapshot.child("createdAt").getValue(Long.class);
                            if (isWithinTimeRange(timestamp, timeRange)) {
                                physicalActivitiesCount++;
                            }
                        }
                        tvPhysicalActivitiesCount.setText("Ngày đã đi : " + physicalActivitiesCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StatisticsActivity.this, "Failed to load physical activities count", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private long getStartDateForTimeRange(String timeRange) {
        Calendar calendar = Calendar.getInstance();
        switch (timeRange) {
            case "Week":
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case "Month":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "All Time":
                calendar.add(Calendar.YEAR, -100); // effectively all time
                break;
        }
        return calendar.getTimeInMillis();
    }

    private boolean isWithinTimeRange(String dateString, String timeRange) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            return date.getTime() >= getStartDateForTimeRange(timeRange);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isWithinTimeRange(long timestamp, String timeRange) {
        return timestamp >= getStartDateForTimeRange(timeRange);
    }
}