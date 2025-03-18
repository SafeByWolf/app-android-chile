package com.safebywolf.safebywolf.Interface;

import com.google.gson.JsonObject;
import com.safebywolf.safebywolf.Model.PatenteEscaneada;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ImagenPatenteApi {
    @Headers({"Content-type:application/json"})
    @POST("actualizarImagen")
    Call<PatenteEscaneada> enviarImagenApi(@Body JsonObject imagenPatenteApi);
}
