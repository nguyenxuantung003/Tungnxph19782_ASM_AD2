package com.example.nguyenxuantung_ph19782.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.CommentAdapter;
import com.example.nguyenxuantung_ph19782.adapter.PostAdapter;
import com.example.nguyenxuantung_ph19782.model.Commet;
import com.example.nguyenxuantung_ph19782.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    private ListView postsListView;
    private Button createPostButton;
    private PostAdapter postAdapter;
    private List<Post> posts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        postsListView = findViewById(R.id.postsListView);
        createPostButton = findViewById(R.id.createPostButton);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, posts,this::showPostDetailsDialog);
        postsListView.setAdapter(postAdapter);

        // Lấy danh sách bài viết từ Firebase
        loadPosts();

        // Xử lý sự kiện nút tạo bài viết
        createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });
        postsListView.setOnItemClickListener((parent, view, position, id) -> {
            Post post = posts.get(position);
            showPostDetailsDialog(post);
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

        // Load and display comments
        loadComments(post.getPostId(), commentsListView);

        commentButton.setOnClickListener(v -> {
            String content = commentEditText.getText().toString().trim();
            if (!content.isEmpty()) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); // Assuming this method returns the username

                addComment(post.getPostId(), userId, username, content);
                commentEditText.setText("");
                // Refresh comments list
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
                CommentAdapter adapter = new CommentAdapter(NewsFeedActivity.this, comments);
                commentsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NewsFeedActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewsFeedActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewsFeedActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        postsRef.orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                // Sắp xếp các bài viết từ mới nhất đến cũ nhất
                Collections.sort(posts, (p1, p2) -> Long.compare(p2.getCreatedAt(), p1.getCreatedAt()));
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NewsFeedActivity.this, "Lỗi khi tải bài viết", Toast.LENGTH_SHORT).show();
            }
        });
    }
}