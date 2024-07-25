package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Group;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<Group> {
    private List<Group> groupList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onJoinClick(Group group);
        void onViewClick(Group group);
    }

    public GroupAdapter(@NonNull Context context, List<Group> groupList, OnItemClickListener listener) {
        super(context, 0, groupList);
        this.groupList = groupList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_group, parent, false);
        }

        TextView groupNameTextView = convertView.findViewById(R.id.groupNameTextView);
        Button actionButton = convertView.findViewById(R.id.actionButton);

        Group group = groupList.get(position);
        groupNameTextView.setText(group.getGroupName());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (group.getMembers() != null && group.getMembers().containsKey(userId)) {
            actionButton.setText("View");
            actionButton.setOnClickListener(v -> listener.onViewClick(group));
        } else {
            actionButton.setText("Join");
            actionButton.setOnClickListener(v -> listener.onJoinClick(group));
        }

        return convertView;
    }
}
