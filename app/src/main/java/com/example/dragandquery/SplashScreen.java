package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;


/***
 * TODO:
 * -anim bugs
 */
public class SplashScreen extends AppCompatActivity {

    //coms
    private ImageView bird;
    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;

    //vars
    private static final int DOT_LOAD_TIME = 750;
    private static final int SPLASH_TIME_OUT = 3000;
    private static final int BIRD_APPEAR_TIME = SPLASH_TIME_OUT/2;
    private static final int BIRD_DISAPPEAR_TIME = SPLASH_TIME_OUT/2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bird = (ImageView) findViewById(R.id.splash_bird);
        dot1 = (ImageView) findViewById(R.id.splash_dot1);
        dot2 = (ImageView) findViewById(R.id.splash_dot2);
        dot3 = (ImageView) findViewById(R.id.splash_dot3);

        //animateBird();
        animateDots();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Navigation.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    public void animateBird(){
        dot1.setVisibility(View.INVISIBLE);
        dot2.setVisibility(View.INVISIBLE);
        dot3.setVisibility(View.INVISIBLE);

        bird.setAlpha(0f);
        bird.setVisibility(View.VISIBLE);
        bird.animate()
                .alpha(1f)
                .setDuration(BIRD_APPEAR_TIME)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bird.setVisibility(View.VISIBLE);
                    }
                });
        bird.setAlpha(1f);
        bird.animate()
                .alpha(0f)
                .setDuration(BIRD_DISAPPEAR_TIME)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bird.setVisibility(View.GONE);
                    }
                });
    }

    public void animateDots(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dot1.setImageResource(R.drawable.full_dot);
            }
        }, DOT_LOAD_TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dot2.setImageResource(R.drawable.full_dot);
            }
        }, 2*DOT_LOAD_TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dot3.setImageResource(R.drawable.full_dot);
            }
        }, 3*DOT_LOAD_TIME);
    }
}
