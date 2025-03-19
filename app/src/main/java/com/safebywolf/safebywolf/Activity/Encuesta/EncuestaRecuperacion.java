package com.safebywolf.safebywolf.Activity.Encuesta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.R;

import java.io.Serializable;

public class EncuestaRecuperacion extends DialogFragment {

    private EncuestaPatrullero encuesta;
    private String json;

    public EncuestaRecuperacion() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_fue_recuperado, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.btnEncuestaRecuperadoSi), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaRecuperadoNo), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaRecuperadoVolver), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnSi = view.findViewById(R.id.btnEncuestaRecuperadoSi);
        Button btnNo = view.findViewById(R.id.btnEncuestaRecuperadoNo);
        Button btnVolver = view.findViewById(R.id.btnEncuestaRecuperadoVolver);
        TextView textViewSubtituloEncuestaRecuperado = view.findViewById(R.id.textViewSubtituloEncuestaRecuperado);
        ImageView imageViewEncuestaRecuperado = view.findViewById(R.id.imageViewEncuestaRecuperado);

        // Obtener el parámetro del Bundle
        Bundle bundle = getArguments();
        if(bundle.containsKey("Encuesta")) {
            encuesta = (EncuestaPatrullero) bundle.getSerializable("Encuesta");
            Gson gson = new Gson();
            json = gson.toJson(encuesta);
            Log.v("ENCUESTAPATRULLERO","EncuestaRecuperacion:"+json);
            try {
                Glide.with(this)
                        .load(encuesta.getImagenPrincipalVehiculo().toString())
                        .into(imageViewEncuestaRecuperado);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("ENCUESTAPATRULLERO", "Error en la descarga de la imagen: " + e.getMessage());
                imageViewEncuestaRecuperado.setVisibility(View.INVISIBLE);
            }
            // Object objetoDeserializado = SerializableUtils.deserializarObjeto(encuesta);
        }

        textViewSubtituloEncuestaRecuperado.setText(encuesta.getPatente());

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Bundle y agregar un parámetro
                encuesta.setRecuperado(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaObservacion dialogFragment = new EncuestaObservacion();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encuesta.setRecuperado(false);
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaObservacion dialogFragment = new EncuestaObservacion();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encuesta.setRecuperado(false);
                dismiss();
            }
        });
        return dialog;
    }

}
