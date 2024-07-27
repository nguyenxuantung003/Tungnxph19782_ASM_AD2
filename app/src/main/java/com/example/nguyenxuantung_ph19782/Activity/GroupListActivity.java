package com.example.nguyenxuantung_ph19782.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.GroupAdapter;
import com.example.nguyenxuantung_ph19782.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupListActivity extends AppCompatActivity {


    private ListView groupListView;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private Button createGroupButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ListView groupListView = findViewById(R.id.groupListView);
        List<Group> groupList = new ArrayList<>();
        createGroupButton = findViewById(R.id.createGroupButton);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupListActivity.this,CreateGroupActivity.class));
            }
        });
        GroupAdapter groupAdapter = new GroupAdapter(this, groupList, new GroupAdapter.OnItemClickListener() {
            @Override
            public void onJoinClick(Group group) {
                joinGroup(group);
            }

            @Override
            public void onViewClick(Group group) {
                viewGroup(group);
            }
        });

        groupListView.setAdapter(groupAdapter);

        loadGroups(groupAdapter);
    }
    private void loadGroups(GroupAdapter groupAdapter) {
        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Group> groupList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    if (group != null) {
                        groupList.add(group);
                    }
                }
                groupAdapter.clear();
                groupAdapter.addAll(groupList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void joinGroup(Group group) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups").child(group.getGroupId());
        groupRef.child("members").child(userId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(GroupListActivity.this, "Joined the group", Toast.LENGTH_SHORT).show();
                        // Refresh group list to update button text
                        loadGroups((GroupAdapter) ((ListView) findViewById(R.id.groupListView)).getAdapter());
                    } else {
                        Toast.makeText(GroupListActivity.this, "Failed to join group", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewGroup(Group group) {
        Intent intent = new Intent(GroupListActivity.this, GroupDetailActivity.class);
        intent.putExtra("groupId", group.getGroupId());
        startActivity(intent);
    }
}