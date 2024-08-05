package com.example.nguyenxuantung_ph19782.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.FriendRequestAdapter;
import com.example.nguyenxuantung_ph19782.model.FriendRequest;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestsFragment extends Fragment {

    private RecyclerView friendRequestRecyclerView;
    private FriendRequestAdapter friendRequestAdapter;
    private List<FriendRequest> friendRequestList;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);

        friendRequestRecyclerView = view.findViewById(R.id.friendRequestRecyclerView);

        friendRequestList = new ArrayList<>();
        friendRequestAdapter = new FriendRequestAdapter(friendRequestList, new FriendRequestAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(FriendRequest friendRequest) {
                acceptFriendRequest(friendRequest);
            }

            @Override
            public void onRejectClick(FriendRequest friendRequest) {
                rejectFriendRequest(friendRequest);
            }
        });

        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendRequestRecyclerView.setAdapter(friendRequestAdapter);

        loadFriendRequests();

        return view;
    }

    private void loadFriendRequests() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        Query query = friendRequestRef.orderByChild("to").equalTo(currentUserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendRequestList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FriendRequest request = snapshot.getValue(FriendRequest.class);
                    if (request != null) {
                        request.setRequestId(snapshot.getKey());

                        String fromUserId = request.getFrom();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(fromUserId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userSnapshot) {
                                Users user = userSnapshot.getValue(Users.class); // Assuming 'Users' model has 'username' field
                                if (user != null) {
                                    request.setUserName(user.getUsername());
                                    friendRequestList.add(request);
                                    friendRequestAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                if (friendRequestList.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn không có lời mời kết bạn nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi tải yêu cầu kết bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptFriendRequest(FriendRequest friendRequest) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends");

        String requestId = friendRequest.getRequestId();
        if (requestId == null) {
            Toast.makeText(getContext(), "ID yêu cầu không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        friendsRef.child(currentUserId).child(friendRequest.getFrom()).setValue(true);
        friendsRef.child(friendRequest.getFrom()).child(currentUserId).setValue(true);

        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        friendRequestRef.child(requestId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa yêu cầu từ danh sách và thông báo cho adapter
                friendRequestList.remove(friendRequest);
                friendRequestAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Yêu cầu đã được chấp nhận", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lỗi khi chấp nhận yêu cầu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectFriendRequest(FriendRequest friendRequest) {
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        friendRequestRef.child(friendRequest.getRequestId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa yêu cầu từ danh sách và thông báo cho adapter
                friendRequestList.remove(friendRequest);
                friendRequestAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Yêu cầu đã bị từ chối", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lỗi khi từ chối yêu cầu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}