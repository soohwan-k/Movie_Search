package org.tech.town.gripcompany.data.model

data class SearchResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)
