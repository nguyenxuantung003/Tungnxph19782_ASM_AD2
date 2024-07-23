package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, ageEditText, genderEditText, heightEditText, weightEditText;
    private Button updateButton,goiythucdonButton;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        ageEditText = findViewById(R.id.age);
        genderEditText = findViewById(R.id.gender);
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        updateButton = findViewById(R.id.update_button);
        goiythucdonButton = findViewById(R.id.goi_y_thuc_don_btn);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        loadUserInfo();

        updateButton.setOnClickListener(v -> updateUserInfo());
        goiythucdonButton.setOnClickListener(v -> openDietSuggestionActivity());

        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateBMI();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateBMI();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    private void loadUserInfo() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    Integer age = dataSnapshot.child("profile").child("age").getValue(Integer.class);
                    String gender = dataSnapshot.child("profile").child("gender").getValue(String.class);
                    Float height = dataSnapshot.child("profile").child("height").getValue(Float.class);
                    Float weight = dataSnapshot.child("profile").child("weight").getValue(Float.class);

                    usernameEditText.setText(username);
                    emailEditText.setText(email);
                    ageEditText.setText(String.valueOf(age));
                    genderEditText.setText(gender);
                    heightEditText.setText(String.valueOf(height));
                    weightEditText.setText(String.valueOf(weight));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String weight = weightEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(age) ||
                TextUtils.isEmpty(gender) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("username", username);
        userUpdates.put("email", email);
        userUpdates.put("profile/age", Integer.parseInt(age));
        userUpdates.put("profile/gender", gender);
        userUpdates.put("profile/height", Float.parseFloat(height));
        userUpdates.put("profile/weight", Float.parseFloat(weight));

        databaseReference.updateChildren(userUpdates)
                .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
    }

    private float calculateBMI(float weight, float height) {
        if (height == 0) return 0;
        return weight / ((height / 100) * (height / 100));
    }

    private void displayBMI(float bmi) {
        TextView bmiTextView = findViewById(R.id.bmi_text_view);
        bmiTextView.setText("BMI: " + String.format("%.2f", bmi));
    }

    private void updateBMI() {
        String heightStr = heightEditText.getText().toString();
        String weightStr = weightEditText.getText().toString();

        if (!TextUtils.isEmpty(heightStr) && !TextUtils.isEmpty(weightStr)) {
            float height = Float.parseFloat(heightStr);
            float weight = Float.parseFloat(weightStr);
            float bmi = calculateBMI(weight, height);
            displayBMI(bmi);
        }
    }

    private void openDietSuggestionActivity() {
        Intent intent = new Intent(ProfileActivity.this, GoiythucdonActivity.class);
        intent.putExtra("userId", currentUser.getUid());
        startActivity(intent);
    }

}