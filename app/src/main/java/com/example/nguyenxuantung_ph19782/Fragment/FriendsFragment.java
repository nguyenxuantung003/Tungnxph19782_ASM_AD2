package com.example.nguyenxuantung_ph19782.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.Activity.ChatActivity;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.FriendAdapter;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private ListView friendListView;
    private FriendAdapter friendAdapter;
    private List<Users> friendList;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        friendListView = view.findViewById(R.id.friendListView);

        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(getContext(), friendList, user -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("userId", user.getUserId());
            intent.putExtra("username", user.getUsername());
            startActivity(intent);
        });

        friendListView.setAdapter(friendAdapter);

        loadFriends();

        return view;
    }

    private void loadFriends() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(currentUserId);

        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String friendId = snapshot.getKey();
                    loadUserInfo(friendId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi tải danh sách bạn bè", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserInfo(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user != null) {
                    friendList.add(user);
                    friendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}