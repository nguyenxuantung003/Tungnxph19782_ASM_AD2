package com.example.nguyenxuantung_ph19782.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.CounselingRequestAdapter;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SentRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CounselingRequestAdapter adapter;
    private List<CounselingRequest> requestList;

    private DatabaseReference counselingRequestsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sent_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSentRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestList = new ArrayList<>();
        adapter = new CounselingRequestAdapter(requestList, request -> {
            // Handle item click
        });
        recyclerView.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests");

            counselingRequestsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CounselingRequest request = snapshot.getValue(CounselingRequest.class);
                        if (request != null) {
                            requestList.add(request);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors
                }
            });
        }

        return view;
    }
}