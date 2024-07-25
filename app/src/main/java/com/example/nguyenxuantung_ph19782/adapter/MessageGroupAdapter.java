package com.example.nguyenxuantung_ph19782.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenxuantung_ph19782.model.Message;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.MessageGroup;

import java.util.List;

public class MessageGroupAdapter extends RecyclerView.Adapter<MessageGroupAdapter.MessageGroupViewHolder> {

    private List<MessageGroup> messageList;

    public MessageGroupAdapter(List<MessageGroup> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageGroupViewHolder holder, int position) {
        MessageGroup message = messageList.get(position);
        holder.messageTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageGroupViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public MessageGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
