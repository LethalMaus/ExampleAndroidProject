package com.lethalmaus.exampleandroidproject.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception
import java.net.InetAddress
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
        job.cancel()
        super.onCleared()
    }

    fun <T> emitLiveData(liveData: MutableLiveData<T>, value: T) {
        liveData.postValue(value!!)
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddress: InetAddress = InetAddress.getByName("google.com")
            !ipAddress.equals("")
        } catch (e: Exception) {
            false
        }
    }
}