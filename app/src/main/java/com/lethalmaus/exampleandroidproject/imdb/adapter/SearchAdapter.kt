package com.lethalmaus.exampleandroidproject.imdb.adapter

import com.bumptech.glide.Glide
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.repository.SearchResult

class SearchAdapter(private val dataSet: List<SearchResult>?, private val action: ((SearchResult) -> Unit)) : BaseTitleAdapter() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        dataSet?.get(position)?.let { searchResult ->
            Glide.with(viewHolder.image.context)
                .load(searchResult.image)
                .into(viewHolder.image)
            viewHolder.title.text = searchResult.title
            viewHolder.description.text = searchResult.description
            viewHolder.itemView.setOnClickListener {
                action.invoke(searchResult)
            }
            if (position == dataSet.size-1) {
                viewHolder.itemView.setBackgroundResource(R.drawable.rv_separator_top_bottom)
            }
        }
    }

    override fun getItemCount() = dataSet?.size?: 0
}