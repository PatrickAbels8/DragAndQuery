package com.example.dragandquery.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dragandquery.R;
import com.example.dragandquery.tutorial.Fragment_Feedback;

/***
 * TODO
 * - feedback:
 *      - leading to next lection
 *      - save new achievement
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
    private String lection_id; //of type "01_03"

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

        if(lection_id.equals("01_01")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_lectionFeedback, fragFeedback)
                    .replace(R.id.container_lectionContent, fragLectionContent_0101)
                    .commit();
        }


    }

    @Override
    public void onBack() {
        fragFeedback.goInvisible();
        if(lection_id.equals("01_01")){
            fragLectionContent_0101.goClickable();
        }

    }

    @Override
    public void onForth() {
        //todo open frag of next lection (start this with intent lection_id+1)
    }

    @Override
    public void onGo(boolean isCorrect) {
        //todo save lection_id as done and the next lection as unlocked
        fragFeedback.goVisible(isCorrect);
        if(lection_id.equals("01_01")){
            fragLectionContent_0101.goInclickable();
        }

    }

}
