package com.example.nguyenxuantung_ph19782.Expert.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.example.nguyenxuantung_ph19782.Expert.Adapter.ConsultationRequestsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ConsultationRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConsultationRequestsAdapter adapter;
    private List<CounselingRequest> requests;
    private DatabaseReference counselingRequestsRef;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultation_requests, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_consultation_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        counselingRequestsRef = FirebaseDatabase.getInstance().getReference().child("CounselingRequests");

        requests = new ArrayList<>();
        adapter = new ConsultationRequestsAdapter(getContext(), requests, getChildFragmentManager());
        recyclerView.setAdapter(adapter);

        loadRequests();

        return view;
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
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}