package com.lethalmaus.exampleandroidproject.repository

import android.net.Uri
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.GenericResponse
import com.lethalmaus.exampleandroidproject.common.createService
import com.lethalmaus.exampleandroidproject.common.mapToGenericResponse
import com.lethalmaus.exampleandroidproject.getAppContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val API_KEY = "apiKey"

class IMDBService {

    interface IMDBEndpoints {

        @GET(imdbSearch)
        fun search(
            @Path("searchQuery") searchQuery: String,
            @Path(API_KEY) apiKey: String = getAppContext().getString(R.string.imdb_api_key)
        ): Call<SearchResponse>

        @GET(imdbTitle)
        fun getTitle(
            @Path("title") title: String,
            @Path(API_KEY) apiKey: String = getAppContext().getString(R.string.imdb_api_key)
        ): Call<TitleResponse>
    }

    private val imdbEndpoints: IMDBEndpoints by lazy {
        createService(IMDBEndpoints::class.java, getAppContext().getString(R.string.imdb_base_url))
    }

    suspend fun search(searchQuery: String): GenericResponse<SearchResponse?> {
        return imdbEndpoints.search(Uri.parse(searchQuery).toString()).mapToGenericResponse()
    }

    suspend fun getTitle(title: String): GenericResponse<TitleResponse?> {
        return imdbEndpoints.getTitle(title).mapToGenericResponse()
    }
}