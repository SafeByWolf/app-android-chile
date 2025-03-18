package com.safebywolf.safebywolf.Activity;

import static com.safebywolf.safebywolf.Activity.TensorFlow.CameraActivity.PERMISSIONS_GPS_REQUEST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.safebywolf.safebywolf.Activity.TensorFlow.UtilsOCR;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Model.ControlVehicular;
import com.safebywolf.safebywolf.Model.PatenteSensitive;
import com.safebywolf.safebywolf.Model.RutSensitive;
import com.safebywolf.safebywolf.Model.SenderPatente;
import com.safebywolf.safebywolf.Model.SenderRut;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IdentityControlCarab extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private Calendar calendar;
    private SimpleDateFormat dateFormat, hourFormat;
    private String date, hour, API_URL, tempURL = "https://4ed7-200-83-50-139.ngrok.io", lugarActualLat, lugarActualLng;
    private Button botonCancelar, botonAceptar;
    private ScrollView scrollView;
    private TextView servicio, cuadrante, lugar, vehiculo, patente, marca, modelo, nombre, dia, mes, anio, nacionalidad;
    private EditText rut;
    private boolean dev = true;
    RutSensitive rs;
    PatenteSensitive ps;
    private FusedLocationProviderClient clientICC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_control_carab);
        clientICC = LocationServices.getFusedLocationProviderClient(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        defineValuesFVBId();
        permissionDialogGPS();
        setOnClickListeners();
    }

    private void defineValuesFVBId() {
        scrollView = findViewById(R.id.scroll_view_handle);
        botonCancelar = findViewById(R.id.cancelarICB);
        botonAceptar = findViewById(R.id.aceptarICB);
        dia = findViewById(R.id.edit_fecha);
        mes = findViewById(R.id.edit_fecha2);
        anio = findViewById(R.id.edit_fecha3);
        servicio = findViewById(R.id.servicioICB);
        cuadrante = findViewById(R.id.cuadranteICB);
        lugar = findViewById(R.id.lugarICB);
        vehiculo = findViewById(R.id.vehiculoICB);
        patente = findViewById(R.id.patenteICB); /*Autocompletar la patente, quizás con volante o maleta*/
        marca = findViewById(R.id.marcaICB);
        modelo = findViewById(R.id.modeloICB);
        rut = findViewById(R.id.rutICB); /*Autocompletar con el rut datos de la persona*/
        nombre = findViewById(R.id.nombreICB);
        nacionalidad = findViewById(R.id.nacionalidadICB);
        nacionalidad.setText("CHILENA");
    }

    private void setOnClickListeners() {

        patente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String patenteChecker = UtilsOCR.esPatenteChilena(s.toString());
                if (s.toString().trim().length() == 6) {
                    vehiculo.setText("Cargando...");
                    marca.setText("Cargando...");
                    modelo.setText("Cargando...");
                    if (patenteChecker != null) {
                        getPatente(s.toString());
                    } else {
                        new AlertDialog.Builder(IdentityControlCarab.this)
                                .setTitle("Información")
                                .setMessage("Formato de patente inválido, verifique la patente")
                                //.setIcon(R.drawable.aporte)
                                .setPositiveButton(android.R.string.yes, null)
                                .show();
                        vehiculo.setText("");
                        marca.setText("");
                        modelo.setText("");
                    }
                }

            }
        });

        rut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                Log.v("sdkfh","s: "+s.toString());
                if(s.toString().length() == 8){
                    String str = s.toString();
                    str = str.replace("-","");
                    String antesDelGuion = str.substring(0,7);
                    String digitoVerificador = str.substring(7,str.length());
                    rut.removeTextChangedListener(this);
                    rut.setText(antesDelGuion+"-"+digitoVerificador);
                    String rutFinal = antesDelGuion+"-"+digitoVerificador;
                    rut.addTextChangedListener(this);

                    if(rutFinal.length()>9 && rutFinal.matches("^(([1-9]\\d{1}(\\d{3}){2}-[\\dkK])||([1-9](\\d{3}){2}-[\\dkK]))$")){
                        nombre.setText("Cargando...");

                        if(validarRut(rutFinal)){
                            String result = formatRut(rutFinal);
                            Log.v("rutPersona", "" + result);
                            getRut(result);
                        }
                        else {
                            new AlertDialog.Builder(IdentityControlCarab.this)
                                    .setTitle("Información")
                                    .setMessage("Formato de rut inválido, verifique el rut")
                                    //.setIcon(R.drawable.aporte)
                                    .setPositiveButton(android.R.string.yes, null)
                                    .show();
                            nombre.setText("");
                        }

                    }

                } else if(s.toString().length() == 10) {
                    String str = s.toString();
                    str = str.replace("-","");
                    String antesDelGuion = str.substring(0,8);
                    String digitoVerificador = str.substring(8,str.length());
                    rut.removeTextChangedListener(this);
                    rut.setText(antesDelGuion+"-"+digitoVerificador);
                    String rutFinal = antesDelGuion+"-"+digitoVerificador;
                    rut.addTextChangedListener(this);
                    Log.v("sdkfh","pase por aca s: "+rutFinal);
                    if(rutFinal.length()>9 && rutFinal.matches("^(([1-9]\\d{1}(\\d{3}){2}-[\\dkK])||([1-9](\\d{3}){2}-[\\dkK]))$")){
                        nombre.setText("Cargando...");
                        if(validarRut(rutFinal)){
                            String result = formatRut(rutFinal);
                            Log.v("rutPersona", "." + result+".");
                            getRut(result);
                        }
                        else {
                            new AlertDialog.Builder(IdentityControlCarab.this)
                                    .setTitle("Información")
                                    .setMessage("Formato de rut inválido, verifique el rut")
                                    //.setIcon(R.drawable.aporte)
                                    .setPositiveButton(android.R.string.yes, null)
                                    .show();
                            nombre.setText("");
                        }

                    }

                }
                rut.setSelection(rut.getText().length());

            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(IdentityControlCarab.this)
                        .setTitle("Información")
                        .setMessage("¿Seguro quieres cancelar?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();

            }
        });
    }

    public void getRut(String rut){
        Log.v("rutPersona", "" + rut);
        if(dev){
            API_URL = getResources().getString(R.string.dev_api);
        } else {
            API_URL = getResources().getString(R.string.prod_api);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SenderRut sr = new SenderRut(rut);
        Log.v("rutPersona", "" + sr);
        Call callTime = jsonPlaceHolderApi.postRut(sr);
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    nombre.setText("Fallo en conexión");
                    Log.v("rutPersona", "" + response.code());
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<String[]>() {}.getType();
                String jsonString = gson.toJson(response.body());
                Log.v("rutPersona", "" + response.body());


                if(!jsonString.equals("[]")){
                    String[] yourList = gson.fromJson(jsonString, type);
                    String rutFormateado = formatRut(yourList[0]);
                    nombre.setText(yourList[1]);
                    rs = new RutSensitive(yourList[1], rutFormateado,yourList[2],yourList[3],yourList[4]);
                    db.collection(Referencias.RUTSCRAPEADAS).document(rutFormateado).set(rs);
                }
                else {
                    new AlertDialog.Builder(IdentityControlCarab.this)
                            .setTitle("Información")
                            .setMessage("El rut ingresado no se encuentra registrado, ingrese el nombre manualmente")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                    nombre.setText("");
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("rutPersona", "" + t.getMessage());
            }
        });
    }
    public void getPatente(String patente){
        if(dev){
            API_URL = getResources().getString(R.string.dev_api);
        } else {
            API_URL = getResources().getString(R.string.prod_api);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SenderPatente sp = new SenderPatente(patente);
        Log.v("rutPersona", "" + sp);
        Call callTime = jsonPlaceHolderApi.postPatente(sp);
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.v("rutPersona", "" + response.body());
                if (!response.isSuccessful()) {
                    new AlertDialog.Builder(IdentityControlCarab.this)
                            .setTitle("Información")
                            .setMessage("Fallo en conexión, ingrese los datos del vehículo manualmente")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                    vehiculo.setText("");
                    marca.setText("");
                    modelo.setText("");
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<String[]>() {}.getType();
                String jsonString = gson.toJson(response.body());
                Log.v("rutPersona", "" + response.body());
                if(!jsonString.equals("[]")){
                    String[] yourList = gson.fromJson(jsonString, type);
                    vehiculo.setText(yourList[3]);
                    marca.setText(yourList[4]);
                    modelo.setText(yourList[5]);
                    ps = new PatenteSensitive(yourList[2], yourList[3],yourList[4],yourList[5],yourList[0],yourList[8], yourList[6], yourList[1], yourList[9], yourList[10], yourList[7]);
                    db.collection(Referencias.PATENTESCRAPEADAS).document(yourList[2]).set(ps);
                }
                else {
                    new AlertDialog.Builder(IdentityControlCarab.this)
                            .setTitle("Información")
                            .setMessage("La patente ingresada no se encuentra registrado, ingrese los datos del vehículo manualmente")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                    vehiculo.setText("");
                    marca.setText("");
                    modelo.setText("");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("patentePersona", "" + t.getMessage());
            }
        });
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    private void checkData(){
        View focusView = null;
        boolean cancelUpload = false;
        if(TextUtils.isEmpty(servicio.getText().toString()) || servicio.getText().toString().length()<3){
            servicio.setError("Ingrese el servicio");
            focusView = servicio;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(cuadrante.getText().toString()) || cuadrante.getText().toString().length()<3){
            cuadrante.setError("Ingrese el servicio");
            focusView = cuadrante;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(lugar.getText().toString()) || lugar.getText().toString().length()<3){
            lugar.setError("Ingrese un lugar");
            focusView = lugar;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(patente.getText().toString())){
            patente.setError("Ingrese una patente");
            focusView = patente;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(vehiculo.getText().toString())){
            vehiculo.setError("Ingrese un vehiculo");
            focusView = vehiculo;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(marca.getText().toString())){
            marca.setError("Ingrese una marca");
            focusView = marca;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(modelo.getText().toString())){
            modelo.setError("Ingrese un modelo");
            focusView = modelo;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(rut.getText().toString())){
            rut.setError("Ingrese un rut");
            focusView = rut;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(nombre.getText().toString())){
            nombre.setError("Ingrese un nombre");
            focusView = nombre;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(dia.getText().toString())){
            dia.setError("Ingrese un día");
            focusView = dia;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(mes.getText().toString())){
            mes.setError("Ingrese un mes");
            focusView = mes;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(anio.getText().toString())){
            anio.setError("Ingrese un año");
            focusView = anio;
            cancelUpload = true;
        }
        if(TextUtils.isEmpty(nacionalidad.getText().toString())){
            nacionalidad.setError("Ingrese una nacionalidad");
            focusView = nacionalidad;
            cancelUpload = true;
        }
        if (cancelUpload){
            focusView.requestFocus();
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                new AlertDialog.Builder(IdentityControlCarab.this)
                        .setTitle("Información")
                        .setMessage("No ha activado los permisos de gps, será enviado de regreso a la pantalla anterior")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                return;
            }
            clientICC.getLastLocation().addOnSuccessListener(IdentityControlCarab.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    if (location != null) {
                        lugarActualLat = String.valueOf(location.getLatitude());
                        lugarActualLng = String.valueOf(location.getLongitude());
                        stageData();
                        cleanData();
                        new AlertDialog.Builder(IdentityControlCarab.this)
                                .setTitle("Información")
                                .setMessage("El control se ha subido exitósamente")
                                .setPositiveButton(android.R.string.yes, null)
                                .show();
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                }
            });
        }

    }

    private void stageData(){

        dateHour();
        String fechaNac = dateSetter();
        String emailCarabFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(IdentityControlCarab.this, Referencias.CORREO);
        String nombreCarabFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(IdentityControlCarab.this, Referencias.NOMBRE);
        String apellidoCarabFirebase = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorString(IdentityControlCarab.this, Referencias.APELLIDO);
        List<String> arrayListGrupos=new ArrayList<>();
        Set<String> gruposCarabFirebase = Utils.leerValorSetString(IdentityControlCarab.this, Referencias.GRUPO);
        arrayListGrupos.addAll(gruposCarabFirebase);
        ControlVehicular cv = new ControlVehicular(lugar.getText().toString(), patente.getText().toString(), vehiculo.getText().toString(),
                marca.getText().toString(), modelo.getText().toString(), formatRut(rut.getText().toString()), nombre.getText().toString(),
                fechaNac, nacionalidad.getText().toString(), servicio.getText().toString(), cuadrante.getText().toString(),
                FieldValue.serverTimestamp(), hour, date,emailCarabFirebase, nombreCarabFirebase ,apellidoCarabFirebase, arrayListGrupos, lugarActualLat, lugarActualLng);

        db.collection(Referencias.CONTROLVEHICULARCARAB).add(cv);

    }

    private void cleanData(){
        vehiculo.setText(null);
        patente.setText(null);
        marca.setText(null);
        modelo.setText(null);
        rut.setText(null);
        nombre.setText(null);
        dia.setText(null);
        mes.setText(null);
        anio.setText(null);
    }

    private void dateHour(){
        calendar = Calendar.getInstance();
        hourFormat = new SimpleDateFormat("hh:mm a");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        hour =hourFormat.format(calendar.getTime());
        date = dateFormat.format(calendar.getTime());
    }

    private String dateSetter(){
        String diaNac, mesNac, anioNac;
        if(dia.getText().toString().equals("")){diaNac= twoDigits(1);}
        else {diaNac = twoDigits(Integer.valueOf(dia.getText().toString()));}
        if(mes.getText().toString().equals("")){mesNac= twoDigits(1);}
        else {mesNac = twoDigits(Integer.valueOf(mes.getText().toString()));}
        if(anio.getText().toString().equals("")){anioNac= twoDigits(1111);}
        else{anioNac = anio.getText().toString();}



        String fdn = diaNac+"-"+mesNac+"-"+anioNac;
        return fdn;
    }

    public boolean validarRut(String rut) {

        boolean validacion = false;
        try {

            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace(",", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);


            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;

            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }

        return validacion;
    }

    private String formatRut(String rut) {
        // Despejar Puntos

        String valor = rut.replace(".","");
        valor = rut.replace(",","");
        // Despejar Guión
        valor = valor.replace(".","");

        // Aislar Cuerpo y Dígito Verificador
        Integer cuerpo = Integer.parseInt(valor.substring(0, valor.length()-2)) ;
        String dv = valor.substring(valor.length()-1).toUpperCase();

        String cuerpoRut = String.format("%,d", cuerpo);

        cuerpoRut = cuerpoRut.replaceAll(",",".");
        // Formatear RUN
        rut = cuerpoRut + "-"+ dv;

        return rut;


    }

    public void permissionDialogGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(IdentityControlCarab.this)
                    .setTitle("Debes activar esta funcionalidad")
                    .setCancelable(false)
                    .setMessage("Necesitamos el GPS para realizar el registro del control vehicular, tras aceptar su uso podrá subir los controles vehiculares.")
                    //.setIcon(R.drawable.aporte)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(IdentityControlCarab.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSIONS_GPS_REQUEST);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


}