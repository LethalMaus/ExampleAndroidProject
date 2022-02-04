package com.lethalmaus.exampleandroidproject.title.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lethalmaus.exampleandroidproject.common.BaseFragment
import com.lethalmaus.exampleandroidproject.databinding.HiddenTitlesFragmentBinding
import com.lethalmaus.exampleandroidproject.title.HIDDEN
import com.lethalmaus.exampleandroidproject.title.TitleManager
import com.lethalmaus.exampleandroidproject.title.adapter.FavouriteHiddenAdapter

class HiddenTitlesFragment : BaseFragment<HiddenTitlesFragmentBinding>(HiddenTitlesFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val hidden = TitleManager.getTitles(requireContext(), HIDDEN)
        if (hidden?.isNotEmpty() == true) {
            binding.noneHidden.visibility = View.GONE
            binding.recyclerView.adapter =
                FavouriteHiddenAdapter(hidden) { navigateToTitle(it) }
            binding.recyclerView.adapter?.notifyItemRangeChanged(
                0,
                hidden.size
            )
        } else {
            binding.noneHidden.visibility = View.VISIBLE
        }
    }
}