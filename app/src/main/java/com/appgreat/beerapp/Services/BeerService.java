package com.appgreat.beerapp.Services;

import com.appgreat.beerapp.Model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BeerService {

    @GET("beers")
    Call<List<Beer>> getBeerPage(@Query("page") int page, @Query("per_page") int qtd);

    @GET("beers/{id}")
    Call<Beer> getBeerId(@Path("id") int id);

    @GET("beers")
    Call<List<Beer>> getBeerName(@Query("beer_name") String name);

}
