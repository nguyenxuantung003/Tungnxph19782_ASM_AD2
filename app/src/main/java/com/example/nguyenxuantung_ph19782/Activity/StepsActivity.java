package com.example.nguyenxuantung_ph19782.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nguyenxuantung_ph19782.CircularProgressDrawable;
import com.example.nguyenxuantung_ph19782.R;

public class StepsActivity extends AppCompatActivity {

    private TextView dateTextView, moveTextView, caloriesTextView, stepsTextView, distanceTextView, floorsTextView;
    private ImageView progressImageView;
    private CircularProgressDrawable progressDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        dateTextView = findViewById(R.id.dateTextView);
        moveTextView = findViewById(R.id.moveTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        stepsTextView = findViewById(R.id.stepsTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        floorsTextView = findViewById(R.id.floorsTextView);

        progressImageView = findViewById(R.id.progressImageView);
        progressDrawable = new CircularProgressDrawable(100); // Giá trị tối đa là 100
        progressImageView.setImageDrawable(progressDrawable);


        // Cập nhật dữ liệu từ Firebase hoặc các cảm biến
        // Ví dụ dữ liệu tĩnh:
        updateData("Thứ Hai, ngày 29 thg 7, 2024", 120, 130, 1269, 0.74, 3);
    }
    private void updateData(String date, int currentCalories, int goalCalories, int steps, double distance, int floors) {
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);
        TextView stepsTextView = findViewById(R.id.stepsTextView);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView floorsTextView = findViewById(R.id.floorsTextView);

        dateTextView.setText(date);
        caloriesTextView.setText(currentCalories + "/" + goalCalories + " KCAL");
        stepsTextView.setText("Bước: " + steps);
        distanceTextView.setText("Quãng đường: " + distance + "KM");
        floorsTextView.setText("Bậc thang Đã leo: " + floors);

        int progress = (int) ((currentCalories / (float) goalCalories) * 100);
        progressDrawable.setProgress(progress);
    }
}