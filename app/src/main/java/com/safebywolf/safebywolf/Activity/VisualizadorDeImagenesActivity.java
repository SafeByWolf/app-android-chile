package com.safebywolf.safebywolf.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
//import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.ImagenPatente;
import com.safebywolf.safebywolf.R;

import java.util.Timer;
import java.util.TimerTask;

public class VisualizadorDeImagenesActivity extends AppCompatActivity {
    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    private ImageView mImageView;
    //ImagenGenerica imagenGenerica = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
    ImagenPatente imagenPatente;
    private String emailUsuarioFirebase;

    private Timer timer;
    private TimerTask doAsynchronousTaskBattery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizador_de_imagenes);

        Toolbar toolbar = findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);

        this.emailUsuarioFirebase = Utils.leerValorString(VisualizadorDeImagenesActivity.this, Referencias.CORREO);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imagenPatente=((ImagenPatente)getIntent().getSerializableExtra("imagen"));
        CircularProgressDrawable circularProgressDrawable=new CircularProgressDrawable(VisualizadorDeImagenesActivity.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        mImageView = (ImageView) findViewById(R.id.image);

        Glide.with(this)
                .asBitmap()
                .load(imagenPatente.getUrl())
                .error(R.drawable.abc_vector_test)
                .fitCenter()
                .placeholder(circularProgressDrawable)
                .listener(new RequestListener<Bitmap>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        mImageView.setImageBitmap(resource);
                        Log.v("mImageViewx","recurso cargado: " +resource.getWidth() +"x"+resource.getHeight());
                        return false;
                    }

                })
                .into(mImageView);
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
        Log.v("foolala", "me voy para atras Visualizador");
        onBackPressed();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("foolala", "me voy para atras Visualizador");
        return super.onKeyDown(keyCode, event);
    }
}
