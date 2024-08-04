package com.example.nguyenxuantung_ph19782.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SendRequestFragment extends Fragment {

    private Spinner spinnerCounselingField;
    private EditText etCounselingContent;
    private Button btnSendCounselingRequest;

    private FirebaseAuth auth;
    private DatabaseReference counselingRequestsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);

        spinnerCounselingField = view.findViewById(R.id.spinnerCounselingField);
        etCounselingContent = view.findViewById(R.id.etCounselingContent);
        btnSendCounselingRequest = view.findViewById(R.id.btnSendCounselingRequest);

        auth = FirebaseAuth.getInstance();
        counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests");

        btnSendCounselingRequest.setOnClickListener(v -> sendCounselingRequest());

        return view;
    }

    private void sendCounselingRequest() {
        String selectedField = spinnerCounselingField.getSelectedItem().toString();
        String content = etCounselingContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Nội dung không thể để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String username = user.getDisplayName();
            String email = user.getEmail();

            // Tạo một khóa mới cho yêu cầu
            String requestId = counselingRequestsRef.push().getKey();

            // Kiểm tra xem requestId có null không
            if (requestId != null) {
                CounselingRequest request = new CounselingRequest(requestId, content, getCurrentDateTime(), email, selectedField, userId, username, "pending", "");

                // Lưu yêu cầu với requestId là khóa
                counselingRequestsRef.child(requestId).setValue(request)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Yêu cầu đã được gửi", Toast.LENGTH_SHORT).show();
                            // Thực hiện hành động sau khi gửi yêu cầu (ví dụ: quay lại hoặc làm mới dữ liệu)
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "Lỗi: Không thể tạo requestId", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}