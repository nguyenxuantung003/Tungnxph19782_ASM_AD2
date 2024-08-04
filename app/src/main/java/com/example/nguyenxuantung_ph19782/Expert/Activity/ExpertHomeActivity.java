package com.example.nguyenxuantung_ph19782.Expert.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nguyenxuantung_ph19782.Expert.Fragment.ConsultationRequestsFragment;
import com.example.nguyenxuantung_ph19782.Expert.Fragment.ChatWithUsersFragment;
import com.example.nguyenxuantung_ph19782.Expert.Fragment.GroupsFragment;
import com.example.nguyenxuantung_ph19782.Expert.Fragment.StatisticsFragment;
import com.example.nguyenxuantung_ph19782.R;
import com.google.android.material.navigation.NavigationView;

public class ExpertHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView profileImageView = headerView.findViewById(R.id.nav_header_imgProfile);
        TextView nameTextView = headerView.findViewById(R.id.nav_header_txtName);

        // Load user info from Firebase or other source
        // profileImageView.setImageURI(userProfileImageUri);
        // nameTextView.setText(userName);

        // Set the default fragment
        if (savedInstanceState == null) {
            loadFragment(new ConsultationRequestsFragment());
            navigationView.setCheckedItem(R.id.nav_yeucautuvan);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_yeucautuvan) {
            selectedFragment = new ConsultationRequestsFragment();
        } else if (id == R.id.nav_chatvoinguoidung) {
            selectedFragment = new ChatWithUsersFragment();
        } else if (id == R.id.nav_nhom) {
            selectedFragment = new GroupsFragment();
        } else if (id == R.id.nav_thongke) {
            selectedFragment = new StatisticsFragment();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frg_container, fragment);
        fragmentTransaction.commit();
    }
}