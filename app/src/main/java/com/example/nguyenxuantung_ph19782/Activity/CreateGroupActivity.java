package com.example.nguyenxuantung_ph19782.Activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText groupNameEditText;
    private EditText groupDescriptionEditText;
    private Button createGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = findViewById(R.id.groupNameEditText);
        groupDescriptionEditText = findViewById(R.id.groupDescriptionEditText);
        createGroupButton = findViewById(R.id.createGroupButton);

        createGroupButton.setOnClickListener(v -> createGroup());
    }

    private void createGroup() {
        String groupName = groupNameEditText.getText().toString().trim();
        String groupDescription = groupDescriptionEditText.getText().toString().trim();

        if (groupName.isEmpty() || groupDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        String groupId = UUID.randomUUID().toString(); // Tạo ID nhóm duy nhất
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Boolean> members = new HashMap<>();
        members.put(userId, true); // Thêm người tạo nhóm vào danh sách thành viên

        Group newGroup = new Group(groupId, groupName, groupDescription, members, userId);
        groupsRef.child(groupId).setValue(newGroup).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreateGroupActivity.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình danh sách nhóm
            } else {
                Toast.makeText(CreateGroupActivity.this, "Failed to create group", Toast.LENGTH_SHORT).show();
            }
        });
    }

}