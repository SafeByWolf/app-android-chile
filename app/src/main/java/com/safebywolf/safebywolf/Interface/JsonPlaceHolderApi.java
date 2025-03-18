package com.safebywolf.safebywolf.Interface;

import com.safebywolf.safebywolf.Model.Posts;
import com.safebywolf.safebywolf.Model.Sender;
import com.safebywolf.safebywolf.Model.SenderPatente;
import com.safebywolf.safebywolf.Model.SenderRut;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface JsonPlaceHolderApi {
    @GET("posts")
    Call<List<Posts>> getPosts();

    @FormUrlEncoded
    @POST("wolf/agregar_usuario")
    Call<ResponseBody> agregarUsuario(@Field("id") String id,
                                      @Field("nombre") String nombre,
                                      @Field("apellido") String apellido,
                                      @Field("email") String email,
                                      @Field("rol") String rol,
                                      @Field("pass") String pass,
                                      @Field("patente") String patente);
    @FormUrlEncoded
    @POST("/loginApp")
    Call<Object> loginApp(@Field("username") String username,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("wolf/enviar_imagen")
    Call<Object> enviarImagen(@Field("id") String id,
                              @Field("longitud") String longitud,
                              @Field("latitud") String latitud,
                              @Field("img") String img
    );

    @GET("/data/reverse-geocode-client")
    Call<Object> getComunaResults(@QueryMap Map<String, String> options);

    @GET("/getDate")
    Call<Object> getTime();

    @GET("/appGlobals")
    Call<Object> getGlobals();

    @Headers({"Content-Type:application/json","Autorization:key=AAAAdRY9hrI:APA91bFUbwveN-VwVp8-p4MzkgnqNP9vcrmYBygApzQ4nGAWpOC6k-tRiMyteeLtvXF4vbBqbMotP0hkABvqJLDgXnvA0mSBqePs0EoYueo2vAPjdrvyy0QgjrpBXDQvumnNivZAi8pA"})
    @POST("fcm/send")
    Call<Object> enviarNotificacionAutoRobado(@Body Sender body);

    @Headers({"Content-Type:application/json"})
    @POST("/webscrapingRutPersona")
    Call<Object> postRut(@Body SenderRut body);

    @Headers({"Content-Type:application/json"})
    @POST("/webscrapingPatenteChile")
    Call<Object> postPatente(@Body SenderPatente body);

}
