package com.example.nguyenxuantung_ph19782.Expert.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticsFragment extends Fragment {

    private TextView soluongyeucau;
    private TextView sonhomdatra;
    private DatabaseReference requestsRef;
    private DatabaseReference groupsRef;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        soluongyeucau = view.findViewById(R.id.soluongyeucau);
        sonhomdatra = view.findViewById(R.id.sonhomdatra);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requestsRef = FirebaseDatabase.getInstance().getReference("CounselingRequests");
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups");

        loadStatistics();

        return view;
    }

    private void loadStatistics() {
        // Count solved requests
        requestsRef.orderByChild("expertId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int resolvedCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String status = snapshot.child("status").getValue(String.class);
                    if ("responded".equals(status)) {
                        resolvedCount++;
                    }
                }
                soluongyeucau.setText("Số lượng yêu cầu đã giải quyết : " + resolvedCount);
                // Do something with the count, e.g., update the UI
                Toast.makeText(getContext(), "Number of resolved requests: " + resolvedCount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Count created groups
        groupsRef.orderByChild("creatorId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int groupCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    groupCount++;
                }
                sonhomdatra.setText("Số nhóm đã tạo: " + groupCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}