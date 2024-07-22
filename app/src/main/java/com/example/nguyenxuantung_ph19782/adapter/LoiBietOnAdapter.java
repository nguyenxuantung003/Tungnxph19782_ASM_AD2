package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nguyenxuantung_ph19782.model.Loibieton;
import com.example.nguyenxuantung_ph19782.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoiBietOnAdapter extends ArrayAdapter<Loibieton> {
    private Context context;
    private ArrayList<Loibieton> loibietons;
    private OnItemActionListener listener;


    public LoiBietOnAdapter(@NonNull Context context, ArrayList<Loibieton> loibietons,OnItemActionListener onItemActionListener) {
        super(context,0,loibietons);
        this.context = context;
        this.loibietons = loibietons;
        this.listener = onItemActionListener;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items_loibieton, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvGratitudeNote = convertView.findViewById(R.id.tvGratitudeNote);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.btnUpdate = convertView.findViewById(R.id.btnUpdate);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Loibieton mentalActivity = loibietons.get(position);
        viewHolder.tvGratitudeNote.setText(mentalActivity.getGratitudeNotes());

        // Format the timestamp to a readable date
        String dateString = mentalActivity.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            viewHolder.tvDate.setText(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.tvDate.setText(dateString);
        }
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUpdateClicked(mentalActivity);
                }
            }
        });

        // Set click listener for delete button
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClicked(mentalActivity);
                }
            }
        });

        return convertView;
    }
    private static class ViewHolder {
        TextView tvGratitudeNote;
        TextView tvDate;
        Button btnDelete;
        Button btnUpdate;
    }
    public interface OnItemActionListener {
        void onUpdateClicked(Loibieton item);

        void onDeleteClicked(Loibieton item);
    }


}
