/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.safebywolf.safebywolf.Activity.TensorFlow.tracking;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.util.Pair;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.safebywolf.safebywolf.R;
import com.safebywolf.safebywolf.Activity.TensorFlow.env.BorderedText;
import com.safebywolf.safebywolf.Activity.TensorFlow.env.ImageUtils;
import org.tensorflow.lite.examples.detection.tflite.Detector.Recognition;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
  private static final float TEXT_SIZE_DIP = 18;
  private static final float MIN_SIZE = 16.0f;

  /*
  private static final int[] COLORS = {
    Color.BLUE,
    Color.RED,
    Color.GREEN,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.WHITE,
    Color.parseColor("#55FF55"),
    Color.parseColor("#FFA500"),
    Color.parseColor("#FF8888"),
    Color.parseColor("#AAAAFF"),
    Color.parseColor("#FFFFAA"),
    Color.parseColor("#55AAAA"),
    Color.parseColor("#AA33AA"),
    Color.parseColor("#0D0068")
  };
   */

  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  private final Queue<Integer> availableColors = new LinkedList<Integer>();
  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();
  private final Paint boxPaint = new Paint();
  private final float textSizePx;
  private final BorderedText borderedText;
  private Matrix frameToCanvasMatrix;
  private int frameWidth;
  private int frameHeight;
  private int sensorOrientation;
  Context context;

  public MultiBoxTracker(final Context context) {
    this.context=context;

    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(10.0f);
    boxPaint.setStrokeCap(Cap.ROUND);
    boxPaint.setStrokeJoin(Join.ROUND);
    boxPaint.setStrokeMiter(100);

    textSizePx =
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
  }

  public synchronized void setFrameConfiguration(
          final int width, final int height, final int sensorOrientation) {
    frameWidth = width;
    frameHeight = height;
    this.sensorOrientation = sensorOrientation;
  }

  public synchronized void drawDebug(final Canvas canvas, int previewWidth, int previewHeight, int orientation) {

      final boolean rotated = sensorOrientation % 180 == 90;
      final float multiplier =
              Math.min(
                      canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
                      canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
      frameToCanvasMatrix =
              ImageUtils.getTransformationMatrix(
                      frameWidth,
                      frameHeight,
                      (int) (multiplier * (rotated ? frameHeight : frameWidth)),
                      (int) (multiplier * (rotated ? frameWidth : frameHeight)),
                      sensorOrientation,
                      false);


      for (final TrackedRecognition recognition : trackedObjects) {
        final RectF trackedPos = new RectF(recognition.location);

        getFrameToCanvasMatrix().mapRect(trackedPos);


        float width = (recognition.location.width() * 100) / previewWidth;
        float height = (recognition.location.height() * 100) / previewHeight;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
          width = (recognition.location.height() * 100) / previewWidth;

          height = (recognition.location.width() * 100) / previewHeight;
        }

        if (recognition.title.equalsIgnoreCase("patente")) {
          //4
          if (width < 4|| height < 4.1 || height>previewHeight || width > previewWidth) {
            //color rojo
            recognition.color = Color.parseColor("#ff0000");
          } else {
            //color verde
            recognition.color = Color.parseColor("#0dff00");
          }

        } else {
          if (width < 13 || height < 23 || height>previewHeight || width > previewWidth) {
            recognition.color = Color.parseColor("#ff0000");
          } else {
            recognition.color = Color.parseColor("#0dff00");
          }
        }

        boxPaint.setColor(recognition.color);

        float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
        canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);

      final String labelString =
              !TextUtils.isEmpty(recognition.title)
                      ? String.format("%s %.2f", recognition.title, (100 * recognition.detectionConfidence))
                      : String.format("%.2f", (100 * recognition.detectionConfidence));


      final String widthHeight = "Height: "+Math.round(recognition.location.width())+ " Width: "+Math.round(recognition.location.height());

      borderedText.drawText(
              canvas, trackedPos.left + cornerSize, trackedPos.top-50, labelString + "%", boxPaint);

      borderedText.drawText(
              canvas, trackedPos.left + cornerSize, trackedPos.top-100, widthHeight + "%", boxPaint);



        //String labelString = !TextUtils.isEmpty(recognition.title) ? String.format("%s", recognition.title) : String.format("");


        //final String widthHeight = "Height: " + Math.round(recognition.location.width()) + " Width: " + Math.round(recognition.location.height());



        String porcentajeWidth = "width: " + String.valueOf(width);
        String porcentajeHeight = "height: " + String.valueOf(height);


        borderedText.drawText(
                canvas, trackedPos.left + cornerSize, trackedPos.top - 150, porcentajeWidth + "%", boxPaint);

        borderedText.drawText(
                canvas, trackedPos.left + cornerSize, trackedPos.top - 100, porcentajeHeight + "%", boxPaint);


        borderedText.drawText(
                canvas, trackedPos.left + cornerSize, trackedPos.top - 50, labelString, boxPaint);

        borderedText.drawText(
                canvas, trackedPos.left + cornerSize, trackedPos.top - 200, "Confianza: "+String.valueOf(recognition.detectionConfidence*100).substring(0,2)+"%", boxPaint);
      }
  }

  public synchronized void trackResults(final List<Recognition> results) {

    processResults(results);
  }

  private Matrix getFrameToCanvasMatrix() {
    return frameToCanvasMatrix;
  }

  public synchronized void draw(final Canvas canvas) {
    final boolean rotated = sensorOrientation % 180 == 90;
    final float multiplier =
            Math.min(
                    canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
                    canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
    frameToCanvasMatrix =
            ImageUtils.getTransformationMatrix(
                    frameWidth,
                    frameHeight,
                    (int) (multiplier * (rotated ? frameHeight : frameWidth)),
                    (int) (multiplier * (rotated ? frameWidth : frameHeight)),
                    sensorOrientation,
                    false);

    for (final TrackedRecognition recognition : trackedObjects) {

      Log.v("screenma","trackedRecognition.title "+recognition.title);

      final RectF trackedPos = new RectF(recognition.location);

      getFrameToCanvasMatrix().mapRect(trackedPos);
      boxPaint.setColor(recognition.color);

      float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
      canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);

      String labelString =
              !TextUtils.isEmpty(recognition.title)
                      ? String.format("%s", recognition.title)
                      : String.format("");

      borderedText.drawText(
              canvas, trackedPos.left + cornerSize, trackedPos.top - 60, labelString, boxPaint);

    }
  }

  private void processResults(final List<Recognition> results) {
    final List<Pair<Float, Recognition>> rectsToTrack = new LinkedList<Pair<Float, Recognition>>();

    screenRects.clear();
    final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

    for (final Recognition result : results) {

      if (result.getLocation() == null) {
        continue;
      }

      final RectF detectionFrameRect = new RectF(result.getLocation());

      final RectF detectionScreenRect = new RectF();
      rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);

      Log.v("multiBox",
              "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect);

      screenRects.add(new Pair<Float, RectF>(result.getConfidence(), detectionScreenRect));

      if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
        Log.w("multiBox","Degenerate rectangle! " + detectionFrameRect);
        continue;
      }

      rectsToTrack.add(new Pair<Float, Recognition>(result.getConfidence(), result));
    }

    trackedObjects.clear();
    if (rectsToTrack.isEmpty()) {
      Log.v("multiBox","Nothing to track, aborting.");
      return;
    }

    for (final Pair<Float, Recognition> potential : rectsToTrack) {

      Log.v("qws",potential.second.getTitle());
      final TrackedRecognition trackedRecognition = new TrackedRecognition();
      trackedRecognition.detectionConfidence = potential.first;
      trackedRecognition.location = new RectF(potential.second.getLocation());
      trackedRecognition.title = potential.second.getTitle();
      if (trackedRecognition.title.equalsIgnoreCase("patente")) {
        //color amarillo
        trackedRecognition.color = Color.parseColor("#FFFF00");
      } else {
        //trackedRecognition.color = ContextCompat.getColor(context, R.color.colorPrimary); //without theme);
        trackedRecognition.color = ContextCompat.getColor(context, R.color.colorPrimary); //without theme);
      }
      trackedObjects.add(trackedRecognition);

    }
  }

  private static class TrackedRecognition {
    RectF location;
    float detectionConfidence;
    int color;
    String title;
  }
}
