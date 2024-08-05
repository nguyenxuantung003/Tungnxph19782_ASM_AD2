package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class HomeActivity extends AppCompatActivity {
    private CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;
    private BottomNavigationView menuHome;
    @SuppressLint({"MissingInflatedId", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menuHome = findViewById(R.id.bottom_navigation_home);
        cardView1 = findViewById(R.id.carview1);
        cardView2 = findViewById(R.id.carview2);
        cardView3 = findViewById(R.id.carview3);
        cardView4 = findViewById(R.id.carview4);
        cardView5 = findViewById(R.id.carview5);
        cardView6 = findViewById(R.id.carview6);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoiBietOnActivity.class));
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MentalCounselingActivity.class));
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GroupListActivity.class));
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,QuanlyvandongActivity.class));
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExerciseListActivity.class));
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BMIActivity.class));
            }
        });








        menuHome.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        menuHome.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home_btn1) {
                    startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                    // Handle home click
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn2) {

                    Intent intent = new Intent(HomeActivity.this, AddFriendActivity.class);
                    startActivity(intent);
                    // Handle search click
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn3) {
                    startActivity(new Intent(HomeActivity.this, FriendActivity.class));
                    // Handle health click
                    return true;
                } else if (item.getItemId() == R.id.menu_home_btn4) {
                    startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                    // Handle profile click
                    return true;
                } else if(item.getItemId() == R.id.menu_home_btn5){
                    startActivity(new Intent(HomeActivity.this,StatisticsActivity.class));
                }
                return false;
            }
        });
    }
}