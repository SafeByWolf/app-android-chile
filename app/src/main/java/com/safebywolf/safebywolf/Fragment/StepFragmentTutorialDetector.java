package com.safebywolf.safebywolf.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragmentTutorialDetector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragmentTutorialDetector extends Fragment implements Step {
    TextView textViewTitle;
    TextView textViewContent;
    ImageView imageViewImagenTutorial;
    ImageView imageViewLogo;
    Button buttonAlerta;
    Button buttonAlerta2;
    Button buttonAlerta3;
    Button buttonAlerta4;
    LinearLayout linearLayoutButtonAlerta;
    LinearLayout linearLayoutButtonAlerta2;
    LinearLayout linearLayoutButtonAlerta3;
    LinearLayout linearLayoutButtonAlerta4;
    TextToSpeech textToSpeech;
    AudioManager audioManager;
    String accountVersionFirebase;
    String grupos;
    String patentesEnObsrvacion = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int position;
    private String mParam2;

    public StepFragmentTutorialDetector() {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_tutorial_detector, container, false);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        imageViewImagenTutorial = (ImageView) view.findViewById(R.id.imageViewTutorial);
        imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogo);
        imageViewLogo.setImageDrawable(getResources().getDrawable(R.drawable.logoredondeado));
        linearLayoutButtonAlerta = view.findViewById(R.id.linearLayoutButtonAlerta);
        buttonAlerta = view.findViewById(R.id.buttonAlerta);
        linearLayoutButtonAlerta2 = view.findViewById(R.id.linearLayoutButtonAlerta2);
        buttonAlerta2 = view.findViewById(R.id.buttonAlerta2);
        linearLayoutButtonAlerta3 = view.findViewById(R.id.linearLayoutButtonAlerta3);
        buttonAlerta3 = view.findViewById(R.id.buttonAlerta3);
        linearLayoutButtonAlerta4 = view.findViewById(R.id.linearLayoutButtonAlerta4);
        buttonAlerta4 = view.findViewById(R.id.buttonAlerta4);
        audioManager = (AudioManager) StepFragmentTutorialDetector.this.getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        accountVersionFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(getActivity(), Referencias.ACCVERSION);
        grupos= com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(getActivity(), Referencias.GRUPOSSTRING);


        if(position==0){
            textViewTitle.setText("¡Vas muy bien!");
            textViewContent.setText("Has iniciado sesión correctamente." +
                    " Ahora explicaremos algunas funcionalidades de la aplicación.\n\n" +
                    "A continuación puedes observar la vista principal de la aplicación, donde se realiza la " +
                    "detección de los vehículos y sus respectivas placas patentes.\n");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.general));
        } else if(position==1){
            textViewTitle.setText("Mapa");
            textViewContent.setText("Permite visualizar la ubicación de los vehículos que presentan patentes con alguna irregularidad y que fueron escaneadas por otros usuarios. \n \n" +
                    "Este botón permite cambiar entre el Mapa y la pantalla principal.\n" +
                    "Recibirás alertas de vehículos robados en un radio cercano a tu ubicación\n");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.mapa));
        } else if(position==2){
            textViewTitle.setText("Galería");
            textViewContent.setText("Al presionar el icono que contiene la patente, podrás visualizar la galería de imágenes del vehículo alertado en el mapa." +
                    "");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.galeria));
        } else if(position==3){
            textViewTitle.setText("Autoseguro");
            textViewContent.setText("Presionando este botón accederás directamente a “www.autoseguro.gob.cl”, página oficial del Gobierno dispuesta a la ciudadanía para revisar si un vehículo mantiene o no encargo vigente por robo.\n" +
                    "Esta búsqueda será útil cuando quieras verificar manualmente alguna patente que NO haya sido alertada de manera automática por la aplicación.\n");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.autoseguro));
        }
        else if(position==4){
            textViewTitle.setText("Zoom dinámico/manual");
            textViewContent.setText(Html.fromHtml("<p style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;margin-left:0cm;line-height:107%;font-size:15px;font-family:\"Calibri\",sans-serif;text-align:justify;'><span style=\"font-family:Roboto;color:#202124;background:white;\">El zoom permite alejar o acercar la imagen que se est&aacute; analizando. La aplicación cuenta con zoom din&aacute;mico y manual.</span></p>\n" +
                    "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;margin-left:0cm;line-height:107%;font-size:15px;font-family:\"Calibri\",sans-serif;text-align:justify;'><b><span style=\"font-family:Roboto;color:#202124;background:white;\">Zoom din&aacute;mico:</span></b><span style=\"font-family:Roboto;color:#202124;background:white;\">&nbsp;con esta opci&oacute;n el zoom se ajustar&aacute; por s&iacute; solo (recomendado).</span></p>\n" +
                    "<p style='margin-top:0cm;margin-right:0cm;margin-bottom:0cm;margin-left:0cm;line-height:107%;font-size:15px;font-family:\"Calibri\",sans-serif;text-align:justify;'><strong><span style=\"font-family:Roboto;color:#202124;background:white;\">Zoom manual:</span></strong><span style=\"font-family:Roboto;color:#202124;background:white;\">&nbsp;con esta opci&oacute;n el zoom podr&aacute; ser regulado seg&uacute;n tus necesidades.</span></p>"));
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.zoom));
        }
        else if(position==5){
            textViewTitle.setText("Total de lecturas");
            textViewContent.setText("Este contador te muestra el total de patentes que escaneaste durante día.");
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.totallecturas));
        }
        else if(position==6){
            textViewTitle.setText("Modo de detección");
            textViewContent.setText("Cada vez que la aplicación detecte un vehículo y/o una placa patente, se visualizará un recuadro naranjo o amarillo respectivamente. " +
                    "Luego, de forma automática se verifica la existencia de una posible irregularidad.");
            /*
            * Cada vez que la aplicación detecte un vehículo, " +
                    "pondrá un recuadro naranjo sobre el auto y uno amarillo sobre la patente. " +
                    " De forma automática se consulta en nuestra base de datos para saber si tiene alguna " +
                    "irregularidad.
                    * */
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.modelo));
        }
        else if(position==7){
            textViewTitle.setText("Alerta");
            textViewContent.setText("Si detectas un vehículo con posible irregularidad, escucharás una alerta verbal." +
                    " Además, se despliega una ventana lateral con la imagen del vehículo alertado.\n" +
                    "Siempre debes esperar por el resultado de la validación que se hará en la central de monitoreo. \n");

            linearLayoutButtonAlerta.setVisibility(View.VISIBLE);
            buttonAlerta.setText("Escuchar alerta");
            Drawable img = getContext().getResources().getDrawable(R.drawable.altavoz);
            img.setBounds(60, 0, 120, 60);
            buttonAlerta.setCompoundDrawables(img, null, null, null);
            buttonAlerta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textToSpeech("Se ha visto vehículo, marca, jhiunday, modelo, tuccson, color, gris... placa patente. A... A... B... B... 1...1... con posible irregularidad.... Se va a validar.");
                }
            });
            imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ventanalateral2));
        }
        else if(position==8){
            if(accountVersionFirebase.equals("full")){
                patentesEnObsrvacion = " o \"vehículo pertenece a tus patentes en observación\"";
            }
            textViewTitle.setText("Resultado de validación");
            textViewContent.setText("El resultado entregado por la central de monitoreo, varía según las siguientes opciones:" +
                    " \"vehículo mantiene encargo vigente por robo\" o \"vehículo NO presenta encargo por robo\""+patentesEnObsrvacion+". \n\n" +
                    "A continuación puedes escuchar los resultado descritos anteriormente:\n\n");
            Drawable img = getContext().getResources().getDrawable(R.drawable.altavoz);
            img.setBounds(60, 0, 120, 60);
            linearLayoutButtonAlerta.setVisibility(View.VISIBLE);
            buttonAlerta.setText("Con encargo");
            buttonAlerta.setCompoundDrawables(img, null, null, null);
            buttonAlerta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textToSpeech("vehículo, placa patente. A... A... B... B... 1...1... mantiene encargo vigente por robo");
                }
            });
            linearLayoutButtonAlerta2.setVisibility(View.VISIBLE);
            buttonAlerta2.setText("Sin encargo");
            buttonAlerta2.setCompoundDrawables(img, null, null, null);
            buttonAlerta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textToSpeech("vehículo, placa patente. A... A... B... B... 1...1... NO presenta encargo por robo");
                }
            });
            if(accountVersionFirebase.equals("full")){
                if(grupos != ""){
                    linearLayoutButtonAlerta3.setVisibility(View.VISIBLE);
                    buttonAlerta3.setText("Con observación");
                    buttonAlerta3.setCompoundDrawables(img, null, null, null);
                    buttonAlerta3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            textToSpeech("vehículo, placa patente. A... A... B... B... 1...1... pertenece a tus patentes en observación");
                        }
                    });
                }
                if(grupos.contains("Policía")){
                    linearLayoutButtonAlerta4.setVisibility(View.VISIBLE);
                    buttonAlerta4.setText("SOAP");
                    buttonAlerta4.setCompoundDrawables(img, null, null, null);
                    buttonAlerta4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            textToSpeech("vehículo, placa patente A... A... B... B... 1...1... posiblemente presenta problemas con su documentación");
                        }
                    });
                }

            }
            imageViewImagenTutorial.setVisibility(View.GONE);
        }

        else if(position==9){
            textViewTitle.setText("Consideraciones generales\n");
            textViewContent.setText(
                    "- La capacidad de lecturas de patentes dependerá directamente de las condiciones de luminosidad.\n" +
                    "- Un nivel y calidad de señal baja, podría afectar el rendimiento óptimo de algunas funcionalidades de la aplicación.\n" +
                    "- Cuando recibas una alerta debes tomarlo con calma.\n" +
                    "- Siempre debes esperar la validación que será realizada por la central de monitoreo y que será informada verbalmente por la aplicación.\n" +
                    "- Las alertas que recibas son solo para que tomes las precauciones del caso, nunca deberás tomar acciones que son propias de Carabineros o de Seguridad Ciudadana.\n" +
                    "- Pasado un minuto la aplicación activa el modo descanso de forma automática, el brillo de la pantalla disminuye al mínimo para ahorrar energía y no desconcentrarte mientras conduces.\n" +
                    "- Es importante que no manipules el celular mientras conduces.\n\n" +

                    "Toma en cuenta estas consideraciones para que tengas una conducción segura.\n\n" +
                    "Para SafeByWolf tu seguridad es lo más importante.");

//            "SafeByWolf red de colaboración para frenar la sustracción de vehículos. \n \n" +
            //safebywolf red de colaboracion para buscar autos robados


            //imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.vertutorial));
            imageViewImagenTutorial.setVisibility(View.GONE);

        }

        else if(position==10){
            textViewTitle.setText("¡Felicidades!\n");
            textViewContent.setText("Has llegado al final de nuestro tutorial. " +
                    " Con tu cooperación podemos " +
                    "ayudar a otras personas víctimas de la delincuencia. \n \n" +
                    "Esperamos que puedas escanear muchas patentes.\n\n" +
                    "Puedes volver a ver este tutorial en el menú tutorial.");

//            "SafeByWolf red de colaboración para frenar la sustracción de vehículos. \n \n" +
            //safebywolf red de colaboracion para buscar autos robados


                    imageViewImagenTutorial.setImageDrawable(getResources().getDrawable(R.drawable.vertutorial));
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

    public void textToSpeech(String text) {
        if(text.equalsIgnoreCase("")){
            return;
        }
        Log.v("volumex","text: "+text);

        textToSpeech = new TextToSpeech(StepFragmentTutorialDetector.this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int index = 0;
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            //guardar valor int volumen siempre y cuando no sea una alerta
                            Log.v("TextToSpeechX","On Start");
                            Log.v("volumex","On Start");
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 150, 0);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.v("TextToSpeechX","On Error");
                        }
                    });

                    // set unique utterance ID for each utterance
                    String mostRecentUtteranceID = (new Random().nextInt() % 9999999) + ""; // "" is String force
                    Log.v("volumex","mostRecentUtteranceID: "+mostRecentUtteranceID);

                    // set params
                    // *** this method will work for more devices: API 19+ ***
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mostRecentUtteranceID);
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,params);
                } else {
                    Log.v("errorlenguaje", "initialization failed");
                }
            }
        });


    }
}