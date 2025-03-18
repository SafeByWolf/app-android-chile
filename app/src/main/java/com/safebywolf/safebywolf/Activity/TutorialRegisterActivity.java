package com.safebywolf.safebywolf.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safebywolf.safebywolf.Adapter.StepperAdapterRegister;
import com.safebywolf.safebywolf.Class.Referencias;
import com.safebywolf.safebywolf.Class.Utils.Utils;
import com.safebywolf.safebywolf.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class TutorialRegisterActivity extends AppCompatActivity implements StepperLayout.StepperListener {
    StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_register);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayoutRegister);
        mStepperLayout.setAdapter(new StepperAdapterRegister(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        Utils.guardarValorBoolean(TutorialRegisterActivity.this, Referencias.TUTORIALREGISTER,true);
        Intent intent=new Intent(TutorialRegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}