package com.lutzed.servoluntario.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lutzed.servoluntario.api.requests.SignInRequest;
import com.lutzed.servoluntario.helpers.Constants;
import com.lutzed.servoluntario.models.User;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by luizfreitas on 17/07/2016.
 */
public class SVApi {

    public static MOApiClient getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest;

                Request.Builder builder = request.newBuilder();
                builder.addHeader("Authorization", "Token token=\"" + Constants.AUTH_TOKEN + "\"");

                newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).addNetworkInterceptor(new StethoInterceptor()).addInterceptor(logging).build();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit.create(MOApiClient.class);
    }

    public interface MOApiClient {

        @GET("/me.json")
        Call<User> getMe();

        @POST("me/sign_in")
        Call<User> signIn(@Body SignInRequest signInRequest);

        @POST("users")
        Call<User> createUser();

        @GET("users")
        Call<User> getUsers();

        @GET("users/{userId}")
        Call<User> getUser(@Path("userId") Long userId);

        @PUT("users/{userId}")
        Call<User> updateUser(@Path("userId") Long userId, @Body User user);

        @DELETE("users/{userId}")
        Call<User> deleteUser(@Path("userId") Long userId);


    }

}