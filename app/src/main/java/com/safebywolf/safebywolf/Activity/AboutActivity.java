package com.safebywolf.safebywolf.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;

import java.util.Timer;
import java.util.TimerTask;

public class AboutActivity extends AppCompatActivity {
    TextView textViewVersion;
    private String emailUsuarioFirebase;
    ImageView imageViewLogo;

    private Timer timer;
    private TimerTask doAsynchronousTaskBattery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.emailUsuarioFirebase = Utils.leerValorString(AboutActivity.this, Referencias.CORREO);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewVersion = findViewById(R.id.version);
        if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
            textViewVersion.setText(BuildConfig.VERSION_NAME+" Dev");
        } else {
            textViewVersion.setText(BuildConfig.VERSION_NAME);
        }

        imageViewLogo = findViewById(R.id.imageViewLogo);
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safebywolf.cl"));
                startActivity(browserIntent);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Log.v("foolala", "me voy para atras About");
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("foolala", "me voy para atras About");
        return super.onKeyDown(keyCode, event);
    }
}