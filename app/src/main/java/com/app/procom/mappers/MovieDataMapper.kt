package com.app.procom.mappers

import com.app.procom.models.movies.MovieDetails as RemoteMovies
import com.app.procom.storage.movies.MovieDetails as LocalDbMovies
import com.app.procom.ui.models.MovieDetails as UiModelMovies

class MovieDataMapper {
    fun map(remoteData: RemoteMovies): LocalDbMovies {
        return LocalDbMovies(
            movieId = remoteData.id,
            imageUrls = remoteData.images,
            longDescription = remoteData.long_disc,
            shortDescription = remoteData.short_desc,
            title = remoteData.title,
            createdDate = remoteData.date
        )
    }

    fun map(localDbMovies: LocalDbMovies): UiModelMovies {
        return UiModelMovies(
            date = localDbMovies.createdDate,
            id = localDbMovies.movieId,
            images = localDbMovies.imageUrls.toString(),
            longDescription = localDbMovies.longDescription,
            shortDescription = localDbMovies.shortDescription,
            isBookmarked = localDbMovies.isFavorite != 0
        )
    }
}