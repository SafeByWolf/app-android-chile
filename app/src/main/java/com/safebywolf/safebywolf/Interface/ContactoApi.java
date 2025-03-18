package com.safebywolf.safebywolf.Interface;

import com.safebywolf.safebywolf.Model.EmailHttp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ContactoApi {

    @Headers({"Content-Type:application/json"})
    @POST("/contactoAPP")
    Call<EmailHttp> enviarCorreo(@Body EmailHttp emailHttp);
}
