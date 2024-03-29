package com.lethalmaus.exampleandroidproject.imdb

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
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

class TitleFragment : BaseFragment<TitleFragmentBinding>(TitleFragmentBinding::inflate) {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
    private val args: TitleFragmentArgs by navArgs()
    private lateinit var title: TitleResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = args.title as TitleResponse
        binding.title.text = title.title
        binding.description.text = title.description
        binding.rating.text = title.imDbRating
        title.poster?.let {
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

        binding.favourite.speed = 0.5f
        if (TitleManager.getTitles(FAVOURITES)?.any { it.id == title.id} == true) {
            binding.favourite.progress = 1f
        } else {
            binding.favourite.progress = 0.5f
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
                removeFavourite()
            } else {
                addFavourite()
            }
            binding.favourite.playAnimation()
        }
    }

    private fun removeFavourite() {
        binding.favourite.setMinAndMaxFrame(0, 30)
        binding.favourite.progress = 0f
        TitleManager.remove(title, FAVOURITES)
        title.title?.let {
            firebaseAnalytics.logEvent("FAVOURITE_REMOVED") {
                param("TITLE", it)
            }
        }
    }

    private fun addFavourite() {
        binding.favourite.setMinAndMaxFrame(30, 60)
        binding.favourite.progress = 0f
        TitleManager.add(title, FAVOURITES)
        title.title?.let {
            firebaseAnalytics.logEvent("FAVOURITE_ADDED") {
                param("TITLE", it)
            }
        }
    }
}