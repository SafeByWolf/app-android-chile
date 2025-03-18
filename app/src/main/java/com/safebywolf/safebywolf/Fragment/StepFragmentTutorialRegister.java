package com.safebywolf.safebywolf.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safebywolf.safebywolf.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragmentTutorialDetector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragmentTutorialRegister extends Fragment implements Step {
    TextView textViewTitle;
    TextView textViewContent;
    ImageView imageViewImagenTutorial;
    ImageView imageViewLogo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int position;
    private String mParam2;

    public StepFragmentTutorialRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepFragmentSample.
     */
    // TODO: Rename and change types and number of parameters
    public static StepFragmentTutorialDetector newInstance(String param1, String param2) {
        StepFragmentTutorialDetector fragment = new StepFragmentTutorialDetector();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_tutorial_register, container, false);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        imageViewImagenTutorial = (ImageView) view.findViewById(R.id.imageViewTutorial);
        imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogo);
        imageViewLogo.setImageDrawable(getResources().getDrawable(R.drawable.logoredondeado));

        if(position==0){
            textViewTitle.setText("Bienvenido a SafeByWolf\n");
            textViewContent.setText("SafeByWolf es una aplicación móvil capaz de escanear patentes de " +
                    "vehículos de manera automática, utilizando la cámara del celular de manera constante y sin la interacción del usuario" +
                    ". Cada patente es enviada y analizada " +
                    "en tiempo real en nuestros servidores, de esta manera, en el caso que la patente presente " +
                    "alguna irregularidad, el usuario recibirá una alerta preventiva.");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.modelo));
        } else if(position==1){
            textViewTitle.setText("Crear Cuenta");
            textViewContent.setText("Para comenzar, necesitas crear una cuenta.");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.registrarse));

        } else if(position==2){
            textViewTitle.setText("Registro");
            textViewContent.setText("Debes completar todos los datos solicitados. " +
                    "Verifica que tu correo electrónico esté correcto, " +
                    "ya que este será tu usuario.\n");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.registro));
        } else if(position==3){
            textViewTitle.setText("Ingreso");
            textViewContent.setText("Para ingresar a tu cuenta de usuario, debes escribir el correo electrónico " +
                    "y la contraseña que utilizaste al momento de registrarte.");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ingresar));
        } else if(position==4){
            textViewTitle.setText("¡Muy bien!");
            textViewContent.setText("Has llegado el final del tutorial de registro.\n\n" +
                    "Ahora hazlo tú.");
            textViewContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //imageViewLogo.setScaleX(20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 800);
            imageViewLogo.setLayoutParams(layoutParams);

            textViewTitle.setGravity(Gravity.CENTER_VERTICAL);
            textViewTitle.setPadding(0,150,0,150);


            //imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ingresar));
        }

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}