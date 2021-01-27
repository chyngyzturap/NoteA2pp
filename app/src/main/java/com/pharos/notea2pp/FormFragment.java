package com.pharos.notea2pp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pharos.notea2pp.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormFragment extends Fragment {
    EditText editText;
    Note note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.edit_txt);
        note = (Note) requireArguments().getSerializable("note");
        if (note != null)
            editText.setText(note.getTitle());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> save());
    }

    private void save() {
        String text = editText.getText().toString().trim();
        String time = getTime();
        if (note == null){
            note = new Note (text, time);
            App.getAppDataBase().noteDao().insert(note);
        } else {
note.setTitle(text);
note.setDate(time);
App.getAppDataBase().noteDao().update(note);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        getParentFragmentManager().setFragmentResult("rk_form", bundle);
        close();
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment);
        navController.navigateUp();
    }

    private String getTime() {
        Date currentDate = new Date();
        DateFormat time = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return time.format(currentDate);
    }
}