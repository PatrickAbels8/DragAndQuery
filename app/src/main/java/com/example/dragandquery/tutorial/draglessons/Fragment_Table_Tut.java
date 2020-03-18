package com.example.dragandquery.tutorial.draglessons;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
 */

public class Fragment_Table_Tut extends Fragment {

    //coms
    private ConstraintLayout cl_table;
    private Button btn_back;
    private Button btn_forth;
    private TextView raw_query;
    private Animation frombottom;
    private Animation tobottom;
    private Context context;

    //vars
    private Fragment_Table_Tut_Listener listener;
    private String lec_id;

    //interface
    public interface Fragment_Table_Tut_Listener{
        void onBack();
        void onForth();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table_tut, container, false);

        //init coms
        context = getContext();
        cl_table = v.findViewById(R.id.frag_table);
        btn_back = v.findViewById(R.id.btn_feedback_back);
        btn_forth = v.findViewById(R.id.btn_feedback_forth);
        raw_query = v.findViewById(R.id.raw_query);
        frombottom = AnimationUtils.loadAnimation(context, R.anim.frombottom);
        tobottom = AnimationUtils.loadAnimation(context, R.anim.tobottom);

        //load lection id
        lec_id = this.getArguments().getString(DragLesson.ID_KEY);

        //get back to edit or forth to next lec
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBack();
            }
        });
        btn_forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onForth();
            }
        });


        return v;
    }

    public void goInvisible(){
        cl_table.startAnimation(tobottom);
        cl_table.setVisibility(View.INVISIBLE);
    }

    //todo good or bad feedback
    public void goVisible(String query, String response, boolean isCorrect){
        cl_table.setVisibility(View.VISIBLE);
        fillTable(response);
        cl_table.startAnimation(frombottom);
        raw_query.setText(query);
        if(isCorrect)
            btn_forth.setVisibility(View.VISIBLE);
        else
            btn_forth.setVisibility(View.INVISIBLE);

    }

    public void fillTable(String response){
        //todo fill table
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Fragment_Table_Tut_Listener){
            listener = (Fragment_Table_Tut_Listener) context;
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
