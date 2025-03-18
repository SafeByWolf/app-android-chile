package com.safebywolf.safebywolf.Interface;

import com.safebywolf.safebywolf.Model.EncuestaPatrullero;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EncuestaApi {

    @Headers({"Content-Type:application/json"})
    @POST("encuestaPatrullero")
    Call<EncuestaPatrullero> enviarEncuesta(@Body RequestBody encuestaPatrullero);

    @Headers({"Content-Type:application/json"})
    @POST("encuestaPatrullero/getEncuestasNoFinalizadas")
    Call<List<EncuestaPatrullero>> getEncuestasNoFinalizadas(@Body Map<String, String> requestBody);
}


