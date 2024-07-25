package com.example.nguyenxuantung_ph19782.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nguyenxuantung_ph19782.Fragment.ChatFragment;
import com.example.nguyenxuantung_ph19782.Fragment.GoalsFragment;

public class GroupPagerAdapter extends FragmentStateAdapter {
    private final String groupId;

    public GroupPagerAdapter(FragmentActivity fa, String groupId) {
        super(fa);
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        args.putString("groupId", groupId);

        switch (position) {
            case 0:
                fragment = new ChatFragment();
                fragment.setArguments(args);
                break;
            case 1:
                // Thay đổi thành GoalsFragment nếu bạn có một Fragment khác cho mục tiêu
                fragment = new GoalsFragment();
                fragment.setArguments(args);
                break;
            default:
                fragment = new ChatFragment();
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
