package com.safebywolf.safebywolf.Activity.TensorFlow;

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

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Log;

import android.os.Looper;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;
import com.safebywolf.safebywolf.Activity.TensorFlow.customview.AutoFitTextureView;
import com.safebywolf.safebywolf.Activity.TensorFlow.env.ImageUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class LegacyCameraConnectionFragment extends Fragment {
    Size previewSize;
    int minimoValorZoom;
    public LegacyCameraConnectionFragment(
            final Camera.PreviewCallback imageListener, final int layout, final Size desiredSize, int sensorOrientation,
            int minimoValorZoom) {
        this.imageListener = imageListener;
        this.layout = layout;
        this.desiredSize = desiredSize;
        this.sensorOrientation = sensorOrientation;
        this.minimoValorZoom = minimoValorZoom;
    }

    /** Conversion from screen rotation to JPEG orientation. */



    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Camera camera;
    private Integer sensorOrientation;
    private Camera.PreviewCallback imageListener;
    private Size desiredSize;
    /** The layout identifier to inflate for this Fragment. */
    private int layout;
    /** An {@link AutoFitTextureView} for camera preview. */
    private AutoFitTextureView textureView;
    private SurfaceTexture availableSurfaceTexture = null;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link
     * TextureView}.
     */
    private final TextureView.SurfaceTextureListener surfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(
                        final SurfaceTexture texture, final int width, final int height) {
                    availableSurfaceTexture = texture;
                    Log.v("previewSize","LegacyCameraConnectionFragment configureTransform 1 : "+width+"x"+height);
                    startCamera();
                }

                @Override
                public void onSurfaceTextureSizeChanged(
                        final SurfaceTexture texture, final int width, final int height) {
                    Log.v("previewSize","LegacyCameraConnectionFragment configureTransform: "+width+"x"+height);
                    //configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture texture) {}
            };
    /** An additional thread for running tasks that shouldn't block the UI. */
    private HandlerThread backgroundThread = null;
    private Handler backgroundHandler;

    @Override
    public View onCreateView(
            final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.v("camera","create viewwwwwwwwww");
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
        startBackgroundThread();
        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        textureAvailable();
    }

    public void textureAvailable(){
        if (textureView.isAvailable()) {
            Log.v("txav","texture avaible start camera");
            startCamera();
        } else {
            Log.v("txav","texture NO avaible setSurfaceTextureListener");
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        stopCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /** Starts a background thread and its {@link Handler}. */
    private void startBackgroundThread() {
        if(backgroundThread == null){
            backgroundThread = new HandlerThread("CameraBackground");
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
        }
    }

    /** Stops the background thread and its {@link Handler}. */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (final InterruptedException e) {
            Log.e("legacycamera", "Exception!");
        }
    }

    private void startCamera() {
        int index = getCameraId();
        camera = Camera.open(index);
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes != null
                && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<Camera.Size> cameraSizes = parameters.getSupportedPreviewSizes();
        Size[] sizes = new Size[cameraSizes.size()];
        int i = 0;
        for (Camera.Size size : cameraSizes) {
            sizes[i++] = new Size(size.width, size.height);
        }

        previewSize = CameraConnectionFragment.chooseOptimalSize(sizes, desiredSize.getWidth(), desiredSize.getHeight());
        //previewSize = CameraConnectionFragment.getOptimalPreviewSize(sizes, desiredSize.getWidth(),desiredSize.getHeight());

        Log.v("previewSize","chooseOptimalSize previewSize final legacyCameraConnection: "+previewSize.getWidth()+"x"+previewSize.getHeight());

        //setzoom
        int zoom = com.safebywolf.safebywolf.Class.Utils.Utils.leerValorInt(LegacyCameraConnectionFragment.this.getActivity(), Referencias.ZOOMINICIAL, 0) + minimoValorZoom;
        Log.v("zoomTotem","zoomTotem leer valor : "+zoom);

        int maxZoom = parameters.getMaxZoom();
        if(zoom>maxZoom){
            zoom = maxZoom;
        }
        int cantidadAHacerZoom=(zoom*maxZoom)/99;


        parameters.setZoom(cantidadAHacerZoom);

        Log.v("zooma","maxZoom: "+maxZoom);
        Log.v("zooma","zoom: "+zoom);
        Log.v("zooma","cantidadAHacerZoom: "+cantidadAHacerZoom);

        parameters.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());


        try {
            camera.setParameters(parameters);
            camera.setPreviewTexture(availableSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("previewSizeT","textureView.setAspectRatio: "+previewSize.getWidth()+"x"+previewSize.getHeight());


        camera.setPreviewCallbackWithBuffer(imageListener);
        Camera.Size s = camera.getParameters().getPreviewSize();
        camera.addCallbackBuffer(new byte[ImageUtils.getYUVByteSize(previewSize.getWidth(), previewSize.getHeight())]);

        Log.v("previewSize","LegacyCameraConnectionFragment previewSize: "+previewSize.getWidth()+" "+previewSize.getHeight());

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
        } else {
            textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
        }
        camera.setDisplayOrientation(sensorOrientation);
        Log.v("getframeOrientration","legacy: "+sensorOrientation);
        camera.startPreview();
        Log.v("camera","startCamera ready");


    }

    public static void cameraZoom(@NonNull Camera camera, int progress) {
        Camera.Parameters parameters = camera.getParameters();
        int maxZoom = parameters.getMaxZoom();
        int cantidadAHacerZoom=progress*maxZoom/100;
        Log.v("zoomTotem","max zoom: "+maxZoom);
        Log.v("zoomTotem","cantidadAHacerZoom: "+cantidadAHacerZoom);
        if (parameters.isZoomSupported()) {
            if (cantidadAHacerZoom >=0 && cantidadAHacerZoom < maxZoom) {
                parameters.setZoom(cantidadAHacerZoom);
                camera.setParameters(parameters);
            } else {
                // zoom parameter is incorrect
                Log.v("zoomTotem","zoom parameter is incorrect");
            }
        }
    }

    protected void stopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private int getCameraId() {
        CameraInfo ci = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_BACK) return i;
        }
        return -1; // No camera found
    }

    public Camera getCamera() {
        return camera;
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

}