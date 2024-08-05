package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.example.nguyenxuantung_ph19782.model.Commet; // Sửa tên thành Comment nếu cần
import com.example.nguyenxuantung_ph19782.model.Post;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivityUserProfile extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private TextView weightTextView;
    private TextView heightTextView;
    private TextView bmiTextView;
    private TextView ageTextView;
    private Button editButton,logoutbtn;
    private DatabaseReference userDatabaseRef;
    private PostAdapter postAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_profile);

        // Khởi tạo các view
        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        genderTextView = findViewById(R.id.genderTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        ageTextView = findViewById(R.id.ageTextview);
        editButton = findViewById(R.id.editButton);
        logoutbtn = findViewById(R.id.logout_button);
        logoutbtn.setOnClickListener(v -> logout());

        userDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Lấy dữ liệu người dùng từ Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy ID người dùng hiện tại
        getUserData(userId);

        // Cài đặt sự kiện cho nút chỉnh sửa
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityUserProfile.this, ProfileActivity.class);
            // Truyền ID người dùng cho ProfileActivity
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void getUserData(String userId) {
        userDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userprofileUrl = dataSnapshot.child("profileUrl").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    Integer age = dataSnapshot.child("profile").child("age").getValue(Integer.class);
                    String gender = dataSnapshot.child("profile").child("gender").getValue(String.class);
                    Integer height = dataSnapshot.child("profile").child("height").getValue(Integer.class);
                    Integer weight = dataSnapshot.child("profile").child("weight").getValue(Integer.class);
                    Float bmi = dataSnapshot.child("profile").child("bmi").getValue(Float.class);

                    // Cập nhật dữ liệu vào các view
                    usernameTextView.setText("Tên : " + (username != null ? username : "N/A"));
                    emailTextView.setText("Email : " + (email != null ? email : "N/A"));
                    ageTextView.setText("Tuổi : " + (age != null ? age : "N/A"));
                    genderTextView.setText("Giới tính : " + (gender != null ? gender : "N/A"));
                    heightTextView.setText("Chiều cao : " + (height != null ? height : "N/A"));
                    weightTextView.setText("Cân nặng : " + (weight != null ? weight : "N/A"));
                    bmiTextView.setText("BMI : " + (bmi != null ? bmi : "N/A"));

                    // Tải ảnh đại diện sử dụng Glide
                    Glide.with(MainActivityUserProfile.this)
                            .load(userprofileUrl)
                            .placeholder(R.drawable.icon_profile) // Thay thế bằng hình ảnh placeholder của bạn
                            .into(profileImageView);

                    // Lấy và hiển thị các bài đăng của người dùng
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
                            postAdapter = new PostAdapter(MainActivityUserProfile.this, postList, MainActivityUserProfile.this::showPostDetailsDialog);
                            ListView postsListView = findViewById(R.id.postsListViewmainuser);
                            postsListView.setAdapter(postAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivityUserProfile.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityUserProfile.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
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
                CommentAdapter adapter = new CommentAdapter(MainActivityUserProfile.this, comments);
                commentsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivityUserProfile.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivityUserProfile.this, "Comment added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivityUserProfile.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logout() {
        // Tạo và hiển thị hộp thoại xác nhận
        new android.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Thực hiện đăng xuất khi nhấn "Có"
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivityUserProfile.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc Activity hiện tại
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng hộp thoại khi nhấn "Không"
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
