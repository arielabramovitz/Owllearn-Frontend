package com.example.owllearn.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.example.owllearn.data.provider.SharedProvider
import com.example.owllearn.ui.decks.data.provider.CardsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SharedViewModel : ViewModel() {
    private val provider = SharedProvider()
    private val _currDeck = MutableLiveData<Deck>()
    private val _decks = MutableLiveData<List<Deck>>()
    private val _previews = MutableLiveData<List<DeckPreview>>()

    val currDeck: LiveData<Deck> = _currDeck
    val decks: LiveData<List<Deck>> = _decks
    val previews: LiveData<List<DeckPreview>> = _previews


    // maps cardId to Card
    private val cardsMap: MutableMap<String, Card> = mutableMapOf()

    // maps deckId to Deck

    private val decksMap: MutableMap<String, Deck> = mutableMapOf()

    // maps deckId to DeckPreview
    private val decksPreviewMap: MutableMap<String, DeckPreview> = mutableMapOf()

    fun getDeck(deckId: String): Deck? {
        return decksMap[deckId]
    }

    fun getCard(cardId: String): Card? {
        return cardsMap[cardId]
    }

    fun reloadDecks(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = provider.getDecks(userId)
            if (loaded != null) {
                _decks.postValue(loaded!!)
                decksMap.clear()
                loaded.forEach { decksMap[it.deckId] = it }
            }

        }
    }

    fun reloadDeckPreviews(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val loaded = provider.getDeckPreviews(userId)
            if (loaded != null) {
                _previews.postValue(loaded!!)
                decksPreviewMap.clear()
                loaded.forEach { decksPreviewMap[it.deckId] = it }
            }
        }
    }

    fun reloadCards(deckId: String) {
        val deck = decksMap[deckId]
        if (deck != null) {
            _currDeck.postValue(deck!!)
            val cards = deck.cards
            cards.forEach { cardsMap[it.cardId] = it }

        }

    }

    fun addCardToDeck(card: Card, deckId: String) {
        val deck = decksMap[deckId]
        val deckPreview = decksPreviewMap[deckId]
        if (deck != null && deckPreview != null) {
            deck.cards.add(card)
            deckPreview.unmarked += 1

        }
    }

    fun createCard(deckId: String, cardId: String, front: String, back: String) {
        val card = Card(cardId, deckId, front, back)
        val deck = decksMap[deckId]
        val deckPreview = decksPreviewMap[deckId]
        if (deck != null && deckPreview != null) {
            deck.cards.add(card)
            deckPreview.unmarked += 1

        }
    }

    fun editCard(deckId: String, cardId: String, front: String, back: String) {
        val card = cardsMap[cardId]
        val deck = decksMap[deckId]
        val deckPreview = decksPreviewMap[deckId]
        if (card != null && deck != null && deckPreview != null) {
            when (card.ranking) {
                "unmarked" -> deckPreview.unmarked -= 1
                "easy" -> deckPreview.easy -= 1
                "medium" -> deckPreview.medium -= 1
                "hard" -> deckPreview.hard -= 1
            }
            deckPreview.unmarked += 1

            card.ranking = "unmarked"
            card.front = front
            card.back = back

        }
    }

    fun deleteDeck(deckId: String) {
        val deck = decksMap[deckId]
        if (deck != null) {
            val d = decks.value!!.toMutableList()
            d.remove(deck)
            _decks.postValue(d)
        }
    }

    fun deleteCard(cardId: String) {
        val card = cardsMap[cardId]
    }


    // when the save button is clicked in deck edit fragment, we call this (assumes the deck is locally edited
    fun uploadDeck(deckId: String) {
        viewModelScope.launch {
            val deck = decksMap[deckId]
            val deckPreview = decksPreviewMap[deckId]
            provider.putDeck(deck, deckPreview)
            provider.putDeckPreview(deckPreview)
        }
    }

    fun createDeck(userId: String, deckId: String, deckName: String) {
        val deck = Deck(deckName, deckId, userId)
        val d = _decks.value?.toMutableList()
        d?.add(deck)
        viewModelScope.launch(Dispatchers.IO) {
            provider.createDeck(deck)
        }

    }


}