package com.app.procom.models.movies

data class MovieDetails(
    val date: String = "",
    val id: Int = 0,
    val images: List<String> = emptyList(),
    val long_disc: String = "",
    val short_desc: String = "",
    val title: String = ""
)