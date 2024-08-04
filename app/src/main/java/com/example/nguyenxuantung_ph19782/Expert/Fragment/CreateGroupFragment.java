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
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    private EditText editTextGroupName;
    private EditText editTextGroupDescription;
    private Button buttonCreateGroup;

    private DatabaseReference groupsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        editTextGroupName = view.findViewById(R.id.editTextGroupName);
        editTextGroupDescription = view.findViewById(R.id.editTextGroupDescription);
        buttonCreateGroup = view.findViewById(R.id.buttonCreateGroup);

        groupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        buttonCreateGroup.setOnClickListener(v -> createGroup());

        return view;
    }

    private void createGroup() {
        String groupName = editTextGroupName.getText().toString().trim();
        String description = editTextGroupDescription.getText().toString().trim();

        if (groupName.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String creatorId = user.getUid();
            String groupId = UUID.randomUUID().toString(); // Tạo ID nhóm duy nhất
            Map<String, Boolean> members = new HashMap<>();
            members.put(creatorId, true); // Thêm người tạo nhóm vào danh sách thành viên

            Group group = new Group(groupId, groupName, description, members, creatorId);

            groupsRef.child(groupId).setValue(group)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Group created successfully", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack(); // Quay lại fragment trước đó
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}