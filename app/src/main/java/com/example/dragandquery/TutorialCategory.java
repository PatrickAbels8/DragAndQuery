package com.example.dragandquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * one activity, several fragments. one freagment for each category, each containing a list of lections
 */
public class TutorialCategory extends AppCompatActivity {

    //coms
    Button[] cats;

    //vars
    public static final String CAT1_EXP = "com.example.dragandquery.TutorialCategory.CAT1_EXP";
    public static final String CAT2_EXP = "com.example.dragandquery.TutorialCategory.CAT2_EXP";
    public static final String CAT3_EXP = "com.example.dragandquery.TutorialCategory.CAT3_EXP";
    public static final String CAT4_EXP = "com.example.dragandquery.TutorialCategory.CAT4_EXP";
    public static final String CAT5_EXP = "com.example.dragandquery.TutorialCategory.CAT5_EXP";
    private int[] exps;
    private int num_cats = 5;
    private int cat_id;
    private int cat_exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_category);

        //intent stuff
        Intent intent = getIntent();


        //init coms&vars
        exps = new int[num_cats];
        cats = new Button[num_cats];
        cats[0] = (Button) findViewById(R.id.btn1);
        cats[1] = (Button) findViewById(R.id.btn2);
        cats[2] = (Button) findViewById(R.id.btn3);
        cats[3] = (Button) findViewById(R.id.btn4);
        cats[4] = (Button) findViewById(R.id.btn5);

        if(intent.hasExtra(Tutorial.CAT_ID)){
            cat_id = intent.getIntExtra(Tutorial.CAT_ID, 1);
            String d = Integer.toString(cat_exp);
            Log.d("##############################", d);
        }
        if(intent.hasExtra(Tutorial.CAT_EXP)){
            cat_exp = intent.getIntExtra(Tutorial.CAT_EXP, 1);
            String s = Integer.toString(cat_exp);
            Log.d("##############################", s);
        }

        //key value store
        for(int i=0; i<num_cats; i++){
            if(i==cat_id){
                cats[i].setText(cat_exp); //error
            }else{
                cats[i].setText("-1");
            }
        }

        //testing

        for(int i=0; i<num_cats; i++){
            cats[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cat_name = view.getResources().getResourceName(view.getId());
                    int cat_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))) -1;
                    int new_exp = Integer.parseInt(cats[cat_id].getText().toString())+1;
                    cats[cat_id].setText(new_exp);
                    exps[cat_id] = new_exp;
                }
            });
        }


        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), Tutorial.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //todo maybe wrong flag
        i.putExtra(CAT1_EXP, exps[0]);
        i.putExtra(CAT2_EXP, exps[1]);
        i.putExtra(CAT3_EXP, exps[2]);
        i.putExtra(CAT4_EXP, exps[3]);
        i.putExtra(CAT5_EXP, exps[4]);
        startActivity(i);
    }
}
