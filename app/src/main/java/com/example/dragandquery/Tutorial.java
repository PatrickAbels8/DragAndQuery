package com.example.dragandquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dragandquery.tutorial.TutorialCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - add nd
 * - pb+tv clicklistener to open overview on corresponding lections
 * -addFlag so that sp is updated even whne pressed back (if needed via override onBackPressed
 */

public class Tutorial extends AppCompatActivity {

    //coms
    ProgressBar[] cats;
    TextView[] titles;

    //vars
    private final static int num_cats = 5;
    private int[] cats_exp;
    public final static String CAT_ID = "com.example.dragandquery.Tutorial.CAT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //intent stuff
        Intent intent = getIntent();

        //init coms
        cats_exp = new int[]{
                loadData(getString(R.string.tutScore1_key), 10),
                loadData(getString(R.string.tutScore2_key), 20),
                loadData(getString(R.string.tutScore3_key), 30),
                loadData(getString(R.string.tutScore4_key), 40),
                loadData(getString(R.string.tutScore5_key), 50)};
        cats = new ProgressBar[]{
                (ProgressBar) findViewById(R.id.pb_cat1), //Rel DB
                (ProgressBar) findViewById(R.id.pb_cat2), //Anfragen
                (ProgressBar) findViewById(R.id.pb_cat3), //Aggreg. fkt.
                (ProgressBar) findViewById(R.id.pb_cat4), //Join
                (ProgressBar) findViewById(R.id.pb_cat5)};//Verschachtelte Anfragen
        titles = new TextView[]{
                (TextView) findViewById(R.id.tv_title_cat1),
                (TextView) findViewById(R.id.tv_title_cat2),
                (TextView) findViewById(R.id.tv_title_cat3),
                (TextView) findViewById(R.id.tv_title_cat4),
                (TextView) findViewById(R.id.tv_title_cat5)};

        for(int i=0; i<num_cats; i++){
            cats[i].setProgress(cats_exp[i]);
        }

        for(int i=0; i<num_cats; i++){
            cats[i].setOnClickListener(new Tutorial.OnClickListener());
            titles[i].setOnClickListener(new Tutorial.OnClickListener());
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
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(view.getContext(), TutorialCategory.class);
            String cat_name = view.getResources().getResourceName(view.getId());
            int cat_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))); //1-5
            intent1.putExtra(CAT_ID, cat_id);
            startActivity(intent1);
        }
    }
}
