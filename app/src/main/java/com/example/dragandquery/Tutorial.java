package com.example.dragandquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO:
 * - shared pref for each category
 * - pb+tv clicklistener to open overview on corresponding lections
 */

public class Tutorial extends AppCompatActivity {

    //coms
    ProgressBar[] cats;

    //vars
    private final static int num_cats = 5;
    private int[] cats_exp;
    public final static String CAT_ID = "com.example.dragandquery.Tutorial.CAT_ID";
    public final static String CAT_EXP = "com.example.dragandquery.Tutorial.CAT_EXP";
    public final static String EXP_AVG = "com.example.dragandquery.Tutorial.EXP_AVG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //intent stuff
        Intent intent = getIntent();

        //init coms
        cats_exp = new int[num_cats];
        cats = new ProgressBar[num_cats];
        cats[0] = (ProgressBar) findViewById(R.id.pb_cat1); //Rel DB
        cats[1] = (ProgressBar) findViewById(R.id.pb_cat2); //Anfragen
        cats[2] = (ProgressBar) findViewById(R.id.pb_cat3); //Aggreg. fkt.
        cats[3] = (ProgressBar) findViewById(R.id.pb_cat4); //Join
        cats[4] = (ProgressBar) findViewById(R.id.pb_cat5); //Verschachtelte Anfragen

        //key value store
        if(intent.hasExtra(TutorialCategory.CAT1_EXP)){
            saveData(getString(R.string.tutCat1_key), intent.getIntExtra(TutorialCategory.CAT1_EXP, 0));
        }
        if(intent.hasExtra(TutorialCategory.CAT2_EXP)){
            saveData(getString(R.string.tutCat2_key), intent.getIntExtra(TutorialCategory.CAT2_EXP, 0));
        }
        if(intent.hasExtra(TutorialCategory.CAT3_EXP)){
            saveData(getString(R.string.tutCat3_key), intent.getIntExtra(TutorialCategory.CAT3_EXP, 0));
        }
        if(intent.hasExtra(TutorialCategory.CAT4_EXP)){
            saveData(getString(R.string.tutCat4_key), intent.getIntExtra(TutorialCategory.CAT4_EXP, 0));
        }
        if(intent.hasExtra(TutorialCategory.CAT5_EXP)){
            saveData(getString(R.string.tutCat5_key), intent.getIntExtra(TutorialCategory.CAT5_EXP, 0));
        }

        cats_exp[0] = loadData(getString(R.string.tutCat1_key), 10);
        cats_exp[1] = loadData(getString(R.string.tutCat2_key), 20);
        cats_exp[2] = loadData(getString(R.string.tutCat3_key), 30);
        cats_exp[3] = loadData(getString(R.string.tutCat4_key), 40);
        cats_exp[4] = loadData(getString(R.string.tutCat5_key), 50);

        for(int i=0; i<num_cats; i++){
            cats[i].setProgress(cats_exp[i]);
        }

        for(int i=0; i<num_cats; i++){
            cats[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(getApplicationContext(), TutorialCategory.class);
                    String cat_name = view.getResources().getResourceName(view.getId());
                    int cat_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))) -1;
                    intent1.putExtra(CAT_ID, cat_id);
                    intent1.putExtra(CAT_EXP, cats_exp[cat_id]);
                    startActivity(intent1);
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
    }

    public int calcTutorialExp(int[] partialExps){
        int num_cats = partialExps.length;
        int sum = 0;
        for (int partialExp : partialExps) {
            sum += partialExp;
        }
        return sum/num_cats;
    }

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_LONG).show();
    }

    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), Navigation.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //todo maybe wrong flag
        i.putExtra(EXP_AVG, calcTutorialExp(cats_exp));
        startActivity(i);
    }
}
