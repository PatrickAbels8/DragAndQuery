package com.example.dragandquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dragandquery.tutorial.TutorialCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - add nd (copy navigation class, include nav layout but this layout, in nav layout include this layout)
 * -
 */

public class Tutorial extends AppCompatActivity {

    //coms
    ProgressBar[] cats;
    TextView[] titles;

    //vars
    private final static int num_cats = 4;
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
                loadData(getString(R.string.tutScore4_key), 40)
        };

        Log.d("###############exp: ", Integer.toString(cats_exp[0]));
        cats = new ProgressBar[]{
                (ProgressBar) findViewById(R.id.pb_cat1), //Rel DB
                (ProgressBar) findViewById(R.id.pb_cat2), //Anfragen
                (ProgressBar) findViewById(R.id.pb_cat3), //Aggreg. fkt.
                (ProgressBar) findViewById(R.id.pb_cat4)}; //Join
        titles = new TextView[]{
                (TextView) findViewById(R.id.tv_title_cat1),
                (TextView) findViewById(R.id.tv_title_cat2),
                (TextView) findViewById(R.id.tv_title_cat3),
                (TextView) findViewById(R.id.tv_title_cat4)};

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_SHORT).show();
    }

    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(view.getContext(), TutorialCategory.class);
            String cat_name = view.getResources().getResourceName(view.getId());
            int cat_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))); //1-4
            intent1.putExtra(CAT_ID, cat_id);
            startActivity(intent1);
        }
    }
}
