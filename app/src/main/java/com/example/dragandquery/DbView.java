package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;


/***
 */

public class DbView extends AppCompatActivity {

    private PhotoView db_view;
    private TextView db_title_school;
    private TextView db_title_cafeteria;
    private TextView db_title_legend;

    public static final int DB_SCHOOL_ER = R.drawable.er_school_colourful_background;
    public static final int DB_CAFETERIA_ER = R.drawable.er_cafeteria_colourfull_background;
    public static final int DB_LEGEND = R.drawable.legende_er_bunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        db_view = findViewById(R.id.db_view);
        db_title_school = findViewById(R.id.db_title_school);
        db_title_cafeteria = findViewById(R.id.db_title_cafetaria);
        db_title_legend = findViewById(R.id.db_title_legend);


        db_title_school.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_view.setImageResource(DB_SCHOOL_ER);
        });

        db_title_cafeteria.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_view.setImageResource(DbView.DB_CAFETERIA_ER);
        });

        db_title_legend.setOnClickListener(view -> {
            db_title_school.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_cafeteria.setBackground(getResources().getDrawable(R.drawable.border_transparent));
            db_title_legend.setBackground(getResources().getDrawable(R.drawable.border_white));
            db_view.setImageResource(DbView.DB_LEGEND);
        });

    }
}
