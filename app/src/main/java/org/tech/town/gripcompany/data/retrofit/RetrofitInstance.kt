package org.tech.town.gripcompany.data.retrofit

import org.tech.town.gripcompany.Constants.Companion.BASE_URL
import org.tech.town.gripcompany.data.api.MovieApi
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }
}
