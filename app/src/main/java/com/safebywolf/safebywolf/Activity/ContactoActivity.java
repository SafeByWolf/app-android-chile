package com.safebywolf.safebywolf.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.ContactoApi;
import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Model.EmailHttp;
import com.safebywolf.safebywolf.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactoActivity extends AppCompatActivity {

    private final ArrayList<String> opcionesContacto = new ArrayList<>();
    private String API_URL;
    private EditText mensajeContactoField, telefonoContactoField, correoContactoField;
    private Spinner spinner;
    private Button botonContacto;
    private String emailUsuarioFirebase;
    ProgressBar loadingSpinner;
    TextView limitTextView;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.emailUsuarioFirebase = Utils.leerValorString(ContactoActivity.this, Referencias.CORREO);
        mensajeContactoField = findViewById(R.id.contactoMensaje);
        spinner = findViewById(R.id.spinnerContacto);
        botonContacto = findViewById(R.id.botonContacto);
        telefonoContactoField = findViewById(R.id.telefonoContacto);
        correoContactoField = findViewById(R.id.correoContacto);
        loadingSpinner = findViewById(R.id.cargandoEnvio);
        limitTextView = findViewById(R.id.limitTextView);

        correoContactoField.addTextChangedListener(textWatcher);
        telefonoContactoField.addTextChangedListener(textWatcher);

        mensajeContactoField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                limitTextView.setText(currentLength + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        this.inicializarOpciones();
    }

    /**
     * Metodo que inicializa las opciones del tipo de contacto a elegir.
     */
    private void inicializarOpciones() {
        this.opcionesContacto.add("Reportar problema");
        this.opcionesContacto.add("Pregunta general");
    }

    private void enviarContacto(View view) {
        botonContacto.setEnabled(false);
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
            API_URL = getResources().getString(R.string.prod_api);
            Log.v("release", "prod");
        } else {
            Log.v("release", "dev");
            API_URL = getResources().getString(R.string.dev_api);
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ContactoApi contactoApi = retrofit.create(ContactoApi.class);
        Log.v("Contacto", "ya pase el retrofit.create:");
        String mensaje = mensajeContactoField.getText().toString();
        String opcionSeleccionada = spinner.getSelectedItem().toString();
        String telefonoContato = String.valueOf(telefonoContactoField.getText());
        String correoContacto = String.valueOf(correoContactoField.getText());
        EmailHttp emailHttp = new EmailHttp(opcionSeleccionada, mensaje, this.emailUsuarioFirebase, correoContacto, telefonoContato);
        Log.v("Contacto", "" + emailHttp);
        Call callTime = contactoApi.enviarCorreo(emailHttp);
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                botonContacto.setEnabled(true);
                botonContacto.setVisibility(view.VISIBLE);
                loadingSpinner.setVisibility(view.INVISIBLE);
                if (response.isSuccessful()) {
                    Log.v("Contacto", "Conexion exitosa");
                    Toast.makeText(ContactoActivity.this, "Correo enviado exitosamente!", Toast.LENGTH_SHORT).show();
                    correoContactoField.setText("");
                    telefonoContactoField.setText("");
                    mensajeContactoField.setText("");
                } else {
                    botonContacto.setEnabled(true);
                    Log.v("Contacto", "Fallo en la conexion");
                    Toast.makeText(ContactoActivity.this, "Correo no enviado.", Toast.LENGTH_SHORT).show();
                    correoContactoField.setText("");
                    telefonoContactoField.setText("");
                    mensajeContactoField.setText("");
                    return;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(ContactoActivity.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                botonContacto.setEnabled(true);
                botonContacto.setVisibility(view.VISIBLE);
                loadingSpinner.setVisibility(view.INVISIBLE);
                correoContactoField.setText("");
                telefonoContactoField.setText("");
                mensajeContactoField.setText("");
                Log.v("Contacto", "" + t.getMessage());
            }
        });
    }

    public void accionarBotonContacto(View view) {
        validarCampos(view);
    }

    private void validarCampos(View view) {
        String correo = correoContactoField.getText().toString().trim();
        String numero = telefonoContactoField.getText().toString().trim();
        String mensaje = mensajeContactoField.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(correo)) {
            correoContactoField.setError(getString(R.string.error_invalid_email));
            focusView = correoContactoField;
            cancel = true;
        } else if (!validarCorreo(correo)) {
            correoContactoField.setError(getString(R.string.error_invalid_email));
            focusView = correoContactoField;
            cancel = true;
        } else if (numero.length() < 9) {
            telefonoContactoField.setError("Ingrese 9 números.");
            focusView = telefonoContactoField;
            cancel = true;
        } else if (numero.isEmpty()) {
            telefonoContactoField.setError("Debe ingresar un número");
            focusView = telefonoContactoField;
            cancel = true;
        } else if (TextUtils.isEmpty(mensaje)) {
            mensajeContactoField.setError("Escriba un mensaje");
            focusView = mensajeContactoField;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            botonContacto.setVisibility(view.INVISIBLE);
            loadingSpinner.setVisibility(view.VISIBLE);
            enviarContacto(view);
        }

        /*
        boolean correoValido = validarCorreo(correo);
        boolean numeroPresente = !numero.isEmpty();
        boolean numeroValido = numero.length() >= 9;
        botonContacto.setEnabled(correoValido && numeroPresente && numeroValido);
         */
    }

    private boolean validarCorreo(String correo) {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}