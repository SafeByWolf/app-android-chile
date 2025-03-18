package com.safebywolf.safebywolf.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.safebywolf.safebywolf.Model.PatenteEscaneada;
import com.safebywolf.safebywolf.R;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterPatentesEscaneadas extends BaseAdapter implements Serializable {
    private Context context;
    private ArrayList<PatenteEscaneada> patenteEscaneadas;
    public AdapterPatentesEscaneadas(Context context, ArrayList<PatenteEscaneada> patenteEscaneadas) {
        this.context = context;
        this.patenteEscaneadas = patenteEscaneadas;
    }

    @Override
    public int getCount() {
        return this.patenteEscaneadas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.patenteEscaneadas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.row_adapter_patente_escaneada,null);
        }
        final TextView textViewPatente=convertView.findViewById(R.id.textViewPatente);
        final TextView textViewCantidad=convertView.findViewById(R.id.textViewCantidad);
        final TextView textViewRobado=convertView.findViewById(R.id.textViewRobado);
        final TextView textViewTiempo=convertView.findViewById(R.id.textViewTiempo);
        final TextView textViewBounds=convertView.findViewById(R.id.textViewBounds);
        textViewPatente.setText(patenteEscaneadas.get(position).getPatente());
        textViewCantidad.setText(String.valueOf(patenteEscaneadas.get(position).getCantidad()));
        textViewTiempo.setText(String.valueOf(patenteEscaneadas.get(position).getTiempoTotal()));
        textViewBounds.setText(String.valueOf(patenteEscaneadas.get(position).getBound()));
        return convertView;
    }
}
