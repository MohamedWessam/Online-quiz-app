package com.wessam.quizapp.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wessam.quizapp.R;
import com.wessam.quizapp.databinding.FragmentTrackingBinding;
import com.wessam.quizapp.model.User;
import com.wessam.quizapp.ui.main.adapter.TrackingAdapter;
import com.wessam.quizapp.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Objects;

public class TrackingFragment extends Fragment {

    private FragmentTrackingBinding binding;
    private PreferenceHelper preferenceHelper;
    private NavController navController;

    public TrackingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceHelper = new PreferenceHelper(Objects.requireNonNull(getContext()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracking, container, false);

        binding.setController(this);

        binding.scoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    public void navigateToLoginActivity(){
        navController.navigate(R.id.action_trackingFragment_to_loginActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void updateUI() {
        if (preferenceHelper.getUserUid() != null) {
            binding.noUserLayout.setVisibility(View.GONE);
            binding.scoreRecyclerView.setVisibility(View.VISIBLE);
            loadScores();
        } else {
            binding.noUserLayout.setVisibility(View.VISIBLE);
            binding.scoreRecyclerView.setVisibility(View.GONE);
        }
    }

    private void loadScores() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(preferenceHelper.getUserUid());

        ArrayList<User> usersList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    usersList.add(user);

                    TrackingAdapter adapter = new TrackingAdapter(getActivity(), usersList);
                    adapter.notifyDataSetChanged();
                    binding.scoreRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}