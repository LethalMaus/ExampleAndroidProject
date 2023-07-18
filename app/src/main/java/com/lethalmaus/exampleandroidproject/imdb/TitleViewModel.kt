package com.lethalmaus.exampleandroidproject.imdb

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.BR
import com.lethalmaus.exampleandroidproject.common.*
import com.lethalmaus.exampleandroidproject.repository.IMDBService
import com.lethalmaus.exampleandroidproject.repository.SearchResponse
import com.lethalmaus.exampleandroidproject.repository.Title
import com.lethalmaus.exampleandroidproject.repository.TitleResponse
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class TitleViewModel : BaseViewModel() {

    val titleLiveData = MutableLiveData<GenericResponse<TitleResponse?>>()
    val searchLiveData = MutableLiveData<GenericResponse<SearchResponse?>>()
    var titleInterface: TitleInterface? = null

    val items: ObservableList<Any> = ObservableArrayList()
    val itemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(
            Title::class.java
        ) { itemBinding: ItemBinding<*>, _: Int, _: Title ->
            itemBinding.clearExtras()
                .set(BR.item, R.layout.title_item)
                .bindExtra(BR.viewModel, this)
        }

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

    fun navigate(title: Title) {
        titleInterface?.navigate(title)
    }
}