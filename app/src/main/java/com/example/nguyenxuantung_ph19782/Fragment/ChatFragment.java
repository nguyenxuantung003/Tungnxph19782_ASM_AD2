package com.example.nguyenxuantung_ph19782.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.MessageAdapter;
import com.example.nguyenxuantung_ph19782.adapter.MessageGroupAdapter;
import com.example.nguyenxuantung_ph19782.model.Message;
import com.example.nguyenxuantung_ph19782.model.MessageGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageGroupAdapter messageAdapter;
    private List<MessageGroup> messageList;
    private String groupId;
    private DatabaseReference messagesRef;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageGroupAdapter(messageList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecyclerView.setAdapter(messageAdapter);

        groupId = getArguments().getString("groupId"); // Nhận groupId từ Bundle
        messagesRef = FirebaseDatabase.getInstance().getReference("groups").child(groupId).child("messages");

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void loadMessages() {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageGroup message = snapshot.getValue(MessageGroup.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        String messageId = messagesRef.push().getKey();
        MessageGroup message = new MessageGroup(FirebaseAuth.getInstance().getCurrentUser().getUid(), messageText);
        messagesRef.child(messageId).setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageEditText.setText("");
            } else {
                Toast.makeText(getContext(), "Error sending message", Toast.LENGTH_SHORT).show();
            }
        });
    }
}