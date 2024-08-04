package com.example.nguyenxuantung_ph19782.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MentalCounselingActivity extends AppCompatActivity {

    private Spinner spinnerCounselingField;
    private EditText etCounselingContent;
    private Button btnSendCounselingRequest;

    private FirebaseAuth auth;
    private DatabaseReference counselingRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_counseling);

        spinnerCounselingField = findViewById(R.id.spinnerCounselingField);
        etCounselingContent = findViewById(R.id.etCounselingContent);
        btnSendCounselingRequest = findViewById(R.id.btnSendCounselingRequest);

        auth = FirebaseAuth.getInstance();
        counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests");

        btnSendCounselingRequest.setOnClickListener(v -> sendCounselingRequest());
    }
    private void sendCounselingRequest() {
        String selectedField = spinnerCounselingField.getSelectedItem().toString();
        String content = etCounselingContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Nội dung không thể để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String username = user.getDisplayName();
            String email = user.getEmail();

            CounselingRequest request = new CounselingRequest(userId, username, email, selectedField, content, getCurrentDateTime());

            counselingRequestsRef.push().setValue(request)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MentalCounselingActivity.this, "Yêu cầu đã được gửi", Toast.LENGTH_SHORT).show();
                        finish(); // Đóng activity và quay lại màn hình trước đó
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MentalCounselingActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}