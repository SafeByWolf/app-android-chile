package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarPerfilActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button botonRegistrar;
    Button botonCancelar;
    TextView textViewNombre;
    TextView textViewApellido;
    TextView textViewEmail;
    TextView textViewRepeatEmail;
    TextView textViewContacto;
    EditText etPlannedDate;
    EditText textViewPassword;
    TextView getTextViewPassword2;
    String emailUsuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailUsuarioFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(EditarPerfilActivity.this, Referencias.CORREO);

        botonCancelar = findViewById(R.id.cancelar);
        botonRegistrar = findViewById(R.id.aceptar);

        textViewNombre = findViewById(R.id.nombre);
        textViewApellido = findViewById(R.id.apellido);
        textViewContacto = findViewById(R.id.contacto);

        String nombre = getIntent().getExtras().getString("nombre");
        String apellido = getIntent().getExtras().getString("apellido");
        String telefono = getIntent().getExtras().getString("telefono");

        textViewNombre.setText(nombre);
        textViewApellido.setText(apellido);
        if(telefono.length() == 14){
            telefono = telefono.substring(4,14);
        } else {
            telefono = "";
        }

        textViewContacto.setText(telefono);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarUsuario();
            }
        });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditarPerfilActivity.this)
                        .setTitle("Información")
                        .setMessage("¿Seguro quieres cancelar?")
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
    }

    private boolean passwordIguales(String ps1, String ps2) {
        if (ps1.equals(ps2)) {
            return true;
        }
        return false;
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

    private void editarUsuario() {
        View focusView = null;
        boolean cancel = false;
        final String nombre = textViewNombre.getText().toString();
        final String apellido = textViewApellido.getText().toString();
        final String contacto = textViewContacto.getText().toString();

        if (isContactValid(contacto)) {
            textViewContacto.setError("EL número debe ser de 9 dígitos");
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            //actualiza los datos de un usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("nombre",nombre,"apellido",apellido,"contacto","+56 9"+contacto).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    Toast.makeText(EditarPerfilActivity.this, "Perfil editado correctamente.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditarPerfilActivity.this, "No se pudo editar perfil, intente mas tarde.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}