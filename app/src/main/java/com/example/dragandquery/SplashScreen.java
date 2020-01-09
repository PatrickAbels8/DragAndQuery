package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/***
 * TODO:
 * -bird from dropbox
 */
public class SplashScreen extends AppCompatActivity {

    //coms
    private ImageView bird;
    private TextView text;
    private AlphaAnimation appear;
    private Animation from_bottom;

    //vars
    private static final int SPLASH_TIME_OUT = 4000;
    private static final int APPEAR_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        text = (TextView) findViewById(R.id.splash_text);
        appear = new AlphaAnimation(0.0f, 1.0f);
        appear.setDuration(APPEAR_TIME);
        appear.setFillAfter(true);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        AnimationSet anims = new AnimationSet(false);
        anims.addAnimation(appear);
        anims.addAnimation(from_bottom);

        text.startAnimation(anims);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Navigation.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
