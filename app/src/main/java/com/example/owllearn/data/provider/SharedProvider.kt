package com.example.owllearn.data.provider

import com.example.gallery.network.entities.ReqBody
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.example.owllearn.ui.network.OwllearnApi

class SharedProvider {
    private val api = OwllearnApi.instance
    suspend fun getDecks(userId: String): List<Deck>? {
        val response = api.getAllDecks(userId)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }

    suspend fun getDeckPreviews(userId: String): List<DeckPreview>? {
        val response = api.getAllDeckPreviews(userId)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }

    suspend fun putDeck(deck: Deck?, deckPreview: DeckPreview?): Deck? {
        if (deck != null && deckPreview != null) {
            val response = api.editDeck(ReqBody(deck, deckPreview))
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            }
        }
        return null
    }

    suspend fun putDeckPreview(deckPreview: DeckPreview?): DeckPreview? {
        if (deckPreview != null) {
            val response = api.editDeckPreview(deckPreview!!)
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            }
        }
        return null
    }

    suspend fun createDeck(deck: Deck?): Deck? {
        if (deck != null) {
            val response = api.createDeck(deck)
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            }
        }
        return null
    }
}