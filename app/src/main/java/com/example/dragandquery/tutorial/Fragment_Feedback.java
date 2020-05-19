package com.example.dragandquery.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * TODO
 * -
 */

public class Fragment_Feedback extends Fragment {

    //coms
    private RelativeLayout rl_feedback;
    private TextView tv_msg;
    private Button btn_back;
    private Button btn_forth;
    private ImageView bird;
    private Animation fromright;
    private Animation toright;
    private Context context;

    //vars
    private Fragment_Feedback_Listener listener;
    public static final long TORIGHT_DURATION = 2000;
    public static final Map<String, Integer> goods = new HashMap<String, Integer>(){{
        put("01_00", R.string.c1l0_good); put("01_01", R.string.c1l1_good); put("01_02", R.string.c1l2_good); put("01_03", R.string.c1l3_good);
        put("01_04", R.string.c1l4_good); put("01_05", R.string.c1l5_good); put("01_06", R.string.c1l6_good); put("01_07", R.string.c1l7_good);
        put("01_08", R.string.c1l8_good); put("01_09", R.string.c1l9_good); put("01_10", R.string.c1l10_good); put("01_11", R.string.c1l11_good);
        put("02_01", R.string.c2l1_good); put("02_02", R.string.c2l2_good); put("02_03", R.string.c2l3_good); put("02_04", R.string.c2l4_good);
        put("02_05", R.string.c2l5_good); put("02_06", R.string.c2l6_good); put("02_07", R.string.c2l7_good); put("02_08", R.string.c2l8_good);
        put("02_09", R.string.c2l9_good); put("02_10", R.string.c2l10_good); put("02_11", R.string.c2l11_good); put("02_12", R.string.c2l12_good);
        put("02_13", R.string.c2l13_good); put("02_14", R.string.c2l14_good); put("02_15", R.string.c2l15_good); put("02_16", R.string.c2l16_good);
        put("03_01", R.string.c3l1_good); put("03_02", R.string.c3l2_good); put("03_03", R.string.c3l3_good); put("03_04", R.string.c3l4_good);
        put("03_05", R.string.c3l5_good); put("03_06", R.string.c3l6_good); put("03_07", R.string.c3l7_good); put("03_08", R.string.c3l8_good);
        put("04_01", R.string.c4l1_good); put("04_02", R.string.c4l2_good); put("04_03", R.string.c4l3_good); put("04_04", R.string.c4l4_good);
        put("04_05", R.string.c4l5_good); put("04_06", R.string.c4l6_good); put("04_07", R.string.c4l7_good); put("04_08", R.string.c4l8_good);
        put("04_09", R.string.c4l9_good);
    }};
    public static final Map<String, Integer> bads = new HashMap<String, Integer>(){{
        put("01_00", R.string.c1l0_bad); put("01_01", R.string.c1l1_bad); put("01_02", R.string.c1l2_bad); put("01_03", R.string.c1l3_bad);
        put("01_04", R.string.c1l4_bad); put("01_05", R.string.c1l5_bad); put("01_06", R.string.c1l6_bad); put("01_07", R.string.c1l7_bad);
        put("01_08", R.string.c1l8_bad); put("01_09", R.string.c1l9_bad); put("01_10", R.string.c1l10_bad); put("01_11", R.string.c1l11_bad);
        put("02_01", R.string.c2l1_bad); put("02_02", R.string.c2l2_bad); put("02_03", R.string.c2l3_bad); put("02_04", R.string.c2l4_bad);
        put("02_05", R.string.c2l5_bad); put("02_06", R.string.c2l6_bad); put("02_07", R.string.c2l7_bad); put("02_08", R.string.c2l8_bad);
        put("02_09", R.string.c2l9_bad); put("02_10", R.string.c2l10_bad); put("02_11", R.string.c2l11_bad); put("02_12", R.string.c2l12_bad);
        put("02_13", R.string.c2l13_bad); put("02_14", R.string.c2l14_bad); put("02_15", R.string.c2l15_bad); put("02_16", R.string.c2l16_bad);
        put("03_01", R.string.c3l1_bad); put("03_02", R.string.c3l2_bad); put("03_03", R.string.c3l3_bad); put("03_04", R.string.c3l4_bad);
        put("03_05", R.string.c3l5_bad); put("03_06", R.string.c3l6_bad); put("03_07", R.string.c3l7_bad); put("03_08", R.string.c3l8_bad);
        put("04_01", R.string.c4l1_bad); put("04_02", R.string.c4l2_bad); put("04_03", R.string.c4l3_bad); put("04_04", R.string.c4l4_bad);
        put("04_05", R.string.c4l5_bad); put("04_06", R.string.c4l6_bad); put("04_07", R.string.c4l7_bad); put("04_08", R.string.c4l8_bad);
        put("04_09", R.string.c4l9_bad);
    }};


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
        context = getContext();
        tv_msg = v.findViewById(R.id.tv_feedback_msg);
        rl_feedback = v.findViewById(R.id.frag_feedback);
        btn_back = v.findViewById(R.id.btn_feedback_back);
        btn_forth = v.findViewById(R.id.btn_feedback_forth);
        bird = v.findViewById(R.id.bird_feedback);
        fromright = AnimationUtils.loadAnimation(context, R.anim.fromright);
        toright = AnimationUtils.loadAnimation(context, R.anim.toright);
        toright.setDuration(TORIGHT_DURATION);

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
        rl_feedback.startAnimation(toright);
        rl_feedback.setVisibility(View.INVISIBLE);
    }

    public void goVisible(boolean isCorrect, String lecid){
        rl_feedback.setVisibility(View.VISIBLE);
        rl_feedback.startAnimation(fromright);
        if(isCorrect){
            startGoodFeedback(getResources().getString(goods.get(lecid)));
        }else{
            startBadFeedback(getResources().getString(bads.get(lecid)));
        }
    }

    public void startGoodFeedback(String good){
        tv_msg.setText(good);
        bird.setImageResource(R.drawable.happy_berry);
        btn_back.setVisibility(View.VISIBLE);
        btn_forth.setVisibility(View.VISIBLE);
    }

    public void startBadFeedback(String bad){
        tv_msg.setText(bad);
        bird.setImageResource(R.drawable.sad_berry);
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
