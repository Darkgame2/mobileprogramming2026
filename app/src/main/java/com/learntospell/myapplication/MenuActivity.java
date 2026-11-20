package com.learntospell.myapplication;

import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private static final String TAG_LC = "Lifecycle";
    private Button btnGoogleSignIn, btnLogout, btnMain, btnLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 1. Inisialisasi Firebase Auth & Google Sign-In
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account.getIdToken());
                            }
                        } catch (ApiException e) {
                            Toast.makeText(this, "Login Google Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // 2. Tombol Login Google
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnLogout = findViewById(R.id.btnLogout);
        btnMain = findViewById(R.id.btnMain);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);

        btnGoogleSignIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Listener Tombol Logout
        btnLogout.setOnClickListener(v -> {
            // 1. Keluar dari sesi Firebase Authentication
            mAuth.signOut();

            // 2. Keluar dari Google Client agar pop-up pilih akun muncul lagi saat login berikutnya
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Toast.makeText(MenuActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                updateUI(null);
            });
        });

        // 3. Tombol Pindah ke Halaman Game (MainActivity)
        btnMain.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String nama = user.getDisplayName();
                            String email = user.getEmail();
                            String uid = user.getUid();

                            Toast.makeText(MenuActivity.this, "Berhasil Login! Halo " + nama + " (" + email + ")", Toast.LENGTH_LONG).show();
                            Log.d("FirebaseAuth", "User UID: " + uid);

                            updateUI(user);
                        }
                    } else {
                        Toast.makeText(MenuActivity.this, "Autentikasi Firebase Gagal", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Kondisi SUDAH LOGIN
            btnGoogleSignIn.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            // PILIH SALAH SATU CARA (Buka kunci tombol Mulai Main):
            btnMain.setVisibility(View.VISIBLE);              // Cara 1: Tombol aktif & bisa diklik
            btnLeaderboard.setVisibility(View.VISIBLE);
            // btnMain.setVisibility(View.VISIBLE); // Cara 2: Munculkan tombol jika pakai Gone

            Toast.makeText(this, "Terhubung Firebase: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.d("FirebaseAuth", "User aktif: " + user.getEmail() + " | UID: " + user.getUid());
        } else {
            // Kondisi BELUM LOGIN / SUDAH LOGOUT
            btnGoogleSignIn.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            // PILIH SALAH SATU CARA (Kunci tombol Mulai Main):
            btnMain.setVisibility(View.GONE);             // Cara 1: Tombol nonaktif (abu-abu)
            btnLeaderboard.setVisibility(View.GONE);
            // btnMain.setVisibility(View.GONE);    // Cara 2: Sembunyikan tombol sepenuhnya
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_LC, "onStart");

        if (mAuth != null) {
            updateUI(mAuth.getCurrentUser());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG_LC, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_LC, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG_LC, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_LC, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LC, "onDestroy, isFinishing=" + isFinishing()
                + ", isChangingConfigurations=" + isChangingConfigurations());
    }
}