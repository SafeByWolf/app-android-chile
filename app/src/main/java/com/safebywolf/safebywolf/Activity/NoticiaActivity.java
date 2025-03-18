package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.Noticia;
import com.safebywolf.safebywolf.R;

public class NoticiaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewTitulo;
    TextView textViewContenido;
    TextView textViewFecha;
    TextView textViewAutor;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    CircularProgressDrawable circularProgressDrawable;
    LinearLayout linearLayoutProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewTitulo = findViewById(R.id.textViewTitulo);
        textViewContenido = findViewById(R.id.textViewContenido);
        textViewFecha = findViewById(R.id.textViewFecha);
        textViewAutor = findViewById(R.id.textViewAutor);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.start();
        linearLayoutProgressBar=findViewById(R.id.linearLayoutProgressBar);

        String id = (String) getIntent().getStringExtra("id");
        Log.v("-noticia","id: "+id);
        if(id != null){
            getNoticia(id);
        }else {
            Toast.makeText(NoticiaActivity.this, "No se puede cargar noticia", Toast.LENGTH_SHORT).show();
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

    public void getNoticia(String id){
        //obtiene una noticia por su id (costo de lectura 1)
        db.collection(Referencias.PUBLICACIONES).document(Referencias.NOTICIA)
                .collection(Referencias.TODO).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.v("-noticia","is success");
                    Noticia noticia = null;
                    try {
                        DocumentSnapshot document = task.getResult();

                        Log.v("-noticia", "getId: "+document.getId());
                        Log.v("-noticia", "getData: "+document.getData());
                        noticia = document.toObject(Noticia.class);
                        Log.v("-noticia", "titulo: "+noticia.getId());

                        textViewTitulo.setText(noticia.getTitulo());
                        textViewContenido.setText(noticia.getContenido());
                        textViewFecha.setText(Utils.parseDateToString(noticia.getTimestamp(),"dd MMMM yyyy hh:mm:ss"));
                        textViewAutor.setText(noticia.getAutor());


                        if(noticia.getImagenes() != null && noticia.getImagenes().size() > 0 && !noticia.getImagenes().get(0).equalsIgnoreCase("")){
                            imageView1.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),noticia.getImagenes().get(0),imageView1);
                        }

                        if(noticia.getImagenes() != null && noticia.getImagenes().size() > 1 && !noticia.getImagenes().get(1).equalsIgnoreCase("")){
                            imageView2.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),noticia.getImagenes().get(1),imageView2);
                        }

                        if(noticia.getImagenes() != null && noticia.getImagenes().size() > 2 && !noticia.getImagenes().get(2).equalsIgnoreCase("")){
                            imageView3.setVisibility(View.VISIBLE);
                            cargarImagenGlide(getApplicationContext(),noticia.getImagenes().get(2),imageView3);
                        }

                    } catch (Exception e){
                        Log.v("-noticia", "error: "+e.getMessage());
                    }


                }
                showProgress(false);
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