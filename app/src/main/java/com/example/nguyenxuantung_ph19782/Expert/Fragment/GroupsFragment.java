package com.example.nguyenxuantung_ph19782.Expert.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.Activity.GroupDetailActivity;
import com.example.nguyenxuantung_ph19782.Expert.Adapter.GroupAdapter;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private DatabaseReference groupsRef;
    private Button btncreategroup;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups2, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGroups);
        btncreategroup = view.findViewById(R.id.createGroupButton2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupList = new ArrayList<>();

        // Initialize groupAdapter with OnItemClickListener
        groupAdapter = new GroupAdapter(groupList, group -> {
            // Handle item click
            Intent intent = new Intent(getContext(), GroupDetailActivity.class);
            intent.putExtra("groupId", group.getGroupId()); // Assuming Group has a getId() method
            startActivity(intent);
        });

        recyclerView.setAdapter(groupAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            groupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");

            groupsRef.orderByChild("creatorId").equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    groupList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Group group = snapshot.getValue(Group.class);
                        if (group != null) {
                            groupList.add(group);
                        }
                    }
                    groupAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors
                }
            });
        }

        btncreategroup.setOnClickListener(v -> {
            Fragment createGroupFragment = new CreateGroupFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_creategroup, createGroupFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}