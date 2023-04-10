package com.app.procom.storage.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.procom.storage.movies.converters.ListStringConverter

@TypeConverters(ListStringConverter::class)
@Entity
data class MovieDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "movie_id")
    val movieId: Int = 0,

    @ColumnInfo(name = "image_urls")
    val imageUrls: List<String> = emptyList(),

    @ColumnInfo(name = "long_desc")
    val longDescription: String = "",

    @ColumnInfo(name = "short_desc")
    val shortDescription: String = "",

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Int = 0,

    @ColumnInfo(name = "date")
    val createdDate: String = ""
)