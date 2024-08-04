package com.example.nguyenxuantung_ph19782.Expert.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.nguyenxuantung_ph19782.R;

import java.util.ArrayList;
import java.util.List;

public class ExpertRequestsActivity extends AppCompatActivity {

    private ListView listViewRequests;
    private DatabaseReference counselingRequestsRef;
    private FirebaseAuth auth;
    private ArrayAdapter<CounselingRequest> adapter;
    private List<CounselingRequest> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_requests);

        listViewRequests = findViewById(R.id.listViewRequests);

        auth = FirebaseAuth.getInstance();
        counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests");

        requests = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requests);
        listViewRequests.setAdapter(adapter);

        loadRequests();
    }

    private void loadRequests() {
        counselingRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CounselingRequest request = snapshot.getValue(CounselingRequest.class);
                    requests.add(request);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(ExpertRequestsActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}