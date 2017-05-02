package com.lutzed.servoluntario.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lutzed.servoluntario.api.requests.FacebookSignInRequest;
import com.lutzed.servoluntario.api.requests.SignInRequest;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.Constants;

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
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by luizfreitas on 17/07/2016.
 */
public class Api {

    public static ApiClient getClient() {
        return getClient(null);
    }

    public static ApiClient getClient(final User user) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest;

                Request.Builder builder = request.newBuilder();
                if (user != null)
                    builder.addHeader("Authorization", "Token token=\"" + user.getAuth() + "\"");

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

        return retrofit.create(ApiClient.class);
    }

    public interface ApiClient {

        @GET("/me.json")
        Call<User> getMe();

        @POST("me/sign_in.json")
        Call<User> signIn(@Body SignInRequest signInRequest);

        @POST("me/facebook_sign_in.json")
        Call<User> signIn(@Body FacebookSignInRequest signInRequest);

        @POST("users.json")
        Call<User> createUser(@Body User user);

        @GET("users.json")
        Call<List<User>> getUsers();

        @GET("causes.json")
        Call<List<Cause>> getCauses();

        @GET("skills.json")
        Call<List<Skill>> getSkills();

        @GET("users/{userId}.json")
        Call<User> getUser(@Path("userId") Long userId);

        @PUT("users/{userId}.json")
        Call<User> updateUser(@Path("userId") Long userId, @Body User user);

        @DELETE("users/{userId}.json")
        Call<User> deleteUser(@Path("userId") Long userId);


    }

}