package com.example.nguyenxuantung_ph19782.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.GoalAdapter;
import com.example.nguyenxuantung_ph19782.adapter.GoalAdapter;
import com.example.nguyenxuantung_ph19782.model.GoalGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GoalsFragment extends Fragment {

    private ListView goalsListView;
    private Button addGoalButton;
    private GoalAdapter adapter;
    private List<GoalGroup> goalList = new ArrayList<>();
    private String groupId;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        goalsListView = view.findViewById(R.id.goalsListView);
        addGoalButton = view.findViewById(R.id.addGoalButton);

        // Get the groupId from arguments
        if (getArguments() != null) {
            groupId = getArguments().getString("groupId");
        }

        // Initialize the current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set up the ListView and adapter
        adapter = new GoalAdapter(getContext(), goalList);
        goalsListView.setAdapter(adapter);

        loadGoals();

        addGoalButton.setOnClickListener(v -> {
            // Check if the current user is the creator of the group
            DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId);
            groupRef.child("creatorId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String creatorId = snapshot.getValue(String.class);
                    if (creatorId != null && creatorId.equals(currentUserId)) {
                        showAddGoalDialog();
                    } else {
                        Toast.makeText(getContext(), "You are not allowed to add goals", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to check permissions", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void loadGoals() {
        DatabaseReference goalsRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("goals");
        goalsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                goalList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GoalGroup goal = snapshot.getValue(GoalGroup.class);
                    if (goal != null) {
                        goalList.add(goal);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void showAddGoalDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_goalgroup, null);

        final EditText titleEditText = dialogView.findViewById(R.id.goalTitleEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.goalDescriptionEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Goal");
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (!title.isEmpty() && !description.isEmpty()) {
                    addGoal(title, description);
                } else {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void addGoal(String title, String description) {
        DatabaseReference goalsRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("goals");
        String goalId = goalsRef.push().getKey();

        if (goalId != null) {
            GoalGroup newGoal = new GoalGroup(title, description, currentUserId);
            goalsRef.child(goalId).setValue(newGoal).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Goal added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add goal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}