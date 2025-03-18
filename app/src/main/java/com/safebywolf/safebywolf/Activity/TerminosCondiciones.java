package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Model.TerminosYCondiciones;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TerminosCondiciones extends AppCompatActivity {
    TextView textViewTitulo;
    TextView textViewContenido;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewTitulo=findViewById(R.id.textViewTitulo);
        textViewContenido=findViewById(R.id.textViewContenido);

        //obtiene los terminos y condiciones (costo de lectura 1)
        db.collection(Referencias.TERMINOSYCONDICIONES).document("0").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                TerminosYCondiciones terminosCondiciones = documentSnapshot.toObject(TerminosYCondiciones.class);
                if(terminosCondiciones != null){
                    textViewTitulo.setText(terminosCondiciones.getTitulo());
                    textViewContenido.setText(Html.fromHtml(terminosCondiciones.getContenido()));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}