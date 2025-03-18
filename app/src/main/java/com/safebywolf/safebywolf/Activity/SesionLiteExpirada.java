package com.safebywolf.safebywolf.Activity;
import com.safebywolf.safebywolf.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SesionLiteExpirada extends AppCompatActivity {
    Button contactUs;
    TextView closeApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_lite_expirada);
        contactUs = (Button) findViewById(R.id.contact_us);
        closeApp = (TextView) findViewById(R.id.exit_button);

        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://safebywolf.cl/#contacto"));
                startActivity(browserIntent);

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (shouldAllowBack()) {
            super.onBackPressed();
        }
    }

    private boolean shouldAllowBack() {
        return false;
    }
}