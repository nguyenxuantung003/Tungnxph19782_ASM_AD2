package com.example.nguyenxuantung_ph19782.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.model.FriendRequest;
import com.example.nguyenxuantung_ph19782.R;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<FriendRequest> friendRequestList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onAcceptClick(FriendRequest request);
        void onRejectClick(FriendRequest request);
    }

    public FriendRequestAdapter(List<FriendRequest> friendRequestList, OnItemClickListener onItemClickListener) {
        this.friendRequestList = friendRequestList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest request = friendRequestList.get(position);
        holder.usernameTextView.setText(request.getFrom()); // Hiển thị tên người gửi yêu cầu
        holder.acceptButton.setOnClickListener(v -> onItemClickListener.onAcceptClick(request));
        holder.rejectButton.setOnClickListener(v -> onItemClickListener.onRejectClick(request));
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button acceptButton;
        Button rejectButton;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
