package com.safebywolf.safebywolf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.Adapter.StepperAdapterDetector;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class TutorialDetectorActivity extends AppCompatActivity  implements StepperLayout.StepperListener {
    StepperLayout mStepperLayout;
    boolean fromDetectorActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_detector);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayoutDetector);
        mStepperLayout.setAdapter(new StepperAdapterDetector(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromDetectorActivity = extras.getBoolean("fromDetectorActivity");
        }
    }

    @Override
    public void onCompleted(View completeButton) {
        if(fromDetectorActivity){
            finish();
        } else {
            Utils.guardarValorBoolean(TutorialDetectorActivity.this, Referencias.TUTORIALDETECTOR,true);
            Intent intent=new Intent(TutorialDetectorActivity.this, DetectorActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onError(VerificationError verificationError) {
        //Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        //Toast.makeText(this, "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        finish();
    }
}