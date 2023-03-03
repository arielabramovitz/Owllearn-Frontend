package com.example.owllearn.ui.dashboard.data.provider

import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import java.util.*

class DeckPreviewProvider {
    // TODO: Replace mock provider with GET
    fun getDecks(userId: String?): List<DeckPreview> {
        return MutableList(4) { DeckPreview(
            "Deck Number $it",
            1,
            1,
            1,
            1,
            UUID.randomUUID().toString()) }
    }
}