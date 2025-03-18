package com.safebywolf.safebywolf.Activity.Encuesta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;

import java.util.List;

public class EncuestaFinalizada extends DialogFragment {

    private EncuestaManager encuestaManager;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    TextView textViewEncuestaFinalizada;

    public EncuestaFinalizada(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_finalizacion, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.btnEncuestaFinalizada), getActivity());
        Utils.touchListener(view.findViewById(R.id.linearLayoutEncuestaFinalizada), getActivity());
        Utils.touchListener(view.findViewById(R.id.textViewEncuestaFinalizada), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        encuestaManager = EncuestaManager.getInstacia();

        Button btnFinalizado = view.findViewById(R.id.btnEncuestaFinalizada);
        textViewEncuestaFinalizada = view.findViewById(R.id.textViewEncuestaFinalizada);

        String textoFinalizacionEncuesta = Utils.leerValorString(getContext(),Referencias.TEXTOFINALIZACIONENCUESTA);

        textViewEncuestaFinalizada.setText(Html.fromHtml(textoFinalizacionEncuesta));

        btnFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
            }
        });

        return dialog;
    }

    private void dismissAll(){
        encuestaManager.setEncuestaAbierta(false);
        // Cerrar todos los DialogFragments
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                ((DialogFragment) fragment).dismiss();
            }
        }
    }
}
