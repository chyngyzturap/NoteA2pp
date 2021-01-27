package com.pharos.notea2pp.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.pharos.notea2pp.Information;
import com.pharos.notea2pp.OnItemClickListener;
import com.pharos.notea2pp.R;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;

    public BoardAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Information information = new Information();
        private Button btnStart;
        private TextView textTitle, textDescription;
        private LottieAnimationView animationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.txtTitle);
            textDescription = itemView.findViewById(R.id.txtDesc);
            btnStart = itemView.findViewById(R.id.btn_start);
            animationView = itemView.findViewById(R.id.lottie_layer_name);

            btnStart.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }

        public void bind(int position) {
            textTitle.setText(information.getTitle(position));
            textDescription.setText(information.getDescription(position));
            animationView.setAnimation(information.getIcon(position));
            btnStart.setVisibility(View.GONE);
            if (position == 2)
                btnStart.setVisibility(View.VISIBLE);
        }
    }
}
