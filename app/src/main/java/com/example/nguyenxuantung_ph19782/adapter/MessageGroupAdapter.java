package com.example.nguyenxuantung_ph19782.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.MessageGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.MessageGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    private List<MessageGroup> messageList;

    public MessageGroupAdapter(List<MessageGroup> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        MessageGroup message = messageList.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (message.getSenderId() != null && message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_MY_MESSAGE;
        } else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageGroup message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MY_MESSAGE:
                ((MyMessageViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_OTHER_MESSAGE:
                ((OtherMessageViewHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,messageTextViewname;

        MyMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageTextViewname = itemView.findViewById(R.id.messageTextViewname);
        }

        void bind(MessageGroup message) {
            messageTextView.setText(message.getMessageText());
        }
    }

    static class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,messageTextViewname;

        OtherMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageTextViewname = itemView.findViewById(R.id.messageTextViewname);
        }

        void bind(MessageGroup message) {

            messageTextView.setText(message.getMessageText());
            messageTextViewname.setText(message.getSenderName());
        }
    }
}