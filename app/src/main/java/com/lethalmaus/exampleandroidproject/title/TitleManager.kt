package com.lethalmaus.exampleandroidproject.title

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lethalmaus.exampleandroidproject.repository.TitleResponse
import java.lang.reflect.Type

const val FAVOURITES = "favourites"
const val HIDDEN = "hidden"

object TitleManager {

    private var favourites: SharedPreferences? = null
    private var hidden: SharedPreferences? = null
    private var gson = Gson()

    private fun loadSharedPrefs(context: Context?, prefId: String) {
        if (prefId == FAVOURITES && favourites == null) {
            favourites = context?.getSharedPreferences(FAVOURITES, Context.MODE_PRIVATE)
        } else if (prefId == HIDDEN && hidden == null) {
            hidden = context?.getSharedPreferences(HIDDEN, Context.MODE_PRIVATE)
        }
    }

    fun add(context: Context?, title: TitleResponse, prefId: String) {
        loadSharedPrefs(context, prefId)
        val titles = getTitles(context, prefId) ?: ArrayList()
        titles.add(title)
        save(titles, prefId)
    }

    fun remove(context: Context?, title: TitleResponse, prefId: String) {
        loadSharedPrefs(context, prefId)
        val titles = getTitles(context, prefId)!!
        titles.remove(title)
        save(titles, prefId)
    }

    fun getTitles(context: Context?, prefId: String): ArrayList<TitleResponse>? {
        loadSharedPrefs(context, prefId)
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