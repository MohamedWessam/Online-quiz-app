package com.wessam.quizapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.ActivityRegisterBinding;
import com.wessam.quizapp.ui.main.MainActivity;
import com.wessam.quizapp.ui.login.LoginActivity;
import com.wessam.quizapp.utils.PreferenceHelper;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        binding.setController(this);
    }

    public void createUser() {
        String email           = Objects.requireNonNull(binding.emailText.getText()).toString().trim();
        String password        = Objects.requireNonNull(binding.passwordText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.confirmPasswordText.getText()).toString().trim();
        if (email.isEmpty()) {
            binding.emailText.setError(getResources().getString(R.string.required));
        } else if (password.isEmpty()) {
            binding.passwordText.setError(getResources().getString(R.string.required));
        } else if (!confirmPassword.equals(password)) {
            binding.confirmPasswordText.setError(getResources().getString(R.string.not_match));
            binding.passwordText.setText("");
            binding.confirmPasswordText.setText("");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            saveUserUid();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String           uid    = user.getUid();
            PreferenceHelper helper = new PreferenceHelper(this);
            helper.setUserUid(uid);
        }
    }

    public void openMainActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void openLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
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