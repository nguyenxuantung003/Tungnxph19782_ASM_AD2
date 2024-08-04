package com.example.nguyenxuantung_ph19782.Expert.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConsultationRequestDetailFragment extends Fragment {

    private TextView requestTitleDetail;
    private TextView requestDescriptionDetail;
    private EditText responseEditText;
    private Button sendResponseButton;
    private DatabaseReference requestRef; // Reference to Firebase for requests

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultation_request_detail, container, false);

        requestTitleDetail = view.findViewById(R.id.txt_request_title_detail);
        requestDescriptionDetail = view.findViewById(R.id.txt_request_description_detail);
        responseEditText = view.findViewById(R.id.edt_response);
        sendResponseButton = view.findViewById(R.id.btn_send_response);

        // Initialize Firebase Database reference
        requestRef = FirebaseDatabase.getInstance().getReference("CounselingRequests");

        // Load request details from arguments
        Bundle args = getArguments();
        if (args != null) {
            String requestTitle = args.getString("username");
            String requestDescription = args.getString("content");
            String requestId = args.getString("requestId"); // Ensure requestId is passed

            requestTitleDetail.setText(requestTitle);
            requestDescriptionDetail.setText(requestDescription);

            sendResponseButton.setOnClickListener(v -> {
                String response = responseEditText.getText().toString();
                if (!response.isEmpty()) {
                    handleSendResponse(requestId, response); // Pass requestId to handleSendResponse
                } else {
                    Toast.makeText(getContext(), "Please enter a response", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void handleSendResponse(String requestId, String response) {
        if (requestId != null) {
            // Update the request in Firebase with the response and status
            requestRef.child(requestId).child("response").setValue(response);
            requestRef.child(requestId).child("status").setValue("responded")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Response sent successfully", Toast.LENGTH_SHORT).show();
                        responseEditText.setText(""); // Clear the response field
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to send response: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "id null", Toast.LENGTH_SHORT).show();
        }
    }
}