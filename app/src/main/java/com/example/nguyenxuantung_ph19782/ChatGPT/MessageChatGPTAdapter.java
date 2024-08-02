package com.example.nguyenxuantung_ph19782.ChatGPT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageChatGPTAdapter extends RecyclerView.Adapter<MessageChatGPTAdapter.MyViewHolder>{
    List<messageChatGPT> messageChatGPTList;

    public MessageChatGPTAdapter(List<messageChatGPT> messageChatGPTList) {
        this.messageChatGPTList = messageChatGPTList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatgpt_items,null);
       MyViewHolder myViewHolder = new MyViewHolder(chatView);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        messageChatGPT messageChatGPT = messageChatGPTList.get(position);
        if (messageChatGPT.getSentby().equals(messageChatGPT.SENT_BY_ME)){
            holder.leftchat.setVisibility(View.GONE);
            holder.rightchat.setVisibility(View.VISIBLE);
            holder.righttextview.setText(messageChatGPT.getMessage());
        } else {
            holder.rightchat.setVisibility(View.GONE);
            holder.leftchat.setVisibility(View.VISIBLE);
            holder.lefttextview.setText(messageChatGPT.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageChatGPTList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftchat,rightchat;
        TextView lefttextview,righttextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftchat = itemView.findViewById(R.id.left_chatgpt_tv);
            rightchat = itemView.findViewById(R.id.right_chatgpt_tv);
            lefttextview = itemView.findViewById(R.id.left_chatgpt_textview);
            righttextview = itemView.findViewById(R.id.right_chatgpt_textview);
        }
    }

}
