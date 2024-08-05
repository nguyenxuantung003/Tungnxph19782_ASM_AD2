package com.example.nguyenxuantung_ph19782.Fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.UserAdapter;
import com.example.nguyenxuantung_ph19782.model.FriendRequest;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> usersList = new ArrayList<>();
    private List<Users> filteredUsersList = new ArrayList<>();
    private Map<String, Boolean> friendsMap = new HashMap<>();
    private Map<String, Boolean> requestSentMap = new HashMap<>();
    private DatabaseReference friendsRef, friendRequestRef, usersRef;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        recyclerView = view.findViewById(R.id.searchResultsRecyclerViewfg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText searchEditText = view.findViewById(R.id.searchEditTextfg);
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
            }
        });

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendsRef = FirebaseDatabase.getInstance().getReference("friends");
        friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        loadUsers();
        setupUserAdapter();

        return view;
    }

    private void loadUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null && !user.getUserId().equals(currentUserId)) {
                        usersList.add(user);
                    }
                }
                filteredUsersList.addAll(usersList);
                userAdapter.notifyDataSetChanged();
                loadFriendStatus();

                // Kiểm tra dữ liệu đã tải
                Log.d("SearchUsersFragment", "Loaded users: " + usersList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void setupUserAdapter() {
        userAdapter = new UserAdapter(filteredUsersList, new UserAdapter.OnItemClickListener() {
            @Override
            public void onSendRequestClick(Users user) {
                sendFriendRequest(user.getUserId());
            }

            @Override
            public void onAcceptRequestClick(Users user) {
                acceptFriendRequest(user.getUserId());
            }

            @Override
            public void onMessageClick(Users user) {
                // Xử lý gửi tin nhắn
            }
        }, friendsMap, requestSentMap);
        recyclerView.setAdapter(userAdapter);
    }

    private void loadFriendStatus() {
        // Tải trạng thái bạn bè
        friendsRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsMap.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    friendsMap.put(dataSnapshot.getKey(), true);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });

        // Tải trạng thái yêu cầu đã gửi
        friendRequestRef.orderByChild("from").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestSentMap.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String toUserId = dataSnapshot.child("to").getValue(String.class);
                    if (toUserId != null) {
                        requestSentMap.put(toUserId, true);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void sendFriendRequest(String toUserId) {
        String requestId = friendRequestRef.push().getKey();
        if (requestId != null) {
            FriendRequest friendRequest = new FriendRequest(requestId, currentUserId, toUserId, "pending");
            friendRequestRef.child(requestId).setValue(friendRequest).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    requestSentMap.put(toUserId, true);
                    userAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Friend request sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to send friend request", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void acceptFriendRequest(String fromUserId) {
        friendsRef.child(currentUserId).child(fromUserId).setValue(true);
        friendsRef.child(fromUserId).child(currentUserId).setValue(true);

        friendRequestRef.orderByChild("from").equalTo(fromUserId).orderByChild("to").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue();
                        }

                        friendsMap.put(fromUserId, true);
                        requestSentMap.remove(fromUserId);
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Friend request accepted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu có
                    }
                });
    }

    private void filterUsers(String query) {
        filteredUsersList.clear(); // Xóa danh sách đã lọc
        if (query.isEmpty()) {
            filteredUsersList.addAll(usersList); // Hiển thị tất cả người dùng nếu truy vấn trống
        } else {
            for (Users user : usersList) {
                String username = user.getUsername();
                if (username != null && username.toLowerCase().contains(query.toLowerCase())) {
                    filteredUsersList.add(user); // Thêm người dùng vào danh sách đã lọc nếu tên chứa truy vấn
                }
            }
        }
        userAdapter.notifyDataSetChanged(); // Cập nhật adapter để phản ánh sự thay đổi
    }
}