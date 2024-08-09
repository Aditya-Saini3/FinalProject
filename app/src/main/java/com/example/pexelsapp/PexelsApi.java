package com.example.pexelsapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PexelsApi {
    @Headers("Authorization: Z5NMTniFfkCrx2asMVNADPkXBZVUnMHL2qCwLcYPA17n57CYOujcipnT")
    @GET("curated")
    Call<PexelsResponse> getCuratedPhotos(
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @Headers("Authorization: Z5NMTniFfkCrx2asMVNADPkXBZVUnMHL2qCwLcYPA17n57CYOujcipnT")
    @GET("search")
    Call<PexelsResponse> searchPhotos(
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
