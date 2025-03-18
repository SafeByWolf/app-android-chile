package com.safebywolf.safebywolf.Activity.Encuesta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.EncuestaApi;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.R;

import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EncuestaObservacionSecundaria extends DialogFragment {

    private String json;

    private EncuestaPatrullero encuesta;

    private EncuestaManager encuestaManager;

    public EncuestaObservacionSecundaria() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_observacion_secundaria, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.observacionEscritaSecundaria), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaObservacionSiguienteSecundaria), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaObservacionVolverSecundaria), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        encuestaManager = EncuestaManager.getInstacia();

        Button btnSiguiente = view.findViewById(R.id.btnEncuestaObservacionSiguienteSecundaria);
        Button btnVolver = view.findViewById(R.id.btnEncuestaObservacionVolverSecundaria);
        EditText observacionEncuesta = view.findViewById(R.id.observacionEscritaSecundaria);
        TextView patente = view.findViewById(R.id.textViewSubtituloEncuestaObservacionSecundario);

        // Obtener el parámetro del Bundle
        Bundle bundle = getArguments();
        if(bundle.containsKey("Encuesta")) {
            encuesta = (EncuestaPatrullero) bundle.getSerializable("Encuesta");
            patente.setText(encuesta.getPatente());
            Gson gson = new Gson();
            json = gson.toJson(encuesta);
            Log.v("ENCUESTAPATRULLERO","EncuestaRecuperacion:"+json);
        }

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String observacion = observacionEncuesta.getText().toString();

                encuesta.setObservacion(observacion);
                encuesta.setFinalizado(true);

                String API_URL = BuildConfig.BUILD_TYPE.equalsIgnoreCase("release") ? getResources().getString(R.string.prod_api) : getResources().getString(R.string.dev_api);

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
                        encuestaManager.setEncuestaAbierta(false);
                        if (response.code() == 200) {
                            Toast.makeText(EncuestaObservacionSecundaria.this.getActivity(), "Encuesta enviada exitosamente!", Toast.LENGTH_SHORT).show();
                            // Crear un Bundle y agregar un parámetro
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Encuesta", encuesta);
                            // Mostrar un DialogFragment con el parámetro
                            EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                            dialogFragment.setArguments(bundle);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
                        } else {
                            Log.v("ENCUESTAPATRULLERO", "onFailure:" + response.toString());
                            Toast.makeText(EncuestaObservacionSecundaria.this.getActivity(), "Error al enviar la encuesta", Toast.LENGTH_SHORT).show();
                            encuestaManager.agregarEncuestaEnvioPendiente(encuesta);
                            // Crear un Bundle y agregar un parámetro
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Encuesta", encuesta);
                            // Mostrar un DialogFragment con el parámetro
                            EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                            dialogFragment.setArguments(bundle);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");

                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(EncuestaObservacionSecundaria.this.getActivity(), "Fallo al enviar la encuesta", Toast.LENGTH_SHORT).show();
                        Log.v("ENCUESTAPATRULLERO","onFailure:" + t.toString());
                        encuestaManager.agregarEncuestaEnvioPendiente(encuesta);
                        // Crear un Bundle y agregar un parámetro
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Encuesta", encuesta);
                        // Mostrar un DialogFragment con el parámetro
                        EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
                    }
                });

                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encuesta.setObservacion("");
                dismiss();
            }
        });

        return dialog;
    }
}
