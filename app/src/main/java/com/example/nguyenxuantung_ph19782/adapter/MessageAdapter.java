package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.R;

import com.example.nguyenxuantung_ph19782.model.Message;
import com.example.nguyenxuantung_ph19782.model.MessageGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
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
            return new MessageAdapter.MyMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new MessageAdapter.OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MY_MESSAGE:
                ((MessageAdapter.MyMessageViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_OTHER_MESSAGE:
                ((MessageAdapter.OtherMessageViewHolder) holder).bind(message);
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

        void bind(Message message) {
            messageTextView.setText(message.getMessage());
        }
    }

    static class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,messageTextViewname;

        OtherMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageTextViewname = itemView.findViewById(R.id.messageTextViewname);
        }

        void bind(Message message) {
            messageTextView.setText(message.getMessage());
            messageTextViewname.setText(message.getSenderName());
        }
    }
}
