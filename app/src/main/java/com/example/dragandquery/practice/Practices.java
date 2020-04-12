package com.example.dragandquery.practice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.dragandquery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * - show ex depending on tut exp
 */

public class Practices extends AppCompatActivity {

    //coms
    Context context;
    LinearLayout layout;

    //vars
    private int complexity; //1 easy:6, 2 medium:8, 3 hard:12
    public static final String EX_ID = "com.example.dragandquery.practice.Practices.EX_ID";
    private List<Button> exercises;
    private boolean neededLections[][]; //todo lections on exercises if needed
    private String dones; //"0123102031"
    public static final String DEFAULT_EASY = "000000";
    public static final String DEFAULT_MEDIUM = "00000000";
    public static final String DEFAULT_HARD = "000000000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practices);

        //init stuff
        context = getApplicationContext();
        layout = (LinearLayout) findViewById(R.id.ll_practices_lections);
        exercises = new ArrayList<>();
        neededLections = new boolean[][]{
                {false, false, true, true},
                {false, false, true, true},
                {true, false, false, true}
        };

        //intent stuff
        Intent intent = getIntent();
        if(intent.hasExtra(Complexity.COMPLEXITY)){
            complexity = intent.getIntExtra(Complexity.COMPLEXITY, 1);
        }

        //choose exercises where matching lessons are already done + color match complexitiy + check already done exercises
        loadExercises();
        for(int i=0; i<exercises.size(); i++){
            exercises.get(i).setOnClickListener(new MyOnExerciseClickListener());
        }

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    //todo check if already done in tutorial
    public void loadExercises(){
        switch(complexity){
            case Complexity.EASY:
                setTitle(getString(R.string.title_prac_easy));
                dones = loadData(getString(R.string.prac_easy_key), DEFAULT_EASY);
                if(lectionsDone(1))
                    addExercise(0, getString(R.string.ex_easy_1));
                if(lectionsDone(2))
                    addExercise(1, getString(R.string.ex_easy_2));
                if(lectionsDone(3))
                    addExercise(2, getString(R.string.ex_easy_3));
                if(lectionsDone(4))
                    addExercise(3, getString(R.string.ex_easy_4));
                if(lectionsDone(5))
                    addExercise(4, getString(R.string.ex_easy_5));
                if(lectionsDone(6))
                    addExercise(5, getString(R.string.ex_easy_6));
                break;
            case Complexity.MEDIUM:
                setTitle(getString(R.string.title_prac_medium));
                dones = loadData(getString(R.string.prac_medium_key), DEFAULT_MEDIUM);
                break;
            case Complexity.HARD:
                setTitle(getString(R.string.title_prac_hard));
                dones = loadData(getString(R.string.prac_hard_key), DEFAULT_HARD);
                break;
        }
    }

    public int getColor(){
        switch(complexity){
            case Complexity.EASY:
                return R.color.easy;
            case Complexity.MEDIUM:
                return R.color.medium;
        }
        return R.color.hard;
    }

    //add a new exercise to every list and layout todo change view when already done
    public void addExercise(int id, String name){
        int numStars = Character.getNumericValue(dones.charAt(id));

        //vertical ll
        LinearLayout ll_v = new LinearLayout(context);
        ll_v.setBackground(getDrawable(R.drawable.btn_lection));
        ll_v.setOrientation(LinearLayout.VERTICAL);

        //horizontal ll for stars
        LinearLayout ll_h = new LinearLayout(context);
        ll_h.setOrientation(LinearLayout.HORIZONTAL);
        ll_h.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        ll_v.addView(ll_h, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        //3 stars
        ImageView star1 = new ImageView(context);
        if(numStars>0)
            star1.setImageResource(R.drawable.star_full);
        else
            star1.setImageResource(R.drawable.star_empty);
        ImageView star2 = new ImageView(context);
        if(numStars>1)
            star2.setImageResource(R.drawable.star_full);
        else
            star2.setImageResource(R.drawable.star_empty);
        ImageView star3 = new ImageView(context);
        if(numStars>2)
            star3.setImageResource(R.drawable.star_full);
        else
            star3.setImageResource(R.drawable.star_empty);
        ll_h.addView(star1);
        ll_h.addView(star2);
        ll_h.addView(star3);

        //button
        Button l = new Button(context);
        l.setText(name);
        l.setTextColor(getResources().getColor(getColor()));
        l.setBackgroundColor(Color.parseColor("#00000000"));
        l.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ll_v.addView(l, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        exercises.add(l);

        //add btn and space to ll
        layout.addView(ll_v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(new Space(context), 300, 15);

    }

    //todo
    public boolean lectionsDone(int ex){
        return true;
    }

    //key value store
    public void saveData(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    //key value store
    public String loadData(String key,  String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String data = sharedPref.getString(key, default_value);
        return data;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /***
     * listeenrs
     */

    public class MyOnExerciseClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Exercise.class);
            i.putExtra(EX_ID, complexity*100+exercises.indexOf(view)); //0-(n-1) + 100/200/300
            startActivity(i);
        }
    }
}
