package org.tech.town.gripcompany.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SEARCH")
data class Search(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    @PrimaryKey val imdbID: String,
    var isFavorite: Boolean = false
)