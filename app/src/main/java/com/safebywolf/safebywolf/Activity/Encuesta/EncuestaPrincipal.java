package com.safebywolf.safebywolf.Activity.Encuesta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.R;

import java.util.Date;

public class EncuestaPrincipal extends DialogFragment {

    private EncuestaPatrullero encuesta;
    TextView textViewSubtituloEncuestaPrincipal;
    ImageView imageViewPatente;
    Button btnSi, btnNo, btnNose, btnPreguntarDespues;
    private EncuestaManager encuestaManager;
    View view;


    public EncuestaPrincipal() {}


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_principal, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.btnEncuestaPrincipalSi), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaPrincipalNo), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaPrincipalNose), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaPrincipalMastarde), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        encuestaManager = EncuestaManager.getInstacia();

        btnSi = view.findViewById(R.id.btnEncuestaPrincipalSi);
        btnNo = view.findViewById(R.id.btnEncuestaPrincipalNo);
        btnNose = view.findViewById(R.id.btnEncuestaPrincipalNose);
        btnPreguntarDespues = view.findViewById(R.id.btnEncuestaPrincipalMastarde);
        textViewSubtituloEncuestaPrincipal = view.findViewById(R.id.textViewSubtituloEncuestaPrincipal);
        imageViewPatente = view.findViewById(R.id.imageViewPatente);

        // Obtener el parámetro del Bundle
        Bundle bundle = getArguments();
        if(bundle.containsKey("Encuesta")) {
            encuesta = (EncuestaPatrullero) bundle.getSerializable("Encuesta");
            textViewSubtituloEncuestaPrincipal.setText(encuesta.getPatente());
            try {
                Glide.with(this)
                        .load(encuesta.getImagenPrincipalVehiculo().toString())
                        .into(imageViewPatente);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("ENCUESTAPATRULLERO", "Error en la descarga de la imagen: " + e.getMessage());
                imageViewPatente.setVisibility(View.INVISIBLE);
            }
        }

        if (encuesta.getIntentos() <= 0) {
            btnPreguntarDespues.setVisibility(View.INVISIBLE);
        }

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(EncuestaPrincipal.this, EncuestaRecuperacion.class);
                intent.putExtra("Encuesta", (Serializable) encuesta);
                startActivity(intent);
                 */
                encuesta.setRespuesta("Si");
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaRecuperacion dialogFragment = new EncuestaRecuperacion();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encuesta.setRecuperado(false);
                encuesta.setRespuesta("No");
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaObservacionSecundaria dialogFragment = new EncuestaObservacionSecundaria();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        btnNose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encuesta.setRecuperado(false);
                encuesta.setRespuesta("No se");
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaObservacionSecundaria dialogFragment = new EncuestaObservacionSecundaria();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        btnPreguntarDespues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encuesta.setRespuesta("Preguntar despues");
                Log.v("ENCUESTAPATRULLERO","Preguntar despues....");
                encuesta.setIntentos(encuesta.getIntentos()-1);
                Log.v("ENCUESTAPATRULLERO","Se setean los intentos de la encuesta:"+encuesta);
                encuestaManager.postergarEncuesta(encuesta);
                dismiss();
            }
        });

        return dialog;
    }

}

