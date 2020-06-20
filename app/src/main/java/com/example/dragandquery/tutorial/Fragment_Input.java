package com.example.dragandquery.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

/***
 */

public class Fragment_Input extends Fragment {

    //coms
    private RelativeLayout rl_input;
    private LinearLayout ll_input;
    private TextView tv_input;
    private Button btn_accept_input;
    private ImageView bird;
    private Animation fromtop;
    private Animation totop;
    private Context context;
    private ScrollView scroller;

    //vars
    private Fragment_Input_Listener listener;
    private boolean isOpen = true;
    private String lection_id;

    //interface
    public interface Fragment_Input_Listener{
        void onAccept();
        void onBird(boolean wasOpen);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input, container, false);

        //init coms
        context = getContext();
        tv_input = v.findViewById(R.id.tv_input);
        rl_input = v.findViewById(R.id.frag_input);
        ll_input = v.findViewById(R.id.ll_input);
        btn_accept_input = v.findViewById(R.id.btn_input);
        bird = v.findViewById(R.id.bird_input);
        fromtop = AnimationUtils.loadAnimation(context, R.anim.fromtop);
        totop = AnimationUtils.loadAnimation(context, R.anim.totop);
        scroller = v.findViewById(R.id.scroll_input);

        //match input to lec id
        lection_id = getArguments().getString(TutorialCategoryLection.LEC_KEY);
        setText();

        rl_input.setAnimation(fromtop);


        //get on to exercise
        btn_accept_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAccept();
            }
        });

        //move on/back to exercise
        bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    listener.onAccept();
                }else{
                    listener.onBird(isOpen);
                }

            }
        });

        return v;
    }

    public void hideBird(){
        bird.setVisibility(View.INVISIBLE);
    }

    public void showBird(){
        bird.setVisibility(View.VISIBLE);
    }

    public void goInvisible(){
        ll_input.setVisibility(View.INVISIBLE);
        isOpen = false;
    }

    public void goInclickable(){
        bird.setClickable(false);
    }

    public void goClickable(){
        bird.setClickable(true);
    }

    //todo make global (just test in category class and intent)
    public void setText(){
        String text = "";
        if(lection_id.substring(0,5).equals("01_00")){
            text = getString(R.string.c1l0_input);
        }else if(lection_id.substring(0,5).equals("01_01")){
            text = getString(R.string.c1l1_input);
        }else if(lection_id.substring(0,5).equals("01_02")){
            text = getString(R.string.c1l2_input);
        }else if(lection_id.substring(0,5).equals("01_03")){
            text = getString(R.string.c1l3_input);
        }else if(lection_id.substring(0,5).equals("01_04")){
            text = getString(R.string.c1l4_input);
        }else if(lection_id.substring(0,5).equals("01_05")){
            text = getString(R.string.c1l5_input);
        }else if(lection_id.substring(0,5).equals("01_06")){
            text = getString(R.string.c1l6_input);
        }else if(lection_id.substring(0,5).equals("01_07")){
            text = getString(R.string.c1l7_input);
        }else if(lection_id.substring(0,5).equals("01_08")){
            text = getString(R.string.c1l8_input);
        }else if(lection_id.substring(0,5).equals("01_09")){
            text = getString(R.string.c1l9_input);
        }else if(lection_id.substring(0,5).equals("01_10")){
            text = getString(R.string.c1l10_input);
        }else if(lection_id.substring(0,5).equals("01_11")){
            text = getString(R.string.c1l11_input);
        }else if(lection_id.substring(0,5).equals("03_01")){
            text = getString(R.string.c3l1_input);
        }else if(lection_id.substring(0,5).equals("04_01")){
            text = getString(R.string.c4l1_input);
        }
        tv_input.setText(text);
    }

    public void goVisible(){
        ll_input.setVisibility(View.VISIBLE);
        isOpen = true;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof Fragment_Input_Listener){
            listener = (Fragment_Input_Listener) context;
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
