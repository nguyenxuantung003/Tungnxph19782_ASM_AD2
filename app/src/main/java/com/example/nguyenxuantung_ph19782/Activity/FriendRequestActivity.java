package com.example.nguyenxuantung_ph19782.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.FriendRequestAdapter;
import com.example.nguyenxuantung_ph19782.model.FriendRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestActivity extends AppCompatActivity {


    private RecyclerView friendRequestRecyclerView;
    private FriendRequestAdapter friendRequestAdapter;
    private List<FriendRequest> friendRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);


        friendRequestRecyclerView = findViewById(R.id.friendRequestRecyclerView);

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

        friendRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendRequestRecyclerView.setAdapter(friendRequestAdapter);

        loadFriendRequests();

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
                        request.setRequestId(snapshot.getKey()); // Đặt requestId từ Firebase key

                        // Lấy thông tin người gửi yêu cầu
                        String fromUserId = request.getFrom();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(fromUserId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userSnapshot) {
                                FriendRequest user = userSnapshot.getValue(FriendRequest.class);
                                if (user != null) {
                                    request.setUserName(user.getFrom()); // Giả sử bạn đã thêm trường username vào FriendRequest
                                    friendRequestList.add(request);
                                    friendRequestAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi nếu có
                                Toast.makeText(FriendRequestActivity.this, "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                if (friendRequestList.isEmpty()) {
                    // Hiển thị thông báo khi không có lời mời kết bạn
                    Toast.makeText(FriendRequestActivity.this, "Bạn không có lời mời kết bạn nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(FriendRequestActivity.this, "Lỗi khi tải yêu cầu kết bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void acceptFriendRequest(FriendRequest friendRequest) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends");

        // Kiểm tra requestId trước khi thực hiện các thao tác
        String requestId = friendRequest.getRequestId();
        if (requestId == null) {
            Toast.makeText(FriendRequestActivity.this, "ID yêu cầu không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm vào danh sách bạn bè
        friendsRef.child(currentUserId).child(friendRequest.getFrom()).setValue(true);
        friendsRef.child(friendRequest.getFrom()).child(currentUserId).setValue(true);

        // Xóa yêu cầu kết bạn
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        friendRequestRef.child(requestId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FriendRequestActivity.this, "Yêu cầu đã được chấp nhận", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FriendRequestActivity.this, "Lỗi khi chấp nhận yêu cầu", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void rejectFriendRequest(FriendRequest friendRequest) {
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        friendRequestRef.child(friendRequest.getRequestId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FriendRequestActivity.this, "Yêu cầu đã bị từ chối", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FriendRequestActivity.this, "Lỗi khi từ chối yêu cầu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}