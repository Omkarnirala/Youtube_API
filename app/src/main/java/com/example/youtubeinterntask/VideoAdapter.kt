package com.example.youtubeinterntask

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(private val listener: ItemClicked) : RecyclerView.Adapter<ViewHolder>() {

    private val items: ArrayList<Video> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item,parent,false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.title.text = currentItem.Title
        holder.description.text = currentItem.description
        Glide.with(holder.itemView.context)
            .load(currentItem.imgURL)
            .centerCrop()
            .fitCenter()
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(updatedNews: ArrayList<Video>){
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.titleView)
    val description: TextView = itemView.findViewById(R.id.descriptionView)
    val thumbnail: ImageView =itemView.findViewById(R.id.videoThumb)

}

interface ItemClicked {
    fun onItemClicked(item: Video)
}