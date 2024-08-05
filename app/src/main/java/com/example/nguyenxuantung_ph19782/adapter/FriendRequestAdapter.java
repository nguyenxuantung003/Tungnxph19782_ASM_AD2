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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private List<FriendRequest> friendRequestList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAcceptClick(FriendRequest friendRequest);
        void onRejectClick(FriendRequest friendRequest);
    }

    public FriendRequestAdapter(List<FriendRequest> friendRequestList, OnItemClickListener listener) {
        this.friendRequestList = friendRequestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest friendRequest = friendRequestList.get(position);
        holder.userNameTextView.setText(friendRequest.getUserName()); // Assuming you have a TextView named userNameTextView in item_friend_request.xml
        holder.acceptButton.setOnClickListener(v -> listener.onAcceptClick(friendRequest));
        holder.rejectButton.setOnClickListener(v -> listener.onRejectClick(friendRequest));
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public Button acceptButton;
        public Button rejectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.usernamefriendrequest); // Assuming you have a TextView with this ID in item_friend_request.xml
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}

