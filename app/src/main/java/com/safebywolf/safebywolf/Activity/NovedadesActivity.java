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
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.safebywolf.safebywolf.Adapter.NovedadAdapter;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Model.RowNovedad;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NovedadesActivity extends AppCompatActivity {
    TextView textViewContenido;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout linearLayoutProgressBar;
    LinearLayout linearLayoutSinNovedades;
    ArrayList<RowNovedad> arrayListRowNovedad = new ArrayList<>();
    RecyclerView recyclerViewNovedades;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedades);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;

        recyclerViewNovedades = findViewById(R.id.recyclerViewNoticias);
        recyclerViewNovedades.setLayoutManager(new LinearLayoutManager(this));

        linearLayoutProgressBar=findViewById(R.id.linearLayoutProgressBar);
        linearLayoutSinNovedades = findViewById(R.id.linearLayoutSinNovedades);

        Log.v("rowNovedad","flavor: "+BuildConfig.FLAVOR);
        Log.v("rowNovedad","version: "+BuildConfig.VERSION_NAME);
        getNovedad();
    }

    public void getNovedad(){
        String versionNameAppCollection = "";
        if(BuildConfig.FLAVOR.equalsIgnoreCase("lite")){
            versionNameAppCollection = Referencias.APPLITE;
        } else {
            versionNameAppCollection = Referencias.APPFULL;
        }

        Log.v("novedadv",versionNameAppCollection);
        Log.v("novedadv",BuildConfig.VERSION_NAME);
        //obtiene las ultimas 10 noticias (costo de lectura 10)
        db.collection(Referencias.PUBLICACIONES)
                .document(Referencias.NOVEDAD)
                .collection(versionNameAppCollection)
                .whereEqualTo("visible",true)
                .whereEqualTo("version",BuildConfig.VERSION_NAME)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult().size()>0){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        RowNovedad rowNovedad = null;
                        try {
                            rowNovedad = documentSnapshot.toObject(RowNovedad.class);
                            Log.v("rowNovedad", "rowNovedad: " + rowNovedad.getTitulo());

                            arrayListRowNovedad.add(rowNovedad);

                        } catch (Exception e) {
                            Log.v("rowNovedad", "No se puedo convertir rowNovedad: " + e);
                        }
                    }
                    NovedadAdapter noticiasAdapter = new NovedadAdapter(context, arrayListRowNovedad);
                    recyclerViewNovedades.setAdapter(noticiasAdapter);
                } else {
                    linearLayoutSinNovedades.setVisibility(View.VISIBLE);
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