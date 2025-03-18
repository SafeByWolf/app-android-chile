package com.safebywolf.safebywolf.Activity.Encuesta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;

import java.util.List;

public class EncuestaPostergacion extends DialogFragment {

    public EncuestaPostergacion() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_postergada, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.btnEncuestaPostergada), getActivity());

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnEncuestaPostergada = view.findViewById(R.id.btnEncuestaPostergada);
        btnEncuestaPostergada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar todos los DialogFragments
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof DialogFragment) {
                        ((DialogFragment) fragment).dismiss();
                    }
                }
            }
        });

        return dialog;
    }
}
