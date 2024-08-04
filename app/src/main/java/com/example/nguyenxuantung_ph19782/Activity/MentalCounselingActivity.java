package com.example.nguyenxuantung_ph19782.Activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.CounselingRequestsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.viewpager2.widget.ViewPager2;

public class MentalCounselingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_counseling);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        CounselingRequestsPagerAdapter adapter = new CounselingRequestsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Gửi yêu cầu");
                    break;
                case 1:
                    tab.setText("Yêu cầu đã gửi");
                    break;
                case 2:
                    tab.setText("Yêu cầu đã trả lời");
                    break;
            }
        }).attach();
    }
}