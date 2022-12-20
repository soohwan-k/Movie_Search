package org.tech.town.gripcompany.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.JsonElement
import org.tech.town.gripcompany.data.api.MovieApi
import org.tech.town.gripcompany.data.model.SearchResponse
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    //싱글턴 적용 되도록 object
    companion object {
        val instance = RetrofitManager()
    }

    //레트로핏 인터페이스 가져오기
    private val movieApi: MovieApi? =
        RetrofitClient.getClient("https://www.omdbapi.com")?.create(MovieApi::class.java)

    //
    fun searchMovies(apiKey: String?, s: String?, completion: (RESPONSE_STATE, String) -> Unit) {
        //unwrapping
        val apiKey1 = apiKey ?: ""
        val s1 = s ?: ""

        val call = movieApi?.getSearchResponses(apiKey = apiKey1, s = s1).let {
            it
        }?: return
        
        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "onResponse: called /response: ${response.raw()}")
                completion(RESPONSE_STATE.OKAY, response.raw().toString())

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "onFailure: called")
                completion(RESPONSE_STATE.FAIL, t.toString())
            }



        })
    }

    enum class RESPONSE_STATE{
        OKAY,
        FAIL
    }
}