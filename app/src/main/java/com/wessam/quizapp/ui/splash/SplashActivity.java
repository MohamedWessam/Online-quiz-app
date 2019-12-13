package com.wessam.quizapp.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.ActivitySplashBinding;
import com.wessam.quizapp.ui.login.LoginActivity;
import com.wessam.quizapp.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        animateLogo();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        decideNextActivity();
    }

    private void animateLogo() {
        binding.imageView.animate().rotation(360).setDuration(1000).setStartDelay(1000).start();
    }

    private void decideNextActivity() {
        new Handler().postDelayed(() -> {
            if (currentUser != null)
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            else
                openLoginActivity();
            finish();
        }, 2500);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(binding.appCompatTextView, "SPLASH_TEXT_TRANSITION"),
                Pair.create(binding.imageView, "SPLASH_LOGO_TRANSITION"));
        startActivity(intent, options.toBundle());
    }

}