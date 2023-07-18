package com.lethalmaus.exampleandroidproject.imdb.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.common.ErrorCode
import com.lethalmaus.exampleandroidproject.common.NetworkError
import com.lethalmaus.exampleandroidproject.common.SuccessResponse
import com.lethalmaus.exampleandroidproject.databinding.SearchFragmentBinding
import com.lethalmaus.exampleandroidproject.repository.SearchResponse
import com.lethalmaus.exampleandroidproject.imdb.HIDDEN
import com.lethalmaus.exampleandroidproject.imdb.TitleInterface
import com.lethalmaus.exampleandroidproject.imdb.TitleManager
import com.lethalmaus.exampleandroidproject.imdb.TitleViewModel
import com.lethalmaus.exampleandroidproject.repository.Title

class SearchFragment : BaseFragment<SearchFragmentBinding>(SearchFragmentBinding::inflate), TitleInterface {

    private val viewModel: TitleViewModel by viewModels()
    private lateinit var searchQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSearchResults()
        observeTitleResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.toFavourites())
        }

        binding.search.doOnTextChanged { text, _, _, count ->
            if (count > 0) {
                binding.loader.visibility = View.VISIBLE
                searchQuery = text.toString()
                viewModel.search(searchQuery)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.titleInterface = this
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.titleInterface = null
    }

    private fun observeSearchResults() {
        viewModel.searchLiveData.observe(this) {
            binding.loader.visibility = View.GONE
            when {
                it is SuccessResponse && it.data?.error?.isNotEmpty() == true ->
                    handleErrorMessage(it.data.error)

                it is SuccessResponse -> {
                    handleSearchResults(it.data)
                }

                it is NetworkError && it.errorCode == ErrorCode.NO_NETWORK_ERROR.errorCode ->
                    handleErrorMessage(getString(R.string.internet_required))

                else ->
                    handleErrorMessage(getString(R.string.error_fetching_search))
            }
        }
    }

    private fun observeTitleResult() {
        viewModel.titleLiveData.observe(this) {
            binding.loader.visibility = View.GONE
            when {
                it is SuccessResponse && it.data?.errorMessage?.isNotEmpty() == true -> {
                    handleErrorMessage(it.data.errorMessage)
                }

                it is SuccessResponse && it.data?.id != null -> {
                    minimizeKeyboard()
                    findNavController().navigate(SearchFragmentDirections.toTitle(it.data))
                }

                it is NetworkError && it.errorCode == ErrorCode.NO_NETWORK_ERROR.errorCode ->
                    handleErrorMessage(getString(R.string.internet_required))

                else ->
                    handleErrorMessage(getString(R.string.error_fetching_title))
            }
        }
    }

    private fun handleErrorMessage(errorMessage: String) {
        Toast.makeText(
            requireActivity(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleSearchResults(searchResponse: SearchResponse?) {
        viewModel.items.clear()
        if (searchResponse?.search?.isNotEmpty() == true) {
            TitleManager.getTitles(HIDDEN)?.let { hidden ->
                searchResponse.search.removeAll { it.id in hidden.map { item -> item.id } }
            }
            viewModel.items.addAll(searchResponse.search)
        }
    }

    override fun navigate(title: Title) {
        binding.loader.visibility = View.VISIBLE
        viewModel.getTitle(title.id)
    }
}