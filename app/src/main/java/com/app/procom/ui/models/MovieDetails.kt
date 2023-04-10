package com.app.procom.ui.models

data class MovieDetails(
    val date: String = "",
    val id: Int = 0,
    val images: String = "https://picsum.photos/200/300/",
    val longDescription: String = "Body",
    val shortDescription: String = "Title",
    val isBookmarked: Boolean = false
)