package com.safebywolf.safebywolf.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.ImagenGenerica;
import com.safebywolf.safebywolf.Model.ImagenPatente;
import com.safebywolf.safebywolf.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GaleryActivity extends AppCompatActivity {
    ArrayList<ImagenPatente> imagenes = new ArrayList<>();

    private String emailUsuarioFirebase;

    private final int REQUEST_VISUALIZADOR = 1;

    private Timer timer;
    private TimerTask doAsynchronousTaskBattery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Galería");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.emailUsuarioFirebase = Utils.leerValorString(GaleryActivity.this, Referencias.CORREO);
        //this.taskBattery();
        Log.v("foolala", this.emailUsuarioFirebase);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //obtener imagenes por getSerializableExtra
        imagenes=((ArrayList<ImagenPatente>)getIntent().getSerializableExtra("imagenes"));
        if(imagenes!=null){
            if(imagenes.size()>0){
                textView.setText(textView.getText()+" " +imagenes.get(0).getPatente());
            }
        }

        ImagenGenerica[] getSpacePhotos = new ImagenGenerica[imagenes.size()];
        int i = 0;
        for (ImagenPatente imagen : imagenes) {
            ImagenGenerica imagenGenerica = new ImagenGenerica(imagen.getUrl(), imagen.getId().toString());
            getSpacePhotos[i] = imagenGenerica;
            i++;
        }
        Log.v("taza", String.valueOf(getSpacePhotos.length));
        GaleryActivity.ImageGalleryAdapter adapter = new GaleryActivity.ImageGalleryAdapter(GaleryActivity.this, getSpacePhotos);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VISUALIZADOR){
            Log.v("foolala", "de regreso en la galeria");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("onResume", "Estoy en el onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("onResume", "Estoy en el stop");
    }

    public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_image, parent, false);

            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
            final ImagenGenerica imagenGenerica = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(GaleryActivity.this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            Glide.with(mContext)
                    .load(imagenGenerica.getUrl())
                    .placeholder(circularProgressDrawable)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.v("fotoError", imagenGenerica.getTitle());
                            Log.v("fotoError", imagenGenerica.getUrl());
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.imageViewImagenGallery);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ImagenGenerica imagenGenerica = mSpacePhotos[position];
                    Intent intent = new Intent(mContext, VisualizadorDeImagenesActivity.class);
                    intent.putExtra(VisualizadorDeImagenesActivity.EXTRA_SPACE_PHOTO, imagenGenerica);
                    intent.putExtra("imagen", imagenes.get(position));
                    startActivityForResult(intent, REQUEST_VISUALIZADOR);
                }
            }
        }

        private ImagenGenerica[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, ImagenGenerica[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.v("foolala", "me voy para atras Galeria");
        onBackPressed();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("foolala", "me voy para atras Galeria");
        return super.onKeyDown(keyCode, event);
    }
}