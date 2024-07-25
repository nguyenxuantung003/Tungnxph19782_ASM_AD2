package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nguyenxuantung_ph19782.model.Users;

import java.util.List;
import com.example.nguyenxuantung_ph19782.R;

public class FriendAdapter extends ArrayAdapter<Users> {
    private final Context context;
    private final List<Users> friends;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onMessageClick(Users user);
    }

    public FriendAdapter(Context context, List<Users> friends, OnItemClickListener listener) {
        super(context, R.layout.item_friend, friends);
        this.context = context;
        this.friends = friends;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        }

        Users user = friends.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        Button messageButton = convertView.findViewById(R.id.messageButton);

        usernameTextView.setText(user.getUsername());
        messageButton.setOnClickListener(v -> listener.onMessageClick(user));

        return convertView;
    }
}