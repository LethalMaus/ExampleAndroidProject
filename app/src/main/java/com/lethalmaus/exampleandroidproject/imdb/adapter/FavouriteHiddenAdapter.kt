package com.lethalmaus.exampleandroidproject.imdb.adapter

import com.bumptech.glide.Glide
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.repository.TitleResponse

class FavouriteHiddenAdapter(private val dataSet: List<TitleResponse>?, private val action: ((TitleResponse) -> Unit)) : BaseTitleAdapter() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        dataSet?.get(position)?.let { title ->
            Glide.with(viewHolder.image.context)
                .load(title.image)
                .into(viewHolder.image)
            viewHolder.title.text = title.title
            viewHolder.description.text = title.plot
            viewHolder.itemView.setOnClickListener {
                action.invoke(title)
            }
            if (position == dataSet.size-1) {
                viewHolder.itemView.setBackgroundResource(R.drawable.rv_separator_top_bottom)
            }
        }
    }

    override fun getItemCount() = dataSet?.size?: 0
}