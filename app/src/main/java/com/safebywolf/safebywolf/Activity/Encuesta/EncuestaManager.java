package com.safebywolf.safebywolf.Activity.Encuesta;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.EncuestaApi;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.Model.PatenteRobadaVista;
import com.safebywolf.safebywolf.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EncuestaManager {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static EncuestaManager instancia;
    private List<EncuestaPatrullero> encuestasPatrulleros;
    private List<EncuestaPatrullero> encuestasEnvioPendientes = new ArrayList<>();
    private List<EncuestaPatrullero> encuestasAntiguas;
    private DetectorActivity contexto;
    private Timer timer = new Timer();
    private int tiempoActivacionEncuestaMinutos = 30;
    private int tiempoVerificacionEncuestasPendientes =30;
    private int intentosParaSaltarEncuesta = 3;
    private Boolean tieneGrupoComuna = false;
    private Boolean encuestaAbierta = false;


    private EncuestaManager(DetectorActivity context) {
        encuestasPatrulleros = new ArrayList<>();
        encuestasAntiguas = new ArrayList<>();
        contexto = context;
    }

    public static EncuestaManager setInstacia(DetectorActivity context) {
        if (instancia == null) {
            instancia = new EncuestaManager(context);
        }
        return instancia;
    }

    public static EncuestaManager getInstacia() {
        return instancia;
    }

    public void marcarVehiculoConEncargo(DetectorActivity context, PatenteRobadaVista patenteRobada) {
        this.tiempoActivacionEncuestaMinutos = Utils.leerValorInt(contexto, Referencias.TIEMPOACTIVACIONENCUESTA, tiempoActivacionEncuestaMinutos);
        this.tiempoVerificacionEncuestasPendientes = Utils.leerValorInt(contexto, Referencias.TIEMPOVERIFICACIONENCUESTASPENDIENTES, tiempoVerificacionEncuestasPendientes);
        this.intentosParaSaltarEncuesta = Utils.leerValorInt(contexto, Referencias.INTENTOSSKIPENCUESTA, intentosParaSaltarEncuesta);

        Log.v("ENCUESTAPATRULLERO","SE MARCO EL VEHICULO CON ENCARGO");
        Log.v("ENCUESTAPATRULLERO","los datos entregados para tiempo de activacion de encuesta es: "+this.tiempoActivacionEncuestaMinutos+", el tiempo para saltar la encuesta es: "+ this.intentosParaSaltarEncuesta + ", tiempo para verificar encuesta pendientes:"+this.tiempoVerificacionEncuestasPendientes);
        if (!existePatente(patenteRobada)) {
            String correoPatrullero = Utils.leerValorString(context, Referencias.CORREO);
            String grupos = Utils.leerValorSetString(context, Referencias.GRUPO).toString();
            String id = firebaseFirestore.collection(Referencias.ENCUESTAS).document().getId();
            List<String> gruposList = Arrays.asList(grupos.split("\\s*,\\s*")); // Separa por comas, eliminando espacios
            EncuestaPatrullero encuestaNueva = new EncuestaPatrullero(id, correoPatrullero, patenteRobada.getPatente(), "", false, gruposList, patenteRobada.getUrlImagen(), intentosParaSaltarEncuesta, false, false, "");
            enviarEncuestaProvisoria(encuestaNueva);
            Log.v("ENCUESTAPATRULLERO", "SE AGREGO LA ENCUESTA AL ARREGLO");
            encuestasPatrulleros.add(0,encuestaNueva);
            Log.v("ENCUESTAPATRULLERO", "SE PROGRAMA LA ENCUESTA");
            programarEncuesta(context);
        }
    }

    private void programarEncuesta(DetectorActivity context) {
        Log.v("ENCUESTAPATRULLERO","HOLA VOY A PROGRAMAR LA ENCUESTA");

        if (encuestaAbierta) {
            Log.v("ENCUESTAPATRULLERO","Encuesta ya esta abierta, esperando a que se cierre..");
        }

        if (encuestasPatrulleros.isEmpty()) {
            Log.v("ENCUESTAPATRULLERO","No hay encuestas en cola.");
        }

        encuestaAbierta = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (!encuestasPatrulleros.isEmpty()) {
                        Log.v("ENCUESTAPATRULLERO","hay encuestas en cola.");
                        abrirEncuesta(context,encuestasPatrulleros.get(0));
                        Log.v("ENCUESTAPATRULLERO","Elimine la encuesta de encuestasPatrulleros");
                        encuestasPatrulleros.remove(0);
                    } else {
                        Log.v("ENCUESTAPATRULLERO","No hay encuestas en cola.");
                    }
                }
            }
        }, (long) tiempoActivacionEncuestaMinutos * 60 * 1000);
    }


    private void abrirEncuesta(final DetectorActivity context, final EncuestaPatrullero encuesta) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    // Crear un Bundle y agregar un parámetro
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Encuesta", encuesta);
                    // Mostrar un DialogFragment con el parámetro
                    EncuestaPrincipal dialogFragment = new EncuestaPrincipal();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(context.getSupportFragmentManager(), "dialog_fragment");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ENCUESTAPATRULLERO", "Error al abrir la EncuestaPrincipal: " + e.getMessage());
                }
            }
        });
    }

    public void postergarEncuesta(EncuestaPatrullero encuesta) {
        Toast.makeText(contexto, "Se volverá a preguntar en 15 minutos", Toast.LENGTH_SHORT).show();
        Log.v("ENCUESTAPATRULLERO","onActivityResult - esperando para postergar la encuesta.");
        encuestasPatrulleros.add(encuesta);
        if(encuestasPatrulleros.size() == 1) {
            programarEncuesta(contexto);
        }
    }

    public void agregarEncuestaEnvioPendiente(EncuestaPatrullero encuestaPatrullero) {
        Log.v("ENCUESTAPATRULLERO","se agrego encuesta a envios pendientes");
        encuestasEnvioPendientes.add(encuestaPatrullero);
        iniciarVerificacionEncuestasPendientes();
    }

    private void enviarEncuestaAlBackend(EncuestaPatrullero encuesta) {
        encuestaAbierta = false;
        Log.v("ENCUESTAPATRULLERO", "Se envian las encuestas al backend...");
        String API_URL = "";

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
            //API_URL = getResources().getString(R.string.prod_api);
            API_URL = contexto.getResources().getString(R.string.prod_api);
            Log.v("release", "prod");
        } else {
            Log.v("release", "dev");
            API_URL = contexto.getResources().getString(R.string.dev_api);
            //API_URL = getResources().getString(R.string.dev_api);
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        EncuestaApi encuestaApi = retrofit.create(EncuestaApi.class);

        Gson gson = new Gson();
        String encuestaJson = gson.toJson(encuesta);

        // Crear el cuerpo de la solicitud HTTP
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), encuestaJson);
        Call callTime = encuestaApi.enviarEncuesta(requestBody);

        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    Toast.makeText(contexto, "Encuesta pendiente enviada...", Toast.LENGTH_SHORT).show();
                    Log.v("ENCUESTAPATRULLERO", "Si se pudo enviar...");
                    encuestasEnvioPendientes.remove(encuesta);
                } else {
                    Log.v("ENCUESTAPATRULLERO", "onFailure:" + response.toString());
                    Log.v("ENCUESTAPATRULLERO", "No se pudo enviar...");
                    Toast.makeText(contexto, "Error al enviar la encuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(contexto, "Fallo al enviar la encuesta", Toast.LENGTH_SHORT).show();
                Log.v("ENCUESTAPATRULLERO","onFailure:" + t.toString());
                Log.v("ENCUESTAPATRULLERO", "Otro fallo al enviar...atrooooh");
            }
        });

    }

    public void iniciarVerificacionEncuestasPendientes() {
        Log.v("ENCUESTAPATRULLERO","se inicia la verificación de encuestas pendientes...");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarEncuestasPendientes();
            }
        }, 0, (long) this.tiempoVerificacionEncuestasPendientes *60 * 1000);
    }

    private void verificarEncuestasPendientes() {
        Log.v("ENCUESTAPATRULLERO", "Se verifica si existen encuestas pendientes");
        if(!encuestasEnvioPendientes.isEmpty()) {
            Log.v("ENCUESTAPATRULLERO", "Si existen encuestas pendientes..");
            for (EncuestaPatrullero encuesta: encuestasEnvioPendientes) {
                enviarEncuestaAlBackend(encuesta);
            }
        }
    }

    public void enviarEncuestaProvisoria(EncuestaPatrullero encuesta) {
        String API_URL = BuildConfig.BUILD_TYPE.equalsIgnoreCase("release") ? contexto.getResources().getString(R.string.prod_api) : contexto.getResources().getString(R.string.dev_api);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        EncuestaApi encuestaApi = retrofit.create(EncuestaApi.class);

        Gson gson = new Gson();
        String encuestaJson = gson.toJson(encuesta);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), encuestaJson);
        Call callTime = encuestaApi.enviarEncuesta(requestBody);
        Log.v("ENCUESTAPATRULLERO","esta es la encuesta que se envia provisoriamente:" + encuestaJson.toString());
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    //Toast.makeText(contexto, "Reporte enviado exitosamente!", Toast.LENGTH_SHORT).show();
                    Log.v("ENCUESTAPATRULLERO", "Se envió la encuesta....");
                } else {
                    Toast.makeText(contexto, "Error al enviar la encuesta", Toast.LENGTH_SHORT).show();
                    Log.v("ENCUESTAPATRULLERO", "No se envio la encuesta, error:" + response.code());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("ENCUESTAPATRULLERO", "Fallo al enviar la encuesta: " + t);
            }
        });
    }

    public Boolean existePatente(PatenteRobadaVista patente) {

        boolean existe = buscarPatenteEnLista(encuestasPatrulleros, patente);
        return existe || buscarPatenteEnLista(encuestasEnvioPendientes, patente);
    }

    private boolean buscarPatenteEnLista(List<EncuestaPatrullero> encuestas, PatenteRobadaVista patente) {
        for (EncuestaPatrullero item : encuestas) {
            if (item.getPatente().equalsIgnoreCase(patente.getPatente())) {
                return true;
            }
        }
        return false;
    }

    public void getEncuestasNoFinalizadas(DetectorActivity context, String emailUsuario) {
        String API_URL = BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")
                ? contexto.getResources().getString(R.string.prod_api)
                : contexto.getResources().getString(R.string.dev_api);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EncuestaApi encuestaApi = retrofit.create(EncuestaApi.class);

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("emailUsuario", emailUsuario);

        Call<List<EncuestaPatrullero>> call = encuestaApi.getEncuestasNoFinalizadas(requestBodyMap);

        call.enqueue(new Callback<List<EncuestaPatrullero>>() {
            @Override
            public void onResponse(Call<List<EncuestaPatrullero>> call, Response<List<EncuestaPatrullero>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EncuestaPatrullero> encuestas = response.body();
                    encuestasPatrulleros.addAll(encuestas);
                    abrirSiguienteEncuesta(context);
                    Log.v("ENCUESTAPATRULLERO", "Encuestas no finalizadas obtenidas y guardadas: " + encuestas.size());
                } else {
                    Log.e("ENCUESTAPATRULLERO", "Error en la respuesta: " + response.code() + " - " + response.message());
                    Log.e("ENCUESTAPATRULLERO", "Error body: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<EncuestaPatrullero>> call, Throwable t) {
                Log.e("ENCUESTAPATRULLERO", "Error al realizar la solicitud: " + t.getMessage());
            }
        });
    }

    public void setTieneGrupoComuna(Boolean isComuna) {this.tieneGrupoComuna = isComuna;}

    public Boolean getTieneGrupoComuna() {return tieneGrupoComuna;}

    public void setEncuestaAbierta(Boolean encuestaAbierta) {this.encuestaAbierta = encuestaAbierta;}

    public Boolean getEncuestaAbierta() {return this.encuestaAbierta;}


    private void abrirSiguienteEncuesta(DetectorActivity context) {
        if (!encuestasAntiguas.isEmpty()) {
            EncuestaPatrullero encuesta = encuestasAntiguas.remove(0);
            abrirEncuesta(contexto, encuesta);
        } else if (!encuestasPatrulleros.isEmpty()) {
            EncuestaPatrullero encuesta = encuestasPatrulleros.remove(0);
            abrirEncuesta(context, encuesta);
        } else {
            Log.v("ENCUESTAPATRULLERO","No hay mas encuestas pendientes");
        }
    }

}
