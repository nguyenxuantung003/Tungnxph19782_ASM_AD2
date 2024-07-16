package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Loibieton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThemLoiBietOnActivity extends AppCompatActivity {
    private EditText etGratitudeNotes;
    private Button btnSave;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_loi_biet_on);
        etGratitudeNotes = findViewById(R.id.etGratitudeNotes);
        btnSave = findViewById(R.id.btnSave);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("MentalActivities");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gratitudeNote = etGratitudeNotes.getText().toString().trim();
                Log.d("DEBUG", "gratitudeNote: " + gratitudeNote);

                if (!TextUtils.isEmpty(gratitudeNote) && currentUser != null) {
                    String userId = currentUser.getUid();
                    String activityId = databaseReference.push().getKey();
                    String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                    Loibieton mentalActivity = new Loibieton(userId, timestamp, gratitudeNote, timestamp, timestamp, activityId);

                    if (activityId != null) {
                        databaseReference.child(activityId).setValue(mentalActivity, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error != null) {
                                    Toast.makeText(ThemLoiBietOnActivity.this, "Đã xảy ra lỗi khi thêm lời biết ơn!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ThemLoiBietOnActivity.this, "Lời biết ơn đã được thêm!", Toast.LENGTH_SHORT).show();
                                    finish(); // Đóng activity và quay lại màn hình trước đó
                                }
                            }
                        });
                    }

                    etGratitudeNotes.setText("");
                } else {
                    Toast.makeText(ThemLoiBietOnActivity.this, "Vui lòng nhập lời biết ơn!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}