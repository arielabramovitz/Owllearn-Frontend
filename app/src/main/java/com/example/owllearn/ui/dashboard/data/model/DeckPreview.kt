package com.example.owllearn.ui.dashboard.data.model

import java.util.UUID

data class DeckPreview(
    val deckName: String,
    val notMarked: Int,
    val easyMarked: Int,
    val mediumMarked: Int,
    val hardMarked: Int,
    val deckId: String
)
