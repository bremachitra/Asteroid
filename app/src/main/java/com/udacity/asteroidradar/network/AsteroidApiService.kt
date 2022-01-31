package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date")
        startDate: String,
        @Query("end_date")
        endDate: String,
        @Query("api_key")
        key: String = API_KEY
    )
            : Any


    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key")
        key: String = API_KEY
    )
            : Any


    object AsteroidsApi {
        val retrofitService: AsteroidApiService by lazy {
            retrofit.create(AsteroidApiService::class.java)
        }
    }
}