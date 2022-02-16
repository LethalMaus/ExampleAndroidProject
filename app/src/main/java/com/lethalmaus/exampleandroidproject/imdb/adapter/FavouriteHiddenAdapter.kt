package com.lethalmaus.exampleandroidproject.imdb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.repository.TitleResponse

class FavouriteHiddenAdapter(private val dataSet: List<TitleResponse>?, private val action: ((TitleResponse) -> Unit)) : RecyclerView.Adapter<FavouriteHiddenAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.title_item, viewGroup, false)
        return ViewHolder(view)
    }

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