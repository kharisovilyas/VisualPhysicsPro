package com.example.visualphysics10.inform.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.databinding.FragmentTestBinding;
import com.example.visualphysics10.net.AppForNet;
import com.example.visualphysics10.net.InternetConnection;
import com.example.visualphysics10.net.TestingList;
import com.example.visualphysics10.net.Testings;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTest extends Fragment {
    public static FragmentTest newInstance(String param1, String param2) {
        return new FragmentTest();
    }
    private FragmentTestBinding binding;
    private ArrayList<Testings> taskList;
    private MaterialTextView taskTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        getDataFromNetwork();
    }

    private void getDataFromNetwork() {
        if(InternetConnection.checkConnection(Objects.requireNonNull(getContext()))){
            Call<TestingList> task = AppForNet.api.getTask();
            taskList = new ArrayList<>();
            taskTextView = binding.materialTextView;
            taskTextView.setText(R.string.task1);
            task.enqueue(new Callback<TestingList>() {
                @Override
                public void onResponse(@NonNull Call<TestingList> call, @NonNull Response<TestingList> response) {
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        taskList = (ArrayList<Testings>) response.body().getTask();
                        taskTextView.setText((CharSequence) taskList);
                    }
                }

                @Override
                public void onFailure(Call<TestingList> call, Throwable t) {
                    taskTextView.setText(R.string.task1);
                }
            });
        }
    }

    private void addToolbar() {
        MaterialToolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.test);
        toolbar.setNavigationOnClickListener(v -> {
            //TODO: выходит из приложения
        });
    }
}