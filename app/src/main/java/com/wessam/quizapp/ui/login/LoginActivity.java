package com.wessam.quizapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.ActivityLoginBinding;
import com.wessam.quizapp.ui.main.MainActivity;
import com.wessam.quizapp.ui.register.RegisterActivity;
import com.wessam.quizapp.utils.PreferenceHelper;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        binding.setController(this);

        getWindow().setEnterTransition(null);

        new Handler().postDelayed(() -> binding.group.setVisibility(View.VISIBLE), 1000);
    }

    public void signIn() {
        String email    = Objects.requireNonNull(binding.emailText.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordText.getText()).toString().trim();
        if (email.isEmpty()) {
            binding.emailText.setError(getResources().getString(R.string.required));
        } else if (password.isEmpty()) {
            binding.passwordText.setError(getResources().getString(R.string.required));
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            saveUserUid();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void openMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void openRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void saveUserUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String           uid    = user.getUid();
            PreferenceHelper helper = new PreferenceHelper(this);
            helper.setUserUid(uid);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}