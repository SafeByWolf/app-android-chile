package com.safebywolf.safebywolf.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;

import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Fragment.DatePickerFragment;
import com.safebywolf.safebywolf.Model.EmailTotal;
import com.safebywolf.safebywolf.Model.SendUbicacion;
import com.safebywolf.safebywolf.Model.TokenApp;
import com.safebywolf.safebywolf.Model.Total;
import com.safebywolf.safebywolf.Model.UsuarioAndroid;
import com.safebywolf.safebywolf.Model.UsuariosTokenApp;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class RegistrarActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button botonRegistrar;
    Button botonCancelar;
    TextView textViewNombre;
    TextView textVieCodigo;
    TextView textViewApellido;
    TextView textViewEmail;
    TextView textViewRepeatEmail;
    TextView textViewContacto;
    EditText etPlannedDate;
    EditText textViewPassword;
    TextView getTextViewPassword2;
    EditText editTextPatente;
    CheckBox checkBoxTerminosYCondiciones;
    String errorCodigo = "";
    ArrayList<TokenApp> codigos = new ArrayList<TokenApp>();
    boolean codigoCorrecto = false;
    TokenApp tokenApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        botonCancelar = findViewById(R.id.cancelar);
        botonRegistrar = findViewById(R.id.aceptar);

        textViewNombre = findViewById(R.id.nombre);
        textVieCodigo = findViewById(R.id.codigo);
        textViewApellido = findViewById(R.id.apellido);
        textViewEmail = findViewById(R.id.email);
        textViewRepeatEmail = findViewById(R.id.repeatEmail);
        textViewContacto = findViewById(R.id.contacto);
        etPlannedDate = (EditText) findViewById(R.id.etPlannedDate);
        checkBoxTerminosYCondiciones = (CheckBox) findViewById(R.id.checkBoxTerminoYCondicione);
        checkBoxTerminosYCondiciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //String s = "Estado: " + (checkBoxTerminosYCondiciones.isChecked() ? "Marcado" : "No Marcado");
                //Toast.makeText(RegistrarActivity.this, s, Toast.LENGTH_LONG).show();
                if(checkBoxTerminosYCondiciones.isChecked()){
                    botonRegistrar.setClickable(true);
                    botonRegistrar.setEnabled(true);
                    botonRegistrar.setBackgroundColor(Color.rgb(255,70,11));
                }else{
                    botonRegistrar.setClickable(false);
                    botonRegistrar.setEnabled(false);
                    botonRegistrar.setBackgroundColor(Color.GRAY);
                }
            }
        });
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.etPlannedDate:
                        showDatePickerDialog(etPlannedDate);
                        break;
                }
            }
        });
        textViewPassword = findViewById(R.id.password);
        getTextViewPassword2 = findViewById(R.id.password2);

        textViewPassword.setTransformationMethod(new PasswordTransformationMethod());
        getTextViewPassword2.setTransformationMethod(new PasswordTransformationMethod());

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textVieCodigo.getText().toString().length() > 0){
                    buscarCodigos(textVieCodigo.getText().toString());
                } else {
                    registrarUsuario();
                }
            }
        });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RegistrarActivity.this)
                        .setTitle("Información")
                        .setMessage("Seguro Quieres Cancelar?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;

    }

    public void clickTextView(View view){
        Intent intent=new Intent(RegistrarActivity.this, TerminosCondiciones.class);
        startActivity(intent);
    }



    private void registrarUsuario() {
        final String nombre = textViewNombre.getText().toString();
        //final String fechaNac = etPlannedDate.getText().toString();
        final String apellido = textViewApellido.getText().toString();
        final String codigo = textVieCodigo.getText().toString();
        final String email = textViewEmail.getText().toString().toLowerCase();
        final String repeatEmail = textViewRepeatEmail.getText().toString().toLowerCase();
        final String contacto = textViewContacto.getText().toString();
        String password = textViewPassword.getText().toString();
        String password2 = getTextViewPassword2.getText().toString();
        View focusView = null;
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            textViewPassword.setError(getString(R.string.error_invalid_password));
            focusView = textViewPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            textViewEmail.setError("Ingrese correo electrónico");
            focusView = textViewEmail;
            cancel = true;
        }
        else if (!isEmailValid(email)) {
            textViewEmail.setError(getString(R.string.error_invalid_email));
            focusView = textViewEmail;
            cancel = true;
        } else if (!isEmailValid(repeatEmail)) {
            textViewRepeatEmail.setError(getString(R.string.error_invalid_email));
            focusView = textViewRepeatEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(repeatEmail)) {
            textViewRepeatEmail.setError("Ingrese correo electrónico");
            focusView = textViewRepeatEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textViewRepeatEmail.setError(getString(R.string.error_invalid_email));
            focusView = textViewRepeatEmail;
            cancel = true;
        } else if (!isEmailValid(repeatEmail)) {
            textViewRepeatEmail.setError(getString(R.string.error_invalid_email));
            focusView = textViewRepeatEmail;
            cancel = true;
        }

        if (!email.equals(repeatEmail)){
            textViewEmail.setError(getString(R.string.error_invalid_email_do_not_match));
            focusView = textViewEmail;
            cancel = true;
        }
        if (isContactValid(contacto)) {
            textViewContacto.setError("El número debe ser de 9 dígitos");
            focusView = textViewContacto;
            cancel = true;
        }else if(!isNumeric(contacto)){
            textViewContacto.setError("Solo se deben ingresar números");
            focusView = textViewContacto;
            cancel = true;
        }
        if (TextUtils.isEmpty(nombre)) {
            textViewNombre.setError("Ingrese nombre");
            focusView = textViewNombre;
            cancel = true;
        }
        if (TextUtils.isEmpty(apellido)) {
            textViewApellido.setError("Ingrese apellido");
            focusView = textViewApellido;
            cancel = true;
        }
        /*
        if (TextUtils.isEmpty(fechaNac)) {
            etPlannedDate.setError("Ingrese fecha de nacimiento");
            focusView = etPlannedDate;
            cancel = true;
        }
        */
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(password2)) {
            getTextViewPassword2.setError("Ingrese una contraseña");
            textViewPassword.setError("Ingrese una contraseña");
            focusView = textViewPassword;
            cancel = true;
        }else if (TextUtils.isEmpty(password)) {
            textViewPassword.setError("Ingrese una contraseña 1");
            focusView = textViewPassword;
            cancel = true;
        }else if (TextUtils.isEmpty(password2)) {
            getTextViewPassword2.setError("Ingrese una contraseña 2");
            focusView = getTextViewPassword2;
            cancel = true;
        }else if (!passwordIguales(password, password2)) {
            getTextViewPassword2.setError("Contraseñas no coinciden");
            focusView = getTextViewPassword2;
            cancel = true;
        }
        if ((codigo.length() > 0) && !verificarCodigo(codigo)) {
            textVieCodigo.setError(errorCodigo);
            focusView = textVieCodigo;
            cancel = true;
        }
        /*else if (mTagContainerLayout.getTags().size() <= 0) {
            editTextPatente.setError("Debe ingresar al menos una patente");
            focusView = editTextPatente;
            cancel = true;
        }
        */
        if (cancel) {
            focusView.requestFocus();
        } else {
            createUser(email,password,nombre,apellido, contacto);
        }

    }

    public void createUser(String email, String password, String nombre, String apellido, String contacto){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = task.getResult().getUser().getUid();
                    UsuarioAndroid usuario;

                    if(tokenApp != null && tokenApp.getToken() != null && tokenApp.getToken() != ""){
                        usuario = new UsuarioAndroid(userId, nombre, apellido, email.toLowerCase(), "+56 9" + contacto, password, "1", tokenApp.getAccountVersion());
                        usuario.setTokenApp(tokenApp.getToken());
                    } else {
                        if(BuildConfig.FLAVOR.equalsIgnoreCase("knox")){
                            usuario = new UsuarioAndroid(userId, nombre, apellido, email.toLowerCase(), "+56 9" + contacto, password, "1", "sinAlertas");
                        } else {
                            usuario = new UsuarioAndroid(userId, nombre, apellido, email.toLowerCase(), "+56 9" + contacto, password, "1", "sinAlertas");
                        }
                    }

                    agregarUsuarioFirebase(usuario);
                    ejecutarLoginActivity();

                } else {
                    Toast.makeText(RegistrarActivity.this, "Este email ya posee una cuenta activa.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void agregarUsuarioFirebase(UsuarioAndroid usuario) {
        //crea un usuario en firebase (costo de escritura 1)
        db.collection(Referencias.USUARIO).document(usuario.getEmail())
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("datos", "DocumentSnapshot successfully written!");
                        Toast.makeText(RegistrarActivity.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                        creaContadorPorUsuario(0, usuario.getEmail());

                        if(tokenApp != null && tokenApp.getToken() != null && tokenApp.getToken() != ""){
                            UsuariosTokenApp usuariosTokenApp = new UsuariosTokenApp(usuario.getEmail());
                            //actualizo el contador de usos del token (costo de escritura 1)
                            db.collection("tokenApp").document(tokenApp.getId()).update("usos", FieldValue.increment(1));
                            //actualizo el usuario que uso el token (costo de escritura 1)
                            db.collection("tokenApp").document(tokenApp.getId()).collection("usuarios").add(usuariosTokenApp);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarActivity.this, "No se pudo crear usuario", Toast.LENGTH_SHORT).show();
                        Log.v("datos", "Error writing document", e);
                    }
                });
    }

    //crea el contador por usuario por primera y unica vez al momento de registrarse
    public void creaContadorPorUsuario(final int i, String emailUsuario){
        //inicializa el contador total de patentes escaneadas por usuario (costo de lectura 1)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuario).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot == null) {
                    Log.v("entreasad", "entras etsa null");
                }
                if (!documentSnapshot.exists()) {
                    Log.v("entreasad", "entras etsa if");
                    //inicializa el contador total de patentes escaneadas por usuario (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuario).set(new EmailTotal(emailUsuario,0));
                    //inicializa el contador total de patentes robadas por usuario (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ROBADAVISTA).collection(Referencias.PORUSUARIO).document(emailUsuario).set(new EmailTotal(emailUsuario,0));
                    //inicializa el contador total de patentes lista negra por usuario (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.LISTANEGRAVISTA).collection(Referencias.PORUSUARIO).document(emailUsuario).set(new EmailTotal(emailUsuario,0) );
                } else {
                    Log.v("entreasad", "NO entras etsa else");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (i<5){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    creaContadorPorUsuario(i+1, emailUsuario);
                }
            }
        });

    }

    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    private void ejecutarLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean passwordIguales(String ps1, String ps2) {
        if (ps1.equals(ps2)) {
            return true;
        }
        return false;
    }

    private boolean verificarCodigo(String codigo) {
        for (TokenApp tokenApp : codigos) {
            if(codigo.equalsIgnoreCase(tokenApp.getToken())){
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
                this.tokenApp = tokenApp;
                codigoCorrecto = true;
                return true;
            }
        }
        errorCodigo = "Código inválido";
        return false;
    }

    public void buscarCodigos(String codigo){
        //busco el token en coleccion tokenApp (costo de lectura 1)
        db.collection("tokenApp").whereEqualTo("token",codigo).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                        TokenApp tokenApp = queryDocumentSnapshot.toObject(TokenApp.class);
                        tokenApp.setId(queryDocumentSnapshot.getId());
                        codigos.add(tokenApp);
                    }
                    registrarUsuario();
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isContactValid(String contact) {
        //TODO: Replace this with your own logic
        return contact.length() != 9;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }
}


