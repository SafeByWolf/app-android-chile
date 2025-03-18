package com.safebywolf.safebywolf.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.safebywolf.safebywolf.Model.RowPatenteEscaneada;
import com.safebywolf.safebywolf.R;

import java.util.ArrayList;

public class PatenteEscaneadaAdapter extends RecyclerView.Adapter<PatenteEscaneadaAdapter.ViewHolder>{
    private ArrayList<RowPatenteEscaneada> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPatenteEscaneadaRowItem;
        ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            super(view);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayoutPatenteEscaneadaRowItem);
            textViewPatenteEscaneadaRowItem = (TextView) view.findViewById(R.id.textViewPatenteEscaneadaRowItem);
        }

        public TextView getTextViewPatenteEscaneadaRowItem() {
            return textViewPatenteEscaneadaRowItem;
        }

        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }
    }

    public PatenteEscaneadaAdapter(ArrayList<RowPatenteEscaneada> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.patentes_escaneadas_row_item, viewGroup, false);
        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        /*
        viewHolder.constraintLayout.animate().alpha(0).translationY(-50).withEndAction(new Runnable() {
            @Override
            public void run() {
                viewHolder.constraintLayout.animate().alpha(0.5f).translationY(50).setDuration(400).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.constraintLayout.animate().alpha(1.0f).translationY(50).setDuration(400);
                    }
                });
            }
        });
         */
        setFadeAnimation(viewHolder.getTextViewPatenteEscaneadaRowItem());
        viewHolder.getTextViewPatenteEscaneadaRowItem().setText(localDataSet.get(position).getPatente());
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
