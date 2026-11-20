package com.learntospell.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

//import tambahan utk authentication
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    //untuk authentication
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private static final String TAG_LC = "Lifecycle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        Button btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    //method pembantu google
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
            Toast.makeText(this, "Terhubung Firebase: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.d("FirebaseAuth", "User aktif: " + user.getEmail() + " | UID: " + user.getUid());
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