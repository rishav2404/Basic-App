package com.rishav.basicapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso


class VideoAdapter(private val context: Context, private var videoArrayList: List<VideoItem>):
RecyclerView.Adapter<VideoAdapter.VideoViewHolder>(){


    private lateinit var myListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClicking(position: Int)
    }

    fun setOnItemClickListener( listener: onItemClickListener){
        myListener = listener
    }

    fun updateList(newList: List<VideoItem>) {
        videoArrayList = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): VideoAdapter.VideoViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_video_item, parent, false)
        return VideoViewHolder(itemView , myListener)
    }


    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
        val currentVideoItem = videoArrayList[position]

        holder.videoTitle.text = currentVideoItem.videoTitle
        holder.channelName.text = currentVideoItem.channelName
        holder.likes.text = currentVideoItem.likes
        holder.tvDateofUpload.text = currentVideoItem.date

        Picasso.get().load(currentVideoItem.thumbnailUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return videoArrayList.size
    }

    class VideoViewHolder(itemView: View , listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var videoTitle : TextView = itemView.findViewById(R.id.tvVideotTitle)
        var channelName : TextView = itemView.findViewById(R.id.tvChannelName)
        var likes : TextView = itemView.findViewById(R.id.tvLikes)
        var tvDateofUpload : TextView = itemView.findViewById(R.id.tvDateofUpload)
        var image : ShapeableImageView = itemView.findViewById(R.id.videoThumbnailImg)

        init {
            itemView.setOnClickListener {
                listener.onItemClicking(adapterPosition)
            }
        }
    }
}