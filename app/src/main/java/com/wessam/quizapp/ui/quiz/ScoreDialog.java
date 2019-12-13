package com.wessam.quizapp.ui.quiz;

import android.app.Activity;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.wessam.quizapp.R;

import java.util.Objects;

class ScoreDialog {

    private ScoreDialog() {
    }

    static void showScoreDialog(Activity activity, String score) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_score);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AppCompatTextView text = dialog.findViewById(R.id.score_text);
        text.setText(score);

        AppCompatButton finishButton = dialog.findViewById(R.id.btn_dialog_finish);
        AppCompatButton retryButton  = dialog.findViewById(R.id.btn_dialog_retry);

        retryButton.setOnClickListener(v -> activity.recreate());
        finishButton.setOnClickListener(v -> {
            dialog.dismiss();
            activity.finish();
        });

        dialog.show();

    }

}
