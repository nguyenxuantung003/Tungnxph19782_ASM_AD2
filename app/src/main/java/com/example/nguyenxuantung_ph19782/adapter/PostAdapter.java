package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private final List<Post> posts;
    private final OnPostClickListener onPostClickListener;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public PostAdapter(Context context, List<Post> posts, OnPostClickListener onPostClickListener) {
        super(context, R.layout.item_post, posts);
        this.context = context;
        this.posts = posts;
        this.onPostClickListener = onPostClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        }

        Post post = posts.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView contentTextView = convertView.findViewById(R.id.contentTextView);
        ImageView postImageView = convertView.findViewById(R.id.postImageView);
        TextView likesTextView = convertView.findViewById(R.id.likesTextView);
        TextView commentsTextView = convertView.findViewById(R.id.commentsTextView);
        Button likeButton = convertView.findViewById(R.id.likeButton);
        Button commentButton = convertView.findViewById(R.id.commentButton);

        usernameTextView.setText(post.getUsername());
        contentTextView.setText(post.getContent());

        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Glide.with(context).load(post.getImageUrl()).into(postImageView);
            postImageView.setVisibility(View.VISIBLE);
        } else {
            postImageView.setVisibility(View.GONE);
        }

        likesTextView.setText(post.getLikes().size() + " Likes");
        commentsTextView.setText(post.getComments().size() + " Comments");

        // Xử lý sự kiện like
        likeButton.setOnClickListener(v -> {
            DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostId()).child("likes");
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            likesRef.child(currentUserId).setValue(true);
        });

        // Xử lý sự kiện bình luận
        commentButton.setOnClickListener(v -> {
            // Mở một activity hoặc dialog để thêm bình luận
            // Implement code to open CommentActivity or dialog
        });

        // Xử lý sự kiện nhấn vào bài viết
        convertView.setOnClickListener(v -> onPostClickListener.onPostClick(post));

        return convertView;
    }
}