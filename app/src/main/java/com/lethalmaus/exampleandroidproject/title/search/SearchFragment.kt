package com.lethalmaus.exampleandroidproject.title.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.common.ErrorCode
import com.lethalmaus.exampleandroidproject.common.NetworkError
import com.lethalmaus.exampleandroidproject.common.SuccessResponse
import com.lethalmaus.exampleandroidproject.databinding.SearchFragmentBinding
import com.lethalmaus.exampleandroidproject.repository.SearchResponse
import com.lethalmaus.exampleandroidproject.repository.SearchResult
import com.lethalmaus.exampleandroidproject.title.HIDDEN
import com.lethalmaus.exampleandroidproject.title.TitleManager
import com.lethalmaus.exampleandroidproject.title.TitleViewModel
import com.lethalmaus.exampleandroidproject.title.adapter.SearchAdapter

class SearchFragment : BaseFragment<SearchFragmentBinding>(SearchFragmentBinding::inflate) {

    private val viewModel: TitleViewModel by viewModels()
    private val titleClickListener = initTitleClickListener()
    private lateinit var searchQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSearchResults()
        observeTitleResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.search.doOnTextChanged { text, _, _, count ->
            if (count > 0) {
                binding.loader.visibility = View.VISIBLE
                searchQuery = text.toString()
                viewModel.search(getString(R.string.imdb_api_key), searchQuery)
            }
        }
    }

    private fun observeSearchResults() {
        viewModel.searchLiveData.observe(this, {
            binding.loader.visibility = View.GONE
            when {
                it is SuccessResponse && it.data?.errorMessage?.isNotEmpty() == true ->
                    handleErrorMessage(it.data.errorMessage)
                it is SuccessResponse -> {
                    if (it.data?.expression == searchQuery) {
                        handleSearchResults(it.data)
                    }
                }
                it is NetworkError && it.errorCode == ErrorCode.NO_NETWORK_ERROR.errorCode ->
                    handleErrorMessage(getString(R.string.internet_required))
                else ->
                    handleErrorMessage(getString(R.string.error_fetching_search))
            }
        })
    }

    private fun observeTitleResult() {
        viewModel.titleLiveData.observe(this, {
            binding.loader.visibility = View.GONE
            when {
                it is SuccessResponse && it.data?.errorMessage?.isNotEmpty() == true -> {
                    handleErrorMessage(it.data.errorMessage)
                }
                it is SuccessResponse && it.data?.id != null -> {
                    minimizeKeyboard()
                    navigateToTitle(it.data)
                }
                it is NetworkError && it.errorCode == ErrorCode.NO_NETWORK_ERROR.errorCode ->
                    handleErrorMessage(getString(R.string.internet_required))
                else ->
                    handleErrorMessage(getString(R.string.error_fetching_title))
            }
        })
    }

    private fun handleErrorMessage(errorMessage: String) {
        Toast.makeText(
            requireActivity(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleSearchResults(searchResponse: SearchResponse?) {
        if (searchResponse?.results?.isNotEmpty() == true) {
            TitleManager.getTitles(requireContext(), HIDDEN)?.let { hidden ->
                searchResponse.results.removeAll { it.id in hidden.map { item -> item.id } }
            }
            binding.noSearchResults.visibility = View.GONE
            binding.recyclerView.adapter =
                SearchAdapter(searchResponse.results, titleClickListener)
            binding.recyclerView.adapter?.notifyItemRangeChanged(
                0,
                searchResponse.results.size
            )
        } else {
            binding.noSearchResults.visibility = View.VISIBLE
        }
    }

    private fun initTitleClickListener(): (SearchResult) -> Unit = {
        binding.loader.visibility = View.VISIBLE
        viewModel.getTitle(getString(R.string.imdb_api_key), it.id)
    }
}