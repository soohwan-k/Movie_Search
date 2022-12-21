package org.tech.town.gripcompany.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.tech.town.gripcompany.data.model.SearchResponse
import org.tech.town.gripcompany.data.repository.MovieRepository
import retrofit2.Response

class MainViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val searchResponse: MutableLiveData<Response<SearchResponse>> = MutableLiveData()

    fun getSearchResponse(apiKey: String, s: String, page: Int) {
        viewModelScope.launch {
            val response = movieRepository.getSearchResponses(apiKey, s, page)
            searchResponse.value = response
        }
    }
}