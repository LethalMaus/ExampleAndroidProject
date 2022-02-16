package com.lethalmaus.exampleandroidproject.imdb

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.databinding.TitleFragmentBinding
import com.lethalmaus.exampleandroidproject.repository.TitleResponse

class TitleFragment(private val title: TitleResponse) : BaseFragment<TitleFragmentBinding>(TitleFragmentBinding::inflate) {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = title.title
        binding.description.text = title.plot
        binding.rating.text = title.imDbRating
        title.image?.let {
            Glide.with(requireContext())
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image)
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        if (TitleManager.getTitles(HIDDEN)?.any { it.id == title.id} == true) {
            binding.hide.setImageResource(R.drawable.show)
        }
        setHideClickListener()

        if (TitleManager.getTitles(FAVOURITES)?.any { it.id == title.id} == true) {
            binding.favourite.setImageResource(R.drawable.unfavour)
        }
        setFavouriteClickListener()
    }

    private fun setHideClickListener() {
        binding.hide.setOnClickListener {
            if (TitleManager.getTitles(HIDDEN)?.any { it.id == title.id } == true) {
                binding.hide.setImageResource(R.drawable.hide)
                TitleManager.remove(title, HIDDEN)
            } else {
                binding.hide.setImageResource(R.drawable.show)
                TitleManager.add(title, HIDDEN)
            }
        }
    }

    private fun setFavouriteClickListener() {
        binding.favourite.setOnClickListener {
            if (TitleManager.getTitles(FAVOURITES)?.any { it.id == title.id } == true) {
                binding.favourite.setImageResource(R.drawable.favour)
                TitleManager.remove(title, FAVOURITES)
                title.title?.let {
                    firebaseAnalytics.logEvent("FAVOURITE_REMOVED") {
                        param("TITLE", it)
                    }
                }
            } else {
                binding.favourite.setImageResource(R.drawable.unfavour)
                TitleManager.add(title, FAVOURITES)
                title.title?.let {
                    firebaseAnalytics.logEvent("FAVOURITE_ADDED") {
                        param("TITLE", it)
                    }
                }
            }
        }
    }
}