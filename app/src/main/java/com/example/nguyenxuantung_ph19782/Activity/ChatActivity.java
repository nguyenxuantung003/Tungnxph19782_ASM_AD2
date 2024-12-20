package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.MessageAdapter;
import com.example.nguyenxuantung_ph19782.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messagesListView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private String chatId;
    private String currentUserId;
    private String otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messagesListView = findViewById(R.id.messagesListView);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter( messageList);
        messagesListView.setLayoutManager(new LinearLayoutManager(this));
        messagesListView.setAdapter(messageAdapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUserId = getIntent().getStringExtra("userId");

        chatId = getChatId(currentUserId, otherUserId);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }



    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String senderName = snapshot.child("username").getValue(String.class);

                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    Message message = new Message(currentUserId, otherUserId, messageText, timestamp, senderName);

                    DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatId);
                    String messageId = messagesRef.push().getKey();
                    messagesRef.child(messageId).setValue(message).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            messageEditText.setText("");
                        } else {
                            Toast.makeText(ChatActivity.this, "Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChatActivity.this, "Lỗi khi lấy tên người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Lỗi khi lấy tên người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatId);
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                Collections.sort(messageList, new Comparator<Message>() {
                    @Override
                    public int compare(Message m1, Message m2) {
                        return m1.getTimestamp().compareTo(m2.getTimestamp());
                    }
                });
                messageAdapter.notifyDataSetChanged();
                messagesListView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Lỗi khi tải tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getChatId(String userId1, String userId2) {
        return userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }
}