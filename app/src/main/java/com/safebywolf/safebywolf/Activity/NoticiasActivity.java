package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safebywolf.safebywolf.Adapter.NoticiasAdapter;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Model.RowNoticia;
import com.safebywolf.safebywolf.R;

import java.util.ArrayList;

public class NoticiasActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerViewNoticias;
    ArrayList<RowNoticia> arrayListRowNoticias = new ArrayList<>();
    Context context;
    LinearLayout linearLayoutProgressBar;
    LinearLayout linearLayoutSinNoticias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;

        recyclerViewNoticias = findViewById(R.id.recyclerViewNoticias);
        recyclerViewNoticias.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutProgressBar=findViewById(R.id.linearLayoutProgressBar);
        linearLayoutSinNoticias = findViewById(R.id.linearLayoutSinNoticias);

        getNoticias();
    }

    public void getNoticias(){
        //obtiene las ultimas 10 noticias (costo de lectura 10)
        db.collection(Referencias.PUBLICACIONES).document(Referencias.NOTICIA)
                .collection(Referencias.TODO)
                .whereEqualTo("visible",true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult().size()>0){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        RowNoticia rowNoticia = null;
                        try {
                            rowNoticia = documentSnapshot.toObject(RowNoticia.class);
                            Log.v("rowNoticia", "rowNoticia: " + rowNoticia.getTitulo());

                            arrayListRowNoticias.add(rowNoticia);

                        } catch (Exception e) {
                            Log.v("rowNoticia", "No se puedo convertir rowNoticia: " + e);
                        }
                    }

                    NoticiasAdapter noticiasAdapter = new NoticiasAdapter(context, arrayListRowNoticias);
                    recyclerViewNoticias.setAdapter(noticiasAdapter);

                } else {
                    linearLayoutSinNoticias.setVisibility(View.VISIBLE);
                }
                showProgress(false);
            }
        });
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
}