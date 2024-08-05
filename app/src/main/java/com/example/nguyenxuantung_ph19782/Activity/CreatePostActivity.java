package com.example.nguyenxuantung_ph19782.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private EditText contentEditText;
    private ImageView postImageView; // Optional: for displaying selected image
    private Button postButton;
    private Uri imageUri; // The URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        contentEditText = findViewById(R.id.contentEditText);
        postImageView = findViewById(R.id.postImageView); // Optional: for displaying selected image
        postButton = findViewById(R.id.postButton);

        // Xử lý sự kiện nút đăng bài viết
        postButton.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String content = contentEditText.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "Nội dung bài viết không thể để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        String postId = UUID.randomUUID().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        long createdAt = System.currentTimeMillis();

        // Giả sử bạn có cách để lấy URL ảnh nếu có
        if (imageUri != null) {
            // Nếu có ảnh, tải ảnh lên và lưu bài viết sau khi có URL của ảnh
            uploadImageAndCreatePost(postId, userId, username, content, createdAt);
        } else {
            // Nếu không có ảnh, tạo bài viết ngay lập tức
            createPostWithoutImage(postId, userId, username, content, createdAt, null);
        }
    }

    private void uploadImageAndCreatePost(String postId, String userId, String username, String content, long createdAt) {
        // Đặt tên cho ảnh
        String fileName = UUID.randomUUID().toString();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Tạo bài viết với URL của ảnh
                createPostWithoutImage(postId, userId, username, content, createdAt, imageUrl);
            }).addOnFailureListener(e -> {
                Log.e("ImageUpload", "Failed to get image URL", e);
                Toast.makeText(CreatePostActivity.this, "Lỗi khi lấy URL ảnh", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Log.e("ImageUpload", "Failed to upload image", e);
            Toast.makeText(CreatePostActivity.this, "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
        });
    }

    private void createPostWithoutImage(String postId, String userId, String username, String content, long createdAt, String imageUrl) {
        Post post = new Post(postId, userId, username, content, imageUrl, createdAt);

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        postsRef.setValue(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreatePostActivity.this, "Đăng bài viết thành công", Toast.LENGTH_SHORT).show();
                finish(); // Trở về màn hình bảng tin
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("CreatePost", "Error: " + error);
                Toast.makeText(CreatePostActivity.this, "Lỗi khi đăng bài viết: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to set the image URI (this should be called when user selects an image)
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        postImageView.setImageURI(imageUri); // Optional: to display the selected image
    }
}