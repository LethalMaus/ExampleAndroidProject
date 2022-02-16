package com.lethalmaus.exampleandroidproject

import android.app.Application

class MainApplication : Application() {

    internal companion object {
        lateinit var instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
fun getAppContext(): MainApplication = MainApplication.instance