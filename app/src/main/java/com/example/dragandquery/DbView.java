package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;


/***
 * TODO:
 * - when sclaing 0.5 in on some entity show its informtion
 */

public class DbView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        PhotoView db = (PhotoView) findViewById(R.id.db_view);
    }
}
