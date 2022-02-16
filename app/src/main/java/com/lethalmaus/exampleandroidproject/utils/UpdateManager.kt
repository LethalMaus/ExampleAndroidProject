package com.lethalmaus.exampleandroidproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.lethalmaus.exampleandroidproject.getAppContext

object UpdateManager {

    private const val UPDATE_CANCELLED = "update_cancelled"
    private val sharedPreferences: SharedPreferences = getAppContext().getSharedPreferences("update", Context.MODE_PRIVATE)

    fun getUpdateLastCancelled(): Long {
        return sharedPreferences.getLong(UPDATE_CANCELLED, 0)
    }

    fun setUpdateCancelled(timestamp: Long) {
        with (sharedPreferences.edit()) {
            this?.putLong(UPDATE_CANCELLED, timestamp)
            this?.commit()
        }
    }
}