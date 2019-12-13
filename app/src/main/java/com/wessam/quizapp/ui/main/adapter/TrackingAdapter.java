package com.wessam.quizapp.ui.main.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.wessam.quizapp.R;
import com.wessam.quizapp.model.User;

import java.util.ArrayList;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder> {

    private Activity activity;
    private ArrayList<User> userList;

    public TrackingAdapter(Activity activity, ArrayList<User> userList) {
        this.activity = activity;
        this.userList = userList;
    }

    @NonNull
    @Override
    public TrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.tracking_layout_item, parent, false);
        return new TrackingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingViewHolder holder, int position) {
        User user = userList.get(position);
        holder.subjectTitle.setText(user.getSubjectTitle());
        holder.result.setText(String.valueOf(user.getHighestScore()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class TrackingViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView subjectTitle, result;

        TrackingViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitle = itemView.findViewById(R.id.subject_title);
            result = itemView.findViewById(R.id.highest_score);
        }

    }

}
