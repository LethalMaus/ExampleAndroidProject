package com.lethalmaus.exampleandroidproject.imdb.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.databinding.FavouritesFragmentBinding
import com.lethalmaus.exampleandroidproject.imdb.FAVOURITES
import com.lethalmaus.exampleandroidproject.imdb.TitleInterface
import com.lethalmaus.exampleandroidproject.imdb.TitleManager
import com.lethalmaus.exampleandroidproject.imdb.TitleViewModel
import com.lethalmaus.exampleandroidproject.repository.Title

class FavouritesFragment : BaseFragment<FavouritesFragmentBinding>(FavouritesFragmentBinding::inflate), TitleInterface {

    private val viewModel: TitleViewModel by viewModels()

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
        binding.search.setOnClickListener {
            findNavController().navigate(FavouritesFragmentDirections.toSearch())
        }
        binding.hidden.setOnClickListener {
            findNavController().navigate(FavouritesFragmentDirections.toHiddenTitles())
        }
        viewModel.items.clear()
        TitleManager.getTitles(FAVOURITES)?.let { viewModel.items.addAll(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.titleInterface = this
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.titleInterface = null
    }

    override fun navigate(title: Title) {
        findNavController().navigate(FavouritesFragmentDirections.toTitle(title))
    }
}