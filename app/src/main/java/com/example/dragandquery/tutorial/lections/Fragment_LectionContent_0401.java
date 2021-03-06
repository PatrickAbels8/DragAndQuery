package com.example.dragandquery.tutorial.lections;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dragandquery.DbView;
import com.example.dragandquery.R;
import com.example.dragandquery.tutorial.Fragment_Content;
import com.github.chrisbanes.photoview.PhotoView;

/***
 * - 
 */

public class Fragment_LectionContent_0401 extends Fragment_Content {

    //coms
    private RelativeLayout rl_exercise;
    private Button btn_go;
    private TextView text;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private ImageView btn_db;
    private LinearLayout db_view;
    private PhotoView db_img;
    private TextView title_school;
    private TextView title_legend;

    //vars
    private Fragment_LectionContent_0401_Listener listener;
    public Context context;
    private boolean db_open = false;

    //interface
    public interface Fragment_LectionContent_0401_Listener{
        void onGo(boolean isCorrect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lectioncontent_0401, container, false);

        //init coms
        rl_exercise = v.findViewById(R.id.frag_lectioncontent_0401);
        btn_go = v.findViewById(R.id.btn_lectioncontent_0401_go);
        context = getContext();
        text = v.findViewById(R.id.tv_c4l1);
        rg = v.findViewById(R.id.rg_c4l1);
        rb1 = v.findViewById(R.id.rb1_c4l1);
        rb2 = v.findViewById(R.id.rb2_c4l1);
        rb3 = v.findViewById(R.id.rb3_c4l1);

        btn_db = v.findViewById(R.id.frag_db);
        db_view = v.findViewById(R.id.db_view);
        db_img = v.findViewById(R.id.db_img);
        title_school = v.findViewById(R.id.db_title_school);
        title_legend = v.findViewById(R.id.db_title_legend);
        hideDB();

        //exercise mode
        rg.clearCheck();
        btn_go.setOnClickListener((View view) -> listener.onGo(verifyAnswer()));

        btn_db.setOnClickListener(new Fragment_LectionContent_0401.MyDBClickListener());
        title_school.setOnClickListener(new Fragment_LectionContent_0401.SchoolListener());
        title_legend.setOnClickListener(new Fragment_LectionContent_0401.LegendListener());

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
        if(context instanceof Fragment_LectionContent_0401_Listener){
            listener = (Fragment_LectionContent_0401_Listener) context;
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

    public class MyDBClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(db_open){
                hideDB();
            }else{
                showDB();
            }
        }
    }

    public class SchoolListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            title_school.setBackground(getResources().getDrawable(R.drawable.border_white));
            title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_img.setImageResource(DbView.DB_SCHOOL_ER);
        }
    }

    public class LegendListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            title_legend.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_img.setImageResource(DbView.DB_LEGEND);
        }
    }

    public void showDB(){
        db_view.setVisibility(View.VISIBLE);
        text.setVisibility(View.INVISIBLE);
        rg.setVisibility(View.INVISIBLE);
        db_open = true;
    }

    public void hideDB(){
        db_view.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        rg.setVisibility(View.VISIBLE);
        db_open = false;
    }
}
