package org.tech.town.gripcompany.data.repository


import org.tech.town.gripcompany.data.model.SearchResponse
import org.tech.town.gripcompany.data.retrofit.RetrofitInstance
import retrofit2.Response


class MovieRepository {

    suspend fun getSearchResponses(apiKey: String, s: String, page: Int): Response<SearchResponse> {
        return RetrofitInstance.movieApi.getSearchResponses(apiKey, s, page)
    }
}