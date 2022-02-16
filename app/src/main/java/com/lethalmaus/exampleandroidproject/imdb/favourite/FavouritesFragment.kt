package com.lethalmaus.exampleandroidproject.imdb.favourite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.databinding.FavouritesFragmentBinding
import com.lethalmaus.exampleandroidproject.imdb.FAVOURITES
import com.lethalmaus.exampleandroidproject.imdb.TitleManager
import com.lethalmaus.exampleandroidproject.imdb.adapter.FavouriteHiddenAdapter
import com.lethalmaus.exampleandroidproject.imdb.search.HiddenTitlesFragment
import com.lethalmaus.exampleandroidproject.imdb.search.SearchFragment

class FavouritesFragment : BaseFragment<FavouritesFragmentBinding>(FavouritesFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frameContainer, SearchFragment(), SearchFragment::class.java.simpleName)
                .addToBackStack(SearchFragment::class.java.simpleName)
                .commit()
        }
        binding.hidden.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frameContainer, HiddenTitlesFragment(), HiddenTitlesFragment::class.java.simpleName)
                .addToBackStack(HiddenTitlesFragment::class.java.simpleName)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val favourites = TitleManager.getTitles(FAVOURITES)
        if (favourites?.isNotEmpty() == true) {
            binding.noFavourites.visibility = View.GONE
            binding.recyclerView.adapter =
                FavouriteHiddenAdapter(favourites) { navigateToTitle(it) }
            binding.recyclerView.adapter?.notifyItemRangeChanged(
                0,
                favourites.size
            )
        } else {
            binding.noFavourites.visibility = View.VISIBLE
        }
    }
}