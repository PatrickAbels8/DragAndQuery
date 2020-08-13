package com.example.dragandquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragandquery.practice.Complexity;
import com.example.dragandquery.practice.Practices;
import com.google.android.material.navigation.NavigationView;

import static com.example.dragandquery.Navigation.SHARED_PREFS;

/***
 */
public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    //vars
    public static String UNAME = "com.example.dragandquery.Settings.UNAME";
    public static String UMAIL = "com.example.dragandquery.Settings.UMAIL";
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //coms
    private TextView save;
    private EditText name;
    private EditText mail;
    private ProgressBar pb_tut;
    private ProgressBar pb_prac;
    private TextView reset_tut;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ViewStub stub = findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.activity_settings);
        stub.inflate();

        // nav drawer
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //components
        name = findViewById(R.id.settings_name);
        mail = findViewById(R.id.settings_mail);
        image = findViewById(R.id.settings_image);
        save = findViewById(R.id.save_settings);
        pb_prac = findViewById(R.id.settings_pb_practise);
        pb_tut = findViewById(R.id.settings_pb_tutorial);
        reset_tut = findViewById(R.id.tv_reset_tutorial);

        //text hints should be the currentty saved data
        String cur_name = loadDataString(getString(R.string.userName_key), getString(R.string.default_name));
        String cur_mail = loadDataString(getString(R.string.userMail_key), getString(R.string.default_mail));
        name.setText(cur_name);
        mail.setText(cur_mail);

        //change profile image
        if(loadDataBoolean(getString(R.string.userImageBool_key), false)){
            image.setImageURI(Uri.parse(loadDataString(getString(R.string.userImage_key), "")));
        } else{
            image.setImageResource(R.drawable.profile_image);
        }
        image.setOnClickListener(view -> {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }else{
                pickImageFromGallery();
            }
        });

        //lets go back to navigation
        save.setOnClickListener(view -> {
            String userName = name.getText().toString();
            String userMail = mail.getText().toString();

            if(userMail.contains("@")){
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
        });

        //show bars
        pb_tut.setProgress(calcAvg(new int[]{
                loadDataInt(getString(R.string.tutScore1_key), 10),
                loadDataInt(getString(R.string.tutScore2_key), 20),
                loadDataInt(getString(R.string.tutScore3_key), 30),
                loadDataInt(getString(R.string.tutScore4_key), 40)
        }));
        pb_prac.setProgress(calcExAvg());

        //global tut/prac reset
        reset_tut.setOnClickListener(new Settings.OnTutResetClickListener());
    }

    public void pickImageFromGallery(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        if (requestCode == PERMISSION_CODE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, getString(R.string.toast_ImagePermissionDenied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            Uri img_uri = data.getData();
            image.setImageURI(img_uri);
            saveDataString(getString(R.string.userImage_key), img_uri.toString());
            saveDataBoolean(getString(R.string.userImageBool_key), true);
        }
        //popup
        if (requestCode == PopUp.REQUEST_CODE && resultCode == RESULT_OK){
            reset_globally();
        }
    }

    public boolean loadDataBoolean(String key, boolean default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPref.getBoolean(key, default_value);
    }

    public void saveDataBoolean(String key, boolean data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    public void saveDataString(String key, String data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public void saveDataInt(String key, int data){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
    }

    public String loadDataString(String key, String default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPref.getString(key, default_value);
    }

    public int loadDataInt(String key, int default_value){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPref.getInt(key, default_value);
    }

    public int calcAvg(int[] partialExps){
        int num_cats = partialExps.length;
        int sum = 0;
        for (int partialExp : partialExps) {
            sum += partialExp;
        }
        return sum/num_cats;
    }

    public int calcExAvg(){
        String e1 = loadDataString(getString(R.string.prac_easy_key), Practices.DEFAULT_EASY);
        String e2 = loadDataString(getString(R.string.prac_medium_key), Practices.DEFAULT_MEDIUM);
        String e3 = loadDataString(getString(R.string.prac_hard_key), Practices.DEFAULT_HARD);
        int num_exs = Practices.DEFAULT_EASY.length()+Practices.DEFAULT_MEDIUM.length()+Practices.DEFAULT_HARD.length();
        int num_dones = getNonZeros(e1)+getNonZeros(e2)+getNonZeros(e3);
        return num_dones==0? 1: 100*num_dones/num_exs;
    }

    public int getNonZeros(String s){
        int num_NonZeros = 0;
        for(int i=0; i<s.length(); i++)
            if(s.charAt(i)!='0')
                num_NonZeros++;
        return num_NonZeros;
    }

    public void reset_globally(){
        reset_tut.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate_short));
        //reset done lections
        saveDataInt(getString(R.string.tutScore1_key), 1);
        saveDataInt(getString(R.string.tutScore2_key), 1);
        saveDataInt(getString(R.string.tutScore3_key), 1);
        saveDataInt(getString(R.string.tutScore4_key), 1);
        //reset unlocked lections todo hard
        saveDataInt(getString(R.string.tutScore1_unlocked_key), (int)(100f/12f+1));
        saveDataInt(getString(R.string.tutScore2_unlocked_key), (int)(100f/16f+1));
        saveDataInt(getString(R.string.tutScore3_unlocked_key), (int)(100f/8f+1));
        saveDataInt(getString(R.string.tutScore4_unlocked_key), (int)(100f/9f+1));
        //reset done exercises
        saveDataString(getString(R.string.prac_easy_key), Practices.DEFAULT_EASY);
        saveDataString(getString(R.string.prac_medium_key), Practices.DEFAULT_MEDIUM);
        saveDataString(getString(R.string.prac_hard_key), Practices.DEFAULT_HARD);
        //reset bars
        pb_tut.setProgress(0);
        pb_prac.setProgress(1);
    }

    //listeners
    public class OnTutResetClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), PopUp.class);
            i.putExtra(PopUp.KEY, PopUp.GLOBALRESET);
            startActivityForResult(i, PopUp.REQUEST_CODE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //swipeHint.setVisibility(View.VISIBLE);
        } else {
            Intent i = new Intent(getApplicationContext(), PopUp.class);
            i.putExtra(PopUp.KEY, PopUp.CLOSEAPP);
            startActivityForResult(i, PopUp.REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        //swipeHint.setVisibility(View.GONE);
        return true;
    }

    @Override
    //not used yet maybe later for action bar items
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(getApplicationContext(), "onOptionItemSelected", Toast.LENGTH_SHORT).show();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), Navigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_free) {
            Intent i = new Intent(getApplicationContext(), Free.class);
            startActivity(i);

        } else if (id == R.id.nav_tutorial) {
            Intent i = new Intent(getApplicationContext(), Tutorial.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_practice) {
            Intent i = new Intent(getApplicationContext(), Complexity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);

        } else if (id == R.id.nav_impressum) {
            Intent i = new Intent(getApplicationContext(), Impressum.class);
            startActivity(i);

        } else if (id == R.id.nav_dbView) {
            Intent i = new Intent(getApplicationContext(), DbView.class);
            startActivity(i);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
