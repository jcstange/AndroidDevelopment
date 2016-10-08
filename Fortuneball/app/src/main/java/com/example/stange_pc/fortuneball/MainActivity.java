package com.example.stange_pc.fortuneball;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String fortuneList[] = {"Don’t count on it", "Ask again later", "You may rely on it", "Without a doubt", "Outlook not so good", "It's decidedly so", "Signs point to yes", "Yes definitely", "Yes", "My sources say NO"};
    TextView mFortuneText;
    Button mGenerateFortuneButton;
    ImageView mFortuneBallImage;
    boolean play = true;
    MediaPlayer mp;
    FloatingActionButton fab;
    FloatingActionButton sound;
    int icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFortuneText = (TextView) findViewById(R.id.fortuneText);
        mFortuneBallImage = (ImageView) findViewById(R.id.fortunateImage);
        mGenerateFortuneButton = (Button) findViewById(R.id.fortuneButton);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        sound = (FloatingActionButton) findViewById(R.id.sound);
        mp = MediaPlayer.create(MainActivity.this, R.raw.song);

        mGenerateFortuneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 5:
                int index = new Random().nextInt(fortuneList.length);
                mFortuneText.setText(fortuneList[index]);
                // 6:
                YoYo.with(Techniques.Swing)
                        .duration(1000)
                        .playOn(mFortuneBallImage);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!mp.isPlaying() || play) {
                    play = false;
                    mp.start();
                    icon = R.drawable.button_pause;
                } else {
                    mp.pause();
                    icon = R.drawable.button_play;
                }
                sound.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                play = true;
                icon = R.drawable.button_play;
                sound.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_search:
                Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.action_share:
                Toast.makeText(this, "Share selected", Toast.LENGTH_SHORT)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*@Override
    public void settings(View item) {
        Snackbar.make(item, "You pressed setting button", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void search(View item) {
        Snackbar.make(item, "You pressed search button", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void share(View item) {
        Snackbar.make(item, "You pressed search share", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }*/
}