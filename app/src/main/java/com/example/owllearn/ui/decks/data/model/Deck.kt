package com.example.owllearn.ui.decks.data.model

import java.util.*

data class Deck(
    val deckName: String,
    val deckId: String,
    val cards: List<Card>
)
