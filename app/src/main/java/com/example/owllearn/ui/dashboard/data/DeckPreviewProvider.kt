package com.example.owllearn.ui.dashboard.data

import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import java.util.*

class DeckPreviewProvider {

    fun getDecks(username: String): List<DeckPreview> {
        return MutableList(4) { DeckPreview("Deck $it", UUID.randomUUID()) }
    }
}