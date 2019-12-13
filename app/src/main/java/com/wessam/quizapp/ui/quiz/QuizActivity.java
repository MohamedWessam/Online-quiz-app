package com.wessam.quizapp.ui.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.ActivityQuizBinding;
import com.wessam.quizapp.model.Questions;
import com.wessam.quizapp.model.User;
import com.wessam.quizapp.utils.Constants;
import com.wessam.quizapp.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.wessam.quizapp.utils.Constants.SUBJECT_TITLE_KEY;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private FirebaseDatabase database;
    private CountDownTimer timer;
    private ObjectAnimator animator;

    private String categoryId, subjectTitle;
    private int index = 0;
    private int correctAnswer = 0;
    private int questionNumber = 0;
    private double currentScore = 0;

    private List<Questions> questionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        PushDownAnim.setPushDownAnimTo(binding.answerTextA, binding.answerTextB, binding.answerTextC, binding.answerTextD, binding.nextButton);
        binding.nextButton.setClickable(false);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        categoryId = intent.getStringExtra(Constants.CATEGORY_ID_KEY);
        subjectTitle = intent.getStringExtra(SUBJECT_TITLE_KEY);

        loadQuestionsList();
    }

    public void onAnswerClick(View view) {
        timer.cancel();
        AppCompatButton clickedButton = (AppCompatButton) view;
        if (clickedButton.getText().equals(questionsList.get(index).getCorrectAnswer())) {
            // right answer
            correctAnswer++;
            makeBlinkEffect(clickedButton, Color.GREEN);
            playGifAnimation(R.mipmap.gif_true);
            playSound(R.raw.sound_tick);
            makeButtonsUnClickable();
        } else {
            // wrong answer
            playGifAnimation(R.mipmap.gif_false);
            makeBlinkEffect(clickedButton, Color.RED);
            playSound(R.raw.sound_false);
            makeButtonsUnClickable();
        }
    }

    public void onNextClick(View view) {
        if (animator != null)
            animator.cancel();
        showQuestion(++index);
        makeButtonsClickable();
        playGifAnimation(R.mipmap.gif_thinking);
        binding.answerTextA.setBackgroundResource(R.drawable.answer_item_button);
        binding.answerTextB.setBackgroundResource(R.drawable.answer_item_button);
        binding.answerTextC.setBackgroundResource(R.drawable.answer_item_button);
        binding.answerTextD.setBackgroundResource(R.drawable.answer_item_button);
    }

    private void loadQuestionsList() {
        playGifAnimation(R.mipmap.gif_thinking);

        DatabaseReference questionsRef = database.getReference().child("questions");
        questionsRef.orderByChild("category_id").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Questions questions = postSnapshot.getValue(Questions.class);
                    questionsList.add(questions);
                }
                //shuffle the questions in the list
                Collections.shuffle(questionsList);
                showQuestion(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion(int index) {
        if (index < questionsList.size()) {
            Questions questions = questionsList.get(index);
            questionNumber++;
            binding.quizNumber.setText(String.format(Locale.US, "%d / %d", questionNumber, questionsList.size()));

            prepareTimer(20000L);

            if (!questions.getImage().isEmpty()) {
                Picasso.get().load(questions.getImage()).into(binding.questionImage);
                binding.imageLayout.setVisibility(View.VISIBLE);
            } else {
                binding.imageLayout.setVisibility(View.GONE);
            }
            binding.questionText.setText(questions.getQuestion());
            binding.answerTextA.setText(questions.getAnswerA());
            binding.answerTextB.setText(questions.getAnswerB());
            binding.answerTextC.setText(questions.getAnswerC());
            binding.answerTextD.setText(questions.getAnswerD());
        } else {
            // no other questions
            currentScore = (correctAnswer * 100) / questionsList.size();
            ScoreDialog.showScoreDialog(this, currentScore + " %");
            saveInDatabase();
        }
    }


    private void saveInDatabase() {
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        String           uid              = preferenceHelper.getUserUid();
        if (uid != null) {
            DatabaseReference userRef = database.getReference().child("user").child(uid).child(subjectTitle);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User   user = dataSnapshot.getValue(User.class);
                    double highestScore;
                    if (user != null) {
                        highestScore = user.getHighestScore();
                        if (currentScore > highestScore) {
                            user.setHighestScore(currentScore);
                            user.setSubjectTitle(subjectTitle);
                            userRef.setValue(user);
                        }
                    } else {
                        user = new User();
                        user.setHighestScore(currentScore);
                        user.setSubjectTitle(subjectTitle);
                        userRef.setValue(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    public void prepareTimer(Long totalTime) {
        if (timer != null)
            timer.cancel();

        timer = new CountDownTimer(totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds  = (int) (millisUntilFinished / 1000);
                int hours    = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes  = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                binding.timerProgress.setProgress(seconds);
                binding.timerText.setText(String.valueOf(seconds));
            }

            public void onFinish() {
                binding.timerText.setText("0");
                makeButtonsUnClickable();
            }
        }.start();
    }

    private void playGifAnimation(int id) {
        binding.gifQuestions.setGifResource(id);
        binding.gifQuestions.play();
    }

    private void playSound(int id) {
        final MediaPlayer mp = MediaPlayer.create(this, id);
        mp.start();
    }

    private void makeBlinkEffect(Button button, int color) {
        animator = ObjectAnimator.ofInt(button, "backgroundColor",
                getResources().getColor(R.color.answer_button_color), color);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.REVERSE);
        animator.start();
    }

    private void makeButtonsUnClickable() {
        binding.answerTextA.setClickable(false);
        binding.answerTextB.setClickable(false);
        binding.answerTextC.setClickable(false);
        binding.answerTextD.setClickable(false);
        binding.nextButton.setClickable(true);
        binding.nextButton.setImageResource(R.drawable.ic_next_pressed);
    }

    private void makeButtonsClickable() {
        binding.answerTextA.setClickable(true);
        binding.answerTextB.setClickable(true);
        binding.answerTextC.setClickable(true);
        binding.answerTextD.setClickable(true);
        binding.nextButton.setClickable(false);
        binding.nextButton.setImageResource(R.drawable.ic_next_unpressed);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}