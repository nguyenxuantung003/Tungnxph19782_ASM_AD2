package com.example.nguyenxuantung_ph19782.Activity;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView friendStatusTextView;
    private Button actionButton;
    private String currentUserId;
    private String userId;
    private boolean isFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        friendStatusTextView = findViewById(R.id.friendStatusTextView);
        actionButton = findViewById(R.id.actionButton);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");

        // Hiển thị thông tin người dùng
        usernameTextView.setText(username);
        // Load profile image nếu có từ Firebase Storage hoặc từ URL
        loadProfileImage(userId);

        // Kiểm tra trạng thái kết bạn
        checkFriendStatus();

        // Xử lý nút hành động
        actionButton.setOnClickListener(v -> {
            if (isFriend) {
                // Chuyển đến màn hình nhắn tin
                Intent chatIntent = new Intent(UserProfileActivity.this, ChatActivity.class);
                chatIntent.putExtra("userId", userId);
                chatIntent.putExtra("username", username);
                startActivity(chatIntent);
            } else {
                // Gửi lời mời kết bạn
                sendFriendRequest();
            }
        });
    }

    private void loadProfileImage(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImageUrl = dataSnapshot.child("profileUrl").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(UserProfileActivity.this).load(profileImageUrl).into(profileImageView);
                    } else {
                        profileImageView.setImageResource(R.drawable.icon_profile); // Đặt ảnh mặc định nếu không có ảnh
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Lỗi khi tải ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFriendStatus() {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(currentUserId);
        friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    isFriend = true;
                    friendStatusTextView.setText("Bạn bè");
                    actionButton.setText("Nhắn tin");
                } else {
                    isFriend = false;
                    friendStatusTextView.setText("Chưa kết bạn");
                    actionButton.setText("Gửi lời mời kết bạn");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Lỗi khi kiểm tra trạng thái kết bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFriendRequest() {
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friendRequests").child(userId).child(currentUserId);
        friendRequestRef.setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfileActivity.this, "Đã gửi lời mời kết bạn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Lỗi khi gửi lời mời kết bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }
}