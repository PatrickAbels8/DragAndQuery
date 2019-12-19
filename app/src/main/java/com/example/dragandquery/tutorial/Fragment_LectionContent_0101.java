package com.example.dragandquery.tutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * TODO
 * - scroll down cat name
 * - outsource input frag and exercise frag
 * - verify answer
 */

public class Fragment_LectionContent_0101 extends Fragment {

    //coms
    private RelativeLayout rl_lectioncontent;
    private LinearLayout ll_input;
    private RelativeLayout rl_exercise;
    private Button btn_go;
    private Button btn_accept_input;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private ImageView bird;

    //vars
    private Fragment_LectionContent_0101_Listener listener;
    private boolean inputOpen;
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
        rl_lectioncontent = (RelativeLayout) v.findViewById(R.id.frag_lectioncontent_0101);
        ll_input = (LinearLayout) v.findViewById(R.id.ll_c1l1_input);
        rl_exercise = (RelativeLayout) v.findViewById(R.id.rl_c1l1_exercise);
        btn_go = (Button) v.findViewById(R.id.btn_lectioncontent_0101_go);
        btn_accept_input = (Button) v.findViewById(R.id.btn_c1l1_input);
        context = getContext();
        rg = (RadioGroup) v.findViewById(R.id.rg_c1l1);
        rb1 = (RadioButton) v.findViewById(R.id.rb1_c1l1);
        rb2 = (RadioButton) v.findViewById(R.id.rb2_c1l1);
        rb3 = (RadioButton) v.findViewById(R.id.rb3_c1l1);
        bird = (ImageView) v.findViewById(R.id.bird_c1l1);
        inputOpen = false;


        /***
         * PUT IN CONTENT HERE
         * - show input tv
         * - when input_Accepted btn clicked mahe invisible
         * - show exercise
         *      1. free mode: aready done
         *      2. multipe choice: when go btn clicked show feedback frgment --> back or forth
         *      - when bird lick either open or close input
         */

        //input mode
        ll_input.setVisibility(View.VISIBLE);
        rl_exercise.setVisibility(View.INVISIBLE);
        btn_accept_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_input.setVisibility(View.INVISIBLE);
                rl_exercise.setVisibility(View.VISIBLE);
            }
        });

        //exercise mode

        rg.clearCheck();
        rl_exercise.setClickable(true);
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputOpen){
                    bird.setImageResource(R.drawable.bird_small_open);
                    ll_input.setVisibility(View.VISIBLE);
                    btn_accept_input.setVisibility(View.INVISIBLE);
                    goInclickableButBird();
                    inputOpen = true;
                }else{
                    bird.setImageResource(R.drawable.bird_small_closed);
                    ll_input.setVisibility(View.INVISIBLE);
                    goClickableButBird();
                    inputOpen = false;
                }
            }
        });
        btn_go.setOnClickListener((View view) -> {
            goInclickable();
            listener.onGo(verifyAnswer());
        });

        //feedback mode --> already outsourced



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

    public void goInclickableButBird(){
        rl_exercise.setAlpha(0.2f);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        btn_go.setClickable(false);
    }

    public void goClickableButBird(){
        rl_exercise.setAlpha(1f);
        rb1.setClickable(true);
        rb2.setClickable(true);
        rb3.setClickable(true);
        btn_go.setClickable(true);
    }

    public void goInclickable(){
        rl_exercise.setAlpha(0.2f);
        bird.setVisibility(View.INVISIBLE);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        btn_go.setClickable(false);
    }

    public void goClickable(){
        rl_exercise.setAlpha(1f);
        bird.setVisibility(View.VISIBLE);
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
