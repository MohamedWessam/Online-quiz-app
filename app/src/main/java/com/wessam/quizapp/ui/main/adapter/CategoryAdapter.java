package com.wessam.quizapp.ui.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.wessam.quizapp.R;
import com.wessam.quizapp.model.Categories;
import com.wessam.quizapp.ui.quiz.QuizActivity;
import com.wessam.quizapp.utils.Constants;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Categories> categoriesList;
    private Activity activity;

    public CategoryAdapter(Activity activity, ArrayList<Categories> categoriesList) {
        this.activity = activity;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.category_layout_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);

        holder.categoryTitle.setText(categories.getName());
        holder.setCategoryImage(categories.getImage());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, QuizActivity.class);
            intent.putExtra(Constants.CATEGORY_ID_KEY, categories.getId());
            intent.putExtra(Constants.SUBJECT_TITLE_KEY, categories.getName());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView categoryImage;
        private AppCompatTextView categoryTitle;

        private CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryTitle = itemView.findViewById(R.id.category_title);
        }

        private void setCategoryImage(String url) {
            Picasso.get().load(url).into(categoryImage);
        }

    }

}
