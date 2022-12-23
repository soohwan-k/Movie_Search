package org.tech.town.gripcompany.data.api


import org.tech.town.gripcompany.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/")
    suspend fun getSearchResponses(
        @Query(value = "apiKey") apiKey: String,
        @Query(value = "s") s: String,
        @Query(value = "page") page: Int
    ): Response<SearchResponse>
}
