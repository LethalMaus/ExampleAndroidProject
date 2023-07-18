package com.lethalmaus.exampleandroidproject.repository

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse (
    @SerializedName("Search") val search: ArrayList<SearchResult>,
    @SerializedName("Error") val error: String?
)

sealed class Title: Serializable {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val poster: String
}

data class SearchResult (
    @SerializedName("imdbID") override val id: String,
    @SerializedName("Poster") override val poster: String,
    @SerializedName("Title") override val title: String,
    @SerializedName("Year") override val description: String
): Title()

data class TitleResponse (
    @SerializedName("imdbID") override val id: String,
    @SerializedName("Title") override val title: String,
    @SerializedName("Poster") override val poster: String,
    @SerializedName("Plot") override val description: String,
    @SerializedName("imdbRating") val imDbRating: String?,
    @SerializedName("Error") val errorMessage: String?,
    @SerializedName("Year") val year: String?
): Title()