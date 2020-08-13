package com.example.endcom.Retrofit;

import com.example.endcom.modelo.ApiCityBik;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("ecobici")
    Call<ApiCityBik> getListCityBik();
}
