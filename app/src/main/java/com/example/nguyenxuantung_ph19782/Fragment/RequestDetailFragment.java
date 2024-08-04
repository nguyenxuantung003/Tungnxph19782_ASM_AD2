package com.example.nguyenxuantung_ph19782.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetailFragment extends Fragment {

    private TextView tvRequestContent;
    private TextView tvResponseContent;

    private DatabaseReference counselingRequestsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);

        tvRequestContent = view.findViewById(R.id.tvRequestContent);
        tvResponseContent = view.findViewById(R.id.tvResponseContent);

        String requestId = getArguments() != null ? getArguments().getString("requestId") : "";

        if (!requestId.isEmpty()) {
            counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests").child(requestId);
            counselingRequestsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CounselingRequest request = dataSnapshot.getValue(CounselingRequest.class);
                    if (request != null) {
                        tvRequestContent.setText(request.getContent());
                        tvResponseContent.setText(request.getResponse());
                    }
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