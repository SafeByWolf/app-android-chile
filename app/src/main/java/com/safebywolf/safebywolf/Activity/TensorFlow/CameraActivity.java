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

import android.Manifest;
import android.app.AlertDialog;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.safebywolf.safebywolf.Activity.TensorFlow.customview.OverlayView;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.util.Size;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.safebywolf.safebywolf.BuildConfig;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.Model.AutoPatenteEnPantalla;
import com.safebywolf.safebywolf.R;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;

import com.safebywolf.safebywolf.Activity.TensorFlow.env.ImageUtils;

public abstract class CameraActivity extends AppCompatActivity
        implements OnImageAvailableListener,
        Camera.PreviewCallback,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
  boolean firstTime = true;
  byte[] bytes;
  Camera camera;
  public Fragment fragmentCamera;
  Timer timerZoom = null;
  Handler handlerZoom = null;
  boolean isTaskZoom = false;
  boolean zoomEnAumento = true;
  int progressZoom = 0;
  boolean zoomEnDisminucion = false;
  int periodZoomMillis = 50;
  int tiempoEsperaZoom = (1000/periodZoomMillis)/3;
  int indiceTiempoEsperaZoom = 0;

  int zoomMinimo = 12;

  FrameLayout frameLayoutContainer;
  int rotationInicial = 0;
  public static final int REQUEST_CODE_PERMISSION_IMAGES = 1001;
  public static final int PERMISSIONS_CAMERA_REQUEST = 102;
  public static final int PERMISSIONS_GPS_REQUEST = 103;
  public static final int PERMISSIONS_TELEPHONE_REQUEST = 104;
  public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
  int zoomInicial = 0;
  int zoomFinal = 30;
  int progressSeekBarCameraZoom = 21;
  SeekBar seekbar;
  TextView textViewZoom;
  int zoomInicialEnLocalStorage = 0;
  int cantidadAAumentarZoomDinamico = 1;
  int cantidadADisminuirZoomDinamico = 1;
  boolean isUserTotem = false;
  int zoomFinalEnLocalStorage = 0;
  float cantidadAHacerZoom = 0;

  LegacyCameraConnectionFragment legacyCameraConnectionFragment;
  CameraConnectionFragment camera2Fragment;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  protected int previewWidth = 0;
  protected int previewHeight = 0;
  private boolean debug = false;
  public Handler handlerBackgroundThread;
  public HandlerThread handlerThread;
  public boolean useCamera2API;
  private boolean isProcessingFrame = false;
  private byte[][] yuvBytes = new byte[3][];
  private int[] rgbBytes = null;
  private int yRowStride;
  int uvRowStride;
  int uvPixelStride;
  public Runnable postInferenceCallback;
  private Runnable imageConverter;
  Image image = null;
  public Runnable postInferenceCallbackCamera2 = new Runnable() {
    @Override
    public void run() {
      if(image != null) {
        image.close();
        image = null;
      }
    }
  };
  private Runnable imageConverterCamera2 = new Runnable() {
    @Override
    public void run() {
      ImageUtils.convertYUV420ToARGB8888(
              yuvBytes[0],
              yuvBytes[1],
              yuvBytes[2],
              previewWidth,
              previewHeight,
              yRowStride,
              uvRowStride,
              uvPixelStride,
              rgbBytes);
    }
  };
  public Runnable postInferenceCallbackCamera1=
          new Runnable() {
            @Override
            public void run() {
              camera.addCallbackBuffer(bytes);
            }
          };
  private Runnable imageConverterCamera1 = new Runnable() {
    @Override
    public void run() {
      ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
    }
  };
  public Runnable deteccionesPorFrame;

  protected TextView textViewEmailValue, textViewVersionCodeValue, textViewEmailText, textViewVersionCodeText, textViewFrameValue, textViewCropValue, textViewInferenceValue, textViewFPSValue, textViewPatenteValue, textViewTemperaturaCPUValue, textViewTemperaturaBateriaValue,textViewVelocidadValue,textViewFrameText,textViewCropText,textViewInferenceText,textViewFPSText,textViewPatenteText,textViewVelocidadText,textViewMSText,textViewTemperaturaCPUText,textViewTemperaturaBateriaText, textViewOCRValue, textViewModeloValue, textViewOCRText, textViewModeloText;
  protected ImageView imageViewAuto, imageViewPatenteCrop;
  public AutoPatenteEnPantalla autoPatenteEnPantalla, autoPatenteEnPantallaAnterior;
  public ArrayList<AutoPatenteEnPantalla> autoPatenteEnPantallaArrayList = new ArrayList<>();
  public LinearLayout linearLayoutAutoDialog,
          linearLayoutContenedorButtonAndDialog,linearLayoutShow, linearLayoutHide, linearLayoutLeftDialog,linearLayoutRightDialog,linearLayoutTop,linearLayoutBottom ;
  public ImageButton imageButtonHide, imageButtonShow;
  Boolean isOpenDialogPatenteAuto = false;
  FragmentTransaction fragmentTransaction = null;

  private long lastTimestamp = 0;
  private long interval = 100; // Intervalo en milisegundos
  OverlayView trackingOverlay;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    Log.d("cameraactivity","onCreate " + this);
    super.onCreate(null);
    onCreateCamera();
  }

  public void onCreateCamera(){

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    setContentView(R.layout.activity_monitoreo_scanner);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
//    getSupportActionBar().setDisplayShowTitleEnabled(false);
    frameLayoutContainer = findViewById(R.id.container);
    imageButtonHide = findViewById(R.id.imageButtonHide);
    imageButtonShow = findViewById(R.id.imageButtonShow);
    linearLayoutHide = findViewById(R.id.linearLayoutHide);
    linearLayoutShow = findViewById(R.id.linearLayoutShow);

    linearLayoutAutoDialog = findViewById(R.id.linearLayoutAutoDialog);
    linearLayoutContenedorButtonAndDialog = findViewById(R.id.linearLayoutContenedorButtonAndDialog);
    linearLayoutLeftDialog = findViewById(R.id.linearLayoutLeftDialog);
    linearLayoutRightDialog = findViewById(R.id.linearLayoutRightDialog);
    linearLayoutTop = findViewById(R.id.linearLayoutTop);
    linearLayoutBottom = findViewById(R.id.linearLayoutBottom);

    imageViewAuto = findViewById(R.id.imageViewAuto);
    imageViewPatenteCrop = findViewById(R.id.imageViewPatenteCrop);

    rotationInicial = getRotation();
    zoomMinimo = Utils.leerValorInt(CameraActivity.this, Referencias.ZOOMMINIMO, 12);
    isUserTotem = Utils.leerValorBoolean(CameraActivity.this, Referencias.TOTEM);

    if(isUserTotem){
      zoomInicialEnLocalStorage = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraActivity.this, Referencias.ZOOMINICIAL, zoomInicial);
    } else {
      zoomInicialEnLocalStorage = zoomInicial;
    }

    cantidadAAumentarZoomDinamico = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraActivity.this, Referencias.CANTIDADAAUMENTARZOOMDINAMICO, 1);
    cantidadADisminuirZoomDinamico = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraActivity.this, Referencias.CANTIDADADISMINUIRZOOMDINAMICO, 1);

    int intervaloPorDB = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraActivity.this, Referencias.FRAMESPORSEGUNDO, 10);
    if(intervaloPorDB > 0){
      interval = (1000/intervaloPorDB);
    } else {
      interval = 10;
    }

    zoomFinalEnLocalStorage = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraActivity.this, Referencias.ZOOMFINAL, zoomFinal);
    if(zoomFinalEnLocalStorage < 0 || zoomFinalEnLocalStorage > 99){
      zoomFinalEnLocalStorage = zoomFinal;
    }
    progressZoom = zoomInicialEnLocalStorage;

    if (hasPermission()) {
      setFragment();
    } else {
      multiplePermissions();
    }

    textViewZoom = findViewById(R.id.textViewZoom);

    textViewFrameText = findViewById(R.id.textViewFrameText);
    textViewCropText = findViewById(R.id.textViewCropText);
    textViewInferenceText = findViewById(R.id.textViewInferenceText);
    textViewFPSText = findViewById(R.id.textViewFPSText);
    textViewPatenteText = findViewById(R.id.textViewPatenteText);
    textViewVelocidadText = findViewById(R.id.textViewVelocidadText);
    textViewTemperaturaCPUText = findViewById(R.id.textViewTemperaturaCPUText);
    textViewTemperaturaBateriaText = findViewById(R.id.textViewTemperaturaBateriaText);
    textViewEmailText = findViewById(R.id.textViewEmailText);
    textViewVersionCodeText = findViewById(R.id.textViewVersionCodeText);
    textViewOCRText = findViewById(R.id.textViewConfianzaOCRText);
    textViewModeloText = findViewById(R.id.textViewConfianzaModeloText);

    textViewEmailValue = findViewById(R.id.textViewEmailValue);
    textViewVersionCodeValue = findViewById(R.id.textViewVersionCodeValue);
    textViewFrameValue = findViewById(R.id.textViewFrameValue);
    textViewCropValue = findViewById(R.id.textViewCropValue);
    textViewInferenceValue = findViewById(R.id.textViewInferenceValue);
    textViewFPSValue = findViewById(R.id.textViewFPSValue);
    textViewPatenteValue = findViewById(R.id.textViewPatenteValue);
    textViewVelocidadValue = findViewById(R.id.textViewVelocidadValue);
    textViewTemperaturaCPUValue = findViewById(R.id.textViewTemperaturaCPUValue);
    textViewTemperaturaBateriaValue = findViewById(R.id.textViewTemperaturaBateriaValue);
    textViewMSText = findViewById(R.id.textViewMSText);
    textViewOCRValue = findViewById(R.id.textViewConfianzaOCRValue);
    textViewModeloValue = findViewById(R.id.textViewConfianzaModeloValue);


    Log.v("cameraActivty1","se crea?");
  }

  public void setInterval(long interval) {
    this.interval = interval;
  }

  protected int[] getRgbBytes() {
    imageConverter.run();
    return rgbBytes;
  }

  /** Callback for Camera2 API */
  @Override
  public void onImageAvailable(final ImageReader reader) {
    Log.v("cameraActivity","onimage");
    //Toast.makeText(this, "camera 2", Toast.LENGTH_LONG).show();

    // We need wait until we have some size from onPreviewSizeChosen
    if (previewWidth == 0 || previewHeight == 0) {
      return;
    }
    if (rgbBytes == null) {
      rgbBytes = new int[previewWidth * previewHeight];
    }
    try {
      final Image image = reader.acquireLatestImage();

      if (image == null) {
        return;
      }

      if (isProcessingFrame) {
        image.close();
        return;
      }
      isProcessingFrame = true;
      Trace.beginSection("imageAvailable");
      final Plane[] planes = image.getPlanes();
      fillBytes(planes, yuvBytes);
      yRowStride = planes[0].getRowStride();
      final int uvRowStride = planes[1].getRowStride();
      final int uvPixelStride = planes[1].getPixelStride();

      imageConverter =
              new Runnable() {
                @Override
                public void run() {
                  ImageUtils.convertYUV420ToARGB8888(
                          yuvBytes[0],
                          yuvBytes[1],
                          yuvBytes[2],
                          previewWidth,
                          previewHeight,
                          yRowStride,
                          uvRowStride,
                          uvPixelStride,
                          rgbBytes);
                }
              };

      postInferenceCallback =
              new Runnable() {
                @Override
                public void run() {
                  if(image!=null) {
                    try {
                      image.close();
                      isProcessingFrame = false;
                    } catch (Exception e){
                      Log.v("exceptionE",e.getMessage());
                    }
                  }
                }
              };

      processImage();
    } catch (final Exception e) {
      Log.e("cameraActivity", "Exception!");
      Trace.endSection();
      return;
    }
    Trace.endSection();
  }

  /** Callback for android.hardware.Camera API */
  @Override
  public void onPreviewFrame(final byte[] bytes, final Camera camera) {
    //Toast.makeText(this, "camera 1", Toast.LENGTH_LONG).show();

    if (isProcessingFrame) {
      Log.v("cameraActivity", "Dropping frame!");
      return;
    }

    try {
      // Initialize the storage bitmaps once when the resolution is known.
      if (rgbBytes == null) {
        //Camera.Size previewSize= LegacyCameraConnectionFragment.getOptimalPreviewSize();
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        previewHeight = previewSize.height;
        previewWidth = previewSize.width;

        rgbBytes = new int[previewWidth * previewHeight];
        Log.v("cameraActivity","Camera activity previewSize: "+previewWidth+"x"+previewHeight);
        onPreviewSizeChosen(new Size(previewWidth, previewHeight), getRotation());
      }
    } catch (final Exception e) {
      Log.v("cameraActivity","Exception: "+e.getMessage());
      return;
    }

    isProcessingFrame = true;
    yuvBytes[0] = bytes;
    yRowStride = previewWidth;

    imageConverter =
            new Runnable() {
              @Override
              public void run() {
                ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
              }
            };

    postInferenceCallback =
            new Runnable() {
              @Override
              public void run() {
                camera.addCallbackBuffer(bytes);
                isProcessingFrame = false;
              }
            };
    processImage();
  }

  @Override
  public synchronized void onStart() {
    Log.v("cameraActivity","onStart: ");
    super.onStart();
  }

  @Override
  public synchronized void onResume() {
    Log.v("osdajsda","Se ejecuta onResume() de Camera activity");
    super.onResume();
    startHandlerInferenceCamera();
  }

  @Override
  public synchronized void onPause() {
    Log.v("osdajsda","on pause camera Activity");
    stopHandlerInferenceCamera();

    if(handlerZoom != null){
      handlerZoom.removeCallbacksAndMessages(null);
      handlerZoom = null;
    }
    super.onPause();
  }

  public void startHandlerInferenceCamera(){
    if(handlerThread == null){
      handlerThread = new HandlerThread("inference");
      handlerThread.start();
    }
    if(handlerBackgroundThread == null) {
      handlerBackgroundThread = new Handler(handlerThread.getLooper());
    }
  }

  /** Stops the background thread and its {@link Handler}. */
  public void stopHandlerInferenceCamera() {
    try {
      if(handlerThread == null) {
        return;
      }
      handlerThread.quitSafely();
      handlerThread.join();
      handlerThread = null;

      handlerBackgroundThread = null;
    } catch (final InterruptedException e) {
      Log.v("cameraActivity","Exception: "+e.getMessage());

    }
  }

  @Override
  public synchronized void onStop() {
    Log.d("cameraActivity","onStop " + this);
    super.onStop();
  }

  @Override
  public synchronized void onDestroy() {
    Log.v("cameraActivity","onDestroy ");
    super.onDestroy();
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handlerBackgroundThread != null) {
      handlerBackgroundThread.post(r);
    }
  }

  /*
  @Override
  public void onRequestPermissionsResult(
          final int requestCode, final String[] permissions, final int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

  }
    */

  public static boolean allPermissionsGranted(final int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  public boolean hasPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

  // Returns true if the device supports the required hardware level, or better.
  private boolean isHardwareLevelSupported(
          CameraCharacteristics characteristics, int requiredLevel) {
    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
      return requiredLevel == deviceLevel;
    }
    // deviceLevel is not LEGACY, can use numerical sort
    return requiredLevel <= deviceLevel;
  }

  private String chooseCamera() {
    final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      for (final String cameraId : manager.getCameraIdList()) {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        // We don't use a front facing camera in this sample.
        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
          continue;
        }

        final StreamConfigurationMap map =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (map == null) {
          continue;
        }

        // Fallback to camera1 API for internal cameras that don't have full support.
        // This should help with legacy situations where using the camera2 API causes
        // distorted or otherwise broken previews.
        useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                        || isHardwareLevelSupported(
                        characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);


        Log.i("cameraActivity", ""+useCamera2API);
        Log.v("cameraActivity","Camera API lv2?:"+ useCamera2API);

        return cameraId;
      }
    } catch (CameraAccessException e) {
      Log.e("cameraActivity", "Not allowed to access camera");
    }

    return null;
  }

  public synchronized void cameraClose(){
    if (useCamera2API) {
      if(camera2Fragment != null){
        camera2Fragment.closeCamera();
        camera2Fragment = null;
      }
    } else {
      if(legacyCameraConnectionFragment != null){
        legacyCameraConnectionFragment.stopCamera();
        legacyCameraConnectionFragment = null;
      }
    }
  }

  protected void setFragment() {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int screenWidth = size.x;
    int screenHeight = size.y;

    Log.v("previewR","width: "+screenWidth+" height: "+screenHeight+" rotation: "+display.getRotation());
    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int)(screenWidth*1.35),(int)(screenHeight*1.35));

    lp2.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
    frameLayoutContainer.setLayoutParams(lp2);

    frameLayoutContainer = findViewById(R.id.container);

    String cameraId = chooseCamera();
    seekbar = findViewById(R.id.camera_sb_expose);

    if (useCamera2API) {
      if (camera2Fragment != null){
        camera2Fragment.setUpCameraOutputs();
      } else {
        Log.v("osdajsda","pase al else");

        camera2Fragment =
                CameraConnectionFragment.newInstance(
                        new CameraConnectionFragment.ConnectionCallback() {
                          @Override
                          public void onPreviewSizeChosen(final Size size, final int rotation) {
                            previewHeight = size.getHeight();
                            previewWidth = size.getWidth();
                            //Log.v("previewR","width: "+previewWidth+" height: "+previewHeight+" rotation: "+rotation);
                            CameraActivity.this.onPreviewSizeChosen(size, getRotation());
                          }
                        },
                        this,
                        getLayoutId(),
                        getDesiredPreviewFrameSize(),
                        zoomMinimo,
                        screenWidth,
                        screenHeight);
      }

      Log.v("camera2x","camera");
      seekbar = findViewById(R.id.camera_sb_expose);
      seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          if(fromUser){
            Log.v("camera2x2","onProgressChanged isFromUser");
            setProgress(progress, fromUser);
          }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
          Log.v("camera2x2","onStopTrackingTouch ");
          if(seekBar.getProgress() <= 69){
            if(isUserTotem) {
              com.safebywolf.safebywolf.Class.Utils.Utils.guardarValorInt(CameraActivity.this, Referencias.ZOOMINICIAL, seekBar.getProgress());
            }
          }
          progressZoom = seekBar.getProgress();
        }
      });

      camera2Fragment.setCamera(cameraId);
      fragmentCamera = camera2Fragment;
    } else {

      rgbBytes = null;
      
      Log.v("cameraActivity","frame: "+getRotation());

      legacyCameraConnectionFragment = new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize(), getRotation(), zoomMinimo);

      fragmentCamera = legacyCameraConnectionFragment;

      seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          if (ContextCompat.checkSelfPermission(
                  CameraActivity.this.getApplicationContext(), Manifest.permission.CAMERA) ==
                  PackageManager.PERMISSION_GRANTED) {
            setProgress(progress, fromUser);
          }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
          if(seekBar.getProgress() <= 69){
            if(isUserTotem) {
              com.safebywolf.safebywolf.Class.Utils.Utils.guardarValorInt(CameraActivity.this, Referencias.ZOOMINICIAL, seekBar.getProgress());
            }
          }
          progressZoom = seekBar.getProgress();
        }
      });
    }

    try {
      getFragmentManager().beginTransaction().replace(R.id.container, fragmentCamera).commit();
      /*
      if(index == 0) {
        index++;
        if (fragmentTransaction == null) {
          fragmentTransaction = getFragmentManager().beginTransaction();
        }
        fragmentTransaction.add(R.id.container, fragmentCamera).commit();
      }
       */

      frameLayoutContainer.post(new Runnable() {
        @Override
        public void run() {
          if(isUserTotem){
            setProgress(zoomInicialEnLocalStorage, false);
          } else {
            setProgress(progressZoom, false);
          }
        }
      });
    } catch (Exception e){

    }
  }

  public void cancelTimeTaskZoom(){
    isTaskZoom = false;
    if(handlerZoom != null){
      handlerZoom.removeCallbacksAndMessages(null);
      handlerZoom = null;
    }
  }

  public void zoomHandler(){
    isTaskZoom = true;

    if(handlerZoom != null) {
      return;
    }

    handlerZoom = new Handler();

    Runnable runnableCode = new Runnable() {
      @Override
      public void run() {
        if(handlerZoom != null) {
          handlerZoom.postDelayed(this, periodZoomMillis);
        }
        Log.v("lkg","taskzoom zoomInicialEnLocalStorage: "+zoomInicialEnLocalStorage +" zoomFinalEnLocalStorage: "+zoomFinalEnLocalStorage+ " indiceTiempoEsperaZoom: "+indiceTiempoEsperaZoom);

        if(progressZoom == zoomInicialEnLocalStorage && indiceTiempoEsperaZoom < tiempoEsperaZoom){
          Log.v("lkg","sali aqui 1");
          indiceTiempoEsperaZoom++;
          return;
        }

        if(progressZoom == zoomInicialEnLocalStorage + zoomFinalEnLocalStorage && indiceTiempoEsperaZoom < tiempoEsperaZoom){
          Log.v("lkg","sali aqui 2");
          indiceTiempoEsperaZoom++;
          return;
        }

        if(indiceTiempoEsperaZoom == tiempoEsperaZoom){
          Log.v("lkg","pase por aqui 1");
          indiceTiempoEsperaZoom = 0;
        }

        if(zoomEnAumento){
          Log.v("lkg","taskzoom if");
          zoomEnAumento(cantidadAAumentarZoomDinamico);
          if(progressZoom == zoomInicialEnLocalStorage + zoomFinalEnLocalStorage){
            zoomEnAumento = false;
          }
        } else {
          Log.v("lkg","taskzoom else");
          zoomEnDisminucion(cantidadADisminuirZoomDinamico);
          if(progressZoom == zoomInicialEnLocalStorage) {
            zoomEnAumento = true;
            if(zoomEnDisminucion == true){
              zoomEnDisminucion = false;
            }
          }
        }

      }
    };
    handlerZoom.post(runnableCode);
  }

  public void zoomEnAumento(int cantidadAAumentarZoomDinamico){
    Log.v("zoomend","--------------------------------");
    Log.v("zoomend","zoom en aumento iniciado");
    if (progressZoom + cantidadAAumentarZoomDinamico < 99 && progressZoom < zoomInicialEnLocalStorage + zoomFinalEnLocalStorage &&  progressZoom < 99) {
      progressZoom = progressZoom + cantidadAAumentarZoomDinamico;
      setProgress(progressZoom, false);
    }
    Log.v("zoomend","zoom en aumento finalizado");
  }

  public void zoomEnAumentoTaskZoom(){
    Log.v("zoomend","--------------------------------");
    Log.v("zoomend","zoom en aumento iniciado TaskZoom");
    if (progressZoom < zoomInicialEnLocalStorage + zoomFinalEnLocalStorage &&  progressZoom < 99) {
      progressZoom++;
      setProgress(progressZoom, false);
    } else {
      zoomEnAumento = false;
    }
    Log.v("zoomend","zoom en aumento finalizado TaskZoom");
  }

  public void zoomEnDisminucion(int cantidadADisminuirZoomDinamico){
    Log.v("zoomend","--------------------------------");
    Log.v("zoomend","zoom en disminusion iniciado");
    if (progressZoom > zoomInicialEnLocalStorage &&  progressZoom > 0) {
      if (progressZoom - cantidadADisminuirZoomDinamico > zoomInicialEnLocalStorage && progressZoom - cantidadADisminuirZoomDinamico > 0) {
        progressZoom = progressZoom - cantidadADisminuirZoomDinamico;
      } else {
        progressZoom--;
      }
      setProgress(progressZoom, false);
    }
    Log.v("zoomend","zoom en disminusion finalizado");
  }

  public void zoomEnDisminucionAlEstarEscaneandoUnVehiculoPorMuchoTiempo(int cantidadADisminuirZoomDinamico){
    Log.v("zoomend","--------------------------------");
    Log.v("zoomend","zoom en disminusion iniciado zoomEnDisminucionAlEstarEscaneandoUnVehiculoPorMuchoTiempo");
    if (progressZoom > zoomInicialEnLocalStorage &&  progressZoom > 0) {
      progressZoom--;
      setProgress(progressZoom, false);
    }
    Log.v("zoomend","zoom en disminusion finalizado zoomEnDisminucionAlEstarEscaneandoUnVehiculoPorMuchoTiempo");
  }

  public void zoomEnDisminucionTaskZoom(){
    Log.v("zoomend","--------------------------------");
    Log.v("zoomend","zoom en disminusion iniciado TaskZoom");
    if (progressZoom > zoomInicialEnLocalStorage &&  progressZoom > 0) {
      progressZoom--;
      setProgress(progressZoom, false);
    } else {
      zoomEnAumento = true;
    }
    Log.v("zoomend","zoom en disminusion finalizado TaskZoom");
  }

  public void zoomReset(){
    progressZoom = 0;
    setProgress(0,false);
  }

  public void setProgress(int progress, boolean fromUser){

    if(fromUser){
     // zoomLocalStorage = progress;
      Log.v("zoomTotem","from user true:" + progress);
    } else {
      if(seekbar == null){
        return;
      }
      seekbar.setProgress(progress);

      Log.v("zoomTotem","from user false: " + progress);
    }

    if(useCamera2API){
      if(camera2Fragment == null || camera2Fragment.getZoom() == null || camera2Fragment.getCaptureSession() == null){
        Log.v("zoomTotem","soy nulo");
        return;
      }

      progressSeekBarCameraZoom = progress;
      textViewZoom.setText(progress+"");

      float maxZoom=camera2Fragment.getZoom().maxZoom;
      cantidadAHacerZoom = ((progress + zoomMinimo) * (maxZoom))/100;

      Log.v("zoomTotem","cantidadAHacerZoom guardado: "+cantidadAHacerZoom);
      com.safebywolf.safebywolf.Class.Utils.Utils.guardarValorFloat(CameraActivity.this, Referencias.CANTIDADAHACERZOOM, (int)((cantidadAHacerZoom*100)/11.1));

      frameLayoutContainer.post(new Runnable() {
        @Override
        public void run() {
          camera2Fragment.getZoom().setZoom(camera2Fragment.getPreviewRequestBuilder(),cantidadAHacerZoom);
          camera2Fragment.setPreviewRequest(camera2Fragment.getPreviewRequestBuilder().build());
          try {
            camera2Fragment
                    .getCaptureSession()
                    .setRepeatingRequest(
                            camera2Fragment.getPreviewRequest(),
                            camera2Fragment.getCaptureCallback(),
                            camera2Fragment.getBackgroundHandler()
                    );
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } else {
      if(legacyCameraConnectionFragment == null || legacyCameraConnectionFragment.getCamera() == null) {
        Log.v("cameraActivity","soy nulo zoom");
        return;
      }
      progressSeekBarCameraZoom = progress;
      textViewZoom.setText(String.valueOf(progress));
      LegacyCameraConnectionFragment.cameraZoom(legacyCameraConnectionFragment.getCamera(), progress + zoomMinimo);
    }
  }



  protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (int i = 0; i < planes.length; ++i) {
      final ByteBuffer buffer = planes[i].getBuffer();
      if (yuvBytes[i] == null) {
        Log.v("cameraActivity","buffer: "+i+" size: "+buffer.capacity());

        yuvBytes[i] = new byte[buffer.capacity()];
      }
      buffer.get(yuvBytes[i]);
    }
  }

  public boolean isDebug() {
    //return false;
    if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")){
      return true;
    }
    return false;
  }

  protected void readyForNextImage() {
    if (postInferenceCallback != null) {
      postInferenceCallback.run();
    }
  }

  public int getRotation(){
    int angle;
    Display display = CameraActivity.this.getWindowManager().getDefaultDisplay();
    switch (display.getRotation()) {
      case Surface.ROTATION_0: // This is display orientation
        angle = 90; // This is camera orientation
        break;
      case Surface.ROTATION_90:
        angle = 0;
        break;
      case Surface.ROTATION_180:
        angle = 270;
        break;
      case Surface.ROTATION_270:
        angle = 180;
        break;
      default:
        angle = 90;
        break;
    }
    return angle;
  }

  protected int getScreenOrientation() {
    switch (getWindowManager().getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_270:
        return 270;
      case Surface.ROTATION_180:
        return 180;
      case Surface.ROTATION_90:
        return 90;
      default:
        return 0;
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    setUseNNAPI(isChecked);
    if (isChecked) {
      Log.v("cameraActivity","NNAPI");
    }
    else {
      Log.v("cameraActivity", "TFLITE");
    }
  }

  @Override
  public void onClick(View v) {

  }



  protected void showOCR(String ocr) {
    textViewOCRValue.setText(ocr);
  }

  protected void showModelo(String modelo) {
    textViewModeloValue.setText(modelo);
  }

  protected void showFPS(String fps) {
    textViewFPSValue.setText(fps);
  }

  protected void showPatente(String patente) {
    textViewPatenteValue.setText(patente);
  }

  protected void showEmail(String email) {
    textViewEmailValue.setText(email);
  }

  protected void showVersionCode(String versionCode) {
    textViewVersionCodeValue.setText(versionCode);
  }

  protected void showFrameInfo(String frameInfo) {
    textViewFrameValue.setText(frameInfo);
  }

  protected void showCropInfo(String cropInfo) {
    textViewCropValue.setText(cropInfo);
  }

  protected void showVelocidad(String velocidad) {
    textViewVelocidadValue.setText(velocidad);
  }

  protected void showTemperaturaCPU(String temperatura) {
    textViewTemperaturaCPUValue.setText(temperatura);
  }

  protected void showTemperaturaBateria(String temperatura) {
    textViewTemperaturaBateriaValue.setText(temperatura);
  }

  protected void showInference(String inferenceTime) {
    textViewInferenceValue.setText(inferenceTime);
  }

  protected abstract void processImage();

  protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

  protected abstract int getLayoutId();

  protected abstract Size getDesiredPreviewFrameSize();

  protected abstract void setDesiredPreviewFrameSize(int w, int h);

  protected abstract void setNumThreads(int numThreads);

  protected abstract void setUseNNAPI(boolean isChecked);


  public void multiplePermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(
              this, Manifest.permission.CAMERA) ==
              PackageManager.PERMISSION_GRANTED) {
        // You can use the API that requires the permission.
        //startCameraSource();
        //new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION}
      } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected. In this UI,
        // include a "cancel" or "no thanks" button that allows the user to
        // continue using your app without granting the permission.
        Log.v("permissionsCamera", "muestra dialogo cámara");
        permissionDialogCamera();
      } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
        // In an educational UI, explain to the user why your app requires this
        // permission for a specific feature to behave as expected. In this UI,
        // include a "cancel" or "no thanks" button that allows the user to
        // continue using your app without granting the permission.
        Log.v("permissionsCamera", "muestra dialogo GPS");
        permissionDialogGPS();
      } else {
        Log.v("permissionsCamera", "solicita permisos para cámara");
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        ActivityCompat.requestPermissions(CameraActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_MULTIPLE_REQUEST);
      }
    }
  }


  public void permissionDialogPhoneState() {
    new AlertDialog.Builder(CameraActivity.this)
            .setTitle("Activa esta funcionalidad")
            .setCancelable(false)
            .setMessage("Hacemos uso del número de teléfono para enviar alertas.")
            //.setIcon(R.drawable.aporte)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_TELEPHONE_REQUEST);
              }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
              }
            })
            .show();
  }

  public void permissionDialogGPS() {
    new AlertDialog.Builder(CameraActivity.this)
            .setTitle("Activa esta funcionalidad")
            .setCancelable(false)
            .setMessage("Hacemos uso del GPS para saber con exactitud dónde fue visto el vehículo.")
            //.setIcon(R.drawable.aporte)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_GPS_REQUEST);
              }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
              }
            })
            .show();
  }

  public void permissionDialogCamera() {
    new AlertDialog.Builder(CameraActivity.this)
            .setTitle("Activa esta funcionalidad")
            .setCancelable(false)
            .setMessage("Hacemos uso de la cámara del dispositivo para capturar y reconocer las " +
                    " patentes de los vehículos mientras conduces.")
            //.setIcon(R.drawable.aporte)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                Log.v("request", "request camera");
                /*
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_CAMERA_REQUEST);

                 */
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_CAMERA_REQUEST);
              }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
              }
            })
            .show();
  }
}