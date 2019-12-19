package com.example.dragandquery.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

/***
 * TODO
 * - bad feedback align with bird
 */

public class Fragment_Feedback extends Fragment {

    //coms
    private RelativeLayout rl_feedback;
    private TextView tv_msg;
    private Button btn_back;
    private Button btn_forth;

    //vars
    private Fragment_Feedback_Listener listener;

    //interface
    public interface Fragment_Feedback_Listener{
        void onBack();
        void onForth();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);

        //init coms
        tv_msg = v.findViewById(R.id.tv_feedback_msg);
        rl_feedback = v.findViewById(R.id.frag_feedback);
        btn_back = v.findViewById(R.id.btn_feedback_back);
        btn_forth = v.findViewById(R.id.btn_feedback_forth);

        //get back to wherever you came from
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBack();
            }
        });

        //move on to wherever you wanna go to
        btn_forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onForth();
            }
        });

        return v;
    }

    public void goInvisible(){
        rl_feedback.setVisibility(View.INVISIBLE);
    }

    public void goVisible(boolean isCorrect){
        rl_feedback.setVisibility(View.VISIBLE);
        if(isCorrect){
            startGoodFeedback();
        }else{
            startBadFeedback();
        }
    }

    public void startGoodFeedback(){
        tv_msg.setText(R.string.feedback_msg_good);
        btn_back.setVisibility(View.VISIBLE);
        btn_forth.setVisibility(View.VISIBLE);
    }

    public void startBadFeedback(){
        tv_msg.setText(R.string.feedback_msg_bad);
        btn_back.setVisibility(View.VISIBLE);
        btn_forth.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Feedback_Listener){
            listener = (Fragment_Feedback_Listener) context;
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
