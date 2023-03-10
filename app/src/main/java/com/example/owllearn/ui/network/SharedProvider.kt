package com.example.owllearn.ui.network

import com.example.gallery.network.entities.ReqBodyDeck
import com.example.gallery.network.entities.ReqBodyPreview
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview

public class SharedProvider {
    private val api = OwllearnApi.instance
    suspend fun getDecks(userId: String): List<Deck>? {
        val response = api.getAllDecks(userId)
        println("getDecksCalled")
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }

    suspend fun getDeckPreviews(userId: String): List<DeckPreview>? {
        val response = api.getAllDeckPreviews(userId)
        println("getDeckPreviewsCalled")

        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }

    suspend fun putDeck(deck: Deck?, deckPreview: DeckPreview?): Deck? {
        if (deck != null && deckPreview != null) {
            val response = api.editDeck(ReqBodyDeck(deck, deckPreview))
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            }
        }
        return null
    }

    suspend fun putDeckPreview(cards: List<Card>?, deckPreview: DeckPreview?): DeckPreview? {
        if (deckPreview != null) {
            val response = api.editDeckPreview(ReqBodyPreview(deckPreview, cards))
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

    suspend fun deleteDeck(deck: Deck?) {
        if (deck != null) {
            val response = api.deleteDeck(deck.userId, deck.deckId)

        }
    }

    }