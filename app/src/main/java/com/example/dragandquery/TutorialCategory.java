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
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.Arrays;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * one activity, several fragments. one freagment for each category, each containing a list of lections
 */
public class TutorialCategory extends AppCompatActivity {

    //coms
    Button[] cats;
    ImageButton reset;

    //vars
    private int[] exps;
    private int num_cats = 5;
    private int cat_id; //1-5
    private int cat_exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_category);

        //intent stuff
        Intent intent = getIntent();

        //init coms&vars
        exps = new int[]{
                loadData(getString(R.string.tutScore1_key), 10),
                loadData(getString(R.string.tutScore2_key), 20),
                loadData(getString(R.string.tutScore3_key), 30),
                loadData(getString(R.string.tutScore4_key), 40),
                loadData(getString(R.string.tutScore5_key), 50)};
        cats = new Button[]{
                (Button) findViewById(R.id.btn1),
                (Button) findViewById(R.id.btn2),
                (Button) findViewById(R.id.btn3),
                (Button) findViewById(R.id.btn4),
                (Button) findViewById(R.id.btn5)};
        reset = (ImageButton) findViewById(R.id.reset);

        //show which category is currently open
        if(intent.hasExtra(Tutorial.CAT_ID)){
            cat_id = intent.getIntExtra(Tutorial.CAT_ID, 1);
            cat_exp = exps[cat_id-1];
            cats[cat_id-1].setAlpha(0.7f);
        }

        //show exps for testing
        for(int i=0; i<num_cats; i++){
            cats[i].setText(Integer.toString(exps[i]));
        }

        //what if someone advanced their experience on some category
        for(int i=0; i<num_cats; i++){
            cats[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cat_name = view.getResources().getResourceName(view.getId());
                    int btn_id = Integer.parseInt(Character.toString(cat_name.charAt(cat_name.length()-1))) -1; //0-4
                    int new_exp = Integer.parseInt(cats[btn_id].getText().toString())+10;
                    cats[btn_id].setText(Integer.toString(new_exp));
                    exps[btn_id] = new_exp;
                    switch (btn_id){
                        case 0:
                            saveData(getString(R.string.tutScore1_key), new_exp);
                            break;
                        case 1:
                            saveData(getString(R.string.tutScore2_key), new_exp);
                            break;
                        case 2:
                            saveData(getString(R.string.tutScore3_key), new_exp);
                            break;
                        case 3:
                            saveData(getString(R.string.tutScore4_key), new_exp);
                            break;
                        case 4:
                            saveData(getString(R.string.tutScore5_key), new_exp);
                            break;
                    }
                }
            });
        }

        //reset exps
        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for(int i=0; i<num_cats; i++){
                    cats[i].setText("0");
                    exps[i] = 0;
                }
                saveData(getString(R.string.tutScore1_key), 0);
                saveData(getString(R.string.tutScore2_key), 0);
                saveData(getString(R.string.tutScore3_key), 0);
                saveData(getString(R.string.tutScore4_key), 0);
                saveData(getString(R.string.tutScore5_key), 0);
                return true;
            }
        });

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

    //key value store
    public void saveData(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
        Toast.makeText(getApplicationContext(), "saved _"+data+"_ under _"+key, Toast.LENGTH_SHORT).show();
    }

    //key value store
    public int loadData(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int data = sharedPref.getInt(key, default_value);
        return data;
    }
}
