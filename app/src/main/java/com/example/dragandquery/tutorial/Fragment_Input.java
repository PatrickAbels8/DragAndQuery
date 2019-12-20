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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

/***
 * TODO
 * -
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

    //vars
    private Fragment_Input_Listener listener;
    private boolean isOpen = true;

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
                    bird.setImageResource(R.drawable.bird_small_open);
                    listener.onAccept();
                }else{
                    bird.setImageResource(R.drawable.bird_small_closed);
                    listener.onBird(isOpen);
                }

            }
        });

        return v;
    }

    public void goInvisible(){
        Log.d("############ hello from", "input.goInvisible()");
        ll_input.setVisibility(View.INVISIBLE);
        isOpen = false;
    }

    public void goInclickable(){
        Log.d("############ hello from", "input.doInclickable()");
        bird.setClickable(false);
    }

    public void goClickable(){
        Log.d("############ hello from", "input.goClickable()");
        bird.setClickable(true);
    }

    //todo add other inputs
    public void goVisible(String lection_id){
        Log.d("############ hello from", "input.goVisible()");
        ll_input.setVisibility(View.VISIBLE);
        String text = "";
        if(lection_id.substring(0,5).equals("01_01")){
            text = getString(R.string.c1l1_input);
        }
        tv_input.setText(text);
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
