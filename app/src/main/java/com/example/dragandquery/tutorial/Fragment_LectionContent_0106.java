package com.example.dragandquery.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

import java.util.Random;

/***
 * TODO
 * - scroll down cat name
 * - actual content
 * - button back to category
 * - verify answer
 */

public class Fragment_LectionContent_0106 extends Fragment {

    //coms
    private RelativeLayout rl_lectioncontent;
    private Button btn_go;

    //vars
    private Fragment_LectionContent_0106_Listener listener;
    public Context context;

    //interface
    public interface Fragment_LectionContent_0106_Listener{
        void onGo(boolean isCorrect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lectioncontent_0106, container, false);

        //init coms
        rl_lectioncontent = (RelativeLayout) v.findViewById(R.id.frag_lectioncontent_0102);
        btn_go = (Button) v.findViewById(R.id.btn_lectioncontent_0106_go);
        context = getContext();

        /***
         * PUT IN CONTENT HERE
         */

        //lets see if exercixe was solved correctly
        btn_go.setOnClickListener((View view) -> {
            listener.onGo(verifyAnswer());
        });

        return v;
    }

    public void goInclickable(){
        rl_lectioncontent.setAlpha(0.6f);
        btn_go.setVisibility(View.INVISIBLE);
    }

    public void goClickable(){
        rl_lectioncontent.setAlpha(1f);
        btn_go.setVisibility(View.VISIBLE);
    }

    //todo check if solution was correct
    public boolean verifyAnswer(){
        return new Random().nextBoolean();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_LectionContent_0106_Listener){
            listener = (Fragment_LectionContent_0106_Listener) context;
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
