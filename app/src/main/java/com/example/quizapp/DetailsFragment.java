package com.example.quizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizapp.Model.QuizListModel;
import com.example.quizapp.ViewModel.QuizListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private QuizListViewModel quizListViewModel;
    private NavController navController;
    private int position;
    private ImageView detailsImage;
    private TextView detailsTitle,detailsDiff,detailsQuestions,detailsDesc,detailScore;
    Button detailsStartbtn;
    private String quizId;
    private long totalQuestions=0L;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
     Long percent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        detailsStartbtn=view.findViewById(R.id.details_start_btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_detailsFragment_to_quizFragment);
//            }
//        });
        detailsStartbtn.setOnClickListener(this);
        position =DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        Log.d("position","position"+ position);

        detailsImage=view.findViewById(R.id.details_image);
        detailsTitle=view.findViewById(R.id.details_title);
        detailsDesc=view.findViewById(R.id.details_desc);
        detailsQuestions=view.findViewById(R.id.details_questions_text);
        detailsDiff=view.findViewById(R.id.details_difficulty_text);
        detailScore=view.findViewById(R.id.details_score_text);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                Glide.with(getContext())
                        .load(quizListModels.get(position).getImage())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(detailsImage);
                detailsTitle.setText(quizListModels.get(position).getName());
                detailsDesc.setText(quizListModels.get(position).getDesc());
                detailsQuestions.setText(quizListModels.get(position).getQuestions()+"");
                detailsDiff.setText(quizListModels.get(position).getLevel());
                quizId=quizListModels.get(position).getQuiz_id();
                totalQuestions=quizListModels.get(position).getQuestions();
                firebaseFirestore=FirebaseFirestore.getInstance();
                firebaseAuth=FirebaseAuth.getInstance();
                //loadResultData();
                loadResultData();

            }
        });
    }

    private void loadResultData() {
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if(task.isSuccessful()){
                        DocumentSnapshot document =task.getResult();
                        if(document!=null && document.exists()){
                            Long correct = document.getLong("correct");
                            Long wrong = document.getLong("wrong");
                            Long missed = document.getLong("unanswered");

                            Long total=correct+wrong+missed;
                            percent = (correct*100)/total;
                            detailScore.setText(percent+" %");
                        }else{
                            Toast.makeText(getContext(), "Take a test first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.details_start_btn:
                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action =DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
                action.setTotalQuestions(totalQuestions);
                action.setQuizid(quizId);
                navController.navigate(action);
                break;
        }
    }
}