package com.lethalmaus.exampleandroidproject.imdb

import com.lethalmaus.exampleandroidproject.repository.Title

fun interface TitleInterface {
    fun navigate(title: Title)
}