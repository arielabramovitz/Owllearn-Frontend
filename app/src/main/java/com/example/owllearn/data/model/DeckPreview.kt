package com.example.owllearn.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeckPreview(
    val deckName: String,
    var unmarked: Int,
    var easy: Int,
    var medium: Int,
    var hard: Int,
    val deckId: String,
    val userId: String
)
