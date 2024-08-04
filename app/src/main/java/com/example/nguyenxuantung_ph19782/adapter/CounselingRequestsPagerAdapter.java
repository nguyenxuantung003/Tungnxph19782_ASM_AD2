package com.example.nguyenxuantung_ph19782.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nguyenxuantung_ph19782.Fragment.AnsweredRequestsFragment;
import com.example.nguyenxuantung_ph19782.Fragment.SendRequestFragment;
import com.example.nguyenxuantung_ph19782.Fragment.SentRequestsFragment;

public class CounselingRequestsPagerAdapter extends FragmentStateAdapter {

    public CounselingRequestsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SendRequestFragment();
            case 1:
                return new SentRequestsFragment();
            case 2:
                return new AnsweredRequestsFragment();
            default:
                return new SendRequestFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab
    }
}
