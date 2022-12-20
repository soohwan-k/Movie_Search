package org.tech.town.gripcompany.data.api

import com.google.gson.JsonElement
import org.tech.town.gripcompany.data.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/")
    fun getSearchResponses(
        @Query(value = "apiKey") apiKey: String,
        @Query(value = "s") s: String,
    ): Call<SearchResponse>

}