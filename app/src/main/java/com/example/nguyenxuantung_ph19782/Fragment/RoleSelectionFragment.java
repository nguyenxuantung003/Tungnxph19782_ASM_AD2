package com.example.nguyenxuantung_ph19782.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RoleSelectionFragment extends Fragment {

    private RadioGroup radioGroupRole;
    private Button btnConfirmRole;
    private DatabaseReference mdatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_role_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroupRole = view.findViewById(R.id.radioGroupRole);
        btnConfirmRole = view.findViewById(R.id.btnConfirmRole);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        btnConfirmRole.setOnClickListener(v -> {
            int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
            RadioButton selectedRoleButton = view.findViewById(selectedRoleId);
            if (selectedRoleButton != null) {
                String role = selectedRoleButton.getText().toString().toLowerCase();
                saveUserData(role);
            } else {
                Toast.makeText(getActivity(), "Please select a role.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String role) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String username = user.getDisplayName() != null ? user.getDisplayName() : "";
            String email = user.getEmail() != null ? user.getEmail() : "";
            String profileUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
            String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
            String updatedAt = createdAt;

            Users newUser = new Users(userId, username, role, email, createdAt, updatedAt, profileUrl);

            mdatabase.child(userId).setValue(newUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Role selected successfully", Toast.LENGTH_SHORT).show();
                        // Return to login screen
                        getActivity().finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}