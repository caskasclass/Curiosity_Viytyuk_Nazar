package it.uninsubria.curiosityapp.data.network

import it.uninsubria.curiosityapp.utils.UNSPLASH_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientId: String = UNSPLASH_API_KEY
    ): UnsplashResponse
}