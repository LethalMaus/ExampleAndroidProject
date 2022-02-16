package com.lethalmaus.exampleandroidproject.imdb

import androidx.lifecycle.MutableLiveData
import com.lethalmaus.exampleandroidproject.common.*
import com.lethalmaus.exampleandroidproject.repository.IMDBService
import com.lethalmaus.exampleandroidproject.repository.SearchResponse
import com.lethalmaus.exampleandroidproject.repository.TitleResponse
import kotlinx.coroutines.launch

class TitleViewModel : BaseViewModel() {

    val titleLiveData = MutableLiveData<GenericResponse<TitleResponse?>>()
    val searchLiveData = MutableLiveData<GenericResponse<SearchResponse?>>()

    fun getTitle(title: String) = launch {
        if (isInternetAvailable()) {
            val response = IMDBService().getTitle(title)
            emitLiveData(titleLiveData, response)
        } else {
            emitLiveData(titleLiveData, NetworkError(ErrorCode.NO_NETWORK_ERROR.errorCode))
        }
    }

    fun search(searchQuery: String) = launch {
        if (isInternetAvailable()) {
            val response = IMDBService().search(searchQuery)
            emitLiveData(searchLiveData, response)
        } else {
            emitLiveData(searchLiveData, NetworkError(ErrorCode.NO_NETWORK_ERROR.errorCode))
        }
    }
}