package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nguyenxuantung_ph19782.R;

import com.example.nguyenxuantung_ph19782.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if (convertView == null) {
            if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_sent, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_received, parent, false);
            }
        }

        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        messageTextView.setText(message.getMessage());

        return convertView;
    }
}
