package com.safebywolf.safebywolf.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.fragment.app.FragmentManager;

import com.safebywolf.safebywolf.Fragment.StepFragmentTutorialDetector;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapterDetector extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "position";

    public StepperAdapterDetector(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final StepFragmentTutorialDetector step = new StepFragmentTutorialDetector();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        step.setArguments(b);
        return (Step) step;
    }

    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context)
                .setTitle("titulo 1");
        switch (position) {
            case 0:
                builder
                        .setSubtitle("titulo 1")
                        .setEndButtonLabel("Siguiente")
                        .setBackButtonLabel("Anterior")
                        .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE);
                break;
            case 1:
                builder
                        .setSubtitle("titulo 2")
                        .setEndButtonLabel("Siguiente")
                        .setBackButtonLabel("Anterior");
                        //.setBackButtonStartDrawableResId(R.drawable.ms_back_arrow);
                break;
            case 2:
                builder
                        .setSubtitle("titulo 3")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 3:
                builder
                        .setSubtitle("titulo 4")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 4:
                builder
                        .setSubtitle("titulo 5")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 5:
                builder
                        .setSubtitle("titulo 6")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 6:
                builder
                        .setSubtitle("titulo 7")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 7:
                builder
                        .setSubtitle("titulo 8")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 8:
                builder
                        .setSubtitle("titulo 9")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 9:
                builder
                        .setSubtitle("titulo 10")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Siguiente");
                break;
            case 10:
                builder
                        .setSubtitle("titulo 11")
                        .setBackButtonLabel("Anterior")
                        .setEndButtonLabel("Finalizar");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }

    @Override
    public int getCount() {
        return 11;
    }
}

