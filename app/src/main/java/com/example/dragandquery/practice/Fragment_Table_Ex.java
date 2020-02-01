package com.example.dragandquery.practice;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dragandquery.R;

/***
 * TODO
 * - show code button
 * - code to tableview
 * - btn to exs
 */

public class Fragment_Table_Ex extends Fragment {

    //coms
    private ConstraintLayout cl_table;
    private Button btn_retry;
    private TextView raw_query;
    private Animation frombottom;
    private Animation tobottom;
    private Context context;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    //vars
    private Fragment_Table_Ex_Listener listener;

    //interface
    public interface Fragment_Table_Ex_Listener{
        void onRetry();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table_ex, container, false);

        //init coms
        context = getContext();
        cl_table = v.findViewById(R.id.frag_table);
        btn_retry = v.findViewById(R.id.frag_retry);
        raw_query = v.findViewById(R.id.raw_query);
        frombottom = AnimationUtils.loadAnimation(context, R.anim.frombottom);
        tobottom = AnimationUtils.loadAnimation(context, R.anim.tobottom);
        star1 = (ImageView) v.findViewById(R.id.star1);
        star2 = (ImageView) v.findViewById(R.id.star2);
        star3 = (ImageView) v.findViewById(R.id.star3);

        //get back to edit or forth to next lec
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRetry();
            }
        });

        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.INVISIBLE);
    }

    public void goVisible(String query, int isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        cl_table.startAnimation(frombottom);
        raw_query.setText(query);

        if(isCorrect>0)
            star1.setImageResource(R.drawable.star_full);
        else
            star1.setImageResource(R.drawable.star_empty);
        if(isCorrect>1)
            star2.setImageResource(R.drawable.star_full);
        else
            star2.setImageResource(R.drawable.star_empty);
        if(isCorrect>2)
            star3.setImageResource(R.drawable.star_full);
        else
            star3.setImageResource(R.drawable.star_empty);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Table_Ex_Listener){
            listener = (Fragment_Table_Ex_Listener) context;
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
