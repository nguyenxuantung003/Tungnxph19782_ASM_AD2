package com.example.nguyenxuantung_ph19782.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.CommentAdapter;
import com.example.nguyenxuantung_ph19782.adapter.PostAdapter;
import com.example.nguyenxuantung_ph19782.model.Commet;
import com.example.nguyenxuantung_ph19782.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView friendStatusTextView;
    private Button actionButton;
    private String currentUserId;
    private String userId;
    private boolean isFriend;
    private ListView postslistView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        friendStatusTextView = findViewById(R.id.friendStatusTextView);
        actionButton = findViewById(R.id.actionButton);
        postslistView = findViewById(R.id.postsListViewView);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(UserProfileActivity.this, postList, UserProfileActivity.this::showPostDetailsDialog);
        postslistView.setAdapter(postAdapter);

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
        loadPosts();

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
    private void loadPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        postsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> postList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }

                // Cài đặt adapter và đính kèm vào ListView
                postAdapter = new PostAdapter(UserProfileActivity.this, postList, UserProfileActivity.this::showPostDetailsDialog);
                ListView postsListView = findViewById(R.id.postsListViewView);
                postsListView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showPostDetailsDialog(Post post) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_post_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView postContent = dialogView.findViewById(R.id.postContent);
        TextView likeCount = dialogView.findViewById(R.id.likeCount);
        ListView commentsListView = dialogView.findViewById(R.id.commentsListView);
        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        Button commentButton = dialogView.findViewById(R.id.commentButton);

        postContent.setText(post.getContent());
        likeCount.setText("Likes: " + post.getLikes().size());

        // Tải và hiển thị các bình luận
        loadComments(post.getPostId(), commentsListView);

        commentButton.setOnClickListener(v -> {
            String content = commentEditText.getText().toString().trim();
            if (!content.isEmpty()) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); // Giả sử phương thức này trả về tên người dùng

                addComment(post.getPostId(), userId, username, content);
                commentEditText.setText("");
                // Làm mới danh sách bình luận
                loadComments(post.getPostId(), commentsListView);
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadComments(String postId, ListView commentsListView) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Commet> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Commet comment = commentSnapshot.getValue(Commet.class);
                    if (comment != null) {
                        comments.add(comment);
                    }
                }

                // Tạo adapter tùy chỉnh để hiển thị Comment
                CommentAdapter adapter = new CommentAdapter(UserProfileActivity.this, comments);
                commentsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComment(String postId, String userId, String username, String content) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
        String commentId = commentsRef.push().getKey();

        long createdAt = System.currentTimeMillis();

        Commet comment = new Commet(commentId, userId, username, content, createdAt);

        commentsRef.child(commentId).setValue(comment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfileActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}