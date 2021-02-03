package com.pharos.notea2pp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pharos.notea2pp.App;
import com.pharos.notea2pp.OnItemClickListener;
import com.pharos.notea2pp.Prefs;
import com.pharos.notea2pp.R;
import com.pharos.notea2pp.model.Note;

import java.util.List;

public class HomeFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private boolean updateApp = false;
    private int position;
    private List<Note> list;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NoteAdapter();
        setHasOptionsMenu(true);
        adapter.setList(loadData());
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort_for_alf) {
            adapter.sortList(App.getAppDataBase().noteDao().sortAll());
            return true;
        } else if (item.getItemId() == R.id.sort_for_time) {
            adapter.sortList(App.getAppDataBase().noteDao().sortTime());
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            updateApp = false;
            openForm(null);
        });
        setFragmentListener();
        init();
    }

    private List<Note> loadData() {
        return list = App.getAppDataBase().noteDao().getAll();
    }

    private void init() {
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int pos, Note note) {
                updateApp = true;
                position = pos;
                adapter.getItem(pos);
                openForm(note);
            }

            @Override
            public void longClick(int pos, Note note) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setTitle("Вы уверены, что хотите удалить?")
                        .setMessage("Удалено")
                        .setPositiveButton("Да", (dialog, which) ->
                        {
                            adapter.removeItem(pos);
                            deleteFromFireStore(note);
                            App.getAppDataBase().noteDao().delete(note);
                        })
                        .setNegativeButton("Нет", null);
                alert.create().show();
            }
        });
    }

    private void setFragmentListener() {
        getParentFragmentManager().setFragmentResultListener(
                "rk_form",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    Note note = (Note) result.getSerializable("note");
                    if (updateApp) {
                        adapter.setItem(note, position);
                    } else {
                        adapter.addItem(note);
                    }
                }
        );
    }

    private void deleteFromFireStore(Note note) {
        db.collection("notes").document(note.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("TAGF3", "Deleted, ID: " + note.getId());
                    App.getAppDataBase().noteDao().update(note);
                })
                .addOnFailureListener(e ->
                        Log.e("TAGF4", "Error with delete operation", e));
    }


    private void openForm(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        NavController navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.form_fragment, bundle);
    }
}