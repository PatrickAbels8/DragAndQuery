package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;


/***
 * TODO:
 * - when sclaing 0.5 in on some entity show its informtion
 */

public class DbView extends AppCompatActivity {

    private PhotoView db_view;
    private TextView db_title_school;
    private TextView db_title_cafetarie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        db_view = (PhotoView) findViewById(R.id.db_view);
        db_title_school = (TextView) findViewById(R.id.db_title_school);
        db_title_cafetarie = (TextView) findViewById(R.id.db_title_cafetaria);

        db_title_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_title_school.setBackground(getResources().getDrawable(R.drawable.border_white));
                db_title_cafetarie.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                db_view.setImageResource(R.drawable.er_school_transparent);
            }
        });

        db_title_cafetarie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                db_title_cafetarie.setBackground(getResources().getDrawable(R.drawable.border_white));
                db_view.setImageResource(R.drawable.er_school_white);
            }
        });

    }
}
