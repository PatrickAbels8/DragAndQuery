package com.example.dragandquery.tutorial.lections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;
import com.example.dragandquery.block.Block;
import com.example.dragandquery.block.BlockFactory;
import com.example.dragandquery.tutorial.Fragment_Content;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * TODO
 * - outsource exercise frag (var arg radiobuttons, question string, answer int)
 * - verify answer (no hard coding)
 * - add background emoji with question marks
 */

public class Fragment_LectionContent_0101 extends Fragment_Content {

    //coms
    private RelativeLayout rl_exercise;
    private Button btn_go;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    //vars
    private Fragment_LectionContent_0101_Listener listener;
    public Context context;

    //interface
    public interface Fragment_LectionContent_0101_Listener{
        void onGo(boolean isCorrect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lectioncontent_0101, container, false);

        //init coms
        rl_exercise = (RelativeLayout) v.findViewById(R.id.frag_lectioncontent_0101);
        btn_go = (Button) v.findViewById(R.id.btn_lectioncontent_0101_go);
        context = getContext();
        rg = (RadioGroup) v.findViewById(R.id.rg_c1l1);
        rb1 = (RadioButton) v.findViewById(R.id.rb1_c1l1);
        rb2 = (RadioButton) v.findViewById(R.id.rb2_c1l1);
        rb3 = (RadioButton) v.findViewById(R.id.rb3_c1l1);

        //exercise mode
        rg.clearCheck();
        btn_go.setOnClickListener((View view) -> {
            listener.onGo(verifyAnswer());
        });

        return v;
    }

    //todo check if solution was correct (db stuff) + NO HARD CODING
    public boolean verifyAnswer(){
        return !rb1.isChecked() && !rb2.isChecked() && rb3.isChecked();
    }

    //for later
    /*public void checkAnswer(){
        answered = true;
        RadioButton rbSelected = findViewById(rg.getCheckedRadioButtonId());
        int answerNr = rg.indexOfChild(rbSelected)+1;
        if(answerNr == currentQuestion.getAnswerNr()){
            //what to do when answer is correct
        }
    }*/


    public void pauseExercise(){
        Log.d("############ hello from", "pauseExercise()");
        rl_exercise.setAlpha(0.2f);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        btn_go.setClickable(false);
    }

    public void startExercise(){
        Log.d("############ hello from", "startExercise()");
        rl_exercise.setVisibility(View.VISIBLE);
        rl_exercise.setAlpha(1f);
        rb1.setClickable(true);
        rb2.setClickable(true);
        rb3.setClickable(true);
        btn_go.setClickable(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_LectionContent_0101_Listener){
            listener = (Fragment_LectionContent_0101_Listener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}