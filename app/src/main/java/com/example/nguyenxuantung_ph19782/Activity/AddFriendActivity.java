package com.example.nguyenxuantung_ph19782.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.UserAdapter;
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

public class AddFriendActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private UserAdapter userAdapter;
    private List<Users> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, new UserAdapter.OnItemClickListener() {
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
                Intent intent = new Intent(AddFriendActivity.this, ChatActivity.class);
                intent.putExtra("userId", user.getUserId());
                startActivity(intent);
            }

        });

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(userAdapter);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        //searchButton.setOnClickListener(v -> searchUsers());
    }

    private void searchUsers(String searchText) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = usersRef.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void sendFriendRequest(String toUserId) {
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        String requestId = friendRequestRef.push().getKey();  // Generate a new unique request ID
        String fromUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FriendRequest friendRequest = new FriendRequest(requestId, fromUserId, toUserId, "pending");
        friendRequestRef.child(requestId).setValue(friendRequest);
    }


    private void acceptFriendRequest(String fromUserId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends");

        friendsRef.child(currentUserId).child(fromUserId).setValue(true);
        friendsRef.child(fromUserId).child(currentUserId).setValue(true);

        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        Query query = friendRequestRef.orderByChild("from").equalTo(fromUserId).orderByChild("to").equalTo(currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}