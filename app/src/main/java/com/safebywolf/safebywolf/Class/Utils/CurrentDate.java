package com.safebywolf.safebywolf.Class.Utils;

import com.safebywolf.safebywolf.Class.Utils.Log;

import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Model.PatenteRobadaVista;
import com.safebywolf.safebywolf.Model.TimeOffset;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrentDate implements Serializable {
    private String fecha;
    private String hora;
    private String horaMenosDiezMinutos;
    private String longTimeMenosDiezMinutos;
    private String longTimeMenosXMinutos;
    private String longTime;
    private Date date;
    private Date dateMenosXMinutos;
    private String day;
    private String month;
    private String year;
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

    public CurrentDate(Date date) {

        date.setTime(date.getTime() + TimeOffset.getOffset());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fecha = dateFormat.format(date);
        day = (String) android.text.format.DateFormat.format("dd", date);
        month = (String) android.text.format.DateFormat.format("MM", date);
        year = (String) android.text.format.DateFormat.format("yyyy", date);
        dateFormat = new SimpleDateFormat("HH:mm:ss");

        hora = dateFormat.format(date);
        Log.v("timeoffsetdate",""+fecha+"..."+hora);
        longTime = String.valueOf(date.getTime());

        this.date=date;

        long timeMenos10 = Long.parseLong(getLongTime()) - (10 * ONE_MINUTE_IN_MILLIS);
        long timeMenos30 = Long.parseLong(getLongTime()) - (10 * ONE_MINUTE_IN_MILLIS);
        dateMenosXMinutos = new Date(timeMenos10);
        horaMenosDiezMinutos = dateFormat.format(dateMenosXMinutos);
        longTimeMenosDiezMinutos=String.valueOf(dateMenosXMinutos.getTime());
        //getServerTime();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long longTimeMenosXMinutos(PatenteRobadaVista patenteRobadaVista, int minutos){
        long time =Long.parseLong(patenteRobadaVista.getLongTime())-(minutos*ONE_MINUTE_IN_MILLIS);
        Date date=new Date(time);
        return date.getTime();
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date getTime) {
        this.date = getTime;
    }

    public Date getDateMenosXMinutos() {
        return dateMenosXMinutos;
    }

    public void setDateMenosXMinutos(Date dateMenosXMinutos) {
        this.dateMenosXMinutos = dateMenosXMinutos;
    }

    public CurrentDate() {
        getServerTime();
    }

    public String getLongTimeMenosDiezMinutos() {
        return longTimeMenosDiezMinutos;
    }

    public void setLongTimeMenosDiezMinutos(String longTimeMenosDiezMinutos) {
        this.longTimeMenosDiezMinutos = longTimeMenosDiezMinutos;
    }

    public void getServerTime(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://worldtimeapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call callTime = jsonPlaceHolderApi.getTime();
        callTime.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    Log.v("dattimecs",""+response.code());

                    return;
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body());
                Log.v("dattimecs",jsonString);
                try {
                    JSONObject objeto = new JSONObject(jsonString);
                    //JSONArray data = new JSONArray(jsonString);
                    //JSONObject obj = data.getJSONObject(0);

                    String datetime = objeto.getString("datetime");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date=dateFormat.parse(datetime);
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    fecha=dateFormat.format(date);
                    dateFormat=new SimpleDateFormat("HH:mm:ss");
                    hora=dateFormat.format(date);
                    longTime=String.valueOf(date.getTime());
                    long time =Long.parseLong(getLongTime())-(10*ONE_MINUTE_IN_MILLIS);
                    Date date1=new Date(time);
                    horaMenosDiezMinutos=dateFormat.format(date1);


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("dattimecs","eror al obtener fecha");
                return;
            }
        });
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public String getHoraMenosDiezMinutos() {
        return horaMenosDiezMinutos;
    }

    public void setHoraMenosDiezMinutos(String horaMenosDiezMinutos) {
        this.horaMenosDiezMinutos = horaMenosDiezMinutos;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}