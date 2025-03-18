package com.safebywolf.safebywolf.Class.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.safebywolf.safebywolf.Class.Utils.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReverseGeocoding {
    String pais;
    String region;
    String ciudad;
    String comuna;
    String direccion;
    String countryCode;

    public ReverseGeocoding(double lat, double lng, Context applicationContext){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder=new Geocoder(applicationContext, Locale.getDefault());
        try {
            addresses=geocoder.getFromLocation(lat,lng,1);
            if(addresses.size()==0){
                return;
            }
            Log.v("getcityt","Pais getCountryName: "+addresses.get(0).getCountryName());
            Log.v("getcityt","CL Chile getCountryCode: "+addresses.get(0).getCountryCode());
            Log.v("getcityt","Region getAdminArea: "+addresses.get(0).getAdminArea());
            Log.v("getcityt","Ciudad getSubAdminArea: "+addresses.get(0).getSubAdminArea());
            Log.v("getcityt","Comuna getLocality: "+addresses.get(0).getLocality());
            Log.v("getcityt","Calle getThoroughfare: "+addresses.get(0).getThoroughfare());
            Log.v("getcityt","Número de casa getSubThoroughfare: "+addresses.get(0).getSubThoroughfare());

            pais = addresses.get(0).getCountryName();
            countryCode = addresses.get(0).getCountryCode();
            region = addresses.get(0).getAdminArea();
            ciudad= addresses.get(0).getSubAdminArea();
            comuna = addresses.get(0).getLocality();
            direccion = addresses.get(0).getThoroughfare() + " " + addresses.get(0).getFeatureName();

            if(pais!=null){ pais=pais.replaceAll("null",""); } else{ pais="Desconocido"; }
            if(countryCode!=null){ countryCode=countryCode.replaceAll("null",""); } else{ countryCode="Desconocido"; }
            if(region!=null){ region=region.replaceAll("null",""); } else{ region="Desconocida"; }
            if(ciudad!=null){ ciudad=ciudad.replaceAll("null",""); } else{ ciudad="Desconocida"; }
            if(comuna!=null){ comuna=comuna.replaceAll("null",""); } else{ comuna="Desconocida"; }
            if(direccion!=null){ direccion=direccion.replaceAll("null",""); } else{ direccion="Desconocida"; }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
