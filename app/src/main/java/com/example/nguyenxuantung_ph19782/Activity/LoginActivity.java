package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.Admin.Activity.AdminHomeActivity;
import com.example.nguyenxuantung_ph19782.Expert.Activity.ExpertHomeActivity;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference mdatabase;
    private EditText edtemail,edtpass;
    private Button btnlogin;
    private TextView tv;
    private ImageView imgbtngg;
    private GoogleSignInClient mgoogleSignInClient;
    private int RC_SIGN_IN = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtemail = findViewById(R.id.login_email);
        edtpass = findViewById(R.id.login_pass);
        btnlogin = findViewById(R.id.btnlogin);
        tv = findViewById(R.id.tvlogintosingup);
        imgbtngg = findViewById(R.id.imagebtngoogle);

        tv.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SingupActivity.class));
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mdatabase = database.getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);
        imgbtngg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndGoogleSignIn();
            }
        });
    }
    private void signOutAndGoogleSignIn() {
        // Sign out Firebase
        FirebaseAuth.getInstance().signOut();

        // Sign out Google
        mgoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                googleSingIn();
            }
        });
    }
    private void googleSingIn() {
        Intent intent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        checkUserProfile(user);
                    } else {
                        // Xử lý khi đăng nhập thất bại
                        Log.w("gg", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//    private void checkUserProfile(FirebaseUser user) {
//        if (user != null) {
//            String userId = user.getUid();
//            mdatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        // Kiểm tra xem profile có đầy đủ thông tin hay không
//                        boolean hasCompleteProfile = snapshot.child("profile").exists()
//                                && snapshot.child("profile").child("gender").exists()
//                                && snapshot.child("profile").child("height").exists()
//                                && snapshot.child("profile").child("weight").exists()
//                                && snapshot.child("profile").child("bmi").exists();
//
//                        if (hasCompleteProfile) {
//                            // Chuyển đến màn hình chính
//                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            // Chuyển đến màn hình cập nhật profile
//                            Intent intent = new Intent(LoginActivity.this, FristUpdateProfileActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } else {
//                        // Trường hợp người dùng mới, lưu thông tin cơ bản và chuyển đến màn hình cập nhật profile
//                        saveUserData(user);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle possible errors.
//                }
//            });
//        }
//    }
private void checkUserProfile(FirebaseUser user) {
    if (user != null) {
        String userId = user.getUid();
        mdatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    boolean hasCompleteProfile = snapshot.child("profile").exists()
                            && snapshot.child("profile").child("gender").exists()
                            && snapshot.child("profile").child("height").exists()
                            && snapshot.child("profile").child("weight").exists()
                            && snapshot.child("profile").child("bmi").exists();

                    Intent intent = null;
                    if (hasCompleteProfile) {
                        if ("user".equalsIgnoreCase(role)) {
                            // Người dùng thông thường
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                        } else if ("expert".equalsIgnoreCase(role)) {
                            // Chuyên gia
                            intent = new Intent(LoginActivity.this, ExpertHomeActivity.class);
                        } else if ("admin".equalsIgnoreCase(role)) {
                            // Quản trị viên
                            intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        } else {
                            // Vai trò không xác định
                            Toast.makeText(LoginActivity.this,"Khong xac dinh duoc vai tro",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Hồ sơ chưa đầy đủ, chuyển đến màn hình cập nhật profile
                        intent = new Intent(LoginActivity.this, FristUpdateProfileActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    // Người dùng mới, lưu thông tin cơ bản và chuyển đến màn hình cập nhật profile
                    saveUserData(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }
}
    private void saveUserData(FirebaseUser user) {
        String userId = user.getUid();
        String username = user.getDisplayName();
        String email = user.getEmail();
        String profileUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
        String createdAt = getCurrentDateTime(); // Lấy thời điểm hiện tại
        String updatedAt = createdAt; // Khởi tạo updatedAt bằng createdAt ban đầu

        // Tạo một đối tượng User để lưu vào Firebase Realtime Database
        Users newUser = new Users(userId, username,"", email, createdAt, updatedAt,profileUrl);

        // Lưu thông tin vào Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        usersRef.setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    checkUserProfile(auth.getCurrentUser());
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu dữ liệu thất bại
                    Toast.makeText(LoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private String getCurrentDateTime() {
        // Lấy thời gian hiện tại dưới dạng chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}