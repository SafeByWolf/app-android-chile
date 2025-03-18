package com.safebywolf.safebywolf.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.TimeOffset;
import com.safebywolf.safebywolf.Model.UsuarioAndroid;
import com.safebywolf.safebywolf.Model.UserLoginTask;
import com.safebywolf.safebywolf.Model.UsuarioPatente;
import com.safebywolf.safebywolf.Model.VersionNueva;
import com.safebywolf.safebywolf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Serializable {
    int completeCount = 0;
    Uri fileUri;
    Timestamp expiredSession;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserLoginTask mAuthTask = null;
    private static final String TAG = LoginActivity.class.getName();
    public AutoCompleteTextView mEmailView;
    VersionNueva versionNueva;
    String emailUsuarioFirebase;
    public EditText mPasswordView;
    public View mProgressView;
    public View mLoginFormView;
    private ProgressDialog pDialog;
    public View mCarabSelectorView;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public static final int progress_bar_type = 0;
    public TextView registrar;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS = 101;
    private int  DELETE_CODE = 100;
    Intent intentUpdate;

    public TextView textViewOlvidoContrasña;
    public boolean sesionIniciada = false;
    LinearLayout linearLayoutProgressBar;
    ScrollView scrollView;
    ArrayList<String> arrayListPatentes;
    Button botonIngresar;

    String tokenFirebaseInstalation;
    String tokenFirebaseMessaging;
    String tokenFirebaseSession;

    boolean isPolicia = false;
    boolean isSoap = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prod);
        TextView textViewVersion = findViewById(R.id.textViewVersion);

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            textViewVersion.setText(BuildConfig.VERSION_NAME + " Dev");
        } else {
            textViewVersion.setText(BuildConfig.VERSION_NAME);
        }

        ImageView imageViewLogoEsquina;
        imageViewLogoEsquina = findViewById(R.id.imageViewLogo);
        imageViewLogoEsquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safebywolf.cl"));
                startActivity(browserIntent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        linearLayoutProgressBar = findViewById(R.id.linearLayoutProgressBar);
        scrollView = findViewById(R.id.scroll);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        textViewOlvidoContrasña = findViewById(R.id.textViewOlvideContrasena);

        registrar = findViewById(R.id.textViewRegistro);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());

        botonIngresar = (Button) findViewById(R.id.botonIngresar);
        showProgress(true);


        if (Utils.leerValorBoolean(LoginActivity.this, Referencias.SESIONINICIADA)) {
            String key = Utils.leerValorString(LoginActivity.this, Referencias.IDUSUARIO);
            String nombre = Utils.leerValorString(LoginActivity.this, Referencias.NOMBRE);
            String apellido = Utils.leerValorString(LoginActivity.this, Referencias.APELLIDO);
            String correo = Utils.leerValorString(LoginActivity.this, Referencias.CORREO);
            String password = Utils.leerValorString(LoginActivity.this, Referencias.PASSWORD);
            String fechaNac = Utils.leerValorString(LoginActivity.this, Referencias.FECHANACIMIENTO);
            Set<String> patentes = Utils.leerValorSetString(LoginActivity.this, Referencias.PATENTES);
            List<String> arrayListPatentes = new ArrayList<>();
            arrayListPatentes.addAll(patentes);

            Log.v("reautenticate", "correo: " + correo + " password: " + password);


            //mEmailView.setText(correo);
            //getUserFirebase(correo);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                getTokenFirebaseInstalation(user.getEmail());
                getTokenFirebaseMessaging(user.getEmail());
            } else {
                Log.v("logins", "nada");
                showProgress(false);
                Utils.logout(db, LoginActivity.this);
            }
        } else {
            showProgress(false);
            // Set up the login form.
        }

        setOnClickListeners();

    }

    public void suscribeTopicNoticia() {
        FirebaseMessaging.getInstance().subscribeToTopic(Referencias.NOTICIA)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void suscribeTopicGrupo(ArrayList<String> grupos) {
        for (int i = 0; i < grupos.size(); i++) {
            String grupo = grupos.get(i).replace(" ", "").toLowerCase(Locale.ROOT);
            FirebaseMessaging.getInstance().subscribeToTopic(grupo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Subscribed";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe failed";
                            }
                            Log.d(TAG, msg);
                            //Toast.makeText(LoginActivity.this, grupo, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void suscribeTopicDescargar() {
        String actualizar = "actualizar";
        FirebaseMessaging.getInstance().subscribeToTopic(actualizar)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(LoginActivity.this, actualizar, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void suscribeTopicNovedad() {
        String novedad = Referencias.NOVEDAD + BuildConfig.FLAVOR.substring(0, 1).toUpperCase() + BuildConfig.FLAVOR.substring(1).toLowerCase();
        FirebaseMessaging.getInstance().subscribeToTopic(novedad)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(LoginActivity.this, novedad, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getTokenFirebaseInstalation(String email){
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed"+ task.getException());
                    return;
                }
                // Get new Instance ID token
                tokenFirebaseInstalation = task.getResult();

                getUserFirebase(email);

                //String msg = getString(R.string.msg_token_fmt, token);
                Log.v("tokendevice","instalation: "+ tokenFirebaseInstalation);
                //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getTokenFirebaseMessaging(String email){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed"+ task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        tokenFirebaseMessaging = task.getResult();

                        // Log and toast
                        Log.v("tokendevice", "FCM: "+tokenFirebaseMessaging);
                    }
                });
    }

    public void setOnClickListeners(){
        textViewOlvidoContrasña.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,OlvidoContrasenaActivity.class);
                startActivity(intent);
            }
        });
        registrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistrarActivity.class);
                startActivity(intent);

            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    iniciarSesionUsuario();
                    return true;
                }
                return false;
            }
        });

        botonIngresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesionUsuario();
            }
        });
    }

    private void ejecutarMainActivity() {
        //showProgress(false);
        boolean tutorialFinalizado = Utils.leerValorBoolean(LoginActivity.this,Referencias.TUTORIALDETECTOR);
        Log.v("tutorial",String.valueOf(tutorialFinalizado));

        Intent intent = null;
        if(tutorialFinalizado){
            intent=new Intent(LoginActivity.this, DetectorActivity.class);
            /*
            if(isPolicia){
                //llamar a actividad menú policia
                intent=new Intent(LoginActivity.this, CarabSelectorActivity.class);

            } else {
                intent=new Intent(LoginActivity.this, DetectorActivity.class);
            }
             */
        } else {
            intent = new Intent(LoginActivity.this, TutorialDetectorActivity.class);
            intent.putExtra("fromDetectorActivity",false);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
    }

    public void downloadFile(String emailUsuarioFirebase, VersionNueva versionNueva){
        if(emailUsuarioFirebase != null && !emailUsuarioFirebase.equalsIgnoreCase("")){
            //actualiza el tokenFirebaseInstalation del usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(emailUsuarioFirebase).update("tokenFirebaseInstalation", "");
        }
        if(versionNueva.getAuthority() != null && !versionNueva.getAuthority().equalsIgnoreCase("") && BuildConfig.APPLICATION_ID.equalsIgnoreCase(versionNueva.getAuthority())){
            String id = versionNueva.getAuthority().replace("com.safebywolf.","");
            new DownloadFileFromURL().execute(id);
        } else {
            String id = BuildConfig.APPLICATION_ID.replace("com.safebywolf.","");
            new DownloadFileFromURL().execute(versionNueva.getUrlDescarga(),id);
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
                        fileUri = Uri.fromFile(file);
                        Log.v("versionappw", "llegue1");
                        if (Build.VERSION.SDK_INT >= 24) {
                            Log.v("versionappw", "entre");
                            fileUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), "com.safebywolf."+authority + ".provider",
                                    file);
                            Log.v("versionappw", fileUri.toString());
                        }


                        intentUpdate = new Intent(Intent.ACTION_VIEW, fileUri);
                        intentUpdate.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                        intentUpdate.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
                        intentUpdate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentUpdate.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intentUpdate);
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

    private void iniciarSesionUsuarioAutomaticamente(UsuarioAndroid usuarioAndroid){

        Log.v("asdasda","login activity account version: "+usuarioAndroid.getAccountVersion());

        //iniciar sesion
        Toast.makeText(LoginActivity.this,"Sesión iniciada con éxito",Toast.LENGTH_SHORT).show();

        updateTokenSession();

        expiredSession = Utils.updateExpiredSession(db);

        guardaSesion(usuarioAndroid);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("accv","requestCode: "+requestCode);
        if (requestCode == DELETE_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Log.v("accv","user accepted the uninstall");
                startActivity(intentUpdate);
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.v("accv","user NOT accepted the uninstall");
            }
        }
    } //onActivityResult

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void iniciarSesionUsuario() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().toLowerCase();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (password.equals("") || password == null){
            cancel = true;
            mPasswordView.setError("Contraseña es obligatoria");
            focusView = mPasswordView;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.v("logins","ssssss ya ps");
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Utils.hideKeyboard(LoginActivity.this);
            showProgress(true);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);

            signInWithEmailAndPassword(email, password);
            Log.v("logins","ya ps");

        }
    }

    private void signInWithEmailAndPassword(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String keyUsuario=task.getResult().getUser().getUid();
                    //getUserDB(email,password);
                    //insertar datos en base de datos
                    /*
                     * Nuevo codigo agregado
                     * */
                    Log.v("logins",keyUsuario);
                    Log.v("logins",task.getResult().getUser().getEmail());
                    getTokenFirebaseMessaging(email);
                    getTokenFirebaseInstalation(email);
                } else{
                    Log.v("logins","error");
                    showProgress(false);
                    Toast.makeText(LoginActivity.this,"Error, usuario o contraseña incorrecta!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("logins","error");
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if(show == false){
            scrollView.setVisibility(View.VISIBLE);
            linearLayoutProgressBar.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.GONE);
            linearLayoutProgressBar.setVisibility(View.VISIBLE);
        }


        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //mLoginFormView=findViewById(R.id.progress_bar);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void getUserFirebase(String username){
        //obtiene un usuario (costo de lectura 1)
        db.collection(Referencias.USUARIO).document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.v("grupossdatoss", ""+document.getData());
//                        IdUsuario usuario = new Gson().fromJson(document.getData().toString(), IdUsuario.class);
                        UsuarioAndroid usuarioAndroid = document.toObject(UsuarioAndroid.class);
                        //comprobar que usuario sea el mismo que creo token para dejar iniciar sesion
                        if(!mismoTokenDeInstalacion(usuarioAndroid) && !tokenExpired(usuarioAndroid)){
                            dialogErrorIngreso("", "Esta cuenta está siendo utilizada por otro usuario, intente mas tarde.");
                            showProgress(false);
                            return;
                        } else {
                            //iniciar sesion automaticamente
                            iniciarSesionUsuarioAutomaticamente(usuarioAndroid);
                            return;
                        }
                    } else {
                        Log.v("logins", "No existe documento");

                    }
                }else {
                    Log.v("logins", "task no es success");

                }
                showProgress(false);
                Utils.logout(db,LoginActivity.this);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("logins", "fallo la caga");
            }
        });
    }

    private boolean mismoTokenDeInstalacion(UsuarioAndroid usuarioAndroid){
        if(usuarioAndroid.getTokenFirebaseInstalation() == null){
            Log.v("tokenExpired","TokenDeInstalacion nulo");
            return true;
        }
        if(usuarioAndroid.getTokenFirebaseInstalation().equalsIgnoreCase("")){
            Log.v("tokenExpired","TokenDeInstalacion vacio, return true");
            return true;
        }
        if(usuarioAndroid.getTokenFirebaseInstalation().equalsIgnoreCase(tokenFirebaseInstalation)){
            Log.v("tokenExpired","TokenDeInstalacion idéntico, return true");
            return true;
        }
        if(!usuarioAndroid.getTokenFirebaseInstalation().equalsIgnoreCase(tokenFirebaseInstalation)){
            Log.v("tokenExpired","TokenDeInstalacion distinto, return false");
            return false;
        }
        Log.v("tokenExpired","TokenDeInstalacion motivo desconocido, return false");
        return false;
    }

    private boolean tokenExpired(UsuarioAndroid usuarioAndroid){
        if(usuarioAndroid.getExpiredSession().getTime() - new CurrentDate(new Date()).getDate().getTime() > 0){
            Log.v("tokenExpired","exp time: "+usuarioAndroid.getExpiredSession().getTime() +" current time: "+new CurrentDate(new Date()).getDate().getTime());
            return false;
        }
        Log.v("tokenExpired","tokenExpired expirado retorna true");
        return true;
    }

    public void updateTokenSession(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            tokenFirebaseSession = task.getResult().getToken();
                            Log.v("tokendevice", "session load automatically: "+tokenFirebaseSession);
                            //actualiza los token de un usuario (costo de escritura 1)
                            db.collection(Referencias.USUARIO).document(mUser.getEmail())
                                    .update("tokenFirebaseSession", tokenFirebaseSession,
                                            "tokenFirebaseMessaging", tokenFirebaseMessaging,
                                            "tokenFirebaseInstalation", tokenFirebaseInstalation,
                                            "ultimoIngreso", FieldValue.serverTimestamp(),
                                            "versionApp",BuildConfig.VERSION_NAME);
                        } else {

                        }
                    }
                });
    }

    public void guardaSesion(UsuarioAndroid usuario){

        Log.v("datosuser","getGrupos antes: "+usuario.getGrupos());
        //se almacena grupos de usuario que corresponde a listaGrupoTipo en patenteRobadaVista
        List<Map<String, String>> listaGrupoTipo = usuario.getGrupos();
        /*
        if (listaGrupoTipo != null){
            Log.v("foolala", "Login " + listaGrupoTipo.toString());
            String gruposString = Utils.listMapToString(listaGrupoTipo);
            Utils.guardarValorString(LoginActivity.this, Utils.GRUPOSSTRING, gruposString);
        }
         */

        Set<String> patentes= new HashSet<String>();
        if(usuario.getPatentes()!=null) {
            for (int i = 0; i < usuario.getPatentes().size(); i++) {
                String patente = usuario.getPatentes().get(i);
                patentes.add(patente);
            }
        }

        Set<String> grupos= new HashSet<String>();
        ArrayList<String> arrayListGrupos = new ArrayList<>();
        if(usuario.getGrupo()!=null){
            Iterator<String> iterator = usuario.getGrupo().iterator();
            while (iterator.hasNext()) {
                String grupo = iterator.next();
                Log.v("datosuser","grupo: "+grupo);
                //consulta un grupo (costo de lectura 1 a n)
                db.collection(Referencias.GRUPO).document(grupo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().contains("isAlertGroup")){
                            boolean isAlertGroup = (boolean) task.getResult().get("isAlertGroup");
                            Log.v("datosuser","isAlertGroup "+isAlertGroup);
                            if(isAlertGroup &&
                                    task.getResult().contains("alertGroupName")){
                                String alertGroupName = (String) task.getResult().get("alertGroupName");
                                Log.v("datosuser","alertGroupName "+alertGroupName);
                                for(int i = 0; i < listaGrupoTipo.size(); i++){
                                    Log.v("datosuser","entreee ");
                                    if(listaGrupoTipo.get(i).get("nombre").equalsIgnoreCase(grupo)){
                                        listaGrupoTipo.get(i).put("isAlertGroup", String.valueOf(isAlertGroup));
                                        listaGrupoTipo.get(i).put("alertGroupName",alertGroupName);
                                    }
                                }
                            }
                        }
                        if (completeCount++ == usuario.getGrupo().size()-1) {
                            String gruposString = Utils.listMapToString(listaGrupoTipo);
                            Utils.guardarValorString(LoginActivity.this, Referencias.GRUPOSSTRING, gruposString);
                            //Log.v("datosuser","getGrupos despues: "+usuario.getGrupos());
                            Log.v("listaGrupoTipo","getGrupos despues: "+gruposString);
                            ejecutarMainActivity();
                        }
                    }
                });
                arrayListGrupos.add(grupo);
                grupos.add(grupo);
            }

            if(grupos.size()>0){
                Utils.guardarValorSetString(LoginActivity.this,Referencias.GRUPO,grupos);
                suscribeTopicGrupo(arrayListGrupos);
            } else {
                Utils.guardarValorSetString(LoginActivity.this,Referencias.GRUPO,null);
                Utils.guardarValorString(LoginActivity.this, Referencias.GRUPOSSTRING, null);
            }
        } else {
            Log.v("getGruposDeUsuario"," pase por aca otro");
            Utils.guardarValorSetString(LoginActivity.this,Referencias.GRUPO,null);
        }

        //get grupo tipo
        if(usuario.getGrupos()!=null){
            for(int i =0; i<usuario.getGrupos().size();i++){
                if(usuario.getGrupos().get(i).get("tipo").equalsIgnoreCase("Policía")){
                    isPolicia = true;
                }
                if(usuario.getGrupos().get(i).get("tipo").equalsIgnoreCase("Soap") || usuario.isSoap()){
                    isSoap = true;
                }
            }
        }

        isSoap = usuario.isSoap();

        if(!usuario.isTotem()){
            suscribeTopicNoticia();
            suscribeTopicNovedad();
            suscribeTopicDescargar();
        }

        Log.v("patentessize",Integer.toString(patentes.size()));
        sesionIniciada=true;
        Utils.guardarValorBoolean(LoginActivity.this,Referencias.SESIONINICIADA,sesionIniciada);

        Utils.guardarValorBoolean(LoginActivity.this,Referencias.SESIONPOLICIA,isPolicia);
        Utils.guardarValorBoolean(LoginActivity.this,Referencias.SOAP, isSoap);
        Utils.guardarValorBoolean(LoginActivity.this,Referencias.TOTEM,usuario.isTotem());

        Utils.guardarValorSetString(LoginActivity.this,Referencias.PATENTES,patentes);
        Utils.guardarValorString(LoginActivity.this,Referencias.IDUSUARIO,usuario.getId());
        Utils.guardarValorString(LoginActivity.this,Referencias.ACCVERSION,usuario.getAccountVersion());
        Utils.guardarValorString(LoginActivity.this,Referencias.TOKENFIREBASEINSTALATION,tokenFirebaseInstalation);
        Utils.guardarValorString(LoginActivity.this,Referencias.NOMBRE,usuario.getNombre());
        Utils.guardarValorString(LoginActivity.this,Referencias.APELLIDO,usuario.getApellido());
        Utils.guardarValorString(LoginActivity.this,Referencias.CORREO,usuario.getEmail());
        Utils.guardarValorString(LoginActivity.this,Referencias.CONTACTO,usuario.getContacto());
        Utils.guardarValorString(LoginActivity.this,Referencias.PASSWORD,usuario.getPassword());

        Utils.guardarValorString(LoginActivity.this,Referencias.EXPIREDSESSION, String.valueOf(expiredSession.toDate().getTime()));
        Utils.guardarValorString(LoginActivity.this,Referencias.IP,usuario.getIp());

        Utils.guardarValorString(LoginActivity.this,Referencias.TAG,usuario.getTag());
        Utils.guardarValorString(LoginActivity.this,Referencias.MODELTYPE,usuario.getModelType());
        Utils.guardarValorBoolean(LoginActivity.this,Referencias.AUTOSINPATENTE,usuario.isAutoSinPatente());

        Utils.guardarValorLong(LoginActivity.this,Referencias.OFFSETTIME,TimeOffset.getOffset());

        if(usuario.getGrupos() == null || (usuario.getGrupos() != null && usuario.getGrupos().size() == 0)){
            ejecutarMainActivity();
        }
    }

    /*
    private void logout() {
        Utils.guardarValorBoolean(LoginActivity.this, Utils.SESIONINICIADA, false);
        Utils.guardarValorString(LoginActivity.this, Utils.IDUSUARIO, "");
        Toast.makeText(LoginActivity.this,"Correo no registrado",Toast.LENGTH_SHORT).show();
    }
     */

    public void dialogErrorIngreso(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("ok, entiendo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.logoutSinUpdatear(LoginActivity.this);
                    }
                })
                .show();
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
}

