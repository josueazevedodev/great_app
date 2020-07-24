package com.appgreat.beerapp.Core;

import com.appgreat.beerapp.Model.Beer;
import com.appgreat.beerapp.Services.BeerService;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.punkapi.com/v2/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }

    public BeerService getBeerService() {
        return this.retrofit.create(BeerService.class);
    }
}
