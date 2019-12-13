package com.wessam.quizapp.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.FragmentCategoryBinding;
import com.wessam.quizapp.model.Categories;
import com.wessam.quizapp.ui.main.adapter.CategoryAdapter;
import com.wessam.quizapp.utils.NetworkHelper;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private DatabaseReference reference;

    public CategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("category");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);

        binding.setController(this);

        binding.categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        loadCategories();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkNetwork();
    }

    public synchronized void checkNetwork() {
        if (NetworkHelper.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            binding.categoriesRecyclerView.setVisibility(View.VISIBLE);
            binding.noInternetLayout.setVisibility(View.GONE);
        } else {
            binding.noInternetLayout.setVisibility(View.VISIBLE);
            binding.categoriesRecyclerView.setVisibility(View.GONE);
            binding.noInternetImage.setGifResource(R.mipmap.no_internet_icon);
            binding.noInternetImage.play();
        }
    }

    private void loadCategories() {
        ArrayList<Categories> categoriesList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Categories categories = dataSnapshot1.getValue(Categories.class);
                    categoriesList.add(categories);

                    CategoryAdapter adapter = new CategoryAdapter(getActivity(), categoriesList);
                    adapter.notifyDataSetChanged();
                    binding.categoriesRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}