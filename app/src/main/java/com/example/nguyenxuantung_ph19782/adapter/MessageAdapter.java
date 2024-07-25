package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nguyenxuantung_ph19782.R;

import com.example.nguyenxuantung_ph19782.model.Message;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.item_message, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        }

        Message message = messages.get(position);

        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        messageTextView.setText(message.getMessage());

        return convertView;
    }
}