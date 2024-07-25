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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Users> userList;
    private OnItemClickListener listener;
    private Set<String> sentRequests;

    public interface OnItemClickListener {
        void onSendRequestClick(Users user);
        void onAcceptRequestClick(Users user);
    }

    public UserAdapter(List<Users> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
        this.sentRequests = new HashSet<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());

        if (sentRequests.contains(user.getUserId())) {
            holder.sendRequestButton.setVisibility(View.GONE);
            holder.requestSentTextView.setVisibility(View.VISIBLE);
        } else {
            holder.sendRequestButton.setVisibility(View.VISIBLE);
            holder.requestSentTextView.setVisibility(View.GONE);
            holder.sendRequestButton.setOnClickListener(v -> {
                listener.onSendRequestClick(user);
                sentRequests.add(user.getUserId());
                notifyItemChanged(position);
            });
        }

        // Thêm logic để xác nhận yêu cầu kết bạn
        holder.acceptRequestButton.setVisibility(View.VISIBLE);
        holder.acceptRequestButton.setOnClickListener(v -> {
            listener.onAcceptRequestClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button sendRequestButton;
        TextView requestSentTextView;
        Button acceptRequestButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            sendRequestButton = itemView.findViewById(R.id.sendRequestButton);
            requestSentTextView = itemView.findViewById(R.id.requestSentTextView);
            acceptRequestButton = itemView.findViewById(R.id.acceptRequestButton);
        }
    }
}
