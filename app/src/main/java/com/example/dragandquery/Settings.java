package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/***
 * TODO
 * -global tutrial reset
 * - change image
 * - tv to button with borders
 */
public class Settings extends AppCompatActivity {

    //vars
    public static String UNAME = "com.example.dragandquery.Settings.UNAME";
    public static String UMAIL = "com.example.dragandquery.Settings.UMAIL";

    //coms
    private TextView save;
    private EditText name;
    private EditText mail;
    private TextView resetTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //components
        name = (EditText) findViewById(R.id.settings_name);
        mail = (EditText) findViewById(R.id.settings_mail);
        save = (TextView) findViewById(R.id.save_settings);
        resetTutorial = (TextView) findViewById(R.id.tv_reset_tutorial);

        //lets go back to navigation
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userMail = mail.getText().toString();

                if(true){
                    Intent i = new Intent(getApplicationContext(), Navigation.class);
                    i.putExtra(UNAME, userName);
                    i.putExtra(UMAIL, userMail);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Oops! Da hat wohl etwas noch nicht gestimmt!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //reset the whole tutorial exp
        resetTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo popup asks again
                //todo when popup accepted reset whole tutorial
            }
        });
    }


}
