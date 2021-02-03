package com.pharos.notea2pp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pharos.notea2pp.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormFragment extends Fragment {
    EditText editText;
    Note note;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        if (note == null) {
            note = new Note(text, time);
            saveToFirestore(note);
        } else {
            note.setTitle(text);
            editFireStore();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        getParentFragmentManager().setFragmentResult("rk_form", bundle);

    }

    private void editFireStore() {
        db.collection("notes").document(note.getId())
                .set(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.e("TAGF1", "Updated ID: " + note.getId());
                            App.getAppDataBase().noteDao().update(note);
                        } else {
                            Log.e("TAGF2", "Error with update operation");
                        }
                    }
                });
    }

    private void saveToFirestore(Note note) {
        FirebaseFirestore.getInstance().collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Adding to DataBased
                        note.setId(documentReference.getId());
                        App.getAppDataBase().noteDao().insert(note);
                        Log.e("TAGF5", "Added ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAGF6", "Error for adding operation", e);
                    }
                });
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