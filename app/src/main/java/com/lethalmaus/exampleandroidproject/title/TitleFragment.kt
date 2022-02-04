package com.lethalmaus.exampleandroidproject.title

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.databinding.TitleFragmentBinding
import com.lethalmaus.exampleandroidproject.repository.TitleResponse

class TitleFragment(private val title: TitleResponse) : BaseFragment<TitleFragmentBinding>(TitleFragmentBinding::inflate) {

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

        if (TitleManager.getTitles(requireContext(), HIDDEN)?.any { it.id == title.id} == true) {
            binding.hide.setImageResource(R.drawable.show)
        }
        setHideClickListener()

        if (TitleManager.getTitles(requireContext(), FAVOURITES)?.any { it.id == title.id} == true) {
            binding.favourite.setImageResource(R.drawable.unfavour)
        }
        setFavouriteClickListener()
    }

    private fun setHideClickListener() {
        binding.hide.setOnClickListener {
            if (TitleManager.getTitles(requireContext(), HIDDEN)?.any { it.id == title.id } == true) {
                binding.hide.setImageResource(R.drawable.hide)
                TitleManager.remove(requireContext(), title, HIDDEN)
            } else {
                binding.hide.setImageResource(R.drawable.show)
                TitleManager.add(requireContext(), title, HIDDEN)
            }
        }
    }

    private fun setFavouriteClickListener() {
        binding.favourite.setOnClickListener {
            if (TitleManager.getTitles(requireContext(), FAVOURITES)?.any { it.id == title.id } == true) {
                binding.favourite.setImageResource(R.drawable.favour)
                TitleManager.remove(requireContext(), title, FAVOURITES)
            } else {
                binding.favourite.setImageResource(R.drawable.unfavour)
                TitleManager.add(requireContext(), title, FAVOURITES)
            }
        }
    }
}