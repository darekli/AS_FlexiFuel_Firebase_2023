package com.example.as_flexifuel_firebase_2023.nbp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NbpApiService {
    @GET("exchangerates/rates/{table}/{currency}/{startDate}/")
    Call<Rate> getExchangeRate(
            @Path("table") String table,
            @Path("currency") String currency,
            @Path("startDate") String startDate
           // @Path("endDate") String endDate
    );


}

