package com.safebywolf.safebywolf.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import com.safebywolf.safebywolf.Class.Utils.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.safebywolf.safebywolf.Activity.NovedadActivity;
import com.safebywolf.safebywolf.Model.RowNovedad;
import com.safebywolf.safebywolf.R;

import java.util.ArrayList;

public class NovedadAdapter extends RecyclerView.Adapter<NovedadAdapter.ViewHolder>{
    Context context;
    private ArrayList<RowNovedad> localDataSet;
    View view;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutNovedad;
        TextView textViewTitulo;
        TextView textViewContenido;
        ImageView imageView;
        Button buttonVerMas;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            linearLayoutNovedad = (LinearLayout) view.findViewById(R.id.linearLayoutNovedad);
            textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
            textViewContenido = (TextView) view.findViewById(R.id.textViewContenido);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            buttonVerMas = (Button) view.findViewById(R.id.buttonVerMas);
        }

        public TextView getTextViewTitulo() {
            return textViewTitulo;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public NovedadAdapter(Context context, ArrayList<RowNovedad> dataSet) {
        context = context;
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NovedadAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.novedad_row_item, viewGroup, false);

        return new NovedadAdapter.ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NovedadAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewTitulo.setText(localDataSet.get(position).getTitulo());

        /**
         * ojo aqui tomar bien el texto del string
         * */
        if(localDataSet.get(position).getContenido() != null) {
            for (int i = 0; i < localDataSet.get(position).getContenido().size(); i++) {
                viewHolder.textViewContenido.setText(viewHolder.textViewContenido.getText() +"\n"+localDataSet.get(position).getContenido().get(i));
            }
        }

        cargarImagen(viewHolder, position);


        viewHolder.linearLayoutNovedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargaContenido(viewHolder);
            }
        });
        viewHolder.buttonVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargaContenido(viewHolder);
            }
        });
        //viewHolder.imageView;

    }

    public void cargaContenido(NovedadAdapter.ViewHolder viewHolder){
        String id = localDataSet.get(viewHolder.getAdapterPosition()).getId();
        Log.v("rowNoticia","click button: "+id);
        Intent intent = new Intent(view.getContext(), NovedadActivity.class);
        intent.putExtra("id",id);
        view.getContext().startActivity(intent);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void cargarImagen(NovedadAdapter.ViewHolder viewHolder, int position){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(view.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.start();

        if(localDataSet.get(position).getImagenes() != null && localDataSet.get(position).getImagenes().size() > 0 && localDataSet.get(position).getImagenes().get(0) != null && !localDataSet.get(position).getImagenes().get(0).equalsIgnoreCase("")) {
            Glide.with(view.getContext())
                    .asBitmap()
                    .load(localDataSet.get(position).getImagenes().get(0))
                    .placeholder(circularProgressDrawable)
                    .fitCenter()
                    .into(viewHolder.imageView);
        }
    }
}
