package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Model.Novedad;
import com.safebywolf.safebywolf.R;

public class NovedadActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewTitulo;
    TextView textViewContenido;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    String url = null;
    Button buttonDescargar;
    CircularProgressDrawable circularProgressDrawable;
    LinearLayout linearLayoutProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedad);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewTitulo = findViewById(R.id.textViewTitulo);
        textViewContenido = findViewById(R.id.textViewContenido);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.start();
        linearLayoutProgressBar=findViewById(R.id.linearLayoutProgressBar);

        buttonDescargar = findViewById(R.id.buttonDescargar);
        buttonDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(url != null && !url.equalsIgnoreCase("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });

        String id = (String) getIntent().getStringExtra("id");
        Log.v("-novedad","id: "+id);
        if(id != null){
            getNovedad(id);
        }else {
            Toast.makeText(NovedadActivity.this, "No se puede cargar novedad", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showProgress(final boolean show) {
        if(show == false){
            linearLayoutProgressBar.setVisibility(View.GONE);
        } else {
            linearLayoutProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void getNovedad(String id){
        String versionNameAppCollection = "";
        if(BuildConfig.FLAVOR.equalsIgnoreCase("lite")){
            versionNameAppCollection = Referencias.APPLITE;
        } else {
            versionNameAppCollection = Referencias.APPFULL;
        }
        //obtiene una noticia por su id (costo de lectura 1)
        db.collection(Referencias.PUBLICACIONES)
                .document(Referencias.NOVEDAD)
                .collection(versionNameAppCollection)
                .document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.v("-novedad","is success");
                    Novedad novedad = null;
                    try {
                        DocumentSnapshot document = task.getResult();

                        Log.v("-novedad", "getId: "+document.getId());
                        Log.v("-novedad", "getData: "+document.getData());
                        novedad = document.toObject(Novedad.class);
                        Log.v("-novedad", "titulo: "+novedad.getId());

                        textViewTitulo.setText(novedad.getTitulo());

                        for(int i = 0; i < novedad.getContenido().size(); i++){
                            textViewContenido.setText(textViewContenido.getText()+"\n"+novedad.getContenido().get(i));
                        }

                        if(novedad.getUrl() != null && !novedad.getUrl().equalsIgnoreCase("")) {
                            buttonDescargar.setVisibility(View.VISIBLE);
                            url = novedad.getUrl();
                        }

                        if(novedad.getImagenes() != null && novedad.getImagenes().size() > 0 && !novedad.getImagenes().get(0).equalsIgnoreCase("")){
                            imageView1.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),novedad.getImagenes().get(0),imageView1);
                        }

                        if(novedad.getImagenes() != null && novedad.getImagenes().size() > 1 && !novedad.getImagenes().get(1).equalsIgnoreCase("")){
                            imageView2.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),novedad.getImagenes().get(1),imageView2);
                        }

                        if(novedad.getImagenes() != null && novedad.getImagenes().size() > 2 && !novedad.getImagenes().get(2).equalsIgnoreCase("")){
                            imageView3.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),novedad.getImagenes().get(2),imageView3);
                        }

                        showProgress(false);

                    } catch (Exception e){
                        Log.v("-novedad", "error: "+e.getMessage());
                    }


                }
            }
        });
    }

    public void cargarImagenGlide(Context context, String urlImagen, ImageView imageView){
        Glide.with(context)
                .asBitmap()
                .load(urlImagen)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(imageView);
    }
}