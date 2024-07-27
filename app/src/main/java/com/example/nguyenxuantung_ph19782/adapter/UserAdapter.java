package com.example.nguyenxuantung_ph19782.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.model.Users;
import com.example.nguyenxuantung_ph19782.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Users> users;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onSendRequestClick(Users user);
        void onAcceptRequestClick(Users user);
        void onMessageClick(Users user);
    }

    public UserAdapter(List<Users> users, OnItemClickListener onItemClickListener) {
        this.users = users;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = users.get(position);
        holder.userNameTextView.setText(user.getUsername());

        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends");
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("friend_requests");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Kiểm tra xem có phải là bạn bè không
        friendsRef.child(currentUserId).child(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.sendRequestButton.setVisibility(View.GONE);
                    holder.acceptRequestButton.setVisibility(View.GONE);
                    holder.messageButton.setVisibility(View.VISIBLE);
                } else {
                    // Kiểm tra xem đã gửi yêu cầu kết bạn chưa
                    friendRequestRef.orderByChild("from").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean requestSent = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.child("to").getValue(String.class).equals(user.getUserId())) {
                                    requestSent = true;
                                    break;
                                }
                            }
                            if (requestSent) {
                                holder.sendRequestButton.setVisibility(View.GONE);
                                holder.acceptRequestButton.setVisibility(View.VISIBLE);
                                holder.messageButton.setVisibility(View.GONE);
                            } else {
                                holder.sendRequestButton.setVisibility(View.VISIBLE);
                                holder.acceptRequestButton.setVisibility(View.GONE);
                                holder.messageButton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });

        holder.sendRequestButton.setOnClickListener(v -> onItemClickListener.onSendRequestClick(user));
        holder.acceptRequestButton.setOnClickListener(v -> onItemClickListener.onAcceptRequestClick(user));
        holder.messageButton.setOnClickListener(v -> onItemClickListener.onMessageClick(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        Button sendRequestButton;
        Button acceptRequestButton;
        Button messageButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            sendRequestButton = itemView.findViewById(R.id.sendRequestButton);
            acceptRequestButton = itemView.findViewById(R.id.acceptRequestButton);
            messageButton = itemView.findViewById(R.id.messageButton);
        }
    }
}
