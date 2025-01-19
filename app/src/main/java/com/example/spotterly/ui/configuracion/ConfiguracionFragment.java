package com.example.spotterly.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotterly.databinding.FragmentConfiguracionBinding;

public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.question1.setOnClickListener(v -> toggleAnswer(binding.question1, binding.answer1));
        binding.question2.setOnClickListener(v -> toggleAnswer(binding.question2, binding.answer2));
        binding.question3.setOnClickListener(v -> toggleAnswer(binding.question3, binding.answer3));
        binding.question4.setOnClickListener(v -> toggleAnswer(binding.question4, binding.answer4));
        binding.question5.setOnClickListener(v -> toggleAnswer(binding.question5, binding.answer5));
        binding.question6.setOnClickListener(v -> toggleAnswer(binding.question6, binding.answer6));
        binding.question7.setOnClickListener(v -> toggleAnswer(binding.question7, binding.answer7));
        binding.question8.setOnClickListener(v -> toggleAnswer(binding.question8, binding.answer8));
        binding.question9.setOnClickListener(v -> toggleAnswer(binding.question9, binding.answer9));
        binding.question10.setOnClickListener(v -> toggleAnswer(binding.question10, binding.answer10));
        binding.question11.setOnClickListener(v -> toggleAnswer(binding.question11, binding.answer11));
        binding.question12.setOnClickListener(v -> toggleAnswer(binding.question12, binding.answer12));
        binding.question13.setOnClickListener(v -> toggleAnswer(binding.question13, binding.answer13));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void toggleAnswer(TextView question, TextView answer) {
        if (answer.getVisibility() == View.GONE) {
            // Mostrar la respuesta
            answer.setVisibility(View.VISIBLE);
        } else {
            // Ocultar la respuesta
            answer.setVisibility(View.GONE);
        }
    }
}
