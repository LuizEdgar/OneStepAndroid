package com.lutzed.servoluntario.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lutzed.servoluntario.api.requests.FacebookSignInRequest;
import com.lutzed.servoluntario.api.requests.SignInRequest;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import retrofit2.http.Query;

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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
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
                })
                .addNetworkInterceptor(new StethoInterceptor()).addInterceptor(logging).build();

        Gson gson = new GsonBuilder().registerTypeAdapter(FeedItem.class, new FeedItemTypeAdapter()).create();

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

        @GET("/me/contacts.json")
        Call<List<Contact>> getMeContacts();

        @GET("/me/locations.json")
        Call<List<Location>> getMeLocations();

        @GET("/me/skills.json")
        Call<List<Skill>> getMeSkills();

        @GET("/me/causes.json")
        Call<List<Cause>> getMeCauses();

        @POST("me/sign_in.json")
        Call<User> signIn(@Body SignInRequest signInRequest);

        @POST("me/facebook_sign_in.json")
        Call<User> signIn(@Body FacebookSignInRequest signInRequest);

        @POST("users.json")
        Call<User> createUser(@Body User user);

        @GET("users.json")
        Call<List<User>> getUsers(@Query("page") int page);

        @GET("causes.json")
        Call<List<Cause>> getCauses(@Query("page") int page);

        @GET("skills.json")
        Call<List<Skill>> getSkills(@Query("page") int page);

        @GET("users/{userId}.json")
        Call<User> getUser(@Path("userId") Long userId);

        @PUT("users/{userId}.json")
        Call<User> updateUser(@Path("userId") Long userId, @Body User user);

        @DELETE("users/{userId}.json")
        Call<User> deleteUser(@Path("userId") Long userId);

        @GET("opportunities/{opportunityId}.json")
        Call<Opportunity> getOpportunity(@Path("opportunityId") Long opportunityId);

        @POST("opportunities.json")
        Call<Opportunity> createOpportunity(@Body Opportunity opportunity);

        @PUT("opportunities/{opportunityId}.json")
        Call<Opportunity> updateOpportunity(@Path("opportunityId") Long opportunityId, @Body Opportunity opportunity);

        @GET("me/feed.json")
        Call<List<FeedItem>> getMeFeed(@Query("page") int page);

        @GET("me/opportunities.json")
        Call<List<FeedItem>> getMeOpportunities(@Query("page") int page);

        @GET("organizations/{organizationId}.json")
        Call<Organization> getOrganization(@Path("organizationId") Long organizationId);

        @GET("volunteers/{volunteerId}.json")
        Call<Volunteer> getVolunteer(@Path("volunteerId") Long volunteerId);

    }

}