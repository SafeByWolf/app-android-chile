package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.Comuna;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ComunasActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AutoCompleteTextView autoCompleteTextViewComunas;
    ArrayList<Comuna> comunas = new ArrayList<>();
    List<String> suggestionList = new ArrayList<String>();
    TextView textViewActivo;
    TextView textViewComunaText;
    TextView textViewComuna;
    LinearLayout linearLayoutLogo;
    ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        autoCompleteTextViewComunas = findViewById(R.id.autoCompleteTextViewComunas);
        textViewActivo = findViewById(R.id.textViewActivo);
        textViewComunaText = findViewById(R.id.textViewComunaText);
        textViewComuna = findViewById(R.id.textViewComuna);
        linearLayoutLogo = findViewById(R.id.linearLayoutLogo);
        imageViewLogo = findViewById(R.id.imageViewLogo);

        //consulta las comunas de chile (costo de lecturas N no optimo)
        db.collection("comunas").whereEqualTo("paisKey","chile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Comuna comuna;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        try {
                            comuna = documentSnapshot.toObject(Comuna.class);
                            comunas.add(comuna);
                            //suggestionList.add(comuna.getComunaKey());
                            suggestionList.add(comuna.getComuna());
                        }catch (Exception e){
                            Log.v("comunasX","Exception: "+e.getMessage());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ComunasActivity.this, android.R.layout.simple_dropdown_item_1line, suggestionList);
                    autoCompleteTextViewComunas.setAdapter(adapter);
                    Log.v("comunasX","size: "+task.getResult().size());
                }
            }
        });
        autoCompleteTextViewComunas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.v("comunasX", "beforeTextChanged: " + autoCompleteTextViewComunas.getText().toString());
                setContentView();
                return false;
            }
        });
        autoCompleteTextViewComunas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("comunasX", "onItemClick: " + autoCompleteTextViewComunas.getText().toString());
                setContentView();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void setContentView(){
        Comuna comuna = getComuna(autoCompleteTextViewComunas.getText().toString());
        if(comuna != null){
            textViewComuna.setVisibility(View.VISIBLE);
            textViewComuna.setText(comuna.getComuna());
            textViewComunaText.setVisibility(View.VISIBLE);
            linearLayoutLogo.setVisibility(View.VISIBLE);
            Log.v("comunasX", "comuna NO es nulo");
            if(comuna.isActivo()){
                textViewActivo.setText("SI está protegido por SafeByWolf");
                imageViewLogo.setImageDrawable(getDrawable(R.drawable.logoredondeadook));
            } else {
                textViewActivo.setText("NO está protegido por SafeByWolf");
                imageViewLogo.setImageDrawable(getDrawable(R.drawable.logoredondeadocancel));

            }
        } else {
            linearLayoutLogo.setVisibility(View.GONE);
            textViewComuna.setVisibility(View.GONE);
            textViewComunaText.setVisibility(View.GONE);
            textViewActivo.setText("NO existe comuna");
            Log.v("comunasX", "comuna es nulo");
            if(autoCompleteTextViewComunas.getText().length() == 0){
                textViewActivo.setText("");
            }
        }
    }

    public Comuna getComuna(String comunaString){
        if(comunaString == null){
            return null;
        }
        comunaString = Utils.normalizarString(comunaString);
        Comuna comuna = null;
        for(int i = 0; i < comunas.size(); i++){
            comuna = comunas.get(i);
            String comunaFromList = Utils.normalizarString(comunas.get(i).getComuna());
            Log.v("comunasX", "comuna normalizada: "+comunaFromList);
            if(comunaFromList.toLowerCase().equalsIgnoreCase(comunaString.toLowerCase())){
                Log.v("comunasX", "si existe");
                return comuna;
            }
        }
        return null;
    }
}