package com.lethalmaus.exampleandroidproject.imdb

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lethalmaus.exampleandroidproject.getAppContext
import com.lethalmaus.exampleandroidproject.repository.TitleResponse
import java.lang.reflect.Type

const val FAVOURITES = "favourites"
const val HIDDEN = "hidden"

object TitleManager {

    private var favourites: SharedPreferences = getAppContext().getSharedPreferences(FAVOURITES, Context.MODE_PRIVATE)
    private var hidden: SharedPreferences? = getAppContext().getSharedPreferences(HIDDEN, Context.MODE_PRIVATE)
    private var gson = Gson()

    fun add(title: TitleResponse, prefId: String) {
        val titles = getTitles(prefId) ?: ArrayList()
        titles.add(title)
        save(titles, prefId)
    }

    fun remove(title: TitleResponse, prefId: String) {
        val titles = getTitles(prefId)!!
        titles.remove(title)
        save(titles, prefId)
    }

    fun getTitles(prefId: String): ArrayList<TitleResponse>? {
        val titlesString = getSharedPreference(prefId)?.getString(prefId, "")
        val type: Type = object : TypeToken<List<TitleResponse?>?>(){}.type
        return gson.fromJson(titlesString, type)
    }

    private fun save(titles: ArrayList<TitleResponse>, prefId: String) {
        with (getSharedPreference(prefId)?.edit()) {
            this?.putString(prefId, gson.toJson(titles))
            this?.commit()
        }
    }

    private fun getSharedPreference(prefId: String): SharedPreferences? {
        return if (prefId == FAVOURITES) favourites else hidden
    }
}