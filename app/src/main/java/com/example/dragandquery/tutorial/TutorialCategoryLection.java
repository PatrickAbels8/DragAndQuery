package com.example.dragandquery.tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
 * -toolbar.set...
 */

public class TutorialCategoryLection
        extends AppCompatActivity
        implements
            Fragment_Feedback.Fragment_Feedback_Listener,
            Fragment_LectionContent_0101.Fragment_LectionContent_0101_Listener,
            Fragment_LectionContent_0102.Fragment_LectionContent_0102_Listener,
            Fragment_LectionContent_0103.Fragment_LectionContent_0103_Listener,
            Fragment_LectionContent_0104.Fragment_LectionContent_0104_Listener,
            Fragment_LectionContent_0105.Fragment_LectionContent_0105_Listener,
            Fragment_LectionContent_0106.Fragment_LectionContent_0106_Listener
{

    //fragments
    Fragment curFrag;
    Fragment_Feedback fragFeedback;
    Fragment_LectionContent_0101 fragLectionContent_0101;
    Fragment_LectionContent_0102 fragLectionContent_0102;
    Fragment_LectionContent_0103 fragLectionContent_0103;
    Fragment_LectionContent_0104 fragLectionContent_0104;
    Fragment_LectionContent_0105 fragLectionContent_0105;
    Fragment_LectionContent_0106 fragLectionContent_0106;

    //vars
    private String lection_id; //of type "01_03_05" when its the 3rd out of 5 lections in cat 01
    private int numCat; //todo
    private int numLec;
    private int numLecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_category_lection);

        //init coms and vars
        fragFeedback = new Fragment_Feedback();
        fragLectionContent_0101 = new Fragment_LectionContent_0101();
        fragLectionContent_0102 = new Fragment_LectionContent_0102();
        fragLectionContent_0103 = new Fragment_LectionContent_0103();
        fragLectionContent_0104 = new Fragment_LectionContent_0104();
        fragLectionContent_0105 = new Fragment_LectionContent_0105();
        fragLectionContent_0106 = new Fragment_LectionContent_0106();

        //intent stuff
        Intent i = getIntent();
        if(i.hasExtra(TutorialCategory.LECTION_ID)){
            lection_id = i.getStringExtra(TutorialCategory.LECTION_ID);
            String[] components = lection_id.split("_");
            numCat = Integer.parseInt(components[0]);
            numLec = Integer.parseInt(components[1]);
            numLecs = Integer.parseInt(components[2]);
        }

        //checkout current lection
        if(lection_id.substring(0, 5).equals("01_01")){
            curFrag = fragLectionContent_0101;
        }else if(lection_id.substring(0, 5).equals("01_02")){
            curFrag = fragLectionContent_0102;
        }else if(lection_id.substring(0, 5).equals("01_03")){
            curFrag = fragLectionContent_0103;
        }else if(lection_id.substring(0, 5).equals("01_04")){
            curFrag = fragLectionContent_0104;
        }else if(lection_id.substring(0, 5).equals("01_05")){
            curFrag = fragLectionContent_0105;
        }else if(lection_id.substring(0, 5).equals("01_06")){
            curFrag = fragLectionContent_0106;
        }

        //open current lection
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_lectionFeedback, fragFeedback)
                .replace(R.id.container_lectionContent, curFrag)
                .commit();
    }

    @Override
    public void onBack() {
        fragFeedback.goInvisible();
        if(lection_id.substring(0, 5).equals("01_01")){
            fragLectionContent_0101.goClickable();
        }else if(lection_id.substring(0, 5).equals("01_02")){
            fragLectionContent_0102.goClickable();
        }
    }

    @Override
    public void onForth() {
        //go forth to next lection if there is one, if not back to cat overview
        if(hasNextLection()){
            Intent i = new Intent(getApplicationContext(), TutorialCategoryLection.class);
            i.putExtra(TutorialCategory.LECTION_ID, getNextLectionID());
            startActivity(i);
        }else{
            Intent i2 = new Intent(getApplicationContext(), TutorialCategory.class);
            i2.putExtra(Tutorial.CAT_ID, numCat);
            i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i2);
        }
    }

    @Override
    public void onGo(boolean isCorrect) {
        fragFeedback.goVisible(isCorrect);

        if(isCorrect){
            setLectionDone();
        }

        /*if(lection_id.substring(0, 4).equals("01_01")){
            fragLectionContent_0101.goInclickable();
        }else if(lection_id.substring(0, 4).equals("01_02")){
            fragLectionContent_0102.goInclickable();
        }else if(lection_id.substring(0, 4).equals("01_03")){
            fragLectionContent_0103.goInclickable();
        }else if(lection_id.substring(0, 4).equals("01_04")){
            fragLectionContent_0104.goInclickable();
        }else if(lection_id.substring(0, 4).equals("01_05")){
            fragLectionContent_0105.goInclickable();
        }else if(lection_id.substring(0, 4).equals("01_06")){
            fragLectionContent_0106.goInclickable();
        }*/
    }

    public void setLectionDone(){
        String exp_key = "";
        String exp_unlocked_key = "";
        if (numCat == 1) {
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
        //Log.d("############ ach -> exp: numDone: ", Integer.toString(numDone));
        //Log.d("############ ach -> exp: numTotal: ", Integer.toString(numTotal));
        //Log.d("############ ach -> exp: exp: ", Integer.toString(exp));
        return exp;
    }

    //when 3 out of 5 lections are unlocked or done, saved unlocked exp should be 61
    public int unlockedachievementsToExperience(){
        String[] counts = lection_id.split("_");
        int numUnlocked = Integer.parseInt(counts[1])+1;
        int numTotal = Integer.parseInt(counts[2]);
        int exp = 100*numUnlocked/numTotal +1;
        //Log.d("############ un.ach -> exp: numDone: ", Integer.toString(numUnlocked));
        //Log.d("############ un.ach -> exp: numTotal: ", Integer.toString(numTotal));
        //Log.d("############ un.ach -> exp: exp: ", Integer.toString(exp));
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

    //check whether you are the last lection of your category
    public boolean hasNextLection(){
        return !(numLec == numLecs);
    }

    public String getNextLectionID(){
        String[] coms = lection_id.split("_");
        String nextLectionID = coms[0]+"_";
        int newLec = numLec +1;
        nextLectionID +=  newLec>9? Integer.toString(newLec): "0"+Integer.toString(newLec);
        nextLectionID += "_"+coms[2];
        return nextLectionID;
    }

}
