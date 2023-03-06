package com.example.owllearn.ui.decks.data.model

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Deck(
    val deckName: String,
    val deckId: String,
    val userId: String,
    val cards: List<Card>
)
