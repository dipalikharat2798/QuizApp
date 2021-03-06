package com.example.quizapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizapp.Model.QuizListModel;
import com.example.quizapp.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuizListModel> quizListModels;
    private OnQuizListItemCLicked onQuizListItemCLicked;
    public QuizListAdapter(OnQuizListItemCLicked onQuizListItemCLicked){
        this.onQuizListItemCLicked=onQuizListItemCLicked;
    }

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.listTitle.setText(quizListModels.get(position).getName());
        String url = quizListModels.get(position).getImage();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.listImage);
        String listDescp = quizListModels.get(position).getDesc();
        if (listDescp.length()> 150){
            listDescp=listDescp.substring(0,150);
        }
        holder.listDesc.setText(listDescp+"...");
        holder.listLevel.setText(quizListModels.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
        if(quizListModels == null){
            return 0;
        } else {
            return quizListModels.size();
        }
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView listImage;
        private TextView listTitle;
        private TextView listDesc;
        private TextView listLevel;
        private Button listBtn;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            listImage = itemView.findViewById(R.id.list_image);
            listTitle = itemView.findViewById(R.id.list_title);
            listDesc = itemView.findViewById(R.id.list_desc);
            listLevel = itemView.findViewById(R.id.list_difficulty);
            listBtn = itemView.findViewById(R.id.list_btn);

            listBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizListItemCLicked.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnQuizListItemCLicked{
        void onItemClicked(int position);
    }
}
