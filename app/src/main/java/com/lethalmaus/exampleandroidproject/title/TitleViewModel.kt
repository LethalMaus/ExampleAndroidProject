package com.lethalmaus.exampleandroidproject.title

import androidx.lifecycle.MutableLiveData
import com.lethalmaus.exampleandroidproject.common.*
import com.lethalmaus.exampleandroidproject.repository.IMDBService
import com.lethalmaus.exampleandroidproject.repository.SearchResponse
import com.lethalmaus.exampleandroidproject.repository.TitleResponse
import kotlinx.coroutines.launch

class TitleViewModel : BaseViewModel() {

    val titleLiveData = MutableLiveData<GenericResponse<TitleResponse?>>()
    val searchLiveData = MutableLiveData<GenericResponse<SearchResponse?>>()

    fun getTitle(apiKey: String, title: String) = launch {
        if (isInternetAvailable()) {
            val response = IMDBService().getTitle(apiKey, title)
            emitLiveData(titleLiveData, response)
        } else {
            emitLiveData(titleLiveData, NetworkError(ErrorCode.NO_NETWORK_ERROR.errorCode))
        }
    }

    fun search(apiKey: String, searchQuery: String) = launch {
        if (isInternetAvailable()) {
            val response = IMDBService().search(apiKey, searchQuery)
            emitLiveData(searchLiveData, response)
        } else {
            emitLiveData(searchLiveData, NetworkError(ErrorCode.NO_NETWORK_ERROR.errorCode))
        }
    }
}