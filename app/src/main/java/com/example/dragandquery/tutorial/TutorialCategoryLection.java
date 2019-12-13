package com.example.dragandquery.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dragandquery.R;
import com.example.dragandquery.Tutorial;
import com.example.dragandquery.tutorial.Fragment_Feedback;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 * TODO
 * - feedback:
 *      - leading to next lection
 * - more than one lection (implements, fragments, declaration of fragment, Container manager, onBack, onForth, onGo)
 */

public class TutorialCategoryLection
        extends AppCompatActivity
        implements Fragment_LectionContent_0101.Fragment_LectionContent_0101_Listener,
        Fragment_Feedback.Fragment_Feedback_Listener{

    //fragments
    Fragment_Feedback fragFeedback;
    Fragment_LectionContent_0101 fragLectionContent_0101;

    //vars
    private String lection_id; //of type "01_03_05" when its the 3rd out of 5 lections in cat 01

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_category_lection);

        fragFeedback = new Fragment_Feedback();
        fragLectionContent_0101 = new Fragment_LectionContent_0101();

        Intent i = getIntent();
        if(i.hasExtra(TutorialCategory.LECTION_ID)){
            lection_id = i.getStringExtra(TutorialCategory.LECTION_ID);
        }

        if(lection_id.substring(0, 5).equals("01_01")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_lectionFeedback, fragFeedback)
                    .replace(R.id.container_lectionContent, fragLectionContent_0101)
                    .commit();
        }


    }

    @Override
    public void onBack() {
        fragFeedback.goInvisible();
        if(lection_id.substring(0, 5).equals("01_01")){
            fragLectionContent_0101.goClickable();
        }
    }

    @Override
    public void onForth() {
        //todo open frag of next lection (start this with intent lection_id+1)
        Intent i = new Intent(getApplicationContext(), TutorialCategory.class);
        i.putExtra(Tutorial.CAT_ID, Integer.parseInt(lection_id.substring(0, 2)));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onGo(boolean isCorrect) {
        fragFeedback.goVisible(isCorrect);

        if(isCorrect){
            setLectionDone();
        }

        if(lection_id.substring(0, 4).equals("01_01")){
            fragLectionContent_0101.goInclickable();
        }
    }

    public void setLectionDone(){
        String exp_key = "";
        String exp_unlocked_key = "";
        if (lection_id.substring(0, 2).equals("01")) {
            exp_key += getString(R.string.tutScore1_key);
            exp_unlocked_key += getString(R.string.tutScore1_unlocked_key);
        }
        saveData(exp_key, achievementsToExperience());
        saveData(exp_unlocked_key, unlockedachievementsToExperience());
    }

    //when 3 out of 5 lections are done, saved exp should be 61
    public int achievementsToExperience(){
        String[] counts = lection_id.split("_");
        int numDone = Integer.parseInt(counts[1]);
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numDone/numTotal +1;
        Log.d("############ ach -> exp: numDone: ", Integer.toString(numDone));
        Log.d("############ ach -> exp: numTotal: ", Integer.toString(numTotal));
        Log.d("############ ach -> exp: exp: ", Integer.toString(exp));
        return exp;
    }

    //when 3 out of 5 lections are unlocked or done, saved unlocked exp should be 61
    public int unlockedachievementsToExperience(){
        String[] counts = lection_id.split("_");
        int numUnlocked = Integer.parseInt(counts[1])+1;
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numUnlocked/numTotal +1;
        Log.d("############ un.ach -> exp: numDone: ", Integer.toString(numUnlocked));
        Log.d("############ un.ach -> exp: numTotal: ", Integer.toString(numTotal));
        Log.d("############ un.ach -> exp: exp: ", Integer.toString(exp));
        return exp;
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
