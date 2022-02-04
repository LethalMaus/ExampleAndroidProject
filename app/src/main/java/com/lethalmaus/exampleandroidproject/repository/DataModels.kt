package com.lethalmaus.exampleandroidproject.repository

data class SearchResponse (
    val results: ArrayList<SearchResult>,
    val expression: String,
    val errorMessage: String?
)
data class SearchResult (
    val id: String,
    val image: String,
    val title: String,
    val description: String
)

data class TitleResponse (
    val id: String?,
    val title: String?,
    val image: String?,
    val plot: String?,
    val imDbRating: String?,
    val errorMessage: String?
)