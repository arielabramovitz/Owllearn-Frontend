package com.example.owllearn.ui.dashboard.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeckPreview(
    val deckName: String,
    val unmarked: Int,
    val easy: Int,
    val medium: Int,
    val hard: Int,
    val deckId: String,
    val userId: String
)
