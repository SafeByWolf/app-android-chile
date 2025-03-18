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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;
import com.safebywolf.safebywolf.Activity.TensorFlow.customview.AutoFitTextureView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressLint("ValidFragment")
public class CameraConnectionFragment extends Fragment {
  Zoom zoom;
  int minimoValorZoom;
  int orientation;
  int screenWidth = 1280;
  int screenHeight = 1280;
  /**
   * The camera preview size will be chosen to be the smallest frame by pixel size capable of
   * containing a DESIRED_SIZE x DESIRED_SIZE square.
   */
  private static final int MINIMUM_PREVIEW_SIZE = 720;

  /** Conversion from screen rotation to JPEG orientation. */
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

  private static final String FRAGMENT_DIALOG = "dialog";

  static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  /** A {@link Semaphore} to prevent the app from exiting before closing the camera. */
  private final Semaphore cameraOpenCloseLock = new Semaphore(1);
  /** A {@link OnImageAvailableListener} to receive frames as they are available. */
  private final OnImageAvailableListener imageListener;
  /** The input size in pixels desired by TensorFlow (width and height of a square bitmap). */
  private final Size inputSize;
  /** The layout identifier to inflate for this Fragment. */
  private final int layout;

  private final ConnectionCallback cameraConnectionCallback;

  public static CameraConnectionFragment newInstance(
          final ConnectionCallback callback,
          final OnImageAvailableListener imageListener,
          final int layout,
          final Size inputSize,
          final int minimoValorZoom,
          final int screenWidth,
          final int screenHeight) {
    return new CameraConnectionFragment(callback, imageListener, layout, inputSize, minimoValorZoom, screenWidth, screenHeight);
  }

  public CameraConnectionFragment(
          final ConnectionCallback connectionCallback,
          final OnImageAvailableListener imageListener,
          final int layout,
          final Size inputSize,
          final int minimoValorZoom,
          final int screenWidth,
          final int screenHeight) {
    this.cameraConnectionCallback = connectionCallback;
    this.imageListener = imageListener;
    this.layout = layout;
    this.inputSize = inputSize;
    this.minimoValorZoom = minimoValorZoom;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }


  public final CameraCaptureSession.CaptureCallback captureCallback =
          new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureProgressed(
                    final CameraCaptureSession session,
                    final CaptureRequest request,
                    final CaptureResult partialResult) {
            }

            @Override
            public void onCaptureCompleted(
                    final CameraCaptureSession session,
                    final CaptureRequest request,
                    final TotalCaptureResult result) {
            }
          };
  /** ID of the current {@link CameraDevice}. */
  private String cameraId;
  /** An {@link AutoFitTextureView} for camera preview. */
  public AutoFitTextureView textureView;
  /** A {@link CameraCaptureSession } for camera preview. */
  public CameraCaptureSession captureSession;
  /** A reference to the opened {@link CameraDevice}. */
  private CameraDevice cameraDevice;
  /** The rotation in degrees of the camera sensor from the display. */
  private Integer sensorOrientation;
  /** The {@link Size} of camera preview. */
  private Size previewSize;
  /** An additional thread for running tasks that shouldn't block the UI. */
  private HandlerThread backgroundThread = null;
  /** A {@link Handler} for running tasks in the background. */
  public Handler backgroundHandler;
  /** An {@link ImageReader} that handles preview frame capture. */
  private ImageReader previewReader;
  /** {@link CaptureRequest.Builder} for the camera preview */
  private CaptureRequest.Builder previewRequestBuilder;
  /** {@link CaptureRequest} generated by {@link #previewRequestBuilder} */
  public CaptureRequest previewRequest;
  /** {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state. */
  private final CameraDevice.StateCallback stateCallback =
          new CameraDevice.StateCallback() {
            @Override
            public void onOpened(final CameraDevice cd) {
              // This method is called when the camera is opened.  We start camera preview here.
              cameraOpenCloseLock.release();
              cameraDevice = cd;
              createCameraPreviewSession();
            }

            @Override
            public void onDisconnected(final CameraDevice cd) {
              cameraOpenCloseLock.release();
              cd.close();
              cameraDevice = null;
            }

            @Override
            public void onError(final CameraDevice cd, final int error) {
              cameraOpenCloseLock.release();
              cd.close();
              cameraDevice = null;
              final Activity activity = getActivity();
              if (null != activity) {
                activity.finish();
              }
            }
          };
  /**
   * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link
   * TextureView}.
   */
  private final TextureView.SurfaceTextureListener surfaceTextureListener =
          new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(
                    final SurfaceTexture texture, final int width, final int height) {
              openCamera(width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(
                    final SurfaceTexture texture, final int width, final int height) {
              Log.v("previewSize","configureTransform: "+width+"x"+height);
              configureTransform(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
              return true;
            }

            @Override
            public void onSurfaceTextureUpdated(final SurfaceTexture texture) {
            }
          };


  public static Size chooseOptimalSize(Size[] choices, int targetWidth, int targetHeight) {
    Size targetSize = new Size(targetWidth,targetHeight);
    float targetRatio = (float)targetHeight/targetWidth;
    Log.v("sizec"," targetRatio:"+targetRatio);
    List<Size> sizes = new ArrayList<>();
    for (Size option : choices) {
      float optionRatio = (float)option.getHeight()/ option.getWidth();

      if(targetRatio == optionRatio && option.getWidth() >= targetWidth && option.getHeight() >= targetHeight){
        Log.v("sizec","size: "+option.getWidth()+"x"+option.getHeight()+ " optionRatio: "+optionRatio);
        sizes.add(option);
      }
    }

    //la resolucion mas baja
    int index = sizes.size()-1;
    if(sizes.size() > 3){
      index = (sizes.size()/2);
    }

    Log.v("sizec","index: "+index);
    if(index < 0 || index >= sizes.size()){
      return targetSize;
    }
    return sizes.get(sizes.size()-1);

  }

  /**
   * Shows a {@link Toast} on the UI thread.
   *
   * @param text The message to show
   */
  private void showToast(final String text) {
    final Activity activity = getActivity();
    if(activity != null){
      if (activity != null) {
        activity.runOnUiThread(
                new Runnable() {
                  @Override
                  public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                  }
                });
      }
    }

  }

  @Override
  public View onCreateView(
          final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    return inflater.inflate(layout, container, false);
  }

  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    textureView = (AutoFitTextureView) view.findViewById(R.id.texture);
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.v("bthread","onResume");
    startBackgroundThread();

    // When the screen is turned off and turned back on, the SurfaceTexture is already
    // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
    // a camera and start preview from here (otherwise, we wait until the surface is ready in
    // the SurfaceTextureListener).
    textureAvailable();

  }

  public void textureAvailable(){
    try {
      if (textureView.isAvailable()) {
        Log.v("txav","texture avaible start camera");
        openCamera(textureView.getWidth(), textureView.getHeight());
      } else {
        Log.v("txav","texture NO avaible setSurfaceTextureListener");
        textureView.setSurfaceTextureListener(surfaceTextureListener);
      }
    } catch (Exception e){
      Log.v("ExceptionE",e.getMessage());
    }
  }

  @Override
  public void onPause() {
    Log.v("osdajsda","on pause camera connection");
    closeCamera();
    stopBackgroundThread();
    super.onPause();
  }

  public void setCamera(String cameraId) {
    this.cameraId = cameraId;
  }

  /** Sets up member variables related to camera. */
  public void setUpCameraOutputs() {
    final Activity activity = getActivity();
    if(activity != null){
      final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
      try {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        zoom = new Zoom(characteristics);

        final StreamConfigurationMap map =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        CameraConnectionFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Size[] cameraSizes = map.getOutputSizes(SurfaceTexture.class);

        previewSize = chooseOptimalSize(cameraSizes,1280, 720);

        // We fit the aspect ratio of TextureView to the size of preview we picked.
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
          textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
        } else {
          Log.v("asdasq" , previewSize.getWidth() + "x" + previewSize.getHeight());
          Log.v("asdasq" , screenWidth + "x" + screenHeight);
          //textureView.setAspectRatio(100, 200);
          textureView.setAspectRatio(previewSize.getHeight() , previewSize.getWidth());
        }
      } catch (final CameraAccessException e) {
        Log.e("cameraConnection", "Exception!");
      } catch (final NullPointerException e) {
        // Currently an NPE is thrown when the Camera2API is used but not supported on the
        // device this code runs.
        Log.v("error", e.getMessage());
      }

      cameraConnectionCallback.onPreviewSizeChosen(previewSize, sensorOrientation);
    }

  }

  /** Opens the camera specified by {@link CameraConnectionFragment#cameraId}. */
  private void openCamera(final int width, final int height) {
    setUpCameraOutputs();
    Log.v("wh","width: "+width+" height: "+height);
    final Activity activity = getActivity();
    if(activity != null){
      final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

      try {
        if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
          throw new RuntimeException("Time out waiting to lock camera opening.");
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission(CameraConnectionFragment.this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          // TODO: Consider calling
          //    ActivityCompat#requestPermissions
          // here to request the missing permissions, and then overriding
          //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
          //                                          int[] grantResults)
          // to handle the case where the user grants the permission. See the documentation
          // for ActivityCompat#requestPermissions for more details.
          return;
        }
      }
      try {
        manager.openCamera(cameraId, stateCallback, backgroundHandler);
      } catch (CameraAccessException e) {
        e.printStackTrace();
      }
    }
  }

  /** Closes the current {@link CameraDevice}. */
  public void closeCamera() {
    Log.v("colseca","close camera?");
    try {
      stopBackgroundThread();
      cameraOpenCloseLock.acquire();
      if (null != captureSession) {
        captureSession.close();
        captureSession = null;
      }
      if (null != cameraDevice) {
        cameraDevice.close();
        cameraDevice = null;
      }
      if (null != previewReader) {
        previewReader.close();
        previewReader = null;
      }
    } catch (final InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
    } finally {
      cameraOpenCloseLock.release();
    }
  }

  /** Starts a background thread and its {@link Handler}. */
  private void startBackgroundThread() {
    if(backgroundThread == null){
      backgroundThread = new HandlerThread("ImageListener");
      backgroundThread.start();
      backgroundHandler = new Handler(backgroundThread.getLooper());
    }
  }

  /** Stops the background thread and its {@link Handler}. */
  public void stopBackgroundThread() {
    try {
      if(backgroundThread == null){
        return;
      }
      backgroundThread.quitSafely();
      backgroundThread.join();
      backgroundThread = null;
      backgroundHandler = null;
    } catch (final InterruptedException e) {
      Log.e("cameraConnection", "Exception!");
    }
  }

  /** Creates a new {@link CameraCaptureSession} for camera preview. */
  public void createCameraPreviewSession() {
    try {
      final SurfaceTexture texture = textureView.getSurfaceTexture();
      assert texture != null;

      // We configure the size of default buffer to be the size of camera preview we want.
      texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

      // This is the output Surface we need to start preview.
      final Surface surface = new Surface(texture);

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
      previewRequestBuilder.addTarget(surface);

      Log.i("cameraConnection","Opening camera preview: " + previewSize.getWidth() + "x" + previewSize.getHeight());

      // Create the reader for the preview frames.
      previewReader =
          ImageReader.newInstance(
              previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);

      previewReader.setOnImageAvailableListener(imageListener, backgroundHandler);
      previewRequestBuilder.addTarget(previewReader.getSurface());

      //setZoom

      Log.v("zoomTotem","zoomcamera connectionFragment: "+ minimoValorZoom);
      float maxZoom=getZoom().maxZoom;
      Log.v("cameraActivity","onResume: ");
      int zoom = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(CameraConnectionFragment.this.getActivity(), Referencias.ZOOMINICIAL, 0) + minimoValorZoom;
      Log.v("zoomTotem","zoomTotem leer valor : "+zoom);

      float cantidadAHacerZoom=zoom*maxZoom/100;
      getZoom().setZoom(previewRequestBuilder, cantidadAHacerZoom);

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice.createCaptureSession(
          Arrays.asList(surface, previewReader.getSurface()),
          new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
              // The camera is already closed

              if (null == cameraDevice) {
                return;
              }

              // When the session is ready, we start displaying the preview.
              captureSession = cameraCaptureSession;
              try {
                // Auto focus should be continuous for camera preview.
                previewRequestBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // Flash is automatically enabled when necessary.
                previewRequestBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                // Finally, we start displaying the camera preview.
                previewRequest = previewRequestBuilder.build();

                Log.v("captureSessionX","captureSession NOOO es nulo");
                captureSession.
                        setRepeatingRequest(
                        previewRequest,
                        captureCallback,
                        backgroundHandler);


              } catch (final CameraAccessException e) {
                Log.e("cameraConnection", "Exception!");
              }
            }

            @Override
            public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
              showToast("Failed");
            }

          },
          null);
    } catch (final CameraAccessException e) {
      Log.e("cameraConnection", "Exception!");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();

    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);

    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Configures the necessary {@link Matrix} transformation to `mTextureView`. This method should be
   * called after the camera preview size is determined in setUpCameraOutputs and also the size of
   * `mTextureView` is fixed.
   *
   * @param viewWidth The width of `mTextureView`
   * @param viewHeight The height of `mTextureView`
   */
  private void configureTransform(final int viewWidth, final int viewHeight) {
    final Activity activity = getActivity();
    if(activity != null){
      if (null == textureView || null == previewSize || null == activity) {
        return;
      }
      final int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
      final Matrix matrix = new Matrix();
      final RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
      final RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
      final float centerX = viewRect.centerX();
      final float centerY = viewRect.centerY();
      if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
        matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
        final float scale =
                Math.max(
                        (float) viewHeight / previewSize.getHeight(),
                        (float) viewWidth / previewSize.getWidth());
        matrix.postScale(scale, scale, centerX, centerY);
        matrix.postRotate(90 * (rotation - 2), centerX, centerY);
      } else if (Surface.ROTATION_180 == rotation) {
        matrix.postRotate(180, centerX, centerY);
      }
      textureView.setTransform(matrix);
    }
  }

  /**
   * Callback for Activities to use to initialize their data once the selected preview size is
   * known.
   */
  public interface ConnectionCallback {
    void onPreviewSizeChosen(Size size, int cameraRotation);
  }

  /** Compares two {@code Size}s based on their areas. */
  static class CompareSizesByArea implements Comparator<Size> {
    @Override
    public int compare(final Size lhs, final Size rhs) {
      // We cast here to ensure the multiplications won't overflow
      return Long.signum(
          (long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
    }
  }

  /** Shows an error message dialog. */
  public static class ErrorDialog extends DialogFragment {
    private static final String ARG_MESSAGE = "message";

    public static ErrorDialog newInstance(final String message) {
      final ErrorDialog dialog = new ErrorDialog();
      final Bundle args = new Bundle();
      args.putString(ARG_MESSAGE, message);
      dialog.setArguments(args);
      return dialog;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
      final Activity activity = getActivity();
      return new AlertDialog.Builder(activity)
          .setMessage(getArguments().getString(ARG_MESSAGE))
          .setPositiveButton(
              android.R.string.ok,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, final int i) {
                  activity.finish();
                }
              })
          .create();
    }
  }

  public CameraCaptureSession.CaptureCallback getCaptureCallback() {
    return captureCallback;
  }

  public CameraCaptureSession getCaptureSession() {
    return captureSession;
  }

  public void setCaptureSession(CameraCaptureSession captureSession) {
    this.captureSession = captureSession;
  }

  public Handler getBackgroundHandler() {
    return backgroundHandler;
  }

  public void setBackgroundHandler(Handler backgroundHandler) {
    this.backgroundHandler = backgroundHandler;
  }

  public CaptureRequest.Builder getPreviewRequestBuilder() {
    return previewRequestBuilder;
  }

  public void setPreviewRequestBuilder(CaptureRequest.Builder previewRequestBuilder) {
    this.previewRequestBuilder = previewRequestBuilder;
  }

  public CaptureRequest getPreviewRequest() {
    return previewRequest;
  }

  public Zoom getZoom() {
    return zoom;
  }

  public void setZoom(Zoom zoom) {
    this.zoom = zoom;
  }

  public void setPreviewRequest(CaptureRequest previewRequest) {
    this.previewRequest = previewRequest;
  }

  public static Camera.Size getOptimalPreviewSize(
          List<Camera.Size> sizes, int w, int h) {
    // Use a very small tolerance because we want an exact match.
    final double ASPECT_TOLERANCE = 0.1;
    double targetRatio = (double) w / h;
    if (sizes == null)
      return null;

    Camera.Size optimalSize = null;/*  w  w  w  .ja va  2 s .c  om*/

    // Start with max value and refine as we iterate over available preview sizes. This is the
    // minimum difference between view and camera height.
    double minDiff = Double.MAX_VALUE;

    // Target view height
    int targetHeight = h;

    // Try to find a preview size that matches aspect ratio and the target view size.
    // Iterate over all available sizes and pick the largest size that can fit in the view and
    // still maintain the aspect ratio.
    for (Camera.Size size : sizes) {
      double ratio = (double) size.width / size.height;
      if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
        continue;
      if (Math.abs(size.height - targetHeight) < minDiff) {
        optimalSize = size;
        minDiff = Math.abs(size.height - targetHeight);
      }
    }

    // Cannot find preview size that matches the aspect ratio, ignore the requirement
    if (optimalSize == null) {
      minDiff = Double.MAX_VALUE;
      for (Camera.Size size : sizes) {
        if (Math.abs(size.height - targetHeight) < minDiff) {
          optimalSize = size;
          minDiff = Math.abs(size.height - targetHeight);
        }
      }
    }

    return optimalSize;
  }
}
