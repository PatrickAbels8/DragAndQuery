package com.example.dragandquery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * TODO
 * -
 */
public class LoginActivity extends AppCompatActivity {

    //vars
    public static String UNAME = "com.example.dragandquery.LoginActivity.UNAME";
    public static String UMAIL = "com.example.dragandquery.LoginActivity.UMAIL";


    //coms
    Button go;
    EditText name;
    EditText mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //components
        name = (EditText) findViewById(R.id.et_loginName);
        mail = (EditText) findViewById(R.id.et_loginMail);
        go = (Button) findViewById(R.id.btn_go);

        //lets go back to navigation
        go.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), "Please enter again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
