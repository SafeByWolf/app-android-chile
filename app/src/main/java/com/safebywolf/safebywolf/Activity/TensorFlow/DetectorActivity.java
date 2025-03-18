/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.safebywolf.safebywolf.Activity.TensorFlow;

import static java.io.File.separator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.safebywolf.safebywolf.Activity.AboutActivity;
import com.safebywolf.safebywolf.Activity.ComunasActivity;
import com.safebywolf.safebywolf.Activity.ContactoActivity;
import com.safebywolf.safebywolf.Activity.Encuesta.EncuestaImagenes;
import com.safebywolf.safebywolf.Activity.Encuesta.EncuestaManager;
import com.safebywolf.safebywolf.Activity.GaleryActivity;
import com.safebywolf.safebywolf.Activity.LogoInicial;
import com.safebywolf.safebywolf.Activity.NoticiasActivity;
import com.safebywolf.safebywolf.Activity.NovedadesActivity;
import com.safebywolf.safebywolf.Activity.PerfilActivity;
import com.safebywolf.safebywolf.Activity.SesionLiteExpirada;
import com.safebywolf.safebywolf.Activity.TensorFlow.Model.PatenteOCRRectF;
import com.safebywolf.safebywolf.Activity.TerminosCondiciones;
import com.safebywolf.safebywolf.Activity.TutorialDetectorActivity;
import com.safebywolf.safebywolf.Class.BroadcastReceiver.CallBroadcastReceiver;
import com.safebywolf.safebywolf.Class.BroadcastReceiver.MyBroadcastReceiverLocation;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.ReverseGeocoding;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.ImagenPatenteApi;
import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Interface.PosiblePatenteRobadaVistaApi;
import com.safebywolf.safebywolf.Model.AlertGroup;
import com.safebywolf.safebywolf.Model.AutoPatenteEnPantalla;
import com.safebywolf.safebywolf.Model.CirclePatente;
import com.safebywolf.safebywolf.Model.ConfiguracionEspecificaTotem;
import com.safebywolf.safebywolf.Model.ConfiguracionTotem;
import com.safebywolf.safebywolf.Model.EmailFechaTotal;
import com.safebywolf.safebywolf.Model.GroupType;
import com.safebywolf.safebywolf.Model.ImagenPatente;
import com.safebywolf.safebywolf.Model.MarkerPatente;
import com.safebywolf.safebywolf.Model.Messages;
import com.safebywolf.safebywolf.Model.NotificacionUsuarioEnGrupo;
import com.safebywolf.safebywolf.Model.Patente;
import com.safebywolf.safebywolf.Model.PatenteColor;
import com.safebywolf.safebywolf.Model.PatenteEscaneada;
import com.safebywolf.safebywolf.Model.PatenteQueue;
import com.safebywolf.safebywolf.Model.PatenteListaNegra;
import com.safebywolf.safebywolf.Model.PatenteRobadaVista;
import com.safebywolf.safebywolf.Model.RowPatenteEscaneada;
import com.safebywolf.safebywolf.Model.SendUbicacion;
import com.safebywolf.safebywolf.Model.TimeOffset;
import com.safebywolf.safebywolf.Model.Total;
import com.safebywolf.safebywolf.Model.Totem;
import com.safebywolf.safebywolf.Model.UrlImagenPatenteApi;
import com.safebywolf.safebywolf.Model.VersionNueva;
import com.safebywolf.safebywolf.R;
import com.safebywolf.safebywolf.Service.LocationService;
import com.safebywolf.safebywolf.Activity.TensorFlow.Model.BitmapRectF;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import com.safebywolf.safebywolf.Activity.TensorFlow.customview.OverlayView;
import com.safebywolf.safebywolf.Activity.TensorFlow.customview.OverlayView.DrawCallback;
import com.safebywolf.safebywolf.Activity.TensorFlow.env.BorderedText;
import com.safebywolf.safebywolf.Activity.TensorFlow.env.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.tflite.Detector;
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;
import com.safebywolf.safebywolf.Activity.TensorFlow.tracking.MultiBoxTracker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SensorEventListener, View.OnClickListener {

    /**
     * vabriables configurables desde firebase
     * */
    String textoFinalizacionEncuesta = "<p>Gracias por su aporte al responder la encuesta.</p>";
    int calidadDeImagenDeDeteccion = 50;
    int cantidadADisminuirZoomDinamico = 2;
    int cantidadAAumentarZoomDinamico = 1;

    float proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom = 0.4f;
    float proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom = 0.6f;

    float proporcionAnchoMinimoAutoRespectoElAltoFlotante = 1.3f;
    float proporcionAnchoMinimoPatenteRespectoElAltoFlotante = 4f;

    float proporcionAnchoMinimoAutoRespectoElPreviewFlotante = 0.1f;
    float proporcionAnchoMinimoPatenteRespectoElPreviewFlotante = 0.04f;

    int esperaModoAhorroMilis = 60000*3;

    String speechModoAhorroDeEnergiaActivado = "Modo no distracción iniciado, la aplicación seguirá escaneando.";

    String speechButtonModoAhorroDeEnergiaActivado = "Modo no distracción activado, la pantalla se oscurecerá automáticamente al cabo de "+ ((int)(esperaModoAhorroMilis/60000))+" minutos, no obstante la aplicación seguirá escaneando.";
    String speechButtonModoAhorroDeEnergiaDesactivado = "Modo no distracción desactivado, la pantalla permanecerá siempre activa.";

    String speechButtonZoomDinamico = "soom dinámico activado";
    String speechButtonZoomManual = "soom manual activado";

    boolean isRecuadroNaranjoEnImagen = true;
    boolean isLimiteParaActualizarCuenta = false;
    boolean isButtonConsultaAutoSeguro = true;
    boolean isConsultasApiGoogleActivado = false;
    boolean isEnviarSoloPatentesIgualesAGoogleOCRAPI = false;
    boolean isSendUsuarioUbicacionGPS = true;

    int distanciaMinimaParaVolverAEscanear = 100;
    int framesPorSegundo = 10;
    float velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos = 4.0f;
    //int minimoZoom = 12; desde cameraActivity

    int tiempoMaximoConZoomAutomaticoSeg = 5;
    int minimoDeLecturasFromDB = 2;
    int maximoDetecciones = 5;
    int maximoDeteccionesTotales = 500;

    int frecuenciaCreacionCollageMillis = 1000;
    int cantidadDeImagenesDelCollage = 4;

    int frecuenciaEnvioUbicacionUsuarioMilis = 5000;
    int esperaEnvioUbicacionUsuarioMilis = 5000;

    int frecuenciaEnvioUbicacionTotemMilis = 60000;
    int esperaEnvioUbicacionTotemMilis = 10000;
    int tiempoMaximoSinLecturasTotemMinutos = 30;
    int tiempoMaximoSinActualizarEstadoTotemMinutos = 10;

    int frecuenciaCreacionUbicacionMilis = 10 * 1000;

    int frecuenciaComprobacionInternetMilis = 30000;
    int esperaComprobacionInternetMilis = 5000;

    int frecuenciaComprobacionGPSMilis = 10000;
    int esperaComprobacionGPSMilis = 10000;

    int esperaEnvioPatenteFirebaseMilis = 40000;

    int esperaFramesAutoSinPatenteMilis = 3000;
    int cantidadMaximaFramesAutoSinPatente = 2; //comienza a contar en 0

    private float confianzaMinimaAutoSinPatenteProbabilidadFlotante = 0.5f;

    // Minimum detection confidence to track a detection.
    private float confianzaMinimaDeteccionPatenteProbabilidadFlotante = 0.35f;
    private float confianzaMinimaOCRProbabilidadFlotante = 0.35f;
    private float confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem = 0.35f;
    private float confianzaMinimaOCRProbabilidadFlotanteTotem = 0.35f;

    int tiempoRafagaMilis = 30000;//30 segundos
    int tiempoSetRafagaMilis = 60000 * 30;//30 minutos

    int tiempoVolverABuscarPatenteEnBDMilis = 60000;// 1 minuto

    int radioDeInteresAlertaPatenteMetros = 2000; //2000 metros

    float proporcionAlturaMinimaAuto = 0.23f;
    float proporcionAlturaMinimaPatente = 0.041f;
    float proporcionAnchoMinimoAuto = 0.13f;
    float proporcionAnchoMinimoPatente = 0.04f;

    float oriHeight;
    float oriWidht;
    float newHeightAuto;
    float newWidthAuto;
    float newHeightPatente;
    float newWidthPatente;

    String speechInicial = "Bienvenido al sistema lector de patentes de chile...";

    //ALERTA
    String speechAlertaSeHaVistoVehiculo = "Se ha visto vehículo";
    String speechAlertaListaNegra = "perteneciente a tus patentes en observación.";
    String speechAlertaSoap = "posiblemente con problemas en su documentación.";
    String speechAlertaConEncargo = "con, encargo vigente por robo,";
    String speechAlertaSinEncargo = "sin, encargo por robo.";

    ///PRE ALERTA
    String speechPreAlertaSeHaVistoVehiculo = "Se ha visto vehículo";
    String speechPreAlertaConPosibleIrregularidad = " con posible irregularidad.";
    String speechPreAlertaSeVaAValidar = " Se va a validar.";

    //POS ALERTA
    String speechPosAlertaCorreccionValidacion = "Corrección, "; //(cuando se cambia el estado del encargo)
    String speechPosAlertaNoIgualAImagen = "Descartar alerta, vehículo, placa patente...";
    String speechPosAlertaEliminada = "Descartar alerta, vehículo, placa patente...";
    String speechPosAlertaVehiculoPlacaPatente = "vehículo, placa patente...";
    String speechPosAlertaListaNegra = "pertenece a tus patentes en observación.";
    String speechPosAlertaSoap = "posiblemente con problemas en su documentación.";
    String speechPosAlertaConEncargo = "mantiene encargo vigente por robo.";
    String speechPosAlertaSinEncargo = "NO, presenta encargo por robo.";
    String speechPosAlertaComunicateConTuCentral = " Para mayor información comunícate, con tu central de monitoreo.";

    /**
     * FIN vabriables configurables desde firebase
     * */

    String API_URL;

    View compassButton;
    Handler handlerCrearCollage = null;
    Handler handlerProcessImage = null;

    Timer timerZoomEnDisminucion = null;
    TimerTask timerTaskEnDisminucion = null;
    int cantidadMaximaDeDeteccionesPorCollage = 9;
    boolean isCreatingCollage = false;
    ArrayList<BitmapRectF> arrayListBitmapRectF = new ArrayList();
    float widthDeteccionPorFrame = 0;
    float heightDeteccionPorFrame = 0;
    int contadorDeZoomAutomatico = 0;
    boolean ceroDetecciones = false;

    int heightCollage=0;
    int widthCollage=0;

    int minimoDeLecturas = minimoDeLecturasFromDB;

    double latitudUserFromDB = 0.0f;
    double longitudUserFromDB = 0.0f;
    String comunaUserFromDB = "Sin comuna";

    float resizeWidthImage = 600; //width por defecto de 400

    DecimalFormat df = new DecimalFormat("#.####");
    ConstraintLayout constraintLayoutPatenteEscaneadaRowItem;
    TextView textViewPatenteEscaneadaRowItem;
    TextView textViewPatenteConfianza;
    ConstraintLayout constraintLayoutPatenteEscaneadaRowItem0;
    TextView textViewPatenteEscaneadaRowItem0;
    TextView textViewPatenteConfianza0;
    ConstraintLayout constraintLayoutPatenteEscaneadaRowItem1;
    TextView textViewPatenteEscaneadaRowItem1;
    TextView textViewPatenteConfianza1;
    ConstraintLayout constraintLayoutPatenteEscaneadaRowItem2;
    TextView textViewPatenteEscaneadaRowItem2;
    TextView textViewPatenteConfianza2;
    ArrayList<RowPatenteEscaneada> arrayListRowPatentesEscaneadas = new ArrayList<>();
    Map<String,RowPatenteEscaneada> mapRowPatentesEscaneadas = new HashMap();
    boolean lecturaCorrecta = false;

    int previousVolume = 0;

    ImageView imageViewLogoEsquina;
    ConstraintLayout constraintLayoutLogoEsquina;

    int contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario = 0;
    boolean firstTimeContadorDeVecesQueSeEjecutaUpdateUbicacionUsuario = true;

    RelativeLayout relativeLayoutTamanoAuto;
    RelativeLayout relativeLayoutTamanoPatente;
    ViewGroup.LayoutParams relativeA;
    ViewGroup.LayoutParams relativeP;
    Timer timerConexion = new Timer();
    TimerTask timerTaskConexion;
    int procesoSendUbicacion = 0;
    int lockUpdateLocationProcess = 0;

    boolean isUpdateUbicacionUsuario = true;

    ConnectivityManager cm = null;
    NetworkInfo activeNetwork = null;
    boolean isConnected = false;
    long ultimoTiempoDeConexion = 0;

    //variables auto sin patente
    int countAutoSinPatente = 0;
    long tiempoActualAutoSinPatente = 0;

    int numeroFrame = 1;

    FloatingActionButton floatingactionbuttonBattery;
    AlertDialog alertDialogEnciendeGPS;
    double lastKnownLatitud = -1;
    double lastKnownLongitud = -1;
    boolean seMantieneLatitudYLongitud = false;
    String lastComuna = "";
    String lastCiudad = "";
    String lastPais = "";
    String lastRegion = "";

    ArrayList<PatenteEscaneada> autosSinPatente = new ArrayList<>();
    String modelType = "1";

    boolean autoSinPatente = false;
    int marginModelo2 = 30;
    LinearLayout linearLayoutContenedorSeekbar;
    FloatingActionButton buttonZoom;
    boolean isZoomAutomaticoActivado = true;
    int heightBitmapDeFondo = 200;
    int contadorPatentesDistintasOnResume = 0;
    LinearLayout linearLayoutInfo;
    TimerTask timerTaskSinGPS;
    int contadorSinGPS = 0;
    Timer timerSinGPS;
    Timer timerSendUbicacionUsuario;
    Timer timerSendUbicacionTotem;
    TimerTask timerTaskSendUbicacionTotem;
    int countUbicacionTotem = 0;
    TimerTask timerTaskSendUbicacionUsuario;
    int countLecturas = 0;

    HashMap<String,Boolean> hashMapSpeech= new HashMap<String,Boolean>();

    AudioManager audioManager;
    AudioFocusRequest mAudioFocusRequest;

    String lastPatenteLeida = "";
    private LocationCallback locationCallback;

    // Configuration values for the prepackaged SSD model.
    private static final int cropSize = 300;
    private static final boolean TF_OD_API_IS_QUANTIZED = true;
    //private static final String TF_OD_API_MODEL_FILE = "mobilenetv1.tflite";
    //private static final String TF_OD_API_MODEL_FILE = "patentes.tflite";
    private static final String TF_OD_API_MODEL_FILE = "deteccion_rapida.tflite";
    private static final String TF_OD_API_LABELS_FILE = "patentes.txt";
    private static final DetectorMode MODE = DetectorMode.TF_OD_API;

    private static final boolean MAINTAIN_ASPECT = false;

    private Size DESIRED_PREVIEW_SIZE = new Size(1280, 720);

    private static final float TEXT_SIZE_DIP = 10;

    private Integer sensorOrientation;
    int contadorDeFrames = 0;

    int fps = 0;
    Timer timerContadorFPS = null;
    Timer timerCrearCollage = null;

    private Detector detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;

    private boolean computingDetection = false;

    private Matrix frameToCropTransformMatrix;
    private Matrix cropToFrameTransformMatrix;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    //letras con las cuales el modelo falla W,G,K,Y
    //H o V => W //C => G //X o R => K //V => Y
    String letrasConProbabilidadDeFalla = "HVCXR";

    boolean fromBackground = false;
    long timestampUltimaConexion = 0;
    int countPermissionsGPS=0;

    boolean usbCharge = false;
    boolean acCharge = false;

    HashMap<String, ArrayList<PatenteQueue>> hashMapPatentesQueue = new HashMap<String, ArrayList<PatenteQueue>>();
    HashMap<String, ArrayList<PatenteEscaneada>> hashMapMaxDetection = new HashMap<String, ArrayList<PatenteEscaneada>>();
    ArrayList<PatenteEscaneada> arrayListPatentesEscaneadasDetection = new ArrayList<>();

    HashMap<String, ArrayList<PatenteQueue>> hashMapPatenteQueue = new HashMap<String, ArrayList<PatenteQueue>>();//llamar hashmap

    LinearLayout linearLayoutCropPatenteAuto, contenedorLinearLayoutCropPatenteAuto;

    ArrayList<ImagenPatente> imagenesPatente = new ArrayList<>();
    ArrayList<ImagenPatente> clickedImagenesPatente;

    private String url = "https://www.autoseguro.gob.cl/";
    LinearLayout linearLayoutSeekbar;
    boolean isTotemActivo = false;
    boolean isTotemEnModoReposo = false;
    String temperaturaBateriaAntesDelReposo = "-1";
    ConfiguracionTotem configurationTotem = new ConfiguracionTotem();
    TextView textViewTCPUNumero;
    TextView textViewTBateriaNumero;
    TextView textViewNivelBateriaNumero;
    TextView textViewTCPU;
    TextView textViewTBateria;
    TextView textViewNivelBateria;
    TextView textViewCargando;
    TextView textViewCargandoString;
    RelativeLayout linearLayoutNegro;
    float batteryTemp;
    float batteryLevel;
    float anteriorBatteryLevel = 0;
    private MyBroadcastReceiverLocation broadcastReceiverLocation;;
    public static String PATENTES = "patentes";
    private static final float ASPECT_RATIO_TOLERANCE = 0.01f;
    LinearLayout linearLayoutPatente;
    LinearLayout linearLayoutPuntoPatente;
    TextView textViewPatenteEscaneada;
    TextView textViewTituloPatenteEscaneada;
    LinearLayout linearLayoutPatenteRed;
    LinearLayout linearLayoutPatenteRobadaVista;
    LinearLayout linearLayoutPatenteEscaneadaVista;
    TextView textViewPatenteRobadaVista;
    int contadorDeMinutosTaskPatentes = 0;

    int mLastRotation = 0;
    boolean isPatentesRobadasVistasInicializadas = false;
    Timer timerCadaUnMinuto;
    ArrayList<MarkerPatente> markerPatentes = new ArrayList<>();
    ArrayList<CirclePatente> circlePatentesArray = new ArrayList<>();
    ArrayList<PatenteColor> patenteColorArray = new ArrayList<>();
    ArrayList<PatenteRobadaVista> posiblesPatentesRobadasVistasObservables = new ArrayList<>();

    float orientationVals[] = new float[18];
    float zoomGoogleMaps = 14;
    float radioCircle = 80;
    private SensorManager mSensorManager;
    Sensor sensorRotacion;
    Sensor sensorTemperatura;
    CallBroadcastReceiver callBroadcastReceiver;
    private float[] mRotationMatrix = new float[16];
    TimerTask doAsynchronousTaskUbicacion = null;
    TimerTask doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto = null;
    HashMap<String,PatenteRobadaVista> mapPatentesSpeech = new HashMap<>();

    Location currentLocation = null;
    float velocidad = 0.0f;

    TextToSpeech textToSpeech = null;
    ArrayList<TextToSpeech> arrayListTextToSpeech = new ArrayList<>();
    ArrayList<String> arrayListTextToSpeechText = new ArrayList<>();
    MediaPlayer mediaPlayerNotificacionPatenteNoRobada;
    private final static int MAX_VOLUME = 100;
    TextView textViewPatentesLeidas;
    TextView textViewPatentesLeidasText;

    ConstraintLayout linearLayoutPatentesLeidas;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    FloatingActionButton buttonCentrar;
    FloatingActionButton buttonTools;
    LinearLayout linearLayoutTools;
    FloatingActionButton buttonSwitchCameraOrMap;
    FloatingActionButton buttonConsultaAutoSeguro;

    boolean isActivarTotem = false;
    boolean centrarCamera = true;
    boolean isDevelopMove = false;
    boolean isUserMove = false;

    private boolean fullScreenCamera = false;
    String fecha;
    String hora;

    ListenerRegistration reiniciarTotem;
    ListenerRegistration configuracionEspecificaTotem;
    ListenerRegistration configuracionEspecificaUsuario;
    ListenerRegistration configuracionTotem;
    ListenerRegistration configuracionApp;
    ListenerRegistration observablePosiblePatenteRobadaVista;
    ListenerRegistration observableBroadcastMessages;

    ArrayList<Patente> patentesRobadasVistas = new ArrayList<>();
    ArrayList<PatenteEscaneada> arrayListPatentesEscaneadas = new ArrayList<>();
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClientObtieneUltimaUbicacion;
    MarkerOptions marker;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SeekBar seekbar;
    TextView textViewZoom;
    ArrayList<String> arrayListPatentesDistintas = new ArrayList<String>();
    ArrayList<Integer> contadorDePatentes = new ArrayList<Integer>();
    int contadorPatentesDistintas = 0;

    private HashMap<String, Integer> captions;

    String idUsuarioFirebase;
    String accountVersionFirebase;
    String ipUsuarioFirebase;
    long expiredSessionUsuarioFirebase;
    String tagUsuarioFirebase;
    String emailUsuarioFirebase;
    String contactoUsuarioFirebase;
    String nombreUsuarioFirebase;
    String apellidoUsuarioFirebase;
    String tokenDeviceFirebase;
    Set<String> setGruposUsuarioFirebase;
    List<String> listaGrupoGlobal = new ArrayList<>();
    boolean isUserPolicia = false;
    boolean isUserSoap = false;

    Double latitud = 0.0;
    Double longitud = 0.0;
    Boolean locationAvaible=false;
    Boolean lastLocationAvaible=false;
    long lastLocationAvaibleTime= (long) 0.0;

    LocationRequest locationRequest;
    int frames = 2;
    int resX = 1080;
    int resY = 720;
    boolean foco = true;
    int zoom = 0;
    int bound = 0;
    Circle accuracyCircle;
    private Marker positionMarker;
    private LinearLayout LinearLayoutButtonDot;
    ImageButton imageButtonDot;

    SupportMapFragment mapFragment;
    BitmapDescriptor markerDescriptor;
    boolean isUserTotem = false;
    boolean isUserLite = false;
    boolean isUserSinAlertas = false;
    Totem totem = null;
    TextView textViewModoTotem;
    Intent intentLocationService;

    boolean existPatenteRobadaEnMapa = false;

    List<Map<String, String>> listaGrupoTipoGlobal = new ArrayList<>();
    List<Map<String, String>> listaGrupoTipoLocalStorageConAlertGroup = new ArrayList<>();

    View view;
    CurrentDate horaTouchListener;
    boolean isModoAhorroDeEnergiaActivado = false;
    boolean isButtonAhorroDeEnergiaActivado = true;
    Intent locationServiceIntent;

    // Variable que indica si voy a ir a otra activity
    // Esta variable se usa para no ejecutar la consulta en el stop y asi evitar cambiar el estado del campo "activo" a false
    boolean irOtraActividad = false;
    public final int REQUEST_ABOUT = 2;

    private EncuestaManager encuestaManager = EncuestaManager.setInstacia(this);

    int tiempoActivacionEncuestaMinutos = 30;
    int intentosParaSaltarEncuesta = 3;
    int tiempoVerificacionEncuestasPendientes = 1;

    boolean encuestaAbierta = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGlobalesFB();
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });

        if(!isDebug()){
            API_URL = getResources().getString(R.string.prod_api);
            Log.v("release","prod");
        } else {
            Log.v("release","dev");
            API_URL = getResources().getString(R.string.dev_api);
        }

        //Audio Focus
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Utils.guardarValorInt(DetectorActivity.this, Referencias.AUDIO, previousVolume);

        Log.v("volumex","previos volume: " + previousVolume);
        AudioAttributes mAudioAttributes =
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setLegacyStreamType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioFocusRequest =
                    //AUDIOFOCUS_GAIN Se utiliza para indicar una ganancia de foco de audio, o una solicitud de foco de audio, de duración desconocida.
                    //AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE Se utiliza para indicar una solicitud temporal de enfoque de audio, que se prevé que dure un breve período de tiempo, durante el cual ninguna otra aplicación o componente del sistema debería reproducir nada. Ejemplos de solicitudes de enfoque de audio exclusivas y transitorias son la grabación de notas de voz y el reconocimiento de voz, durante los cuales el sistema no debería reproducir ninguna notificación y la reproducción de medios debería haberse pausado.
                    //AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK Se utiliza para indicar una solicitud temporal de enfoque de audio, que se prevé que dure un breve período de tiempo y en la que es aceptable que otras aplicaciones de audio sigan reproduciéndose después de haber reducido su nivel de salida (también conocido como "ducking"). Ejemplos de cambios temporales son la reproducción de instrucciones de manejo donde la reproducción de música de fondo es aceptable.
                    //AUDIOFOCUS_GAIN_TRANSIENT Se utiliza para indicar una ganancia temporal o una solicitud de enfoque de audio, que se prevé que dure un breve período de tiempo. Ejemplos de cambios temporales son la reproducción de instrucciones de manejo o una notificación de evento.
                    new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(mAudioAttributes)
                            .setWillPauseWhenDucked(true)
                            .setAcceptsDelayedFocusGain(false)
                            .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                                @Override
                                public void onAudioFocusChange(int focusChange) {
                                    Log.v("audis","focusChange: "+focusChange);
                                    switch (focusChange) {
                                        case AudioManager.AUDIOFOCUS_GAIN:
                                            Log.v("audis","AUDIOFOCUS_GAIN");
                                            break;
                                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                                            Log.v("audis","AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                                            //setVolume(MEDIA_VOLUME_DUCK);
                                            break;
                                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                            Log.v("audis","AUDIOFOCUS_LOSS_TRANSIENT");
                                            break;
                                        case AudioManager.AUDIOFOCUS_LOSS:
                                            Log.v("audis","AUDIOFOCUS_LOSS");
                                            break;

                                    }
                                }
                            })
                            .build();
        }

        //Text to speech
        initTextToSpeech();

        constraintLayoutPatenteEscaneadaRowItem = findViewById(R.id.constraintLayoutPatenteEscaneadaRowItem);
        constraintLayoutPatenteEscaneadaRowItem0 = findViewById(R.id.constraintLayoutPatenteEscaneadaRowItem0);
        constraintLayoutPatenteEscaneadaRowItem1 = findViewById(R.id.constraintLayoutPatenteEscaneadaRowItem1);
        constraintLayoutPatenteEscaneadaRowItem2 = findViewById(R.id.constraintLayoutPatenteEscaneadaRowItem2);

        textViewPatenteEscaneadaRowItem = findViewById(R.id.textViewPatenteEscaneadaRowItem);
        textViewPatenteEscaneadaRowItem0 = findViewById(R.id.textViewPatenteEscaneadaRowItem0);
        textViewPatenteEscaneadaRowItem1 = findViewById(R.id.textViewPatenteEscaneadaRowItem1);
        textViewPatenteEscaneadaRowItem2 = findViewById(R.id.textViewPatenteEscaneadaRowItem2);
        textViewPatenteConfianza = findViewById(R.id.textViewConfianza);
        textViewPatenteConfianza0 = findViewById(R.id.textViewConfianza0);
        textViewPatenteConfianza1 = findViewById(R.id.textViewConfianza1);
        textViewPatenteConfianza2 = findViewById(R.id.textViewConfianza2);
       //recyclerViewPatentesEscaneadas = findViewById(R.id.recyclerViewPatentesEscaneadas);
        //recyclerViewPatentesEscaneadas.setLayoutManager(new LinearLayoutManager(this));

        relativeLayoutTamanoAuto = findViewById(R.id.relativeLayoutTamanoAuto);
        relativeLayoutTamanoPatente = findViewById(R.id.relativeLayoutTamanoPatente);
        imageViewLogoEsquina = findViewById(R.id.imageViewLogoEsquina);
        imageViewLogoEsquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safebywolf.cl"));
                startActivity(browserIntent);
            }
        });

        constraintLayoutLogoEsquina = findViewById(R.id.constraintLayoutLogoEsquina);
        constraintLayoutLogoEsquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safebywolf.cl"));
                startActivity(browserIntent);
            }
        });
        df.setRoundingMode(RoundingMode.CEILING);
        Log.v("previewSize","primer desire detectorActivity: "+DESIRED_PREVIEW_SIZE.getWidth() +" - "+DESIRED_PREVIEW_SIZE.getHeight());

        String gruposString = Utils.leerValorString(DetectorActivity.this, Referencias.GRUPOSSTRING);
        if (gruposString != null && !gruposString.equals("")){
            this.listaGrupoTipoLocalStorageConAlertGroup = Utils.stringToListMap(gruposString);
            Log.v("listaGrupoTipo","listaGrupoTipo: "+ listaGrupoTipoLocalStorageConAlertGroup);
            for(Map<String, String> grupoTipoConAlertGroup : listaGrupoTipoLocalStorageConAlertGroup){
                //se crea grupo tipo
                Map<String, String> grupoTipo = new HashMap<>();
                grupoTipo.put("nombre", grupoTipoConAlertGroup.get("nombre"));
                grupoTipo.put("tipo", grupoTipoConAlertGroup.get("tipo"));
                listaGrupoTipoGlobal.add(grupoTipo);

                //se seta el tipo de grupo comuna en la encuenta for olavex
                if (grupoTipoConAlertGroup.get("tipo").equalsIgnoreCase("Comuna")) {
                    encuestaManager.setTieneGrupoComuna(true);
                    Log.v("tipogripp","comuna sii");
                }

            }
        }

        linearLayoutContenedorSeekbar = findViewById(R.id.linearLayoutContenedorSeekbar);

        buttonZoom = findViewById(R.id.buttonZoom);
        buttonZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonZoom();
            }
        });

        if(isDebug()) {
            imageViewPatenteCrop.setVisibility(View.VISIBLE);
            //desactivaZoomAutomatico();
            //linearLayoutContenedorSeekbar.setVisibility(View.VISIBLE);
        }

        textViewPatentesLeidas =findViewById(R.id.textViewPatentesLeidas);
        textViewPatentesLeidasText=findViewById(R.id.textViewPatentesLeidasText);
        linearLayoutPatentesLeidas = findViewById(R.id.linearLayoutPatentesLeidas);

        linearLayoutNegro = findViewById(R.id.linearLayoutNegro);
        textViewTCPUNumero = findViewById(R.id.textViewTemperaturaCPUNumero);
        textViewTBateriaNumero = findViewById(R.id.textViewTemperaturaBateriaNumero);
        textViewNivelBateriaNumero = findViewById(R.id.textViewNivelBateriaNumero);
        textViewTCPU = findViewById(R.id.textViewTemperaturaCPU);
        textViewTBateria = findViewById(R.id.textViewTemperaturaBateria);
        textViewNivelBateria = findViewById(R.id.textViewNivelBateria);
        textViewModoTotem = findViewById(R.id.textViewModoTotem);
        textViewCargando = findViewById(R.id.textViewCargando);
        textViewCargandoString = findViewById(R.id.textViewCargandoString);

        linearLayoutCropPatenteAuto = findViewById(R.id.linearLayoutCropPatenteAuto);
        contenedorLinearLayoutCropPatenteAuto = findViewById(R.id.contenedorLinearLayoutCropPatenteAuto);

        this.registerReceiver(this.broadcastReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        buttonCentrar = findViewById(R.id.buttonCentrar);
        buttonCentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existPatenteRobadaEnMapa = false;
                centrarCamara();
                brilloDePantallaNormal();
                textToSpeech("Centrar mapa", false);
            }
        });

        //iniciarFragmentGoogleMap();

        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mLastRotation = mWindowManager.getDefaultDisplay().getRotation();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("frames") != null) {
                String frames = getIntent().getExtras().getString("frames");
                this.frames = Integer.parseInt(frames);
            }

            if (getIntent().getExtras().getString("resolucionX") != null) {
                String resX = getIntent().getExtras().getString("resolucionX");
                this.resX = Integer.parseInt(resX);
            }
            if (getIntent().getExtras().getString("resolucionY") != null) {
                String resY = getIntent().getExtras().getString("resolucionY");
                this.resY = Integer.parseInt(resY);
            }
            if (getIntent().getExtras().getString("foco") != null) {
                String foco = getIntent().getExtras().getString("foco");
                this.foco = Boolean.parseBoolean(foco);
            }
            if (getIntent().getExtras().getString("zoom") != null) {
                String zoom = getIntent().getExtras().getString("zoom");
                this.zoom = Integer.parseInt(zoom);
            }
            if (getIntent().getExtras().getString("bound") != null) {
                String bound = getIntent().getExtras().getString("bound");
                this.bound = Integer.parseInt(bound);
            }
        }
        Log.v("getextradataresX", resX + "");
        Log.v("getextradataresY", resY + "");
        Log.v("getextradataframes", frames + "");
        Log.v("getextradataZoom", zoom + "");
        Log.v("getextradatabound", bound + "");

        //textViewContadorPatentes = findViewById(R.id.textViewContadorPatentes);
        //textViewContadorPatentesDivididas = findViewById(R.id.textViewContadorPatentesDivididas);

        linearLayoutPatente = findViewById(R.id.linearLayoutPatente);
        linearLayoutPuntoPatente = findViewById(R.id.linearLayoutPuntoPatente);
        linearLayoutPatenteEscaneadaVista = findViewById(R.id.linearLayoutPatenteEscaneadaVista);
        textViewPatenteEscaneada = findViewById(R.id.textViewPatenteEscaneada);
        textViewTituloPatenteEscaneada = findViewById(R.id.textViewTituloPatenteEscaneada);

        linearLayoutPatenteRed = findViewById(R.id.linearLayoutPatenteRed);
        linearLayoutPatenteRobadaVista = findViewById(R.id.linearLayoutPatenteRobadaVista);
        textViewPatenteRobadaVista = findViewById(R.id.textViewPatenteRobadaVista);

        floatingactionbuttonBattery = findViewById(R.id.floatingactionbuttonBattery);
        floatingactionbuttonBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isButtonAhorroDeEnergiaActivado){
                    actionButtonActivarAhorroDeEnergia(true);
                } else {
                    actionButtonDesactivarAhorroDeEnergia(true);
                    brilloDePantallaNormal();
                }
            }
        });

        // Se comenta este bloque de codigo para que no se muestre la galeria cuando el usuario
        // hace click en el letrero de patente robada
        /*
        linearLayoutPatenteRobadaVista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagenesPatente != null) {
                    if (imagenesPatente.size() > 0) {
                        if (textViewPatenteRobadaVista != null) {
                            if (textViewPatenteRobadaVista.getText().toString() != null) {
                                if (textViewPatenteRobadaVista.getText().toString().length() > 0) {
                                    getClickedImagenPatente(textViewPatenteRobadaVista.getText().toString());
                                }
                            }
                        }
                    }
                }
            }
        });

         */

        String expSession = Utils.leerValorString(DetectorActivity.this, Referencias.EXPIREDSESSION);
        Log.v("tokenExpired",expSession);
        if(Utils.isLong(expSession)){
            expiredSessionUsuarioFirebase = Long.parseLong(expSession);
            Log.v("tokenExpired","isLong");
        }

        accountVersionFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.ACCVERSION);
        idUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.IDUSUARIO);
        nombreUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.NOMBRE);
        apellidoUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.APELLIDO);
        tokenDeviceFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.TOKENFIREBASEINSTALATION);
        emailUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.CORREO);
        ipUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.IP);
        isUserPolicia = Utils.leerValorBoolean(DetectorActivity.this, Referencias.SESIONPOLICIA);
        isUserSoap = Utils.leerValorBoolean(DetectorActivity.this, Referencias.SOAP);
        isUserTotem = Utils.leerValorBoolean(DetectorActivity.this, Referencias.TOTEM);

        Log.v("asdasda","account version: "+accountVersionFirebase);
        if(accountVersionFirebase.equalsIgnoreCase("lite")){
            isUserLite = true;
        }

        if(accountVersionFirebase.equalsIgnoreCase("sinAlertas")){
            isUserSinAlertas = true;
        }

        Log.v("patenteRobadaVista","policia : "+ isUserPolicia);
        Log.v("patenteRobadaVista","SOAP : "+ isUserSoap);

        tagUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.TAG);

        if(Utils.leerValorString(DetectorActivity.this, Referencias.MODELTYPE) != null && !Utils.leerValorString(DetectorActivity.this, Referencias.MODELTYPE).equalsIgnoreCase("") ){
            modelType = Utils.leerValorString(DetectorActivity.this, Referencias.MODELTYPE);
        }
        autoSinPatente = Utils.leerValorBoolean(DetectorActivity.this, Referencias.AUTOSINPATENTE);

        Log.v("lala1", "email usuario " + emailUsuarioFirebase);
        contactoUsuarioFirebase = Utils.leerValorString(DetectorActivity.this, Referencias.CONTACTO);
        setGruposUsuarioFirebase = Utils.leerValorSetString(DetectorActivity.this, Referencias.GRUPO);

        // si el arreglo es null, significa que el usuario no tiene grupo
        if (setGruposUsuarioFirebase == null
                || setGruposUsuarioFirebase.isEmpty()
                || (setGruposUsuarioFirebase != null && setGruposUsuarioFirebase.size() == 0)) {
            setGruposUsuarioFirebase = new HashSet<String>();
        }

        if (setGruposUsuarioFirebase.size() == 0) {
            Log.v("getGruposDeUsuario"," == 0 ");
            setGruposUsuarioFirebase.add("SIN GRUPO");
        }

        Log.v("guf","gruposUsuarioFirebase: "+ setGruposUsuarioFirebase);

        listaGrupoGlobal = new ArrayList<>(setGruposUsuarioFirebase);
        setGruposUsuarioFirebase.add("TODO");

        LinearLayoutButtonDot = findViewById(R.id.LinearLayoutButtonDot);
        imageButtonDot = findViewById(R.id.imageButtonDot);
        LinearLayoutButtonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu();
            }
        });

        imageButtonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu();
            }
        });

        //mediaPlayerNotificacionPatenteRobada = MediaPlayer.create(this, R.raw.got);
        mediaPlayerNotificacionPatenteNoRobada = MediaPlayer.create(this, R.raw.bip3);
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 100) / Math.log(MAX_VOLUME)));
        mediaPlayerNotificacionPatenteNoRobada.setVolume(volume, volume);
        buttonSwitchCameraOrMap = findViewById(R.id.buttonSwitchCameraOrMap);

        linearLayoutTools = findViewById(R.id.linearLayoutTools);
        buttonTools = findViewById(R.id.buttonTools);
        buttonTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTools.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                if(linearLayoutTools.getVisibility() == View.VISIBLE){
                    linearLayoutTools.animate().translationY(50)
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    linearLayoutTools.setVisibility(View.GONE);
                                }
                            });
                } else {
                    buttonTools.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    linearLayoutTools.setVisibility(View.VISIBLE);
                    linearLayoutTools.animate().translationY(-50);

                }

            }
        });

        buttonConsultaAutoSeguro = findViewById(R.id.buttonConsultaAutoSeguro);
        if(isButtonConsultaAutoSeguro){
            buttonConsultaAutoSeguro.setVisibility(View.VISIBLE);
        } else {
            buttonConsultaAutoSeguro.setVisibility(View.GONE);
        }
        buttonConsultaAutoSeguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech("Consultar patente en autoseguro", false);
                brilloDePantallaNormal();
                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                    }
                }, 3000);

            }
        });

        if(isUserSinAlertas){
            buttonConsultaAutoSeguro.setVisibility(View.GONE);
            buttonSwitchCameraOrMap.setVisibility(View.GONE);
            ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.buttonSwitchCameraOrMap);
            constraintSet.clear(R.id.buttonTools, ConstraintSet.BOTTOM);
            constraintSet.clear(R.id.buttonZoom, ConstraintSet.LEFT);

            float dip = 16f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
            constraintSet.connect(R.id.buttonTools, ConstraintSet.BOTTOM, R.id.linearLayoutBotonesAbajo, ConstraintSet.TOP, (int) px);
            constraintSet.connect(R.id.buttonTools, ConstraintSet.START, R.id.parent_layout, ConstraintSet.START, (int) px);
//
//            constraintSet.connect(R.id.buttonZoom, ConstraintSet.BOTTOM, R.id.parent_layout, ConstraintSet.BOTTOM, (int) px);
//            constraintSet.connect(R.id.buttonZoom, ConstraintSet.START, R.id.buttonTools, ConstraintSet.END, (int) px);
//
//            constraintSet.connect(R.id.linearLayoutContenedorSeekbar, ConstraintSet.BOTTOM, R.id.parent_layout, ConstraintSet.BOTTOM, (int) ((int) px-(px/2)));
//            constraintSet.connect(R.id.linearLayoutContenedorSeekbar, ConstraintSet.START, R.id.buttonZoom, ConstraintSet.END, (int) px);
            constraintSet.applyTo(constraintLayout);
        }

        Log.v("wolfTotem", this.emailUsuarioFirebase);

        textViewZoom = findViewById(R.id.textViewZoom);
        seekbar = findViewById(R.id.camera_sb_expose);
        linearLayoutSeekbar = findViewById(R.id.linearLayoutSeekbar);

        int zoom = Utils.leerValorInt(DetectorActivity.this, Referencias.ZOOMINICIAL, zoomInicial);
        progressSeekBarCameraZoom = zoom;
        textViewZoom.setText(progressSeekBarCameraZoom+"");
        Log.v("zoomTotem","zm: "+progressSeekBarCameraZoom+"");

        if(this.emailUsuarioFirebase.contains(Referencias.WOLFTOTEM)){
            isUserTotem = true;
        }

        if (isUserTotem) {
            //textViewPatentesLeidas.setVisibility(View.GONE);
            //textViewPatentesLeidasText.setVisibility(View.GONE);
            //linearLayoutPatentesLeidas.setVisibility(View.GONE);
            Log.v("wolftotem", "soy totem");
            //updatea el campo reiniciar a false en el caso de un totem (costo de escritura 1)
            db.collection(Referencias.TOTEM).document(emailUsuarioFirebase).update("reiniciar",false);

            floatingactionbuttonBattery.hide();

            textViewTCPUNumero.setVisibility(View.VISIBLE);
            textViewTBateriaNumero.setVisibility(View.VISIBLE);
            textViewTCPU.setVisibility(View.VISIBLE);
            textViewTBateria.setVisibility(View.VISIBLE);
            textViewNivelBateria.setVisibility(View.VISIBLE);
            textViewNivelBateriaNumero.setVisibility(View.VISIBLE);
            textViewCargando.setVisibility(View.VISIBLE);
            textViewCargandoString.setVisibility(View.VISIBLE);
            Log.v("wolfTotem", "email contiene wolftotem");
            //si es usuario totem enviar ubicacion y temperatura de dispositivo

            relativeLayoutTamanoAuto.setVisibility(View.VISIBLE);
            relativeLayoutTamanoPatente.setVisibility(View.VISIBLE);

            buttonSwitchCameraOrMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isActivarTotem) {
                        Log.v("totemqs","pse aqui");
                        activarTotem();
                    } else {
                        desactivarTotem();
                    }
                }
            });

            float dip = 100f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());

            //set tamaño de boton
            buttonSwitchCameraOrMap.setCustomSize((int) px);
            //cambiar icono de boton a camara
            buttonSwitchCameraOrMap.setImageResource(R.drawable.camera);

            dip = 20f;
            float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());

            constraintLayoutLogoEsquina.setVisibility(View.GONE);
            //cambiando de posicion el boton totem
            ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.buttonSwitchCameraOrMap, ConstraintSet.BOTTOM);
            constraintSet.connect(R.id.buttonSwitchCameraOrMap, ConstraintSet.TOP, R.id.parent_layout, ConstraintSet.TOP, (int) px2);
            constraintSet.applyTo(constraintLayout);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("volumex","clic en cualquier lado");
                    brilloDePantallaNormal();
                }
            });

            //ocultar boton autoseguro
            buttonConsultaAutoSeguro.hide();

            buttonCentrar.hide();

            buttonTools.hide();
        } else {
            Log.v("wolfTotem", "email NO contiene wolftotem");
            buttonSwitchCameraOrMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    escondeMuestraPreviewCamara();
                    brilloDePantallaNormal();
                }
            });
        }

        linearLayoutInfo = findViewById(R.id.linearLayoutInfo);
        if(isDebug() || isUserTotem) {
            linearLayoutInfo.setVisibility(View.VISIBLE);
        }

        marker = new MarkerOptions();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;

        /*

        Button buttonMaximizarGoogleMaps = findViewById(R.id.buttonMaximizarGoogleMaps);
        buttonMaximizarGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMaximizarGoogleMaps) {
                    maximizarGoogleMaps();
                    buttonMaximizarGoogleMaps.setText("minimizar");
                    isMaximizarGoogleMaps = true;
                } else {
                    minimizarGoogleMaps();
                    buttonMaximizarGoogleMaps.setText("maximizar");
                    isMaximizarGoogleMaps = false;
                }
            }
        });

                Button buttonGuardar = findViewById(R.id.guardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearExcel();
            }
        });
         */

        if(!BuildConfig.FLAVOR.contains("lite")) {
            //verificar si existe una notificacion de ingreso a grupo (costo de lectura 1 o mas)
            db.collection(Referencias.NOTIFICACIONUSUARIOENGRUPO)
                    .whereEqualTo("email", emailUsuarioFirebase)
                    .whereEqualTo("aceptado", "NULL").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            NotificacionUsuarioEnGrupo notificacionUsuarioEnGrupo = null;
                            try {
                                notificacionUsuarioEnGrupo = documentSnapshot.toObject(NotificacionUsuarioEnGrupo.class);
                                notificacionUsuarioEnGrupo.setId(documentSnapshot.getId());
                                Log.v("sinnotificacion", notificacionUsuarioEnGrupo.getGrupo());
                            } catch (Exception ex) {
                                Log.v("sinnotificacion", "No se puedo convertir NotificacionUsuarioEnGrupo: " + ex);
                            }
                            Log.v("sinnotificacion", "usuario SIIIII posee notificaciones");
                            if (notificacionUsuarioEnGrupo != null) {
                                //crear dialogo usuario en grupo
                                dialogoNotificacionUsuarioEnGrupo(notificacionUsuarioEnGrupo.getGrupo(),
                                        notificacionUsuarioEnGrupo.getTipo(), notificacionUsuarioEnGrupo);
                            }
                        }
                    } else {
                        Log.v("sinnotificacion", "usuario no posee notificaciones");
                    }
                }
            });
        }
        // inicializarContadorDeTotales();

        //permissionGPS();

        imageButtonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("imageButtonHideShow","click: "+isOpenDialogPatenteAuto.toString());
                linearLayoutContenedorButtonAndDialog.animate().translationX(-(linearLayoutCropPatenteAuto.getWidth())).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ocualtaMenuLateralAutoPatente();
                    }
                });
            }
        });

        imageButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muestraMenuLateralAutoPatente();
            }

        });

        setSizeDialog();

        callBroadcastReceiver = new CallBroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                boolean isCalling = intent.getBooleanExtra("call",false);
                if(isCalling){
                    Log.v("llamadas","llamando");
                    brilloDePantallaNormal();
                } else {
                    Log.v("llamadas","se corto llamada");
                }
            }
        };
        registerReceiver(callBroadcastReceiver, new IntentFilter("onCallStateChanged"));

        Log.v("location", "se inicializaRecepcionDePatentes");

        CurrentDate currentDate = new CurrentDate(new Date());
        Log.v("dattimecsServer", "listar patentes desde hora: " + currentDate.getDateMenosXMinutos());
        Timestamp timestamp = new Timestamp(currentDate.getDateMenosXMinutos());
        Log.v("patenteRobadaVista", "currentDate.getDateMenosXMinutos(): "+currentDate.getDateMenosXMinutos());

        if(!isUserSinAlertas) {
            if (isUserTotem == false) {
                if(!isSpeechInicialVerbalizado()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech(speechInicial,true);
                        }
                    }, 500);
                }

                Log.v("patenteRobadaVista", "se esta ejecutando el observable, osea es usuario");
                if(isUserLite){
                    dialogInicial();
                }

                //ejecuto observable de la coleccion posiblePatenteRobadaVista despues de obtener el tiempo del servidor
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    completableFutureGetServerTimestamp().thenRun(new Runnable() {
                        @Override
                        public void run() {
                            getPosiblePatenteRobadaVistaFirebaseObservaCadaVezQueHayaUnCambio();
                        }
                    });
                } else {
                    Utils.getServerTime(DetectorActivity.this,API_URL,0);
                    getPosiblePatenteRobadaVistaFirebaseObservaCadaVezQueHayaUnCambio();
                }

                //observable de la coleccion broadcastMessages
                observableMessages();
            } else {
                //observable que permite reiniciar un totem
                observableReiniciarTotem();
            }
        }

        if(!isDebug() && !isUserTotem){
            textViewEmailText.setVisibility(View.GONE);
            textViewVersionCodeText.setVisibility(View.GONE);
            textViewFrameText.setVisibility(View.GONE);
            textViewCropText.setVisibility(View.GONE);
            textViewInferenceText.setVisibility(View.GONE);
            textViewFPSText.setVisibility(View.GONE);
            textViewPatenteText.setVisibility(View.GONE);
            textViewVelocidadText.setVisibility(View.GONE);
            textViewMSText.setVisibility(View.GONE);
            textViewTemperaturaCPUText.setVisibility(View.GONE);
            textViewTemperaturaBateriaText.setVisibility(View.GONE);
            textViewOCRText.setVisibility(View.GONE);
            textViewModeloText.setVisibility(View.GONE);

            textViewVersionCodeValue.setVisibility(View.GONE);
            textViewEmailValue.setVisibility(View.GONE);
            textViewFrameValue.setVisibility(View.GONE);
            textViewCropValue.setVisibility(View.GONE);
            textViewInferenceValue.setVisibility(View.GONE);
            textViewFPSValue.setVisibility(View.GONE);
            textViewPatenteValue.setVisibility(View.GONE);
            textViewVelocidadValue.setVisibility(View.GONE);
            textViewTemperaturaCPUValue.setVisibility(View.GONE);
            textViewTemperaturaBateriaValue.setVisibility(View.GONE);
            textViewOCRValue.setVisibility(View.GONE);
            textViewModeloValue.setVisibility(View.GONE);
        }

        encuestaManager.getEncuestasNoFinalizadas(emailUsuarioFirebase);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Log.v("onKeyDown","onKeyUp");
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Utils.guardarValorInt(DetectorActivity.this, Referencias.AUDIO, currentVolume);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.v("onKeyDown","onKeyDown");
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Utils.guardarValorInt(DetectorActivity.this, Referencias.AUDIO, currentVolume);
            return true;
        }
        return false;
    }

    public void buttonZoom(){
        if(isZoomAutomaticoActivado){
            desactivaZoomAutomatico();
            textToSpeech(speechButtonZoomManual, false);
            cancelTimeTaskZoom();
            contadorDeZoomAutomatico = 0;

        } else {
            activaZoomAutomatico();
            zoomEnDisminucion = false;
            cancelTimeTaskZoom();
            zoomHandler();
            Log.v("lkg","taskzoommm||||");
            textToSpeech(speechButtonZoomDinamico, false);
        }
    }

    public void initTextToSpeech() {
        if(textToSpeech != null){
            return;
        }
        if(isUserTotem){
            Log.v("utteranceID","return text.equalsIgnoreCase() || isUserTotem " );
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(mAudioFocusRequest);
        }

        textToSpeech = new TextToSpeech(DetectorActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int index = 0;
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            //guardar valor int volumen siempre y cuando no sea una alerta
                            Log.v("utteranceID","en proceso utteranceID: "+ utteranceId );
                            Log.v("TextToSpeechX","On Start");
                            Log.v("volumex","On Start");
                            if(hashMapSpeech.containsKey(utteranceId)){
                                Log.v("utteranceID", "contiene key: "+utteranceId);
                            } else {
                                Log.v("utteranceID", "no contiene key "+hashMapSpeech.toString());
                            }
                            boolean isAlerta = hashMapSpeech.get(utteranceId);
                            Log.v("utteranceID", "onStart utteranceID: " +utteranceId+" alerta: "+isAlerta);
                            if(isAlerta) {
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                            }
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.v("utteranceID", "onDone utteranceID: " +utteranceId+" alerta: "+hashMapSpeech.get(utteranceId));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                audioManager.abandonAudioFocusRequest(mAudioFocusRequest);
                            }

                            hashMapSpeech.remove(utteranceId);

                            //baja volumen anterior a la alerta
                            int currentVolume = Utils.leerValorInt(DetectorActivity.this, Referencias.AUDIO, 30);
                            Log.v("utteranceID", "onStart baja volumen utteranceID: " +utteranceId+" currentVolume: "+currentVolume);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.v("TextToSpeechX","On Error");
                        }
                    });

                } else {
                    Log.v("volumex", "initialization failed: " +status);
                }
            }
        });
    }

    public void textToSpeech(String text, boolean isAlerta){
        if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
            isAlerta = false;
        }
        if(text.equalsIgnoreCase("") || isUserTotem){
            Log.v("utteranceID","return text.equalsIgnoreCase() || isUserTotem " );
            return;
        }
        for(int i = 0; i < arrayListTextToSpeechText.size(); i++){
            if(arrayListTextToSpeechText.get(i).equalsIgnoreCase(text)){
                Log.v("utteranceID","return  arrayListTextToSpeechText.get(i).equalsIgnoreCase(text)" );
                return;
            }
        }
        int utteranceId = (new Random().nextInt() % 9999999);
        Log.v("utteranceID","mas reciente utteranceID: "+ utteranceId + " text: "+text);

        textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null,String.valueOf(utteranceId));

        hashMapSpeech.put(String.valueOf(utteranceId),isAlerta);

        arrayListTextToSpeech.add(textToSpeech);
        if(isAlerta){
            arrayListTextToSpeechText.add(text);
        }
    }

    public boolean prvIsIgualAImagen(PatenteRobadaVista patenteRobadaVista){
        if(patenteRobadaVista.getIgualAImagen() != null && patenteRobadaVista.getIgualAImagen().equalsIgnoreCase("true")){
            return true;
        }
        return false;
    }

    public boolean prvIsValidada(PatenteRobadaVista patenteRobadaVista){
        if(patenteRobadaVista.getVerificada() != null && patenteRobadaVista.getVerificada().equalsIgnoreCase("true")){
            return true;
        }
        return false;
    }

    public void remueveMenuLateralAutoPatente(PatenteRobadaVista prv){

        if(prvIsIgualAImagen(prv) && ((prv.isBaseListaNegra() && verificaSiPatentePerteneceAGrupo(prv))  || (prv.isBaseSoap() && isUserSoap))){
            return;
        }

        ocualtaMenuLateralAutoPatente();

        int index = autoPatenteEnPantallaArrayList.size()-1;
        if((autoPatenteEnPantallaArrayList.size() == 1 &&
                prv.getPatente().equalsIgnoreCase(autoPatenteEnPantallaArrayList.get(index).getPatente()))) {
            Log.v("prro0", "patente: "+prv.getPatente()+" - first"+ autoPatenteEnPantallaArrayList.get(index).getPatente());

            autoPatenteEnPantallaArrayList.clear();
            imageViewAuto.setImageBitmap(null);
            autoPatenteEnPantalla = null;

            linearLayoutCropPatenteAuto.setVisibility(View.GONE);
            linearLayoutHide.setVisibility(View.GONE);
            linearLayoutShow.setVisibility(View.GONE);
        } else if(autoPatenteEnPantallaArrayList.size() > 1 &&
                prv.getPatente().equalsIgnoreCase(autoPatenteEnPantallaArrayList.get(index).getPatente()))
        {

            Log.v("prro1", "patente: "+prv.getPatente()+" - first"+ autoPatenteEnPantallaArrayList.get(index).getPatente());

            autoPatenteEnPantallaArrayList.remove(index);
            index = autoPatenteEnPantallaArrayList.size()-1;
            autoPatenteEnPantalla = autoPatenteEnPantallaArrayList.get(index);
            Log.v("prro4", "patente: "+prv.getPatente()+" - first: "+ autoPatenteEnPantalla.getPatente()+" size: "+autoPatenteEnPantallaArrayList.size());

            imageViewAuto.setImageBitmap(autoPatenteEnPantallaArrayList.get(index).getBitmapAuto());
        }

    }

    public void creaMenuLateralAutoPatente(){
        isOpenDialogPatenteAuto = true;
        linearLayoutCropPatenteAuto.setPadding(5,5,5,5);
        linearLayoutCropPatenteAuto.setVisibility(View.VISIBLE);
        linearLayoutCropPatenteAuto.animate().translationX(0);
        linearLayoutHide.setVisibility(View.VISIBLE);
        linearLayoutShow.setVisibility(View.GONE);
    }

    public void muestraMenuLateralAutoPatente(){
        linearLayoutContenedorButtonAndDialog.animate().translationX(0);
        linearLayoutCropPatenteAuto.setVisibility(View.VISIBLE);
        linearLayoutContenedorButtonAndDialog.setVisibility(View.VISIBLE);
        linearLayoutHide.setVisibility(View.VISIBLE);
        linearLayoutShow.setVisibility(View.GONE);
        isOpenDialogPatenteAuto = true;
    }

    public void muestraMenuLateralAutoPatenteAutomaticamente(){
        linearLayoutContenedorButtonAndDialog.animate().translationX(0);
        linearLayoutCropPatenteAuto.setVisibility(View.VISIBLE);
        linearLayoutContenedorButtonAndDialog.setVisibility(View.VISIBLE);
        linearLayoutHide.setVisibility(View.VISIBLE);
        linearLayoutShow.setVisibility(View.GONE);
        isOpenDialogPatenteAuto = true;
    }

    public void ocualtaMenuLateralAutoPatenteEnXSegundos(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ocualtaMenuLateralAutoPatente();
                    }
                });
            }
        }, 60000);
    }

    public void ocualtaMenuLateralAutoPatente(){
        if(isOpenDialogPatenteAuto){
            linearLayoutCropPatenteAuto.setVisibility(View.GONE);
            linearLayoutContenedorButtonAndDialog.setVisibility(View.GONE);
            linearLayoutHide.setVisibility(View.GONE);
            linearLayoutShow.setVisibility(View.VISIBLE);
            isOpenDialogPatenteAuto = false;
        }
    }

    //si es usuario totem enviar ubicacion y temperatura de dispositivo
    public void getConfiguracionGlobalTotemFirebase() {
        //busca la configuracion global de un totem (costo de lectura 1)
        configuracionTotem = db.collection(Referencias.CONFIGURACIONTOTEM).whereEqualTo("id", Referencias.UNO).limit(1).addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                                configurationTotem = q.toObject(ConfiguracionTotem.class);
                                Log.v("configurationTotem", configurationTotem.getHoraTermino());
                            }
                        } else {
                            Log.v("configTotem", "configTotem no encontrada");
                        }
                    }
                });

    }

    // Obtiene las restricciones de los rectángulos que revisan los vehículos
    private void getConfiguracionEspecificaTotem(){
        //busca la configuracion especifica de un totem (costo de lectura 1)
        configuracionEspecificaTotem = db.collection(Referencias.CONF).document("app").collection(Referencias.TOTEM).document(emailUsuarioFirebase).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.v("nonConfExpecificaTotem", "Falló cambio de configuración: "+error.getMessage());
                }
                else {
                    Log.v("nonConfExpecificaTotem", "cambio de configuración totem");
                    try {
                        getVariablesFromDatabase(value.getData());
                        ejecutarAccionVariablesFromDatabase();
                        if(latitudUserFromDB == 0.0f || longitudUserFromDB == 0.0f) {
                            iniciarLocationService("Normal", "false");
                            taskSinGPS();
                        }
                    }
                    catch (Exception err){
                        ConfiguracionEspecificaTotem configuracionEspecificaTotem = new ConfiguracionEspecificaTotem(emailUsuarioFirebase);
                        //en caso de no encontrar una configuracion especifica de un totem crea una (costo de escritura 1)
                        db.collection(Referencias.CONF).document("app").collection(Referencias.TOTEM).document(emailUsuarioFirebase).set(configuracionEspecificaTotem);
                    }
                }
            }
        });

    }

    private void getConfiguracionEspecificaUsuario(){
        //obtiene la configuracion especifica de un usuario (costo de lectura 1)
        configuracionEspecificaUsuario = db.collection(Referencias.CONF).document("app").collection(Referencias.USUARIO).document(emailUsuarioFirebase).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.v("nonConfExpecificaTotem", "Falló cambio de configuración: "+error.getMessage());
                }
                else {
                    Log.v("nonConfExpecificaTotem", "cambio de configuración totem");
                    try {
                        getVariablesFromDatabase(value.getData());
                        ejecutarAccionVariablesFromDatabase();
                        if(latitudUserFromDB == 0.0f || longitudUserFromDB == 0.0f) {
                            iniciarLocationService("Normal", "false");
                            taskSinGPS();
                        }
                    }
                    catch (Exception err){

                    }
                }
            }
        });

    }

    private void centrarCamara() {
        LatLng latLng = new LatLng(latitud, longitud);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomGoogleMaps);
        if(googleMap != null){
            googleMap.animateCamera(cameraUpdate);
        }
        defaulZoomCircle();
    }

    private void cuadroVisible(){
        final int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        relativeA = relativeLayoutTamanoAuto.getLayoutParams();
        relativeP = relativeLayoutTamanoPatente.getLayoutParams();

        oriWidht = displayMetrics.widthPixels;
        oriHeight = displayMetrics.heightPixels;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            oriWidht = displayMetrics.heightPixels;
            oriHeight =  displayMetrics.widthPixels;
        }

        newHeightAuto = (float) (oriHeight * proporcionAlturaMinimaAuto *1.35);
        newWidthAuto = (float) (oriWidht * proporcionAnchoMinimoAuto *1.35);
        newHeightPatente = (float) (oriHeight * proporcionAlturaMinimaPatente *1.35);
        newWidthPatente = (float) (oriWidht * proporcionAnchoMinimoPatente *1.35);

        relativeP.height = (int) newHeightPatente;
        relativeP.width = (int)  newWidthPatente;
        relativeLayoutTamanoPatente.setLayoutParams(relativeP);
        relativeA.height = (int) newHeightAuto;
        relativeA.width = (int)  newWidthAuto;
        relativeLayoutTamanoAuto.setLayoutParams(relativeA);
        Log.v("pruebaHoy", String.valueOf(newHeightAuto)+' '+String.valueOf(newHeightPatente) );

    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        try {
            detector =
                    TFLiteObjectDetectionAPIModel.create(
                            this,
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            cropSize,
                            TF_OD_API_IS_QUANTIZED);

        } catch (final IOException e) {
            e.printStackTrace();
            Log.e("detectorAc", "Exception initializing Detector!");
            ejecutarLogoInicialActivity();
            finish();
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        tracker = new MultiBoxTracker(this);

        if(previewHeight == 0 || previewWidth == 0 ){
            return;
        }

        /*
        sensorOrientation = rotation - (getScreenOrientation());
        if(sensorOrientation < 0){
            sensorOrientation = sensorOrientation*-1;
        }

         */
        sensorOrientation = rotation;

        Log.v("jksdfsdkf!","rotation: "+ rotation);
        Log.v("jksdfsdkf","sensorOrientation: "+ sensorOrientation);
        Log.v("jksdfsdkf","getScreenOrientation(): "+ getScreenOrientation());

        Log.i("detectorAc", " "+previewWidth+" "+ previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        float resizeWidth = cropSize;
        float aspectRatioPreview = (float) previewWidth/previewHeight;
        float resizeHeight = resizeWidth/aspectRatioPreview; //se calcula respecto al width
        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.v("resizeWidth","ORIENTATION_PORTRAIT previewWidth: "+previewWidth+" previewHeight: "+previewHeight );
            Log.v("resizeWidth","ORIENTATION_PORTRAIT aspectRatioPreview: "+aspectRatioPreview);
            Log.v("resizeWidth","ORIENTATION_PORTRAIT resizeHeight: "+resizeHeight);
            frameToCropTransformMatrix =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            (int) resizeHeight, cropSize,
                            getRotation(),
                            MAINTAIN_ASPECT);
        } else {
            Log.v("resizeWidth","ORIENTATION_LANDSCAPE previewWidth: "+previewWidth+" previewHeight: "+previewHeight );
            Log.v("resizeWidth","ORIENTATION_LANDSCAPE aspectRatioPreview: "+aspectRatioPreview);
            Log.v("resizeWidth","ORIENTATION_LANDSCAPE resizeHeight: "+resizeHeight);
            frameToCropTransformMatrix =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            cropSize, (int) resizeHeight,
                            getRotation(),
                            MAINTAIN_ASPECT);
        }

        cropToFrameTransformMatrix = new Matrix();
        frameToCropTransformMatrix.invert(cropToFrameTransformMatrix);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        if (isDebug()) {
                            tracker.drawDebug(canvas, previewWidth, previewHeight, orientation);
                        } else {
                            tracker.draw(canvas);
                        }

                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, getRotation());
        Log.v("rotationX","sensorOrientation"+sensorOrientation);
    }

    public void setSizeDialog(){
        final int orientation = getResources().getConfiguration().orientation;
        Log.v("orientationX","orientation: "+orientation);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 0.5f;
            linearLayoutLeftDialog.setLayoutParams(params);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params2.weight = 0.5f;
            linearLayoutRightDialog.setLayoutParams(params2);

            LinearLayout.LayoutParams paramsTop = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsTop.weight = 0.33f;

            linearLayoutTop.setLayoutParams(paramsTop);
            Log.v("orientationX","params.weight: "+params.weight);

            LinearLayout.LayoutParams paramsBottom = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsBottom.weight = 0.47f;
            linearLayoutBottom.setLayoutParams(paramsBottom);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 0.7f;
            linearLayoutLeftDialog.setLayoutParams(params);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params2.weight = 0.3f;
            linearLayoutRightDialog.setLayoutParams(params2);

            LinearLayout.LayoutParams paramsTop = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsTop.weight = 0.4f;
            linearLayoutTop.setLayoutParams(paramsTop);

            LinearLayout.LayoutParams paramsBottom = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsBottom.weight = 0.4f;
            linearLayoutBottom.setLayoutParams(paramsBottom);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSizeDialog();
        if (hasPermission()) {
            Log.v("previewR","onConfigurationChanged");
            setFragment();
            //onPreviewSizeChosen(new Size(previewWidth, previewHeight), getRotation());
        } else {
            multiplePermissions();
        }

    }

    @Override
    protected void processImage() {

        if(modelType.equalsIgnoreCase("1")){

            trackingOverlay.postInvalidate();

            // No mutex needed as this method is not reentrant.
            if (computingDetection) {
                readyForNextImage();
                return;
            }
            computingDetection = true;

            runInBackground(new Runnable() {
                @Override
                public void run() {

                    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

                    contadorDeFrames++;

                    //crea el bitmap con tamaño de 300x300
                    final Canvas canvasCroppedBitmap = new Canvas(croppedBitmap);
                    canvasCroppedBitmap.drawBitmap(rgbFrameBitmap, frameToCropTransformMatrix, null);

                    final long startTime = SystemClock.uptimeMillis();

                    //TENSORFLOW OBJECT DETECT
                    final List<Detector.Recognition> listaDeDetecciones = detector.recognizeImage(croppedBitmap);
                    lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                    final List<Detector.Recognition> listaDeDeteccionesFiltradas =
                            new ArrayList<Detector.Recognition>();

                    for(Detector.Recognition deteccion : listaDeDetecciones) {
                        if(preFiltrosAutoOPatenteConElCrop(deteccion) == false){
                            continue;
                        }
                        final RectF rectF = deteccion.getLocation();
                        //pone el recuadro (rectF) en el preview
                        cropToFrameTransformMatrix.mapRect(rectF);
                        deteccion.setLocation(rectF);

                        if(filtrosDeFormaAutoOPatenteRespectoDelPreview(deteccion) == false){
                            continue;
                        }

                        listaDeDeteccionesFiltradas.add(deteccion);
                    }

                    final List<Detector.Recognition> listaDeDeteccionesUnicasFiltradas =
                            new ArrayList<Detector.Recognition>();

                    //Este for quita las detecciones repetidas que esten contenidas unas a otras
                    final ArrayList<Integer> index = new ArrayList<>();
                    for(int i = 0; i < listaDeDeteccionesFiltradas.size(); i++){
                        final Detector.Recognition deteccioni = listaDeDeteccionesFiltradas.get(i);
                        if(lastKnownLatitud == 0.0f || lastKnownLongitud == 0.0f || lastKnownLatitud == -1 || lastKnownLongitud == -1){
                            Log.v("patenteRobadaVista","gps 0.0 se descarta patente");
                            continue;
                        }
                        for (int j = i + 1; j < listaDeDeteccionesFiltradas.size(); j++) {
                            final Detector.Recognition deteccionj = listaDeDeteccionesFiltradas.get(j);
                            if (deteccioni.getLocation().contains(deteccionj.getLocation())) {
                                index.add(j);
                                continue;
                            } else if (deteccionj.getLocation().contains(deteccioni.getLocation())) {
                                index.add(i);
                                continue;
                            }
                        }
                        if(index.contains(i)){
                            continue;
                        }
                        deteccioni.setBitmap(Bitmap.createBitmap(rgbFrameBitmap, 0,0, rgbFrameBitmap.getWidth(),rgbFrameBitmap.getHeight()));
                        listaDeDeteccionesUnicasFiltradas.add(deteccioni);
                    }
                    index.clear();

                    tracker.trackResults(listaDeDeteccionesFiltradas);

                    trackingOverlay.postInvalidate();

                    if(listaDeDeteccionesUnicasFiltradas.size() > 0){
                        deteccionesPorFrame(listaDeDeteccionesUnicasFiltradas);
                        ceroDetecciones = false;
                    } else {
                        ceroDetecciones = true;
                    }

                    numeroFrame++;

                    debugDevData();

                    readyForNextImage();

                    computingDetection = false;
                }
            });
        }
        else if(modelType.equalsIgnoreCase("2")){
            // No mutex needed as this method is not reentrant.
            if (computingDetection) {
                return;
            }

            runInBackground(new Runnable() {
                @Override
                public void run() {
                    computingDetection = true;
                    contadorDeFrames++;
                    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

                    reconocerTextoEnImagenModelo2(rgbFrameBitmap);

                    debugDevData();

                    computingDetection = false;

                    readyForNextImage();
                }
            });

        }

    }

    public boolean filtrosDeFormaAutoOPatenteRespectoDelPreview(Detector.Recognition deteccion){
        float rectFWidth = deteccion.getLocation().width();
        float rectFHeight = deteccion.getLocation().height();
        //condiciones de forma de patente
        if (deteccion.getTitle().equalsIgnoreCase("patente")) {

            //El ancho es aproximadamente 3 veces el alto
            Log.v("cropResizePatenteA", "widthPatente " + rectFWidth + " mayor a heightPatente "+ proporcionAnchoMinimoPatenteRespectoElAltoFlotante * rectFHeight);
            if(rectFWidth > proporcionAnchoMinimoPatenteRespectoElAltoFlotante * rectFHeight){
                Log.v("cropResizePatenteA", "entre al if se retorna widthPatente " + rectFWidth + " mayor a heightPatente "+ proporcionAnchoMinimoPatenteRespectoElAltoFlotante * rectFHeight);
                return false;
            }
            if (isPortrait()) {
                Log.v("cropResizePatenteP", "widthPatente " + rectFHeight + " proporcionAnchoMinimoPatenteRespectoElPreviewProbabilidadFlotante * previewWidth: " + proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewHeight);
                if (rectFHeight < proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewHeight) {
                    Log.v("cropResizePatenteP", "entre al if se retorna widthAuto " + rectFHeight + " proporcionAnchoMinimoPatenteRespectoElPreviewProbabilidadFlotante * previewWidth: " + proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewHeight);
                    return false;
                }
                //El ancho es aproximadamente 2 veces el alto
                if(rectFHeight <= rectFWidth){
                    Log.v("cropResizePatente", "widthPatente 1: " + rectFWidth + " mayor a heightPatente "+ rectFHeight);
                    return false;
                }
            } else {
                Log.v("cropResizePatenteP", "widthPatente " + rectFWidth + " proporcionAnchoMinimoPatenteRespectoElPreviewProbabilidadFlotante * previewWidth: " + proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewWidth);
                if (rectFWidth < proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewWidth) {
                    Log.v("cropResizePatenteP", "entre al if se retorna widthAuto " + rectFWidth + " proporcionAnchoMinimoPatenteRespectoElPreviewProbabilidadFlotante * previewWidth: " + proporcionAnchoMinimoPatenteRespectoElPreviewFlotante * previewWidth);
                    return false;
                }
                //El ancho es aproximadamente 2 veces el alto
                if(rectFWidth <= rectFHeight ){
                    Log.v("cropResizePatente", "widthPatente 2: " + rectFWidth + " mayor a heightPatente "+ rectFHeight);
                    return false;
                }
            }
        }

        if(deteccion.getTitle().equalsIgnoreCase("auto")) {
            Log.v("cropResizeAutoA", "widthAuto: " + rectFWidth +" proporcionAnchoMinimoAutoRespectoElAltoFlotante * rectFHeight: " + proporcionAnchoMinimoAutoRespectoElAltoFlotante * rectFHeight);
            if(rectFWidth > proporcionAnchoMinimoAutoRespectoElAltoFlotante * rectFHeight){
                Log.v("cropResizeAutoA", "entre al if se retorna widthAuto: " + rectFWidth +" proporcionAnchoMinimoAutoRespectoElAltoFlotante * rectFHeight: " + proporcionAnchoMinimoAutoRespectoElAltoFlotante * rectFHeight);
                return false;
            }
            if (isPortrait()) {
                Log.v("cropResizeAutoP", "widthAuto: " + rectFHeight + " proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth: " + proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewHeight);
                if (rectFHeight < proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewHeight) {
                    Log.v("cropResizeAutoP", "entre al if se retorna widthAuto: " + rectFHeight + " proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth: " + proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewHeight);
                    return false;
                }
            } else {
                Log.v("cropResizeAutoP", "widthAuto: " + rectFWidth + " proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth: " + proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth);
                if (rectFWidth < proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth) {
                    Log.v("cropResizeAutoP", "entre al if se retorna widthAuto: " + rectFWidth + " proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth: " + proporcionAnchoMinimoAutoRespectoElPreviewFlotante * previewWidth);
                    return false;
                }
            }
        }
        return true;
    }

    public void debugDevData(){
        if(!isDebug() && !isUserTotem){
            return;
        }

        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        showEmail(emailUsuarioFirebase);
                        showVersionCode(BuildConfig.VERSION_CODE+"");
                        showFrameInfo(previewWidth + "x" + previewHeight);
                        //showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                        showInference(lastProcessingTimeMs + "ms");
                        showFPS(fps + "");
                        showPatente(lastPatenteLeida + "");
                        showVelocidad(String.format("%.2f", velocidad));
                        showTemperaturaCPU(textViewTCPUNumero.getText() + " C°");
                        showTemperaturaBateria(textViewTBateriaNumero.getText() + " C°");
                        if(isUserTotem){
                            showOCR(confianzaMinimaOCRProbabilidadFlotanteTotem+" %");
                            showModelo(confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem+" %");
                        } else {
                            showOCR(confianzaMinimaOCRProbabilidadFlotante+" %");
                            showModelo(confianzaMinimaDeteccionPatenteProbabilidadFlotante+" %");
                        }
                    }
                });
    }

    public void contadorFPS(){
        if(timerContadorFPS != null){
            return;
        }
        timerContadorFPS = new Timer();
        Handler handler = new Handler();
        timerContadorFPS.schedule(new TimerTask(){
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isZoomAutomaticoActivado) {
                            contadorDeZoomAutomatico++;
                        }
                        Log.v("zoomdpf","countZoomDeteccionPorFrame: "+ contadorDeZoomAutomatico);

                        fps = contadorDeFrames;
                        contadorDeFrames = 0;
                        minimoDeLecturas = minimoDeLecturasFromDB;
//                if(fps > 15 && fps <= 25){
//                    minimoDeLecturas = minimoDeLecturasFromDB +1;
//                }
                        Log.v("cpi","cantidadDePatentesIguales: "+ minimoDeLecturasFromDB);
                        if(isZoomAutomaticoActivado && !isTaskZoom && zoomEnDisminucion == false && ceroDetecciones == true) {
                            zoomHandler();
                        }

                        if((contadorDeZoomAutomatico >= tiempoMaximoConZoomAutomaticoSeg || arrayListBitmapRectF.size() >= (fps/2))  && zoomEnDisminucion == false && !isTaskZoom){
                            zoomEnDisminucion = true;
                            Log.v("zoomdpf","countZoomDeteccionPorFrame: "+ contadorDeZoomAutomatico);
                            contadorDeZoomAutomatico = 0;
                            if(timerZoomEnDisminucion != null){
                                return;
                            }
                            if(timerTaskEnDisminucion != null){
                                return;
                            }
                            cancelTimeTaskZoom();
                            timerZoomEnDisminucion = new Timer();
                            timerTaskEnDisminucion = new TimerTask() {
                                @Override
                                public void run() {
                                    contadorDeZoomAutomatico = 0;
                                    zoomEnDisminucion(cantidadADisminuirZoomDinamico*2);
                                    if(progressZoom == zoomInicialEnLocalStorage) {
                                        timerZoomEnDisminucion.cancel();
                                        timerTaskEnDisminucion.cancel();
                                        timerZoomEnDisminucion = null;
                                        timerTaskEnDisminucion = null;
                                        zoomEnDisminucion = false;
                                    }
                                }
                            };timerZoomEnDisminucion.schedule(timerTaskEnDisminucion,0,periodZoomMillis);
                        }
                    }
                });
            }
        }, 1000,1000);
    }

    public void crearCollage(){
        //isCreatingCollage = true;
        if(arrayListBitmapRectF.size() == 0){
            return;
        }
        ArrayList<BitmapRectF> arrayListBitmapRectFAux = (ArrayList<BitmapRectF>) arrayListBitmapRectF.clone();
        arrayListBitmapRectF.clear();

        //isCreatingCollage = false;
//                indxx++;
//                if(indxx > 40){
//                    indxx = 0;
//                    primeraVez = false;
//                }

        widthCollage = 0;
        heightCollage = 0;
        int widthCollageAux = 0;
        int heightCollageAux = 0;
        int maxHeight= 0;
        int maxHeightAcumulado= 0;
        int maxWidthAcumulado= 0;
        int maxWidth = 0;
        int width = 0;
        int height = 0;

        /***
         * Se calcula el tamaño del collage
         * */
        if(sensorOrientation == 0 || sensorOrientation == 180){
            for(int i = 0; i < arrayListBitmapRectFAux.size(); i++){
                BitmapRectF bitmapRectF = arrayListBitmapRectFAux.get(i);

                width = bitmapRectF.getCropBitmap().getWidth();
                height = bitmapRectF.getCropBitmap().getHeight();

                if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                    maxHeightAcumulado = maxHeightAcumulado + maxHeight;
                    heightCollageAux = maxHeightAcumulado;
                    heightCollage = heightCollageAux;
                    maxWidth = 0;
                    maxHeight = 0;
                }

                if(height > maxHeight){
                    maxHeight = height;
                    heightCollageAux = maxHeight + maxHeightAcumulado;
                    if (heightCollageAux > heightCollage) {
                        heightCollage = heightCollageAux;
                    }
                }

                maxWidth = maxWidth + width;
                if (maxWidth > widthCollageAux) {
                    widthCollageAux = maxWidth;
                    if (widthCollageAux > widthCollage) {
                        widthCollage = widthCollageAux;
                    }
                }
            }
        } else if(sensorOrientation == 90) {
            for(int i = 0; i < arrayListBitmapRectFAux.size(); i++){
                BitmapRectF bitmapRectF = arrayListBitmapRectFAux.get(i);

                width = bitmapRectF.getCropBitmap().getHeight();
                height = bitmapRectF.getCropBitmap().getWidth();

                if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                    maxHeightAcumulado = maxHeightAcumulado + maxHeight;
                    widthCollageAux = maxHeightAcumulado;
                    widthCollage = widthCollageAux;
                    maxWidth = 0;
                    maxHeight = 0;
                }

                if(height > maxHeight){
                    maxHeight = height;
                    widthCollageAux = maxHeight + maxHeightAcumulado;
                    if (widthCollageAux > widthCollage) {
                        widthCollage = widthCollageAux;
                    }
                }

                maxWidth = maxWidth + width;
                if (maxWidth > heightCollageAux) {
                    heightCollageAux = maxWidth;
                    if (heightCollageAux > heightCollage) {
                        heightCollage = heightCollageAux;
                    }
                }

            }
        }

        //comentar
//                if(arrayListBitmapRectFAux.size() > 0 ){
//                    if(primeraVez == false){
//                        primeraVez = true;
//                    }
//                    else {
//                        return;
//                    }
//                }

        Log.v("ctm23","widthCollageAux: "+widthCollageAux+" heightCollageAux: "+heightCollageAux+" arrayListBitmapRectFAux: "+arrayListBitmapRectFAux.size());
        /***
         * Se arma el collage
         * */
        if (arrayListBitmapRectFAux.size() != 0){

            Bitmap bitmapCollage = Bitmap.createBitmap(widthCollageAux, heightCollageAux, Config.ARGB_8888);
            Canvas canvasCollage = new Canvas(bitmapCollage);
            //bitmapCollage = rotateBitmap(bitmapCollage,sensorOrientation);
            Paint paintBordeNaranjo = new Paint();
            paintBordeNaranjo.setColor(Color.parseColor("#FF460B"));
            paintBordeNaranjo.setStyle(Paint.Style.STROKE);
            paintBordeNaranjo.setStrokeWidth(5.0f);
            paintBordeNaranjo.setTextSize(50);

            if(sensorOrientation == 0) {
                float top = 0;
                float bottom = 0;
                float left = 0;
                float right = 0;
                float maxBottom = 0;
                float maxBottomAcumulado = 0;
                for (int i = 0; i < arrayListBitmapRectFAux.size(); i++) {
                    BitmapRectF bitmapRectF = arrayListBitmapRectFAux.get(i);

                    if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                        left = 0;
                        right = 0;
                        maxBottomAcumulado = maxBottomAcumulado + maxBottom;
                        top = maxBottomAcumulado;
                        maxBottom = 0;
                    }

                    if (bitmapRectF.getCropBitmap().getHeight() > maxBottom) {
                        maxBottom = bitmapRectF.getCropBitmap().getHeight();
                    }

                    right = right + bitmapRectF.getCropBitmap().getWidth();
                    bottom = top + bitmapRectF.getCropBitmap().getHeight();
                    RectF newRectF = new RectF(left, top, right, bottom);
                    bitmapRectF.setRectF(newRectF);
                    canvasCollage.drawBitmap(bitmapRectF.getCropBitmap(), left, top, null);
                    Log.v("pasdja", "armando mi collage 0 left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
                    left = right;
                    bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage, newRectF, Color.GREEN);
                    canvasCollage.drawText(i + "", newRectF.centerX(), newRectF.centerY(), paintBordeNaranjo);
                }
            }
            if(sensorOrientation == 90) {

                float top = heightCollageAux;
                float bottom = heightCollageAux;
                float left = 0;
                float right = 0;
                float maxBottom = 0;
                float maxBottomAcumulado = 0;

                for (int i = 0; i < arrayListBitmapRectFAux.size(); i++) {
                    BitmapRectF bitmapRectF = arrayListBitmapRectFAux.get(i);
                    Log.v("pasdja22", "bitmapRectF.getCropBitmap().getWidth(): " + bitmapRectF.getCropBitmap().getWidth()+" bitmapRectF.getCropBitmap().getHeight(): "+bitmapRectF.getCropBitmap().getHeight());

                    if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                        top = heightCollageAux;
                        bottom = heightCollageAux;
                        maxBottomAcumulado = maxBottomAcumulado + maxBottom;
                        left = maxBottomAcumulado;
                        maxBottom = 0;
                    }

                    if (bitmapRectF.getCropBitmap().getWidth() > maxBottom) {
                        maxBottom = bitmapRectF.getCropBitmap().getWidth();
                    }

                    top = top - bitmapRectF.getCropBitmap().getHeight();
                    right = left + bitmapRectF.getCropBitmap().getWidth();
                    RectF newRectF = new RectF(left, top, right, bottom);
                    bitmapRectF.setRectF(newRectF);
                    canvasCollage.drawBitmap(bitmapRectF.getCropBitmap(), left, top, null);
                    Log.v("pasdja", "armando mi collage 90 left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
                    bottom = top;
                    bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage, newRectF, Color.GREEN);
                    canvasCollage.drawText(i + "", newRectF.centerX(), newRectF.centerY(), paintBordeNaranjo);
                }
            }
            if (sensorOrientation == 180) {
                float top = heightCollageAux;
                float bottom = heightCollageAux;
                float left = widthCollageAux;
                float right = widthCollageAux;
                float maxBottom = 0;
                float maxBottomAcumulado = 0;
                for (int i = 0; i < arrayListBitmapRectFAux.size(); i++) {
                    BitmapRectF bitmapRectF = arrayListBitmapRectFAux.get(i);

                    if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                        left = widthCollageAux;
                        right = widthCollageAux;
                        maxBottomAcumulado = maxBottomAcumulado + maxBottom;
                        bottom = heightCollageAux - maxBottomAcumulado;
                        maxBottom = 0;
                    }

                    if (bitmapRectF.getCropBitmap().getHeight() > maxBottom) {
                        maxBottom = bitmapRectF.getCropBitmap().getHeight();
                    }

                    left = left - bitmapRectF.getCropBitmap().getWidth();
                    top = bottom - bitmapRectF.getCropBitmap().getHeight();
                    RectF newRectF = new RectF(left, top, right, bottom);
                    bitmapRectF.setRectF(newRectF);
                    canvasCollage.drawBitmap(bitmapRectF.getCropBitmap(), left, top, null);
                    Log.v("pasdja", "armando mi collage 180 left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
                    right = left;
                    bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage, newRectF, Color.GREEN);
                    canvasCollage.drawText(i + "", newRectF.centerX(), newRectF.centerY(), paintBordeNaranjo);
                }
            }

            RectF rectFCoolage = new RectF(0,0,bitmapCollage.getWidth(),bitmapCollage.getHeight());
            bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage,rectFCoolage,Color.GREEN);

            Bitmap finalBitmapCollage = bitmapCollage;
            /*
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageViewPatenteCrop.setImageBitmap(finalBitmapCollage);
                }
            });
             */

            Log.v("pasdja","**************** inicio ******************* size: "+arrayListBitmapRectFAux.size()+" RectF collage widht:"+ rectFCoolage.width()+" height: "+rectFCoolage.height());
            //se pone crop image en preview
            //showImageViewPatenteCropDebug(bitmapCollage);

            reconocerTextoEnImagen(bitmapCollage, arrayListBitmapRectFAux, numeroFrame);

            Log.v("bitmapRectF1",arrayListBitmapRectFAux.size()+"");
        }
    }

    public void crearCollageRunnable(){
        // Create the Handler object (on the main thread by default)
        if(handlerCrearCollage != null){
            return;
        }
        handlerCrearCollage = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                crearCollage();
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                handlerCrearCollage.postDelayed(this, frecuenciaCreacionCollageMillis);
            }
        };
        // Start the initial runnable task by posting through the handler
        handlerCrearCollage.post(runnableCode);



//        if(timerCrearCollage != null){
//            return;
//        }
//
//        timerCrearCollage = new Timer();
//        timerCrearCollage.schedule(new TimerTask(){
//            public void run() {
//                 crearCollage();
//            }
//        }, 1000,frecuenciaCreacionCollageMillis);
    }

    /**
     * previewWidth y previewHeight son variables globales con el tamaño del preview (previsualizacion de la cámara)
     * Tanto en modo "Portrait" como en modo "Landscape" el previewWidth y el previewHeight no cambian es decir;
     * El previewWidth siempre es el lado mayor en este caso 1280
     * El previewHeight siempre es el lado menor en este caso 720
     * */
    public void deteccionesPorFrame(List<Detector.Recognition> listaDeDeteccionesUnicasFiltradas){
        widthDeteccionPorFrame = 0;
        heightDeteccionPorFrame = 0;
        for(int i = 0; i < listaDeDeteccionesUnicasFiltradas.size(); i++){
        //for (final Detector.Recognition resultDetectionTF : results) {
            final Detector.Recognition deteccion = listaDeDeteccionesUnicasFiltradas.get(i);
            final RectF rectF = deteccion.getLocation();
            final RectF originalRectF = deteccion.getLocation();

            /**
             * rectFWidth y rectFHeight variables que tienen el tamaño del recuadro de la detección
             * */
//            float rectFWidth = rectF.width();
//            float rectFHeight = rectF.height();
//
//            if (isPortrait()) {
//                rectFWidth = rectF.height(); //En portrait el lado mas grande del recuadro es el rectF.height
//                rectFHeight = rectF.width(); //En portrait el lado mas pequeño del racuadro es el rectF.width
//            }
//
//            //calcula el resize que se hará al crop
//            float scaleResizeWidth = 0;
//            float scaleResizeHeight = 0;
//            float razonAspectRatio = 0; // W/H del rectF
//
//            razonAspectRatio = rectFWidth/rectFHeight; // W/H del rectF
//            float resizeHeight = resizeWidthImage/razonAspectRatio; //se calcula respecto al width
//            scaleResizeWidth = (resizeWidthImage / rectFWidth);
//            scaleResizeHeight = (resizeHeight / rectFHeight);
//
//            if(scaleResizeHeight == 0 || scaleResizeWidth == 0){
//                continue;
//            }

            Bitmap cropOriginalBitmap = Bitmap.createBitmap((int) (rectF.right - rectF.left), (int)  (rectF.bottom - rectF.top), Config.ARGB_8888);
            new Canvas(cropOriginalBitmap).drawBitmap(deteccion.getBitmap(), -rectF.left, -rectF.top, null);

            Bitmap originalBitmap = deteccion.getBitmap();
            if(isRecuadroNaranjoEnImagen) {
                originalBitmap = ponerRecuadroSobreBitmap(originalBitmap, originalRectF, Color.parseColor("#FF460B"));
            }

            //este codigo lleva tiempo comentado y es una forma opcional de agrandar el bitmap
            //Bitmap resizeCropBitmap = Bitmap.createScaledBitmap(cropOriginalBitmap, (int)((rectF.right - rectF.left)*scaleResizeWidth), (int)((rectF.bottom - rectF.top)*scaleResizeHeight), false);
            //cropOriginalBitmap.recycle();

            BitmapRectF bitmapRectF = new BitmapRectF(originalBitmap, cropOriginalBitmap, deteccion.getTitle(), deteccion.getConfidence(), sensorOrientation);
            bitmapRectF.setOriginalRectF(rectF);
            arrayListBitmapRectF.add(bitmapRectF);

            widthDeteccionPorFrame = widthDeteccionPorFrame + rectF.width();
            heightDeteccionPorFrame = heightDeteccionPorFrame + rectF.height();
            if(i+1 == listaDeDeteccionesUnicasFiltradas.size()){
                if(isZoomAutomaticoActivado && zoomEnDisminucion == false){
                    if (widthDeteccionPorFrame < previewWidth * proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom && heightDeteccionPorFrame < previewHeight * proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom) {
                        Log.v("zoomAut2","zoomEnAumento widthDeteccionPorFrame: "+widthDeteccionPorFrame+" < previewWidth * "+proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom+": "+previewWidth * proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom+ " Y heightDeteccionPorFrame: "+heightDeteccionPorFrame+" heightDeteccionPorFrame < previewHeight * " + proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom + ": "+previewHeight * proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom);
                        cancelTimeTaskZoom();
                        zoomEnAumento(cantidadAAumentarZoomDinamico);
                    } else if(widthDeteccionPorFrame < previewWidth * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom && heightDeteccionPorFrame < previewHeight * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom ) {
                        Log.v("zoomAut2","se mantiene  zoom widthDeteccionPorFrame "+widthDeteccionPorFrame+" < previewWidth * "+proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+": "+previewWidth * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+ " Y heightDeteccionPorFrame: "+heightDeteccionPorFrame +"heightDeteccionPorFrame < previewHeight * "+proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+": "+previewHeight * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom);
                        cancelTimeTaskZoom();
                    } else {
                        Log.v("zoomAut2","zoomEnDisminucion widthDeteccionPorFrame "+widthDeteccionPorFrame+" >= previewWidth * "+proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+": "+previewWidth * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+ " || heightDeteccionPorFrame: "+heightDeteccionPorFrame +"heightDeteccionPorFrame => previewHeight * "+proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom+": "+previewHeight * proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom);
                        cancelTimeTaskZoom();
                        zoomEnDisminucion(cantidadADisminuirZoomDinamico);
                    }
                }
            }
        }
    }

    public boolean isPortrait(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }
        return false;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap ponerRecuadroSobreBitmap(Bitmap bitmap, RectF rectF, int color){

        Paint paintBordeNaranjo = new Paint();
        paintBordeNaranjo.setColor(color);

        paintBordeNaranjo.setStyle(Paint.Style.STROKE);
        paintBordeNaranjo.setStrokeWidth(5.0f);

        new Canvas(bitmap).drawRect(rectF, paintBordeNaranjo);

        //Bitmap originalBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        //new Canvas(originalBitmap).drawBitmap(bitmap, 0, 0, null);

        return bitmap;
    }

    public void reconocerTextoEnImagen(final Bitmap bitmapCollage, final ArrayList<BitmapRectF> arrayListBitmapRectF, final int numeroFrm){
        final InputImage image = InputImage.fromBitmap(bitmapCollage, sensorOrientation);
        final TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        ArrayList<PatenteOCRRectF> patenteOCRRectFArrayList = new ArrayList<>();
        recognizer.process(image)
            .addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text result) {
                    Log.v("patenteRobadaVista","numero de frame "+numeroFrm +" bitmapRefctFs size:"+ arrayListBitmapRectF.size() + " bitmapCollage width: "+bitmapCollage.getWidth() + " height: "+bitmapCollage.getHeight() );
                    if(lecturaCorrecta == false){
                        // actualiza los campos de errores de un usuario a vacio (costo de escritura 1)
                        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("error","","errorDescrip","","errorCode",0);
                    }
                    lecturaCorrecta = true;

                    /**
                     * auto sin patente, no se detecto ningun texto dentro del bitmapCollage
                     * */
                    if(result.getTextBlocks().size() == 0){
                        Log.v("patenteRobadaVista","numero de frame "+numeroFrm +" NO se detecto ningun texto dentro del bitmapCollage");
                        autoSinPatente(arrayListBitmapRectF, numeroFrm);
                    } else {
                        Log.v("patenteRobadaVista","numero de frame "+numeroFrm +" Se detectaron "+ result.getTextBlocks().size() +" textos");
                    }


                    //recorre los textBlocks de textRecognition
                    boolean existePatente = false;
                    for (Text.TextBlock textBlock : result.getTextBlocks()) {
                        //recorre las lineas de textRecognition
                        for (int i = 0; i < textBlock.getLines().size(); i++) {
                            final Text.Line line = textBlock.getLines().get(i);
                            final String lineText = line.getText();
                            //Log.v("patenteRobadaVista2", "****");
                            //Log.v("patenteRobadaVista2","lineas:"+ textBlock.getLines().size()+ " linea: "+i+" lineText: "+line.getText());
                            Log.v("patenteRobadaVista", "numero de frame "+numeroFrm +" texto detectado line: " + lineText + " getBoundingBox: "+line.getBoundingBox());
                            //Log.v("textodetectado", " texto detectado: "+lineText);

                            final String patente = UtilsOCR.esPatenteChilena(lineText);
                            if (patente != null) {
                                Log.v("patenteRobadaVista", "numero de frame "+numeroFrm +" esPatenteChilena: " + patente);
                                countLecturas = 0;
                                countAutoSinPatente = 0;
                                existePatente = true;
                                PatenteOCRRectF patenteOCRRectF = new PatenteOCRRectF(patente,new RectF(line.getBoundingBox()),confianzaOCR(line));
                                patenteOCRRectFArrayList.add(patenteOCRRectF);
                            }
                        }
                    }
                    //mostrarDeteccionesForDebug(bitmapCollage, arrayListBitmapRectF, patenteOCRRectFArrayList);
                    if(!existePatente && autoSinPatente){
                        //auto sin patente, no detectó texto patente
                        autoSinPatente(arrayListBitmapRectF, numeroFrm);
                    }

                    //se libera memoria en variables
                    //bitmapCollage.recycle();


                }
            }).addOnCompleteListener(new OnCompleteListener<Text>() {
            @Override
            public void onComplete(@NonNull Task<Text> task) {
                //elimina las patentes repetidas dejando la que tiene la mayor confianzaOCR
                ArrayList<PatenteOCRRectF> patenteOCRRectFArrayListSinRepetidas = new ArrayList<>();
                for(int i = 0; i < patenteOCRRectFArrayList.size(); i++){
                    PatenteOCRRectF patenteOCRRectFMayor = patenteOCRRectFArrayList.get(i);
                    for(int j = i + 1; j < patenteOCRRectFArrayList.size(); j++){
                        PatenteOCRRectF patenteOCRRectFj = patenteOCRRectFArrayList.get(j);
                        if(patenteOCRRectFMayor.getPatente().equals(patenteOCRRectFj) && patenteOCRRectFMayor.getConfianzaOCR() < patenteOCRRectFj.getConfianzaOCR()){
                            patenteOCRRectFMayor = patenteOCRRectFj;
                        }
                    }
                    patenteOCRRectFArrayListSinRepetidas.add(patenteOCRRectFMayor);
                }

                for(int i = 0; i < patenteOCRRectFArrayListSinRepetidas.size(); i++){
                    float confianzaOCR = patenteOCRRectFArrayListSinRepetidas.get(i).getConfianzaOCR();
                    String patente = patenteOCRRectFArrayListSinRepetidas.get(i).getPatente();
                    RectF rectF = patenteOCRRectFArrayListSinRepetidas.get(i).getRectF();
                    if(isUserTotem && confianzaOCR < confianzaMinimaOCRProbabilidadFlotanteTotem * 100){
                        Log.v("patenteRobadaVista", "se descarta por ocr muy bajo: "+patente+ " confianzaMinimaOCRProbabilidadFlotanteTotem: "+confianzaMinimaOCRProbabilidadFlotanteTotem);
                        continue;
                    } else if(confianzaOCR < confianzaMinimaOCRProbabilidadFlotante * 100){
                        Log.v("patenteRobadaVista", "se descarta por ocr muy bajo: "+patente+ " confianzaMinimaOCRProbabilidadFlotante: "+confianzaMinimaOCRProbabilidadFlotante);
                        continue;
                    } else {
                        Log.v("patenteRobadaVista", "se acepta ocr: "+patente);
                    }

                    if (isMaxDetection(patente) == false) {
                        lastPatenteLeida = patente;
                        final BitmapRectF bitmapRectF = getOriginalBitmapByCropResizeRectF(arrayListBitmapRectF, rectF, sensorOrientation, bitmapCollage);
                        //modelo 1
                        if(bitmapRectF != null){
                            Log.v("patenteRobadaVista","bitmapRecF distinto de nulo");
                            PatenteEscaneada patenteEscaneada = crearPatenteEscaneada(patente, bitmapRectF, confianzaOCR);
                            PatenteQueue patenteQueue = new PatenteQueue(patenteEscaneada, bitmapRectF);
                            enqueueDeteccionesPorPatente(patenteQueue);
                        }
                    }
                }
                //se recorre el hashmap de patentes escaneadas para comenzar a enviar las patentes
                dequeueDeteccionesPorPatente();
                Log.v("pasdja","****************** fin ***************** "+numeroFrm);
            }
        });
    }

    public void mostrarDeteccionesForDebug(Bitmap bitmapCollage, final ArrayList<BitmapRectF> arrayListBitmapRectF, ArrayList<PatenteOCRRectF> patenteOCRRectFArrayList){
        if(isDebug()){

            RectF imagenRectF = null;

            Paint paintBordeNaranjo = new Paint();
            paintBordeNaranjo.setColor(Color.parseColor("#FF460B"));
            paintBordeNaranjo.setStyle(Paint.Style.STROKE);
            paintBordeNaranjo.setStrokeWidth(5.0f);
            paintBordeNaranjo.setTextSize(100);

            Paint paintBordeGreen = new Paint();
            paintBordeGreen.setColor(Color.GREEN);
            paintBordeGreen.setStyle(Paint.Style.STROKE);
            paintBordeGreen.setStrokeWidth(5.0f);

            bitmapCollage = rotateBitmap(bitmapCollage,sensorOrientation);
            Canvas canvas = new Canvas(bitmapCollage);

            for (int j = 0; j < patenteOCRRectFArrayList.size(); j++) {
                PatenteOCRRectF patenteOCRRectF = patenteOCRRectFArrayList.get(j);
                RectF rectFText = patenteOCRRectF.getRectF();

                float left = 0;
                float top = 0;
                float right = 0;
                float bottom = 0;
                float maxHeight = 0;
                float maxHeightAcumulado = 0;
                float width = 0;
                float height = 0;

                for (int i = 0; i < arrayListBitmapRectF.size(); i++) {
                    RectF rectFCrop = arrayListBitmapRectF.get(i).getRectF();

                    height = rectFCrop.height();
                    width = rectFCrop.width();
                    Log.v("pasdja222", "width: " + width+" height: "+height);

                    if(sensorOrientation == 90){
                        height = rectFCrop.width();
                        width = rectFCrop.height();
                    }

                    if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                        left = 0;
                        right = 0;
                        maxHeightAcumulado = maxHeightAcumulado + maxHeight;
                        top = maxHeightAcumulado;
                        maxHeight = 0;
                    }

                    if (height > maxHeight) {
                        maxHeight = height;
                    }

                    right = right + width;
                    bottom = top + height;
                    imagenRectF = new RectF(left, top, right, bottom);
                    left = right;

                    Log.v("pasdja", "rectFText.centerX(): " + rectFText.centerX() + " rectFText.centerY(): " + rectFText.centerY() + " rectF de cada deteccion left: " + imagenRectF.left + " top: " + imagenRectF.top + " right: " + imagenRectF.right + " bottom: " + imagenRectF.bottom + " i: " + i);
                    if (imagenRectF.contains(rectFText.centerX(), rectFText.centerY())) {
                        Log.v("pasdja", "lo contiene ");
                        bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage, imagenRectF, Color.parseColor("#FF460B"));
                        bitmapCollage = ponerRecuadroSobreBitmap(bitmapCollage, rectFText, Color.GREEN);
                        canvas.drawText(i + "", imagenRectF.centerX(), imagenRectF.centerY(), paintBordeNaranjo);
                        break;
                    }
                }
            }

            //se pone crop image en preview
            imageViewPatenteCrop.setImageBitmap(bitmapCollage);
        }
    }

    public float confianzaOCR(Text.Line line){
        float confianzaOCR = 0;
        for(int k = 0; k < line.getElements().size(); k++){
            confianzaOCR = confianzaOCR + line.getElements().get(k).getConfidence();
            Log.v("confidencep", "elementos: indice: " + k + " text: "+line.getElements().get(k).getText() + " confianza: " + line.getElements().get(k).getConfidence() + " size: "+ line.getElements().get(k).getSymbols().size());
            for(int l = 0; l < line.getElements().get(k).getSymbols().size(); l++){
                Log.v("confidencep", "simbolos: indice: "+ k +" simbol "+ l +" text: "+line.getElements().get(k).getSymbols().get(l).getText() + " confianza: " + line.getElements().get(k).getSymbols().get(l).getConfidence());
            }
        }
        confianzaOCR = confianzaOCR / line.getElements().size();
        Log.v("confidencep", "confianzaOCR: "+confianzaOCR);

        return confianzaOCR*100;
    }

    public void reconocerTextoEnImagenModelo2(Bitmap bitmap) {
        final Bitmap bitmapFinal = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvasCollage = new Canvas(bitmapFinal);
        canvasCollage.drawBitmap(bitmap, 0, 0, null);
        Integer sensorOrientationFinal = new Integer(sensorOrientation);

        InputImage image = InputImage.fromBitmap(bitmapFinal, sensorOrientationFinal);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result2 =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text result) {
                                if(lecturaCorrecta == false){
                                    // actualiza los campos de errores de un usuario a vacio (costo de escritura 1)
                                    db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("error","","errorDescrip","","errorCode",0);
                                }
                                lecturaCorrecta = true;
                                String resultText = result.getText();

                                for (Text.TextBlock textBlock : result.getTextBlocks()) {
                                    for (int i = 0; i < textBlock.getLines().size(); i++) {
                                        Text.Line line = textBlock.getLines().get(i);
                                        String lineText = line.getText();

                                        String patente = UtilsOCR.esPatenteChilena(lineText);
                                        if (patente != null) {

                                            float confianzaOCR = 0;
                                            Log.v("confidencep", "patente: " + patente);
                                            for(int k = 0; k < line.getElements().size(); k++){
                                                confianzaOCR = confianzaOCR + line.getElements().get(k).getConfidence();
                                                Log.v("confidencep", "elementos: indice: " + k + " text: "+line.getElements().get(k).getText() + " confianza: " + line.getElements().get(k).getConfidence() + " size: "+ line.getElements().get(k).getSymbols().size());
                                                for(int l = 0; l < line.getElements().get(k).getSymbols().size(); l++){
                                                    Log.v("confidencep", "simbolos: indice: "+ k +" simbol "+ l +" text: "+line.getElements().get(k).getSymbols().get(l).getText() + " confianza: " + line.getElements().get(k).getSymbols().get(l).getConfidence());
                                                }
                                            }
                                            confianzaOCR = confianzaOCR / line.getElements().size();
                                            lastPatenteLeida = patente;

                                            if(confianzaOCR < confianzaMinimaOCRProbabilidadFlotante){
                                                break;
                                            }

                                            float finalConfianzaOCR = confianzaOCR*100;

                                            if(isMaxDetection(patente) == false) {
                                                Log.v("patenteRobadaVista","pasooooooooooooooooo");
                                                RectF rectF = new RectF(line.getBoundingBox().left - marginModelo2, line.getBoundingBox().top - marginModelo2, line.getBoundingBox().right + marginModelo2, line.getBoundingBox().bottom + marginModelo2);
                                                BitmapRectF bitmapRectF = new BitmapRectF(bitmapFinal, null, "patente", 0, sensorOrientation);
                                                bitmapRectF.setRectF(rectF);
                                                bitmapRectF.setOriginalRectF(rectF);
                                                //modelo2
                                                if(bitmapRectF != null){
                                                    PatenteEscaneada patenteEscaneada = crearPatenteEscaneada(patente, bitmapRectF, finalConfianzaOCR);
                                                    PatenteQueue patenteQueue = new PatenteQueue(patenteEscaneada, bitmapRectF);
                                                    enqueueDeteccionesPorPatente(patenteQueue);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Text>() {
                            @Override
                            public void onComplete(@NonNull Task<Text> task) {
                                dequeueDeteccionesPorPatente();
                            }
                        });
    }

    /**
     * auto sin patente
     * la patente debe estar contenido dentro del auto
     */
    public void autoSinPatente(ArrayList<BitmapRectF> bitmapRectFsArray, int numeroFrm){
        /**
         * auto sin patente
         */
        if(autoSinPatente){

            boolean existAuto = false;
            //Log.v("nFrame","numero de frame: "+numeroFrm+" size: "+bitmapRectFsArray.size());
            for (int i=0; i < bitmapRectFsArray.size(); i++) {
                BitmapRectF bitmapRectF = null;
                //comprobar que cumple con las condiciones para ser tratado como un auto sin patente
                Log.v("asp","mc: "+bitmapRectFsArray.get(i).getConfidence()+"");
                Log.v("asp","ph: "+bitmapRectFsArray.get(i).getRectF().height()+" pW:"+bitmapRectFsArray.get(i).getRectF().width());

                //se debe partir de la base que siempre se analiza un objeto auto
                if(bitmapRectFsArray.get(i).getTitle().equalsIgnoreCase("auto")){
                    BitmapRectF bitmapRectFAutoSinPatente = bitmapRectFsArray.get(i);

                    existAuto = true;
                    Log.v("nFrame","numero de frame: "+numeroFrm+" auto");
                    RectF rectFAuto = bitmapRectFAutoSinPatente.getOriginalRectF();

                    //filtros para un auto sin patente
                    if(bitmapRectFAutoSinPatente.getConfidence() < confianzaMinimaAutoSinPatenteProbabilidadFlotante){
                        Log.v("nFrame","numero de frame: "+numeroFrm+" menor a la confianza minima: "+bitmapRectFAutoSinPatente.getConfidence() + " de: "+confianzaMinimaAutoSinPatenteProbabilidadFlotante);
                        countAutoSinPatente = 0;
                        break;
                    }

                    float widthAutoSinPatente = bitmapRectFAutoSinPatente.getOriginalRectF().width();
                    float heightAutoSinPatente = bitmapRectFAutoSinPatente.getOriginalRectF().height();

                    if(bitmapRectFAutoSinPatente.getSensorOrientation() == Configuration.ORIENTATION_PORTRAIT){
                        widthAutoSinPatente = bitmapRectFAutoSinPatente.getOriginalRectF().height();
                        heightAutoSinPatente = bitmapRectFAutoSinPatente.getOriginalRectF().width();
                    }

                    //el auto sin patente tiene que ser mayor al 20% del preview
                    if(heightAutoSinPatente < 0.2 * previewHeight ||
                            widthAutoSinPatente < 0.2 * previewWidth){
                        break;
                    }

                    boolean autoContienePatente = false;
                    boolean existePatente = false;
                    int indice = 0;
                    //verificar si auto tiene un objeto patente dentro de su bounding box
                    for (int j=0; j<bitmapRectFsArray.size(); j++) {
                        BitmapRectF bitmapRectFAux = bitmapRectFsArray.get(j);
                        if(bitmapRectFAux.getTitle().equalsIgnoreCase("patente")){
                            existePatente = true;
                            Log.v("nFrame","numero de frame: "+numeroFrm+" patente");
                            RectF rectFPatente = bitmapRectFAux.getOriginalRectF();
                            if(rectFAuto.contains(rectFPatente)){
                                Log.v("nFrame","numero de frame: "+numeroFrm+" auto contiene patente");
                                bitmapRectFAux.setAutoContainsPatente(true);
                                autoContienePatente = true;
                                indice = j;
                                break;
                            }
                        }
                    }

                    //si tiene un objeto patente
                    if(autoContienePatente){
                        //verificar si la confianza es mayor a un 50% NO sumar
                        float confianzaPatenteAutoSinPatente = 0.5f;
                        BitmapRectF bitmapRectFPatenteDeAutoSinPatente = bitmapRectFsArray.get(indice);
                        if(bitmapRectFPatenteDeAutoSinPatente.getConfidence() >= confianzaPatenteAutoSinPatente){
                            Log.v("nFrame","numero de frame: "+numeroFrm+ " se descarta frame por tener patente con una confianza mayor a un 50%");
                            countAutoSinPatente = 0;
                            break;
                        } else {
                            Log.v("nFrame","numero de frame: "+numeroFrm+ " confianza menor a un 50%: "+bitmapRectFPatenteDeAutoSinPatente.getConfidence());
                        }
                    } else if(existePatente) {
                        Log.v("nFrame","numero de frame: "+numeroFrm+ " se descarta frame por tener patente fuera del bounding box del auto");
                        countAutoSinPatente = 0;
                        break;
                    }

                    //si pasa todos las restricciones anteriores se asigna el auto sin patente
                    bitmapRectF = bitmapRectFAutoSinPatente;
                }

                //comprueba que todas las patentes tengan el campo AutoContainsPatente = true
                for(int j=0; j<bitmapRectFsArray.size(); j++){
                    if(bitmapRectFsArray.get(j).getTitle().equalsIgnoreCase("patente")){
                        if(!bitmapRectFsArray.get(j).isAutoContainsPatente()){
                            Log.v("nFrame","numero de frame: "+numeroFrm+ " se descarta frame porque existe una patente que no está contenida en un auto");
                            countAutoSinPatente = 0;
                            break;
                        }
                    }
                }

                //en la ultima iteración
                if(i+1 == bitmapRectFsArray.size()){
                    if(!existAuto){
                        Log.v("nFrame","numero de frame: "+numeroFrm+" NO existe auto, se descarta frame");
                        countAutoSinPatente = 0;
                        break;
                    }

                    if(bitmapRectF == null){
                        Log.v("nFrame","numero de frame: "+numeroFrm+" se descarta frame porque bitmapRectF es nulo");
                        countAutoSinPatente = 0;
                        break;
                    }

                    //Crear restriccion de tiempo
                    if(tiempoActualAutoSinPatente == 0 ||
                            new CurrentDate(new Date()).getDate().getTime() - tiempoActualAutoSinPatente
                                    < esperaFramesAutoSinPatenteMilis){
                        Log.v("nFrame","numero de frame: "+numeroFrm+" aumenta contador");
                        countAutoSinPatente++;
                    } else {
                        Log.v("nFrame","numero de frame: "+numeroFrm+" setea contador a 1 por exceder el tiempo maximo:" + esperaFramesAutoSinPatenteMilis +" por:"+ tiempoActualAutoSinPatente);
                        countAutoSinPatente = 1;
                    }

                    tiempoActualAutoSinPatente = new CurrentDate(new Date()).getDate().getTime();
                    Log.v("nFramec","countAutoSinPatente: "+countAutoSinPatente+" cantidadMaximaFramesAutoSinPatente: "+cantidadMaximaFramesAutoSinPatente);
                    //crear auto sin patente
                    if(countAutoSinPatente >= cantidadMaximaFramesAutoSinPatente * fps) {
                        Log.v("nFrame","numero de frame: "+numeroFrm+" crear auto sin patente");
                        PatenteEscaneada patenteEscaneada = crearPatenteEscaneada("", bitmapRectF, 0);
                        patenteEscaneada.setSinPatente(true);
                        if (autosSinPatente.size() == 0 || (new CurrentDate(new Date()).getDate().getTime() - autosSinPatente.get(autosSinPatente.size() - 1).getTiempoInicial() > 30000)) {
                            autosSinPatente.add(patenteEscaneada);
                            setComunaFromAPI(patenteEscaneada, bitmapRectF, false, false, -1);
                        }
                        countAutoSinPatente = 0;
                    }
                }
            }
            Log.v("nFrame","numero de frame: "+numeroFrm+ " countAutoSinPatente: "+countAutoSinPatente);
        }
    }

    //pre Filtros Para Eliminar Objetos Que No Son Auto o patente
    public boolean preFiltrosAutoOPatenteConElCrop(Detector.Recognition deteccion) {
        /*
        if(TF_OD_API_MODEL_FILE.equalsIgnoreCase("mobilenetv1.tflite")){
            if (resultDetectionTF.getTitle().equalsIgnoreCase("car")) {
                resultDetectionTF.setTitle("auto");
            }
        }
         */
        //limitar autos o patentes deteccion exclusiva
        /*
        if(deteccion.title.equalsIgnoreCase("auto")){
            return false;
        }
         */
        if (isUserTotem && deteccion.getLocation() != null && deteccion.getConfidence() < confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem) {
            return false;
        } else if (deteccion.getLocation() != null && deteccion.getConfidence() < confianzaMinimaDeteccionPatenteProbabilidadFlotante) {
            return false;
        }

        /**
         * Pre filtros para eliminar casos donde se detecten objetos que no son autos o patentes
         * */
        if (deteccion.getLocation().width() == 0) {
            Log.v("cropResize", "rectFWidth " + deteccion.getLocation().width() + " igual a 0");
            return false;
        }

        if (deteccion.getLocation().height() == 0) {
            Log.v("cropResize", "rectFHeight " + deteccion.getLocation().height() + " igual a 0");
            return false;
        }

        if (deteccion.getLocation().height() > cropSize) {
            Log.v("cropResize", "widthPatente " + deteccion.getLocation().height() + " mayor al previewHeight: " + previewHeight);
            return false;
        }

        if (deteccion.getLocation().width() > cropSize) {
            Log.v("cropResize", "widthPatente " + deteccion.getLocation().width() + " mayor al previewHeight: " + previewWidth);
            return false;
        }
        return true;
    }

    public void showImageViewPatenteCropDebug(final Bitmap bitmap){
        if(isDebug()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!bitmap.isRecycled()){
                        imageViewPatenteCrop.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    public void aumentaContadorDeTotales(){
        //mediaPlayerNotificacionPatenteNoRobada.start();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if ((contadorPatentesDistintas >= maximoDeteccionesTotales) && isUserLite){
                    irOtraActividad = true;
                    startActivityForResult(new Intent(DetectorActivity.this, SesionLiteExpirada.class), REQUEST_ABOUT);
                }
                else{
                    contadorPatentesDistintas++;
                    contadorPatentesDistintasOnResume++;
                    textViewPatentesLeidas.setText(contadorPatentesDistintas+"");

                    if(arrayListPatentesDistintas.size() > 0 && contadorPatentesDistintasOnResume % 20 == 0){
                        updateTotalPatenteEscaneada(contadorPatentesDistintasOnResume);
                        updateTotalPatenteEscaneadaPorUsuario(contadorPatentesDistintasOnResume);
                        contadorPatentesDistintasOnResume = 0;
                    }
                    contadorDePatentes.add(1);
                }
            }
        });
    }

    public void dequeueDeteccionesPorPatente(){
        Log.v("patenteRobadaVista","dequeueDeteccionesPorPatente");
        if(velocidad > velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos){
            minimoDeLecturas = 1;
        }
        final HashMap<String, ArrayList<PatenteQueue>> hashMapPatentesQueueCopy = (HashMap<String, ArrayList<PatenteQueue>>) hashMapPatentesQueue.clone();
        hashMapPatentesQueue.clear();
        ArrayList<String> patentesToDelete = new ArrayList<>();
        for (Map.Entry<String,ArrayList<PatenteQueue>> entry : hashMapPatentesQueueCopy.entrySet()) {
            final String patente = entry.getKey();
            final ArrayList<PatenteQueue> patenteQueueArrayList = entry.getValue();
            Log.v("pasdja3"," - "+patente+" size: "+patenteQueueArrayList.size() +" cantidadDePatentesIguales: "+ minimoDeLecturasFromDB);
            if(patenteQueueArrayList.size() >= minimoDeLecturas) {
                Log.v("patenteRobadaVista","mayor o igual a minimoDeLecturas: "+minimoDeLecturas);
                for(PatenteQueue patenteQueue : patenteQueueArrayList) {
                    clasificarPatente(patenteQueue,true);
                }
            } else {
                if(hashMapMaxDetection.containsKey(patente)){
                    Log.v("pasdja3", "patente: "+patente+" hashMapMaxDetection.get(patente).size(): "+hashMapMaxDetection.get(patente).size()+" maximoDetecciones: "+(maximoDetecciones-2));
                    if(hashMapMaxDetection.get(patente).size() >= (maximoDetecciones - minimoDeLecturas) || patentesToDelete.contains(patente)){
                        patentesToDelete.add(patente);
                    }
                }
            }
        }
        hashMapPatentesQueueCopy.clear();
        for(int i = 0; i < patentesToDelete.size(); i++){
            hashMapMaxDetection.get(patentesToDelete.get(i)).remove(0);
        }
        patentesToDelete.clear();
        Log.v("patEscDetecMap","**************************: ");
    }

    public void enqueueDeteccionesPorPatente(PatenteQueue patenteQueue){
        final String patente = patenteQueue.getPatenteEscaneada().getPatente();
        ArrayList<PatenteQueue> patenteQueueArray;
        //se almacenan las patentes escaneadas en hashmap donde posteriormente son extraídas
        if(hashMapPatentesQueue.containsKey(patente)){
            patenteQueueArray = hashMapPatentesQueue.get(patente);
            patenteQueueArray.add(patenteQueue);
            hashMapPatentesQueue.put(patente, patenteQueueArray);
        } else {
            //si no existe agregar
            Log.v("patenteRobadaVista","no existe agregar hashMapPatentesQueue");
            patenteQueueArray = new ArrayList<PatenteQueue>();
            patenteQueueArray.add(patenteQueue);
            hashMapPatentesQueue.put(patente, patenteQueueArray);
        }
    }

    public boolean isMaxDetection(String patente){
        int index = existPatenteInPatenteEscaneadaArray(arrayListPatentesEscaneadasDetection, patente);
        //si existe key agregar
        if (index == -1) {
            //llamo a onreceive
            Log.v("patenteRobadaVista","isMaxDetection caso 1");
            /*
            //si ultimo tiempo de conexion es mayor al tiempo maximo conexion para enviar patente a firebase no hacer nada
            if(ultimoTiempoDeConexion != 0 && new CurrentDate(new Date()).getDate().getTime() - ultimoTiempoDeConexion > esperaEnvioPatenteFirebaseMilis){
                Log.v("patenteRobadaVista","se excede tiempo maximo de no conexion a internet: "+ esperaEnvioPatenteFirebaseMilis);
                return false;
            }
             */

            PatenteEscaneada patenteEscaneada = new PatenteEscaneada(patente, getLastKnownLatitudString(), getLastKnownLongitudString());
            arrayListPatentesEscaneadasDetection.add(patenteEscaneada);
            return false;
        } else {
            if(hashMapMaxDetection.containsKey(patente)){
                //si es menor a 10 agregar
                if (hashMapMaxDetection.get(patente).size() < maximoDetecciones - 1) {
                    ArrayList<PatenteEscaneada> arrayAux = hashMapMaxDetection.get(patente);
                    PatenteEscaneada patenteEscaneada = new PatenteEscaneada(patente, getLastKnownLatitudString(), getLastKnownLongitudString());
                    arrayAux.add((PatenteEscaneada) patenteEscaneada);
                    hashMapMaxDetection.put(patente, arrayAux);
                    //llamo a onreceive
                    Log.v("patenteRobadaVista","isMaxDetection caso 2: "+hashMapMaxDetection.get(patente).size() +" "+ (maximoDetecciones - 1));
                    return false;

                } else {
                    double latitudInicial = Double.parseDouble(hashMapMaxDetection.get(patente).get(0).getLatitud());
                    double longitudInicial = Double.parseDouble(hashMapMaxDetection.get(patente).get(0).getLongitud());

                    double latitudFinal = lastKnownLatitud;
                    double longitudFinal = lastKnownLongitud;

                    //Log.v("patenteRobadaVista","latitudInicial: "+latitudInicial+" longitudInicial: "+longitudInicial +" latitudFinal: "+latitudFinal+" longitudFinal:"+longitudFinal );

                    if(latitudInicial == latitudFinal && longitudInicial == longitudFinal){
                        //Log.v("patenteRobadaVista","isMaxDetection caso 3.5 "+patente);
                        return true;
                    }

                    if(!isUserTotem) {
                        boolean verificaSiPatenteEstaDentroDelAreaMaximaPermitida = isDistanciaMenor(latitudInicial, longitudInicial, latitudFinal, longitudFinal, distanciaMinimaParaVolverAEscanear);
                        //si es mayor a 100 metros limpiar arraylist
                        if (verificaSiPatenteEstaDentroDelAreaMaximaPermitida == false && !existeSetRafaga(hashMapMaxDetection.get(patente).get(0)) && existPatenteInPatenteDistintasArray(arrayListPatentesDistintas, patente) != -1) {
                            hashMapMaxDetection.get(patente).clear();
                            PatenteEscaneada patenteEscaneada = new PatenteEscaneada(patente, getLastKnownLatitudString(), getLastKnownLongitudString());
                            hashMapMaxDetection.get(patente).add(patenteEscaneada);
                            hashMapMaxDetection.put(patente, hashMapMaxDetection.get(patente));
                            Log.v("patenteRobadaVista", "isMaxDetection caso 3");
                            //llamo a onreceive
                            return false;
                        }
                    }
                }
            } else {
                //si no existe agregar
                PatenteEscaneada patenteEscaneada = new PatenteEscaneada(patente, getLastKnownLatitudString(), getLastKnownLongitudString());
                ArrayList<PatenteEscaneada> bagPatenterobadaVista = new ArrayList<PatenteEscaneada>();
                bagPatenterobadaVista.add((PatenteEscaneada) patenteEscaneada);
                hashMapMaxDetection.put(patente, bagPatenterobadaVista);
                Log.v("patenteRobadaVista","isMaxDetection caso 4");
                return false;
            }
        }

        Log.v("patenteRobadaVista","isMaxDetection caso 5 "+patente);
        return true;
    }

    public RectF rotateRectF(RectF rectF, int sensorOrientation){
        RectF rectFRotated = rectF;

        if(sensorOrientation == 0 || sensorOrientation == 180){

        } else if (sensorOrientation == 90 || sensorOrientation == 270){
            //se debe rotar rectF 90 grados en sentido positivo
        }

        return rectFRotated;
    }

    public BitmapRectF getOriginalBitmapByCropResizeRectF(ArrayList<BitmapRectF> arrayListBitmapRectF, RectF rectFText, Integer sensorOrientation, Bitmap bitmapCollage){
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        float maxHeight = 0;
        float width = 0;
        float height = 0;
        float maxHeightAcumulado = 0;
        RectF imagenRectF = null;

        for (int i = 0; i < arrayListBitmapRectF.size(); i++) {
            RectF rectFCrop = arrayListBitmapRectF.get(i).getRectF();

            height = rectFCrop.height();
            width = rectFCrop.width();
            if(sensorOrientation == 90){
                height = rectFCrop.width();
                width = rectFCrop.height();
            }

            if (i > 0 && i % cantidadDeImagenesDelCollage == 0) {
                left = 0;
                right = 0;
                maxHeightAcumulado = maxHeightAcumulado + maxHeight;
                top = maxHeightAcumulado;
                maxHeight = 0;
            }

            if (height > maxHeight) {
                maxHeight = height;
            }

            right = right + width;
            bottom = top + height;
            imagenRectF = new RectF(left, top, right, bottom);
            left = right;

            Log.v("pasdja", "* size: "+arrayListBitmapRectF.size()+" rectF de cada deteccion left: " + imagenRectF.left + " top: " + imagenRectF.top + " right: " + imagenRectF.right + " bottom: " + imagenRectF.bottom + " i: " + i);
            Log.v("pasdja", "** rectFTextleft: " + rectFText.left + " rectFTexttop: " + rectFText.top + " rectFTextright: " + rectFText.right + " rectFTextbottom: " + rectFText.bottom+" i: " + i);
            Log.v("pasdja", "*** rectFText.centerX(): "+rectFText.centerX()+" rectFText.centerY(): "+rectFText.centerY()+ "i: " + i);
            if (imagenRectF.contains(rectFText.centerX(), rectFText.centerY())) {
                Log.v("pasdja", "**** Lo contiene retorna bitmap***");
                return arrayListBitmapRectF.get(i);
            }
        }
        Log.v("pasdja", "**** return null arrayListBitmapRectF: "+arrayListBitmapRectF.size());
        Log.v("pasdja", "*******************************");
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    protected void setDesiredPreviewFrameSize(int w, int h) {
        DESIRED_PREVIEW_SIZE = new Size(w,h);
    }

    @Override
    public void onClick(View view) {
        Log.v("volumex","clic en cualquier lado");
        brilloDePantallaNormal();
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(
                () -> {
                    try {
                        detector.setUseNNAPI(isChecked);
                    } catch (UnsupportedOperationException e) {
                        Log.e("detectorAc", "Failed to set \"Use NNAPI\".");
                        runOnUiThread(
                                () -> {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                });
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }

    public boolean existeGrupo(List<String> grupos, String grupoABuscar) {
        Log.v("asde3r3","pase 13.1 grupos: "+grupos+ " grupoABuscar: "+grupoABuscar);
        for (String grupo : grupos) {
            Log.v("asde3r3","pase 13.2 grupo: "+grupo +" grupoABuscar: "+grupoABuscar);
            if (grupoABuscar!= null && grupo!=null && grupoABuscar.equalsIgnoreCase(grupo)) {
                Log.v("asde3r3","pase 13.3 existe");
                return true;
            }
            Log.v("asde3r3","pase 13.4");
        }
        Log.v("asde3r3","pase 13.5");
        return false;
    }

    public boolean existeGrupoTipo(List<GroupType> grupoTipos, String grupoABuscar, String tipo) {
        for (GroupType grupo : grupoTipos) {
            if (grupoABuscar != null && grupoABuscar.equalsIgnoreCase(grupo.getNombre()) && tipo.equalsIgnoreCase(grupo.getTipo())) {
                return true;
            }
        }
        return false;
    }

    public boolean existeTipo(List<Map<String,String>> listaGrupoTipo, String tipoABuscar) {
        for (Map<String, String> grupoTipo : listaGrupoTipo) {
            // Pregunto si el usuario tiene un grupo de tipo comuna
            if (tipoABuscar != null && tipoABuscar.equalsIgnoreCase(grupoTipo.get("tipo"))) {
                return true;
            }
        }
        return false;
    }

    public void crearPatenteEscaneadaFirebase(PatenteEscaneada patenteEscaneada) {
        //obtiene un id de coleccion patenteEscaneada (costo de lectura 0)
        String id = db.collection(Referencias.PATENTEESCANEADA).document().getId();
        //PatenteEscaneadaFirebase patenteEscaneadaFirebase = new PatenteEscaneadaFirebase(id,pE.getPatente(),pE.getLatitud(),pE.getLongitud(),pE.getGrupo(),pE.getListaGrupoTipo(),pE.getTipo(),pE.getUrlImagen(),pE.getUrlImagenAmpliada(),pE.getEmailUsuario(),pE.getComuna(),pE.getFecha());
        Log.v("patenteRobadaVista", "ENTRE AL METODO crearPatenteEscaneadaFirebase con la patenteEscaneada:"+patenteEscaneada.toString());
        patenteEscaneada.setId(id);
        Log.v("patenteRobadaVista", "crearPatenteEscaneadaFirebase - se genera el id de la patente escaneada: "+id);
        Log.v("patenteRobadaVista", "crearPatenteEscaneadaFirebase - patente escaneada: "+patenteEscaneada.toString());
        //crea una patente escaneada en coleccion patenteEscaneada (costo de escritura 1)
        db.collection(Referencias.PATENTEESCANEADA).document(id).set(patenteEscaneada).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.v("patenteRobadaVista", "crearPatenteEscaneadaFirebase - hubo un error: "+task.getException().getMessage());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("patenteRobadaVista", "onFailure: "+e.getMessage());
            }
        });
        Log.v("patenteRobadaVista", "crearPatenteEscaneadaFirebase - despues de db.collection(Referencias.PATENTEESCANEADA)");

    }

    private void updateUrlImagenPatente(PatenteEscaneada patente, boolean isEscaneada) {
        Map<String, Object> patenteMap = new HashMap<>();
        patenteMap.put("urlImagen", patente.getUrlImagen());
        patenteMap.put("timestamp", FieldValue.serverTimestamp());
        if(isEscaneada){
            //actualiza el campo urlImagen y timestamp en patenteEscaneada (costo de escritura 1)
            db.collection(Referencias.PATENTEESCANEADA)
                    .document(patente.getId())
                    .update(patenteMap);
        } else {
            enviarImagenPatenteApi(patente, false);
            Log.v("crearafaga1","patente update: "+patente.getId());
            //actualiza el campo urlImagen y timestamp en posiblePatenteRobadaVista (costo de escritura 1)
            db.collection(Referencias.POSIBLEPATENTEROBADAVISTA).document(patente.getId()).update(patenteMap);
        }
        //llamar al hashmap dequeue
        dequeueHashMapPatenteGustafox(patente.getPatente());

    }

    private void updateUrlImagenAmpliadaPatente(PatenteEscaneada patente, boolean isEscaneada) {
        Map<String, Object> patenteMap = new HashMap<>();
        patenteMap.put("urlImagenAmpliada", patente.getUrlImagenAmpliada());
        patenteMap.put("timestamp", FieldValue.serverTimestamp());
        if(isEscaneada){
            //actualiza el campo urlImagenAmpliada y timestamp en patenteEscaneada (costo de escritura 1)
            db.collection(Referencias.PATENTEESCANEADA)
                    .document(patente.getId())
                    .update(patenteMap);
        } else {
            enviarImagenPatenteApi(patente, true);
            Log.v("crearafaga1","patente update: "+patente.getId());
            //actualiza el campo urlImagenAmpliada y timestamp en posiblePatenteRobadaVista (costo de escritura 1)
            db.collection(Referencias.POSIBLEPATENTEROBADAVISTA).document(patente.getId()).update(patenteMap);
        }

    }

    public String getLastKnownLatitudString() {
        Log.v("Sefaasd2","latitudUserFromDB: "+latitudUserFromDB);
        if(latitudUserFromDB != 0.0f){
            this.lastKnownLatitud = latitudUserFromDB;
            Log.v("Sefaasd2","es != 0.0  ");
        } else {
            Log.v("Sefaasd2","else latitudUserFromDB: "+latitudUserFromDB);
        }
        Log.v("Sefaasd2","lastKnownLatitud: "+lastKnownLatitud);
        return String.valueOf(this.lastKnownLatitud);
    }

    public String getLastKnownLongitudString() {
        if(longitudUserFromDB != 0.0f){
            this.lastKnownLongitud = longitudUserFromDB;
        }
        return String.valueOf(this.lastKnownLongitud);
    }

    //Este metodo se ejecuta cada 5 segundos
    public void updateUbicacionUsuario(int i) {
        isUpdateUbicacionUsuario = false;
        Log.v("locationn","Lock update location process: "+ i);
        Set<String> patentesSet = Utils.leerValorSetString(DetectorActivity.this, PATENTES);
        List<String> patentesArray = new ArrayList<>();
        patentesArray.addAll(patentesSet);
        CurrentDate currentDate = new CurrentDate(new Date());
        Log.v("locationn","Lock antes del update");

        //se actualiza el campo activo en coleccion usuario (costo de escritura 1)
        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("activo", "true", "ubicacionActiva", locationAvaible)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        SendUbicacion sendUbicacion = new SendUbicacion(idUsuarioFirebase, emailUsuarioFirebase, nombreUsuarioFirebase, apellidoUsuarioFirebase, getLastKnownLatitudString(), getLastKnownLongitudString(), currentDate.getFecha(), currentDate.getHora(), currentDate.getLongTime(), patentesArray, idUsuarioFirebase, listaGrupoGlobal, tokenDeviceFirebase, locationAvaible, latitud.toString(), longitud.toString());
                        //cuando el contador sea igual a 12 quiere decir que ha pasado 1 minuto desde la ultima vez que se envio ubicacion gps
                        // se envia ubicacion gps cada 5 segundos 12*5 = 60 segundos

                        //crea un nuevo registro de ubicacion usuario, este registro se está creando cada 10 segundos
                        Log.v("locationnnn","division: "+Math.ceil(frecuenciaCreacionUbicacionMilis /frecuenciaEnvioUbicacionUsuarioMilis));
                        Log.v("locationnnn","contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario: "+contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario);
                        if(firstTimeContadorDeVecesQueSeEjecutaUpdateUbicacionUsuario || contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario >= Math.ceil(frecuenciaCreacionUbicacionMilis /frecuenciaEnvioUbicacionUsuarioMilis)){
                            firstTimeContadorDeVecesQueSeEjecutaUpdateUbicacionUsuario = false;
                            contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario = 0;
                            Log.v("locationnnn","guarda ubicacion");
                            //se crea la ubicación del usuario en coleccion ubicacion (costo de escritura 1)
                            db.collection(Referencias.UBICACION).document(emailUsuarioFirebase).collection(new CurrentDate(new Date()).getFecha()).add(sendUbicacion).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Log.v("locationn","Unlock update location process: "+i);
                                    isUpdateUbicacionUsuario = true;
                                }
                            });
                        }

                        //se sobrescribe la ubicación del usuario en coleccion ubicacion (costo de escritura 1)
                        db.collection(Referencias.UBICACION).document(emailUsuarioFirebase).set(sendUbicacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.v("locationn","Unlock update location process: "+i);
                                isUpdateUbicacionUsuario = true;
                            }
                        });
                        contadorDeVecesQueSeEjecutaUpdateUbicacionUsuario++;
                    }
                });
    }

    public void taskConexion() {
        timerConexion.schedule(
                timerTaskConexion = new TimerTask(){
                    public void run() {
                        isConnected();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!isConnected){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetectorActivity.this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }, esperaComprobacionInternetMilis,frecuenciaComprobacionInternetMilis);
    }

    public boolean isConnected(){
        cm = (ConnectivityManager) DetectorActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        //Log.v("patenteRobadaVista","isconnected: "+isConnected);
        if(isConnected){
            ultimoTiempoDeConexion = 0;
        } else {
            if(ultimoTiempoDeConexion == 0){
                ultimoTiempoDeConexion = new CurrentDate(new Date()).getDate().getTime();
            }
        }
        return isConnected;
    }

    public void taskSendUbicacionUsuario() {
        if(timerSendUbicacionUsuario == null) {
            timerSendUbicacionUsuario = new Timer();
            Log.v("locationnn","procesoSendUbicacion: "+procesoSendUbicacion);
            procesoSendUbicacion++;
        }

        if(timerTaskSendUbicacionUsuario == null) {

            timerTaskSendUbicacionUsuario = new TimerTask() {
                public void run() {
                    if (isUpdateUbicacionUsuario) {
                        //Log.v("locationnn", "Task send ubicacion lat: " + lastKnownLatitud + " lng: " + lastKnownLongitud + " location_avaible: " + locationAvaible);
                        //lockUpdateLocationProcess++;
                        updateUbicacionUsuario(lockUpdateLocationProcess);
                    }
                    return;
                }
            };
            timerSendUbicacionUsuario.schedule(timerTaskSendUbicacionUsuario, esperaEnvioUbicacionUsuarioMilis, frecuenciaEnvioUbicacionUsuarioMilis);
        }
    }

    public void taskSinGPS() {
        if(timerSinGPS == null){
            timerSinGPS = new Timer();
        }

        if(timerTaskSinGPS == null) {
            timerTaskSinGPS = new TimerTask() {
                public void run() {
                    Log.v("gps0", "latitud: " + latitud + " longitud: " + longitud);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (latitud == 0.0 || longitud == 0.0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetectorActivity.this, "Sin GPS", Toast.LENGTH_SHORT).show();
                                        iniciarLocationService("Normal", "false");
                                    }
                                });

                            } else {
                                contadorSinGPS = 0;
                            }
                        }
                    });
                }
            };
            timerSinGPS.schedule(timerTaskSinGPS, esperaComprobacionGPSMilis, frecuenciaComprobacionGPSMilis);
        }
    }

    public void taskSendStatusTotem() {
        if(timerSendUbicacionTotem == null){
            timerSendUbicacionTotem = new Timer();
        }
        if(timerTaskSendUbicacionTotem == null) {
            timerTaskSendUbicacionTotem = new TimerTask() {
                public void run() {
                    Log.v("totem","task isconected:"+isConnected);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateStatusTotem();
                        }
                    });
                }
            };
            timerSendUbicacionTotem.schedule(timerTaskSendUbicacionTotem, esperaEnvioUbicacionTotemMilis, frecuenciaEnvioUbicacionTotemMilis);
        }
    }


    public void activaODesactivaTotem() {
        //retorno en caso que el estado del totem sea igual al estado de la variable golbal de actividad de totem
        //es decir no hago nada en caso que sean iguales
        if(totem == null){
            Log.v("totemHD","totem nulo retornar");
            return;
        }

        if(totem != null && totem.isActivo() == isTotemActivo){
            Log.v("totemHD","totem igual retornar: "+isTotemActivo);
            return;
        }
        //si totem es activo enciendo el modo totem
        if (totem.isActivo()) {
            Log.v("totemHD","activar totem: "+totem.isActivo());
            activarTotem();
        } else {
            Log.v("totemHD","desactivar totem: "+totem.isActivo());
            totemEnModoReposo();
        }
    }

    public void activarGPS() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);

    }

    private boolean canToggleGPS() {
        PackageManager pacman = getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                //test if recevier is exported. if so, we can toggle GPS.
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                    return true;
                }
            }
        }

        return false; //default
    }


    public void desactivarGPS() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


    public void setVerificarPatenteRobadaVistaPorPatente(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista) {
        ArrayList<PatenteRobadaVista> patentesRobadasVistasReverse = patentesRobadasVistas;
        Collections.reverse(patentesRobadasVistasReverse);
        for (int i = 0; i < patentesRobadasVistasReverse.size(); i++) {
            if (patentesRobadasVistasReverse.get(i).getVerificada().equalsIgnoreCase("false")) {
                if (patentesRobadasVistasReverse.get(i).getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                    patentesRobadasVistasReverse.get(i).setVerificada("true");
                }
            }
        }
    }

    public boolean isUsuarioEnGrupo(PatenteRobadaVista patenteRobadaVista) {
        for (int i = 0; i < listaGrupoGlobal.size(); i++) {
            if (listaGrupoGlobal.get(i).equalsIgnoreCase(Referencias.ROOT)) {
                return true;
            }
            if (patenteRobadaVista.getGrupo() != null) {
                for (int j = 0; j < patenteRobadaVista.getGrupo().size(); j++) {
                    if (listaGrupoGlobal.get(i).equalsIgnoreCase(patenteRobadaVista.getGrupo().get(j))) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }

        if (listaGrupoGlobal.size() == 0) {
            return true;
        }
        return false;
    }

    public boolean isSpeechInicialVerbalizado(){
        String fechaAnterior = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHINICIALVERBALIZADO);
        if(fechaAnterior != null && !fechaAnterior.equalsIgnoreCase(String.valueOf(new CurrentDate(new Date()).getFecha()))){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHINICIALVERBALIZADO, String.valueOf(new CurrentDate(new Date()).getFecha()));
            return false;
        } else {
            return true;
        }
    }

    //metodo que permite reiniciar la app de un totem
    public void observableReiniciarTotem(){
        //observable que reinicia un totem (costo de lectura 1)
        reiniciarTotem = db.collection(Referencias.TOTEM)
                .whereEqualTo("emailUsuario", emailUsuarioFirebase)
                .whereEqualTo("reiniciar", true).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot dc : queryDocumentSnapshots) {
                    Totem totem;
                    try {
                        totem = dc.toObject(Totem.class);
                        if(totem.isReiniciar()){
                            Utils.ejecutarLoginActivity(DetectorActivity.this);
                        }
                    } catch (Exception e){
                        Log.v("totemE",e.getMessage());
                    }
                }
            }
        });
    }

    public void observableMessages(){
        //observableBroadcastMessages
        CurrentDate currentDate = new CurrentDate(new Date());
        String fecha = currentDate.getYear()+"-"+currentDate.getMonth()+"-"+currentDate.getDay();
        Log.v("fechasda","fecha: "+fecha +" -"+currentDate.getDate().toString());
        //observable de coleccion broadcastMessages (costo de lectura 1)
        Query query = db.collection("broadcastMessages").whereEqualTo("fecha",fecha).limit(1);
        observableBroadcastMessages = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                    Messages messages = queryDocumentSnapshot.toObject(Messages.class);
                    textToSpeech(messages.getText(),true);
                }
            }
        });
    }

    //posible patente robada vista observable
    public void getPosiblePatenteRobadaVistaFirebaseObservaCadaVezQueHayaUnCambio() {
        //en caso que haya un observable activo lo remueve
        if(observablePosiblePatenteRobadaVista != null){
            observablePosiblePatenteRobadaVista.remove();
        }

        CurrentDate currentDate = new CurrentDate(new Date());
        Timestamp timestamp = new Timestamp(currentDate.getDateMenosXMinutos());

        //observable de coleccion posiblePatenteRobdadaVista obtiene la ultima patente (costo de lectura 1 o mas)
        Query query = db.collection(Referencias.POSIBLEPATENTEROBADAVISTA)
                .whereGreaterThanOrEqualTo("timestamp", timestamp)
                .whereEqualTo("headRafaga",true)
                .whereEqualTo("verificada", "true")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .orderBy("urlImagenAmpliada")
                .orderBy("urlImagen")
                .limit(1);

        observablePosiblePatenteRobadaVista = query.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.v("patenteRobadaVista", "Listen failed."+ e);
                            return;
                        }

                        Log.v("nadafunciona", "dentro de la query. Resultados query" + queryDocumentSnapshots.size());
                        for (QueryDocumentSnapshot dc : queryDocumentSnapshots) {
                            PatenteRobadaVista patenteRobadaVista = null;
                            try {
                                Log.v("patenteRobadaVista", " Objeto creado en getPosiblePatenteRobadaVistaFirebaseObservaCadaVezQueHayaUnCambio - ID: "+dc.getId()+" observable dc.getData()" + dc.getData());
                                patenteRobadaVista = dc.toObject(PatenteRobadaVista.class);
                            } catch (Exception a) {
                                Log.v("patenteRobadaVista", "Error convertir CadaVezQueHaya" + a);
                                return;
                            }
                            Gson gson = new Gson();
                            String json = gson.toJson(dc.getData());

                            Log.v("patenteRobadaVista", "ESTE ES UN OBJETO:"+json);

                            //si NO es robada y si es lista negra pero no pertenece a los grupos de usuario retorna
                            if(!verificacionesComunesPatenteRobadaVista(patenteRobadaVista)){
                                Log.v("patenteRobadaVista", "no cumple con verificacionesComunesPatenteRobadaVista");
                                continue;
                            }

                            //2) patentes con o sin encargo
                            // Se convierte OFICIALMENTE en una patente robadaVista/ListaNegra con/sin encargo
                            else if (patenteRobadaVista.getRobada() != null && patenteRobadaVista.getIgualAImagen() != null
                                    && patenteRobadaVista.getVisible()) {
                                Log.v("patenteRobadaVista", "patente caso 2 - se convierte oficialmente en una patente robadaVista/ListaNegra con/sin encargo");
                                patenteRobadaVistaConOSinEncargo(patenteRobadaVista);
                            }
                            //3) posible patentes desestimadas
                            //Cuando la patente no es igual a la imagen, es un falsa alarma, la imagen no coincide con el textrecognizer
                            else if (patenteRobadaVista.getVisible() == false) {
                                Log.v("patenteRobadaVista", "patente caso 3 - la patente no es igual a la imagen, es una falsa alarma, la imagen no coincide");
                                posiblePatenteRobadaVistaDesestimada(patenteRobadaVista);
                            } else {
                                Log.v("patenteRobadaVista", "no pertenece a ninguna categoria CadaVezQueHaya");
                            }

                            break;
                        }
                    }
                });
    }

    //si NO es robada y si es lista negra pero no pertenece a los grupos de usuario retorna
    public boolean verificaSiPatentePerteneceAGrupoEscaneada(PatenteEscaneada patenteEscaneada){
        //if(patenteEscaneada.isBaseRobada()){
        //  return true;
        //}
        //else if (patenteEscaneada.isBaseListaNegra()) {
        for (String grupoUsuario : listaGrupoGlobal) {
            if (patenteEscaneada.getListaGrupoTipo() != null) {
                for (GroupType patenteGrupoTipo : patenteEscaneada.getListaGrupoTipo()) {
                    if (patenteGrupoTipo.getTipo().equalsIgnoreCase("Grupo") &&
                            patenteGrupoTipo.getNombre().equalsIgnoreCase(grupoUsuario)) {
                        return true;
                    }
                }
            }
        }
        //}
        return false;
    }

    /**
     * Metodo que se encarga de verificar los grupos de cada usuario
     * @param patenteRobadaVista Datos de la patente
     */
    public boolean verificaTipoGrupoUsuario(PatenteRobadaVista patenteRobadaVista) {
        List<GroupType> gruposPatente = patenteRobadaVista.getListaGrupoTipo();
        double latitudInicial = Double.parseDouble(patenteRobadaVista.getLatitud());
        double longitudInicial = Double.parseDouble(patenteRobadaVista.getLongitud());
        boolean verificacionDistancia = isDistanciaMenor(latitudInicial, longitudInicial, latitud, longitud, radioDeInteresAlertaPatenteMetros);
        if(listaGrupoTipoGlobal == null){
            Log.v("lala","verificaTipoGrupoUsuario = null");
            return false;
        }
        // Recorro todos los grupos del usuario
        for (Map<String, String> grupoTipo : listaGrupoTipoGlobal) {
            // Pregunto si el usuario tiene un grupo de tipo comuna
            if (grupoTipo.get("tipo").equalsIgnoreCase("Comuna") || grupoTipo.get("tipo").equalsIgnoreCase("Seguridad pública")) {
                // Pregunto 2 cosas
                // 1: Si la patente esta dentro de la comuna del grupo del usuario
                // o
                // 2: La patente se encuentra dentro del radio de interes
                Log.v("lala1", "usuario tiene grupo tipo comuna");
                Log.v("lala2", "verificarSiGrupoPatentePerteneceGrupoUsuario " + verificarSiGrupoPatentePerteneceGrupoUsuarioComuna(gruposPatente));
                Log.v("lala2", "verificacionDistancia " + verificacionDistancia);
                Log.v("lala2", "verificarSiComunaPatenteEsIgualAComunaUsuario " + verificarSiComunaPatenteEsIgualAComunaUsuario(patenteRobadaVista.getCiudad()));
                if (verificarSiGrupoPatentePerteneceGrupoUsuarioComuna(gruposPatente) ||
                        verificacionDistancia ||
                        verificarSiComunaPatenteEsIgualAComunaUsuario(patenteRobadaVista.getCiudad())) {
                    Log.v("lala1", "patente vista en comuna y/o dentro del radio de interes");
                    return true;
                }
            }
            // Pregunto si el usuario tiene un grupo de tipo privado (Ej: tipo COPEC, MALL PLAZA)
            else if (grupoTipo.get("tipo").equalsIgnoreCase("Privado")) {
                Log.v("lala1", "usuario tiene grupo tipo Privado");
                if (verificarSiGrupoPatentePerteneceGrupoUsuario(gruposPatente)) {
                    Log.v("lala1", "usuario tiene grupo de la patente " + emailUsuarioFirebase);
                    if (verificacionDistancia) {
                        Log.v("lala1", "patente vista  dentro del radio de interes");
                        return true;
                    }
                } else {
                    Log.v("lala1", "usuario NO tiene grupo de la patente " + emailUsuarioFirebase);
                    if (verificacionDistancia) {
                        Log.v("lala1", "patente vista  dentro del radio de interes");
                        return true;
                    }
                }
            } else if (grupoTipo.get("tipo").equalsIgnoreCase("Seguridad privada")) {
                Log.v("lala1", "usuario tiene grupo tipo Seguridad privada");
                // osarch tiene el radio de interes x2
                if (verificacionDistancia) {
                    Log.v("lala1", "patente vista  dentro del radio de interes");
                    return true;
                }
            } else if (grupoTipo.get("tipo").equalsIgnoreCase("Policía") || grupoTipo.get("tipo").equalsIgnoreCase("Soap")) {
                Log.v("lala1", "usuario tiene grupo Policía o soap o ");
                // osarch tiene el radio de interes x2
                if (verificacionDistancia || verificarSiComunaPatenteEsIgualAComunaUsuario(patenteRobadaVista.getComuna())) {
                    Log.v("lala1", "patente vista  dentro del radio de interes o estoy dentro de la comuna");
                    return true;
                }
            }
        }

        //si usuario NO tiene grupos y si la patente es robada y yo NO fuí quien la escaneó hablar de todos modos
        if (!verificarSiUsuarioTieneGrupo() && verificacionDistancia) {
            // Compruebo que la distancia entre mi posición y de la patente esté dentro de la distnacia permitida
            Log.v("lala1", "usuario NO tiene grupos patente vista robada dentro del radio de interes");
            return true;
        }

        return false;
    }

    //si NO es robada y si es lista negra pero no pertenece a los grupos de usuario retorna
    public boolean verificaSiPatentePerteneceAGrupo(PatenteRobadaVista patenteRobadaVista){
        for (String grupoUsuario : listaGrupoGlobal) {
            if (patenteRobadaVista.getListaGrupoTipo() != null) {
                for (GroupType patenteGrupoTipo : patenteRobadaVista.getListaGrupoTipo()) {
                    Log.v("patenteRobadaVista", "grupos patente patenteGrupoTipo.getNombre(): " +patenteGrupoTipo.getNombre()+ " patenteGrupoTipo.getTipo(): "+patenteGrupoTipo.getTipo());

                    if (patenteGrupoTipo.getTipo().equalsIgnoreCase("Grupo") &&
                            patenteGrupoTipo.getNombre().equalsIgnoreCase(grupoUsuario)) {
                        return true;
                    }
                    //soporte para soap
                    else if((patenteRobadaVista.isBaseSoap() && isUserSoap) && patenteGrupoTipo.getTipo().equalsIgnoreCase("Usuario") &&
                            patenteGrupoTipo.getNombre().equalsIgnoreCase(grupoUsuario)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void muestraLinearLayoutMenuLateralDialogConBitmapRectF(String patente, BitmapRectF bitmapRectF, boolean isBitmapRoatated){
        if(autoPatenteEnPantalla == null || !(autoPatenteEnPantalla.getPatente().equalsIgnoreCase(patente))) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    creaMenuLateralAutoPatente();
                    muestraMenuLateralAutoPatenteAutomaticamente();
                }
            });
            Log.v("autoPatenteP","se crea");

            autoPatenteEnPantalla = new AutoPatenteEnPantalla(patente);
            autoPatenteEnPantallaArrayList.add(autoPatenteEnPantalla);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageViewAuto.setImageBitmap(null);
                }
            });
            Log.v("prroaa","size: "+autoPatenteEnPantallaArrayList.size());
        } else {
            Log.v("autoPatenteP","no es nulo?");
        }

        //si bitmapRectF es != null se muestra bitmap en menu lateral
        if(bitmapRectF != null){
            Log.v("autoPatentePs","set auto en autoPatenteEnPantalla: "+bitmapRectF.getTitle());
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(bitmapRectF.getTitle().equalsIgnoreCase("patente") && autoPatenteEnPantalla!= null && !autoPatenteEnPantalla.isPatente()) {
                        if(autoPatenteEnPantalla == null){
                            return;
                        }
                        autoPatenteEnPantalla.setPatente(true);
                        Bitmap bitmap = bitmapRectF.getCropBitmap().copy(bitmapRectF.getCropBitmap().getConfig(),true);
                        if(!isBitmapRoatated){
                            bitmap = rotateBitmap(bitmap,bitmapRectF.getSensorOrientation());
                        }
                        autoPatenteEnPantalla.setBitmapPatente(bitmap);
                        autoPatenteEnPantallaArrayList.get(autoPatenteEnPantallaArrayList.size()-1).setBitmapPatente(bitmap);
                        Log.v("autoPatenteP","set patente en imageViewPatente: "+autoPatenteEnPantalla.isPatente());
                        isOpenDialogPatenteAuto = true;
                        Bitmap finalBitmap = bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linearLayoutAutoDialog.setVisibility(View.VISIBLE);
                                imageViewAuto.setImageBitmap(finalBitmap);

                            }
                        });
                    }
                    if(bitmapRectF.getTitle().equalsIgnoreCase("auto") && autoPatenteEnPantalla!= null && !autoPatenteEnPantalla.isAuto()){
                        if(autoPatenteEnPantalla == null){
                            return;
                        }
                        autoPatenteEnPantalla.setAuto(true);
                        Bitmap bitmap = bitmapRectF.getCropBitmap().copy(bitmapRectF.getCropBitmap().getConfig(),true);
                        if(!isBitmapRoatated){
                            bitmap = rotateBitmap(bitmap,bitmapRectF.getSensorOrientation());
                        }
                        autoPatenteEnPantalla.setBitmapAuto(bitmap);
                        autoPatenteEnPantallaArrayList.get(autoPatenteEnPantallaArrayList.size()-1).setBitmapAuto(bitmap);

                        Log.v("autoPatenteP","set auto en autoPatenteEnPantalla: "+autoPatenteEnPantalla.isAuto());
                        isOpenDialogPatenteAuto = true;
                        Bitmap finalBitmap = bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linearLayoutAutoDialog.setVisibility(View.VISIBLE);
                                imageViewAuto.setImageBitmap(finalBitmap);
                            }
                        });
                    }
                }
            });
        }
    }

    public void muestraLinearLayoutMenuLateralDialogConPatenteRobadaVista(PatenteRobadaVista patenteRobadaVista){
        if((autoPatenteEnPantalla == null || !(autoPatenteEnPantalla.getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente()))) && patenteRobadaVista != null && patenteRobadaVista.getUrlImagenAmpliada()!= null && !patenteRobadaVista.getUrlImagenAmpliada().equalsIgnoreCase("")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    creaMenuLateralAutoPatente();
                    if(patenteRobadaVista.getUrlImagenAmpliada() != null || patenteRobadaVista.getUrlImagen() != null){
                        muestraMenuLateralAutoPatenteAutomaticamente();
                    }

                }
            });
            Log.v("autoPatenteP","se crea");

            autoPatenteEnPantalla = new AutoPatenteEnPantalla(patenteRobadaVista.getPatente());
            autoPatenteEnPantallaArrayList.add(autoPatenteEnPantalla);
            Log.v("prroaa","size: "+autoPatenteEnPantallaArrayList.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageViewAuto.setImageBitmap(null);
                }
            });
        } else {
            Log.v("autoPatenteP","no es nulo?");
        }

        //elimina menu lateral cuando una patente es de base robada y se valdia como sin encargo
        if((patenteRobadaVista.isBaseRobada()
                || (patenteRobadaVista.isBaseListaNegra() && !verificaSiPatentePerteneceAGrupo(patenteRobadaVista))
                || (patenteRobadaVista.isBaseSoap() && !isUserSoap))
                && patenteRobadaVista.getRobada() != null
                && patenteRobadaVista.getRobada().equalsIgnoreCase("false")){
            remueveMenuLateralAutoPatente(patenteRobadaVista);
            return;
        }

        //si patenteRobadaVista es != null se muestra bitmap en menu lateral desde la url con glide
        if(patenteRobadaVista != null ){
            if(patenteRobadaVista.getUrlImagenAmpliada() != null
                    //&& !patenteRobadaVista.getUrlImagenAmpliada().equalsIgnoreCase("")
                    //&& patenteRobadaVista.getTitleImagenAmpliada() != null && patenteRobadaVista.getTitleImagenAmpliada().equalsIgnoreCase("patente")
                    //&& autoPatenteEnPantalla!= null && !autoPatenteEnPantalla.isPatente() && !patenteRobadaVista.getUrlImagenAmpliada().equalsIgnoreCase("")
            ) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(autoPatenteEnPantalla == null){
                            return;
                        }
                        autoPatenteEnPantalla.setPatente(true);
                        isOpenDialogPatenteAuto = true;

                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(DetectorActivity.this);
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.setColorFilter(ContextCompat.getColor(DetectorActivity.this, R.color.white), PorterDuff.Mode.SRC_IN );
                        circularProgressDrawable.start();

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(patenteRobadaVista.getUrlImagenAmpliada())
                                .placeholder(circularProgressDrawable)
                                .fitCenter()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                        imageViewAuto.setImageBitmap(bitmap);
                                        autoPatenteEnPantalla.setBitmapPatente(bitmap.copy(bitmap.getConfig(),false));
                                        autoPatenteEnPantallaArrayList.get(autoPatenteEnPantallaArrayList.size()-1).setBitmapPatente(bitmap.copy(bitmap.getConfig(),false));
                                    }
                                });


                        //imageViewPatente.setImageBitmap(rotatedBitmap);
                        linearLayoutAutoDialog.setVisibility(View.VISIBLE);

                    }
                });
            }
            else if(patenteRobadaVista.getUrlImagen() != null
                    //&& patenteRobadaVista.getTitleImagenAmpliada() != null && patenteRobadaVista.getTitleImagenAmpliada().equalsIgnoreCase("auto")
                    //&& autoPatenteEnPantalla != null && !autoPatenteEnPantalla.isAuto() && !patenteRobadaVista.getUrlImagen().equalsIgnoreCase("")
            ){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(autoPatenteEnPantalla == null){
                            return;
                        }
                        autoPatenteEnPantalla.setAuto(true);

                        isOpenDialogPatenteAuto = true;
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(DetectorActivity.this);
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.setColorFilter(ContextCompat.getColor(DetectorActivity.this, R.color.white), PorterDuff.Mode.SRC_IN );
                        circularProgressDrawable.start();

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(patenteRobadaVista.getUrlImagen())
                                .placeholder(circularProgressDrawable)
                                .fitCenter()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                        imageViewAuto.setImageBitmap(bitmap);
                                        autoPatenteEnPantalla.setBitmapAuto(bitmap.copy(bitmap.getConfig(),false));
                                        autoPatenteEnPantallaArrayList.get(autoPatenteEnPantallaArrayList.size()-1).setBitmapAuto(bitmap.copy(bitmap.getConfig(),false));
                                    }
                                });

                        //imageViewAuto.setImageBitmap(rotatedBitmap);
                        linearLayoutAutoDialog.setVisibility(View.VISIBLE);

                    }
                });
            }
        }
    }


    public void escondeLinearLayoutContenedorDePatentes(PatenteRobadaVista prv){
        if(prvIsIgualAImagen(prv) && ((prv.isBaseListaNegra() && verificaSiPatentePerteneceAGrupo(prv))  || (prv.isBaseSoap() && isUserSoap))){
            return;
        }
        if(autoPatenteEnPantallaArrayList.size() == 0){
            linearLayoutPatente.setVisibility(View.GONE);
            linearLayoutPatenteEscaneadaVista.setVisibility(View.GONE);
        } else if (autoPatenteEnPantallaArrayList.size() > 0){
            textViewPatenteEscaneada.setText(autoPatenteEnPantalla.getPatente());
        }

        //elimina el linearLayoutPatenteRed cartel que muestra una patente robada
        /*
        if (textViewPatenteRobadaVista != null) {
            if (textViewPatenteRobadaVista.getText() != null) {
                if (textViewPatenteRobadaVista.getText().toString().length() > 0) {
                    if (textViewPatenteRobadaVista.getText().toString().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                        if (linearLayoutPatenteRed != null) {
                            linearLayoutPatenteRed.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

         */
    }

    /**
     * muestra el linear layout contenedor de patentes
     */
    public void muestraLinearLayoutContenedorDePatentesSuperior(PatenteRobadaVista patenteRobadaVista){
        boolean encargo = false;

        // muestra contenedor de patentes robado (rojo) o normal (negro)
        if(patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("true")){
            encargo = true;
        } else if(patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("false")){
            encargo = false;

            //eliminar cartel cuando la patente sea de base robada y sin encargo
            if(patenteRobadaVista.isBaseRobada()
                    || (patenteRobadaVista.isBaseListaNegra() && !verificaSiPatentePerteneceAGrupo(patenteRobadaVista))
                    || (patenteRobadaVista.isBaseSoap() && !isUserSoap)){
                linearLayoutPatente.setVisibility(View.INVISIBLE);
                linearLayoutPatenteEscaneadaVista.setVisibility(View.INVISIBLE);
                return;
            }
        }

        linearLayoutPatente.setVisibility(View.VISIBLE);
        linearLayoutPatenteEscaneadaVista.setVisibility(View.VISIBLE);
        if(encargo){
            Log.v("ENCUESTAPATRULLERO","ACA SE MARCA CON ENCARGO?");
            // marcarVehiculoConEncargo(patenteRobadaVista);
            textViewTituloPatenteEscaneada.setTextColor(getResources().getColor(R.color.colorWarning));
            textViewTituloPatenteEscaneada.setText("Patente con encargo");
            linearLayoutPatenteEscaneadaVista.setBackground(getResources().getDrawable(R.drawable.linear_layout_border_red_patente));
            linearLayoutPuntoPatente.setBackground(getResources().getDrawable(R.drawable.linear_layout_circular_red));
        } else {
            textViewTituloPatenteEscaneada.setTextColor(getResources().getColor(R.color.black));
            textViewTituloPatenteEscaneada.setText("Patente alertada");
            linearLayoutPatenteEscaneadaVista.setBackground(getResources().getDrawable(R.drawable.linear_layout_border_black_patente));
            linearLayoutPuntoPatente.setBackground(getResources().getDrawable(R.drawable.linear_layout_circular_green));
        }

        //muestra punto verde por un segundo
        timerTaskPuntoVerdePatente();
        //cambia el texto en textViewPatenteEscaneada
        textViewPatenteEscaneada.setText(patenteRobadaVista.getPatente());
    }

    //1) caso 1 posibles patentes robadas vistas pre alerta
    public void posiblePatenteRobadaVista(PatenteRobadaVista patenteRobadaVista, BitmapRectF bitmapRectF) {
        //si la patente no tiene el mismo id del usuario quien escanea retorna
        if (!emailUsuarioFirebase.equalsIgnoreCase(patenteRobadaVista.getEmailUsuario())) {
            Log.v("patenteRobadaVista", "caso 1 retorna porque patente.getIdUsuario != idUsuarioFirebase CadaVezQueHayaUnCambio: " + idUsuarioFirebase +"!="+patenteRobadaVista.getIdUsuario());
            return;
        }

        muestraLinearLayoutContenedorDePatentesSuperior(patenteRobadaVista);
        muestraLinearLayoutMenuLateralDialogConPatenteRobadaVista(patenteRobadaVista);
        muestraLinearLayoutMenuLateralDialogConBitmapRectF(patenteRobadaVista.getPatente(), bitmapRectF, false);
        getImagenPatente(patenteRobadaVista);
        brilloDePantallaNormal();

        //si existe el id en array posiblesPatentesRobadasVistasObservables retorno porque quiere decir que ya la dijo anteriormente
        //sirve para evitar que cada vez que haya un cambio en la base de datos vuelva a hablar
        if (existPatenteRobadaVistaById(patenteRobadaVista, posiblesPatentesRobadasVistasObservables)) {
            Log.v("patenteRobadaVista", "retorna porque existe en posiblesPatentesRobadasVistasObservables id:" + patenteRobadaVista.getId());
            return;
        }

        creaMarcadorEnMapa(patenteRobadaVista);
        /*
         * Para poder hablar patente "Se ha visto un posible auto con irregularidad placa patente..." debe cumplir lo siguiente
         * 1) campo verificado en false
         * 2) patente no debe estar en array posiblesPatentesRobadasVistasObservables, si patente se encuentra esperar 10 minutos
         * */

        //verifico si patente tiene su campo verificado en "false"
        boolean isVerificada = patenteRobadaVista.getVerificada().equalsIgnoreCase("false");

        //verifico si la patente que llega se ha dicho antes de X minutos en posiblesPatentesRobadasVistasObservables para no volver a decir que se ha visto una "posible patente robdada"
        //boolean siTieneUnTiempoMayorAXMinutosSeVaADecir = siTieneUnTiempoMayorAXMinutosSeVaADecir(patenteRobadaVista, 10, posiblesPatentesRobadasVistasObservables);

        //se va a decir cuando no existe en posiblesPatentesRobadasVistasObservables
        if(patenteValidaParaHablar(patenteRobadaVista)){
            String text = preAlerta(patenteSeparadaPorPuntosSuspencivos(patenteRobadaVista), patenteRobadaVista);
            Log.v("patenteRobadaVista", "preAlerta: "+text);
            textToSpeech(text, true);
        }

        //en posiblesPatentesRobadasVistasObservables almaceno todas las patentes que me llegan para no repetirlas
        insertIdPatenteRobadaVistaInArray(patenteRobadaVista, posiblesPatentesRobadasVistasObservables);
        printArray(posiblesPatentesRobadasVistasObservables, "posiblesPatentesRobadasVistasObservables");
    }

    //2) caso 2 patentes con o sin encargo
    public void patenteRobadaVistaConOSinEncargo(PatenteRobadaVista patenteRobadaVista) {
        Log.v("patenteRobadaVista", "patenteRobadaVistaConOSinEncargo - entra a ConSinEncargo id: " + patenteRobadaVista.getId());
        // muestra contenedor de patentes robado (rojo) o normal (negro)
        muestraLinearLayoutContenedorDePatentesSuperior(patenteRobadaVista);
        muestraLinearLayoutMenuLateralDialogConPatenteRobadaVista(patenteRobadaVista);
        getImagenPatente(patenteRobadaVista);

        //se va a decir cuando el estado sea distinto "robada", "igualAImagen", "delete", "cambiada"
        if(patenteValidaParaHablar(patenteRobadaVista)){
            String text = "";
            if(existPatenteRobadaVistaByPatente(patenteRobadaVista, posiblesPatentesRobadasVistasObservables)){
                text = posAlerta(patenteRobadaVista);
                Log.v("patenteRobadaVista", "patenteRobadaVistaConOSinEncargo - posAlerta NoigualAImagen con/sin encargo: "+text);
            } else {
                text = alerta(patenteRobadaVista);
                Log.v("patenteRobadaVista", "patenteRobadaVistaConOSinEncargo - alerta con encargo: "+text);
            }

            textToSpeech(text, true);
            ocualtaMenuLateralAutoPatenteEnXSegundos();

            // Al usuario que leyó la patente no se le mueve el mapa automaticamente
            if (!emailUsuarioFirebase.equalsIgnoreCase(patenteRobadaVista.getIdUsuario())) {
                Log.i("lala", "moviendo camara");
                centrarCamera = false;
                isUserMove = true;
                isDevelopMove = false;
                existPatenteRobadaEnMapa = true;
                escondePreviewCamara();
                centrarCamaraAPatente(Double.parseDouble(patenteRobadaVista.getLatitud()),
                        Double.parseDouble(patenteRobadaVista.getLongitud()));
            }

            insertIdPatenteRobadaVistaInArray(patenteRobadaVista, posiblesPatentesRobadasVistasObservables);
            printArray(posiblesPatentesRobadasVistasObservables, "patenteRobadaVista");
        }
        creaMarcadorEnMapa(patenteRobadaVista);
    }

    public boolean patenteValidaParaHablar(PatenteRobadaVista patenteRobadaVista){

        /*
        if(!patenteRobadaVista.isHeadSetRafaga()){
            Log.v("patenteRobadaVista","--> no es head set rafaga");
            return false;
        } else {
            Log.v("patenteRobadaVista","--> Es headSetRafaga");
        }

         */
        Log.v("patenteRobadaVista","--> patenteValidaParaHablar");

        if(!mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente())){
            Log.v("patenteRobadaVista","--> inserto key: "+patenteRobadaVista.getPatente());
            mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
            return true;
        } else {
            //si existe indice
            PatenteRobadaVista patenteRobadaVistaMapPatentesSpeech = mapPatentesSpeech.get(patenteRobadaVista.getPatente());
            if(patenteRobadaVistaMapPatentesSpeech.getRobada() == null && patenteRobadaVista.getRobada() != null){
                Log.v("patenteRobadaVista","--> igual robada una nula la otra no a null prvSpeech.getRobada(): "+patenteRobadaVistaMapPatentesSpeech.getRobada() +" patenteRobadaVista.getRobada(): "+patenteRobadaVista.getRobada());
                mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
                return true;
            }  else if (patenteRobadaVistaMapPatentesSpeech.getRobada() != null && patenteRobadaVista.getRobada() != null && !(patenteRobadaVistaMapPatentesSpeech.getRobada().equalsIgnoreCase(patenteRobadaVista.getRobada()))){
                Log.v("patenteRobadaVista","--> igual robada distinto a null y distinta robado patenteRobadaVista.getRobada(): "+patenteRobadaVista.getRobada()+" prvSpeech.getRobada(): "+patenteRobadaVistaMapPatentesSpeech.getRobada() );
                mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
                return true;
            } else if(patenteRobadaVistaMapPatentesSpeech.getIgualAImagen() == null && patenteRobadaVista.getIgualAImagen() != null ||
                    (patenteRobadaVistaMapPatentesSpeech.getIgualAImagen() != null && patenteRobadaVista.getIgualAImagen() != null && !(patenteRobadaVistaMapPatentesSpeech.getIgualAImagen().equalsIgnoreCase(patenteRobadaVista.getIgualAImagen())))
            ){
                mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
                Log.v("patenteRobadaVista","--> igual a imagen");
                return true;
            }
            /*else if(
                //si se elimina antes de validar

                    patenteRobadaVistaDelMap.getIgualAImagen() == null && patenteRobadaVistaDelMap.isDelete() != patenteRobadaVista.isDelete()
                            //o se elimina y el campo igual a imagen es true
                            || patenteRobadaVistaDelMap.getIgualAImagen() != null && patenteRobadaVistaDelMap.getIgualAImagen().equalsIgnoreCase("true") && patenteRobadaVistaDelMap.isDelete() != patenteRobadaVista.isDelete()){
                Log.v("patenteRobadaVista","--> igual delete");
                mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
                return true;


            }*/ else if(patenteRobadaVistaMapPatentesSpeech.getCambiada() == null && patenteRobadaVista.getCambiada() != null ||
                    (patenteRobadaVistaMapPatentesSpeech.getCambiada() != null && patenteRobadaVista.getCambiada() != null && !(patenteRobadaVistaMapPatentesSpeech.getCambiada().equalsIgnoreCase(patenteRobadaVista.getCambiada())))
            ){
                Log.v("patenteRobadaVista","--> igual cambiada");
                mapPatentesSpeech.put(patenteRobadaVista.getPatente(),patenteRobadaVista);
                return true;
            } else {
                Log.v("patenteRobadaVista","--> no cumple ninguna");
            }

        }
        Log.v("patenteRobadaVista","--> no cumple ninguna 2");
        return false;
    }

    // Retorna TRUE: si el usuario tiene grupo
    // Retorna FALSE: si el usuario no tiene
    private boolean verificarSiUsuarioTieneGrupo() {
        if (listaGrupoTipoGlobal.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean verificaTipoGrupoListaNegra(){
        return false;
    }

    //3) caso 3 patentes desestimadas pos alerta
    public void posiblePatenteRobadaVistaDesestimada(PatenteRobadaVista patenteRobadaVista) {
        Log.v("patenteRobadaVista", "entra a desestimadas id: " + patenteRobadaVista.getId());

        //limpiar marcadores de patentes y circulos
        if (googleMap != null) {
            limpiarMarkerPatente(patenteRobadaVista);
            limpiarMarkerCircle(patenteRobadaVista);
        }

        //menu lateral dialog se hace invisible
        if(autoPatenteEnPantalla != null && autoPatenteEnPantalla.getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
            //textViewPatenteEscaneada
            remueveMenuLateralAutoPatente(patenteRobadaVista);
        }

        //linear layout patentes se hace invisible
        if(textViewPatenteEscaneada != null && textViewPatenteEscaneada.getText().toString().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
            escondeLinearLayoutContenedorDePatentes(patenteRobadaVista);
        }

        Log.v("patenteRobadaVista", "Antes de patenteValidaParaHablar");

        //se va a decir cuando el estado sea distinto "robada", "igualAImagen", "delete", "cambiada"
        if(patenteValidaParaHablar(patenteRobadaVista)){
            String text = posAlerta(patenteRobadaVista);
            Log.v("patenteRobadaVista", "posAlerta desestimada: "+text);
            textToSpeech(text, true);
        }
        Log.v("patenteRobadaVista", "DESPUES de patenteValidaParaHablar");

        insertIdPatenteRobadaVistaInArray(patenteRobadaVista, posiblesPatentesRobadasVistasObservables);

    }

    public void escondePreviewCamara() {
        fullScreenCamera = !fullScreenCamera;
        LatLng latLng = new LatLng(lastKnownLatitud,lastKnownLongitud);
        centrarMapa(latLng);
        cemeraSmallSize();
    }

    //actualiza patentes robadas cada un minuto
    public void taskListarPatenteRobadaVista() {
        if(doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto == null){
            final Handler handler = new Handler();
            timerCadaUnMinuto = new Timer();
            doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if(numeroFrame >= 1000){
                                numeroFrame = 0;
                            }

                            //inicializo contadores de totales a las 00:00h
                            CurrentDate currentDate = new CurrentDate(new Date());

                            if(isUserTotem) {
                                //contador de lecturas
                                if (!(Utils.horaActualEsMayorAHoraLimite(currentDate.getHora(), configurationTotem.getHoraTermino())
                                        || Utils.horaActualEsMenorAHoraInicio(currentDate.getHora(), configurationTotem.getHoraInicio())) && countLecturas >= 10) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetectorActivity.this, "Totem sin lecturas en los ultimos: "+countLecturas+" minutos", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    if (countLecturas > tiempoMaximoSinLecturasTotemMinutos) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DetectorActivity.this, "Totem sin lecturas en los ultimos: "+countLecturas+" minutos, se reinicia", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ejecutarLogoInicialActivity();
                                        finish();
                                    }
                                    countLecturas++;
                                }

                                if(comunaUserFromDB.equalsIgnoreCase("Sin comuna")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetectorActivity.this, "Comuna no configurada", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                if(latitudUserFromDB == 0.0f || longitudUserFromDB == 0.0f){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetectorActivity.this, "Ubicación no configurada", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                listarPatenteRobadaVistaTaskEjecutaCadaXTiempo();
                            }

                            Calendar c = Calendar.getInstance();
                            c.setTime(currentDate.getDate());
                            String hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

                            if (hora.equalsIgnoreCase("0:0")) {
                                inicializarContadorDeTotales();
                            }

                            if (hora.equalsIgnoreCase("0:1")) {
                                Utils.ejecutarLoginActivity(DetectorActivity.this);
                            }

                            //actualizar tiempo de sesion
                            Log.v("expiresesion",expiredSessionUsuarioFirebase+"");

                            //cuando es menor a un minuto vuelve a actualizar el campo expiredSession
                            if(expiredSessionUsuarioFirebase - currentDate.getDate().getTime() < 60000){
                                Timestamp timestamp = Utils.updateExpiredSession(db);
                                Utils.guardarValorString(DetectorActivity.this, Referencias.EXPIREDSESSION, String.valueOf(timestamp));
                                expiredSessionUsuarioFirebase = timestamp.toDate().getTime();
                            }

                            //modo ahoro de energia
                            if(isButtonAhorroDeEnergiaActivado && !isModoAhorroDeEnergiaActivado) {
                                if (new CurrentDate(new Date()).getDate().getTime() - Long.parseLong(horaTouchListener.getLongTime()) >=
                                        esperaModoAhorroMilis) {
                                    disminuirBrillo();
                                }
                            }

                            if(!isUserTotem) {
                                if(hora.equalsIgnoreCase("3:0")){
                                    verificarVersionApp();
                                }

                                currentDate = new CurrentDate(new Date());
                                if (locationAvaible == false && currentDate.getDate().getTime() - lastLocationAvaibleTime > 60000 * 5 && !fromBackground) {
                                    destruyetaskSendUbicacionUsuarioUsuario();
                                    //favor enciende el GPS
                                    enciendeGPSDialog();
                                }
                            }
                        }
                    });
                }
            };
            timerCadaUnMinuto.schedule(doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto, 10000, 60000);
        }
    }

    public void dialogInicial() {
        AlertDialog alertDialog =  new AlertDialog.Builder(DetectorActivity.this)
                .setView(R.layout.dialog_alert_view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public void enciendeGPSDialog() {
        if(alertDialogEnciendeGPS != null){
            return;
        }
        alertDialogEnciendeGPS = new AlertDialog.Builder(DetectorActivity.this)
                .setTitle("Favor activar el GPS")
                .setCancelable(false)
                .setMessage("Hacemos uso del GPS para saber con exactitud dónde fue visto el vehículo.")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        mGoogleApiClient=null;
                        alertDialogEnciendeGPS = null;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sinGPSExterminarAPPDialog();
                    }
                })
                .show();
    }

    public void sinGPSExterminarAPPDialog() {
        new AlertDialog.Builder(DetectorActivity.this)
                .setTitle("Favor activar el GPS")
                .setCancelable(false)
                .setMessage("Sin el GPS activo la aplicación no puede continuar, se va a cerrar.")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        finish();
                    }
                })
                .show();
    }

    public void destruyetaskSendUbicacionUsuarioUsuario(){
        if (doAsynchronousTaskUbicacion != null) {
            doAsynchronousTaskUbicacion.cancel();
            doAsynchronousTaskUbicacion = null;
        }
    }

    public void listarPatenteRobadaVistaTaskEjecutaCadaXTiempo() {
        ArrayList<PatenteRobadaVista> patentesRobadasVistasReverse = new ArrayList<>(posiblesPatentesRobadasVistasObservables);
        Collections.reverse(patentesRobadasVistasReverse);

        for (PatenteRobadaVista patenteRobadaVista : patentesRobadasVistasReverse) {
            if (!verificacionesComunesPatenteRobadaVista(patenteRobadaVista)) {
                Log.v("patenteRobadaVista", "retorna por no cumplir verificacionesComunesPatenteRobadaVista");
                //limpio todos los marker option de esa patente en particular
                limpiarMarkerPatente(patenteRobadaVista);

                //limpio todos los marker circle de esa patente en particular
                limpiarMarkerCircle(patenteRobadaVista);
                continue;
            }
            Log.v("patenteRobadaVista", patentesRobadasVistasReverse.size() + "");
            creaMarcadorEnMapa(patenteRobadaVista);
        }

        Log.v("patentesCadaXTiempo", "offset: " + TimeOffset.getOffset());
        Log.v("patentesCadaXTiempo", "new Date(): " + new Date().getTime());
        printArray(posiblesPatentesRobadasVistasObservables, "patentesCadaXTiempo");

        //habla en acerca de las patentes vistas
        if (patentesRobadasVistas.size() == 0) {
            if (contadorDeMinutosTaskPatentes >= 10) {
                //textToSpeech("No se han reportado patentes robadas en los últimos 10 minutos");
                contadorDeMinutosTaskPatentes = 0;
            }
        } else {
            contadorDeMinutosTaskPatentes = 0;
        }
        contadorDeMinutosTaskPatentes++;
        isPatentesRobadasVistasInicializadas = true;
    }


    public boolean verificacionesComunesPatenteRobadaVista(PatenteRobadaVista patenteRobadaVista) {

        //verifica patente es nula
        if (patenteRobadaVista == null) {
            Log.v("patenteRobadaVista", "retorna false porque patente robada vista es nula");
            return false;
        }

        //verifica patente vacia
        if (patenteRobadaVista.getPatente().equalsIgnoreCase("")) {
            Log.v("patenteRobadaVista", "retorna porque patente es nula");
            return false;
        }

        //cuando no se debe alertar y (no es lista negra o (es lista negra y no pertenece a mi grupo))
        if(!patenteRobadaVista.isAlertar() && (!patenteRobadaVista.isBaseListaNegra() || (patenteRobadaVista.isBaseListaNegra() && !verificaSiPatentePerteneceAGrupo(patenteRobadaVista)))){
            Log.v("patenteRobadaVista", "retorna patente alertar = false");
            return false;
        }

        //verifica latitud y longitud
        if (patenteRobadaVista.getLongitud() == null || patenteRobadaVista.getLatitud() == null || patenteRobadaVista.getLongitud().isEmpty() || patenteRobadaVista.getLatitud().isEmpty()
                || patenteRobadaVista.getLongitud().equalsIgnoreCase("0.0")
                || patenteRobadaVista.getLatitud().equalsIgnoreCase("0.0")) {
            Log.v("patenteRobadaVista", "retorna false por latitud o longitud nula");
            return false;
        }

        //verifica el tiempo actual
        CurrentDate currentDate = new CurrentDate(new Date());
        if (patenteRobadaVista.getLongTime() != null && Long.parseLong(patenteRobadaVista.getLongTime())-1000 > Long.parseLong(currentDate.getLongTime())) {
            Log.v("patenteRobadaVista", "time patente:" + patenteRobadaVista.getLongTime());
            Log.v("patenteRobadaVista", "time currenDate:" + currentDate.getLongTime());
            Log.v("patenteRobadaVista", "retorna porque la hora del celular no es correcta");
            return false;
        }

        //la patente se tuvo que haber leido en los ultimos 10 minutos
        if(patenteRobadaVista.getLongTime() != null && Long.parseLong(patenteRobadaVista.getLongTime()) < currentDate.getDateMenosXMinutos().getTime()){
            Log.v("patenteRobadaVista", "patente: "+patenteRobadaVista.getPatente()+" retorna porque han pasado mas de 10 minutos desde que se leyó patenteRobadaVista.getLongTime(): "+patenteRobadaVista.getLongTime()+" currentDate.getDateMenosXMinutos().getTime(): "+currentDate.getDateMenosXMinutos().getTime()+" resta: "+(Long.parseLong(patenteRobadaVista.getLongTime()) < currentDate.getDateMenosXMinutos().getTime()));
            return false;
        }

        //solo base robada pero se marca como SIN encargo y no la leí (pos alerta)
        if (patenteRobadaVista.isBaseRobada()
                && !patenteRobadaVista.isBaseListaNegra()
                && !patenteRobadaVista.isBaseSoap()
                && patenteRobadaVista.getRobada() != null
                && patenteRobadaVista.getRobada().equalsIgnoreCase("false")
                && !patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)
                && !mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente())) {
            Log.v("patenteRobadaVista", "retorna porque patente viene de una baseRobada pero se marca como SIN encargo y NO fui quien la escaneó y no se ha hablado: "+patenteRobadaVista.getPatente());
            return false;
        }

        //solo base lista negra se marca patente como SIN encargo y NO es de mi grupo y NO la leí (pos alerta)
        if (patenteRobadaVista.isBaseListaNegra()
                && !patenteRobadaVista.isBaseSoap()
                && !patenteRobadaVista.isBaseRobada()
                && patenteRobadaVista.getRobada() != null
                && patenteRobadaVista.getRobada().equalsIgnoreCase("false")
                && !verificaSiPatentePerteneceAGrupo(patenteRobadaVista)
                && !patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)
                && !mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente())) {
            Log.v("patenteRobadaVista", "retorna porque patente viene de una baseListaNegra pero NO está robada y no pertenece a mis grupos y no fui quien la escaneo y no se ha hablado");
            return false;
        }

        //solo base SOAP y no soy policia o no soy usuario soap (pre y pos alerta)
        if(patenteRobadaVista.isBaseSoap()
                && !isUserSoap
                && !patenteRobadaVista.isBaseListaNegra()
                && !patenteRobadaVista.isBaseRobada()){
            Log.v("patenteRobadaVista", "retorno porque patente pertenece a una base SOAP y NO soy policia o SOAP  ");
            return false;
        }

        //verifica si cumple con las condiciones de tipo de cada grupo comuna, privado, policia, etc
        if(verificaTipoGrupoUsuario(patenteRobadaVista)){
            Log.v("patenteRobadaVista", "Cumple con verificaSiTipoGrupoUsuarioPerteneceATipoGrupoPatente");

            //verifica si es cambiada
            if(mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente()) && patenteRobadaVista.getCambiada().equalsIgnoreCase("true")){
                Log.v("patenteRobadaVista", "Cumple con verificaSiPatenteSeHabloYEsCambiada");
                return true;
            }

            //verifica si es eliminada
            /*
            if(!mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente()) && patenteRobadaVista.isDelete()){
                Log.v("patenteRobadaVista", "Cumple con verificaSiPatenteNOSeHabloYEsEliminada");
                return true;
            }
             */

            //verifica si patente es robada idependienteme de la base o quien la leyó
            if(patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("true")){
                Log.v("patenteRobadaVista", "Cumple con verificaSiPatenteEsRobada");
                return true;
            }

            //verifica si patente viene de baseRobada y yo fui quien la escaneó hablar
            if(patenteRobadaVista.isBaseRobada() && patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)){
                Log.v("patenteRobadaVista", "Cumple con robada y yo la leí patenteRobadaVista.getEmailUsuario(): "+patenteRobadaVista.getEmailUsuario()+" emailUsuarioFirebase: "+emailUsuarioFirebase+ " patenteRobadaVista.isBaseRobada(): "+patenteRobadaVista.isBaseRobada());
                return true;
            }

            //solo base lista negra y yo la lei (pre alerta)
            if (patenteRobadaVista.isBaseListaNegra()
                    && !patenteRobadaVista.isBaseSoap()
                    && !patenteRobadaVista.isBaseRobada()
                    && patenteRobadaVista.getIgualAImagen() == null
                    && patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)) {
                Log.v("patenteRobadaVista", "Cumple porque patente viene de una baseListaNegra y robada igual null y no pertenece a mis grupos y pero fui quien la escaneo");
                return true;
            }

            //solo base lista negra cuando se valida como SIN Encargo (pos alerta)
            if (patenteRobadaVista.isBaseListaNegra()
                    && !patenteRobadaVista.isBaseSoap()
                    && !patenteRobadaVista.isBaseRobada()
                    && patenteRobadaVista.getRobada() != null
                    && patenteRobadaVista.getRobada().equalsIgnoreCase("false")
                    && patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)
                    && mapPatentesSpeech.containsKey(patenteRobadaVista.getPatente())) {
                Log.v("patenteRobadaVista", "Cumple porque patente viene de una baseListaNegra pero NO está robada y no pertenece a mis grupos y pero fui quien la escaneo");
                return true;
            }

            //usuario SOAP o policia y la patente es SOAP y aemas esta en mi grupo
            if(isUserSoap && (patenteRobadaVista.isBaseSoap() && (verificaSiPatentePerteneceAGrupo(patenteRobadaVista) || patenteRobadaVista.getEmailUsuario().equalsIgnoreCase(emailUsuarioFirebase)) )){
                Log.v("patenteRobadaVista", "Cumple con verificaUsuarioSoapYPatenteSoap");
                return true;
            }

            //verificaSiPatentePerteneceAGrupo
            if (verificaSiPatentePerteneceAGrupo(patenteRobadaVista)) {
                Log.v("patenteRobadaVista", "Cumple con verificaSiPatentePerteneceAGrupo");
                return true;
            }

        } else {
            Log.v("patenteRobadaVista", "No cumple con verificaTipoGrupoUsuario");
        }

        Log.v("patenteRobadaVista", "No cumple ninguna return false");
        return false;
    }

    public void insertPatenteRobadaVistaInArray(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> array) {
        int index = indexPatenteListaNegraInArray(array, patenteRobadaVista);
        if (index == -1) {
            array.add(patenteRobadaVista);
        } else {
            array.set(index, patenteRobadaVista);
        }
    }

    public void insertIdPatenteRobadaVistaInArray(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> array) {
        int index = existIdInPatenteRobadaVistaInArray(array, patenteRobadaVista);
        if (index == -1) {
            array.add(patenteRobadaVista);
        } else {
            array.set(index, patenteRobadaVista);
        }
    }

    public boolean creaMarcadorEnMapa(PatenteRobadaVista patenteRobadaVista) {

        CurrentDate currentDate = new CurrentDate(new Date());

        //buscar si la patente tiene un color sino agregar color
        int color = buscarColorPatente(patenteRobadaVista.getPatente());
        Log.v("patenteRobadaVista", "color: "+color + "");
        //añadir color
        patenteRobadaVista.setColorInt(color);

        if (googleMap != null) {
            //limpio todos los marker option de esa patente en particular
            limpiarMarkerPatente(patenteRobadaVista);

            //limpio todos los marker circle de esa patente en particular
            limpiarMarkerCircle(patenteRobadaVista);

            //elimina marcador en mapa cuando una patente es de base robada o base lista negra y se valdia como sin encargo
            if((patenteRobadaVista.isBaseRobada()
                    || (patenteRobadaVista.isBaseListaNegra() && !verificaSiPatentePerteneceAGrupo(patenteRobadaVista))
                    || (patenteRobadaVista.isBaseSoap() && !isUserSoap))
                    && patenteRobadaVista.getRobada() != null
                    && patenteRobadaVista.getRobada().equalsIgnoreCase("false")){
                removePatenteRobadaVistaByPatente(posiblesPatentesRobadasVistasObservables, patenteRobadaVista,"patenteRobadaVista");
                return false;
            }

            //se crea marcador de patente y se añade a la lista de markerPatentes
            if (patenteRobadaVista.getVisible()) {
                MarkerPatente markerPatente = new MarkerPatente(addMarkerPatenteRobadaVista(patenteRobadaVista, currentDate), patenteRobadaVista);
                markerPatentes.add(markerPatente);

                CirclePatente circlePatente = new CirclePatente(addCirclePatenteRobadaVista(patenteRobadaVista), patenteRobadaVista);
                circlePatentesArray.add(circlePatente);
                return true;
            }
        }
        return false;
    }

    public String patenteSeparadaPorPuntosSuspencivos(PatenteRobadaVista patenteRobadaVista) {
        String patenteSeparadaPorPuntosSuspencivos;
        //convierte patente con puntos para hablar lentamente
        patenteSeparadaPorPuntosSuspencivos = "";
        for (int x = 0; x < patenteRobadaVista.getPatente().length(); x++) {
            patenteSeparadaPorPuntosSuspencivos = patenteSeparadaPorPuntosSuspencivos + String.valueOf(patenteRobadaVista.getPatente().charAt(x)) + "...";
        }
        return patenteSeparadaPorPuntosSuspencivos;
    }

    public void getClickedImagenPatente(String patente) {
        Log.v("imagenessPat", patente);
        clickedImagenesPatente = new ArrayList<>();
        for (ImagenPatente imagenPatente : imagenesPatente) {
            if (imagenPatente.getPatente().equalsIgnoreCase(patente)) {
                Log.v("imagenessID", imagenPatente.getId());
                Log.v("imagenessURL", imagenPatente.getUrl() + " as");
                clickedImagenesPatente.add(imagenPatente);
            }
        }
        if (clickedImagenesPatente.size() > 0) {
            Intent intent = new Intent(DetectorActivity.this, GaleryActivity.class);
            intent.putExtra("imagenes", clickedImagenesPatente);
            startActivity(intent);
        }

    }

    public void menu(){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(DetectorActivity.this, LinearLayoutButtonDot);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_main, popup.getMenu());

        Intent intentTemp = getIntent();
        boolean prevIsCarab = Boolean.parseBoolean(intentTemp.getStringExtra("fromCarab"));
        if (prevIsCarab){
            popup.getMenu().findItem(R.id.cambiar_actividad).setVisible(true);
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.cambiar_actividad){
                    new AlertDialog.Builder(DetectorActivity.this)
                            .setTitle("Confirmación")
                            .setMessage("¿Seguro quieres volver al Menú?")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    return true;
                }
                if (id == R.id.exit) {
                    logout();
                    return true;
                }
                if (id == R.id.perfil) {
                    perfil();
                    return true;
                }
                if (id == R.id.terminosYCondiciones) {
                    terminosYCondiciones();
                    return true;
                }
                if (id == R.id.contacto) {
                    contacto();
                    return true;
                }
                if (id == R.id.about) {
                    about();
                    return true;
                }
                if (id == R.id.novedades) {
                    novedades();
                    return true;
                }
                if (id == R.id.noticias) {
                    noticias();
                    return true;
                }
                if (id == R.id.comunas) {
                    comunas();
                    return true;
                }
                if (id == R.id.tutorial) {
                    tutorial();
                    return true;
                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public void getImagenPatente(PatenteRobadaVista patenteRobadaVista) {
        if (patenteRobadaVista.getUrlImagen() != null && !patenteRobadaVista.getUrlImagen().equalsIgnoreCase("")) {
            //comparar id para no agregar imagen nuevamente
            if (!existIdInImagenesPatente(imagenesPatente, patenteRobadaVista.getId())) {
                //crear patente imagen
                ImagenPatente imagenPatente = new ImagenPatente(patenteRobadaVista.getId(),
                        patenteRobadaVista.getPatente(),
                        patenteRobadaVista.getUrlImagen(),
                        patenteRobadaVista.getFecha(),
                        patenteRobadaVista.getIdUsuario(),
                        patenteRobadaVista.getLatitud(),
                        patenteRobadaVista.getLongitud());
                imagenesPatente.add(imagenPatente);
            }else{
                Log.v("patenteRobadaVistaa", "existe imagen!");
            }

        }
    }

    public boolean isPatenteEqualsToImagenesPatente(ArrayList<ImagenPatente> imagenesPatente, String patente) {
        for (ImagenPatente imagenPatente : imagenesPatente) {
            if (imagenPatente.getPatente().equalsIgnoreCase(patente)) {
                return true;
            }
        }
        return false;
    }

    public boolean existIdInImagenesPatente(ArrayList<ImagenPatente> imagenesPatente, String id) {
        for (ImagenPatente imagenPatente : imagenesPatente) {
            if (imagenPatente.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public String marcaModeloColor(String tag, String text) {
        if (text != null && !text.equalsIgnoreCase("")) {
            if (tag.equalsIgnoreCase("marca")) {
                return ", marca, " + text;
            }
            if (tag.equalsIgnoreCase("modelo")) {
                return ", modelo, " + text;
            }
            if (tag.equalsIgnoreCase("color")) {
                return ", color, " + text;
            }
        }
        return "";
    }

    public String alerta(PatenteRobadaVista patenteRobadaVista){
        String text = "";
        String minutos = getMinutes(patenteRobadaVista);;

        String patente = Utils.patentePuntoSuspensivos(patenteRobadaVista.getPatente());
        boolean isPatenteDeInteres = false;
        //caso cuando es oficialmente una patente robada/listaNegra con/sin encargo
        if (patenteRobadaVista.getIgualAImagen() != null && patenteRobadaVista.getRobada() != null) {
            //si existe en array posiblesPatentesRobadasVistasObservables significa que yo fui quien vio esa patente
            //por tanto la asistente de google debe decir patente ... presenta encargo por robo

            if (!existPatenteRobadaVistaByPatente(patenteRobadaVista, posiblesPatentesRobadasVistasObservables)) {
                String distanciaYDireccion = "";
                String haceMinutos = "";
                String robado = "";
                String listaNegra = "";
                String soap = "";

                if (patenteRobadaVista.getVerificada().equalsIgnoreCase("true")) {

                    //Base Lista negra
                    if(patenteRobadaVista.isBaseListaNegra()){
                        boolean verificarSiGrupoUsuarioEsListaNegraPatente = verificarSiGrupoUsuarioEsListaNegraPatente(patenteRobadaVista);

                        //pertenece a mi grupo
                        if (verificarSiGrupoUsuarioEsListaNegraPatente) {
                            listaNegra = speechAlertaListaNegra ;
                            isPatenteDeInteres = true;
                        }
                    }

                    //Base SOAP
                    if(patenteRobadaVista.isBaseSoap() && isUserSoap) {
                        soap = speechAlertaSoap;
                        isPatenteDeInteres = true;
                    }

                    //CON o SIN encargo
                    if (patenteRobadaVista.getRobada().equalsIgnoreCase("true")) {
                        robado = speechAlertaConEncargo;
                        isPatenteDeInteres = true;
                    } else if(patenteRobadaVista.getRobada().equalsIgnoreCase("false")) {
                        robado = speechAlertaSinEncargo;
                    }

                    distanciaYDireccion = aUnaDistanciaDe(kmDeDistanciaDePatenteRobadaVistaHastaUsuario(patenteRobadaVista), patenteRobadaVista)
                            + enDireccion(patenteRobadaVista) + "...";

                }

                if (minutos.equalsIgnoreCase("0")) {
                    haceMinutos = "";
                } else if (minutos.equalsIgnoreCase("1")) {
                    haceMinutos = ", hace " + minutos + " minuto";
                } else {
                    haceMinutos = ", hace " + minutos + " minutos";
                }


                if(patenteRobadaVista.getMarca() == null){
                    patenteRobadaVista.setMarca("");
                }

                if(patenteRobadaVista.getModelo() == null){
                    patenteRobadaVista.setModelo("");
                }

                if(patenteRobadaVista.getColor() == null){
                    patenteRobadaVista.setColor("");
                }

                text = speechAlertaSeHaVistoVehiculo + " "
                        + listaNegra + " "
                        + soap + " "
                        + robado + " "
                        + marcaModeloColor("marca", patenteRobadaVista.getMarca().toLowerCase()) + " "
                        + marcaModeloColor("modelo", patenteRobadaVista.getModelo().toLowerCase()) + " "
                        + marcaModeloColor("color", patenteRobadaVista.getColor().toLowerCase()) + " "
                        + ". placa patente, " + " "
                        + patente + " "
                        + haceMinutos + " "
                        + distanciaYDireccion;
            }
        }

        if(!isPatenteDeInteres){
            text = "";
        }

        return text;
    }

    public String preAlerta(String patente, PatenteRobadaVista patenteRobadaVista){
        String text = "";

        if (patenteRobadaVista.getIgualAImagen() == null && patenteRobadaVista.getRobada() == null) {

            if(patenteRobadaVista.getMarca() == null){
                patenteRobadaVista.setMarca("");
            }

            if(patenteRobadaVista.getModelo() == null){
                patenteRobadaVista.setModelo("");
            }

            if(patenteRobadaVista.getColor() == null){
                patenteRobadaVista.setColor("");
            }

            text = speechPreAlertaSeHaVistoVehiculo + " "
                    + marcaModeloColor("marca", patenteRobadaVista.getMarca().toLowerCase(Locale.ROOT))
                    + marcaModeloColor("modelo", patenteRobadaVista.getModelo().toLowerCase(Locale.ROOT))
                    + marcaModeloColor("color", patenteRobadaVista.getColor().toLowerCase(Locale.ROOT))
                    + ". placa patente, " + " "
                    + patente + " "
                    + speechPreAlertaConPosibleIrregularidad + " "
                    + speechPreAlertaSeVaAValidar;
        }
        return text;
    }

    public String posAlerta(PatenteRobadaVista patenteRobadaVista) {
        String patente = patenteSeparadaPorPuntosSuspencivos(patenteRobadaVista);
        String text = "";
        if (existPatenteRobadaVistaByPatente(patenteRobadaVista, posiblesPatentesRobadasVistasObservables)) {

            //Se elimina patente
            if (patenteRobadaVista.isDelete()) {
                text = speechPosAlertaEliminada + " " + patente + " ";
                return text;
            }

            //NO igual a imagen
            else if ((patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("false"))
                    && (patenteRobadaVista.getIgualAImagen() != null && patenteRobadaVista.getIgualAImagen().equalsIgnoreCase("false"))
                    && !patenteRobadaVista.isDelete()) {
                text = speechPosAlertaNoIgualAImagen + " " + patente + " ";
                return text;
            }

            //Igual a imagen y es verificada
            else if( (patenteRobadaVista.getIgualAImagen() != null && patenteRobadaVista.getIgualAImagen().equalsIgnoreCase("true")) && ( patenteRobadaVista.getVerificada() != null && patenteRobadaVista.getVerificada().equalsIgnoreCase("true"))){

                //Se cambia estado de validacion
                if ((patenteRobadaVista.getCambiada() != null && patenteRobadaVista.getCambiada().equalsIgnoreCase("true")) && !patenteRobadaVista.isDelete()) {
                    text = speechPosAlertaCorreccionValidacion + " ";
                }

                text = text.concat(speechPosAlertaVehiculoPlacaPatente + " " + patente + " ");

                /**
                 * BASE Lista negra
                 * */
                if (patenteRobadaVista.isBaseListaNegra()) {
                    boolean verificarSiGrupoUsuarioEsListaNegraPatente = verificarSiGrupoUsuarioEsListaNegraPatente(patenteRobadaVista);

                    //pertenece a mi grupo
                    if (verificarSiGrupoUsuarioEsListaNegraPatente) {
                        text = text.concat(speechPosAlertaListaNegra + " ");
                    }
                }

                /**
                 * BASE SOAP
                 * */
                if (patenteRobadaVista.isBaseSoap() && isUserSoap) {
                    text = text.concat(speechPosAlertaSoap + " ");
                }

                /**
                 * CON o SIN Encargo
                 * */
                //1) Se marca patente como "CON encargo"
                if ((patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("true"))
                        && !patenteRobadaVista.isDelete()) {
                    text = text.concat(speechPosAlertaConEncargo + " ");
                    Log.v("ENCUESTAPATRULLERO", "SE MARCO CON ENCARGO EL VEHICULO");
                    if (encuestaAbierta) {
                        if (!isUserTotem && encuestaManager.getTieneGrupoComuna()) {
                            encuestaManager.marcarVehiculoConEncargo(DetectorActivity.this, patenteRobadaVista);
                        }
                    }

                }

                //2) Se marca patente como "SIN encargo"
                else if ((patenteRobadaVista.getRobada() != null && patenteRobadaVista.getRobada().equalsIgnoreCase("false"))
                        && !patenteRobadaVista.isDelete()) {
                    text = text.concat(speechPosAlertaSinEncargo + " ");
                }

            }
            text = text.concat(comunicarseConCentral() + " ");
            Log.v("patenteQL", patenteRobadaVista.getPatente()+" "+patenteRobadaVista.getId());
            Log.v("patenteQL", text);
        }
        return text;
    }

    private String comunicarseConCentral(){
        String result = " ";

        if (accountVersionFirebase.equals("full") && BuildConfig.FLAVOR.equalsIgnoreCase("full") && existeTipo(listaGrupoTipoGlobal,"Comuna")) {
            result = speechPosAlertaComunicateConTuCentral;
            return result;
        }
        return result;
    }

    /**
     * verifica si los grupos de la patente lista negra pertenece a los grupos del usuario
     * */
    private boolean verificarSiGrupoUsuarioEsListaNegraPatente(PatenteRobadaVista patenteRobadaVista){
        // Recorro los grupos del usuario
        for (String grupoUsuario : listaGrupoGlobal){
            // Recorro los grupos de la patente
            for (GroupType grupoTipo : patenteRobadaVista.getListaGrupoTipo()){
                // Hay un grupo de la patente que coincide con el del usuario y ademas ese grupo de la patente es lista negra
                if (grupoTipo.getTipo().equalsIgnoreCase("Grupo")  && grupoTipo.getNombre().equalsIgnoreCase(grupoUsuario) &&
                        grupoTipo.getTipoDeBase().equalsIgnoreCase("listaNegra")){
                    return true;
                }
            }
        }
        return false;
    }

    public int existPatenteInPatenteDistintasArray(ArrayList<String> patentesRobadasVistas, String patente) {
        for (int i = 0; i < patentesRobadasVistas.size(); i++) {
            if (patentesRobadasVistas.get(i).equalsIgnoreCase(patente)) {
                return i;
            }
        }
        return -1;
    }

    public int existPatenteInPatenteEscaneadaArray(ArrayList<PatenteEscaneada> patentesRobadasVistas, String patente) {
        for (int i = 0; i < patentesRobadasVistas.size(); i++) {
            if (patentesRobadasVistas.get(i).getPatente().equalsIgnoreCase(patente)) {
                return i;
            }
        }
        return -1;
    }

    public int indexPatenteListaNegraInArray(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista) {
        for (int i = 0; i < patentesRobadasVistas.size(); i++) {
            if (patentesRobadasVistas.get(i).getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                return i;
            }
        }
        return -1;
    }

    public int existIdInPatenteRobadaVistaInArray(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista) {
        for (int i = 0; i < patentesRobadasVistas.size(); i++) {
            if (patentesRobadasVistas.get(i).getId() != null && patenteRobadaVista.getId() != null && patentesRobadasVistas.get(i).getId().equalsIgnoreCase(patenteRobadaVista.getId())) {
                return i;
            }
        }
        return -1;
    }

    public int existPatenteInPatenteRobadaArray(ArrayList<PatenteListaNegra> patentesRobada, String patente) {
        for (int i = 0; i < patentesRobada.size(); i++) {
            if (patentesRobada.get(i).getPatente().equalsIgnoreCase(patente)) {
                return i;
            }
        }
        return -1;
    }

    public void printArray(ArrayList<PatenteRobadaVista> arrayList, String tag) {
        for (int i = 0; i < arrayList.size(); i++) {
            Log.v(tag, "ID" + ": " + arrayList.get(i).getId());
            Log.v(tag, "patente" + ": " + arrayList.get(i).getPatente());
            Log.v(tag, "longTime" + ": " + arrayList.get(i).getLongTime());
            Log.v(tag, "fecha" + ": " + arrayList.get(i).getFecha());
            Log.v(tag, "hora" + ": " + arrayList.get(i).getHora());
        }
        Log.v(tag, "size" + ": " + arrayList.size());
    }

    public void setVisiblePatenteRobadaVistaFirebase(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista, boolean visible) {
        ArrayList<PatenteRobadaVista> patentesRobadasVistasReverse = new ArrayList<>(patentesRobadasVistas);
        Collections.reverse(patentesRobadasVistasReverse);
        for (int i = 0; i < patentesRobadasVistasReverse.size(); i++) {
            if (patentesRobadasVistasReverse.get(i).getVerificada().equalsIgnoreCase("false")) {
                if (patentesRobadasVistasReverse.get(i).getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                    patentesRobadasVistasReverse.get(i).setVisible(visible);
                }
            }
        }
    }

    public void removePatenteRobadaVistaByIdArray(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista, String tag) {
        for (int i = patentesRobadasVistas.size() - 1; i >= 0; i--) {
            if (patentesRobadasVistas.get(i).getId().equalsIgnoreCase(patenteRobadaVista.getId())) {
                Log.v("patenteVEliminando", tag + ":" + patentesRobadasVistas.get(i).getId());
                patentesRobadasVistas.remove(i);
            }
        }
    }

    public void removePatenteRobadaVistaByPatente(ArrayList<PatenteRobadaVista> patentesRobadasVistas, PatenteRobadaVista patenteRobadaVista, String tag) {
        for (int i = patentesRobadasVistas.size() - 1; i >= 0; i--) {
            if (patentesRobadasVistas.get(i).getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                Log.v("patenteRobadaVistaR", "remove " + tag + ":" + patentesRobadasVistas.get(i).getId());
                patentesRobadasVistas.remove(i);
            }
        }
    }

    public boolean existPatenteRobadaVistaByPatente(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> patentesRobadasVistas) {
        ArrayList<PatenteRobadaVista> patentesRobadasVistasReverse = new ArrayList<>(patentesRobadasVistas);
        Collections.reverse(patentesRobadasVistasReverse);
        for (PatenteRobadaVista patenteRobadaVistaAux : patentesRobadasVistasReverse) {
            if (patenteRobadaVistaAux.getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                return true;
            }
        }
        return false;
    }

    public boolean existPatenteRobadaVistaById(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> patentesRobadasVistas) {
        ArrayList<PatenteRobadaVista> patentesRobadasVistasReverse = new ArrayList<>(patentesRobadasVistas);
        Collections.reverse(patentesRobadasVistasReverse);
        for (PatenteRobadaVista patenteRobadaVistaAux : patentesRobadasVistasReverse) {
            if (patenteRobadaVista.getId() != null && patenteRobadaVistaAux.getId() != null && patenteRobadaVistaAux.getId().equalsIgnoreCase(patenteRobadaVista.getId())) {
                return true;
            }
        }
        return false;
    }

    public int indexPatenteRobadaVistaById(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> arrayList) {
        int i = 0;
        for (PatenteRobadaVista patenteRobadaVistaAux : arrayList) {
            if (patenteRobadaVistaAux.getId().equalsIgnoreCase(patenteRobadaVista.getId())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int indexPatenteRobadaVistaByPatente(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteRobadaVista> arrayList) {
        int i = 0;
        for (PatenteRobadaVista patenteRobadaVistaAux : arrayList) {
            if (patenteRobadaVistaAux.getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int indexPatenteRobadaVistaByPatenteEscaneda(PatenteRobadaVista patenteRobadaVista, ArrayList<PatenteEscaneada> arrayList) {
        int i = 0;
        for (PatenteEscaneada patenteRobadaVistaAux : arrayList) {
            if (patenteRobadaVistaAux.getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public boolean isDistanciaMenor(double latitudInicial, double longitudInicial, double latitudFinal, double longitudFinal, double distanciaMaximaEnMetros) {
        float distancia = getDistancia(latitudInicial, longitudInicial, latitudFinal, longitudFinal);
        Log.v("lala", "latitudInicial: "+latitudInicial);
        Log.v("lala", "longitudInicial: "+longitudInicial);
        Log.v("lala", "latitudFinal: "+latitudFinal);
        Log.v("lala", "longitudFinal: "+longitudFinal);
        Log.v("lala", "distancia: "+distancia);
        Log.v("lala", "distanciaMaximaEnMetros: "+distanciaMaximaEnMetros);
        if (distancia < distanciaMaximaEnMetros) {
            return true;
        }
        return false;
    }

    public float getDistancia(double latitudInicial, double longitudInicial, double latitudFinal, double longitudFinal) {
        if(latitudInicial == 0.0 || longitudInicial == 0.0 || latitudFinal == 0.0 || longitudFinal == 0.0){
            return 0f;
        }
        Location locationInicial = new Location("patenteLocation");
        locationInicial.setLatitude(latitudInicial);
        locationInicial.setLongitude(longitudInicial);

        Location locationFinal = new Location("currentLocation");
        locationFinal.setLatitude(latitudFinal);
        locationFinal.setLongitude(longitudFinal);

        float distancia = locationFinal.distanceTo(locationInicial);
        return distancia;
    }

    public boolean siTieneUnTiempoMayorAXMinutosSeVaADecir(PatenteRobadaVista patenteRobadaVista, float tiempoEnMinutos, ArrayList<PatenteRobadaVista> patentesRobadasVistasArray) {
        /*
         * recorre arraylist de patentesRobadasVistasObservables buscando la ultima
         * patente que coincida con la patente observada si tiene un tiempo mayor a Xmin
         * se vuelve a decir
         * */

        int indiceAuxMayor = -1;
        for (int i = 0; i < patentesRobadasVistasArray.size(); i++) {
            long tiempoAuxMayor = 0;
            if (Long.parseLong(patentesRobadasVistasArray.get(i).getLongTime()) > tiempoAuxMayor && patentesRobadasVistasArray.get(i).getPatente().equalsIgnoreCase(patenteRobadaVista.getPatente())) {
                indiceAuxMayor = i;
            }
        }
        if (indiceAuxMayor > -1) {
            //encontado
            long miliseconds = Long.parseLong(patentesRobadasVistasArray.get(indiceAuxMayor).getLongTime());
            if (Long.parseLong(patenteRobadaVista.getLongTime()) - miliseconds > (60000 * tiempoEnMinutos)) {
                return true;
            } else {
                return false;
            }

        }

        //no existe patente en array
        return true;
    }

    public String enDireccion(PatenteRobadaVista patenteRobadaVista) {
        if (comparaEmailPatenteConEmailUsuario(patenteRobadaVista)) {
            return "";
        } else {
            if (patenteRobadaVista.getLatitud() == null || patenteRobadaVista.getLongitud() == null) {
                return "";
            } else if (
                    (patenteRobadaVista.getLatitud() != null && patenteRobadaVista.getLongitud() != null) &&
                            patenteRobadaVista.getLatitud().equalsIgnoreCase("") ||
                            patenteRobadaVista.getLongitud().equalsIgnoreCase("") ||
                            patenteRobadaVista.getLatitud().equalsIgnoreCase("0.0") ||
                            patenteRobadaVista.getLongitud().equalsIgnoreCase("0.0")
            ){
                return "";
            } else {
                ReverseGeocoding reverseGeocoding = new ReverseGeocoding(Double.parseDouble(patenteRobadaVista.getLatitud()),Double.parseDouble(patenteRobadaVista.getLongitud()),getApplicationContext());
                return reverseGeocoding.getDireccion();
            }

        }
    }

    public boolean comparaEmailPatenteConEmailUsuario(PatenteRobadaVista patenteRobadaVista) {
        if(patenteRobadaVista.getIdUsuario() == null){
            return false;
        }
        Log.v("isVistoPatenteSpeech", patenteRobadaVista.getIdUsuario());
        Log.v("isVistoPatenteSpeech", nombreUsuarioFirebase);
        Log.v("isVistoPatenteSpeech", idUsuarioFirebase);
        if (patenteRobadaVista.getIdUsuario()
                .equalsIgnoreCase(emailUsuarioFirebase)) {
            return true;
        } else {
            return false;
        }
    }

    public float kmDeDistanciaDePatenteRobadaVistaHastaUsuario(PatenteRobadaVista patenteRobadaVista) {
        Location locationDePatente = new Location("patenteRobadaVista");
        locationDePatente.setLatitude(Double.parseDouble(patenteRobadaVista.getLatitud()));
        locationDePatente.setLongitude(Double.parseDouble(patenteRobadaVista.getLongitud()));
        //retorna la distancia en metros
        if(currentLocation!=null){
            return currentLocation.distanceTo(locationDePatente);
        } else {
            return -1;
        }

    }

    public String positionSpeech(PatenteRobadaVista patenteRobadaVista) {
        String position = patenteRobadaVista.getPosition();
        if (comparaEmailPatenteConEmailUsuario(patenteRobadaVista)) {
            if (position.equalsIgnoreCase("left")) {
                return ", en carril izquierdo";
            } else if (position.equalsIgnoreCase("center")) {
                return ", delante de usted";
            } else if (position.equalsIgnoreCase("right")) {
                return ", en carril derecho";
            } else {
                return "carril desconocido";
            }
        } else {
            return "";
        }
    }

    public String aUnaDistanciaDe(float distancia, PatenteRobadaVista patenteRobadaVista) {
        if(distancia == -1 || distancia == 0){
            return "";
        }
        int distanciaKilometrosInt = (int) (distancia / 1000);
        int distanciaMetrosInt = (int) (distancia % 1000);
        if (distanciaKilometrosInt >= 1) {
            if (distanciaKilometrosInt ==
                    1) {
                return ", a una distancia de, " + distanciaKilometrosInt + ", kilómetro";
            } else {
                return ", a una distancia de, " + distanciaKilometrosInt + ", kilómetros";
            }
        } else {
            if (distanciaMetrosInt < 50) {
                if (comparaEmailPatenteConEmailUsuario(patenteRobadaVista)) {
                    return "";
                } else {
                    return ", a metros de usted.";
                }
            } else {
                distanciaMetrosInt = distanciaMetrosInt / 10;
                distanciaMetrosInt = distanciaMetrosInt * 10;
                return ", a una distancia de, " + distanciaMetrosInt + ", metros";
            }
        }
    }

    public void zoomCircle() {
        if (isUserTotem) {
            return;
        }
        if (googleMap != null) {
            float zoom = googleMap.getCameraPosition().zoom;

            zoom = (float) Math.floor(zoom);
            if (zoom == 11) {
                radioCircle = 100;
            } else if (zoom == 12) {
                radioCircle = 90;
            } else if (zoom == 13) {
                radioCircle = 80;
            } else if (zoom == 14) {
                radioCircle = 70;
            } else if (zoom == 15) {
                radioCircle = 60;
            } else if (zoom == 16) {
                radioCircle = 50;
            } else if (zoom == 17) {
                radioCircle = 40;
            } else if (zoom == 18) {
                radioCircle = 30;
            } else if (zoom == 19) {
                radioCircle = 20;
            } else if (zoom == 20) {
                radioCircle = 10;
            } else if (zoom == 21) {
                radioCircle = 5;
            }
            Log.v("updateZoomCamara", zoom + "");
            drawCircleLocation();
            UpdateCirclePatentes();
        }
    }

    public void defaulZoomCircle() {
        radioCircle = 70;
        UpdateCirclePatentes();
    }

    public void drawCircleLocation() {
        if(googleMap == null){
           return;
        }
        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.dot);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        if (positionMarker != null) {
            positionMarker.remove();
        }
        //add marker position
        final MarkerOptions positionMarkerOptions = new MarkerOptions()
                .position(new LatLng(latitud, longitud))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .anchor(0.5f, 0.5f);

        positionMarker = googleMap.addMarker(positionMarkerOptions);

        //draw circle
        int accuracyStrokeColor = Color.argb(255, 130, 182, 228);
        int accuracyFillColor = Color.argb(100, 130, 182, 228);
        if (accuracyCircle != null) {
            accuracyCircle.remove();
        }
        //add cirlce option
        final CircleOptions accuracyCircleOptions = new CircleOptions()
                .center(new LatLng(latitud, longitud))
                .radius(radioDeInteresAlertaPatenteMetros)
                .fillColor(accuracyFillColor)
                .strokeColor(accuracyStrokeColor)
                .strokeWidth(2.0f);

        accuracyCircle = googleMap.addCircle(accuracyCircleOptions);
    }

    public void UpdateCirclePatentes() {
        for (CirclePatente circlePatente : circlePatentesArray) {
            circlePatente.getMarkerCircle().setRadius(radioCircle);
        }
    }

    public void limpiarMarkerCircle(PatenteRobadaVista patenteRobadaVista) {
        ArrayList circlePatentesToRemove = new ArrayList();
        for (int i = 0; i < circlePatentesArray.size(); i++) {
            Log.v("cualPatenteLLega", patenteRobadaVista.getPatente() + " - " + circlePatentesArray.get(i).getPatenteRobadaVista().getPatente());
            if (patenteRobadaVista.getPatente().equalsIgnoreCase(circlePatentesArray.get(i).getPatenteRobadaVista().getPatente())) {
                if (circlePatentesArray.get(i).getMarkerCircle() != null) {
                    circlePatentesArray.get(i).getMarkerCircle().remove();
                }
                circlePatentesToRemove.add(circlePatentesArray.get(i));
                //circlePatentesArray.remove(i);
            }
        }
        circlePatentesArray.removeAll(circlePatentesToRemove);
    }

    public void limpiarMarkerPatente(PatenteRobadaVista patenteRobadaVista) {
        ArrayList markerPatentesToRemove = new ArrayList();
        for (int i = 0; i < markerPatentes.size(); i++) {
            Log.v("cualPatenteLLega", patenteRobadaVista.getPatente() + " - " + markerPatentes.get(i).getPatenteRobadaVista().getPatente());
            if (patenteRobadaVista.getPatente().equalsIgnoreCase(markerPatentes.get(i).getPatenteRobadaVista().getPatente())) {
                if (markerPatentes.get(i).getMarkerOptions() != null) {
                    markerPatentesToRemove.add(markerPatentes.get(i));
                }
                markerPatentes.get(i).getMarkerOptions().remove();
                //markerPatentes.remove(i);
            }
        }
        markerPatentes.removeAll(markerPatentesToRemove);
    }

    /*
    public void limpiarMarkerPatenteConcurrencia() {
        //limpio todos los marker option de esa patente en particular
        for (int i = markerPatentes.size()-1; i >= 0; i--) {

        }
            ArrayList<MarkerPatente> indexClear = new ArrayList<>();
        for (int i = 0; i < markerPatentes.size(); i++) {
            for (int j = i + 1; j < markerPatentes.size(); j++) {
                if (markerPatentes.get(i).getPatenteRobadaVista().getPatente().equalsIgnoreCase(markerPatentes.get(j).getPatenteRobadaVista().getPatente())) {
                    if (markerPatentes.get(i).getMarkerOptions() != null) {
                        markerPatentes.get(i).getMarkerOptions().remove();
                        //markerPatentes.remove(i);
                        indexClear.add(markerPatentes.get(i));
                    }
                }
                j++;
            }
            i++;
        }
        for (int i = 0; i < indexClear.size(); i++) {
            markerPatentes.remove(indexClear.get(i));
            Log.v("limpiarMarkerPatenteCon", i + "");
        }
    }
     */

    public int buscarColorPatente(String patente) {
        int color = 0;
        for (PatenteColor patenteColor : patenteColorArray) {
            if (patenteColor.getPatente().equalsIgnoreCase(patente)) {
                color = patenteColor.getColor();
                break;
            }
        }
        //quiere decir que no se encontro un color
        if (color == 0) {
            Random rnd = new Random();
            int colorAux = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            PatenteColor patenteColor = new PatenteColor(patente, colorAux);
            patenteColorArray.add(patenteColor);
            color = colorAux;

        }
        return color;
    }

    public Marker addMarkerPatenteRobadaVista(PatenteRobadaVista patenteRobadaVista, CurrentDate currentDate) {
        LatLng latLng = new LatLng(Double.parseDouble(patenteRobadaVista.getLatitud()), Double.parseDouble(patenteRobadaVista.getLongitud()));
        Log.v("LatLngdespues", String.valueOf(latLng.latitude));
        Log.v("LatLngdespues", String.valueOf(latLng.longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        String minutesString = getMinutes(patenteRobadaVista);
        Bitmap bitmap = writeOnDrawable(R.drawable.pat200red, patenteRobadaVista.getPatente().toUpperCase(), minutesString).getBitmap();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.snippet(patenteRobadaVista.getPatente());
        return googleMap.addMarker(markerOptions);
    }

    public Circle addCirclePatenteRobadaVista(PatenteRobadaVista patenteRobadaVista) {
        //draw circle
        int accuracyFillColor = patenteRobadaVista.getColorInt();
        int accuracyStrokeColor = patenteRobadaVista.getColorInt();
        //add cirlce option
        final CircleOptions accuracyCircleOptions = new CircleOptions()
                .center(new LatLng(Double.parseDouble(patenteRobadaVista.getLatitud()), Double.parseDouble(patenteRobadaVista.getLongitud())))
                .radius(radioCircle)
                .fillColor(accuracyFillColor)
                .strokeColor(accuracyStrokeColor)
                .strokeWidth(2.0f);
        Log.v("updateZoomCamaraCir", radioCircle + "");
        return googleMap.addCircle(accuracyCircleOptions);
    }

    public String getMinutes(PatenteRobadaVista patenteRobadaVista) {
        CurrentDate currentDate = new CurrentDate(new Date());
        long currentTimeLong = Long.parseLong(currentDate.getLongTime());

        long patenteRobadaVistaTimeLong = Long.parseLong(patenteRobadaVista.getLongTime());

        long elapsedTime = currentTimeLong - patenteRobadaVistaTimeLong;

        Log.v("tiempotranscurridoA", String.valueOf(elapsedTime));

        //convierto el tiempo transcurrido a positivo
        if (elapsedTime < 0) {
            elapsedTime = 0;
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        return String.valueOf(minutes);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("location", "se ejecuta on map ready");
        this.googleMap = googleMap;

        //se llama a constructor google maps dentro del metodo callbackOnConnected
        constructorGoogleMap();
    }

    public void constructorGoogleMap() {
        if(googleMap ==null || isUserTotem){
            return;
        }
        markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        Log.v("location", "constructorGoogleMap");

        Log.v("location", "google maps NO es nulo");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.v("cameramove","idle develop "+ String.valueOf(isDevelopMove));
                Log.v("cameramove","idle user "+ String.valueOf(isUserMove));
                if (isDevelopMove) {
                    Log.v("location","developmove");
                    centrarCamera = true;
                    buttonCentrar.hide();
                    isDevelopMove = false;
                }
                if (isUserMove) {
                    Log.v("location","usermove");
                    zoomCircle();
                    if (fullScreenCamera == true) {
                        buttonCentrar.show();
                    }
                    isUserMove = false;
                }
            }
        });
        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                Log.v("cameramove","started develop "+ String.valueOf(isDevelopMove));
                Log.v("cameramove","started user "+ String.valueOf(isUserMove));
                if (reason == REASON_GESTURE) {
                    // The user gestured on the map.
                    centrarCamera = false;
                    isUserMove = true;
                    isDevelopMove = false;
                    existPatenteRobadaEnMapa = false;
                    brilloDePantallaNormal();
                } else if (reason == REASON_API_ANIMATION) {
                    // The user tapped something on the map.
                } else if (reason == REASON_DEVELOPER_ANIMATION) {
                    if (existPatenteRobadaEnMapa){
                        centrarCamera = false;
                        isUserMove = true;
                        isDevelopMove = false;
                    }
                    else {
                        isDevelopMove = true;
                        isUserMove = false;
                    }

                }
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet()!=null) {
                    Log.v("queImprimeMarker", marker.getSnippet());
                    getClickedImagenPatente(marker.getSnippet());
                }
                //asasas
                return false;
            }
        });
        getLastLocation();

    }

    public void detenerLocationService(){
        if(locationServiceIntent != null){
            stopService(locationServiceIntent);
            locationServiceIntent=null;
            Log.v("locationService","stop service");
        }

        if (broadcastReceiverLocation != null) {
            Log.v("locationService","unregisterReceiver location");
            //broadcastReceiver.abortBroadcast();
            unregisterReceiver(broadcastReceiverLocation);

            broadcastReceiverLocation =null;
        }
    }

    public void iniciarLocationService(String power, String isChangePower) {
        if(locationServiceIntent==null){
            Log.v("locationService","create service");
            locationServiceIntent=new Intent(this, LocationService.class);
            locationServiceIntent.putExtra("power",power);
            locationServiceIntent.putExtra("isChangePower",isChangePower);

            //se supone que las versiones posteriores a "Oreo" deben ejecutar el servico como startForegroundService
            startService(locationServiceIntent);
        }

        if (broadcastReceiverLocation == null) {
            Log.v("locationService","registrando Receiver location");

            broadcastReceiverLocation = new MyBroadcastReceiverLocation() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitud = intent.getDoubleExtra("latitud",0.0);
                    longitud = intent.getDoubleExtra("longitud",0.0);
                    velocidad = intent.getFloatExtra("speed",0.0f);

                   if(latitud != 0.0 && longitud != 0.0){
                       if((lastKnownLatitud == -1 && lastKnownLongitud == -1) || (!isDistanciaMenor(latitud, longitud,lastKnownLatitud, lastKnownLongitud,20))){
                           Log.v("lastKnownLatituds","lastKnownLatitud == -1");
                           lastKnownLatitud = latitud;
                           lastKnownLongitud = longitud;
                           seMantieneLatitudYLongitud = false;
                       } else {
                           seMantieneLatitudYLongitud = true;
                           Log.v("locationXD","onReceive latitud: "+latitud+" longitud: "+longitud);
                       }
                   }

                    Log.v("locationXD","onReceive latitud: "+latitud+" longitud: "+longitud);

                    fromBackground = false;

                    constructorGoogleMap();
                    //verifica si el gps esta encendido o apagado
                    locationAvaible = intent.getBooleanExtra("location_avaible",false);

                    lastLocationAvaible = locationAvaible;
                    CurrentDate currentDate = new CurrentDate(new Date());
                    lastLocationAvaibleTime = currentDate.getDate().getTime();

                    Log.v("locationn", "lat: "+ lastKnownLatitud+ " lng: "+lastKnownLongitud + " location_avaible: " + locationAvaible);

                    //draw circle location
                    if(!isUserTotem){
                        drawCircleLocation();

                        LatLng currentLatLng = new LatLng(latitud, longitud);
                        if (centrarCamera && googleMap != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomGoogleMaps));
                        }
                    }
                }
            };
            registerReceiver(broadcastReceiverLocation, new IntentFilter("location_update"));
        }

        //muestra activity del menu de gps del telefono
        //startService(intentLocationService);
    }

    public void centrarMapa(LatLng latLng){
        if(googleMap == null){
            return;
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomGoogleMaps));
    }

    public Bitmap textAsBitmapDebug(Bitmap bitmap, float positionX, float positionY, String text,int textColor, float scaleTextSize) {
        Paint pPatente = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPatente.setTextSize(80f * scaleTextSize);
        pPatente.setColor(textColor);
        pPatente.setTextAlign(Paint.Align.LEFT);



        int width = (int) (pPatente.measureText(text)); // round
        int height = (int) (pPatente.descent() + 1000f); //230 con 3 elementos más
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), (int) (bitmap.getHeight()), Bitmap.Config.ARGB_8888);

        Canvas cPatente = new Canvas(image);
        cPatente.drawText(text, positionX, positionY, pPatente);


        return image;
    }


    public Bitmap textAsBitmap(Bitmap bitmap, Patente patenteRobadaVista, String patenteText, String fechaText, String horaText, String comuna, String latitudLongitud, int textColor, float scaleTextSize, float scaleLogo, float heightBitmapDeFondo) {
        Paint pPatente = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPatente.setTextSize(80f * scaleTextSize);
        pPatente.setColor(textColor);
        pPatente.setTextAlign(Paint.Align.LEFT);

        Paint pMarcaModeloColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        pMarcaModeloColor.setTextSize(35f * scaleTextSize);
        pMarcaModeloColor.setColor(textColor);
        pMarcaModeloColor.setTextAlign(Paint.Align.LEFT);

        Paint pMarca = new Paint(Paint.ANTI_ALIAS_FLAG);
        pMarca.setTextSize(25f * scaleTextSize);
        pMarca.setColor(textColor);
        pMarca.setTextAlign(Paint.Align.LEFT);

        Paint pModelo = new Paint(Paint.ANTI_ALIAS_FLAG);
        pModelo.setTextSize(35f * scaleTextSize);
        pModelo.setColor(textColor);
        pModelo.setTextAlign(Paint.Align.LEFT);

        Paint pColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        pColor.setTextSize(25f * scaleTextSize);
        pColor.setColor(textColor);
        pColor.setTextAlign(Paint.Align.LEFT);

        Paint pFechaHora = new Paint(Paint.ANTI_ALIAS_FLAG);
        pFechaHora.setTextSize(25f * scaleTextSize);
        pFechaHora.setColor(textColor);
        pFechaHora.setTextAlign(Paint.Align.LEFT);

        Paint pComuna = new Paint(Paint.ANTI_ALIAS_FLAG);
        pComuna.setTextSize(25f * scaleTextSize);
        pComuna.setColor(textColor);
        pComuna.setTextAlign(Paint.Align.LEFT);

        Paint pUbicacion = new Paint(Paint.ANTI_ALIAS_FLAG);
        pUbicacion.setTextSize(20f * scaleTextSize);
        pUbicacion.setColor(textColor);
        pUbicacion.setTextAlign(Paint.Align.LEFT);

        Paint pSafeByWolf = new Paint(Paint.ANTI_ALIAS_FLAG);
        pSafeByWolf.setTextSize(55f * scaleTextSize);
        pSafeByWolf.setColor(Color.WHITE);
        pSafeByWolf.setTextAlign(Paint.Align.LEFT);

        Paint pSafeByWolf2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        pSafeByWolf2.setTextSize(25f * scaleTextSize);
        pSafeByWolf2.setColor(Color.WHITE);
        pSafeByWolf2.setTextAlign(Paint.Align.LEFT);
        pSafeByWolf2.setAlpha(200);
        pSafeByWolf2.setAntiAlias(true);

        int width = (int) (pPatente.measureText(patenteText) + pFechaHora.measureText(fechaText + "/" + horaText) + 250f); // round
        int height = (int) (pPatente.descent() + 1000f); //230 con 3 elementos más
        Bitmap image = Bitmap.createBitmap(bitmap.getWidth(), (int) (bitmap.getHeight()+heightBitmapDeFondo), Bitmap.Config.ARGB_8888);

        Canvas cPatente = new Canvas(image);
        cPatente.drawText(patenteText, 0, -pPatente.ascent(), pPatente);

        Canvas cMarcaModeloColor = new Canvas(image);
        if (patenteRobadaVista.getMarca() != null && patenteRobadaVista.getColor() != null && patenteRobadaVista.getMarca() != "" && patenteRobadaVista.getColor() != "") {
            cMarcaModeloColor.drawText(patenteRobadaVista.getMarca() + "-" + patenteRobadaVista.getColor(), pPatente.measureText(patenteText) + 15f, pPatente.descent() + (20f*scaleTextSize), pMarcaModeloColor);
        }else if ((patenteRobadaVista.getMarca() != null && patenteRobadaVista.getMarca() != "") && (patenteRobadaVista.getColor() == null || patenteRobadaVista.getColor() == "") ) {
            cMarcaModeloColor.drawText(patenteRobadaVista.getMarca(), pPatente.measureText(patenteText) + 15f, pPatente.descent() + (20f*scaleTextSize), pMarcaModeloColor);
        } else if ((patenteRobadaVista.getColor() != null && patenteRobadaVista.getColor() != "") && (patenteRobadaVista.getMarca() == null || patenteRobadaVista.getMarca() == "")) {
            cMarcaModeloColor.drawText(patenteRobadaVista.getColor(), pPatente.measureText(patenteText) + 15f, pPatente.descent() + (20f*scaleTextSize), pMarcaModeloColor);
        }

        Canvas cModelo = new Canvas(image);
        if (patenteRobadaVista.getMarca() != null) {
            if (patenteRobadaVista.getModelo() != "") {
                cModelo.drawText(patenteRobadaVista.getModelo(), pPatente.measureText(patenteText) + 15f, -pMarca.ascent() + (50f*scaleTextSize), pModelo);
            }
        }

        Canvas cFechaHora = new Canvas(image);
        cFechaHora.drawText(fechaText + "/" + horaText, 0f, cFechaHora.getHeight() - (70f*scaleTextSize), pFechaHora);

        Canvas cComuna = new Canvas(image);
        if (comuna != null) {
            if (comuna != "") {
                cComuna.drawText(comuna, 0f, cComuna.getHeight() - (45f*scaleTextSize), pComuna);
            } else {
                cComuna.drawText("Sin Comuna", 0f, cComuna.getHeight() - (45f*scaleTextSize), pComuna);
            }
        } else {
            cComuna.drawText("Sin Comuna", 0f, cComuna.getHeight() - (45f*scaleTextSize), pComuna);
        }

        Canvas cUbicacion = new Canvas(image);
        if (!cUbicacion.equals("0.0/0.0")) {
            cUbicacion.drawText(latitudLongitud, 0f, cUbicacion.getHeight() - (20f*scaleTextSize), pUbicacion);
        }

        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logoredondeado);
        Bitmap resizedBitmapLogo = Bitmap.createScaledBitmap(bitmapLogo, (int)(100*scaleLogo), (int)(100*scaleLogo), true);

        Canvas cSafeByWolf2 = new Canvas(image);
        cSafeByWolf2.drawBitmap(resizedBitmapLogo, cSafeByWolf2.getWidth() - (110f*scaleLogo), cSafeByWolf2.getHeight() - (110f*scaleLogo), pSafeByWolf2);
        //cSafeByWolf2.drawText("SafeByWolf", cSafeByWolf2.getWidth() - 150f, cSafeByWolf2.getHeight() - 25f, pSafeByWolf2);
        return image;
    }

    //sobrepone dos bitmap
    // insertaTextoEnBitmap
    private Bitmap sobreponeBitmap(Bitmap backgroundBitmap, int widthBackgroundBitmap, int heightBackgroundBitmap, Bitmap bitmapASobreponer, boolean isCrop) {
        Bitmap bmOverlay;
        if(isCrop){
            bmOverlay = Bitmap.createBitmap(widthBackgroundBitmap, heightBackgroundBitmap+this.heightBitmapDeFondo, backgroundBitmap.getConfig());
        } else{
            bmOverlay = Bitmap.createBitmap(widthBackgroundBitmap, heightBackgroundBitmap, backgroundBitmap.getConfig());
        }
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(backgroundBitmap, (bmOverlay.getWidth() - backgroundBitmap.getWidth()) / 2, (bmOverlay.getHeight() - backgroundBitmap.getHeight()) / 2, new Paint());
        canvas.drawBitmap(bitmapASobreponer,(bmOverlay.getWidth() - bitmapASobreponer.getWidth()) / 2, (bmOverlay.getHeight() - bitmapASobreponer.getHeight()) / 2, new Paint());
        return bmOverlay;
    }

    public BitmapDrawable writeOnDrawable(int drawableId, String patente, String minutos) {

        Bitmap bmInit = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bm = Bitmap.createScaledBitmap(
                bmInit, 220, 200, false);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40f);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(patente, bm.getWidth() / 2, (bm.getHeight() / 2) - 20, paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        paint2.setTextSize(27f);
        paint2.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
        paint2.setAntiAlias(true);
        paint2.setFakeBoldText(true);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setTextAlign(Paint.Align.CENTER);

        Canvas canvas2 = new Canvas(bm);
        canvas2.drawText("hace " + minutos + " min", bm.getWidth() / 2, (bm.getHeight() / 2) + 20, paint2);

        return new BitmapDrawable(bm);
    }

    public int existePatenteEnPatentesDistintas(String patente, ArrayList<String> array){
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equalsIgnoreCase(patente)) {
                return i;
            }
        }
        return -1;
    }

    public int existePatenteEnPatentesEscaneadas(String patente, ArrayList<PatenteEscaneada> array){
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getPatente().equalsIgnoreCase(patente)) {
                return i;
            }
        }
        return -1;
    }

    public void clasificarPatente(PatenteQueue patenteQueue, boolean buscar){
        //busca si la patente vista se encuentra en el array de patentes distintas
        final String patente = patenteQueue.getPatenteEscaneada().getPatente();
        final PatenteEscaneada patenteEscaneada = patenteQueue.getPatenteEscaneada();

        int i = existePatenteEnPatentesDistintas(patente, arrayListPatentesDistintas);
        if(i != -1) {
            if (arrayListPatentesDistintas.get(i).equals(patente)) {
                int index = existePatenteEnPatentesEscaneadas(patente, arrayListPatentesEscaneadas);
                if(index != -1){
                    patenteEscaneada.setTipo(arrayListPatentesEscaneadas.get(index).getTipo());
                    if (patenteEscaneada.getTipo().equalsIgnoreCase(Referencias.ROBADA) || patenteEscaneada.getTipo().equalsIgnoreCase(Referencias.LISTANEGRA)) {
                        siExisteRobadaOListaNegra(patenteQueue, index);
                    } else if (patenteEscaneada.getTipo().equalsIgnoreCase(Referencias.ESCANEADA)) {
                        patenteSiExisteNoRobada(patenteQueue, buscar, index);
                    }
                }
            }
        } else {
            patenteNoExiste(patenteQueue);
        }
    }

    private void patenteSiExisteNoRobada(PatenteQueue patenteQueue, boolean buscar, int indicePatente) {

        //voy a volver a consultar patente en un tiempo mayor a 1 minuto
        if ((Long.parseLong(new CurrentDate(new Date()).getLongTime()) - (arrayListPatentesEscaneadas.get(indicePatente).getLastHeadTime()) > tiempoVolverABuscarPatenteEnBDMilis) && buscar) {
            patenteQueue.getPatenteEscaneada().setTiempoInicial(Long.parseLong(new CurrentDate(new Date()).getLongTime()));
            //busca nuevamente porque paso tiempo
            //mediaPlayerNotificacionPatenteNoRobada.start();
            enqueueHashMapPatente(patenteQueue, indicePatente);

        } else {
            if(existeGrupo(listaGrupoGlobal, "BCI") && isUserTotem) {
                if(!existeRafaga(arrayListPatentesEscaneadas.get(indicePatente))){
                    Log.v("patenteRobadaVista", "inicializa bag sdfs 1");
                    setComunaFromAPI(patenteQueue.getPatenteEscaneada(), patenteQueue.getBitmapRectF(), false, false, indicePatente);
                }
            }
        }
    }

    public void siExisteRobadaOListaNegra(PatenteQueue patenteQueue, int indicePatente){
        if (!existeSetRafaga(arrayListPatentesEscaneadas.get(indicePatente))) {
            //crea nueva rafaga
            enqueueHashMapPatente(patenteQueue, indicePatente);
        } else {
            setComunaFromAPI(patenteQueue.getPatenteEscaneada(), patenteQueue.getBitmapRectF(), false,  false, indicePatente);
        }
    }

    private void patenteNoExiste(PatenteQueue patenteQueue) {
        final String patente = patenteQueue.getPatenteEscaneada().getPatente();
        final PatenteEscaneada patenteEscaneada = patenteQueue.getPatenteEscaneada();
        int indicePatente = existPatenteInPatenteEscaneadaArray(arrayListPatentesEscaneadas,patente);
        if(indicePatente == -1){
            Log.v("patenteRobadaVistan", "patenteNoExiste indicePatente == -1 patente: "+patente);
            if(!isUserTotem){
                mediaPlayerNotificacionPatenteNoRobada.start();
            }
            aumentaContadorDeTotales();

            arrayListPatentesEscaneadas.add(patenteEscaneada);
            indicePatente = (arrayListPatentesEscaneadas.size() - 1);

            RowPatenteEscaneada rowPatenteEscaneada = new RowPatenteEscaneada(patente,String.valueOf(patenteEscaneada.getConfianzaOCR()), patenteEscaneada.isGreaterDetectionPatente(), patenteEscaneada.isGreaterDetectionAuto());

            Log.v("mapRowPatentesEscan","mapRowPatentesEscan: "+arrayListRowPatentesEscaneadas.size());

            mapRowPatentesEscaneadas.put(patente,rowPatenteEscaneada);

            if(mapRowPatentesEscaneadas.size() == 1){
                pushOrPopAnimatePatentesEscaneadas();
            }
        }
        enqueueHashMapPatente(patenteQueue, indicePatente);
    }

    public void pushOrPopAnimatePatentesEscaneadas(){
        Map.Entry<String,RowPatenteEscaneada> entry = mapRowPatentesEscaneadas.entrySet().iterator().next();
        if (arrayListRowPatentesEscaneadas.size() < 3) {
            pushPatenteAnimate(entry.getValue());
        } else {
            popPatenteAnimate();
            pushPatenteAnimate(entry.getValue());
        }
        Log.v("patenteRobadaVistan","mapRowPatentesEscaneadas.size(): "+mapRowPatentesEscaneadas.size());
    }

    public void popPatenteAnimate(){
        constraintLayoutPatenteEscaneadaRowItem0.animate().translationY(-100).alpha(0.1f).withEndAction(new Runnable() {
            @Override
            public void run() {
                arrayListRowPatentesEscaneadas.remove(0);
                constraintLayoutPatenteEscaneadaRowItem0.setVisibility(View.GONE);
                constraintLayoutPatenteEscaneadaRowItem0.animate().translationY(0).alpha(1);
            }
        });
    }

    public void pushPatenteAnimate(RowPatenteEscaneada rowPatenteEscaneada){
        constraintLayoutPatenteEscaneadaRowItem.animate().alpha(0).translationY(150).withEndAction(new Runnable() {
            @Override
            public void run() {
                arrayListRowPatentesEscaneadas.add(rowPatenteEscaneada);
                constraintLayoutPatenteEscaneadaRowItem.setVisibility(View.VISIBLE);
                textViewPatenteEscaneadaRowItem.setText(rowPatenteEscaneada.getPatente());
                if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
                    textViewPatenteConfianza.setText(rowPatenteEscaneada.getConfianza());
                    textViewPatenteConfianza.setVisibility(View.VISIBLE);
                }
                textViewPatenteConfianza.setText(rowPatenteEscaneada.getConfianza());
                constraintLayoutPatenteEscaneadaRowItem.animate().alpha(1).translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        constraintLayoutPatenteEscaneadaRowItem.setVisibility(View.GONE);
                        constraintLayoutPatenteEscaneadaRowItem0.setVisibility(View.VISIBLE);
                        if(arrayListRowPatentesEscaneadas.size() >= 1) {
                            textViewPatenteEscaneadaRowItem0.setText(arrayListRowPatentesEscaneadas.get(0).getPatente());
                            Log.v("isGreaterDetection", "Item0: " + textViewPatenteEscaneadaRowItem0.getText() + " getPatente: " + rowPatenteEscaneada.getPatente()+ " isGreaterDetection: " + rowPatenteEscaneada.isGreaterDetectionPatente());
                            if(textViewPatenteEscaneadaRowItem0.getText().equals(rowPatenteEscaneada.getPatente())){
                                if(rowPatenteEscaneada.isGreaterDetectionPatente()){
                                    constraintLayoutPatenteEscaneadaRowItem0.setBackgroundColor(getResources().getColor(R.color.green));
                                } else if(rowPatenteEscaneada.isGreaterDetectionAuto()){
                                    constraintLayoutPatenteEscaneadaRowItem0.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }

                            }
                            if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
                                textViewPatenteConfianza0.setText(arrayListRowPatentesEscaneadas.get(0).getConfianza());
                                textViewPatenteConfianza0.setVisibility(View.VISIBLE);
                            }
                        }
                        if(arrayListRowPatentesEscaneadas.size() > 1){
                            constraintLayoutPatenteEscaneadaRowItem1.setVisibility(View.VISIBLE);
                            textViewPatenteEscaneadaRowItem1.setText(arrayListRowPatentesEscaneadas.get(1).getPatente());
                            Log.v("isGreaterDetection","Item1: "+textViewPatenteEscaneadaRowItem1.getText() + " getPatente: " + rowPatenteEscaneada.getPatente()+ " isGreaterDetection: " + rowPatenteEscaneada.isGreaterDetectionPatente());

                            if(textViewPatenteEscaneadaRowItem1.getText().equals(rowPatenteEscaneada.getPatente())){
                                if(rowPatenteEscaneada.isGreaterDetectionPatente()){
                                    constraintLayoutPatenteEscaneadaRowItem1.setBackgroundColor(getResources().getColor(R.color.green));
                                } else if(rowPatenteEscaneada.isGreaterDetectionAuto()){
                                    constraintLayoutPatenteEscaneadaRowItem1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }
                            }
                            if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
                                textViewPatenteConfianza1.setText(arrayListRowPatentesEscaneadas.get(1).getConfianza());
                                textViewPatenteConfianza1.setVisibility(View.VISIBLE);
                            }
                        }
                        if(arrayListRowPatentesEscaneadas.size() > 2){
                            constraintLayoutPatenteEscaneadaRowItem2.setVisibility(View.VISIBLE);
                            textViewPatenteEscaneadaRowItem2.setText(arrayListRowPatentesEscaneadas.get(2).getPatente());
                            Log.v("isGreaterDetection","Item2: "+textViewPatenteEscaneadaRowItem2.getText() + " getPatente: " + rowPatenteEscaneada.getPatente()+ " isGreaterDetection: " + rowPatenteEscaneada.isGreaterDetectionPatente());
                            if(textViewPatenteEscaneadaRowItem2.getText().equals(rowPatenteEscaneada.getPatente())){
                                if(rowPatenteEscaneada.isGreaterDetectionPatente()){
                                    constraintLayoutPatenteEscaneadaRowItem2.setBackgroundColor(getResources().getColor(R.color.green));
                                } else if(rowPatenteEscaneada.isGreaterDetectionAuto()){
                                    constraintLayoutPatenteEscaneadaRowItem2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }
                            }
                            if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
                                textViewPatenteConfianza2.setText(arrayListRowPatentesEscaneadas.get(2).getConfianza());
                                textViewPatenteConfianza2.setVisibility(View.VISIBLE);
                            }
                        }
                        mapRowPatentesEscaneadas.remove(rowPatenteEscaneada.getPatente());
                        if(mapRowPatentesEscaneadas.size() > 0){
                            pushOrPopAnimatePatentesEscaneadas();
                        }
                    }
                });
            }
        });
    }

    public boolean isGreaterDetection(BitmapRectF bitmapRectF, PatenteEscaneada patenteEscaneada){
        final int orientation = getResources().getConfiguration().orientation;
        float rectFWidth = bitmapRectF.getOriginalRectF().width();
        float rectFHeight = bitmapRectF.getOriginalRectF().height();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            rectFWidth = bitmapRectF.getOriginalRectF().height(); //En portrait el lado mas grande del recuadro es el rectF.height
            rectFHeight = bitmapRectF.getOriginalRectF().width(); //En portrait el lado mas pequeño del racuadro es el rectF.width
        }

        if(isUserTotem) {
            if (bitmapRectF.getTitle().equalsIgnoreCase("patente")) {
                Log.v("isGreaterDetection", "patente width: "+rectFWidth +" height:"+ rectFHeight);
                if (proporcionAnchoMinimoPatente * previewWidth <= rectFWidth && proporcionAlturaMinimaPatente * previewHeight <= rectFHeight) {
                    patenteEscaneada.setGreaterDetectionPatente(true);
                    Log.v("isGreaterDetection", "patente return true previewWidth: "+previewWidth+" proporcionAnchoMinimoPatenteTotem: "+ proporcionAnchoMinimoPatente * previewWidth+" rectFWidth: "+rectFWidth +" previewHeight: "+previewHeight+" proporcionAlturaMinimaPatenteTotem: "+ proporcionAlturaMinimaPatente * previewHeight+" rectFHeight: "+rectFHeight);
                    return true;
                } else {
                    Log.v("isGreaterDetection", "patente return false previewWidth: "+previewWidth+" proporcionAnchoMinimoPatenteTotem: "+ proporcionAnchoMinimoPatente * previewWidth+" rectFWidth: "+rectFWidth +" previewHeight: "+previewHeight+" proporcionAlturaMinimaPatenteTotem: "+ proporcionAlturaMinimaPatente * previewHeight+" rectFHeight: "+rectFHeight);
                    return false;
                }

            } else if ((bitmapRectF.getTitle().equalsIgnoreCase("auto"))) {
                if(proporcionAnchoMinimoAuto * previewWidth <= rectFWidth && proporcionAlturaMinimaAuto * previewHeight <= rectFHeight){
                    patenteEscaneada.setGreaterDetectionAuto(true);
                    Log.v("isGreaterDetection", "auto return true previewWidth: "+ previewWidth + " proporcionAnchoMinimoAutoTotem: "+ proporcionAnchoMinimoAuto * previewWidth+" rectFWidth: "+rectFWidth +" previewHeight: "+ previewHeight+" proporcionAlturaMinimaAutoTotem: "+ proporcionAlturaMinimaAuto * previewHeight+" rectFHeight: "+rectFHeight);
                    return true;
                } else {
                    Log.v("isGreaterDetection", "auto return false previewWidth: "+previewWidth+" proporcionAnchoMinimoAutoTotem: "+ proporcionAnchoMinimoAuto * previewWidth+" rectFWidth: "+rectFWidth +" previewHeight: "+previewHeight+" proporcionAlturaMinimaAutoTotem: "+ proporcionAlturaMinimaAuto * previewHeight+" rectFHeight: "+rectFHeight);
                    return false;
                }
            }
            Log.v("isGreaterDetection", "auto return false");
            return false;
        } else {
            return true;
        }
    }

    public PatenteEscaneada crearPatenteEscaneada(String patente, BitmapRectF bitmapRectF, float confianzaOCR){
        PatenteEscaneada patenteEscaneada = new PatenteEscaneada();
        patenteEscaneada.setPatente(patente);
        patenteEscaneada.setConfianzaOCR((int) (confianzaOCR));
        patenteEscaneada.setTiempoInicial(Long.parseLong(new CurrentDate(new Date()).getLongTime()));

        //patenteEscaneada.setGrupo(this.gruposUsuarioFirebaseArray);

        patenteEscaneada.setIdUsuario(emailUsuarioFirebase);
        patenteEscaneada.setEmailUsuario(emailUsuarioFirebase);
        patenteEscaneada.setNombreUsuario(nombreUsuarioFirebase);
        patenteEscaneada.setApellidoUsuario(apellidoUsuarioFirebase);
        patenteEscaneada.setContactoUsuario(contactoUsuarioFirebase);

        //set ubicacion GPS
        patenteEscaneada.setLatitud(String.valueOf(lastKnownLatitud));
        patenteEscaneada.setLongitud(String.valueOf(lastKnownLongitud));

        if(comunaUserFromDB != null && !comunaUserFromDB.equalsIgnoreCase("") && !comunaUserFromDB.equalsIgnoreCase("Sin comuna")){
            patenteEscaneada.setComuna(comunaUserFromDB);
            patenteEscaneada.setCiudad(comunaUserFromDB);
        } else {
            //set direccion y comuna
            ReverseGeocoding reverseGeocoding = new ReverseGeocoding(lastKnownLatitud, lastKnownLongitud, getApplicationContext());
            patenteEscaneada.setComuna(Utils.normalizarString(reverseGeocoding.getComuna()));
            patenteEscaneada.setPais(Utils.normalizarString(reverseGeocoding.getPais()));
            patenteEscaneada.setRegion(Utils.normalizarString(reverseGeocoding.getRegion()));
            patenteEscaneada.setCiudad(Utils.normalizarString(reverseGeocoding.getCiudad()));
            patenteEscaneada.setDireccion(reverseGeocoding.getDireccion());
            Log.v("lala","entreeeeeeeeeeeeeeeee comuna: "+patenteEscaneada.getComuna());
        }

        if(patente.equalsIgnoreCase("")){
            patenteEscaneada.setTipo(Referencias.SINPATENTE);
        } else {
            patenteEscaneada.setTipo(Referencias.ESCANEADA);
        }

        patenteEscaneada.setBoundLeft(String.valueOf(bitmapRectF.getRectF().left));
        patenteEscaneada.setBoundTop(String.valueOf(bitmapRectF.getRectF().top));
        patenteEscaneada.setBoundRight(String.valueOf(bitmapRectF.getRectF().right));
        patenteEscaneada.setBoundBottom(String.valueOf(bitmapRectF.getRectF().bottom));
        patenteEscaneada.setBound(String.valueOf(bitmapRectF.getRectF().bottom-bitmapRectF.getRectF().top));
        patenteEscaneada.setPositionX(String.valueOf(bitmapRectF.getRectF().centerX()));
        patenteEscaneada.setPositionY(String.valueOf(bitmapRectF.getRectF().centerY()));
        patenteEscaneada.setZoom(String.valueOf(progressSeekBarCameraZoom));
        patenteEscaneada.setVelocidad(String.valueOf(velocidad));

        patenteEscaneada.setTitleImagenAmpliada(bitmapRectF.getTitle());
        CurrentDate currentDate = new CurrentDate(new Date());
        patenteEscaneada.setLongTime(currentDate.getLongTime());
        patenteEscaneada.setFecha(currentDate.getFecha());
        patenteEscaneada.setHora(currentDate.getHora());

        Log.v("patenteRobadaVista","antes de isGreaterDetection");

        isGreaterDetection(bitmapRectF, patenteEscaneada);

        Log.v("patenteRobadaVista","Despues de isGreaterDetection");

        return patenteEscaneada;
    }

    public CompletableFuture completableFutureReverseGeocodingAPI(Patente patenteEscaneada){
        CompletableFuture<Void> future = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    reverseGeocodingAPI(patenteEscaneada);
                }
            });
        } else {
            Log.v("setCiudad","es de loa antiguos");
        }
        return future;
    }

    public CompletableFuture completableFutureGetServerTimestamp(){
        CompletableFuture<Void> future = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    Utils.getServerTime(DetectorActivity.this,API_URL,0);
                }
            });
        } else {
            Log.v("setCiudad","es de los antiguos");
        }
        return future;
    }


    public void reverseGeocodingAPI(Patente patenteEscaneada){
        Log.v("jsoncomuna","buscaCiudadReverseGeocoding: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bigdatacloud.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Map<String, String> queryMaps = new HashMap<>();
        queryMaps.put("latitude", getLastKnownLatitudString());
        queryMaps.put("longitude", getLastKnownLongitudString());
        queryMaps.put("localityLanguage", "es");

        Call callTime = jsonPlaceHolderApi.getComunaResults(queryMaps);
        try {
            Response response = callTime.execute();
            Gson gson = new Gson();
            String jsonString = gson.toJson(response.body());
            Log.v("jsoncomuna","jsoncomuna: "+jsonString);
            //jsoncomuna: {"latitude":-34.9968818,"longitude":-71.2355332,"continent":"América del Sur","lookupSource":"coordinates","continentCode":"SA","localityLanguageRequested":"es","city":"Curicó","countryName":"Chile","countryCode":"CL","postcode":"","principalSubdivision":"Región del Maule","principalSubdivisionCode":"CL-ML","plusCode":"47QC2Q37+6Q","locality":"Curicó","localityInfo":{"administrative":[{"name":"Chile","description":"país de América del Sur","isoName":"Chile","order":3.0,"adminLevel":2.0,"isoCode":"CL","wikidataId":"Q298","geonameId":3895114.0},{"name":"Región del Maule","description":"región de Chile","isoName":"Maule","order":4.0,"adminLevel":4.0,"isoCode":"CL-ML","wikidataId":"Q2166","geonameId":3880306.0},{"name":"Curicó","description":"provincia de Chile","order":5.0,"adminLevel":6.0,"wikidataId":"Q201338","geonameId":3892868.0},{"name":"Curicó","description":"ciudad de Chile","order":6.0,"adminLevel":8.0,"wikidataId":"Q13030","geonameId":3892870.0}],"informative":[{"name":"América del Sur","description":"continente, principalmente en el cuadrante suroeste de la Tierra","order":1.0,"isoCode":"SA","wikidataId":"Q18","geonameId":6255150.0},{"name":"Cordillera de Los Andes","description":"cordillera en Sudamérica occidental","order":2.0,"wikidataId":"Q5456"}]}}

            JSONObject objeto = new JSONObject(jsonString);

            Log.v("setCiudad","se setea comuna desde free geocoding: "+objeto.getString("locality") +" lat: "+getLastKnownLatitudString()+" lng: "+getLastKnownLongitudString());

            JSONObject localityInfo= objeto.getJSONObject("localityInfo");
            JSONArray administrative = localityInfo.getJSONArray("administrative");
            lastPais = Utils.normalizarString(objeto.getString("countryName"));
            lastRegion = Utils.normalizarString(objeto.getString("principalSubdivision"));
            String comuna = Utils.normalizarString(objeto.getString("locality"));
            lastCiudad = Utils.normalizarString(objeto.getString("city"));

            for(int i = 0; i <  administrative.length(); i++){
                administrative.getJSONObject(i);
                Log.v("impr",administrative.getJSONObject(i).toString());
                if(administrative.getJSONObject(i).getInt("adminLevel") == 8){
                    if(comuna == null || (comuna != null && comuna == "")){
                        comuna = administrative.getJSONObject(i).getString("name");
                    }
                    Log.v("impr","comuna: "+comuna);
                }
            }

            lastComuna = comuna;
            patenteEscaneada.setPais(lastPais);
            patenteEscaneada.setRegion(lastRegion);
            patenteEscaneada.setCiudad(lastCiudad);
            patenteEscaneada.setComuna(lastComuna);

            Log.v("setCiudad","reverseGeocodingAPI lastCiudad: "+lastCiudad);

        } catch (IOException | JSONException e) {
            Log.v("setCiudad","error e: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void concatenarGrupos(List<String> grupos, String tipo, String tipoDeBase, List<String> listGrupos, List<GroupType> listaGrupoTipo){
        Log.v("asde3r3","pase 11.1 patenteEscaneada.getComuna(): "+grupos);

        if (grupos == null) {
            return;
        }
        if(grupos.size() == 0){
            return;
        }
        for(String grupo:grupos) {
            Log.v("dsdfs","grupo");
            if (grupo != "TODO" && grupo != "SIN GRUPO") {
                Log.v("asde3r3","pase 13");

                if(!existeGrupo(listGrupos, grupo)){
                    Log.v("asde3r3","pase 13 antes del add");
                    listGrupos.add(grupo);
                    Log.v("asde3r3","pase 13 DESPUES del add");
                }
                Log.v("asde3r3","pase 14");
                if(!existeGrupoTipo(listaGrupoTipo, grupo, tipo)){
                    listaGrupoTipo.add(new GroupType(grupo,tipo, tipoDeBase));
                }
                Log.v("asde3r3","pase 15");
            }
        }
        Log.v("asde3r3","pase 16");

        return;
    }

    public void buscarPatenteListaNegraEnBaseDeDatosFirebase(PatenteEscaneada patenteEscaneada, BitmapRectF bitmapRectF, int indicePatente) {
        List<String> listGrupos = new ArrayList<>();
        List<GroupType> listaGrupoTipo = new ArrayList<>();
        Log.v("patenteRobadaVista", "buscarPatenteListaNegraEnBaseDeDatosFirebase - patente: "+  patenteEscaneada.getPatente());

        //busca cada patente escaneada en coleccion patenteListaNegra (costo de lectura 1 o mas)
        db.collection(Referencias.PATENTELISTANEGRA).whereEqualTo("patente", patenteEscaneada.getPatente())
                .whereEqualTo("visible",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.v("patenteRobadaVista", "on complete listanegra");

                PatenteListaNegra patenteListaNegra = null;
                Log.v("patenteRobadaVista", "ANTES DEL FOR ");

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    try {
                        patenteListaNegra = documentSnapshot.toObject(PatenteListaNegra.class);
                        Log.v("patenteRobadaVista", "ENTRE AL FOR lista negra grupo:"+patenteListaNegra.getGrupo());

                        //si patente pertenece a una base SOAP y no soy SOAP se descarta
                        if(patenteListaNegra.isBaseSoap() && !isUserSoap){
                            Log.v("patenteRobadaVista", "retorno porque patente pertenece a una base SOAP y no sy policia o no soy SOAP  ");
                            continue;
                        }

                        //en patenteEscaneada se setea toda la información proveniente de la base de datos
                        if(patenteListaNegra != null) {
                            Log.v("patenteRobadaVista", "distinto de nulo");
                            if (patenteListaNegra.getPatente().equalsIgnoreCase(patenteEscaneada.getPatente())) {
                                Log.v("patenteRobadaVista", "patenteListaNegra.getPatente(): "+patenteListaNegra.getPatente());
                                if (patenteListaNegra.isBaseRobada()) {
                                    setPatenteEscaneadaConPatenteListaNegra(patenteEscaneada, patenteListaNegra, Referencias.ROBADA, listGrupos, listaGrupoTipo);
                                } else {
                                    setPatenteEscaneadaConPatenteListaNegra(patenteEscaneada, patenteListaNegra, Referencias.LISTANEGRA, listGrupos, listaGrupoTipo);
                                }
                            } else {
                                Log.v("patenteRobadaVista", "patenteListaNegra.getPatente2(): "+patenteListaNegra.getPatente() + "" + patenteEscaneada.getPatente());
                            }
                        }

                    } catch (Exception e) {
                        Log.v("patenteRobadaVista", "error try catch lista negra message: "+e.getMessage());
                        return;
                    }
                }

                //verifico si patente proviene de base lista negra y robada
                boolean tipoAmbas = false;
                if(patenteEscaneada.isBaseRobada() && patenteEscaneada.isBaseListaNegra()){
                    tipoAmbas = true;
                    patenteEscaneada.setTipo("robada");
                }
                patenteEscaneada.setTipoAmbas(tipoAmbas);

                Log.v("patenteRobadaVista","isBaseRobada: "+patenteEscaneada.isBaseRobada());
                Log.v("patenteRobadaVista","isBaseListaNegra: "+patenteEscaneada.isBaseListaNegra());
                Log.v("patenteRobadaVista","isBaseSoap: "+patenteEscaneada.isBaseSoap());

                //concatena los grupos del usuario
                if(isGreaterDetection(bitmapRectF, patenteEscaneada)) {
                    concatenarGrupos(listaGrupoGlobal, "Usuario", "", listGrupos, listaGrupoTipo);
                }

                patenteEscaneada.setGrupo(listGrupos);
                patenteEscaneada.setListaGrupoTipo(listaGrupoTipo);

                //Crea una alerta de patente robada de forma rapida para ser alertado al usuario de forma local (llamar a caso 1)
                if(!isUserTotem && !isUserSinAlertas){
                    Log.v("patenteRobadaVista"," antes crearPatenteListaNegraPreAlerta: ");
                    PatenteRobadaVista patenteRobadaVista = crearPatenteListaNegraPreAlerta(patenteEscaneada);
                    Log.v("patenteRobadaVista"," despues crearPatenteListaNegraPreAlerta: ");
                    if (patenteRobadaVista != null) {
                        //obtiene un id de coleccion patenteRobadaVista (costo de lectura 0)
                        String idPatenteRobadaVistaAux = db.collection(Referencias.POSIBLEPATENTEROBADAVISTA).document().getId();
                        patenteRobadaVista.setId(idPatenteRobadaVistaAux);
                        //si NO es robada y si es lista negra pero no pertenece a los grupos de usuario retorna
                        if (verificacionesComunesPatenteRobadaVista(patenteRobadaVista)) {
                            //aun no roto el bitmapRectF.bitmap
                            //patenteRobadaVista tiene un String URL en la imagen
                            //bitmapRectF trae un bitmap
                            posiblePatenteRobadaVista(patenteRobadaVista, bitmapRectF);
                        } else {
                            Log.v("groups","no pertenece a grupo: "+ listaGrupoGlobal +" "+patenteRobadaVista.getListaGrupoTipo());
                        }
                    }
                }

                int index = existPatenteInPatenteDistintasArray(arrayListPatentesDistintas, patenteEscaneada.getPatente());
                if(index == -1 && !patenteEscaneada.getPatente().equalsIgnoreCase("")) {
                    arrayListPatentesDistintas.add(patenteEscaneada.getPatente());
                }

                arrayListPatentesEscaneadas.set(indicePatente,patenteEscaneada);
                Log.v("patentesEscaneadas","patenteEscaneada: "+patenteEscaneada.getMarca());
                Log.v("patentesEscaneadas","patentesEscaneadas: "+ arrayListPatentesEscaneadas.get(indicePatente).getMarca());
                
                //buscarPosibleAutoClonado(patenteEscaneada, patenteListaNegra, bitmapRectF, indicePatente);

                //existe en los registros de coleccion patente robada aux
                if (tipoAmbas || patenteEscaneada.isBaseRobada() || patenteEscaneada.isBaseListaNegra() || patenteEscaneada.isBaseSoap() || (existeGrupo(listaGrupoGlobal, "BCI") && isUserTotem && patenteEscaneada.isBaseRobada() && patenteEscaneada.isBaseListaNegra())) {
                    CompletableFuture future = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        future = completableFutureGoogleOCR(patenteEscaneada, bitmapRectF.getCropBitmap());
                    }
                    if(future != null){
                        future.thenRun(() -> {
                            Log.v("patentesEscaneadas", "inicializa bag sdfs 3");
                            if(isEnviarSoloPatentesIgualesAGoogleOCRAPI && patenteEscaneada.getResponseApiGoogle() != null && patenteEscaneada.getResponseApiGoogle().equalsIgnoreCase(patenteEscaneada.getPatente())) {
                                setComunaFromAPI(patenteEscaneada, bitmapRectF, true, false, indicePatente);
                            } else if(isEnviarSoloPatentesIgualesAGoogleOCRAPI == false){
                                setComunaFromAPI(patenteEscaneada, bitmapRectF, true, false, indicePatente);
                            }
                        });
                    } else {
                        Log.v("patentesEscaneadas", "inicializa bag sdfs 3");
                        setComunaFromAPI(patenteEscaneada, bitmapRectF, true, false, indicePatente);
                    }
                    return;
                }

                Log.v("patentesEscaneadas", "aaaaaaaaaa");
                /**
                 *  Si pasa los if anteriores es una patente escaneada normal(no tiene encargo ni es una patente en lista negra)
                 */
                if(!existeSetRafaga(patenteEscaneada)){
                    crearSetRafaga(patenteEscaneada, indicePatente);
                }

                if(!existeRafaga(patenteEscaneada)) {
                    crearRafaga(patenteEscaneada, indicePatente);
                }

                Log.v("patenteRobadaVista", "no hay patente en base de datos que coincida con patente: "+patenteEscaneada.getPatente());

                Log.v("patenteRobadaVista","patente escaneada normal");
                //llamar al hashmap dequeue
                deleteKeyHashMapPatente(patenteEscaneada.getPatente());

            }
        });
    }

    public CompletableFuture<Void> completableFutureGoogleOCR(PatenteEscaneada patenteEscaneada, Bitmap bitmap){
        // Crear un CompletableFuture para realizar la llamada a la API
        CompletableFuture<Void> future = null;
        if(isConsultasApiGoogleActivado && modelType.equalsIgnoreCase("1")){
            Log.v("APIGOOGLE","despues del run");

            Log.v("APIGOOGLE","patente lista negra NOOO es nula");
            // Creación de imagen64 para enviar a endpoint.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            // Fin de imagen64
            Log.v("APIGOOGLE","imagen64");

            OkHttpClient client = new OkHttpClient();

            // Construir el cuerpo del JSON con el base64 de la imagen
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("img", encodedImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());

            Request request = new Request.Builder()
                    .url("https://api.safebywolf.link/textImage")
                    .post(body)
                    .build();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                future = CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            okhttp3.Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                // Procesar el JSON de respuesta
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    String result = jsonResponse.getString("result");
                                    patenteEscaneada.setResponseApiGoogle(result);
                                    Log.v("APIGOOGLE","Respuesta de patente:"+result);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
        return future;
    }


    //Método que realiza la actualización del total de patentes escaneadas por el usuario
    public void updateTotalPatenteEscaneadaPorUsuario(int total) {
        CurrentDate currentDate = new CurrentDate(new Date());
        if (listaGrupoGlobal.size() > 0) {
            Log.v("contador", "size " + listaGrupoGlobal.size());
            for (String grupo : listaGrupoGlobal) {
                //respaldo //por usuario por fecha
                Log.v("contador", "emailuser: " + emailUsuarioFirebase);
                Log.v("contador", "grupo: " + grupo);
                Log.v("contador", "current date: " + currentDate.getFecha());

                //actualiza el contador total de una patente escaneada por grupo (costo de escritura 1)
                DocumentReference refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo);
                refTotalFecha.update("total", FieldValue.increment(total));

                //actualiza el contador total de una patente escaneada por grupo por fecha (costo de escritura 1)
                refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORFECHA).document(currentDate.getFecha());
                refTotalFecha.update("total", FieldValue.increment(total));

                //actualiza el contador total de una patente escaneada por grupo por usuario (costo de escritura 1)
                refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase);
                refTotalFecha.update("total", FieldValue.increment(total));

                //actualiza el contador total de una patente escaneada por grupo por usuario por fecha (costo de escritura 1)
                refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha());
                refTotalFecha.update("total", FieldValue.increment(total));
            }
        }

        //actualiza el total de una patente escaneada por usuario
        DocumentReference refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase);
        refTotalFecha.update("total", FieldValue.increment(total));

        //actualiza el total de una patente escaneada por usuario por fecha
        refTotalFecha = db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha());
        refTotalFecha.update("total", FieldValue.increment(total));

        //actualiza el total general de patentes escaneadas por fecha
        refTotalFecha = db.collection(Referencias.TOTAL).document(Referencias.TOTALPATENTEESCANEADA).collection(Referencias.PORFECHA).document(currentDate.getFecha());
        refTotalFecha.update("total", FieldValue.increment(total));
    }

    public void updateTotalPatenteEscaneada(int total) {
        //actualiza el total general de patentes escaneadas
        DocumentReference ref = db.collection(Referencias.TOTAL).document(Referencias.TOTALPATENTEESCANEADA);
        ref.update("total", FieldValue.increment(total));
    }

    public boolean compruebaSiPatenteTieneCaracteresConProbabilidadDeFalla(String patente, int indexBuscarHaciaDelante){
        for(int i = indexBuscarHaciaDelante; i < this.letrasConProbabilidadDeFalla.length(); i++){
            String letra = String.valueOf(patente.charAt(i));
            if(this.letrasConProbabilidadDeFalla.contains(letra.toUpperCase())){
                return true;
            }
        }
        return false;
    }

    public Map<String,Integer> insertaMap(String patenteAux, Map<String, Integer> mapPatentes, int cantidad){
        if(mapPatentes.containsKey(patenteAux)){
            mapPatentes.put(patenteAux,mapPatentes.get(patenteAux)+cantidad);
        } else {
            mapPatentes.put(patenteAux,cantidad);
        }
        return mapPatentes;
    }

    public ArrayList<String> cambiaLetra(String patente, int indiceLetra){
        String patenteAux = null;
        char[] patenteCharArray = patente.toCharArray();
        ArrayList<String> patentesAux = new ArrayList<>();
        if(patenteCharArray[indiceLetra] == 'H'){
            //caso 1 era una W
            patenteCharArray[indiceLetra] = 'W';
            patenteAux = new String(patenteCharArray);
            patentesAux.add(patenteAux);
        } else if(patenteCharArray[indiceLetra] == 'V'){
            patenteCharArray[indiceLetra] = 'W';
            patenteAux = new String(patenteCharArray);
            patentesAux.add(patenteAux);

            patenteCharArray[indiceLetra] = 'Y';
            patenteAux = new String(patenteCharArray);
            patentesAux.add(patenteAux);
        } else if(patenteCharArray[indiceLetra] == 'C'){
            //caso 3 era una G
            patenteCharArray[indiceLetra] = 'G';
            patenteAux = new String(patenteCharArray);
            patentesAux.add(patenteAux);
        } else if(patenteCharArray[indiceLetra] == 'X' || patenteCharArray[indiceLetra] == 'R'){
            //caso 4 era una K
            patenteCharArray[indiceLetra] = 'K';
            patenteAux = new String(patenteCharArray);
            patentesAux.add(patenteAux);
        }
        return patentesAux;
    }

    public ArrayList<String> arrayCombinacionesDePatente(String patente, ArrayList<String> patentesArrayList){
        //recorre en profundidad
        int indiceLetraEnAnchura = 0;
        String patenteRaiz;
        int indicePatenteRaiz;
        ArrayList<Integer> indice = new ArrayList<>();
        ArrayList<String> patentes = new ArrayList();
        patentes.add(patente);
        indice.add(0);
        int i = 0;
        while(i < patentes.size()){
            patenteRaiz = patentes.get(i);
            indicePatenteRaiz = indice.get(i);
            indiceLetraEnAnchura = indicePatenteRaiz;
            Log.v("patentesSize", "patentesSize primer while");
            while(compruebaSiPatenteTieneCaracteresConProbabilidadDeFalla(patenteRaiz,indicePatenteRaiz)){

                Log.v("patentesSize", "patentesSize segundo while");
                if(indiceLetraEnAnchura == -1){
                    break;
                }
                indiceLetraEnAnchura = retornaIndiceCaracterConProbabilidadDeFalla(patenteRaiz, indiceLetraEnAnchura);
                if(indiceLetraEnAnchura == -1){
                    break;
                }
                ArrayList<String> nuevasPatentes = cambiaLetra(patenteRaiz, indiceLetraEnAnchura);
                for(String patenteNueva:nuevasPatentes){
                    patentes.add(patenteNueva);
                    indice.add(indiceLetraEnAnchura);
                }
                indiceLetraEnAnchura++;
                indicePatenteRaiz = indiceLetraEnAnchura;
                if(indiceLetraEnAnchura >= patenteRaiz.length() || indicePatenteRaiz == -1){
                    break;
                }
            }
            i++;
        }
        Log.v("patentesSize","patentesSize: "+patentes.size());
        Log.v("patentesSize","array to string: "+patentes.toString());
        return patentes;
    }

    public int cantidadDeRepetidasPatenteEnArray(String patente, ArrayList<String> patentesArrayList){
        int contador=0;
        for(String patenteAux: patentesArrayList){
            if(patente.equalsIgnoreCase(patenteAux)){
                contador++;
            }
        }
        return contador;
    }

    public int retornaIndiceCaracterConProbabilidadDeFalla(String patente, int indexBuscarHaciaDelante){
        for(int i = indexBuscarHaciaDelante; i < patente.length(); i++){
            String letraPatente = String.valueOf(patente.charAt(i));
            if(this.letrasConProbabilidadDeFalla.contains(letraPatente.toUpperCase())){
                return patente.indexOf(letraPatente.toUpperCase());
            }
        }
        return -1;
    }

    public String clasificarPatente(String patenteOriginal, ArrayList<String> patentesArrayList){
        ArrayList<String> patentesArrayListClone = (ArrayList<String>) patentesArrayList.clone();

        if(!compruebaSiPatenteTieneCaracteresConProbabilidadDeFalla(patenteOriginal,0)
                || patentesArrayListClone.size() == 0){
            return patenteOriginal;
        }

        String posiblePatenteCorrecta = patenteOriginal;
        int patenteMayorVecesRepetida = 0;
        int vecesRepetida = 0;

        //Retorna array list con todas las combinaciones de patentes posibles
        ArrayList<String> arrayCombinacionesDePatente = arrayCombinacionesDePatente(patenteOriginal,patentesArrayListClone);

        //Comprueba cuantas veces se ha visto una patente
        for (String combinacionDePatente : arrayCombinacionesDePatente){
            for (String patente : patentesArrayList){
                if(patente.equalsIgnoreCase(combinacionDePatente)){
                    vecesRepetida++;
                }
            }
            if(vecesRepetida > patenteMayorVecesRepetida){
                posiblePatenteCorrecta = combinacionDePatente;
                patenteMayorVecesRepetida = vecesRepetida;
            }
        }
        return posiblePatenteCorrecta;
    }

    public boolean existeRafaga(PatenteEscaneada patenteEscaneada) {
        if (((Long.parseLong(new CurrentDate(new Date()).getLongTime()) - patenteEscaneada.getLastHeadTime())  < tiempoRafagaMilis)
        ) {
            Log.v("crearafaga", "existe rafaga true");
            return true;
        }
        Log.v("crearafaga", "NOOOOO existe rafaga true");
        return false;
    }

    public boolean existeSetRafaga(PatenteEscaneada patenteEscaneada) {
        if ((Long.parseLong(new CurrentDate(new Date()).getLongTime()) - (patenteEscaneada.getLastSetHeadTime()) < tiempoSetRafagaMilis)
        ) {
            Log.v("crearafaga", "existe SET rafaga true");
            return true;
        }
        Log.v("crearafaga", "NOOOOO existe SET rafaga true");
        return false;
    }

    public void touchListener(){
        horaTouchListener=new CurrentDate(new Date());
        view = findViewById(R.id.linearLayoutContetMonitoreo);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                brilloDePantallaNormal();
                return true;
            }
        });
    }

    public void brilloDePantallaNormal(){
        horaTouchListener = new CurrentDate(new Date());
        isModoAhorroDeEnergiaActivado = false;
        Utils.setBrilloDePantalla(-1, DetectorActivity.this);
    }

    public void disminuirBrillo(){
        isModoAhorroDeEnergiaActivado = true;
        Utils.setBrilloDePantalla(0, DetectorActivity.this);
        if(fromBackground == false){
            textToSpeech(speechModoAhorroDeEnergiaActivado, false);
        }
    }

    public void actionButtonActivarAhorroDeEnergia(boolean hablar){
        isButtonAhorroDeEnergiaActivado = true;
        if(hablar) {
            textToSpeech(speechButtonModoAhorroDeEnergiaActivado, false);
        }

        floatingactionbuttonBattery.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        floatingactionbuttonBattery.setImageDrawable(getResources().getDrawable(R.drawable.energiara));
    }

    public void actionButtonDesactivarAhorroDeEnergia(boolean hablar){
        isModoAhorroDeEnergiaActivado = false;
        Log.v("touchlistener","clicked");
        horaTouchListener=new CurrentDate(new Date());
        isButtonAhorroDeEnergiaActivado = false;
        //desactivar
        if(hablar){
            textToSpeech(speechButtonModoAhorroDeEnergiaDesactivado, false);
        }

        floatingactionbuttonBattery.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        floatingactionbuttonBattery.setImageDrawable(getResources().getDrawable(R.drawable.energiar));
    }

    public void timerTaskPuntoVerdePatente() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run() {
                // time ran out.
                linearLayoutPuntoPatente.setVisibility(View.INVISIBLE);
                timer.cancel();
            }
        }, 1000);
        linearLayoutPuntoPatente.setVisibility(View.VISIBLE);
    }


    /*
    public void updateAdapterListViewPatentes() {
        ArrayList<PatenteEscaneada> auxPatentesEscaneadas = new ArrayList<>(patentesEscaneadas);
        Collections.reverse(auxPatentesEscaneadas);
        adapterPatenteEscaneada = new AdapterPatentesEscaneadas(DetectorActivity.this, auxPatentesEscaneadas);
        listViewPatenteEscaneada.setAdapter((ListAdapter) adapterPatenteEscaneada);
    }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            logout();
            return true;
        }
        if (id == R.id.terminosYCondiciones) {
            terminosYCondiciones();
            return true;
        }
        if (id == R.id.contacto) {
            contacto();
            return true;
        }
        if (id == R.id.perfil) {
            perfil();
            return true;
        }
        if (id == R.id.about) {
            about();
            return true;
        }
        if (id == R.id.novedades) {
            novedades();
            return true;
        }
        if (id == R.id.noticias) {
            noticias();
            return true;
        }
        if (id == R.id.comunas) {
            comunas();
            return true;
        }
        if (id == R.id.tutorial) {
            tutorial();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Utils.logout(db,DetectorActivity.this);

        if (doAsynchronousTaskUbicacion != null) {
            doAsynchronousTaskUbicacion.cancel();
            doAsynchronousTaskUbicacion = null;
        }
        if (doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto != null) {
            doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto.cancel();
            doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto = null;
        }

    }

    public void verificarVersionApp() {
        //verifica la version de la aplicacion (costo de lectura 1)
        db.collection(Referencias.VERSION).document("appFull").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("versionapp", "version obtenida");
                        VersionNueva versionNueva = document.toObject(VersionNueva.class);
                        Utils.guardarValorString(DetectorActivity.this, Referencias.VERSION, document.getString("version"));
                        String versionName = BuildConfig.VERSION_NAME;
                        if (!BuildConfig.FLAVOR.equalsIgnoreCase("knox") && !versionNueva.getVersion().equalsIgnoreCase(versionName) && !BuildConfig.BUILD_TYPE.equals("debug")) {
                            ejecutarLogoInicialActivity();
                            finish();
                        }
                    }
                }
            }
        });
    }

    private void ejecutarLogoInicialActivity() {
        Intent intent = new Intent(this, LogoInicial.class);
        startActivity(intent);
        finish();
    }

    public void callbackOnConnected() {
        if (this.mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(DetectorActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(DetectorActivity.this)
                    .addOnConnectionFailedListener(DetectorActivity.this).build();
            mGoogleApiClient.connect();
            Log.v("callbackOnConnected","mGoogleApiClient es nulo, se crea una nueva instancia");
        } else {
            Log.v("callbackOnConnected","mGoogleApiClient NO es nulo, deberia estar creado el servicio de ubicacion");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mGoogleApiClient == null){
            return;
        }
        Log.v("location", "se ejecuto on connected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.v("locationnx", "LocationSettingsStatusCodes.SUCCESS");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.v("locationnx", "LocationSettingsStatusCodes.RESOLUTION_REQUIRED");

                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(DetectorActivity.this, REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.v("locationnx", "SETTINGS_CHANGE_UNAVAILABLE");

                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        mGoogleApiClient = null;
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("locationnx", "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("locationnx", "onConnectionFailed");

    }

    public void getLastLocation() {
        //if (mMap != null) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClientObtieneUltimaUbicacion = LocationServices.getFusedLocationProviderClient(DetectorActivity.this);

        fusedLocationProviderClientObtieneUltimaUbicacion.getLastLocation()
                .addOnSuccessListener(DetectorActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            currentLocation = location;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = new Date(currentLocation.getTime());
                            String hora = dateFormat.format(date);
                            Log.v("location", hora + "");

                            //velocidad en m/s
                            Log.v("speddlocation",""+location.getSpeed());
                            velocidad = location.getSpeed();
                            //30km/h = 8.333m/s
                            //40km/h = 11.11m/s
                            //50km/h = 13.889m/s
                            //60km/h = 16.667m/s
                            //80km/h = 22.222m/s
                            //100km/h = 27.778m/s
                            //120km/h = 33.333m/s
                            /*
                            if(velocidad){}
                             */

                            if(!isUserTotem) {
                                drawCircleLocation();
                            }

                            //latitud = location.getLatitude();
                            //longitud = location.getLongitude();
                        }
                    }
                });
        //}
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        // It is good practice to check that we received the proper sensor event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Convert the rotation-vector to a 4x4 matrix.
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, orientationVals);

            // Optionally convert the result from radians to degrees
            orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
            orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
            orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);

            Log.v(" Yaw: ", "or0" + orientationVals[0] + "\n Pitch: "
                    + orientationVals[1] + "\n Roll (not used): "
                    + orientationVals[2]);
            updateCamera(orientationVals[0]);
        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float ambient_temperature = event.values[0];
            Log.v("temparturacpu", "Ambient Temperature: " + String.valueOf(ambient_temperature) + "C°");
            //temperaturelabel.setText("Ambient Temperature:\n " + String.valueOf(ambient_temperature) + getResources().getString(R.string.celsius));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateCamera(float bearing) {
        Log.v("sensorchanged", bearing + "Sensor");
        if (googleMap != null) {
            if (centrarCamera) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitud, longitud))
                        .bearing(bearing).zoom(zoomGoogleMaps).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }


    /**
     * Generates a list of acceptable preview sizes.  Preview sizes are not acceptable if there is
     * not a corresponding picture size of the same aspect ratio.  If there is a corresponding
     * picture size of the same aspect ratio, the picture size is paired up with the preview size.
     * <p/>
     * This is necessary because even if we don't use still pictures, the still picture size must be
     * set to a size that is the same aspect ratio as the preview size we choose.  Otherwise, the
     * preview images may be distorted on some devices.
     */
    /*
    private List<SizePair> generateValidPreviewSizeList() {
        Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = camera.getParameters();
        List<android.hardware.Camera.Size> supportedPreviewSizes =
                parameters.getSupportedPreviewSizes();
        List<android.hardware.Camera.Size> supportedPictureSizes =
                parameters.getSupportedPictureSizes();
        List<SizePair> validPreviewSizes = new ArrayList<>();
        this.previewSizeHeight = 0;
        this.previewSizeWidth = 0;
        for (android.hardware.Camera.Size previewSize : supportedPreviewSizes) {
            float previewAspectRatio = (float) previewSize.width / (float) previewSize.height;
            // By looping through the picture sizes in order, we favor the higher resolutions.
            // We choose the highest resolution in order to support taking the full resolution
            // picture later.
            for (android.hardware.Camera.Size pictureSize : supportedPictureSizes) {
                float pictureAspectRatio = (float) pictureSize.width / (float) pictureSize.height;
                if (Math.abs(previewAspectRatio - pictureAspectRatio) < ASPECT_RATIO_TOLERANCE) {
                    if (previewSize.width >= this.previewSizeWidth && previewSize.height >= this.previewSizeHeight) {
                        this.previewSizeWidth = previewSize.width;
                        this.previewSizeHeight = previewSize.height;
                        this.pictureSizeWidht = pictureSize.width;
                        this.pictureSizeHeight = pictureSize.height;
                    }
                    validPreviewSizes.add(new SizePair(previewSize, pictureSize));
                    Log.v("sizeCam", "Preview: " + previewSize.width + "/" + previewSize.height + " - Picture: " + pictureSize.width + "/" + pictureSize.height);
                    break;
                }
            }
        }
        Log.v("sizeMio", "Preview: " + previewSizeWidth + "/" + previewSizeHeight + "- Picture: " + pictureSizeWidht + "/" + pictureSizeHeight);
        setSizeCamera();
        return validPreviewSizes;
    }
     */

    private static class SizePair {
        private Size mPreview;
        private Size mPicture;

        public SizePair(android.hardware.Camera.Size previewSize,
                        android.hardware.Camera.Size pictureSize) {
            mPreview = new Size(previewSize.width, previewSize.height);
            if (pictureSize != null) {
                mPicture = new Size(pictureSize.width, pictureSize.height);
            }
        }

        public Size previewSize() {
            return mPreview;
        }

        @SuppressWarnings("unused")
        public Size pictureSize() {
            return mPicture;
        }
    }

    //muestra Camera Preview
    public void cameraFullScreen() {
        frameLayoutContainer.setVisibility(View.VISIBLE);
        //cambia icono a camara
        if(isUserTotem) {
            buttonSwitchCameraOrMap.setImageResource(R.drawable.camera);
        } else {
            buttonSwitchCameraOrMap.setImageResource(R.drawable.location);
        }

        //muestra la barra de zoom
        if(!isZoomAutomaticoActivado){
            linearLayoutContenedorSeekbar.setVisibility(View.VISIBLE);
        }
        //buttonZoom.show();
        buttonZoom.setVisibility(View.VISIBLE);

        //esconde boton de centrar camara
        buttonCentrar.hide();

        //detenerGoogleMaps();
        //detenerSensorRotacion();
    }

    //esconde Camera Preview
    public void cemeraSmallSize() {
        frameLayoutContainer.setVisibility(View.GONE);

        //cambia icono a camara
        buttonSwitchCameraOrMap.setImageResource(R.drawable.camera);

        //esconde la barra de zoom
        linearLayoutContenedorSeekbar = findViewById(R.id.linearLayoutContenedorSeekbar);
        linearLayoutContenedorSeekbar.setVisibility(View.GONE);
        //buttonZoom.hide();
        buttonZoom.setVisibility(View.GONE);
        //esconde boton de centrar camara
        buttonCentrar.show();

        //iniciarGoogleMap();
        iniciarSensorRotacion();
    }

    public void escondeMuestraPreviewCamara() {
        if (fullScreenCamera == true) {
            Log.v("permissionspreview", "muestra camara");
            muestraPreviewCamara();
            textToSpeech("Cámara", false);
        } else {
            Log.v("permissionspreview", "muestra google maps");
            escondePreviewCamara();
            textToSpeech("Mapa", false);
        }
    }

    public void muestraPreviewCamara() {
        fullScreenCamera = !fullScreenCamera;
        cameraFullScreen();
    }

    public void iniciarSensorRotacion() {
        sensorRotacion = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(sensorRotacion != null){
            mSensorManager.registerListener(this, sensorRotacion, SensorManager.SENSOR_DELAY_NORMAL);//SensorManager.SENSOR_DELAY_Fastest
        } else {
            Log.v("temparturacpu", "sensorRotacion no soportadfo");
        }
    }

    public void detenerSensorRotacion(){
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(DetectorActivity.this);
        }

        if (sensorRotacion != null) {
            sensorRotacion = null;
        }
    }

    public void detenerGoogleMaps() {
        if (this.googleMap == null) {
            return;
        }
        this.googleMap.clear();
        destroyMGoogleApiClient();

        mapFragment.onStop();
        ViewGroup parentView = (ViewGroup) mapFragment.getView().getParent();
        parentView.removeView(mapFragment.getView());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mapFragment);
        transaction.commit();
        mapFragment = null;
        googleMap = null;
    }

    private void iniciarGoogleMap() {
        if(mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mapFragment, mapFragment);
            transaction.commitNow();

            compassButton = mapFragment.getView().findViewWithTag("GoogleMapCompass");//to access the compass button
            if (compassButton != null) {
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_END);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, buttonCentrar.getId());
                rlp.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                rlp.rightMargin = 50;
                rlp.topMargin = 250;
            }

            mapFragment.getMapAsync(this);
        }
    }

    //si el usuario desactiva el modo totem de forma manual se debe activar todo, pues todo vuelve a la normalidad
    //google maps es requerido
    public void desactivarTotem() {
        isTotemActivo = false;
        isActivarTotem = false;
        textViewModoTotem.setVisibility(View.GONE);
        frameLayoutContainer.setVisibility(View.VISIBLE);
        buttonZoom.show();
        if(isTotemEnModoReposo){
            recreate();
            isTotemEnModoReposo = false;
        }

        this.linearLayoutNegro.setVisibility(View.GONE);
        Utils.setBrilloDePantalla(-1, DetectorActivity.this);
    }

    //modo totem boton leyendo patentes
    public void activarTotem() {
        isTotemActivo = true;
        isActivarTotem = true;
        textViewModoTotem.setVisibility(View.VISIBLE);
        textViewModoTotem.setText("TOTEM ESCANEANDO PATENTES");
        linearLayoutPatente.setVisibility(View.GONE);
        //linearLayoutContenedorSeekbar.setVisibility(View.VISIBLE);
        buttonZoom.show();
        detenerLocationListener();
        //detenerGoogleMaps();
        inicializarContadorDeTotales();
        Utils.setBrilloDePantalla(0, DetectorActivity.this);
        buttonCentrar.hide();
        frameLayoutContainer.setVisibility(View.INVISIBLE);
        linearLayoutNegro.setVisibility(View.VISIBLE);
        linearLayoutNegro.setBackground(getResources().getDrawable(R.drawable.transparent_without_border));
        if(isTotemEnModoReposo){
            recreate();
            isTotemEnModoReposo = false;
        }
    }

    public void totemEnModoReposo() {
        isTotemEnModoReposo = true;
        isTotemActivo = false;
        textViewModoTotem.setVisibility(View.VISIBLE);
        textViewModoTotem.setText("TOTEM EN MODO REPOSO");
        linearLayoutNegro.setVisibility(View.VISIBLE);
        linearLayoutNegro.setBackground(getResources().getDrawable(R.drawable.black));
        linearLayoutPatente.setVisibility(View.GONE);
        //linearLayoutContenedorSeekbar.setVisibility(View.GONE);
        buttonZoom.hide();
        detenerLocationListener();
        //detenerGoogleMaps();
        Utils.setBrilloDePantalla(0, DetectorActivity.this);
        stopHandlerInferenceCamera();
        cameraClose();
    }

    public int getBrightness() {
        //Get the content resolver
        ContentResolver cResolver = getContentResolver();
        int brightness=-1;
        try {
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        Log.v("currentBrightness",String.valueOf(brightness));
        return brightness;
    }

    public void detenerLocationListener() {
        if (fusedLocationProviderClientObtieneUltimaUbicacion != null) {
            if (this.locationCallback != null) {
                this.fusedLocationProviderClientObtieneUltimaUbicacion.removeLocationUpdates(locationCallback);
                Log.v("locationCallback", "locationCallback detenido correctamente");
            } else {
                Log.v("locationCallback", "locationCallback es nulo");
            }

        } else {
            Log.v("locationCallback", "fusedLocationProviderClientObtieneUltimaUbicacion es nulo");
        }

        if (intentLocationService != null) {
            stopService(intentLocationService);
            intentLocationService = null;
        }

        detenerSensorRotacion();
    }

    public void destroyMGoogleApiClient() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.disconnect();
            this.mGoogleApiClient = null;
        }
    }

    private BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Are we charging / charged?
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            // How are we charging?
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


            Log.v("usbCharge", String.valueOf(usbCharge));
            Log.v("usbChargeacCharge", String.valueOf(acCharge));

            batteryLevel = (float) (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
            if(anteriorBatteryLevel == 0){
                anteriorBatteryLevel = batteryLevel;
            }

            if(batteryLevel % 20 == 0){
                //si la bateria anterior es mayor quiere decir que se esta descargando
                if(anteriorBatteryLevel > batteryLevel){
                    if(batteryLevel > 20) {
                        anteriorBatteryLevel = batteryLevel;
                    }
                    textToSpeech("Batería en un "+(int)(batteryLevel) +"%"+" y disminuyendo.",false);
                }
            }
            if(batteryLevel <= 20 && batteryLevel % 5 == 0){
                if(anteriorBatteryLevel > batteryLevel){
                    anteriorBatteryLevel = batteryLevel;
                    textToSpeech("Batería en un "+(int)(batteryLevel)+"% y disminuyendo."+" Favor conecte a la corriente.",false);
                }
            }

            batteryTemp = (float) (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
            if (isCharging) {
                Log.v("temperatures", "cargando");
                textViewCargandoString.setText("cargando");
            } else {
                Log.v("temperatures", "NO cargando");
                textViewCargandoString.setText("No cargando");
            }

            Log.v("temperatures", String.valueOf(batteryLevel + "%"));
            textViewTBateriaNumero.setText(String.valueOf(batteryTemp));
            textViewNivelBateriaNumero.setText(String.valueOf(batteryLevel + " %"));
            temperaturaCPU();
        }
    };

    public void updateStatusTotem() {
        Log.v("configTotem","updateTotem");
        if (textViewCargandoString == null || textViewTBateriaNumero == null || textViewTCPUNumero == null || configurationTotem == null || textViewNivelBateriaNumero == null || this.getLastKnownLatitudString() == null || this.getLastKnownLongitudString() == null) {
            Log.v("configTotem","algo es nulo");
            return;
        }
        CurrentDate currentDate = new CurrentDate(new Date());

        //setea totem como activo o inactivo
        float currentTemperaturaCPU = Float.parseFloat(this.textViewTCPUNumero.getText().toString().replace(" °C", ""));
        float currentTemperaturaBateria = Float.parseFloat(textViewTBateriaNumero.getText().toString().replace(" °C", ""));
        float currentNivelBateria = Float.parseFloat(textViewNivelBateriaNumero.getText().toString().replace(" %", ""));

        boolean activo;
        boolean inactivoPorTemperatura;

        //si hora actual es mayor al limite de hora maximo o hora actual es menor a hora inicio significa que el totem debe estar desactivado
        if (Utils.horaActualEsMayorAHoraLimite(currentDate.getHora(), configurationTotem.getHoraTermino())
                || Utils.horaActualEsMenorAHoraInicio(currentDate.getHora(), configurationTotem.getHoraInicio())) {
            activo = false;
            inactivoPorTemperatura = false;
            Log.v("configTotem", "totem inactivo por estar fuera del rango de horas de trabajo");
            //si temperatura de CPU es mayor a la temperatura máxima, se debe desactivar dispositivo
        } else if (currentTemperaturaCPU > Float.parseFloat(configurationTotem.getLimiteMaximoDeTemperaturaCPU())
                || currentTemperaturaBateria > Float.parseFloat(configurationTotem.getLimiteMaximoDeTemperaturaBateria())) {
            activo = false;
            inactivoPorTemperatura = true;
            if(temperaturaBateriaAntesDelReposo.equalsIgnoreCase("-1")){
                temperaturaBateriaAntesDelReposo = String.valueOf(currentTemperaturaBateria);
            }
            Log.v("configTotem", "totem inactivo por exceder temperatura máxima");
        }
        //si temperatura de CPU esta en el rango de menor a maximo y mayor a minimo comprobar si totem es distinto de null y comprobar si esta incativo por temperatura para no activar hasta que este bajo la temperatura minima de CPU
        else if ((currentTemperaturaCPU <= Float.parseFloat(configurationTotem.getLimiteMaximoDeTemperaturaCPU()) && currentTemperaturaCPU >= Float.parseFloat(configurationTotem.getLimiteMinimoDeTemperaturaCPU()))
                || (currentTemperaturaBateria <= Float.parseFloat(configurationTotem.getLimiteMaximoDeTemperaturaBateria()) && currentTemperaturaBateria >= Float.parseFloat(configurationTotem.getLimiteMinimoDeTemperaturaBateria()))
        ) {
            //si esta incativo por temperatura no activar hasta que este bajo la temperatura minima de CPU
            if (this.totem != null && this.totem.isInactivoPorTemperatura() == true) {
                activo = false;
                inactivoPorTemperatura = true;
                Log.v("configTotem", "totem dentro del rango de temperatura pero inactivo por exceder temperatura máxima");
            }
            //activar porque no esta inactivo por temperatura
            else {
                temperaturaBateriaAntesDelReposo = "-1";
                activo = true;
                inactivoPorTemperatura = false;
                Log.v("configTotem", "totem dentro del rango de temperatura activo por no estar inactivo por temperatura");
            }
        }
        //si es bajo la temperatura minima activar
        else {
            activo = true;
            inactivoPorTemperatura = false;
            Log.v("configTotem", "totem activo por estar bajo la temperatura");
        }

        totem = new Totem(this.idUsuarioFirebase, this.emailUsuarioFirebase, this.listaGrupoGlobal, String.valueOf(currentTemperaturaCPU), String.valueOf(currentTemperaturaBateria), String.valueOf(currentNivelBateria), currentDate.getHora(), activo, inactivoPorTemperatura, this.getLastKnownLatitudString(), this.getLastKnownLongitudString(), this.textViewCargandoString.getText().toString(), currentDate.getFecha(), currentDate.getLongTime(), this.usbCharge, this.acCharge, lastPatenteLeida, false, ipUsuarioFirebase, tagUsuarioFirebase, comunaUserFromDB, temperaturaBateriaAntesDelReposo);

        activaODesactivaTotem();

        //actualiza los campos de un totem (costo de escritura 1)
        this.db.collection(Referencias.TOTEM).document(this.emailUsuarioFirebase).set(totem, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                countUbicacionTotem = 0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(countUbicacionTotem > tiempoMaximoSinActualizarEstadoTotemMinutos){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetectorActivity.this, "Totem no pudo actualizar su estado en los últimos: "+countLecturas+" minutos, se reinicia", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ejecutarLogoInicialActivity();
                    finish();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetectorActivity.this, "Totem no pudo actualizar su estado en los últimos: "+countUbicacionTotem+" minutos", Toast.LENGTH_SHORT).show();
                    }
                });

                countUbicacionTotem++;
            }
        });
    }

    public void temperaturaCPU() {
        Process process;
        try {
            String[] list = {"cat sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
                    "cat sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
                    "cat sys/class/thermal/thermal_zone0/temp",
                    "cat sys/class/i2c-adapter/i2c-4/4-004c/temperature",
                    "cat sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
                    "cat sys/devices/platform/omap/omap_temp_sensor.0/temperature",
                    "cat sys/devices/platform/tegra_tmon/temp1_input",
                    "cat sys/kernel/debug/tegra_thermal/temp_tj",
                    "cat sys/devices/platform/s5p-tmu/temperature",
                    "cat sys/devices/virtual/thermal/thermal_zone0/temp",
                    "cat sys/class/hwmon/hwmon0/device/temp1_input",
                    "cat sys/devices/platform/s5p-tmu/curr_temp",
                    "cat sys/htc/cpu_temp",
                    "cat sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/ext_temperature",
                    "cat sys/devices/platform/tegra-tsensor/tsensor_temperature"};
            for (int i = 0; i < list.length; i++) {
                process = Runtime.getRuntime().exec(list[i]);
                Log.v("tempcp", list[i]);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                if (line != null) {
                    float temp = Float.parseFloat(line);
                    temp = temp / 1000.0f;
                    Log.v("tempcpu", "" + temp);
                    this.textViewTCPUNumero.setText(String.valueOf(temp + ""));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("tempcpux", e.getMessage());
        }
    }

    public void dialogoNotificacionUsuarioEnGrupo(String grupoNotificacion, String tipoGrupoNotificacion, NotificacionUsuarioEnGrupo notificacionUsuarioEnGrupo) {
        new AlertDialog.Builder(DetectorActivity.this)
                .setTitle("Solicitud de ingreso a grupo\n")
                .setMessage("Quieres pertenecer a grupo: " + "\n\n''" + grupoNotificacion + "''")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //si grupos usuario firebase solo tiene el elemento sin grupo se añade nuevo grupo y se saca sin grupo
                        for (String grupo : setGruposUsuarioFirebase){
                            if (grupo.equalsIgnoreCase("SIN GRUPO")){
                                setGruposUsuarioFirebase.remove("SIN GRUPO");
                            }
                        }

                        //se modifica notificacion se cambia aceptado a true
                        notificacionUsuarioEnGrupo.setAceptado("TRUE");

                        //se actualiza la notificacion de ingreso a grupo de un usuario (costo de escritura 1)
                        db.collection(Referencias.NOTIFICACIONUSUARIOENGRUPO).document(notificacionUsuarioEnGrupo.getId()).set(notificacionUsuarioEnGrupo);

                        //crea contador por grupo por usuario
                        creaContadorPorGrupoPorUsuarioPorFecha(0, grupoNotificacion);

                        //grupos usuario firebase se agrega nuevo grupo
                        setGruposUsuarioFirebase.add(grupoNotificacion);
                        // Copio el Set a una List
                        listaGrupoGlobal.add(grupoNotificacion);

                        //se crea grupo tipo
                        Map<String, String> grupoTipo = new HashMap<>();
                        grupoTipo.put("nombre", grupoNotificacion);
                        grupoTipo.put("tipo", tipoGrupoNotificacion);

                        //se añade grupo tipo a gruposArrayFirebase
                        listaGrupoTipoGlobal.add(grupoTipo);

                        //se actualiza el campo grupo en la coleccion usuario (costo de escritura 1)
                        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("grupo", listaGrupoGlobal,
                                "grupos", listaGrupoTipoGlobal);
                        Toast.makeText(DetectorActivity.this, "Has aceptado la solicitud de ingreso a " + grupoNotificacion, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //se actualiza la notificacion de ingreso a grupo de un usuario (costo de escritura 1)
                        notificacionUsuarioEnGrupo.setAceptado("FALSE");
                        db.collection(Referencias.NOTIFICACIONUSUARIOENGRUPO).document(notificacionUsuarioEnGrupo.getId()).set(notificacionUsuarioEnGrupo);
                        Toast.makeText(DetectorActivity.this, "Has rechazado la solicitud de ingreso a " + grupoNotificacion, Toast.LENGTH_SHORT).show();
                    }
                })
                /*
                .setNeutralButton("Recordar mas tarde", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })*/
                .setInverseBackgroundForced(true)
                .show();
    }

    private boolean verificarSiGrupoPatentePerteneceGrupoUsuario(List<GroupType> grupoPatente){
        if(listaGrupoTipoGlobal == null){
            return false;
        }
        for (Map<String, String> grupoTipo : listaGrupoTipoGlobal){
            for (GroupType groupType : grupoPatente){
                if (grupoTipo.get("nombre").equalsIgnoreCase(groupType.getNombre()) &&
                        groupType.getTipo().equalsIgnoreCase("Grupo")){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verificarSiGrupoPatentePerteneceGrupoUsuarioComuna(List<GroupType> grupoPatente){
        if(listaGrupoTipoGlobal == null){
            return false;
        }
        for (Map<String, String> grupoTipo : listaGrupoTipoGlobal){
            for (GroupType grupoTipoPatente : grupoPatente){
                if (grupoTipo.get("nombre").equalsIgnoreCase(grupoTipoPatente.getNombre()) &&
                        grupoTipo.get("tipo").equalsIgnoreCase("Comuna") &&
                        grupoTipoPatente.getTipo().equalsIgnoreCase("Grupo")){
                    return true;
                }
            }
        }
        return false;
    }


    private boolean verificarSiComunaPatenteEsIgualAComunaUsuario(String ciudadPatente){
        if (listaGrupoTipoGlobal == null){
            return false;
        }

        Log.v("lala","ciudadPatente antes de normalizar: "+ciudadPatente);
        // Remuevo caracteres extraños ciudad patente
        String ciudadPatenteNorm = Utils.normalizarString(ciudadPatente);
        Log.v("lala","ciudadPatente despues de normalizar: "+ciudadPatenteNorm);

        for (Map<String, String> grupoTipo : listaGrupoTipoGlobal){
            // Remuevo caracteres extraños ciudad patente
            String nombreGrupo = Utils.normalizarString(grupoTipo.get("nombre"));
            Log.v("lala","nombreGrupo despues de normalizar: "+nombreGrupo);
            // Compruebo que la patente se encuentre
            if (nombreGrupo.equals(ciudadPatenteNorm)){
                return true;
            }
        }

        return false;
    }

    private void perfil(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, PerfilActivity.class), REQUEST_ABOUT);
    }

    private void about(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, AboutActivity.class), REQUEST_ABOUT);
    }

    private void novedades(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, NovedadesActivity.class), REQUEST_ABOUT);
    }

    private void noticias(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, NoticiasActivity.class), REQUEST_ABOUT);
    }


    private void comunas(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, ComunasActivity.class), REQUEST_ABOUT);
    }

    private void terminosYCondiciones(){
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, TerminosCondiciones.class), REQUEST_ABOUT);
    }

    private void contacto() {
        irOtraActividad = true;
        startActivityForResult(new Intent(DetectorActivity.this, ContactoActivity.class), REQUEST_ABOUT);
    }

    private void tutorial(){
        irOtraActividad = true;
        Intent intent = new Intent(DetectorActivity.this, TutorialDetectorActivity.class);
        intent.putExtra("fromDetectorActivity",true);
        startActivity(intent);
    }

    public PatenteEscaneada setPatenteEscaneadaConPatenteListaNegra(PatenteEscaneada patenteEscaneada, PatenteListaNegra patenteListaNegra, String tipo, List<String> listGrupos, List<GroupType> listaGrupoTipo) {

        //setea campo alertar
        patenteEscaneada.setAlertar(patenteListaNegra.isAlertar());

        patenteEscaneada.setTipo(tipo);

        boolean isAACH = false;
        if(patenteListaNegra.getDuenoDatabase() != null && patenteListaNegra.getDuenoDatabase().equalsIgnoreCase("AACH")){
            isAACH = true;
        }

        //cuando se crea una patente escaneada que no es robada ni lista negra se debe verificar que patente robada no sea nulo
        if (patenteListaNegra != null) {
            /*
            if (patenteListaNegra.getGrupo() != null) {
                patenteEscaneada.setGrupo(patenteListaNegra.getGrupo());
            }
             */
            if (patenteListaNegra.getMarca() != null) {
                if(isAACH){
                    patenteEscaneada.setMarca(patenteListaNegra.getMarca());
                } else if(patenteEscaneada.getMarca() == null){
                    patenteEscaneada.setMarca(patenteListaNegra.getMarca());
                }
            }
            if (patenteListaNegra.getModelo() != null) {
                if(isAACH){
                    patenteEscaneada.setModelo(patenteListaNegra.getModelo());
                } else if(patenteEscaneada.getModelo() == null){
                    patenteEscaneada.setModelo(patenteListaNegra.getModelo());
                }
            }
            if (patenteListaNegra.getColor() != null) {
                if(isAACH){
                    patenteEscaneada.setColor(patenteListaNegra.getColor());
                } else if(patenteEscaneada.getColor() == null){
                    patenteEscaneada.setColor(patenteListaNegra.getColor());
                }
            }
            if (patenteListaNegra.getAno() != null) {
                if(isAACH){
                    patenteEscaneada.setAno(patenteListaNegra.getAno());
                } else if(patenteEscaneada.getAno() == null){
                    patenteEscaneada.setAno(patenteListaNegra.getAno());
                }
            }
            if (patenteListaNegra.getMotor() != null) {
                if(isAACH){
                    patenteEscaneada.setMotor(patenteListaNegra.getMotor());
                } else if(patenteEscaneada.getMotor() == null){
                    patenteEscaneada.setMotor(patenteListaNegra.getMotor());
                }
            }
            if (patenteListaNegra.getChasis() != null) {
                if(isAACH){
                    patenteEscaneada.setChasis(patenteListaNegra.getChasis());
                } else if(patenteEscaneada.getChasis() == null){
                    patenteEscaneada.setChasis(patenteListaNegra.getChasis());
                }
            }
            if (patenteListaNegra.getTipoVehiculo() != null) {
                if(isAACH){
                    patenteEscaneada.setTipoVehiculo(patenteListaNegra.getTipoVehiculo());
                } else if(patenteEscaneada.getTipoVehiculo() == null){
                    patenteEscaneada.setTipoVehiculo(patenteListaNegra.getTipoVehiculo());
                }
            }
            if (patenteListaNegra.getFechaCreacion() != null) {
                if(isAACH){
                    patenteEscaneada.setFechaCreacion(patenteListaNegra.getFechaCreacion());
                } else if(patenteEscaneada.getFechaCreacion() == null){
                    patenteEscaneada.setFechaCreacion(patenteListaNegra.getFechaCreacion());
                }
            }
            if (patenteListaNegra.getFechaRobo() != null) {
                if(isAACH){
                    patenteEscaneada.setFechaRobo(patenteListaNegra.getFechaRobo());
                } else if(patenteEscaneada.getFechaRobo() == null){
                    patenteEscaneada.setFechaRobo(patenteListaNegra.getFechaRobo());
                }
            }

            Log.v("asde3r3","pase 1");

            if (patenteListaNegra.getDuenoDatabase() != null && !patenteListaNegra.getDuenoDatabase().equalsIgnoreCase("")) {
                Log.v("asde3r3","pase 1.1: patenteEscaneada.getDuenoDatabase():" +patenteEscaneada.getDuenoDatabase());

                List listDuenoDatabase = patenteEscaneada.getDuenoDatabase();
                if(listDuenoDatabase.size() == 0){
                    Log.v("asde3r3","pase 1.2");
                    listDuenoDatabase.add(patenteListaNegra.getDuenoDatabase());
                } else {
                    Log.v("asde3r3","pase 1.3");
                    boolean existeDuenoDatabase = false;
                    for (int i = 0; i < listDuenoDatabase.size(); i++) {
                        if (listDuenoDatabase.get(i).toString().equalsIgnoreCase(patenteListaNegra.getDuenoDatabase())) {
                            existeDuenoDatabase = true;
                        }
                    }
                    if (!existeDuenoDatabase) {
                        listDuenoDatabase.add(patenteListaNegra.getDuenoDatabase());
                        patenteEscaneada.setDuenoDatabase(listDuenoDatabase);
                    }
                }

            }

            Log.v("asde3r3","pase 2");

            if (patenteListaNegra.getObservacion() != null && !patenteListaNegra.getObservacion().equalsIgnoreCase("")) {
                for(int i = 0; i < patenteListaNegra.getGrupo().size(); i++){
                    String key = patenteListaNegra.getGrupo().get(i);
                    if(!patenteEscaneada.getObservacion().containsKey(key) && patenteListaNegra.getDuenoDatabase().equals(key)){
                        HashMap hashMapObservaciones = patenteEscaneada.getObservacion();
                        hashMapObservaciones.put(key, patenteListaNegra.getObservacion());
                        patenteEscaneada.setObservacion(hashMapObservaciones);
                    }
                }
            }

            Log.v("asde3r3","pase 3");

            if (patenteListaNegra.getGrupoCompartido() != null && patenteListaNegra.getGrupoCompartido().size() > 0){
                if (patenteListaNegra.isBaseListaNegra()){
                    HashMap<String, List<String>> gruposCompartidos = patenteEscaneada.getGruposCompartidos();
                    String duenoDatabase = patenteListaNegra.getDuenoDatabase();
                    for (String grupo : patenteListaNegra.getGrupoCompartido()){
                        if (!gruposCompartidos.containsKey(duenoDatabase)){
                            List<String> grupos = new ArrayList<>();
                            grupos.add(grupo);
                            gruposCompartidos.put(duenoDatabase, grupos);
                        }
                        else {
                            List<String> grupos = gruposCompartidos.get(duenoDatabase);
                            grupos.add(grupo);
                            gruposCompartidos.put(duenoDatabase, grupos);
                        }
                    }
                    patenteEscaneada.setGruposCompartidos(gruposCompartidos);
                }
            }

            Log.v("asde3r3","pase 4");

            //concatenar grupos patente robada
            if(patenteListaNegra.isBaseRobada()){
                concatenarGrupos(patenteListaNegra.getGrupo(), "Grupo", "robada", listGrupos, listaGrupoTipo);
            } if(patenteListaNegra.isBaseListaNegra()){
                concatenarGrupos(patenteListaNegra.getGrupo(), "Grupo", "listaNegra", listGrupos, listaGrupoTipo);
            } if(patenteListaNegra.isBaseSoap()){
                concatenarGrupos(patenteListaNegra.getGrupo(), "Grupo", "soap", listGrupos, listaGrupoTipo);
            } if(patenteListaNegra.isBaseClonado()){
                concatenarGrupos(patenteListaNegra.getGrupo(), "Grupo", "clonado", listGrupos, listaGrupoTipo);
            }

            /*
            if (patenteListaNegra.getListaGrupoTipo() != null) {
                patenteEscaneada.setListaGrupoTipo(patenteListaNegra.getListaGrupoTipo());
            }
             */

            //datos de AACH
            if (patenteListaNegra.getAseguradora() != null && isAACH) {
                patenteEscaneada.setAseguradora(patenteListaNegra.getAseguradora());
            }
            if (patenteListaNegra.getIdProse() != null && isAACH) {
                patenteEscaneada.setIdProse(patenteListaNegra.getIdProse());
            }
            if (patenteListaNegra.getSinSiniestro() != null && isAACH) {
                patenteEscaneada.setSinSiniestro(patenteListaNegra.getSinSiniestro());
            }
            //fin datos AACH

            Log.v("asde3r3","pase 6");

            //concatenar origen
            if(patenteListaNegra.getOrigen()!= null){
                patenteEscaneada.getOrigen().add(patenteListaNegra.getOrigen());
            }

            //Base de datos
            if(patenteListaNegra.isBaseRobada()){
                patenteEscaneada.setBaseRobada(true);
            }
            if(patenteListaNegra.isBaseListaNegra()){
                patenteEscaneada.setBaseListaNegra(true);
            }
            if(patenteListaNegra.isBaseSoap()){
                patenteEscaneada.setBaseSoap(true);
            }
            if(patenteListaNegra.isBaseClonado()){
                patenteEscaneada.setBaseClonado(true);
            }

            //concatenar datos de denuncias si es que proviene de la base de safebywolf-denuncias
            if(patenteListaNegra.getDenuncianteEmail() != null && !patenteListaNegra.getDenuncianteEmail().equalsIgnoreCase("")){
                patenteEscaneada.getDenunciantes().add(patenteListaNegra.getDenuncianteEmail());
                patenteEscaneada.setDenuncianteEmail(patenteListaNegra.getDenuncianteEmail());
            }
            if (patenteListaNegra.getDenuncianteTelefono() != null && !patenteListaNegra.getDenuncianteTelefono().equalsIgnoreCase("")) {
                patenteEscaneada.setDenuncianteTelefono(patenteListaNegra.getDenuncianteTelefono());
            }
            if (patenteListaNegra.getDirRobo() != null && !patenteListaNegra.getDirRobo().equalsIgnoreCase("")) {
                patenteEscaneada.setDirRobo(patenteListaNegra.getDirRobo());
            }
            if (patenteListaNegra.getComisaria() != null && !patenteListaNegra.getComisaria().equalsIgnoreCase("")) {
                patenteEscaneada.setComisaria(patenteListaNegra.getComisaria());
            }
            if (patenteListaNegra.getFechaDenuncia() != null && !patenteListaNegra.getFechaDenuncia().equalsIgnoreCase("")) {
                patenteEscaneada.setFechaDenuncia(patenteListaNegra.getFechaDenuncia());
            }
            if (patenteListaNegra.getFechaRobo() != null && !patenteListaNegra.getFechaRobo().equalsIgnoreCase("")) {
                patenteEscaneada.setFechaRobo(patenteListaNegra.getFechaRobo());
            }
            //fin datos de denuncia

            Log.v("asde3r3","pase 7");

            //Datos alertGroup campo isAlertGroup y alertGroupName
            if(listaGrupoTipoLocalStorageConAlertGroup != null){
                ArrayList alertGroups = new ArrayList();
                for(int i = 0; i < listaGrupoTipoLocalStorageConAlertGroup.size(); i++){
                    if(listaGrupoTipoLocalStorageConAlertGroup.get(i).containsKey("isAlertGroup") && Boolean.parseBoolean(listaGrupoTipoLocalStorageConAlertGroup.get(i).get("isAlertGroup"))){
                        AlertGroup alertGroup = new AlertGroup(listaGrupoTipoLocalStorageConAlertGroup.get(i).get("nombre"), listaGrupoTipoLocalStorageConAlertGroup.get(i).get("alertGroupName"));
                        alertGroups.add(alertGroup);
                    }
                }
                if(alertGroups.size() == 0){
                    patenteEscaneada.setAlertToGroups(null);
                } else {
                    patenteEscaneada.setAlertGroup(true);
                    patenteEscaneada.setAlertToGroups(alertGroups);
                }
            }
            

            Log.v("asde3r3","pase 8");
            //fin datos alertGroup

        }

        return patenteEscaneada;
    }

    public PatenteEscaneada setPatenteEscaneadaConPatenteEscaneadaGlobal(PatenteEscaneada patenteEscaneada, PatenteEscaneada patenteEscaneadaGlobal) {
        patenteEscaneada.setTipoAmbas(patenteEscaneadaGlobal.getTipoAmbas());

        if (patenteEscaneadaGlobal.getGrupo() != null) {
            patenteEscaneada.setGrupo(patenteEscaneadaGlobal.getGrupo());
        }
        if (patenteEscaneadaGlobal.getMarca() != null) {
            patenteEscaneada.setMarca(patenteEscaneadaGlobal.getMarca());
        }
        if (patenteEscaneadaGlobal.getModelo() != null) {
            patenteEscaneada.setModelo(patenteEscaneadaGlobal.getModelo());
        }
        if (patenteEscaneadaGlobal.getColor() != null) {
            patenteEscaneada.setColor(patenteEscaneadaGlobal.getColor());
        }
        if (patenteEscaneadaGlobal.getAno() != null) {
            patenteEscaneada.setAno(patenteEscaneadaGlobal.getAno());
        }
        if (patenteEscaneadaGlobal.getMotor() != null) {
            patenteEscaneada.setMotor(patenteEscaneadaGlobal.getMotor());
        }
        if (patenteEscaneadaGlobal.getChasis() != null) {
            patenteEscaneada.setChasis(patenteEscaneadaGlobal.getChasis());
        }
        if (patenteEscaneadaGlobal.getTipo() != null) {
            patenteEscaneada.setTipo(patenteEscaneadaGlobal.getTipo());
        }
        if (patenteEscaneadaGlobal.getTipoVehiculo() != null) {
            patenteEscaneada.setTipoVehiculo(patenteEscaneadaGlobal.getTipoVehiculo());
        }
        if (patenteEscaneadaGlobal.getDuenoDatabase() != null) {
            patenteEscaneada.setDuenoDatabase(patenteEscaneadaGlobal.getDuenoDatabase());
        }
        if (patenteEscaneadaGlobal.getObservacion() != null) {
            patenteEscaneada.setObservacion(patenteEscaneadaGlobal.getObservacion());
        }

        if (patenteEscaneadaGlobal.getGruposCompartidos()!= null){
            patenteEscaneada.setGruposCompartidos(patenteEscaneadaGlobal.getGruposCompartidos());
        }

        if (patenteEscaneadaGlobal.getListaGrupoTipo() != null) {
            patenteEscaneada.setListaGrupoTipo(patenteEscaneadaGlobal.getListaGrupoTipo());
        }
        //datos de AACH
        if (patenteEscaneadaGlobal.getAseguradora() != null) {
            patenteEscaneada.setAseguradora(patenteEscaneadaGlobal.getAseguradora());
        }
        if (patenteEscaneadaGlobal.getIdProse() != null) {
            patenteEscaneada.setIdProse(patenteEscaneadaGlobal.getIdProse());
        }
        if (patenteEscaneadaGlobal.getSinSiniestro() != null) {
            patenteEscaneada.setSinSiniestro(patenteEscaneadaGlobal.getSinSiniestro());
        }
        //fin datos AACH

        if (patenteEscaneadaGlobal.getFechaCreacion() != null) {
            patenteEscaneada.setFechaCreacion(patenteEscaneadaGlobal.getFechaCreacion());
        }
        if (patenteEscaneadaGlobal.getFechaRobo() != null) {
            patenteEscaneada.setFechaRobo(patenteEscaneadaGlobal.getFechaRobo());
        }

        //datos Denuncias
        /*if (patenteEscaneadaGlobal.getDenuncianteNombre() != null && !patenteEscaneadaGlobal.getDenuncianteNombre().equalsIgnoreCase("")) {
            patenteEscaneada.setDenuncianteNombre(patenteEscaneadaGlobal.getDenuncianteNombre());
        }
        if (patenteEscaneadaGlobal.getDenuncianteRut() != null && !patenteEscaneadaGlobal.getDenuncianteRut().equalsIgnoreCase("")) {
            patenteEscaneada.setDenuncianteRut(patenteEscaneadaGlobal.getDenuncianteRut());
        }*/
        if(patenteEscaneadaGlobal.getDenuncianteEmail() != null && !patenteEscaneadaGlobal.getDenuncianteEmail().equalsIgnoreCase("")){
            patenteEscaneada.setDenuncianteEmail(patenteEscaneadaGlobal.getDenuncianteEmail());
        }
        if (patenteEscaneadaGlobal.getDenuncianteTelefono() != null && !patenteEscaneadaGlobal.getDenuncianteTelefono().equalsIgnoreCase("")) {
            patenteEscaneada.setDenuncianteTelefono(patenteEscaneadaGlobal.getDenuncianteTelefono());
        }
        if (patenteEscaneadaGlobal.getDirRobo() != null && !patenteEscaneadaGlobal.getDirRobo().equalsIgnoreCase("")) {
            patenteEscaneada.setDirRobo(patenteEscaneadaGlobal.getDirRobo());
        }
        if (patenteEscaneadaGlobal.getComisaria() != null && !patenteEscaneadaGlobal.getComisaria().equalsIgnoreCase("")) {
            patenteEscaneada.setComisaria(patenteEscaneadaGlobal.getComisaria());
        }
        if (patenteEscaneadaGlobal.getFechaDenuncia() != null && !patenteEscaneadaGlobal.getFechaDenuncia().equalsIgnoreCase("")) {
            patenteEscaneada.setFechaDenuncia(patenteEscaneadaGlobal.getFechaDenuncia());
        }
        if (patenteEscaneadaGlobal.getFechaRobo() != null && !patenteEscaneadaGlobal.getFechaRobo().equalsIgnoreCase("")) {
            patenteEscaneada.setFechaRobo(patenteEscaneadaGlobal.getFechaRobo());
        }
        if(patenteEscaneadaGlobal.getDenunciantes() != null){
            patenteEscaneada.setDenunciantes(patenteEscaneadaGlobal.getDenunciantes());
        }

        if(patenteEscaneadaGlobal.getOrigen() != null){
            patenteEscaneada.setOrigen(patenteEscaneadaGlobal.getOrigen());
        }
        //fin datos Denuncias

        //Base de datos
        if(patenteEscaneadaGlobal.isBaseRobada()){
            patenteEscaneada.setBaseRobada(true);
        }

        if(patenteEscaneadaGlobal.isBaseListaNegra()){
            patenteEscaneada.setBaseListaNegra(true);
        }

        if(patenteEscaneadaGlobal.isBaseSoap()){
            patenteEscaneada.setBaseSoap(true);
        }

        patenteEscaneada.setCambiada("false");

        //Datos totem
        patenteEscaneada.setTotem(isUserTotem);

        patenteEscaneada.setRecuperado(false);
        //fin datos totem

        //Datos alertGroup campo isAlertGroup y alertGroupName
        if(patenteEscaneadaGlobal.getAlertToGroups() != null){
            patenteEscaneada.setAlertToGroups(patenteEscaneadaGlobal.getAlertToGroups());
        }

        patenteEscaneada.setAlertGroup(patenteEscaneadaGlobal.isAlertGroup());
        //fin datos alertGroup

        patenteEscaneada.setAlertar(patenteEscaneadaGlobal.isAlertar());

        return patenteEscaneada;
    }

    //crea el contador por grupo por usuario por fecha
    public void creaContadorPorGrupoPorUsuarioPorFecha(final int i, String grupo){
        CurrentDate currentDate = new CurrentDate(new Date());
        EmailFechaTotal emailFechaTotal = new EmailFechaTotal(emailUsuarioFirebase, currentDate.getFecha(), 0);

        if(grupo != null ){
            creaContadorPorGrupoPorUsuarioPorFechaEspecifico(i,grupo,currentDate, emailFechaTotal);
        } else {
            //si usuario no tiene grupo inicializar contadores en el grupo SIN GRUPO
            if (setGruposUsuarioFirebase.size() > 0) {
                //por grupo por fecha
                for (String grupoUsuario : setGruposUsuarioFirebase) {
                    creaContadorPorGrupoPorUsuarioPorFechaEspecifico(i, grupoUsuario, currentDate, emailFechaTotal);
                }
            } else {
                creaContadorPorGrupoPorUsuarioPorFechaEspecifico(i, "SIN GRUPO", currentDate, emailFechaTotal);
            }
        }
    }

    public void creaContadorPorGrupoPorUsuarioPorFechaEspecifico(final int i, String grupo, CurrentDate currentDate, EmailFechaTotal emailFechaTotal){
        //inicializa el contador total de patentes escaneadas por grupo por usuario (costo de lectura 1 o mas)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    //inicializa el contador total de patentes escaneadas por grupo por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);

                    //inicializa el contador total de patentes robadas por grupo por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ROBADAVISTA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);

                    //inicializa el contador total de patentes lista negra por grupo por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.LISTANEGRAVISTA).collection(Referencias.PORGRUPO).document(grupo).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (i<5){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    creaContadorPorGrupoPorUsuarioPorFechaEspecifico(i+1, grupo, currentDate, emailFechaTotal);
                }
            }
        });
    }

    //crea el contador por usuario por fecha
    public void creaContadorPorUsuarioPorFecha(final int i){
        CurrentDate currentDate = new CurrentDate(new Date());
        EmailFechaTotal emailFechaTotal = new EmailFechaTotal(emailUsuarioFirebase, currentDate.getFecha(), 0);

        //inicializa el contador total de patentes escaneadas por usuario por fecha (costo de lectura 1)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    //inicializa el contador total de patentes escaneadas por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);

                    //inicializa el contador total de patentes robadas por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.ROBADAVISTA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);

                    //inicializa el contador total de patentes lista negra por usuario por fecha (costo de escritura 1)
                    db.collection(Referencias.TOTALPATENTE).document(Referencias.LISTANEGRAVISTA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).set(emailFechaTotal);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(i<5){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    creaContadorPorUsuarioPorFecha(i+1);
                }
            }
        });

    }

    public void inicializarContadorDeTotales() {
        if(Utils.leerValorString(this,Referencias.CONTADORDETOTALES) != null
                && new CurrentDate(new Date()).getFecha().equalsIgnoreCase(Utils.leerValorString(this,Referencias.CONTADORDETOTALES))){
            Log.v("contadorDeTotales", "contadorDeTotales ya está creado con fecha " + Utils.leerValorString(this,Referencias.CONTADORDETOTALES));
            Log.v("contadorDeTotales", "contadorDeTotales ya está creado con fecha " + Utils.leerValorString(this,Referencias.CONTADORDETOTALES));
            return;
        }
        Utils.guardarValorString(this,Referencias.CONTADORDETOTALES,new CurrentDate(new Date()).getFecha());
        Log.v("contadorDeTotales", "se crea contadorDeTotales con fecha " + Utils.leerValorString(this,Referencias.CONTADORDETOTALES));
        creaContadorPorGrupoPorUsuarioPorFecha(0, null);
        creaContadorPorUsuarioPorFecha(0);
        //porgrupoporfecha bakend
        //porgrupo bakend
        //
    }

    // Metodo de la version nueva (aun en test)
    private PatenteRobadaVista crearPatenteListaNegraPreAlerta(PatenteEscaneada patenteEscaneada) {
        PatenteRobadaVista patenteRobadaVista = new PatenteRobadaVista(null, patenteEscaneada.getPatente(), patenteEscaneada.getLatitud(), patenteEscaneada.getLongitud(), "", patenteEscaneada.getFecha(), patenteEscaneada.getHora(), "", patenteEscaneada.getDireccion(), idUsuarioFirebase, nombreUsuarioFirebase, apellidoUsuarioFirebase, emailUsuarioFirebase, contactoUsuarioFirebase, "any", patenteEscaneada.getLongTime(), patenteEscaneada.getPositionX(), patenteEscaneada.getTipo(), patenteEscaneada.getTipoAmbas());
        patenteRobadaVista.setAlertar(patenteEscaneada.isAlertar());
        patenteRobadaVista.setHeadRafaga(true);
        patenteRobadaVista.setHeadSetRafaga(true);
        patenteRobadaVista.setBaseRobada(patenteEscaneada.isBaseRobada());
        patenteRobadaVista.setBaseListaNegra(patenteEscaneada.isBaseListaNegra());
        patenteRobadaVista.setBaseSoap(patenteEscaneada.isBaseSoap());
        patenteRobadaVista.setCambiada("false");

        patenteRobadaVista.setComuna(patenteEscaneada.getComuna());

        if (patenteEscaneada.getMarca() != null) {
            patenteRobadaVista.setMarca(patenteEscaneada.getMarca());
        }
        if (patenteEscaneada.getModelo() != null) {
            patenteRobadaVista.setModelo(patenteEscaneada.getModelo());
        }
        if (patenteEscaneada.getColor() != null) {
            patenteRobadaVista.setColor(patenteEscaneada.getColor());
        }
        if (patenteEscaneada.getDuenoDatabase() != null) {
            patenteRobadaVista.setDuenoDatabase(patenteEscaneada.getDuenoDatabase());
        }
        if (patenteEscaneada.getObservacion() != null) {
            patenteRobadaVista.setObservacion(patenteEscaneada.getObservacion());
        }
        if (patenteEscaneada.getListaGrupoTipo() != null) {
            patenteRobadaVista.setListaGrupoTipo(patenteEscaneada.getListaGrupoTipo());
        }

        if (patenteEscaneada != null) {
            if (patenteEscaneada.getGrupo().size() > 0) {
                patenteRobadaVista.getGrupo().addAll(patenteEscaneada.getGrupo());
            }
        }

        return patenteRobadaVista;
    }

    private void setComunaFromAPI(PatenteEscaneada patenteEscaneada, BitmapRectF bitmapRectF, boolean isFromDataBase, boolean isEscaneada, int indicePatente) {
        if(seMantieneLatitudYLongitud && !lastCiudad.equalsIgnoreCase("")){
            patenteEscaneada.setCiudad(lastCiudad);
            patenteEscaneada.setComuna(lastComuna);
            Log.v("setCiudad","seMantieneLatitudYLongitud lastCiudad: "+lastCiudad);
            crearPosiblePatenteRobadaVistaFirebase(patenteEscaneada, bitmapRectF, isFromDataBase, isEscaneada, indicePatente);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isUserTotem) {
                completableFutureReverseGeocodingAPI(patenteEscaneada).thenRun(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("setCiudad","por aca 3");
                        crearPosiblePatenteRobadaVistaFirebase(patenteEscaneada, bitmapRectF, isFromDataBase, isEscaneada, indicePatente);
                    }
                });
            } else {
                Log.v("setCiudad","por aca?");
                //reverseGeocodingAPI(patenteEscaneada);
                crearPosiblePatenteRobadaVistaFirebase(patenteEscaneada, bitmapRectF, isFromDataBase, isEscaneada, indicePatente);
            }
        }
    }

    public void setComunaYDireccionFromGeocoding(int indicePatente){
        /*
        //set la comuna
        ReverseGeocoding reverseGeocoding = new ReverseGeocoding(lastKnownLatitud, lastKnownLongitud, getApplicationContext());
        String comuna = null;

        Log.v("comunaRes","comuna es nula se debe setear comuna desde reverse geocoding");
        String pais = reverseGeocoding.getPais();
        String region = reverseGeocoding.getRegion();
        comuna = reverseGeocoding.getComuna();
        String ciudad = reverseGeocoding.getCiudad();

        if (comuna != null) {
            comuna = Utils.normalizarString(comuna);
        }
        //setea la comuna en patente escaneada
        patentesEscaneadas.get(indicePatente).setPais(pais);
        patentesEscaneadas.get(indicePatente).setRegion(region);
        patentesEscaneadas.get(indicePatente).setComuna(comuna);
        patentesEscaneadas.get(indicePatente).setCiudad(ciudad);

        //si es totem se trae la comuna desde la base de datos siempre que este setado
        if(isUserTotem && comunaTotem!=null && !comunaTotem.equalsIgnoreCase("")){
            patentesEscaneadas.get(indicePatente).setComuna(comunaTotem);
            patentesEscaneadas.get(indicePatente).setCiudad(comunaTotem);
            comuna = comunaTotem;
        }

        //concatena la comuna en los grupos
        concatenarGrupos(Arrays.asList(comuna),"Comuna", "", patentesEscaneadas.get(indicePatente).getGrupo(), patentesEscaneadas.get(indicePatente).getListaGrupoTipo());
        //set direccion
        String direccion = reverseGeocoding.getDireccion();
        patentesEscaneadas.get(indicePatente).setDireccion(direccion);
         */
    }

    private void crearPosiblePatenteRobadaVistaFirebase(PatenteEscaneada patenteEscaneada, BitmapRectF bitmapRectF, boolean isFromDataBase, boolean isEscaneada, int indicePatente) {

        Log.v("nFrame","crearPosiblePatenteRobadaVistaFirebase");
        if(indicePatente >= 0) {
            if(isEnviarSoloPatentesIgualesAGoogleOCRAPI && patenteEscaneada.getResponseApiGoogle() != null && !patenteEscaneada.getResponseApiGoogle().equalsIgnoreCase(patenteEscaneada.getPatente()) && modelType.equalsIgnoreCase("1")) {
                Log.v("patenteRobadaVista","se retorna en crearPosiblePatenteRobadaVistaFirebase porque esta activo enviarSoloPatentesIgualesAGoogleOCRAPI el tipo de modelo es 1 y la patente no coincide con la lectura de google OCR");
                return;
            }

            //concatena la comuna en grupo
            concatenarGrupos(Arrays.asList(patenteEscaneada.getComuna()), "Comuna", "", arrayListPatentesEscaneadas.get(indicePatente).getGrupo(), arrayListPatentesEscaneadas.get(indicePatente).getListaGrupoTipo());

            //concatena los grupos del usuario
            if(isGreaterDetection(bitmapRectF, patenteEscaneada)) {
                concatenarGrupos(listaGrupoGlobal, "Usuario", "", arrayListPatentesEscaneadas.get(indicePatente).getGrupo(), arrayListPatentesEscaneadas.get(indicePatente).getListaGrupoTipo());
            }

            setPatenteEscaneadaConPatenteEscaneadaGlobal(patenteEscaneada, arrayListPatentesEscaneadas.get(indicePatente));
        }

        Log.v("patentesEscaneadas","patentesEscaneadas0: "+patenteEscaneada.getMarca());
        /**
         * Rotar bitmap patente robada
         * */
        //Rotar bitmap
        Bitmap bitmapOriginal = rotateBitmap(bitmapRectF.getOriginalBitmap(), bitmapRectF.getSensorOrientation());
        Bitmap bitmapCrop = null;
        Log.v("patenteRobadaVista","rote imagen");
        Log.v("asde3r3","pase 14");

        //modelo 2 lee patentes desde preview
        if(modelType.equalsIgnoreCase("2")) {

            float rectFWidth = bitmapRectF.getRectF().right - bitmapRectF.getRectF().left;
            float rectFHeight = bitmapRectF.getRectF().bottom - bitmapRectF.getRectF().top;

            float scaleResizeWidth = 0;
            float scaleResizeHeight = 0;
            float razonAspectRatio = 0; // W/H del rectF

            razonAspectRatio = rectFWidth / rectFHeight; // W/H del rectF
            float resizeHeight = resizeWidthImage / razonAspectRatio; //se calcula respecto al width
            scaleResizeWidth = (resizeWidthImage / bitmapRectF.getRectF().width());
            scaleResizeHeight = (resizeHeight / bitmapRectF.getRectF().height());

            if (scaleResizeHeight == 0 || scaleResizeWidth == 0) {
                return;
            }

            Log.v("lala2", "right: " + bitmapRectF.getRectF().right + " left: " + bitmapRectF.getRectF().left + " resta: " + (bitmapRectF.getRectF().right - bitmapRectF.getRectF().left) + " scaleResizeWidth: " + scaleResizeWidth + " calculo completo: " + ((bitmapRectF.getRectF().right - bitmapRectF.getRectF().left) * scaleResizeWidth) + "");

            Bitmap bitmapCropAux = Bitmap.createBitmap(bitmapOriginal, (int) (bitmapRectF.getRectF().left), (int) (bitmapRectF.getRectF().top), (int) (bitmapRectF.getRectF().right - bitmapRectF.getRectF().left), (int) (bitmapRectF.getRectF().bottom - bitmapRectF.getRectF().top));
            bitmapCrop = Bitmap.createScaledBitmap(bitmapCropAux, (int) ((bitmapRectF.getRectF().right - bitmapRectF.getRectF().left) * scaleResizeWidth), (int) ((bitmapRectF.getRectF().bottom - bitmapRectF.getRectF().top) * scaleResizeHeight), false);
            bitmapCropAux.recycle();

            if(isRecuadroNaranjoEnImagen){
                bitmapOriginal = ponerRecuadroSobreBitmap(bitmapOriginal, bitmapRectF.getRectF(), Color.parseColor("#FF460B"));
            }

            //mdelo 1 lee patentes desde una imagen auto o patente
        } else {
            Log.v("patenteRobadaVista","antes de rotar bitmap crop");

            bitmapCrop = rotateBitmap(bitmapRectF.getCropBitmap(), bitmapRectF.getSensorOrientation());
            Log.v("patenteRobadaVista","rote bitmap crop");
        }

        if(arrayListPatentesEscaneadas.get(indicePatente).getResponseApiGoogle() != null){
            patenteEscaneada.setResponseApiGoogle(arrayListPatentesEscaneadas.get(indicePatente).getResponseApiGoogle());
        }

        //se setea bitmap antes de poner el texto (patente, marca, modelo, color)
        bitmapRectF.setOriginalBitmap(bitmapOriginal);
        bitmapRectF.setCropBitmap(bitmapCrop);

        //se pone texto (patente, marca, modelo, color) en bitmap
        bitmapOriginal = sobreponeBitmap(bitmapOriginal, bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), textAsBitmap(bitmapOriginal, patenteEscaneada, patenteEscaneada.getPatente(), patenteEscaneada.getFecha(), patenteEscaneada.getHora(), patenteEscaneada.getComuna(), patenteEscaneada.getLatitud() + " / " + patenteEscaneada.getLongitud(), Color.parseColor("#FF460B"),1, 1, 0), false);
        bitmapCrop = sobreponeBitmap(bitmapCrop, bitmapCrop.getWidth(), bitmapCrop.getHeight(), textAsBitmap(bitmapCrop, patenteEscaneada, patenteEscaneada.getPatente(), patenteEscaneada.getFecha(), patenteEscaneada.getHora(), patenteEscaneada.getComuna(), patenteEscaneada.getLatitud() + " / " + patenteEscaneada.getLongitud(), Color.parseColor("#FF460B"),0.5f, 0.7f, heightBitmapDeFondo), true);

        Log.v("asde3r3","pase 15");
        /**
         * Crear ID base de datos
         * */

        //obtiene id de coleccion posiblePatenteRobadaVista (costo de lectura 0)
        String id = db.collection(Referencias.POSIBLEPATENTEROBADAVISTA).document().getId();
        patenteEscaneada.setId(id);

        //Log.v("asde3r32","pase 13 "+indicePatente+" id: "+patenteEscaneada.getId()+" isAlertGroup "+patenteEscaneada.isAlertGroup() + " "+ patenteEscaneada.getAlertToGroups().get(0).getGroupName());

        Log.v("patenteRobadaVista", "ID CREADO DESDE COLECCION - POSIBLEPATENTEROBADAVISTA  ID: " + id);

        /**
         * Crear Ráfaga
         * */
        crearRafaga(patenteEscaneada, indicePatente);
        crearSetRafaga(patenteEscaneada, indicePatente);
        Log.v("patenteRobadaVista", "rafaga creada");


        /**
         * Vehículos SIN patente
         * */
        //para lecturas de autos sin patente se setea un longTime en patente
        if(patenteEscaneada.getPatente().equalsIgnoreCase("") && patenteEscaneada.getTipo().equalsIgnoreCase(Referencias.SINPATENTE)){
            Log.v("nFrame","Vehículos SIN patente dentro de crear");
            patenteEscaneada.setPatente(new CurrentDate(new Date()).getLongTime());

            patenteEscaneada.setHeadRafaga(true);
            patenteEscaneada.setHeadSetRafaga(true);

            List<String> listGrupos = new ArrayList<>();
            List<GroupType> listaGrupoTipo = new ArrayList<>();

            concatenarGrupos(Arrays.asList(patenteEscaneada.getComuna()), "Comuna", "", listGrupos, listaGrupoTipo);
            concatenarGrupos(listaGrupoGlobal, "Usuario", "", listGrupos, listaGrupoTipo);

            patenteEscaneada.setGrupo(listGrupos);
            patenteEscaneada.setListaGrupoTipo(listaGrupoTipo);

        } else {
            patenteEscaneada.setSinPatente(false);
        }
        Log.v("patentesEscaneadas","patentesEscaneadas4: "+patenteEscaneada.getMarca());
        /**
         * Set tiempo final
         * */
        //añadir patente robada vista actual
        Log.v("patenteRobadaVista", "numero Id1: " + patenteEscaneada.getId());
        patenteEscaneada.setTiempoFinal(Long.parseLong(new CurrentDate(new Date()).getLongTime()));
        patenteEscaneada.setTiempoTotal(Long.parseLong(new CurrentDate(new Date()).getLongTime())-patenteEscaneada.getTiempoInicial());
        Log.v("patenteRobadaVista","set tiempo total");
        /**
         * Si es buscada en las bases de datos
         * */
        if (isFromDataBase && patenteEscaneada.isHeadRafaga()) {
            patenteEscaneada.setConsultada(true);
        } else {
            patenteEscaneada.setIdSetRafaga(patenteEscaneada.getIdSetRafaga());
        }
        Log.v("patentesEscaneadas","patentesEscaneadas1: "+patenteEscaneada.getMarca());
        /**
         * Crea patente en firebase
         * */
        patentesRobadasVistas.add(patenteEscaneada);
        Log.v("patenteRobadaVista","isEscaneada: "+isEscaneada);

        //se crea la patenteEscaneada o posiblePatenteRobadaVista segun sea el caso
        enviarPatenteBackend(patenteEscaneada);

        Log.v("patentesEscaneadas","patentesEscaneadas2: "+patenteEscaneada.getMarca());

        /**
         * Actualiza url de imagen en firebase
         * */
        Bitmap finalBitmapOriginal = bitmapOriginal;
        actualizarUrlImagenAmpliadaPatente(bitmapCrop, patenteEscaneada, isEscaneada)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String urlImagen = task.getResult().toString();
                            Log.v("urlimagensuccess", "isSuccessful crop: "+patenteEscaneada.getId());
                            patenteEscaneada.setUrlImagenAmpliada(urlImagen);
                            updateUrlImagenAmpliadaPatente(patenteEscaneada, isEscaneada);

                            actualizarUrlImagenPatente(finalBitmapOriginal, patenteEscaneada, isEscaneada)
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                String urlImagen = task.getResult().toString();
                                                Log.v("urlimagensuccess", "isSuccessful full: "+patenteEscaneada.getId());
                                                patenteEscaneada.setUrlImagen(urlImagen);
                                                updateUrlImagenPatente(patenteEscaneada, isEscaneada);
                                            } else {
                                                Log.v("urlimagensuccess", "nooooooo isSuccessful");
                                            }
                                        }
                                    });

                        } else {
                            Log.v("urlimagensuccess", "nooooooo isSuccessful");
                        }
                    }
                });
    }

    public void crearRafaga(PatenteEscaneada patenteEscaneada, int indicePatente) {
        //crea rafaga en caso de ser necesario
        if(indicePatente!=-1 && !existeRafaga(arrayListPatentesEscaneadas.get(indicePatente))) {
            Log.v("patenteRobadaVista", "crear rafaga");
            String idRafaga = new CurrentDate(new Date()).getLongTime() + Utils.random();
            patenteEscaneada.setIdRafaga(idRafaga);
            arrayListPatentesEscaneadas.get(indicePatente).setIdRafaga(idRafaga);
            arrayListPatentesEscaneadas.get(indicePatente).setLastHeadTime(Long.parseLong(new CurrentDate(new Date()).getLongTime()));
            patenteEscaneada.setHeadRafaga(true);
        } else {
            if(indicePatente!=-1) {
                patenteEscaneada.setIdRafaga(arrayListPatentesEscaneadas.get(indicePatente).getIdRafaga());
                patenteEscaneada.setHeadRafaga(false);
            }
        }
        if(indicePatente!=-1) {
            Log.v("patenteRobadaVista", arrayListPatentesEscaneadas.get(indicePatente).getIdRafaga() + " - " + arrayListPatentesEscaneadas.get(indicePatente).getId());
        }
    }

    public void crearSetRafaga(PatenteEscaneada patenteEscaneada, int indicePatente) {
        //crea SET rafaga en caso de ser necesario
        if(indicePatente!=-1 && !existeSetRafaga(arrayListPatentesEscaneadas.get(indicePatente))) {
            Log.v("patenteRobadaVista", "crear SET rafaga");
            String idSetRafaga = new CurrentDate(new Date()).getLongTime()+ Utils.random();
            patenteEscaneada.setIdSetRafaga(idSetRafaga);
            arrayListPatentesEscaneadas.get(indicePatente).setIdSetRafaga(idSetRafaga);
            arrayListPatentesEscaneadas.get(indicePatente).setLastSetHeadTime(new CurrentDate(new Date()).getDate().getTime());
            patenteEscaneada.setHeadSetRafaga(true);
            crearPatenteEscaneadaFirebase(patenteEscaneada);
        } else {
            if(indicePatente!=-1) {
                patenteEscaneada.setIdSetRafaga(arrayListPatentesEscaneadas.get(indicePatente).getIdSetRafaga());
                patenteEscaneada.setHeadSetRafaga(false);
            }

        }
        Log.v("hsr","hsr2: "+patenteEscaneada.isHeadSetRafaga()+" id:"+patenteEscaneada.getId());
    }



    private Task<Uri> actualizarUrlImagenPatente(Bitmap bitmap, final Patente patenteRobadaVista, boolean isEscaneada) {

        Log.v("compressBitmap", "antes del compress");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, calidadDeImagenDeDeteccion, stream);

        final byte[] bytes = stream.toByteArray();

        bitmap.recycle();
        int random = Utils.random();
        final String path = "patentesRobadas" + separator + patenteRobadaVista.getPatente() + separator + fecha + separator + hora + patenteRobadaVista.getId()+ random + ".jpg";

        // Create a storage reference from our app and Create a reference to path image
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata("id", patenteRobadaVista.getId())
                .build();

        UploadTask uploadTask = storageRef.putBytes(bytes, metadata);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                String idPatenteRobadaVista = task.getResult().getMetadata().getCustomMetadata("id");
                patenteRobadaVista.setId(idPatenteRobadaVista);

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        });
        return urlTask;
        /*.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String urlImagen = task.getResult().toString();
                    Log.v("urlimagensuccess", "isSuccessful full: "+patenteRobadaVista.getId());
                    patenteRobadaVista.setUrlImagen(urlImagen);
                    updateUrlImagenPatente(patenteRobadaVista, isEscaneada);

                } else {
                    Log.v("urlimagensuccess", "nooooooo isSuccessful");
                }
            }
        });
        */

    }

    private Task<Uri> actualizarUrlImagenAmpliadaPatente(Bitmap bitmap, final Patente patenteRobadaVista, boolean isEscaneada) {

        Log.v("compressBitmap", "antes del compress");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, calidadDeImagenDeDeteccion, stream);

        final byte[] bytes = stream.toByteArray();

        bitmap.recycle();
        int random = Utils.random();
        final String path = "patentesRobadas" + separator + patenteRobadaVista.getPatente() + separator + fecha + separator + hora + patenteRobadaVista.getId()+ random + ".jpg";

        // Create a storage reference from our app and Create a reference to path image
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata("id", patenteRobadaVista.getId())
                .build();

        UploadTask uploadTask = storageRef.putBytes(bytes, metadata);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                String idPatenteRobadaVista = task.getResult().getMetadata().getCustomMetadata("id");
                patenteRobadaVista.setId(idPatenteRobadaVista);
                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        });
        return urlTask;
        /*.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String urlImagen = task.getResult().toString();
                    Log.v("urlimagensuccess", "isSuccessful crop: "+patenteRobadaVista.getId());
                    patenteRobadaVista.setUrlImagenAmpliada(urlImagen);
                    updateUrlImagenAmpliadaPatente(patenteRobadaVista, isEscaneada);

                } else {
                    Log.v("urlimagensuccess", "nooooooo isSuccessful");
                }
            }
        });
         */

    }

    public void enqueueHashMapPatente(PatenteQueue patenteQueue, int indicePatente){
        //si existe key agregar al hashmap
        Log.v("hashMapPatenteQueue","hashMapPatenteQueue size: "+hashMapPatenteQueue.size());

        if(hashMapPatenteQueue.containsKey(patenteQueue.getPatenteEscaneada().getPatente())){
            if(hashMapPatenteQueue.get(patenteQueue.getPatenteEscaneada().getPatente()) != null && hashMapPatenteQueue.get(patenteQueue.getPatenteEscaneada().getPatente()).size() < 10){
                ArrayList<PatenteQueue> arrayListPatenteQueue = hashMapPatenteQueue.get(patenteQueue.getPatenteEscaneada().getPatente());
                arrayListPatenteQueue.add(patenteQueue);
                hashMapPatenteQueue.put(patenteQueue.getPatenteEscaneada().getPatente(), arrayListPatenteQueue);
                Log.v("patenteRobadaVista","agregando a la cola: "+hashMapPatenteQueue.get(patenteQueue.getPatenteEscaneada().getPatente()).size()+" patente: "+patenteQueue.getPatenteEscaneada().getPatente());
            }
        } else {
            //volver a buscar
            Log.v("patenteRobadaVista","Creando llave: "+patenteQueue.getPatenteEscaneada().getPatente());
            hashMapPatenteQueue.put(patenteQueue.getPatenteEscaneada().getPatente(), new ArrayList<PatenteQueue>());
            Log.v("patenteRobadaVista","getPatenteEscaneada: "+patenteQueue.getPatenteEscaneada().getPatente());
            buscarPatenteListaNegraEnBaseDeDatosFirebase(patenteQueue.getPatenteEscaneada(), patenteQueue.getBitmapRectF(), indicePatente);
        }
    }

    public void dequeueHashMapPatenteGustafox(String patente){
        if(hashMapPatenteQueue.containsKey(patente)){
            if(hashMapPatenteQueue.get(patente) == null){
                hashMapPatenteQueue.remove(patente);
                return;
            }
            if (hashMapPatenteQueue.get(patente).size() > 0){
                int i = 0;
                PatenteQueue patenteQueue = hashMapPatenteQueue.get(patente).get(i);
                clasificarPatente(patenteQueue, false);
                Log.v("hashMapPatenteque", "crear patente");
                hashMapPatenteQueue.get(patente).remove(i); //se elimina por mientras
            } else {
                hashMapPatenteQueue.remove(patente);
            }
        }
    }

    public void deleteKeyHashMapPatente(String patente){
        Log.v("patenteRobadaVista","deleteKeyHashMapPatente: "+patente);
        if(hashMapPatenteQueue.containsKey(patente)){
            hashMapPatenteQueue.remove(patente);
        }
    }

    public void dequeueHashMapPatenteAntiguas(HashMap<String, ArrayList<PatenteQueue>> hashMapPatenteQueueAux){
        for (String key : hashMapPatenteQueueAux.keySet()) {
            ArrayList<PatenteQueue> ArrayListPatenteQueues = hashMapPatenteQueueAux.get(key);
            int i = 0;
            for (PatenteQueue patenteQueue : ArrayListPatenteQueues) {
                //se descartan las patentes que estan dentro del hashmap y que tienen un tiempo mayor a 1 minuto
                if(new CurrentDate(new Date()).getDate().getTime() - patenteQueue.getCurrentDate().getDate().getTime() > 60000){

                    //se elimina objeto
                    if(hashMapPatenteQueue.containsKey(key)){
                        if(hashMapPatenteQueue.get(key).size() > 0){
                            hashMapPatenteQueue.get(key).remove(patenteQueue); //se elimina objeto
                        }
                    }
                }
                i++;
            }

            //eliminaKeyHashMapPatenteQueue
            if(hashMapPatenteQueue.containsKey(key)){
                if(hashMapPatenteQueue.get(key).size()==0){
                    hashMapPatenteQueue.remove(key);
                }
            }

        }
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int sensorOrientation) {
        bitmap = rotateImage(bitmap, sensorOrientation);
        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    private String getComunaFromJSONGoogleApi(String jsonString) {
        String comuna = null;
        try {
            JSONObject objeto = new JSONObject(jsonString);
            String status = objeto.getString("status");
            // significa que las coordenadas arrojaron contenido
            if (status.equals("OK")) {
                if (objeto.has("results")) {
                    JSONArray jsonArray = objeto.getJSONArray("results");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.has("address_components")) {
                        JSONArray addressComps = jsonObject.getJSONArray("address_components");
                        for (int i = 0; i < addressComps.length(); i++) {
                            JSONObject addressCompsObj = addressComps.getJSONObject(i);
                            if (addressCompsObj.has("types")) {
                                JSONArray types = addressCompsObj.getJSONArray("types");

                                for (int j = 0; j < types.length(); j++) {
                                    String type = types.getString(j);
                                    if (type.equals("administrative_area_level_3")) {

                                        Log.v("comunaRes", "la comuna es: " + addressCompsObj.getString("long_name"));
                                        comuna = addressCompsObj.getString("long_name");
                                        return comuna;
                                    }
                                }

                                Log.v("comunaRes", "index: " + i + ": " + addressCompsObj.toString());
                            } else {
                                Log.v("comunaRes", "no hay campo types");
                            }
                        }
                    } else {
                        Log.v("comunaRes", "no hay campo address_components");
                    }
                } else {
                    Log.v("comunaRes", "no hay campo results");
                }
            }
            // status: ZERO_RESULTS | UNKNOWN_ERROR | INVALID_REQUEST | REQUEST_DENIED | OVER_QUERY_LIMIT
            // | ZERO_RESULTS
            else {
                Log.v("comunaRes", "No hay resultados y/o ocurrio un error");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // en este caso debe retornar null
        return comuna;
    }

    public void getTotalDePatentesDistintasLeidasPorDia(){
        CurrentDate currentDate = new CurrentDate(new Date());

        //obtiene el total de patentes escaneadas del día por usuario por fecha (costo de lectura 1)
        db.collection(Referencias.TOTALPATENTE).document(Referencias.ESCANEADA).collection(Referencias.PORUSUARIO).document(emailUsuarioFirebase).collection(Referencias.PORFECHA).document(currentDate.getFecha()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Total totalPatentesLidasDistintas = documentSnapshot.toObject(Total.class);
                    contadorPatentesDistintas = totalPatentesLidasDistintas.getTotal();
                    Log.v("contador",contadorPatentesDistintas+"");
                    textViewPatentesLeidas.setText(contadorPatentesDistintas+"");
                }
            }
        });
    }

    public void iniciarSensorTemperatura(){
        sensorTemperatura = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sensorTemperatura != null){
            mSensorManager.registerListener(this, sensorTemperatura, SensorManager.SENSOR_DELAY_NORMAL);//SensorManager.SENSOR_DELAY_Fastest
        } else {
            Log.v("temparturacpu", "temperatura no soportado");
        }
    }

    @Override
    public synchronized void onStop() {
        super.onStop();
        Log.v("onAlgo", "se ejecuta onStop()");
        onStopOrOnPause();
    }

    @Override
    public synchronized void onPause() {
        Log.v("onAlgo","Se ejecuta onPause()");
        Log.v("locationn","Se ejecuta onPause()");

        onStopOrOnPause();

        super.onPause();
    }

    public void onStopOrOnPause(){
        //se actualiza el tokenFirebaseInstalation de coleccion usuario (costo de escritura 1)
        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenFirebaseInstalation", "");

        if(handlerProcessImage != null){
            handlerProcessImage.removeCallbacksAndMessages(null);
            handlerProcessImage = null;
        }

        if(handlerCrearCollage != null){
            handlerCrearCollage.removeCallbacksAndMessages(null);
            handlerCrearCollage = null;
        }

        if (doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto != null) {
            doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto.cancel();
            doAsynchronousTaskListarPatenteRobadaVistaCadaUnMinuto = null;
        }

        if(textToSpeech!=null){
            textToSpeech.stop();
        }

        if(timerCrearCollage != null){
            timerCrearCollage.cancel();
            timerCrearCollage = null;
        }

        if(timerContadorFPS != null){
            timerContadorFPS.cancel();
            timerContadorFPS = null;
        }
        //se cancela timer sinGPS
        if(timerSinGPS != null){
            timerSinGPS.cancel();
            timerSinGPS = null;
        }
        //se cancela timerTask sinGPS
        if(timerTaskSinGPS != null){
            timerTaskSinGPS.cancel();
            timerTaskSinGPS = null;
        }

        //Cancela timer de conexion a internet
        if(timerTaskConexion!=null){
            timerTaskConexion.cancel();
        }

        cancelSendUserUbicacionGPS();

        //cancela timer de la ubicacion del totem
        if(timerSendUbicacionTotem != null){
            timerSendUbicacionTotem.cancel();
            timerSendUbicacionTotem = null;
        }

        //Cancela timer de la ubicacion del totem
        if(timerTaskSendUbicacionTotem!=null){
            timerTaskSendUbicacionTotem.cancel();
            timerTaskSendUbicacionTotem = null;
        }

        //Cancela task GPS
        if(timerTaskSinGPS!=null){
            timerTaskSinGPS.cancel();
        }

        destroyMGoogleApiClient();

        brilloDePantallaNormal();

        fromBackground = true;

        if(timerZoom!=null){
            timerZoom.cancel();
        }

        updateTotalPatenteEscaneada(contadorPatentesDistintasOnResume);
        updateTotalPatenteEscaneadaPorUsuario(contadorPatentesDistintasOnResume);
        contadorPatentesDistintasOnResume = 0;

        locationAvaible = false;
        detenerLocationService();

        //se actualiza los campos de inactividad de un usuario (costo de escritura 1)
        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("activo", "false", "ultimaConexion", FieldValue.serverTimestamp(), "ubicacionActiva", locationAvaible);

        //se actualiza los campos de inactividad coleccion ubicacion (costo de escritura 1)
        db.collection(Referencias.UBICACION).document(emailUsuarioFirebase).update("locationAvaible", locationAvaible);

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }

        if(timerTaskConexion != null){
            timerTaskConexion.cancel();
            timerTaskConexion = null;
        }
    }

    public void dialogErrorIngreso(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(DetectorActivity.this)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("ok, entiendo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .show();
    }

    @Override
    public synchronized void onResume() {
        iniciarGoogleMap();
        initTextToSpeech();
        Log.v("monresume","Se ejecuta onResume() de detector activity");
        touchListener();
        contadorFPS();
        crearCollageRunnable();

        if(isUserTotem) {
            taskSendStatusTotem();
        } else {
            isEnviarUbicacionGPS();
        }

        Log.v("onAlgo","se ejecuta onResume()");
        getTokenFirebaseInstalation();

        taskConexion();

        contadorPatentesDistintasOnResume = 0;

        fromBackground = true;

        if(timerZoom!=null){
            timerZoom.cancel();
        }

        if(!isUserTotem){
            if(isZoomAutomaticoActivado){
                cancelTimeTaskZoom();
                zoomHandler();
            }
        }

        actionButtonActivarAhorroDeEnergia(false);

        //actualiza los campos de actividad del usuario (costo de escritura 1)
        db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("activo", "true",
                "ultimaConexion", FieldValue.serverTimestamp());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (this.mGoogleApiClient == null) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Log.v("permissionsCamera", "inicia location service");
                //onResume
                callbackOnConnected();
                Log.v("onResume", "callback on connected");

            }
        }

        if(!isUserTotem) {
            verificarVersionApp();

            //verifica si han pasado mas de 10 minutos en background
            CurrentDate currentDate = new CurrentDate(new Date());
            Log.v("ultimaConexion",currentDate.getHora());
            if(timestampUltimaConexion != 0){
                if(currentDate.getDate().getTime() - timestampUltimaConexion > 60000 * 10){
                    ejecutarLogoInicialActivity();
                } else {
                    timestampUltimaConexion = 0;
                }
            }
        }

        //lista las patentes cada 1 minuto
        //si es usuario totem cada un minuto debe apagarlo o encenderlo
        taskListarPatenteRobadaVista();

        inicializarContadorDeTotales();

        getTotalDePatentesDistintasLeidasPorDia();

        super.onResume();
    }

    public void isEnviarUbicacionGPS(){
        if(isSendUsuarioUbicacionGPS) {
            taskSendUbicacionUsuario();
            isUpdateUbicacionUsuario = true;
        } else {
            cancelSendUserUbicacionGPS();
        }
    }

    public void cancelSendUserUbicacionGPS(){
        //cancela timer de la ubicacion del usuario
        if(timerSendUbicacionUsuario != null) {
            timerSendUbicacionUsuario.cancel();
            timerSendUbicacionUsuario = null;
            isUpdateUbicacionUsuario = false;
        }
        //Cancela timer de la ubicacion del usuario
        if(timerTaskSendUbicacionUsuario!=null){
            timerTaskSendUbicacionUsuario.cancel();
            timerTaskSendUbicacionUsuario = null;
            Log.v("locationnn", "se cancela user timerTask");
            procesoSendUbicacion --;
        }
            Log.v("locationnn", "se cancela user location timer");
    }

    public void desactivaZoomAutomatico(){
        isZoomAutomaticoActivado = false;
        buttonZoom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        buttonZoom.setImageDrawable(getResources().getDrawable(R.drawable.manual));
        linearLayoutContenedorSeekbar.setVisibility(View.VISIBLE);
        linearLayoutContenedorSeekbar.animate().translationX(10);
        cancelTimeTaskZoom();
    }

    public void activaZoomAutomatico(){
        isZoomAutomaticoActivado = true;
        buttonZoom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        buttonZoom.setImageDrawable(getResources().getDrawable(R.drawable.eye));

//        if(!isDebug()){
            linearLayoutContenedorSeekbar.animate().translationX(-10).withEndAction(new Runnable() {
                @Override
                public void run() {
                    linearLayoutContenedorSeekbar.setVisibility(View.GONE);
                }
            });
//        }
    }

    @Override
    public synchronized void onDestroy() {
        Log.v("onAlgo", "se ejecuta onDestroy()");
        if(reiniciarTotem != null){
            reiniciarTotem.remove();
        }

        if(configuracionEspecificaTotem != null){
            configuracionEspecificaTotem.remove();
        }

        if(configuracionEspecificaUsuario != null){
            configuracionEspecificaUsuario.remove();
        }

        if(configuracionTotem != null){
            configuracionTotem.remove();
        }

        if(configuracionApp != null){
            configuracionApp.remove();
        }

        if(observablePosiblePatenteRobadaVista != null){
            observablePosiblePatenteRobadaVista.remove();
        }

        if(observableBroadcastMessages != null){
            observableBroadcastMessages.remove();
        }

        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }

        if(callBroadcastReceiver != null){
            unregisterReceiver(callBroadcastReceiver);
            callBroadcastReceiver = null;
        }

        if (broadcastReceiverLocation != null) {
            unregisterReceiver(broadcastReceiverLocation);
            broadcastReceiverLocation = null;
        }

        //timer de la tarea que envia la ubicacion del usuario
        if (timerCadaUnMinuto != null) {
            timerCadaUnMinuto.cancel();
            timerCadaUnMinuto = null;
        }

        //desactiva los sensores
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }

        if (broadcastReceiverBattery != null) {
            unregisterReceiver(broadcastReceiverBattery);
        }

        super.onDestroy();
    }

    private void centrarCamaraAPatente(double latitud, double longitud) {
        existPatenteRobadaEnMapa = true;
        LatLng latLng = new LatLng(latitud, longitud);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomGoogleMaps);
        googleMap.animateCamera(cameraUpdate);
        defaulZoomCircle();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.v("permissionsCamera", "on request permission result requestCode: "+requestCode);
        // Si el código de solicitud coincide con el código de solicitud utilizado en el DialogFragment,
        // reenviar la llamada al DialogFragment.
        if (requestCode == REQUEST_CODE_PERMISSION_IMAGES) {
            DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(EncuestaImagenes.TAG);
            if (dialogFragment != null) {
                Log.v("permissionsCamera", "distinto de nulo");
                dialogFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else {
                Log.v("permissionsCamera", "nulo");
            }
        }
        else if (requestCode == PERMISSIONS_CAMERA_REQUEST) {
            Log.v("permissionsCamera", "on request permission result camera");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("permissionsCamera","se aceptan los permisos Camera");
                Log.v("previewR","se aceptan los permisos Camera");
                setFragment();
                //onPreviewSizeChosen(new Size(previewWidth, previewHeight), getRotation());

                //actualiza el campo permisoCamara de coleccion usuario (costo de escritura 1)
                db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoCamara",true);
            } else {
                Log.v("permissionsCamera","NO se aceptan los permisos Camera");
                permissionDialogCamera();

                //actualiza el campo permisoCamara de coleccion usuario (costo de escritura 1)
                db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoCamara",false);
            }
        }

        else if (requestCode == PERMISSIONS_GPS_REQUEST) {
            countPermissionsGPS++;
            Log.v("permissionsCamera", "on request permission result GPS");
            if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.v("permissionsCamera","se aceptan los permisos GPS");
                //se aceptan los permisos de gps
                detenerLocationService();
                iniciarLocationService("Normal","false");
                callbackOnConnected();

                //actualiza el campo permisoGPS de coleccion usuario (costo de escritura 1)
                db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoGPS",true);
            } else {
                Log.v("permissionsCamera","NO se aceptan los permisos GPS");
                permissionDialogGPS();

                //actualiza el campo permisoGPS de coleccion usuario (costo de escritura 1)
                db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoGPS",false);
            }
            Log.v("permissionsCamera","grantResults.length: "+grantResults.length);
        }
        else {
            for (int i = 0; i<grantResults.length;i++) {
                //camara
                if(i == 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.v("permissionsCamera","!! se aceptan los permisos camara setfragment");
                    setFragment();

                    //actualiza el campo permisoCamara de coleccion usuario (costo de escritura 1)
                    db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoCamara",true);
                }

                //GPS
                if(i == 1 && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.v("permissionsCamera","!! se aceptan los permisos GPS");
                    detenerLocationService();
                    iniciarLocationService("Normal","false");
                    callbackOnConnected();

                    //actualiza el campo permisoGPS de coleccion usuario (costo de escritura 1)
                    db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("permisoGPS",true);
                }
            }
        }

        if(allPermissionsGranted(grantResults)){
            Log.v("permissionsCamera","allPermissionsGranted");
            Log.v("previewR","allPermissionsGranted");
        } else {
            //camara
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                permissionDialogCamera();
            }
            //gps
            else if(grantResults[1] != PackageManager.PERMISSION_GRANTED){
                permissionDialogGPS();
            }
            else if(grantResults[2] != PackageManager.PERMISSION_GRANTED){
                permissionDialogPhoneState();
            }
        }
    }

    public void getGlobalesFB(){
        //obtengo las variables globales de un usuario (costo de lectura 1)
        configuracionApp = db.collection(Referencias.CONF).document("app").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.v("datosGlobales", "dfsaf");
                if (error!=null){
                    Log.v("datosGlobales", "Falló cambio de configuración");
                }
                else {
                    getVariablesFromDatabase(value.getData());
                    ejecutarAccionVariablesFromDatabase();
                    if(isUserTotem){
                        getConfiguracionGlobalTotemFirebase();
                        getConfiguracionEspecificaTotem();
                    } else {
                        getConfiguracionEspecificaUsuario();
                    }
                }
            }
        });

    }

    private void getVariablesFromDatabase(Map document){
        /**
         * Valores Booleanos
         */
        if(document.get("limiteParaActualizarCuenta") != null && Utils.isBoolean(document.get("limiteParaActualizarCuenta").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.LIMITEPARAACTUALIZARCUENTA, Boolean.parseBoolean(document.get("limiteParaActualizarCuenta").toString()));
            isLimiteParaActualizarCuenta =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.LIMITEPARAACTUALIZARCUENTA);
        }

        if(document.get("recuadroNaranjoEnImagen") != null && Utils.isBoolean(document.get("recuadroNaranjoEnImagen").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.RECUADRONARANJOENIMAGEN, Boolean.parseBoolean(document.get("recuadroNaranjoEnImagen").toString()));
            isRecuadroNaranjoEnImagen =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.RECUADRONARANJOENIMAGEN);
        }

        if(document.get("zoomAutomaticoActivado") != null && Utils.isBoolean(document.get("zoomAutomaticoActivado").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.ZOOMAUTOMATICOACTIVADO, Boolean.parseBoolean(document.get("zoomAutomaticoActivado").toString()));
            isZoomAutomaticoActivado =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.ZOOMAUTOMATICOACTIVADO);
        }

        if(document.get("buttonConsultaAutoSeguro") != null && Utils.isBoolean(document.get("buttonConsultaAutoSeguro").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.BUTTONAUTOSEGURO, Boolean.parseBoolean(document.get("buttonConsultaAutoSeguro").toString()));
            isButtonConsultaAutoSeguro =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.BUTTONAUTOSEGURO);
        }

        if(document.get("sendUsuarioUbicacionGPS") != null && Utils.isBoolean(document.get("sendUsuarioUbicacionGPS").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.SENDUSUARIOUBICACIONGPS, Boolean.parseBoolean(document.get("sendUsuarioUbicacionGPS").toString()));
            isSendUsuarioUbicacionGPS =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.SENDUSUARIOUBICACIONGPS);
            Log.v("qwerty1","Utils.leerValorBoolean(DetectorActivity.this,Referencias.SENDUSUARIOUBICACIONGPS) "+Utils.leerValorBoolean(DetectorActivity.this,Referencias.SENDUSUARIOUBICACIONGPS));
        }

        if(document.get("enviarSoloPatentesIgualesAGoogleOCRAPI") != null && Utils.isBoolean(document.get("enviarSoloPatentesIgualesAGoogleOCRAPI").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.ENVIARSOLOPATENTESIGUALESAGOOGLEOCRAPI, Boolean.parseBoolean(document.get("enviarSoloPatentesIgualesAGoogleOCRAPI").toString()));
            isEnviarSoloPatentesIgualesAGoogleOCRAPI = Utils.leerValorBoolean(DetectorActivity.this,Referencias.ENVIARSOLOPATENTESIGUALESAGOOGLEOCRAPI);
        }

        if(document.get("consultasApiGoogleActivado") != null && Utils.isBoolean(document.get("consultasApiGoogleActivado").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.APIGOOGLEOCR, Boolean.parseBoolean(document.get("consultasApiGoogleActivado").toString()));
            isConsultasApiGoogleActivado = Utils.leerValorBoolean(DetectorActivity.this,Referencias.APIGOOGLEOCR);
        }

        if(document.get("encuestaAbierta") != null && Utils.isBoolean(document.get("encuestaAbierta").toString())){
            Utils.guardarValorBoolean(DetectorActivity.this, Referencias.ENCUESTAABIERTA, Boolean.parseBoolean(document.get("encuestaAbierta").toString()));
            encuestaAbierta =  Utils.leerValorBoolean(DetectorActivity.this,Referencias.ENCUESTAABIERTA);
        }

        /**
         * Valores Enteros
         */
        if(document.get("maximoDetecciones") != null && Utils.isInt(document.get("maximoDetecciones").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.MAXDETECCIONES, Integer.parseInt(document.get("maximoDetecciones").toString()));
        }

        if(document.get("maximoDeteccionesTotales") != null && Utils.isInt(document.get("maximoDeteccionesTotales").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.MAXDETECCIONEST, Integer.parseInt(document.get("maximoDeteccionesTotales").toString()));
        }

        if(document.get("frecuenciaEnvioUbicacionUsuarioMilis") != null && Utils.isInt(document.get("frecuenciaEnvioUbicacionUsuarioMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FENVIOUBICACIONUSUARIO, Integer.parseInt(document.get("frecuenciaEnvioUbicacionUsuarioMilis").toString()));
        }

        if(document.get("esperaEnvioUbicacionUsuarioMilis") != null && Utils.isInt(document.get("esperaEnvioUbicacionUsuarioMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.EENVIOUBICACIONUSUARIO, Integer.parseInt(document.get("esperaEnvioUbicacionUsuarioMilis").toString()));
        }

        if(document.get("frecuenciaEnvioUbicacionTotemMilis") != null && Utils.isInt(document.get("frecuenciaEnvioUbicacionTotemMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FENVIOUBICACIONTOTEM, Integer.parseInt(document.get("frecuenciaEnvioUbicacionTotemMilis").toString()));
        }

        if(document.get("esperaEnvioUbicacionTotemMilis") != null && Utils.isInt(document.get("esperaEnvioUbicacionTotemMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.EENVIOUBICACIONTOTEM, Integer.parseInt(document.get("esperaEnvioUbicacionTotemMilis").toString()));
        }

        if(document.get("frecuenciaCreacionUbicacionMilis") != null && Utils.isInt(document.get("frecuenciaCreacionUbicacionMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FCREACIONUBICACION, (Integer.parseInt(document.get("frecuenciaCreacionUbicacionMilis").toString())));
        }

        if(document.get("frecuenciaComprobacionInternetMilis") != null && Utils.isInt(document.get("frecuenciaComprobacionInternetMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FCOMPROBACIONINTERNET, Integer.parseInt(document.get("frecuenciaComprobacionInternetMilis").toString()));
        }

        if(document.get("esperaComprobacionInternetMilis") != null && Utils.isInt(document.get("esperaComprobacionInternetMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.ECOMPROBACIONINTERNET, Integer.parseInt(document.get("esperaComprobacionInternetMilis").toString()));
        }

        if(document.get("frecuenciaComprobacionGPSMilis") != null && Utils.isInt(document.get("frecuenciaComprobacionGPSMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FCOMPROBACIONGPS, Integer.parseInt(document.get("frecuenciaComprobacionGPSMilis").toString()));
        }

        if(document.get("esperaComprobacionGPSMilis") != null && Utils.isInt(document.get("esperaComprobacionGPSMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.ECOMPROBACIONGPS, Integer.parseInt(document.get("esperaComprobacionGPSMilis").toString()));
        }

        if(document.get("esperaEnvioPatenteFirebaseMilis") != null && Utils.isInt(document.get("esperaEnvioPatenteFirebaseMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.EENVIOPATENTEFIREBASE, Integer.parseInt(document.get("esperaEnvioPatenteFirebaseMilis").toString()));
        }

        if(document.get("esperaFramesAutoSinPatenteMilis") != null && Utils.isInt(document.get("esperaFramesAutoSinPatenteMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.EFRAMESAUTOSINPATENTE, Integer.parseInt(document.get("esperaFramesAutoSinPatenteMilis").toString()));
        }

        if(document.get("cantidadMaximaFramesAutoSinPatente") != null && Utils.isInt(document.get("cantidadMaximaFramesAutoSinPatente").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.CMFRAMESAUTOSINPATENTE, Integer.parseInt(document.get("cantidadMaximaFramesAutoSinPatente").toString()));
        }

        if(document.get("tiempoRafagaMilis") != null && Utils.isInt(document.get("tiempoRafagaMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TRAFAGA, Integer.parseInt(document.get("tiempoRafagaMilis").toString()));
        }

        if(document.get("tiempoSetRafagaMilis") != null && Utils.isInt(document.get("tiempoSetRafagaMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TSETRAFAGA, Integer.parseInt(document.get("tiempoSetRafagaMilis").toString()));
        }

        if(document.get("tiempoVolverABuscarPatenteEnBDMilis") != null && Utils.isInt(document.get("tiempoVolverABuscarPatenteEnBDMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TVOLVERABUSCARPATENTEENBD, Integer.parseInt(document.get("tiempoVolverABuscarPatenteEnBDMilis").toString()));
        }

        if(document.get("radioDeInteresAlertaPatenteMetros") != null && Utils.isInt(document.get("radioDeInteresAlertaPatenteMetros").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.RDEINTERESALERTAPATENTE, Integer.parseInt(document.get("radioDeInteresAlertaPatenteMetros").toString()));
        }

        if(document.get("esperaModoAhorroMilis") != null && Utils.isInt(document.get("esperaModoAhorroMilis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.EMODOAHORRO, Integer.parseInt(document.get("esperaModoAhorroMilis").toString()));
        }

        if(document.get("cantidadAAumentarZoomDinamico") != null && Utils.isInt(document.get("cantidadAAumentarZoomDinamico").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.CANTIDADAAUMENTARZOOMDINAMICO, Integer.parseInt(document.get("cantidadAAumentarZoomDinamico").toString()));
        }

        if(document.get("cantidadADisminuirZoomDinamico") != null && Utils.isInt(document.get("cantidadADisminuirZoomDinamico").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.CANTIDADADISMINUIRZOOMDINAMICO, Integer.parseInt(document.get("cantidadADisminuirZoomDinamico").toString()));
        }

        if(document.get("calidadDeImagenDeDeteccion") != null && Utils.isInt(document.get("calidadDeImagenDeDeteccion").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.CALIDADDEIMAGENDEDETECCION, Integer.parseInt(document.get("calidadDeImagenDeDeteccion").toString()));
        }

        if(document.get("distanciaMinimaParaVolverAEscanear") != null && Utils.isInt(document.get("distanciaMinimaParaVolverAEscanear").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.DISTANCIAMINESCANEAR, Integer.parseInt(document.get("distanciaMinimaParaVolverAEscanear").toString()));
        }

        if(document.get("framesPorSegundo") != null && Utils.isInt(document.get("framesPorSegundo").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FRAMESPORSEGUNDO, Integer.parseInt(document.get("framesPorSegundo").toString()));
        }

        if(document.get("tiempoMaximoConZoomAutomaticoSeg") != null && Utils.isInt(document.get("tiempoMaximoConZoomAutomaticoSeg").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TIEMPOMAXZOOMAUTO, Integer.parseInt(document.get("tiempoMaximoConZoomAutomaticoSeg").toString()));
        }

        if(document.get("zoomFinal") != null && Utils.isInt(document.get("zoomFinal").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.ZOOMFINAL, Integer.parseInt(document.get("zoomFinal").toString()));
        }

        if(document.get("zoomMinimo") != null && Utils.isInt(document.get("zoomMinimo").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.ZOOMMINIMO, Integer.parseInt(document.get("zoomMinimo").toString()));
        }

        if(document.get("cantidadDeImagenesDelCollage") != null && Utils.isInt(document.get("cantidadDeImagenesDelCollage").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.CANTIDADDEIMAGENESDELCOLLAGE, Integer.parseInt(document.get("cantidadDeImagenesDelCollage").toString()));
        }

        if(document.get("frecuenciaCreacionCollageMillis") != null && Utils.isInt(document.get("frecuenciaCreacionCollageMillis").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.FRECCREACIONCOLLAGE, Integer.parseInt(document.get("frecuenciaCreacionCollageMillis").toString()));
        }

        if(!isUserTotem) {
            if (document.get("minimoDeLecturas") != null && Utils.isInt(document.get("minimoDeLecturas").toString())) {
                Utils.guardarValorInt(DetectorActivity.this, Referencias.MINLECTURAS, Integer.parseInt(document.get("minimoDeLecturas").toString()));
            }
        }

        if(document.get("tiempoMaximoSinLecturasTotemMinutos") != null && Utils.isInt(document.get("tiempoMaximoSinLecturasTotemMinutos").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TMAXSACTUALIZART, Integer.parseInt(document.get("tiempoMaximoSinLecturasTotemMinutos").toString()));
        }

        if(document.get("tiempoMaximoSinActualizarEstadoTotemMinutos") != null && Utils.isInt(document.get("tiempoMaximoSinActualizarEstadoTotemMinutos").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TMAXSLECTURAST, Integer.parseInt(document.get("tiempoMaximoSinActualizarEstadoTotemMinutos").toString()));
        }

        /**
         * Valores Float
         */
        if(document.get("confianzaMinimaOCRProbabilidadFlotante") != null && Utils.isFloat(document.get("confianzaMinimaOCRProbabilidadFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINOCRTOTEM, Float.parseFloat(document.get("confianzaMinimaOCRProbabilidadFlotante").toString()));
        }

        if(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante") != null && Utils.isFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPFTOTEM, Float.parseFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante").toString()));
        }

        if(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem") != null && Utils.isFloat(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINOCRTOTEM, Float.parseFloat(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem").toString()));
        }

        if(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem") != null && Utils.isFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPFTOTEM, Float.parseFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem").toString()));
        }

        if(document.get("confianzaMinimaAutoSinPatenteProbabilidadFlotante") != null && Utils.isFloat(document.get("confianzaMinimaAutoSinPatenteProbabilidadFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINAUTOSINPATENTEPF, Float.parseFloat(document.get("confianzaMinimaAutoSinPatenteProbabilidadFlotante").toString()));
        }

        if(document.get("confianzaMinimaOCRProbabilidadFlotante") != null && Utils.isFloat(document.get("confianzaMinimaOCRProbabilidadFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINOCR, Float.parseFloat(document.get("confianzaMinimaOCRProbabilidadFlotante").toString()));
        }

        if(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem") != null && Utils.isFloat(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINOCRTOTEM, Float.parseFloat(document.get("confianzaMinimaOCRProbabilidadFlotanteTotem").toString()));
        }

        if(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem") != null && Utils.isFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPFTOTEM, Float.parseFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem").toString()));
        }

        if(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante") != null && Utils.isFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPF, Float.parseFloat(document.get("confianzaMinimaDeteccionPatenteProbabilidadFlotante").toString()));
        }

        if(document.get("proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom") != null && Utils.isFloat(document.get("proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONMINIMAAUTOPATENTERESPECTOELPREVIEWPARAHACERZOOM, Float.parseFloat(document.get("proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom").toString()));
        }

        if(document.get("proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom") != null && Utils.isFloat(document.get("proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONMAXIMAAUTOPATENTERESPECTOELPREVIEWPARAHACERZOOM, Float.parseFloat(document.get("proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom").toString()));
        }

        if(document.get("proporcionAnchoMinimoAutoRespectoElAltoFlotante") != null && Utils.isFloat(document.get("proporcionAnchoMinimoAutoRespectoElAltoFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTORESPECTOELALTO, Float.parseFloat(document.get("proporcionAnchoMinimoAutoRespectoElAltoFlotante").toString()));
        }

        if(document.get("proporcionAnchoMinimoPatenteRespectoElAltoFlotante") != null && Utils.isFloat(document.get("proporcionAnchoMinimoPatenteRespectoElAltoFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTERESPECTOELALTO, Float.parseFloat(document.get("proporcionAnchoMinimoPatenteRespectoElAltoFlotante").toString()));
        }

        if(document.get("proporcionAnchoMinimoAutoRespectoElPreviewFlotante") != null && Utils.isFloat(document.get("proporcionAnchoMinimoAutoRespectoElPreviewFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTORESPECTOELPREVIEW, Float.parseFloat(document.get("proporcionAnchoMinimoAutoRespectoElPreviewFlotante").toString()));
        }

        if(document.get("proporcionAnchoMinimoPatenteRespectoElPreviewFlotante") != null && Utils.isFloat(document.get("proporcionAnchoMinimoPatenteRespectoElPreviewFlotante").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTERESPECTOELPREVIEW, Float.parseFloat(document.get("proporcionAnchoMinimoPatenteRespectoElPreviewFlotante").toString()));
        }

        if(document.get("velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos") != null && Utils.isFloat(document.get("velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos").toString())){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.VELOCIDADMAXUNADETECCION, Float.parseFloat(document.get("velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos").toString()));
        }

        if(document.get("proporcionAlturaMinimaAuto") != null){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONALTURAMINIMAAUTO, Float.parseFloat(document.get("proporcionAlturaMinimaAuto").toString()));
        }
        if(document.get("proporcionAlturaMinimaPatente") != null){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONALTURAMINIMAPATENTE, Float.parseFloat(document.get("proporcionAlturaMinimaPatente").toString()));
        }
        if(document.get("proporcionAnchoMinimoAuto") != null){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTO, Float.parseFloat(document.get("proporcionAnchoMinimoAuto").toString()));
        }
        if(document.get("proporcionAnchoMinimoPatente") != null){
            Utils.guardarValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTE, Float.parseFloat(document.get("proporcionAnchoMinimoPatente").toString()));
        }

        /**
         * Valores String
         */
        if(document.get("textoFinalizacionEncuesta") != null && document.get("textoFinalizacionEncuesta") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.TEXTOFINALIZACIONENCUESTA, (document.get("textoFinalizacionEncuesta").toString()));
        }
        if(document.get("latitud") != null && document.get("latitud") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.LATITUD, (document.get("latitud").toString()));
        }
        if(document.get("longitud") != null && document.get("longitud") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.LONGITUD, (document.get("longitud").toString()));
        }
        if(document.get("comuna") != null && document.get("comuna") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.COMUNA, (document.get("comuna").toString()));
        }

        if(document.get("speechButtonZoomDinamico") != null && document.get("speechButtonZoomDinamico") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHBUTTONZOOMDINAMICO, (document.get("speechButtonZoomDinamico").toString()));
        }

        if(document.get("speechButtonZoomManual") != null && document.get("speechButtonZoomManual") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHBUTTONZOOMMANUAL, (document.get("speechButtonZoomManual").toString()));
        }

        if(document.get("speechModoAhorroDeEnergiaActivado") != null && document.get("speechModoAhorroDeEnergiaActivado") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHAHORRODEENERGIAACTIVADO, (document.get("speechModoAhorroDeEnergiaActivado").toString()));
        }

        if(document.get("speechButtonModoAhorroDeEnergiaActivado") != null && document.get("speechButtonModoAhorroDeEnergiaActivado") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHBUTTONAHORRODEENERGIAACTIVADO, (document.get("speechButtonModoAhorroDeEnergiaActivado").toString()));
        }

        if(document.get("speechButtonModoAhorroDeEnergiaDesactivado") != null && document.get("speechButtonModoAhorroDeEnergiaDesactivado") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHBUTTONAHORRODEENERGIADESACTIVADO, (document.get("speechButtonModoAhorroDeEnergiaDesactivado").toString()));
        }

        if(document.get("speechInicial") != null && document.get("speechInicial") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHINICIAL, (document.get("speechInicial").toString()));
        }

        /**
         * Alerta
         * */
        if(document.get("speechAlertaSinEncargo") != null && document.get("speechAlertaSinEncargo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHALERTASINENCARGO, (document.get("speechAlertaSinEncargo").toString()));
        }
        if(document.get("speechAlertaConEncargo") != null && document.get("speechAlertaConEncargo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHALERTACONENCARGO, (document.get("speechAlertaConEncargo").toString()));
        }
        if(document.get("speechAlertaSoap") != null && document.get("speechAlertaSoap") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHALERTASOAP, (document.get("speechAlertaSoap").toString()));
        }
        if(document.get("speechAlertaListaNegra") != null && document.get("speechAlertaListaNegra") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHALERTALISTANEGRA, (document.get("speechAlertaListaNegra").toString()));
        }
        if(document.get("speechAlertaSeHaVistoVehiculo") != null && document.get("speechAlertaSeHaVistoVehiculo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHALERTASEHAVISTOVEHICULO, (document.get("speechAlertaSeHaVistoVehiculo").toString()));
        }
        /**
         * Pos Alerta
         * */
        if(document.get("speechPosAlertaVehiculoPlacaPatente") != null && document.get("speechPosAlertaVehiculoPlacaPatente") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTAVEHICULOPLACAPATENTE, (document.get("speechPosAlertaVehiculoPlacaPatente").toString()));
        }
        if(document.get("speechPosAlertaCorreccionValidacion") != null && document.get("speechPosAlertaCorreccionValidacion") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACORRECCIONVALIDACION, (document.get("speechPosAlertaCorreccionValidacion").toString()));
        }
        if(document.get("speechPosAlertaListaNegra") != null && document.get("speechPosAlertaListaNegra") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTALISTANEGRA, (document.get("speechPosAlertaListaNegra").toString()));
        }
        if(document.get("speechPosAlertaSoap") != null && document.get("speechPosAlertaSoap") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTASOAP, (document.get("speechPosAlertaSoap").toString()));
        }
        if(document.get("speechPosAlertaConEncargo") != null && document.get("speechPosAlertaConEncargo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACONENCARGO, (document.get("speechPosAlertaConEncargo").toString()));
        }
        if(document.get("speechPosAlertaSinEncargo") != null && document.get("speechPosAlertaSinEncargo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTASINENCARGO, (document.get("speechPosAlertaSinEncargo").toString()));
        }
        if(document.get("speechPosAlertaNoIgualAImagen") != null && document.get("speechPosAlertaNoIgualAImagen") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTANOIGUALAIMAGEN, (document.get("speechPosAlertaNoIgualAImagen").toString()));
        }
        if(document.get("speechPosAlertaEliminada") != null && document.get("speechPosAlertaEliminada") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTAELIMINADA, (document.get("speechPosAlertaEliminada").toString()));
        }
        if(document.get("speechPosAlertaComunicateConTuCentral") != null && document.get("speechPosAlertaComunicateConTuCentral") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACOMUNICATECONTUCENTRAL, (document.get("speechPosAlertaComunicateConTuCentral").toString()));
        }

        /**
         * PRE ALERTA
         * */
        if(document.get("speechPreAlertaConPosibleIrregularidad") != null && document.get("speechPreAlertaConPosibleIrregularidad") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPREALERTACONPOSIBLEIRREGULARIDAD, (document.get("speechPreAlertaConPosibleIrregularidad").toString()));
        }
        if(document.get("speechPreAlertaSeHaVistoVehiculo") != null && document.get("speechPreAlertaSeHaVistoVehiculo") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPREALERTASEHAVISTOVEHICULO, (document.get("speechPreAlertaSeHaVistoVehiculo").toString()));
        }
        if(document.get("speechPreAlertaSeVaAValidar") != null && document.get("speechPreAlertaSeVaAValidar") != ""){
            Utils.guardarValorString(DetectorActivity.this, Referencias.SPEECHPREALERTASEVAAVALIDAR, (document.get("speechPreAlertaSeVaAValidar").toString()));
        }

        /**
         *  INICIO ENCUESTA
         */



        if(document.get("tiempoActivacionEncuestaMinutos") != null && Utils.isInt(document.get("tiempoActivacionEncuestaMinutos").toString())){
            Log.v("ENCUESTAPATRULLERO","se guarda el valor de tiempoActivacionEncuestaMinutos:"+document.get("tiempoActivacionEncuestaMinutos"));
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TIEMPOACTIVACIONENCUESTA, Integer.parseInt(document.get("tiempoActivacionEncuestaMinutos").toString()));
        }

        if(document.get("intentosParaSaltarEncuesta") != null && Utils.isInt(document.get("intentosParaSaltarEncuesta").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.INTENTOSSKIPENCUESTA, Integer.parseInt(document.get("intentosParaSaltarEncuesta").toString()));
        }

        if(document.get("tiempoVerificacionEncuestasPendientes") != null && Utils.isInt(document.get("tiempoVerificacionEncuestasPendientes").toString())){
            Utils.guardarValorInt(DetectorActivity.this, Referencias.TIEMPOVERIFICACIONENCUESTASPENDIENTES, Integer.parseInt(document.get("tiempoVerificacionEncuestasPendientes").toString()));
        }

        /**
         * FIN ENCUESTA
         */

        leerLocales();
        if(maximoDetecciones == 0){
            Log.v("datosGlobales", "maximoDetecciones = 0 :( ");
            defaultSetting();
        }
        Log.v("datosGlobales", "maximoDetecciones: "+maximoDetecciones);
        //onPause();
        //onResume();
        Log.v("datosGlobales", "Se cambió configuración");
    }

    private void ejecutarAccionVariablesFromDatabase(){
        if(isZoomAutomaticoActivado){
            activaZoomAutomatico();
        } else {
            desactivaZoomAutomatico();
        }
        cuadroVisible();
        if(isButtonConsultaAutoSeguro){
            buttonConsultaAutoSeguro.setVisibility(View.VISIBLE);
        } else {
            buttonConsultaAutoSeguro.setVisibility(View.GONE);
        }

        if(framesPorSegundo > 0) {
            setInterval((1000 / framesPorSegundo));
        }
        isEnviarUbicacionGPS();

        if (Utils.isDouble(Utils.leerValorString(DetectorActivity.this, Referencias.LATITUD))) {
            latitudUserFromDB = Double.parseDouble(Utils.leerValorString(DetectorActivity.this, Referencias.LATITUD));
            lastKnownLatitud = latitudUserFromDB;
        } else {
            latitudUserFromDB = 0.0f;
        }

        if (Utils.isDouble(Utils.leerValorString(DetectorActivity.this, Referencias.LONGITUD))) {
            longitudUserFromDB = Double.parseDouble(Utils.leerValorString(DetectorActivity.this, Referencias.LONGITUD));
            lastKnownLongitud = longitudUserFromDB;
        }else {
            longitudUserFromDB = 0.0f;
        }

        String comuna = Utils.leerValorString(DetectorActivity.this, Referencias.COMUNA);
        if(comuna != null && !comuna.equalsIgnoreCase("")){
            comunaUserFromDB = comuna;
            Log.v("Sefaasd","entree ciudadTotemAux: "+comuna);
        } else {
            comunaUserFromDB = "Sin comuna";
        }
    }

    private void leerLocales(){
        Log.v("datosGlobales","1: "+frecuenciaEnvioUbicacionUsuarioMilis);

        proporcionAnchoMinimoPatente = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTE, proporcionAnchoMinimoPatente);
        proporcionAnchoMinimoAuto = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTO, proporcionAnchoMinimoAuto);
        proporcionAlturaMinimaPatente = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONALTURAMINIMAPATENTE, proporcionAlturaMinimaPatente);
        proporcionAlturaMinimaAuto = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONALTURAMINIMAAUTO, proporcionAlturaMinimaAuto);

        proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONMINIMAAUTOPATENTERESPECTOELPREVIEWPARAHACERZOOM, proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom);
        proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONMAXIMAAUTOPATENTERESPECTOELPREVIEWPARAHACERZOOM, proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom);

        proporcionAnchoMinimoAutoRespectoElAltoFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTORESPECTOELALTO, proporcionAnchoMinimoAutoRespectoElAltoFlotante);
        proporcionAnchoMinimoPatenteRespectoElAltoFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTERESPECTOELALTO, proporcionAnchoMinimoPatenteRespectoElAltoFlotante);
        proporcionAnchoMinimoAutoRespectoElPreviewFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOAUTORESPECTOELPREVIEW, proporcionAnchoMinimoAutoRespectoElPreviewFlotante);
        proporcionAnchoMinimoPatenteRespectoElPreviewFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.PROPORCIONANCHOMINIMOPATENTERESPECTOELPREVIEW, proporcionAnchoMinimoPatenteRespectoElPreviewFlotante);
        esperaModoAhorroMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.EMODOAHORRO, esperaModoAhorroMilis);
        speechModoAhorroDeEnergiaActivado = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHAHORRODEENERGIAACTIVADO);

        speechButtonModoAhorroDeEnergiaActivado = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHBUTTONAHORRODEENERGIAACTIVADO);

        speechButtonModoAhorroDeEnergiaActivado = speechButtonModoAhorroDeEnergiaActivado.replace("[tiempo]",""+((int)(esperaModoAhorroMilis/60000)));

        speechButtonModoAhorroDeEnergiaDesactivado = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHBUTTONAHORRODEENERGIADESACTIVADO);

        speechButtonZoomDinamico = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHBUTTONZOOMDINAMICO);
        speechButtonZoomManual = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHBUTTONZOOMMANUAL);

        cantidadAAumentarZoomDinamico = Utils.leerValorInt(DetectorActivity.this,Referencias.CANTIDADAAUMENTARZOOMDINAMICO,cantidadAAumentarZoomDinamico);
        cantidadADisminuirZoomDinamico = Utils.leerValorInt(DetectorActivity.this,Referencias.CANTIDADADISMINUIRZOOMDINAMICO,cantidadADisminuirZoomDinamico);
        calidadDeImagenDeDeteccion =  Utils.leerValorInt(DetectorActivity.this,Referencias.CALIDADDEIMAGENDEDETECCION,calidadDeImagenDeDeteccion);

        tiempoMaximoConZoomAutomaticoSeg = Utils.leerValorInt(DetectorActivity.this,Referencias.TIEMPOMAXZOOMAUTO,tiempoMaximoConZoomAutomaticoSeg);
        cantidadDeImagenesDelCollage = Utils.leerValorInt(DetectorActivity.this, Referencias.CANTIDADDEIMAGENESDELCOLLAGE, cantidadDeImagenesDelCollage);
        frecuenciaCreacionCollageMillis = Utils.leerValorInt(DetectorActivity.this, Referencias.FRECCREACIONCOLLAGE, frecuenciaCreacionCollageMillis);
        minimoDeLecturasFromDB = Utils.leerValorInt(DetectorActivity.this, Referencias.MINLECTURAS, minimoDeLecturasFromDB);
        maximoDetecciones = Utils.leerValorInt(DetectorActivity.this, Referencias.MAXDETECCIONES, maximoDetecciones);
        maximoDeteccionesTotales = Utils.leerValorInt(DetectorActivity.this, Referencias.MAXDETECCIONEST, maximoDeteccionesTotales);
        frecuenciaEnvioUbicacionUsuarioMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.FENVIOUBICACIONUSUARIO, frecuenciaEnvioUbicacionUsuarioMilis);
        Log.v("datosGlobales","2: "+frecuenciaEnvioUbicacionUsuarioMilis);
        esperaEnvioUbicacionUsuarioMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.EENVIOUBICACIONUSUARIO, esperaEnvioUbicacionUsuarioMilis);
        frecuenciaCreacionUbicacionMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.FCREACIONUBICACION, frecuenciaCreacionUbicacionMilis);
        frecuenciaComprobacionInternetMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.FCOMPROBACIONINTERNET, frecuenciaComprobacionInternetMilis);
        esperaComprobacionInternetMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.ECOMPROBACIONINTERNET, esperaComprobacionInternetMilis);
        frecuenciaComprobacionGPSMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.FCOMPROBACIONGPS, frecuenciaComprobacionGPSMilis);
        esperaComprobacionGPSMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.ECOMPROBACIONGPS, esperaComprobacionGPSMilis);
        esperaEnvioPatenteFirebaseMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.EENVIOPATENTEFIREBASE, esperaEnvioPatenteFirebaseMilis);

        distanciaMinimaParaVolverAEscanear =  Utils.leerValorInt(DetectorActivity.this,Referencias.DISTANCIAMINESCANEAR, distanciaMinimaParaVolverAEscanear);
        framesPorSegundo =  Utils.leerValorInt(DetectorActivity.this,Referencias.FRAMESPORSEGUNDO, framesPorSegundo);

        velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos = Utils.leerValorFloat(DetectorActivity.this, Referencias.VELOCIDADMAXUNADETECCION, velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos);
        confianzaMinimaOCRProbabilidadFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.CONFMINOCR, confianzaMinimaOCRProbabilidadFlotante);
        confianzaMinimaOCRProbabilidadFlotanteTotem = Utils.leerValorFloat(DetectorActivity.this, Referencias.CONFMINOCRTOTEM, confianzaMinimaOCRProbabilidadFlotanteTotem);
        confianzaMinimaDeteccionPatenteProbabilidadFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPF, confianzaMinimaDeteccionPatenteProbabilidadFlotante);
        confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem = Utils.leerValorFloat(DetectorActivity.this, Referencias.CONFMINDETECCIONSINPATENTEPFTOTEM, confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem);

        tiempoRafagaMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.TRAFAGA, tiempoRafagaMilis);
        tiempoSetRafagaMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.TSETRAFAGA, tiempoSetRafagaMilis);
        tiempoVolverABuscarPatenteEnBDMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.TVOLVERABUSCARPATENTEENBD, tiempoVolverABuscarPatenteEnBDMilis);
        radioDeInteresAlertaPatenteMetros = Utils.leerValorInt(DetectorActivity.this, Referencias.RDEINTERESALERTAPATENTE, radioDeInteresAlertaPatenteMetros);

        //AUTO SIN PATENTE
        esperaFramesAutoSinPatenteMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.EFRAMESAUTOSINPATENTE, esperaFramesAutoSinPatenteMilis);
        cantidadMaximaFramesAutoSinPatente = Utils.leerValorInt(DetectorActivity.this, Referencias.CMFRAMESAUTOSINPATENTE, cantidadMaximaFramesAutoSinPatente);
        confianzaMinimaAutoSinPatenteProbabilidadFlotante = Utils.leerValorFloat(DetectorActivity.this, Referencias.CONFMINAUTOSINPATENTEPF, confianzaMinimaAutoSinPatenteProbabilidadFlotante);

        //SALUDO DE BIENVENIDA AL INICIO
        speechInicial = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHINICIAL);

        //POS ALERTA
        speechPosAlertaNoIgualAImagen = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTANOIGUALAIMAGEN);
        speechPosAlertaEliminada = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTAELIMINADA);
        speechPosAlertaConEncargo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACONENCARGO);
        speechPosAlertaSinEncargo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTASINENCARGO);
        speechPosAlertaListaNegra = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTALISTANEGRA);
        speechPosAlertaSoap = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTASOAP);
        speechPosAlertaCorreccionValidacion = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACORRECCIONVALIDACION);
        speechPosAlertaVehiculoPlacaPatente = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTAVEHICULOPLACAPATENTE);
        speechPosAlertaComunicateConTuCentral = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPOSALERTACOMUNICATECONTUCENTRAL);

        //ALERTA
        speechAlertaSeHaVistoVehiculo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHALERTASEHAVISTOVEHICULO);
        speechAlertaListaNegra = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHALERTALISTANEGRA);
        speechAlertaSoap = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHALERTASOAP);
        speechAlertaConEncargo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHALERTACONENCARGO);
        speechAlertaSinEncargo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHALERTASINENCARGO);

        //PRE ALERTA
        speechPreAlertaSeHaVistoVehiculo = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPREALERTASEHAVISTOVEHICULO);
        speechPreAlertaSeVaAValidar = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPREALERTASEVAAVALIDAR);
        speechPreAlertaConPosibleIrregularidad = Utils.leerValorString(DetectorActivity.this, Referencias.SPEECHPREALERTACONPOSIBLEIRREGULARIDAD);

        //TOTEM
        tiempoMaximoSinLecturasTotemMinutos = Utils.leerValorInt(DetectorActivity.this, Referencias.TMAXSLECTURAST, tiempoMaximoSinLecturasTotemMinutos);
        tiempoMaximoSinActualizarEstadoTotemMinutos = Utils.leerValorInt(DetectorActivity.this, Referencias.TMAXSACTUALIZART, tiempoMaximoSinActualizarEstadoTotemMinutos);
        frecuenciaEnvioUbicacionTotemMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.FENVIOUBICACIONTOTEM, frecuenciaEnvioUbicacionTotemMilis);
        esperaEnvioUbicacionTotemMilis = Utils.leerValorInt(DetectorActivity.this, Referencias.EENVIOUBICACIONTOTEM, esperaEnvioUbicacionTotemMilis);

        //ENCUESTA
        Log.v("ENCUESTAPATRULLERO","tiempo de activacion encuesta en minutos -  modo leer:"+tiempoActivacionEncuestaMinutos);
        tiempoActivacionEncuestaMinutos = Utils.leerValorInt(DetectorActivity.this, Referencias.TIEMPOACTIVACIONENCUESTA, tiempoActivacionEncuestaMinutos);
        Log.v("ENCUESTAPATRULLERO","tiempo de activacion encuesta en minutos -  modo leer:"+tiempoActivacionEncuestaMinutos);
        intentosParaSaltarEncuesta = Utils.leerValorInt(DetectorActivity.this, Referencias.INTENTOSSKIPENCUESTA, intentosParaSaltarEncuesta);
        tiempoVerificacionEncuestasPendientes = Utils.leerValorInt(DetectorActivity.this, Referencias.TIEMPOVERIFICACIONENCUESTASPENDIENTES, tiempoVerificacionEncuestasPendientes);

        Log.v("datosGlobales","esperaEnvioUbicacionTotemMilis: "+esperaEnvioUbicacionTotemMilis);
        Log.v("datosGlobales","frecuenciaCreacionUbicacionSegundos: "+ frecuenciaCreacionUbicacionMilis);
        Log.v("datosGlobales","frecuenciaEnvioUbicacionUsuarioMilis: "+frecuenciaEnvioUbicacionUsuarioMilis);
        Log.v("datosGlobales","division: "+Math.ceil(frecuenciaCreacionUbicacionMilis /frecuenciaEnvioUbicacionUsuarioMilis));
        Log.v("datosGlobales","maximoDetecciones: "+maximoDetecciones);
    }

    private void getTokenFirebaseInstalation(){
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("aaaaaaaaaaaaas", "getInstanceId failed "+ task.getException());
                    return;
                }
                //atualiza el campo tokenFirebaseInstalation de coleccion usuario (costo de escritura 1)
                db.collection(Referencias.USUARIO).document(emailUsuarioFirebase)
                        .update("tokenFirebaseInstalation", task.getResult());
            }
        });
    }

    private boolean isUIThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Log.v("error2", "error: "+e.getMessage());
        if (isUIThread()) {

            //actualiza el campo tokenFirebaseInstalation en coleccion usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenFirebaseInstalation", "",
                    "ubicacionActiva",false);
        } else {  //handle non UI thread throw uncaught exception

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    //actualiza el campo tokenFirebaseInstalation en coleccion usuario (costo de escritura 1)
                    db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenFirebaseInstalation", "",
                            "ubicacionActiva",false);
                }
            });
        }
        ejecutarLogoInicialActivity();
        finish();
    }

    public String getDeviceId(Context context) {

        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();

            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        Log.v("imeiR","deviceId: "+deviceId);

        return deviceId;
    }

    public void defaultSetting() {
        textoFinalizacionEncuesta = "<p>Gracias por su aporte al responder la encuesta.</p>";
        speechInicial = "Bienvenido al sistema lector de patentes de chile...";

        //ALERTA
        speechAlertaSeHaVistoVehiculo = "Se ha visto vehículo";
        speechAlertaListaNegra = "perteneciente a tus patentes en observación.";
        speechAlertaSoap = "posiblemente con problemas en su documentación.";
        speechAlertaConEncargo = "con, encargo vigente por robo,";
        speechAlertaSinEncargo = "sin, encargo por robo.";

        ///PRE ALERTA
        speechPreAlertaSeHaVistoVehiculo = "Se ha visto vehículo";
        speechPreAlertaConPosibleIrregularidad = " con posible irregularidad.";
        speechPreAlertaSeVaAValidar = " Se va a validar.";

        //POS ALERTA
        speechPosAlertaCorreccionValidacion = "Corrección, "; //(cuando se cambia el estado del encargo)
        speechPosAlertaNoIgualAImagen = "Descartar alerta, vehículo, placa patente...";
        speechPosAlertaEliminada = "Descartar alerta, vehículo, placa patente...";
        speechPosAlertaVehiculoPlacaPatente = "vehículo, placa patente...";
        speechPosAlertaListaNegra = "pertenece a tus patentes en observación.";
        speechPosAlertaSoap = "posiblemente con problemas en su documentación.";
        speechPosAlertaConEncargo = "mantiene encargo vigente por robo.";
        speechPosAlertaSinEncargo = "NO, presenta encargo por robo.";
        speechPosAlertaComunicateConTuCentral = " Para mayor información comunícate, con tu central de monitoreo.";

        proporcionAnchoMinimoAutoRespectoElAltoFlotante = 1.3f;
        proporcionAnchoMinimoPatenteRespectoElAltoFlotante = 4f;
        proporcionAnchoMinimoAutoRespectoElPreviewFlotante = 0.1f;
        proporcionAnchoMinimoPatenteRespectoElPreviewFlotante = 0.04f;

        proporcionMinimaAutoPatenteRespectoDelPreviewParaHacerZoom = 0.4f;
        proporcionMaximaAutoPatenteRespectoDelPreviewParaHacerZoom = 0.6f;

        esperaModoAhorroMilis = 60000*3;
        speechModoAhorroDeEnergiaActivado = "Modo no distracción iniciado, la aplicación seguirá escaneando.";
        speechButtonModoAhorroDeEnergiaActivado = "Modo no distracción activado, la pantalla se oscurecerá automáticamente al cabo de "+ ((int)(esperaModoAhorroMilis/60000))+" minutos, no obstante la aplicación seguirá escaneando.";
        speechButtonModoAhorroDeEnergiaDesactivado = "Modo no distracción desactivado, la pantalla permanecerá siempre activa.";

        speechButtonZoomDinamico = "soom dinámico activado";
        speechButtonZoomManual = "soom manual activado";

        cantidadADisminuirZoomDinamico = 2;
        cantidadAAumentarZoomDinamico = 1;
        calidadDeImagenDeDeteccion = 50;

        isButtonConsultaAutoSeguro = true;
        isRecuadroNaranjoEnImagen = true;
        isSendUsuarioUbicacionGPS = true;
        isConsultasApiGoogleActivado = false;
        isEnviarSoloPatentesIgualesAGoogleOCRAPI = false;

        distanciaMinimaParaVolverAEscanear = 100;
        framesPorSegundo = 10;
        velocidadMaximaParaEscanearConUnaDeteccionEnMetrosSegundos = 4.0f;
        tiempoMaximoConZoomAutomaticoSeg = 5;
        cantidadDeImagenesDelCollage = 4;
        frecuenciaCreacionCollageMillis = 1000;
        minimoDeLecturasFromDB = 2;
        tiempoMaximoSinLecturasTotemMinutos = 30;
        tiempoMaximoSinActualizarEstadoTotemMinutos = 10;

        maximoDetecciones = 5;
        maximoDeteccionesTotales = 500;

        frecuenciaEnvioUbicacionUsuarioMilis = 5000;
        esperaEnvioUbicacionUsuarioMilis = 5000;

        frecuenciaEnvioUbicacionTotemMilis = 60000;
        esperaEnvioUbicacionTotemMilis = 10000;

        frecuenciaCreacionUbicacionMilis = 10 * 1000;

        frecuenciaComprobacionInternetMilis = 30000;
        esperaComprobacionInternetMilis = 5000;

        frecuenciaComprobacionGPSMilis = 3000;
        esperaComprobacionGPSMilis = 10000;

        esperaEnvioPatenteFirebaseMilis = 40000;

        esperaFramesAutoSinPatenteMilis = 3000;
        cantidadMaximaFramesAutoSinPatente = 2; //comienza a contar en 0

        confianzaMinimaAutoSinPatenteProbabilidadFlotante = 0.5f;

        // Minimum detection confidence to track a detection.
        confianzaMinimaDeteccionPatenteProbabilidadFlotante = 0.35f;
        confianzaMinimaOCRProbabilidadFlotante = 0.35f;

        confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem = 0.35f;
        confianzaMinimaOCRProbabilidadFlotanteTotem = 0.35f;

        tiempoRafagaMilis = 30000;//30 segundos
        tiempoSetRafagaMilis = 60000 * 30;//30 minutos

        tiempoVolverABuscarPatenteEnBDMilis = 60000;// 1 minuto

        radioDeInteresAlertaPatenteMetros = 0; //2000 metros

        proporcionAlturaMinimaAuto = 0.23f;
        proporcionAlturaMinimaPatente = 0.041f;
        proporcionAnchoMinimoAuto = 0.13f;
        proporcionAnchoMinimoPatente = 0.04f;

        // Encuesta
        encuestaAbierta = true;
        tiempoActivacionEncuestaMinutos = 30; // 30 minutos.
        intentosParaSaltarEncuesta = 3; // 3 oportunidades para saltar la encuesta.
        tiempoVerificacionEncuestasPendientes = 1; // 1 minuto para la verificacion de encuestas pendientes
    }

    private void enviarPatenteBackend(PatenteEscaneada patente) {
        Gson gson = new GsonBuilder().setLenient().create();
        Gson gsonPatente = new Gson();
        CurrentDate currentDate = new CurrentDate(new Date());
        patente.setTimestamp(currentDate.getDate());
        patente.setFechaCreacion(currentDate.getDate());
        //imagenes
        patente.setUrlImagen(null);
        patente.setUrlImagenAmpliada(null);
        String jsonPatenteEscaneada = gsonPatente.toJson(patente);

        runInBackground(() -> {
            /**
             * Enviar posiblePatenteRobadaVista a Api
             */
            Log.v("patenteRobadaVista","enviar patente a api -- Empezo");

            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL+"posiblePatenteRobadaVista/").addConverterFactory(GsonConverterFactory.create(gson)).build();
            PosiblePatenteRobadaVistaApi patenteApi = retrofit.create(PosiblePatenteRobadaVistaApi.class);
            JsonObject jsonObject = JsonParser.parseString(jsonPatenteEscaneada).getAsJsonObject();
            Log.v("patenteRobadaVista", String.valueOf(jsonObject));
            Call calltime = patenteApi.enviarPatentePosibleRobadaVista(jsonObject);
            Log.v("patenteRobadaVista","enviar patente a api -- paso antes de callTime");
            calltime.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    Log.v("patenteRobadaVista","api -- onResponse");
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Toast.makeText(DetectorActivity.this, "Patente enviada exitosamente!", Toast.LENGTH_SHORT).show();
                            Log.v("patenteRobadaVista", "api -- successful");
                        } else {
                            //Toast.makeText(DetectorActivity.this, "Fallo en la conexion", Toast.LENGTH_SHORT).show();
                            Log.v("patenteRobadaVista", "api -- fallo en la conexion");
                        }
                    } else {
                        // Handle the case where response.body() is null
                        Log.e("patenteRobadaVista", "api -- response body is null");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.v("patenteRobadaVista","api -- OnFailure");
                    Toast.makeText(DetectorActivity.this, "Error al enviar enviar patente", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        });
    }

    private void enviarImagenPatenteApi(PatenteEscaneada patente, boolean isAmpliada) {
        CurrentDate currentDate = new CurrentDate(new Date());
        patente.setTimestamp(currentDate.getDate());
        UrlImagenPatenteApi urlImagenPatenteApi;
        if (isAmpliada) {
            urlImagenPatenteApi = new UrlImagenPatenteApi(patente.getId(),patente.getUrlImagenAmpliada(), patente.getPatente(), isAmpliada, patente.getTimestamp());
        } else {
            urlImagenPatenteApi = new UrlImagenPatenteApi(patente.getId(),patente.getUrlImagen(), patente.getPatente(), isAmpliada, patente.getTimestamp());
        }

        Gson gson = new GsonBuilder().setLenient().create();
        Gson gsonPatente = new Gson();
        String jsonImagenPatenteApi = gsonPatente.toJson(urlImagenPatenteApi);
        runInBackground(() -> {
            /**
             * Enviar posiblePatenteRobadaVista a Api
             */
            Log.v("patenteRobadaVista","enviar imagenUrl -- Empezo");
            Log.v("patenteRobadaVista","enviar imagenUrl a api -- paso antes de callTime 1");

            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL+"posiblePatenteRobadaVista/").addConverterFactory(GsonConverterFactory.create(gson)).build();
            Log.v("patenteRobadaVista","enviar patente a api -- paso antes de callTime 2");
            ImagenPatenteApi imagenPatenteApi = retrofit.create(ImagenPatenteApi.class);
            Log.v("patenteRobadaVista","enviar patente a api -- paso antes de callTime 3");
            JsonObject jsonObject = JsonParser.parseString(jsonImagenPatenteApi).getAsJsonObject();
            Log.v("patenteRobadaVista", String.valueOf(jsonObject));
            Call calltime = imagenPatenteApi.enviarImagenApi(jsonObject);
            Log.v("patenteRobadaVista","enviar patente a api -- paso antes de callTime 4");
            calltime.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.v("patenteRobadaVista", response.toString());
                    Log.v("patenteRobadaVista","api -- onResponse");
                    Log.v("patenteRobadaVista", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        Log.v("patenteRobadaVista", response.body().toString());

                        if (response.isSuccessful()) {
                            //Toast.makeText(DetectorActivity.this, "imagen Patente enviada exitosamente!", Toast.LENGTH_SHORT).show();
                            Log.v("patenteRobadaVista", "api -- successful");
                        } else {
                            //Toast.makeText(DetectorActivity.this, "Fallo enviando patente imagen", Toast.LENGTH_SHORT).show();
                            Log.v("patenteRobadaVista", "api -- fallo en la conexion");
                        }
                    } else {
                        // Handle the case where response.body() is null
                        Log.e("patenteRobadaVista", "api -- response body is null");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.v("patenteRobadaVista","api -- OnFailure");
                    Log.v("patenteRobadaVista",t.toString());
                    //Toast.makeText(DetectorActivity.this, "Error al enviar patente imagen", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        });
    }

}
