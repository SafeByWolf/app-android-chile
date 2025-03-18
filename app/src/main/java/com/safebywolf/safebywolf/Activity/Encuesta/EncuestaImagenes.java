package com.safebywolf.safebywolf.Activity.Encuesta;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Interface.EncuestaApi;
import com.safebywolf.safebywolf.Model.EncuestaPatrullero;
import com.safebywolf.safebywolf.R;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EncuestaImagenes extends DialogFragment {

    private static final int REQUEST_CODE_PERMISSION_IMAGES = 1001;
    public static final String TAG = "ENCUESTA_IMAGENES_DIALOG_FRAGMENT";
    private EncuestaPatrullero encuesta;
    private ImageView imageViewImagenResuelta, imageViewEncuestaGaleria, imageViewEncuestaCamara;
    private Button btnFinalizarEncuesta, btnEncuestaImagenesVolver;
    private boolean cargandoImagen = false;
    private TextView textViewSubirImagenCamara, textViewSubirImagenGaleria;
    private LinearLayout linearLayoutSubirImagenCamara, linearLayoutSubirImagenGaleria;
    private Uri imageUri;
    private static final int PICK_IMAGE = 100;

    private String json;
    private EncuestaManager encuestaManager;
    int id;

    public EncuestaImagenes(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_encuesta_imagenes, null);
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Utils.touchListener(child, getActivity());
        }
        Utils.touchListener(view.findViewById(R.id.btnEncuestaImagenesFinalizar), getActivity());
        Utils.touchListener(view.findViewById(R.id.btnEncuestaImagenesVolver), getActivity());

        id = getId();

        // Crear el diálogo
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        encuestaManager = EncuestaManager.getInstacia();

        imageViewEncuestaGaleria = view.findViewById(R.id.imageViewEncuestaGaleria);
        imageViewEncuestaCamara = view.findViewById(R.id.imageViewEncuestaCamara);
        imageViewImagenResuelta = view.findViewById(R.id.imageViewImagenResuelta);

        btnFinalizarEncuesta = view.findViewById(R.id.btnEncuestaImagenesFinalizar);
        btnEncuestaImagenesVolver = view.findViewById(R.id.btnEncuestaImagenesVolver);

        textViewSubirImagenGaleria = view.findViewById(R.id.textViewSubirImagenGaleria);
        textViewSubirImagenCamara = view.findViewById(R.id.textViewSubirImagenCamara);

        linearLayoutSubirImagenCamara = view.findViewById(R.id.linearLayoutSubirImagenCamara);
        linearLayoutSubirImagenGaleria = view.findViewById(R.id.linearLayoutSubirImagenGaleria);

        // Obtener el parámetro del Bundle
        Bundle bundle = getArguments();
        if(bundle.containsKey("Encuesta")) {
            encuesta = (EncuestaPatrullero) bundle.getSerializable("Encuesta");
            Gson gson = new Gson();
            json = gson.toJson(encuesta);
            Log.v("ENCUESTAPATRULLERO","EncuestaObservacion:"+json);
        }


        // Recursos de la camara
        imageViewEncuestaCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        linearLayoutSubirImagenCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        textViewSubirImagenCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Permisos concedidos, abrir el selector de imágenes
                openImagePicker();
            }
        });

        // Fin recursos de la camara

        // Recursos de galeria
        imageViewEncuestaGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        linearLayoutSubirImagenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        textViewSubirImagenGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btnFinalizarEncuesta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri);
                    btnFinalizarEncuesta.setEnabled(false);
                    btnEncuestaImagenesVolver.setEnabled(false);
                } else {
                    enviarEncuesta();
                    btnFinalizarEncuesta.setEnabled(false);
                    btnEncuestaImagenesVolver.setEnabled(false);
                }

            }
        });

        btnEncuestaImagenesVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaObservacion dialogFragment = new EncuestaObservacion();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(requireActivity().getSupportFragmentManager(), "dialog_fragment");
            }
        });

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            // Actualiza la UI con la imagen seleccionada
            imageViewImagenResuelta.setImageURI(uri);
            // Ajuste del LayoutParams
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageViewImagenResuelta.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            imageViewImagenResuelta.setLayoutParams(layoutParams);
            imageUri = uri;
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }


    /**
     * TODO: REVISAR ESTE METODO YA QUE ESTA RETORNANDO NULL EL NOMBRE DEL URI.
     *
     * Se obtiene el nombre de un archivo uri.
     * @param uri El URi del que se obtendra su nombre.
     * @return Se retorna un string que sera el nombre del archivo URI.
     */
    private String getFileNameFromUri(Uri uri) {
        String filename = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = EncuestaImagenes.this.getContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                filename = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return filename;
    }


    /**
     * Sube una imagen a Firebase Storage y actualiza la URL de descarga en la encuesta.
     *
     * @param uri El URI de la imagen que se desea subir a Firebase Storage
     */
    private void uploadImageToFirebase(Uri uri) {
        cargandoImagen = true;
        btnFinalizarEncuesta.setText("CARGANDO...");

        // Generar un nombre de archivo único usando timestamp
        String uniqueImageName = String.valueOf(System.currentTimeMillis());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("imagesEncuestaPatrullero")
                .child(encuesta.getCorreoVigia())
                .child(uniqueImageName);

        Log.v("ENCUESTAPATRULLERO", "uploadImageToFirebase....");

        storageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(EncuestaImagenes.this.getActivity(), "La imagen se subió exitosamente.", Toast.LENGTH_SHORT).show();
                        Log.v("ENCUESTAPATRULLERO", "La imagen se subió exitosamente...");

                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                encuesta.setImagenEncuestaObservacion(downloadUri.toString());
                                Log.v("ENCUESTAPATRULLERO", "La URL de descarga de la imagen se completó...");
                                cargandoImagen = false;
                                btnFinalizarEncuesta.setText("FINALIZAR");
                                enviarEncuesta();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(EncuestaImagenes.this.getActivity(), "Ocurrió un error al subir la imagen", Toast.LENGTH_SHORT).show();
                        Log.v("ENCUESTAPATRULLERO", "Ocurrió un error al subir la imagen...");

                        cargandoImagen = false;
                        btnFinalizarEncuesta.setText("FINALIZAR");
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_IMAGES) {
            boolean permisoAlmacenamientoConcedido = false;
            boolean permisoCamaraConcedido = false;

            // Verifica cada permiso
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    permisoAlmacenamientoConcedido = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                } else if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    permisoCamaraConcedido = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            if (permisoAlmacenamientoConcedido && permisoCamaraConcedido) {
                // Ambos permisos concedidos, abrir el selector de imágenes
                openImagePicker();
            } else {
                // Algún permiso fue denegado, muestra un mensaje al usuario
                Toast.makeText(EncuestaImagenes.this.getActivity(), "Permisos de cámara y almacenamiento son necesarios", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * el metodo abre el imagepicker solo para camara, en caso de no ser necesario solo camara quitar la linea .cameraOnly()
     */
    private void openImagePicker() {
        ImagePicker.with(EncuestaImagenes.this)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(1280,720)
                .start();
    }

    private void habilitarBoton() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(cargandoImagen){
                    btnFinalizarEncuesta.setText("CARGANDO...");
                    btnFinalizarEncuesta.setBackgroundColor(R.color.gray);
                } else {
                    btnFinalizarEncuesta.setText("FINALIZAR");
                    btnFinalizarEncuesta.setBackgroundColor(R.color.colorPrimary);
                }
                btnFinalizarEncuesta.setEnabled(!cargandoImagen);
            }
        });

    }

    private void dismissAll(){
        // Cerrar todos los DialogFragments
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                ((DialogFragment) fragment).dismiss();
            }
        }
    }

    private void enviarEncuesta() {
        String API_URL = BuildConfig.BUILD_TYPE.equalsIgnoreCase("release") ? getResources().getString(R.string.prod_api) : getResources().getString(R.string.dev_api);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        EncuestaApi encuestaApi = retrofit.create(EncuestaApi.class);
        encuesta.setFinalizado(true);
        Gson gson = new Gson();
        String encuestaJson = gson.toJson(encuesta);

        // Crear el cuerpo de la solicitud HTTP
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), encuestaJson);
        Call callTime = encuestaApi.enviarEncuesta(requestBody);
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    //Toast.makeText(EncuestaImagenes.this.getContext(), "Reporte enviado exitosamente!", Toast.LENGTH_SHORT).show();
                    Log.v("ENCUESTAPATRULLERO", "Se envió la encuesta....");
                    // Crear un Bundle y agregar un parámetro
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Encuesta", encuesta);
                    // Mostrar un DialogFragment con el parámetro
                    EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");

                    // Cerrar todos los DialogFragments
                    dismissAll();

                } else {
                    Log.v("ENCUESTAPATRULLERO", "onFailure:" + response.toString());
                    Log.v("ENCUESTAPATRULLERO", "Error al enviar la encuesta con imágen...");
                    //Toast.makeText(EncuestaImagenes.this.getContext(), "Error al enviar el reporte", Toast.LENGTH_SHORT).show();
                    encuestaManager.agregarEncuestaEnvioPendiente(encuesta);

                    // Cerrar todos los DialogFragments
                    dismissAll();

                    // Crear un Bundle y agregar un parámetro
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Encuesta", encuesta);
                    // Mostrar un DialogFragment con el parámetro
                    EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //Toast.makeText(EncuestaImagenes.this.getContext(), "Error al enviar el reporte", Toast.LENGTH_SHORT).show();
                Log.v("ENCUESTAPATRULLERO","onFailure:" + t.toString());
                Log.v("ENCUESTAPATRULLERO", "Fallo al enviar la encuesta....atroooo...");
                encuestaManager.agregarEncuestaEnvioPendiente(encuesta);
                // Crear un Bundle y agregar un parámetro
                Bundle bundle = new Bundle();
                bundle.putSerializable("Encuesta", encuesta);
                // Mostrar un DialogFragment con el parámetro
                EncuestaFinalizada dialogFragment = new EncuestaFinalizada();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
                // Cerrar todos los DialogFragments
                dismissAll();
            }
        });
    }

}

