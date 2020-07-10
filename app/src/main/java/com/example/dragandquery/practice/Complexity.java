package com.example.dragandquery.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dragandquery.R;

public class Complexity extends AppCompatActivity {

    //coms
    private TextView easy;
    private TextView medium;
    private TextView hard;

    //vars
    public static final String COMPLEXITY = "com.example.dragandquery.practice.Complexity.COMPLEXITY";
    public static final int EASY = 1;
    public static final int MEDIUM = 2;
    public static final int HARD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complexity);

        //init stuff
        easy = findViewById(R.id.prac_easy);
        medium = findViewById(R.id.prac_medium);
        hard = findViewById(R.id.prac_hard);

        //tag for listener
        easy.setTag(EASY);
        medium.setTag(MEDIUM);
        hard.setTag(HARD);

        //open practiceswith the right complexity
        easy.setOnClickListener(new MyOnClickListener());
        medium.setOnClickListener(new MyOnClickListener());
        hard.setOnClickListener(new MyOnClickListener());

    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Practices.class);
            i.putExtra(COMPLEXITY, (int)view.getTag());
            startActivity(i);

        }
    }
}
