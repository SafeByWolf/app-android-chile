package com.safebywolf.safebywolf.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidoContrasenaActivity extends AppCompatActivity {
    Button buttonCancelar;
    Button buttonAceptar;
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final TextView textViewEmail=findViewById(R.id.email);
        buttonAceptar=findViewById(R.id.aceptar);
        buttonCancelar=findViewById(R.id.cancelar);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OlvidoContrasenaActivity.this)
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

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel=false;
                if(textViewEmail.getText().toString().equals("")){
                    textViewEmail.setError(("Ingrese correo"));
                }
                else if (!isEmailValid(textViewEmail.getText().toString())) {
                    textViewEmail.setError(getString(R.string.error_invalid_email));
                    focusView = textViewEmail;
                    cancel = true;
                }
                else if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
                else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = textViewEmail.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(OlvidoContrasenaActivity.this, "Email enviado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            }
        });



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


}
