package com.lethalmaus.exampleandroidproject.repository

import android.net.Uri
import com.lethalmaus.exampleandroidproject.common.GenericResponse
import com.lethalmaus.exampleandroidproject.common.createService
import com.lethalmaus.exampleandroidproject.common.mapToGenericResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val API_KEY = "apiKey"

class IMDBService {

    interface IMDBEndpoints {

        @GET(imdbSearch)
        fun search(
            @Path(API_KEY) apiKey: String,
            @Path("searchQuery") searchQuery: String
        ): Call<SearchResponse>

        @GET(imdbTitle)
        fun getTitle(
            @Path(API_KEY) apiKey: String,
            @Path("title") title: String
        ): Call<TitleResponse>
    }

    private val imdbEndpoints: IMDBEndpoints by lazy {
        createService(IMDBEndpoints::class.java, "https://imdb-api.com/")
    }

    suspend fun search(apiKey: String, searchQuery: String): GenericResponse<SearchResponse?> {
        return imdbEndpoints.search(apiKey, Uri.parse(searchQuery).toString()).mapToGenericResponse()
    }

    suspend fun getTitle(apiKey: String, title: String): GenericResponse<TitleResponse?> {
        return imdbEndpoints.getTitle(apiKey, title).mapToGenericResponse()
    }
}