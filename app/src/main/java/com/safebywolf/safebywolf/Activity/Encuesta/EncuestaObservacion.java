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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.R;

import java.io.Serializable;

public class EncuestaObservacion extends DialogFragment {

    private EncuestaPatrullero encuesta;
    private String json;

    public EncuestaObservacion(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_observacion, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.observacionEscrita), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaObservacionSiguiente), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaObservacionVolver), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnSiguiente = view.findViewById(R.id.btnEncuestaObservacionSiguiente);
        Button btnVolver = view.findViewById(R.id.btnEncuestaObservacionVolver);
        EditText observacionEncuesta = view.findViewById(R.id.observacionEscrita);
        TextView patente = view.findViewById(R.id.textViewSubtituloEncuestaObservacion);

        // Obtener el parámetro del Bundle
        Bundle bundle = getArguments();
        if(bundle.containsKey("Encuesta")) {
            encuesta = (EncuestaPatrullero) bundle.getSerializable("Encuesta");
            patente.setText(encuesta.getPatente());
            Gson gson = new Gson();
            json = gson.toJson(encuesta);
            Log.v("ENCUESTAPATRULLERO","EncuestaObservacion:"+json);
        }

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacion = observacionEncuesta.getText().toString();
                encuesta.setObservacion(observacion);
                Log.v("ENCUESTAPATRULLERO","EncuestaObservacion:"+json);
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaImagenes dialogFragment = new EncuestaImagenes();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), EncuestaImagenes.TAG);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                encuesta.setObservacion("");
                dismiss();
            }
        });

        return dialog;
    }
}
