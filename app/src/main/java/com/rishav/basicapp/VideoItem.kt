package com.rishav.basicapp

import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val channelName: String,
    val date: String,
    val id: Int,
    val likes: String,
    val thumbnailUrl: String,
    val videoTitle: String,
    val videoUrl: String
)