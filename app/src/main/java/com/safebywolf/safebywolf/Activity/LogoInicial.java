package com.safebywolf.safebywolf.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Model.TimeOffset;
import com.safebywolf.safebywolf.Model.VersionNueva;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogoInicial extends AppCompatActivity {
    ConnectivityManager cm = null;
    NetworkInfo activeNetwork = null;
    boolean isConnected = false;
    long ultimoTiempoConexion = 0;
    VersionNueva versionNueva;
    String emailUsuarioFirebase;

    boolean isTotem = false;
    int i =0;
    String API_URL;
    ImageView logo_AACH;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_inicial_prod);
        //textViewAmbiente=findViewById(R.id.textViewAmbiente);

        if(BuildConfig.FLAVOR.equalsIgnoreCase("knox") || BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
            getServerTime(0);
        } else {
            getVersion(0);
        }
    }

    public boolean getConexion(int i){
        ultimoTiempoConexion = i;
        cm = (ConnectivityManager)LogoInicial.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            ultimoTiempoConexion = 0;
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LogoInicial.this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return isConnected;
    }

    public void getVersion(final int i) {
        Log.v("versionapp",BuildConfig.VERSION_NAME);
        Log.v("versionapp","build flavors: "+BuildConfig.FLAVOR);
        Log.v("versionapp","build BUILD_TYPE: "+BuildConfig.BUILD_TYPE);

        //si usuario es totem pasar de largo sin preguntar si hay actualizaciones
        String emailUsuarioFirebase = Utils.leerValorString(LogoInicial.this, Referencias.CORREO);

        if((emailUsuarioFirebase != null && emailUsuarioFirebase.contains(Referencias.WOLFTOTEM)) || Utils.leerValorBoolean(LogoInicial.this, Referencias.TOTEM)){
            isTotem = true;
            getServerTime(i+1);
            return;
        }

        //consulta la version de app actual (costo de lectura 1)
        db.collection(Referencias.VERSION).document("appFull").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("versionapp", "version obtenida");
                        versionNueva = document.toObject(VersionNueva.class);
                        Log.v("versionapp", "version convertida a objeto");
                        Log.v("versionapp", document.getString("version"));

                        String versionName = BuildConfig.VERSION_NAME;
                        Utils.guardarValorString(LogoInicial.this, Referencias.VERSION, versionName);

                        if(emailUsuarioFirebase == null || (emailUsuarioFirebase != null && emailUsuarioFirebase.equalsIgnoreCase(""))){
                            getServerTime(i+1);
                            return;
                        }

                        if (versionNueva.getVersion().equalsIgnoreCase(versionName) || BuildConfig.BUILD_TYPE.equals("debug")) {
                            getServerTime(i+1);
                        } else {
                            logo_AACH=findViewById(R.id.logo_AACH);
                            logo_AACH.setVisibility(View.GONE);

                            LinearLayout linearLayoutDescarga = findViewById(R.id.linearLayoutDescarga);
                            linearLayoutDescarga.setVisibility(View.VISIBLE);

                            TextView textViewVersionNueva = findViewById(R.id.textViewVersionNueva);
                            textViewVersionNueva.setText("Descarga la nueva versión de SafeByWolf v" + versionNueva.getVersion());
                            textViewVersionNueva.setVisibility(View.VISIBLE);
                            Button buttonDescargar = findViewById(R.id.buttonDescargar);
                            buttonDescargar.setVisibility(View.VISIBLE);
                            buttonDescargar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.v("versionappbutton", "boton descarga");
                                    if (verificarPermisos()) {
                                        downloadFile(emailUsuarioFirebase, versionNueva);

                                    } else {
                                        ActivityCompat.requestPermissions(LogoInicial.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                                    }
                                }
                            });
                        }
                    } else {
                        Log.v("versionapp", "No such document");
                    }
                } else {
                    Log.v("versionapp", "get failed with "+ task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("dattimecs","onFailure: "+e.getMessage());
                Log.v("dattimecs","onFailure i: "+ i);
                if(i<5){
                    Log.v("dattimecs","onFailure i < 5 ");

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    getVersion(i+1);
                }
                if(i >= 5){
                    dialogErrorSinConexionInternet(e);
                }
            }
        });

    }

    public void downloadFile(String emailUsuarioFirebase, VersionNueva versionNueva){
        if(emailUsuarioFirebase != null && !emailUsuarioFirebase.equalsIgnoreCase("")){
            //actualiza el tokenFirebaseInstalation del usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenFirebaseInstalation", "");
        }
        //Log.v("versionapp","BuildConfig.APPLICATION_ID: "+BuildConfig.APPLICATION_ID + " versionNueva.getAuthority(): "+versionNueva.getAuthority());
        if(versionNueva.getAuthority() != null && !versionNueva.getAuthority().equalsIgnoreCase("") && BuildConfig.APPLICATION_ID.equalsIgnoreCase(versionNueva.getAuthority())){
            String id = versionNueva.getAuthority().replace("com.safebywolf.","");
            Log.v("versionapp","Entra aqui BuildConfig.APPLICATION_ID: "+BuildConfig.APPLICATION_ID + " versionNueva.getAuthority(): "+versionNueva.getAuthority());
            new DownloadFileFromURL().execute(id);
        } else {
            String id = BuildConfig.APPLICATION_ID.replace("com.safebywolf.","");
            Log.v("versionapp","Else entra aca BuildConfig.APPLICATION_ID: "+BuildConfig.APPLICATION_ID + " versionNueva.getAuthority(): "+versionNueva.getAuthority());
            new DownloadFileFromURL().execute(versionNueva.getUrlDescarga(),id);
        }

    }

    public boolean verificarPermisos() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                return true;
            }
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void getServerTime(int i) {

        Long offset = Utils.leerValorLong(LogoInicial.this, Referencias.OFFSETTIME,0);
        if(offset != 0){
            if(getConexion(i)){
                Log.v("dattimecsServer","offset: "+offset);
                new TimeOffset(offset);
                startLoginActivity();
                return;
            }
        }

        getConexion(i);
        Log.v("dattimecs", "getServerTime" );

        if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")){
            API_URL = getResources().getString(R.string.prod_api);
            Log.v("release","prod");
        } else {
            Log.v("release","dev");
            API_URL = getResources().getString(R.string.dev_api);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call callTime = jsonPlaceHolderApi.getTime();
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Date myTime = new Date();
                if (!response.isSuccessful()) {
                    Log.v("dattimecs", "" + response.code());
                    return;
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body());
                Log.v("dattimecs", jsonString);
                try {
                    JSONObject objeto = new JSONObject(jsonString);
                    String datetime = objeto.getString("datetime");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date serverDate = dateFormat.parse(datetime);
                    Log.v("dattimecsServer", serverDate.toString());
                    long offset = serverDate.getTime() - myTime.getTime();
                    new TimeOffset(offset);
                    Log.v("timeoffset", "" + TimeOffset.getOffset());
                    startLoginActivity();
                } catch (JSONException | ParseException e) {
                    Log.v("dattimecs", "" +  e.getMessage());
                    return;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                try {
                    if (i<5) {
                        TimeUnit.MILLISECONDS.sleep(2000);
                        getServerTime(i+1);
                    }

                    if(i>=5){
                        if(t.getMessage().contains("Unable to resolve host")){
                            dialogErrorSinConexionInternet((Exception) t);
                        } else {
                            Log.v("adsdas", "error: " + t.getMessage() + "");
                            dialogErrorInicioSesion(t);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startLoginActivity(){
        boolean tutorialFinalizado = Utils.leerValorBoolean(LogoInicial.this, Referencias.TUTORIALREGISTER);
        Log.v("tutorialR",String.valueOf(tutorialFinalizado));
        Intent intent;
        if(tutorialFinalizado){
            intent=new Intent(LogoInicial.this, LoginActivity.class);
        } else {
            intent = new Intent(LogoInicial.this, TutorialRegisterActivity.class);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        finish();
    }

    public void dialogErrorInicioSesion(Throwable e) {
        if(emailUsuarioFirebase != null && !emailUsuarioFirebase.equalsIgnoreCase("")){
            //actualiza el campo errorDescrip que muestra si hubo un error al iniciar sesion (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("error","serverTime","errorDescrip",e.getMessage(),"errorCode",1,"errorTimestamp", FieldValue.serverTimestamp());
        }

        if(isTotem){
            finish();
            return;
        }
        new AlertDialog.Builder(LogoInicial.this)
                .setTitle("No se pudo ingresar al sistema")
                .setCancelable(false)
                .setMessage("Por favor intente más tarde.")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        finish();
                    }
                })
                .show();
    }

    public void dialogErrorSinConexionInternet(Exception e) {

        if(emailUsuarioFirebase != null && !emailUsuarioFirebase.equalsIgnoreCase("")){
            //actualiza el campo errorDescrip que muestra si hubo un error al iniciar sesion (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("error","noConnection","errorDescrip",e.getMessage(),"errorCode",2,"errorTimestamp", FieldValue.serverTimestamp());
        }

        if(isTotem){
            finish();
            return;
        }
        new AlertDialog.Builder(LogoInicial.this)
                .setTitle("No se pudo ingresar al sistema")
                .setCancelable(false)
                .setMessage("Dispositivo sin conexión, por favor conéctate a internet.")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!verificarPermisos()) {
                Toast.makeText(this, "Permisos no admitidos por el usuario.", Toast.LENGTH_SHORT).show();
            } else {
                downloadFile(emailUsuarioFirebase, versionNueva);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Descargando aplicación, por favor espere...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        File file;

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            try {
                Log.v("versionappw", "llegue0");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(f_url[0]);
                String authority = f_url[1];
                Log.v("versionappwname", storageRef.getName());
                file = getOutputMediaFile(storageRef.getName());
                storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        dismissDialog(progress_bar_type);
                        Uri fileUri = Uri.fromFile(file);
                        Log.v("versionappw", "llegue1");
                        if (Build.VERSION.SDK_INT >= 24) {
                            Log.v("versionappw", "entre: "+"com.safebywolf."+authority + ".provider");
                            fileUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), "com.safebywolf."+authority + ".provider",
                                    file);
                            Log.v("versionappw", fileUri.toString());
                        }
                        Log.v("versionappw", "salo3");
                        Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                        intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        LogoInicial.this.startActivity(intent);
                        //activity.finish();
                        //Dismiss Progress Dialog\\

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissDialog(progress_bar_type);
                        //Dismiss Progress Dialog\\
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.v("progresssss", progress + "");
                        //displaying percentage in progress dialog
                        //yourProgressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        publishProgress("" + (int) (progress));
                    }
                });

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(String name) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!file.exists()) {
            file.mkdirs();
            Log.v("dadasd", "se retorna nulo");
        }
        File mediaFile;
        String fileName = name;
        mediaFile = new File(file.getPath() + File.separator + fileName);
        return mediaFile;
    }
}
