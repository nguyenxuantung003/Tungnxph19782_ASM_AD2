package com.example.nguyenxuantung_ph19782.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nguyenxuantung_ph19782.Fragment.FriendRequestsFragment;
import com.example.nguyenxuantung_ph19782.Fragment.FriendsFragment;
import com.example.nguyenxuantung_ph19782.Fragment.SearchUsersFragment;

public class FragmentFriendAdapter extends FragmentStateAdapter {

    public FragmentFriendAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FriendsFragment();
            case 1:
                return new FriendRequestsFragment();
            case 2:
                return new SearchUsersFragment();
            default:
                return new FriendsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab
    }
}