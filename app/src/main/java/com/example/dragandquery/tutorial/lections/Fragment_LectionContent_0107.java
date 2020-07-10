package com.example.dragandquery.tutorial.lections;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dragandquery.R;
import com.example.dragandquery.tutorial.Fragment_Content;

/***
 * - 
 */

public class Fragment_LectionContent_0107 extends Fragment_Content {

    //coms
    private RelativeLayout rl_exercise;
    private Button btn_go;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    //vars
    private Fragment_LectionContent_0107_Listener listener;
    public Context context;

    //interface
    public interface Fragment_LectionContent_0107_Listener{
        void onGo(boolean isCorrect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lectioncontent_0107, container, false);

        //init coms
        rl_exercise = v.findViewById(R.id.frag_lectioncontent_0107);
        btn_go = v.findViewById(R.id.btn_lectioncontent_0107_go);
        context = getContext();
        rg = v.findViewById(R.id.rg_c1l7);
        rb1 = v.findViewById(R.id.rb1_c1l7);
        rb2 = v.findViewById(R.id.rb2_c1l7);
        rb3 = v.findViewById(R.id.rb3_c1l7);

        //exercise mode
        rg.clearCheck();
        btn_go.setOnClickListener((View view) -> listener.onGo(verifyAnswer()));

        return v;
    }

    public boolean verifyAnswer(){
        return !rb1.isChecked() && rb2.isChecked() && !rb3.isChecked();
    }

    public void pauseExercise(){
        rl_exercise.setAlpha(0.2f);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        btn_go.setClickable(false);
    }

    public void startExercise(){
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
        if(context instanceof Fragment_LectionContent_0107_Listener){
            listener = (Fragment_LectionContent_0107_Listener) context;
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
