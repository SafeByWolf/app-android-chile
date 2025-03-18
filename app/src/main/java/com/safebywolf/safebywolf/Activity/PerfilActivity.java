package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.safebywolf.safebywolf.Activity.TensorFlow.CameraActivity;
import com.safebywolf.safebywolf.Adapter.GruposAdapter;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.TokenApp;
import com.safebywolf.safebywolf.Model.Total;
import com.safebywolf.safebywolf.Model.UsuarioAndroid;
import com.safebywolf.safebywolf.Model.UsuariosTokenApp;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PerfilActivity extends AppCompatActivity {

    boolean isLimiteParaActualizarCuenta = false;
    TextView textViewEmail;
    TextView textViewNombre;
    TextView textViewApellido;
    TextView textViewTelefono;
    Button buttonEditarPerfil;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String emailUsuarioFirebase;
    TextView textViewTotalDelDia;
    TextView textViewTotalGeneral;
    TextView accountVersionValue;
    View layoutCodigo;
    Button buttonActualizarCuenta;
    TextView textVieCodigo;
    String errorCodigo = "";
    LinearLayout linearLayoutTokenApp;
    TextView textViewTokenApp, textViewTokenAppValue;
    int contadorDeErroresDeCodigo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewEmail=findViewById(R.id.textViewEmail);
        textViewNombre=findViewById(R.id.textViewNombre);
        textViewApellido=findViewById(R.id.textViewApellido);
        textViewTelefono=findViewById(R.id.textViewTelefono);
        textViewTotalDelDia = findViewById(R.id.textViewTotalDelDia);
        textViewTotalGeneral = findViewById(R.id.textViewTotalGeneral);
        accountVersionValue = findViewById(R.id.accountVersionValue);
        textVieCodigo = findViewById(R.id.editTextCodigo);

        linearLayoutTokenApp = findViewById(R.id.linearLayoutTokenApp);
        textViewTokenApp = findViewById(R.id.textViewTokenApp);
        textViewTokenAppValue = findViewById(R.id.textViewTokenAppValue);

        layoutCodigo = findViewById(R.id.layoutCodigo);
        buttonActualizarCuenta = findViewById(R.id.buttonActualizarCuenta);
        buttonActualizarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLimiteParaActualizarCuenta){
                    Toast.makeText(PerfilActivity.this,"Has alcanzado el límite máximo de intentos permitidos",Toast.LENGTH_SHORT).show();
                } else {
                    final String codigo = textVieCodigo.getText().toString();
                    buscarCodigo(codigo);
                }
                if(contadorDeErroresDeCodigo >= 9){
                    isLimiteParaActualizarCuenta = true;
                    Toast.makeText(PerfilActivity.this,"Has alcanzado el límite máximo de intentos permitidos",Toast.LENGTH_SHORT).show();
                    buttonActualizarCuenta.setBackgroundColor(getResources().getColor(R.color.browser_actions_title_color));
                    Utils.guardarValorBoolean(PerfilActivity.this, Referencias.LIMITEPARAACTUALIZARCUENTA,true);
                    //actualiza el campo limite para actualizar cuenta en configuracion usuario (costo escritura 1)
                    db.collection(Referencias.CONF).document("app").collection(Referencias.USUARIO)
                            .document(emailUsuarioFirebase).set(new HashMap<String, Object>() {{
                                put("limiteParaActualizarCuenta", true);
                            }}, SetOptions.merge());
                }
            }
        });

        buttonEditarPerfil=findViewById(R.id.buttonEditarPerfil);
        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre",textViewNombre.getText().toString());
                bundle.putString("apellido",textViewApellido.getText().toString());
                bundle.putString("telefono",textViewTelefono.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        emailUsuarioFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(PerfilActivity.this, Referencias.CORREO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
        getPatentesEscaneadasDelDia();
        getPatentesEscaneadasGeneral();
        isLimiteParaActualizarCuenta = Utils.leerValorBoolean(PerfilActivity.this,Referencias.LIMITEPARAACTUALIZARCUENTA);
        if(isLimiteParaActualizarCuenta){
            buttonActualizarCuenta.setBackgroundColor(getResources().getColor(R.color.browser_actions_title_color));
        }
    }

    public void buscarCodigo(String codigo){
        if (textVieCodigo.getText().toString().length() == 0){
            textVieCodigo.setError("Ingrese código válido");
            View focusView = textVieCodigo;
            focusView.requestFocus();
        } else {
            //consulta un token desde coleccion tokenApp (costo de lectura 1)
            db.collection("tokenApp").whereEqualTo("token", codigo).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.size() == 0) {
                        textVieCodigo.setError("Código inválido");
                        View focusView = textVieCodigo;
                        focusView.requestFocus();
                        contadorDeErroresDeCodigo++;
                    } else {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            TokenApp tokenApp = queryDocumentSnapshot.toObject(TokenApp.class);
                            tokenApp.setId(queryDocumentSnapshot.getId());
                            if (verificarCodigo(tokenApp)) {
                                //consulta los usuarios de un token desde coleccion tokenApp -> usuarios (costo de lectura 1)
                                db.collection("tokenApp").document(tokenApp.getId()).collection("usuarios").whereEqualTo("email", emailUsuarioFirebase).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot.size() == 0) {
                                            //actualiza el campo tokenApp de coleccion usuario (costo de escritura 1)
                                            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenApp", tokenApp.getToken(), "accountVersion", tokenApp.getAccountVersion());
                                            UsuariosTokenApp usuariosTokenApp = new UsuariosTokenApp(emailUsuarioFirebase);
                                            //actualiza el campo usos de coleccion tokenApp (costo de escritura 1)
                                            db.collection("tokenApp").document(tokenApp.getId()).update("usos", FieldValue.increment(1));
                                            //crea el registro de usuario dentro de coleccion tokenApp -> usuarios (costo de escritura 1)
                                            db.collection("tokenApp").document(tokenApp.getId()).collection("usuarios").add(usuariosTokenApp);
                                            dialogInformacion("Código aceptado", "Su cuenta ha sido actualizada a la versión: " + tokenApp.getAccountVersion());
                                        } else {
                                            contadorDeErroresDeCodigo++;
                                            dialogInformacion("No necesitas actualizar", "Ya usaste este código: " + tokenApp.getAccountVersion());
                                        }
                                    }
                                });
                            } else {
                                contadorDeErroresDeCodigo++;
                                textVieCodigo.setError(errorCodigo);
                                View focusView = textVieCodigo;
                                focusView.requestFocus();
                            }
                        }
                    }
                }
            });
        }
    }

    public void dialogInformacion(String titleText, String contentText) {
        new AlertDialog.Builder(PerfilActivity.this)
                .setTitle(titleText)
                .setCancelable(false)
                .setMessage(contentText)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restartApp(PerfilActivity.this, 1000);
                    }
                })
                .show();
    }

    private void restartApp(Context context, long delay) {
        Intent intent = PerfilActivity.this.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + delay, pendingIntent);
        System.exit(0);
    }

    private boolean verificarCodigo(TokenApp tokenApp) {
        boolean visible = tokenApp.isVisible();
        Date expirationDate = tokenApp.getExpirationDate();
        Date currentDate = new CurrentDate(new Date()).getDate();
        int usos = tokenApp.getUsos();
        int usuarios = tokenApp.getUsuarios();
        if (usos >= usuarios) {
            errorCodigo = "Código caducado";
            return false;
        }
        if (currentDate.getTime() > expirationDate.getTime()) {
            errorCodigo = "Código caducado";
            return false;
        }
        if (!visible) {
            errorCodigo = "Código inválido";
            return false;
        }
        return true;
    }

    public void getPatentesEscaneadasGeneral(){
        //consulta el total de patentes general escaneadas (costo de lectura 1)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA)
                .collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                Total total = documentSnapshot.toObject(Total.class);
                textViewTotalGeneral.setText(total.getTotal()+"");
            }
        });
    }

    public void getPatentesEscaneadasDelDia(){
        //consulta el total de patentes escaneadas del dia (costo de lectura 1)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA)
                .collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).
                document(new CurrentDate(new Date()).getFecha()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Total total = document.toObject(Total.class);
                                textViewTotalDelDia.setText(total.getTotal()+"");
                            }
                        }

                    }
                });
    }

    public void getUserData(){
        //consulta el usuario (costo de lectura 1)
        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                Log.v("racssa","task.getResult(): "+task.getResult().toString());

                UsuarioAndroid usuarioAndroid = task.getResult().toObject(UsuarioAndroid.class);
                if(usuarioAndroid == null){
                    return;
                }
                textViewEmail.setText(usuarioAndroid.getEmail());
                textViewNombre.setText(usuarioAndroid.getNombre());
                textViewApellido.setText(usuarioAndroid.getApellido());
                textViewTelefono.setText(usuarioAndroid.getContacto());

                if(usuarioAndroid.getAccountVersion() != null ){
                    if((usuarioAndroid.getAccountVersion().equalsIgnoreCase("") || usuarioAndroid.getAccountVersion().equalsIgnoreCase("full"))){
                        accountVersionValue.setText("Full - Sin límites");
                    } else if(usuarioAndroid.getAccountVersion().equalsIgnoreCase("lite")){
                        accountVersionValue.setText("Limited - Con límite de lecturas");
                        layoutCodigo.setVisibility(View.VISIBLE);
                        buttonActualizarCuenta.setVisibility(View.VISIBLE);
                    } else if(usuarioAndroid.getAccountVersion().equalsIgnoreCase("sinAlertas")){
                        accountVersionValue.setText("Silent - Sin alertas");
                        layoutCodigo.setVisibility(View.VISIBLE);
                        buttonActualizarCuenta.setVisibility(View.VISIBLE);
                    }
                    if(usuarioAndroid.getTokenApp() != null && !usuarioAndroid.getTokenApp().equalsIgnoreCase("")){
                        //linearLayoutTokenApp.setVisibility(View.VISIBLE);
                        //textViewTokenAppValue.setText(usuarioAndroid.getTokenApp());
                    }
                } else {
                    accountVersionValue.setText("Full sin límites");
                }

                recyclerView = findViewById(R.id.recyclerViewGrupos);
                GruposAdapter gruposAdapter;
                if(usuarioAndroid.getGrupo()!=null && usuarioAndroid.getGrupo().size()>0){
                    gruposAdapter = new GruposAdapter(usuarioAndroid.getGrupo());
                } else {
                    ArrayList arrayList = new ArrayList<>();
                    arrayList.add("No estas asociado a ningún grupo");
                    gruposAdapter = new GruposAdapter(arrayList);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(PerfilActivity.this));
                recyclerView.setAdapter(gruposAdapter);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}