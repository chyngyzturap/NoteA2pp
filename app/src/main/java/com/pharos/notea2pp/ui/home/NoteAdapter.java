package com.pharos.notea2pp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pharos.notea2pp.OnItemClickListener;
import com.pharos.notea2pp.R;
import com.pharos.notea2pp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final ArrayList<Note> list;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public NoteAdapter(Context context) {
        list = new ArrayList<>();
this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Note note){
        list.add(0,note);
        notifyItemChanged(list.indexOf(0));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Note getItem(int pos){
        return list.get(pos);
    }

    public void remove(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }
    public void setList(List<Note> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void setItem(Note note, int id){
        list.set(id,note);
        notifyDataSetChanged();
    }
    public void sortList(List<Note> sort){
        list.clear();
        list.addAll(sort);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle,txtDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        itemView.setOnLongClickListener(v -> {onItemClickListener.longClick(getAdapterPosition());
        return true;});
        }
        public void bind(Note note){
            txtTitle.setText(note.getTitle());
            txtDate.setText(note.getDate());
        }
    }

}
