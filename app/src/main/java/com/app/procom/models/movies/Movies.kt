package com.app.procom.models.movies

data class Movies(
    val next: Next,
    val results: List<MovieDetails>
)