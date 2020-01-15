package com.example.dragandquery.practice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * TODO:
 * - anything just like tut cat, but: ex to show depending on tut exp + key store bitstring "1100101" for done exs
 */

public class Practices extends AppCompatActivity {

    //coms
    Context context;
    LinearLayout layout;

    //vars
    private int complexity; //1 easy, 2 medium, 3 hard
    public static final String EX_ID = "com.example.dragandquery.practice.Practices.EX_ID";
    private List<Button> exercises;
    private boolean neededLections[][]; //todo lections on exercises if needed
    private String dones; //todo not possible right now but later "1101001101"

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
                if(lectionsDone(1))
                    addExercise(getString(R.string.ex_easy_1));
                if(lectionsDone(2))
                    addExercise(getString(R.string.ex_easy_2));
                if(lectionsDone(3))
                    addExercise(getString(R.string.ex_easy_3));
                if(lectionsDone(4))
                    addExercise(getString(R.string.ex_easy_4));
                break;
            case Complexity.MEDIUM:
                setTitle(getString(R.string.title_prac_medium));
                break;
            case Complexity.HARD:
                setTitle(getString(R.string.title_prac_hard));
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
    public void addExercise(String name){
        //create exercise btn
        Button l = new Button(context);
        l.setText(name);
        l.setTextColor(getResources().getColor(getColor()));
        l.setBackground(getDrawable(R.drawable.btn_lection));
        l.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        exercises.add(l);

        //add btn and space to ll
        layout.addView(l, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(new Space(context), 300, 15);

    }

    //todo
    public boolean lectionsDone(int ex){
        return new Random().nextBoolean();
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

    public class MyOnExerciseClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Exercise.class);
            i.putExtra(EX_ID, complexity*100+exercises.indexOf(view)); //0-(n-1) + 100/200/300
            startActivity(i);
        }
    }
}
