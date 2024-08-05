package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.nguyenxuantung_ph19782.R;
import androidx.annotation.Nullable;

import com.example.nguyenxuantung_ph19782.model.Commet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends ArrayAdapter<Commet> {

    private final Context context;
    private final List<Commet> comments;

    public CommentAdapter(Context context, List<Commet> comments) {
        super(context, 0, comments);
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_commets, parent, false);
        }

        Commet comment = comments.get(position);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView contentTextView = convertView.findViewById(R.id.contentTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);

        usernameTextView.setText(comment.getUsername());
        contentTextView.setText(comment.getContent());
        dateTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(comment.getCreatedAt())));

        return convertView;
    }
}
